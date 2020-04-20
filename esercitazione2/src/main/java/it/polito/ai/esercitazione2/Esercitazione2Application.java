package it.polito.ai.esercitazione2;

import it.polito.ai.esercitazione2.dtos.CourseDTO;
import it.polito.ai.esercitazione2.dtos.StudentDTO;
import it.polito.ai.esercitazione2.dtos.TeamDTO;
import it.polito.ai.esercitazione2.repositories.CourseRepository;
import it.polito.ai.esercitazione2.repositories.StudentRepository;
import it.polito.ai.esercitazione2.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Esercitazione2Application {

    private static final String SAMPLE_CSV_FILE_PATH = "example.csv";

    public static void main(String[] args) {
        SpringApplication.run(Esercitazione2Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(CourseRepository repoC, StudentRepository repoS, TeamService service) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                repo.findAll().stream().forEach(c -> System.out.println(c));
//                repoC.findAll().forEach(System.out::println);
//                repoS.findAll().forEach(System.out::println);

                service.addStudentToCourse("s257009", "ApplicazioniInternet");
                service.enableCourse("ApplicazioniInternet");
                service.enableCourse("ProgrammazioneDiSistema");
                service.enableCourse("MachineLearning");
                System.out.println("################### TEST ABILITA CORSO NON ESISTENTE ###################");
                try {
                    service.enableCourse("corso che non ce");
                } catch (CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                service.addStudentToCourse("s257009", "ApplicazioniInternet");
                service.addStudentToCourse("s256665", "ApplicazioniInternet");

                System.out.println("\n################### TEST STUDENTI ISCRITTI AL CORSO ###################");
                service.getEnrolledStudents("ApplicazioniInternet").forEach(System.out::println);
                service.getEnrolledStudents("ProgrammazioneDiSistema").forEach(System.out::println);

                System.out.println("\n################### TEST STUDENTI ISCRITTI AL CORSO NON ESISTENTE ###################");
                try {
                    service.addStudentToCourse("s257009", "ApplicazioniTorinese");
                } catch (CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }
                try {
                    service.getEnrolledStudents("ApplicazioniCucine").forEach(System.out::println);
                } catch (CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST STUDENTE NON ESISTENTE ###################");
                try {
                    service.addStudentToCourse("s000000", "ApplicazioniInternet");
                } catch (StudentNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }
                try {
                    service.getStudent("s000000");
                } catch (StudentNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST CORSO NON ESISTENTE E STUDENTE NON ESISTENTE (GET) ###################");
                System.out.println(service.getCourse("ApplicazioniMilanesi"));
                System.out.println(service.getStudent("s000000"));
                System.out.println(service.getCourse("ApplicazioniInternet"));
                System.out.println(service.getStudent("s257009"));

                StudentDTO s1 = new StudentDTO();
                s1.setFirstName("Giorgio");
                s1.setName("Chiacchio");
                s1.setId("s254332");

                service.addStudent(s1);

                CourseDTO c1 = new CourseDTO();
                c1.setName("Analisi1");
                c1.setMin(1);
                c1.setMax(5);
                c1.setEnable(true);

                service.addCourse(c1);
                service.disableCourse("Analisi1");

                System.out.println("\n################### TEST TUTTI I CORSI ###################");
                service.getAllCourses().forEach(System.out::println);

                System.out.println("\n################### TEST TUTTI GLI STUDENTI ###################");
                service.getAllStudents().forEach(System.out::println);

                System.out.println("\n################### TEST ADD ALL ###################");
                StudentDTO s2 = new StudentDTO();
                s2.setFirstName("Giovanni");
                s2.setName("Mastrolindo");
                s2.setId("s251132");
                StudentDTO s3 = new StudentDTO();
                s3.setFirstName("Jon");
                s3.setName("Snow");
                s3.setId("s250032");
                StudentDTO s4 = new StudentDTO();
                s4.setFirstName("Daenerys");
                s4.setName("Targaryen");
                s4.setId("s253365");

                List<StudentDTO> tmp = new ArrayList<>();
                tmp.add(s2);
                tmp.add(s3);
                tmp.add(s4);
                service.addAll(tmp).forEach(System.out::println);


                List<String> tmp2 = new ArrayList<>();
                tmp2.add("Studente che non c'è");
                tmp.forEach(s -> tmp2.add(s.getId()));

                try {
                    service.enrollAll(tmp2, "MachineLearning").forEach(System.out::println);
                } catch (StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                tmp2.remove(0);
                try {
                    service.enrollAll(tmp2, "MachineEating").forEach(System.out::println);
                } catch (StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                service.enrollAll(tmp2, "MachineLearning").forEach(System.out::println);

                System.out.println("\n################### TEST ADD AND ENROLL ALL (CSV) ###################");
                System.out.println(Paths.get(SAMPLE_CSV_FILE_PATH).toAbsolutePath());
                Reader reader;

                try {
                    reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                    System.out.println("\n1.Corso esistente e attivo");
                    service.addAndEnroll(reader, "MachineLearning").forEach(System.out::println);
                } catch (IOException | StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                try {
                    System.out.println("\n2.Corso non esistente");
                    reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                    service.addAndEnroll(reader, "MachineFarming").forEach(System.out::println);

                } catch (IOException | StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                try {
                    System.out.println("\n3.Corso esistente e non attivo");
                    reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                    service.disableCourse("ProgrammazioneDiSistema");
                    service.addAndEnroll(reader, "ProgrammazioneDiSistema").forEach(System.out::println);

                } catch (IOException | StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET COURSES ###################");
                System.out.println("\n1.Studente esistente ");
                service.getCourses("s256665").forEach(System.out::println);
                System.out.println("\n2.Studente non esistente ");
                try {
                    service.getCourses("s000000");

                } catch (StudentNotFoundException | CourseNotFoundException e) {
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST PROPOSE TEAM ###################");

                System.out.println("\n1.Corso non esistente ");
                try{
                service.proposeTeam("MachineShinig","CodeOfGames",tmp2);
                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Vincoli non rispettati ");
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",new ArrayList<>());
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n3.Studenti duplicati ");
                tmp2.add(s1.getId());
                tmp2.add(s1.getId());
                service.addStudentToCourse("s254332","MachineLearning");
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n4.Studente non esistente ");
                tmp2.remove(tmp2.size()-1);
                tmp2.add("s000000");
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n5.Studente non iscritto al corso ");
                tmp2.remove(tmp2.size()-1);

                StudentDTO s5 = new StudentDTO();
                s5.setFirstName("Jamie");
                s5.setName("Lannister");
                s5.setId("s103499");
                service.addStudent(s5);

                tmp2.add(s5.getId());
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n6.Corso disabilitato ");
                tmp2.remove(tmp2.size()-1);
                service.disableCourse("MachineLearning");
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n7.Tutti i vincoli soddisfatti ");
                service.enableCourse("MachineLearning");
                try{
                    TeamDTO test = service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                    System.out.println(
                            "ID: "+test.getId()+" "
                                    +"COURSE: "+test.getCourse().getName()+" "
                                    +"GROUP NAME: "+test.getName()+" "
                                    +"STATUS: "+test.getStatus()
                    );
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n8.Studente gia in un gruppo");
                tmp2.remove(0);
                service.disableCourse("MachineLearning");
                try{
                    service.proposeTeam("MachineLearning","CodeOfGames",tmp2);
                }catch (TeamServiceException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET TEAM FOR STUDENT ###################");
                System.out.println("\n1.Studente non esistente");
                try{
                    service.getTeamsForStudent("s000000");
                }catch (StudentNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Studente esistente");
                try{
                    List<TeamDTO> team = service.getTeamsForStudent("s251132");
                    if (team.isEmpty()) {
                        System.out.println("Studente non appartenente a nessun gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getName()));
                    }

                }catch (StudentNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n3.Studente senza gruppo");
                try{
                    List<TeamDTO> team = service.getTeamsForStudent("s103499");
                    if (team.isEmpty()) {
                        System.out.println("Non appartiene a nessun gruppo");
                    } else {
                        team.forEach(s->System.out.println(s.getName()));
                    }

                }catch (StudentNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET MEMBERS ###################");
                System.out.println("\n1.Team non esistente");
                try{
                    List<StudentDTO> team = service.getMembers(355L);
                    team.forEach(s->System.out.println(s.getId()));

                }catch (TeamNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Team esistente");
                try{
                    List<StudentDTO> team = service.getMembers(10L);
                    team.forEach(s->System.out.println(s.getId()));

                }catch (TeamNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET COURSE TEAMS ###################");
                System.out.println("\n1.Corso non esistente");
                try{
                    List<TeamDTO> team = service.getTeamForCourse("Apicoltura");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessun gruppo");
                    } else {
                        team.forEach(s->System.out.println(s.getName()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Corso esistente");
                try{
                    List<TeamDTO> team = service.getTeamForCourse("MachineLearning");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessun gruppo");
                    } else {
                        team.forEach(s->System.out.println(s.getName()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n3.Corso senza gruppi");
                try{
                    List<TeamDTO> team = service.getTeamForCourse("ProgrammazioneDiSistema");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessun gruppo");
                    } else {
                        team.forEach(s->System.out.println(s.getName()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET STUDENTS IN TEAMS ###################");
                System.out.println("\n1.Corso non esistente");
                try{
                    List<StudentDTO> team = service.getStudentsInTeams("Apicoltura");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessuno studente iscritto ad un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Corso esistente");
                try{
                    List<StudentDTO> team = service.getStudentsInTeams("MachineLearning");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessuno studente iscritto ad un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n3.Corso senza gruppi");
                try{
                    List<StudentDTO> team = service.getStudentsInTeams("ProgrammazioneDiSistema");
                    if (team.isEmpty()) {
                        System.out.println("Non c'e nessuno studente iscritto ad un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n################### TEST GET  AVAIABLE STUDENTS ###################");
                System.out.println("\n1.Corso non esistente");
                try{
                    List<StudentDTO> team = service.getAvailableStudents("Apicoltura");
                    if (team.isEmpty()) {
                        System.out.println("Tutti gli studenti fanno parte di un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n2.Corso esistente");
                try{
                    List<StudentDTO> team = service.getAvailableStudents("MachineLearning");
                    if (team.isEmpty()) {
                        System.out.println("Tutti gli studenti fanno parte di un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

                System.out.println("\n3.Corso senza studenti disponibili");
                try{
                    List<StudentDTO> team = service.getAvailableStudents("Analisi1");
                    if (team.isEmpty()) {
                        System.out.println("Tutti gli studenti fanno parte di un gruppo!");
                    } else {
                        team.forEach(s->System.out.println(s.getId()));
                    }

                }catch (CourseNotFoundException e){
                    System.out.println("Eccezione : " + e.getMessage());
                }

            }
        };
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
