import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-vms-cont-component',
  template: `
    <h1 style="text-align: center ; padding: 40px ; font-style: italic ; font-size: 25px">Welcome on VMs management page</h1>
  `,
  styles: ['h1 { font-weight: normal; }']
})
export class VmsContComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
