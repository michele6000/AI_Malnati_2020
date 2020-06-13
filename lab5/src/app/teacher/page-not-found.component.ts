import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-page-not-found',
  template: '<h1>Page not found</h1>',
  styles: ['*{text-align: center; color:red; padding: 50px;}']
})
export class PageNotFoundComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }
}
