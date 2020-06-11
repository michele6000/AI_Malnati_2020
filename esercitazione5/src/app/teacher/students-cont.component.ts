import { Component, OnInit } from '@angular/core';
import { Student } from '../student/student.model';
import { StudentService } from '../services/student.service';

@Component({
  selector: 'app-students-cont',
  templateUrl: './students-cont.component.html',
  styleUrls: ['./students-cont.component.css']
})
export class StudentsContComponent implements OnInit {
  STUDENT_DATA: Student[] = [];
  STUDENT_OPTIONS: Student[] = [];


  constructor(private studentService: StudentService) {
  }

  ngOnInit(): void {
    this.studentService.listEnrolledStudents(1).subscribe(value => this.STUDENT_DATA = value);
    this.studentService.query().subscribe(value => this.STUDENT_OPTIONS = value);
  }

  onAddStudent(student: Student){
    this.studentService.updateEnrolled([student], 1)
      .subscribe( s => {
        this.studentService.listEnrolledStudents(1).subscribe(value => this.STUDENT_DATA = value);
        this.studentService.query().subscribe(value => this.STUDENT_OPTIONS = value);
      });
  }

  onDeleteStudent(students: Student[]){
    this.studentService.updateEnrolled(students, 0)
      .subscribe( s => {
        this.studentService.listEnrolledStudents(1).subscribe(value => this.STUDENT_DATA = value);
        this.studentService.query().subscribe(value => this.STUDENT_OPTIONS = value);
      });
  }
}
