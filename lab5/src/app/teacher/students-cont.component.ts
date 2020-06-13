import { Component, OnInit } from '@angular/core';
import {StudentService} from '../services/student.service';
import {Student} from '../student/student.model';

@Component({
  selector: 'app-students-cont',
  templateUrl: './students-cont.component.html',
  styleUrls: ['./students-cont.component.css']
})

export class StudentsContComponent implements OnInit {
  options: Student[] = [];
  enrolledStudents: Student[] = [];

  constructor(private studentService: StudentService) { }

  ngOnInit(): void {
    // studenti iscritti al corso (visibili nella tabella)
    this.studentService.listEnrolledStudents(1).subscribe(value => this.enrolledStudents = value);
    this.studentService.query().subscribe(value => this.options = value);   // options
  }

  onDeleteStudents(selected: Student[]){
    this.studentService.updateEnrolled(selected, 0)             // aggiorno il courseID settandolo a 0
      .subscribe( s => {                                           // rifaccio le get per recuperare gli array
        this.studentService.listEnrolledStudents(1).subscribe(value => this.enrolledStudents = value);
        this.studentService.query().subscribe(value => this.options = value);
      });
  }

  onAddStudent(selected: Student){
    this.studentService.updateEnrolled([selected], 1)    // aggiorno il courseID mettendolo a 1
      .subscribe( s => {                                           // rifaccio le get per recuperare gli array
        this.studentService.listEnrolledStudents(1).subscribe(value => this.enrolledStudents = value);
        this.studentService.query().subscribe(value => this.options = value);
      });
  }

}
