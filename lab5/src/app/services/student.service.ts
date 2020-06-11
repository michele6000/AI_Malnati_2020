import { Injectable } from '@angular/core';
import {Student} from '../student/student.model';
import {from, Observable, of} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {concatMap, map, toArray} from 'rxjs/operators';
import {AuthService} from '../auth/auth.service';

// const API_STUDENTS_URL : string = "http://localhost:3000/students";
// const API_STUDENTS_URL : string = "http://localhost:4200/api/students";    // http con proxy
const API_STUDENTS_URL = 'https://localhost:4200/api/students';     // https con proxy

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  // create a new student
  create(){ }

  // update a student
  update(student: Student){}

  // find a student, unique id given
  findStudent(studentId: string){
    return this.http.get<Student[]>( API_STUDENTS_URL + '?studentId=' + studentId, {headers: this.getAuthHeader()}).pipe(
      map(students => students || [])
    );
  }

  // return the Student collection
  query(): Observable<Student[]>{
    return this.http.get<Student[]>(API_STUDENTS_URL, {headers: this.getAuthHeader()}).pipe(
      map(students => students || [])
    );
  }

  // delete a student
  delete(studentId: string) {
    return this.http.delete(API_STUDENTS_URL + '/' + studentId, {headers: this.getAuthHeader()}).pipe();
  }

  updateEnrolled(students: Student[], courseId: number){
    return from(students).pipe(
      concatMap(student => {
        student.courseId = courseId;  // aggiorno l'id del corso
        return this.http.put<Student>(API_STUDENTS_URL + '/' + student.id, student, {headers: this.getAuthHeader()});
      }),
      toArray()
    );
  }

  listEnrolledStudents(courseId: number): Observable<Student[]>{
    return this.http.get<Student[]>( API_STUDENTS_URL + '?courseId=' + courseId, {headers: this.getAuthHeader()}).pipe(
      map(students => students || [])
    );
  }

  getAuthHeader(): HttpHeaders {
    const header = new HttpHeaders(
      {
        Authorization: 'Bearer ' + this.auth.getToken()
      }
    );
    return header;
  }
}
