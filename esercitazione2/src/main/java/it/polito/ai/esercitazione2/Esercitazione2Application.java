package it.polito.ai.esercitazione2;

import it.polito.ai.esercitazione2.dtos.CourseDTO;
import it.polito.ai.esercitazione2.dtos.StudentDTO;
import it.polito.ai.esercitazione2.repositories.CourseRepository;
import it.polito.ai.esercitazione2.repositories.StudentRepository;
import it.polito.ai.esercitazione2.services.CourseNotFoundException;
import it.polito.ai.esercitazione2.services.StudentNotFoundException;
import it.polito.ai.esercitazione2.services.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SpringBootApplication
public class Esercitazione2Application {
    
    @Bean
    public CommandLineRunner runner(CourseRepository repoC, StudentRepository repoS, TeamService service){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                repo.findAll().stream().forEach(c -> System.out.println(c));
//                repoC.findAll().forEach(System.out::println);
//                repoS.findAll().forEach(System.out::println);

                service.addStudentToCourse("s257009","ApplicazioniInternet");
                service.enableCourse("ApplicazioniInternet");
                service.enableCourse("ProgrammazioneDiSistema");
                service.enableCourse("MachineLearning");
                System.out.println("################### TEST ABILITA CORSO NON ESISTENTE ###################");
                try{
                service.enableCourse("corso che non ce");}
                catch (CourseNotFoundException e){
                    System.out.println(e.getMessage());
                }

                service.addStudentToCourse("s257009","ApplicazioniInternet");
                service.addStudentToCourse("s256665","ApplicazioniInternet");

                System.out.println("\n################### TEST STUDENTI ISCRITTI AL CORSO ###################");
                service.getEnrolledStudents("ApplicazioniInternet").forEach(System.out::println);
                service.getEnrolledStudents("ProgrammazioneDiSistema").forEach(System.out::println);

                System.out.println("\n################### TEST STUDENTI ISCRITTI AL CORSO NON ESISTENTE ###################");
                try{
                    service.addStudentToCourse("s257009","ApplicazioniTorinese");
                }catch(CourseNotFoundException e){System.out.println(e.getMessage());}
                try{
                    service.getEnrolledStudents("ApplicazioniCucine").forEach(System.out::println);
                }catch(CourseNotFoundException e){System.out.println(e.getMessage());}

                System.out.println("\n################### TEST STUDENTE NON ESISTENTE ###################");
                try{
                    service.addStudentToCourse("s000000","ApplicazioniInternet");
                }catch(StudentNotFoundException e){System.out.println(e.getMessage());}
                try{
                    service.getStudent("s000000");
                }catch(StudentNotFoundException e){System.out.println(e.getMessage());}

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

                CourseDTO c1= new CourseDTO();
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

                System.out.println("\n################### TEST ENROLL ALL ###################");
                List<String> tmp2 = new ArrayList<>();
                tmp2.add("Studente che non c'è");
                tmp.forEach(s->tmp2.add(s.getId()));

                try{
                service.enrollAll(tmp2,"MachineLearning").forEach(System.out::println);
                }
                catch (StudentNotFoundException | CourseNotFoundException e){
                    System.out.println(e.getMessage());
                }

                tmp2.remove(0);
                try{
                    service.enrollAll(tmp2,"MachineEating").forEach(System.out::println);
                }
                catch (StudentNotFoundException | CourseNotFoundException e){
                    System.out.println(e.getMessage());
                }

                service.enrollAll(tmp2,"MachineLearning").forEach(System.out::println);


            }
        };
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(Esercitazione2Application.class, args);
    }

}
