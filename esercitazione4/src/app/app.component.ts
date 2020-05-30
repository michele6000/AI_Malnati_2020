import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Student } from './student/student.model';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

const STUDENT_DATA: Student[] = [
  { id: 's25001', group: 'team1' , name: 'Hydrogen', firstName: 'Santini' },
  { id: 's25002', group: 'team2' , name: 'Helium', firstName: 'Salta' },
  { id: 's25003', group: 'team3' , name: 'Lithium', firstName: 'Marchio' },
  { id: 's25004', group: 'team1' , name: 'Beryllium', firstName: 'Greco' },
  { id: 's25005', group: 'team2' , name: 'Boron', firstName: 'Caso' },
  { id: 's25006', group: 'team3' , name: 'Carbon', firstName: 'Bruno' },
  { id: 's25007', group: 'team' , name: 'Nitrogen', firstName: 'Ferrero' },
  { id: 's25008', group: 'team' , name: 'Oxygen', firstName: 'Moretti' },
  { id: 's25009', group: '<none>' , name: 'Fluorine', firstName: 'De Sanctis' },
  { id: 's25010', group: '<none>' , name: 'Neon', firstName: 'Lorenzi' }
];

const options: Student[] = [
  { id: 's299001', group: 'team2' , name: 'New_Hydrogen', firstName: 'New_Santini' },
  { id: 's299002', group: 'team3' , name: 'New_Helium', firstName: 'New_Salta' },
  { id: 's299003', group: 'team5' , name: 'New_Lithium', firstName: 'New_Marchio' },
  { id: 's299004', group: 'team6' , name: 'New_Beryllium', firstName: 'New_Greco' },
  { id: 's299005', group: 'team7' , name: 'New_Boron', firstName: 'New_Caso' },
  { id: 's299006', group: 'team3' , name: 'New_Carbon', firstName: 'New_Bruno' },
  { id: 's299007', group: 'team9' , name: 'New_Nitrogen', firstName: 'New_Ferrero' },
  { id: 's299008', group: 'team7' , name: 'New_Oxygen', firstName: 'New_Moretti' },
  { id: 's299009', group: 'team6' , name: 'New_Fluorine', firstName: 'New_De Sanctis' },
  { id: 's299010', group: 'team0' , name: 'New_Neon', firstName: 'New_Lorenzi' }
];

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'esercitazione4';

  @ViewChild('sidenav')
  sidenav: MatSidenav;

  @ViewChild('MatTable')
  table: MatTable<Student>;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  columnsToDisplay: string[] = ['select', 'id', 'name', 'firstName', 'group'];
  dataSource = new MatTableDataSource<Student>(STUDENT_DATA);
  selection = new SelectionModel<Student>(true, []);
  student: Student;

  myControl = new FormControl();
  filteredOptions: Observable<Student[]>;

  constructor() {
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => (typeof value === 'string' ? value : value.name)),
      map(student =>
        student ? this._filteredOptions(student) : options.slice()
      )
    );
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  deleteStudents() {
    const newDS: Student[] = [];

    this.dataSource.data.forEach(s => {
      if (!this.selection.isSelected(s)) {
        newDS.push(s);
      }
    });

    delete this.dataSource;
    this.dataSource = new MatTableDataSource<Student>(newDS);
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

  toggleForMenuClick() {
    this.sidenav.opened ? this.sidenav.close() : this.sidenav.open();
  }

  displayOptions(option: Student): string {
    return option
      ? option.name + ' ' + option.firstName + ' (' + option.id + ')'
      : '';
  }

  addStudentToDB() {
    if (!this.dataSource.data.includes(this.student)) {
      STUDENT_DATA.push(this.student);
      this.dataSource._updateChangeSubscription();
      const index = options.indexOf(this.student, 0);
      if (index > -1) {
        options.splice(index, 1);
      }
    }
  }

  private _filteredOptions(value: string): Student[] {
    const filterValue = value.toLowerCase();
    return options.filter(
      student => student.name.toLowerCase().indexOf(filterValue) === 0
    );
  }
}
