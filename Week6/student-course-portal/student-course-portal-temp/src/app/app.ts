import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AsyncPipe, NgIf } from '@angular/common';

import { Header } from './components/header/header';
import { LoadingService } from './services/loading';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    Header,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  isLoading$;

  constructor(private loadingService: LoadingService) {
    this.isLoading$ = this.loadingService.isLoading$;
  }

}