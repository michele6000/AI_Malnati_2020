import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-page-not-found-component',
  template: `
    <h1 style="text-align: center ; padding: 40px ; font-style: italic ; font-size: 25px">This page is not yet available!</h1>
  `,
  styles: ['h1 { font-weight: normal; }']
})
export class PageNotFoundComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
