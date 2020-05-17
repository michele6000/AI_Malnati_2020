package it.polito.ai.lab3;

import it.polito.ai.lab3.entities.User;
import it.polito.ai.lab3.repositories.CourseRepository;
import it.polito.ai.lab3.repositories.StudentRepository;
import it.polito.ai.lab3.repositories.UserRepository;
import it.polito.ai.lab3.services.NotificationService;
import it.polito.ai.lab3.services.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class Lab3Application {

    private final static String CSV_FILE_PATH = "fileTest.csv";

    public static void main(String[] args) {
        SpringApplication.run(Lab3Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(CourseRepository courseRepo, StudentRepository studentRepo, TeamService service, NotificationService mail, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                //courseRepo.findAll().stream().forEach(c->System.out.println(c));
//                //studentRepo.findAll().stream().forEach(s->System.out.println(s));
//
//                System.out.println("-------------TEST ENABLE / DISABLE COURSE, ADD STUDENT TO COURSE, GET ENROLLED STUDENTS-------------");
//                service.addStudentToCourse("s257009", "ApplicazioniInternet");
//                service.enableCourse("ApplicazioniInternet");
//                service.enableCourse("ProgrammazioneDiSistema");
//                service.enableCourse("MachineLearning");
//                try {
//                    service.disableCourse("NotExistentCourse");
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                service.addStudentToCourse("s257009", "ApplicazioniInternet");
//                service.addStudentToCourse("s256665", "ApplicazioniInternet");
//                service.addStudentToCourse("s257009", "ProgrammazioneDiSistema");
//                service.getEnrolledStudents("ApplicazioniInternet").stream().forEach(s -> System.out.println(s));
//                service.getEnrolledStudents("ProgrammazioneDiSistema").stream().forEach(s -> System.out.println(s));
//
//                try {
//                    service.getEnrolledStudents("Analisi1").stream().forEach(s -> System.out.println(s));
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                service.getEnrolledStudents("MachineLearning").stream().forEach(s -> System.out.println(s));
//
//                try {
//                    service.addStudentToCourse("NotExistentStudent", "ApplicazioniInternet");
//                } catch (StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//                try {
//                    service.addStudentToCourse("NotExistentStudent", "NotExistentCourse");
//                } catch (StudentNotFoundException | CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//                try {
//                    service.addStudentToCourse("s257009", "NotExistentCourse");
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//
//                System.out.println("-------------TEST ADD STUDENT / ADD COURSE, GET ALL STUDENTS / ALL COURSES-------------");
//                StudentDTO s1 = new StudentDTO();
//                s1.setFirstName("Anna");
//                s1.setId("s234567");
//                s1.setName("Agosta");
//                service.addStudent(s1);
//
//                CourseDTO c1 = new CourseDTO();
//                c1.setEnabled(true);
//                c1.setMax(3);
//                c1.setMin(1);
//                c1.setName("Analisi1");
//                service.addCourse(c1);
//                service.disableCourse("Analisi1");
//
//                service.getAllCourses().stream().forEach(System.out::println);
//                service.getAllStudents().stream().forEach(System.out::println);
//
//                System.out.println(service.getStudent("s257009"));
//                System.out.println(service.getCourse("ProgrammazioneDiSistema"));
//                System.out.println(service.getStudent("NotExistentStudent"));
//                System.out.println(service.getCourse("NotExistentCourse"));
//
//                System.out.println("-------------TEST ADD ALL-------------");
//
//                StudentDTO s2 = new StudentDTO();
//                s2.setFirstName("Giovanni");
//                s2.setId("s234568");
//                s2.setName("Caso");
//                List<StudentDTO> studentsToAdd = new ArrayList<>();
//                studentsToAdd.add(s1);
//                studentsToAdd.add(s2);
//                List<Boolean> studentsAdded = service.addAll(studentsToAdd);
//                studentsAdded.forEach(System.out::println);
//
//                System.out.println("-------------TEST ENROLL ALL-------------");
//
//                List<String> studentsToEnroll = new ArrayList<>();
//                studentsToEnroll.add("NotExistentStudent");
//                studentsToEnroll.add("s257009");
//                studentsToEnroll.add("s234568");
//                try {
//                    service.enrollAll(studentsToEnroll, "ApplicazioniInternet").forEach(System.out::println);
//                } catch (StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.enrollAll(studentsToEnroll, "NotExistentCourse").forEach(System.out::println);
//                } catch (CourseNotFoundException | StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                System.out.println("-------------TEST SU FILE CSV-------------");
//
//                System.out.println(Paths.get(CSV_FILE_PATH).toAbsolutePath());
//                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
//                service.addAndEnroll(reader, "MachineLearning").forEach(System.out::println);
//
//                try {
//                    reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
//                    service.addAndEnroll(reader, "NotExistentCourse").forEach(System.out::println);
//                } catch (StudentNotFoundException | CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
//                    service.disableCourse("ProgrammazioneDiSistema");
//                    service.addAndEnroll(reader, "ProgrammazioneDiSistema").forEach(System.out::println);
//
//                } catch (StudentNotFoundException | CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//
//                System.out.println("-------------TEST GET COURSES-------------");
//                service.getCourses("s257009").forEach(System.out::println);
//                try {
//                    service.getCourses("NotExistentStudent").forEach(System.out::println);
//                } catch (StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//                try {
//                    service.getCourses("").forEach(System.out::println);
//                } catch (StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                System.out.println("-------------TEST TEAM PROPOSAL-------------");
//                List<String> students = new ArrayList<>();
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//                students.add("s257009");
//                students.add("s123456");
//                students.add("s123457");
//                students.add("s123458");
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//                students.remove(1);
//                students.remove(2);
//                students.add("s257009");
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//                students.remove("s257009");
//                students.add("NotExistentStudent");
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//                students.remove("NotExistentStudent");
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML", students);
//                    System.out.println("Successful proposal!");
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                students.clear();
//                students.add("s257009");
//                service.enableCourse("Analisi1");
//                try {
//                    service.proposeTeam("Analisi1", "ProjectAI", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.proposeTeam("ProgrammazioneDiSistema", "ProjectPDS", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML2", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//                try {
//                    service.proposeTeam("NotExistentCourse", "ProjectML2", students);
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                students.clear();
//                students.add("s256665");
//                service.enrollAll(students, "MachineLearning");
//                try {
//                    service.proposeTeam("MachineLearning", "ProjectML2", students);
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                System.out.println("-------------TEST GET TEAMS FOR STUDENT-------------");
//
//                try {
//                    service.getTeamsForStudent("NotExistentStudent");
//                } catch (StudentNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//                try {
//                    service.getTeamsForStudent("s256665").forEach(t -> System.out.println(t.getName()));
//                    System.out.println("---Lista vuota---");
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                System.out.println("-------------TEST GET MEMBERS-------------");
//
//                Long teamId = 1567853798798L;
//
//                try {
//                    System.out.println(service.getMembers(teamId).size());
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                System.out.println("-------------TEST GET TEAMS FOR STUDENT / GET MEMBERS FUNZIONANTE-------------");
//
//                service.getTeamsForStudent("s257009").forEach(t -> System.out.println(t.getName()));
//                Long teamId2 = 13L;
//                service.getMembers(teamId2).forEach(m -> System.out.println(m.getId()));
//
//                System.out.println("-------------TEST GET TEAM FOR COURSE-------------");
//
//                try {
//                    service.getTeamForCourse("NotExistentCourse");
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.getTeamForCourse("ApplicazioniInternet").forEach(t -> System.out.println(t.getName()));
//                    System.out.println("---Lista vuota---");
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                service.getTeamForCourse("MachineLearning").forEach(t -> System.out.println(t.getName()));
//
//                System.out.println("-------------GET STUDENTS IN TEAMS-------------");
//                try {
//                    service.getStudentsInTeams("NotExistentCourse");
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.getStudentsInTeams("ApplicazioniInternet").forEach(s -> System.out.println(s.getId()));
//                    System.out.println("---Lista vuota---");
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                service.getStudentsInTeams("MachineLearning").forEach(s -> System.out.println(s.getId()));
//
//                System.out.println("-------------GET AVAILABLE STUDENTS-------------");
//                try {
//                    service.getAvailableStudents("NotExistentCourse");
//                } catch (CourseNotFoundException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                try {
//                    service.getAvailableStudents("ApplicazioniInternet").forEach(s -> System.out.println(s.getId() + " ApplicazioniInternet"));
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                service.getAvailableStudents("MachineLearning").forEach(s -> System.out.println(s.getId() + " MachineLearning"));
//
//                try {
//                    service.getAvailableStudents("Analisi1").forEach(s -> System.out.println(s.getId() + " Analisi1"));
//                    System.out.println("---Lista vuota---");
//                } catch (TeamServiceException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                mail.sendMessage("paola.caso96@gmail.com", "prova", "ciao sono il lab3!");

//                userRepo.save(User.builder().username("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles(Arrays.asList("ROLE_USER","ROLE_ADMIN"))
//                .build());

                System.out.println("ALL USERS:\n");
                userRepo.findAll().forEach(u->System.out.println("User: "+u.toString()));
           }
     };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
