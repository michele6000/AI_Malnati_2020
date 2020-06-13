import { Injectable } from '@angular/core';
import { Student } from '../student/student.model';
import { from, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { concatMap, map, toArray } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';

// const URL_API_STUDENTS : string = "http://localhost:3000/students";
// const URL_API_STUDENTS : string = "http://localhost:4200/api/students";    // http con proxy
const URL_API_STUDENTS = 'https://localhost:4200/api/students'; // https con proxy

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  create() {}

  update(student: Student) {}

  find(studentId: string) {
    return this.http
      .get<Student[]>(URL_API_STUDENTS + '?studentId=' + studentId, {
        headers: this.getAuthHeader()
      })
      .pipe(map(students => students || []));
  }

  query(): Observable<Student[]> {
    return this.http
      .get<Student[]>(URL_API_STUDENTS, { headers: this.getAuthHeader() })
      .pipe(map(students => students || []));
  }

  delete(studentId: string) {
    return this.http
      .delete(URL_API_STUDENTS + '/' + studentId, {
        headers: this.getAuthHeader()
      })
      .pipe();
  }

  updateEnrolled(students: Student[], courseId: number) {
    return from(students).pipe(
      concatMap(student => {
        student.courseId = courseId;
        return this.http.put<Student>(
          URL_API_STUDENTS + '/' + student.id,
          student,
          { headers: this.getAuthHeader() }
        );
      }),
      toArray()
    );
  }

  listEnrolledStudents(courseId: number): Observable<Student[]> {
    return this.http
      .get<Student[]>(URL_API_STUDENTS + '?courseId=' + courseId, {
        headers: this.getAuthHeader()
      })
      .pipe(map(students => students || []));
  }

  getAuthHeader(): HttpHeaders {
    return new HttpHeaders({
      Authorization: 'Bearer ' + this.auth.getToken()
    });
  }
}
