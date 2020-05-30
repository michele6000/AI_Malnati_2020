import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material/sidenav';
import {Student} from './student/student.model';
import {MatTable, MatTableDataSource, MatTableModule} from '@angular/material/table';
import {SelectionModel} from '@angular/cdk/collections';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {map, startWith} from 'rxjs/operators';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

const STUDENTS: Student[] = [
  {id: 's257009', name: 'Caso', firstName: 'Paola', group: 'TeamProva'},
  {id: 's256665', name: 'Michele', firstName: 'Greco', group: 'TeamProva'},
  {id: 's238906', name: 'Bruno', firstName: 'Alberto', group: 'TeamProva'},
];
const OPTIONS: Student[] = [
  {id: 's253309', name: 'Burlacu', firstName: 'Iustin', group: ''},
  {id: 's247948', name: 'Buffo', firstName: 'Matteo', group: ''},
  {id: 's222767', name: 'Massafra', firstName: 'Christian', group: 'TeamProva2'},
  {id: 's236564', name: 'Lamberti', firstName: 'Giovanni', group: ''},
  {id: 's378748', name: 'Agosta', firstName: 'Anna', group: ''}
];

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'lab4';

  @ViewChild('MatTable')
  table: MatTable<Student>;
  @ViewChild('sidenav') sidenav: MatSidenav;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  colsToDisplay: string[] = ['select', 'id', 'firstName', 'name', 'group'];
  students = new MatTableDataSource<Student>(STUDENTS);
  selection = new SelectionModel<Student>(true, []);
  studentCtrl = new FormControl();
  options: Observable<Student[]>;
  studentToAdd: Student;

  constructor() {
    this.options = this.studentCtrl.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value.name),
        map(student => student ? this._filterStudents(student) : OPTIONS.slice())
      );
  }

  ngOnInit() {
    this.students.paginator = this.paginator;
    this.students.sort = this.sort;
  }

  toggleForMenuClick(){
    this.sidenav.opened ? this.sidenav.close() : this.sidenav.open();
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
  checkboxLabel(row?: Student): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }

  deleteSelected(){

    const filteredStudents: Student[] = [];
    this.students.data.forEach(s => {
      if (!this.selection.isSelected(s)) {
          filteredStudents.push(s);
    }});
    delete this.students;
    this.students = new MatTableDataSource<Student>(filteredStudents);
    this.selection.clear();
  }

  private _filterStudents(value: string): Student[] {
    const filterValue = value.toLowerCase();
    return OPTIONS.filter(s => s.name.toLowerCase().indexOf(filterValue) === 0);
  }

  displayOptions(student: Student): string {
    return student ? student.name + ' ' + student.firstName + ' (' + student.id + ')' : '';
  }

  addStudent(student: Student){
    this.studentToAdd = student;
  }

  commitStudent() {
    if (this.studentToAdd != null && !this.students.data.includes(this.studentToAdd)) {
      this.students.data.push(this.studentToAdd);
      const index = OPTIONS.indexOf(this.studentToAdd, 0);
      if (index > -1) {
        OPTIONS.splice(index, 1);
      }
      this.students._updateChangeSubscription();
    }
  }
}
