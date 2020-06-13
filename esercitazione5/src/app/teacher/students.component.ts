import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { Student } from '../student/student.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrls: ['./students.component.css']
})
export class StudentsComponent implements OnInit {
  @ViewChild('MatTable')
  table: MatTable<Student>;
  _enrolledStudents: Student[];

  @Input()
  set enrolledStudents(students: Student[]) {
    this._enrolledStudents = students;
    delete this.dataSource;
    this.dataSource = new MatTableDataSource<Student>(this._enrolledStudents);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => (typeof value === 'string' ? value : value.name)),
      map(student =>
        student ? this._filteredOptions(student) : this.options.slice()
      )
    );
  }
  get enrolledStudents() {
    return this._enrolledStudents;
  }
  @Input() options: Student[] = [];
  @Output() addStudentE = new EventEmitter<Student>();
  @Output() deleteStudentE = new EventEmitter<Student[]>();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  columnsToDisplay: string[] = ['select', 'id', 'name', 'firstName', 'group'];
  student: Student;

  myControl = new FormControl();
  filteredOptions: Observable<Student[]>;
  dataSource: MatTableDataSource<Student>;
  selection = new SelectionModel<Student>(true, []);

  constructor() {
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => (typeof value === 'string' ? value : value.name)),
      map(student =>
        student ? this._filteredOptions(student) : this.options.slice()
      )
    );
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<Student>(this.enrolledStudents);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  deleteStudents() {
    if (this.selection.isEmpty()) {
      return;
    }

    this.deleteStudentE.emit(this.selection.selected);
    this.selection.clear();
  }

  addStudent(option: Student) {
    this.student = option;
  }

  masterToggle() {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach(row => this.selection.select(row));
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  checkboxLabel(row?: Student): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${
      row.id + 1
    }`;
  }

  displayOptions(option: Student): string {
    return option
      ? option.name + ' ' + option.firstName + ' (' + option.id + ')'
      : '';
  }

  addStudentToDB() {
    if (!this.dataSource.data.includes(this.student)) {
      this.addStudentE.emit(this.student);
      this.dataSource._updateChangeSubscription(); // TODO:togliere forse
    }
  }

  private _filteredOptions(value: string): Student[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(
      student => student.name.toLowerCase().indexOf(filterValue) === 0
    );
  }
}
