import { Component } from '@angular/core';

@Component({
  template: `
  <div class="text-center">
    <h1>{{ 'app.page-not-found.title' | translate }}</h1>
    <button class="btn btn-primary" [routerLink]="['/']">{{ 'app.page-not-found.go-home' | translate }}</button>
  </div>
  `
})
export class PageNotFoundComponent { }