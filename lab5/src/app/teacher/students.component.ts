import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {Student} from '../student/student.model';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {SelectionModel} from '@angular/cdk/collections';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit {

  @ViewChild('MatTable')
  table: MatTable<Student>;

  @ViewChild
  (MatPaginator, {static: true}) paginator: MatPaginator;

  @ViewChild
  (MatSort, {static: true}) sort: MatSort;

  colsToDisplay: string[] = ['select', 'id', 'firstName', 'name', 'group'];

  _enrolledStudents: Student[];
  selection = new SelectionModel<Student>(true, []);
  studentCtrl = new FormControl();
  optionsObserv: Observable<Student[]>;
  studentToAdd: Student;
  students: MatTableDataSource<Student>;

  @Output()
  addStudentEvent = new EventEmitter<Student>();

  @Output()
  deleteStudentsEvent = new EventEmitter <Student[]>();

  @Input()
  set enrolledStudents(students: Student[]) {
    delete(this.students);
    this._enrolledStudents = students;
    this.students = new MatTableDataSource<Student>(this._enrolledStudents);
    this.students.paginator = this.paginator;
    this.students.sort = this.sort;
  }

  get enrolledStudents() { return this._enrolledStudents; }

  @Input('options')
  options: Student[];

constructor() {
  }

ngOnInit() {
  this.optionsObserv = this.studentCtrl.valueChanges
    .pipe(
      startWith(''),
      map(value => typeof value === 'string' ? value : value.name),
      map(student => student ? this._filterStudents(student) : this.options.slice())
    );
  this.students = new MatTableDataSource<Student>(this.enrolledStudents);
  this.students.paginator = this.paginator;
  this.students.sort = this.sort;
  }


  /** Whether the number of selected elements matches the total number of rows. */
isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.students.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.students.data.forEach(row => this.selection.select(row));
  }

  /** The label for the checkbox on the passed row */
checkboxLabel(row ?: Student): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }

deleteSelected() {
    this.deleteStudentsEvent.emit(this.selection.selected);
    this.selection.clear();
  }

  private _filterStudents(value: string): Student[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(s => s.name.toLowerCase().indexOf(filterValue) === 0);
  }

displayOptions(student: Student): string {
      return student ? student.name + ' ' + student.firstName + ' (' + student.id + ')' : '';
  }

addStudent(student: Student) {
    this.studentToAdd = student;
  }

commitStudent() {
    if (this.studentToAdd != null && !this.students.data.find(s => s.id === this.studentToAdd.id)) {
      this.addStudentEvent.emit(this.studentToAdd);
    }
  }

}
