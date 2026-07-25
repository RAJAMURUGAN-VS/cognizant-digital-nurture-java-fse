import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorHandlerInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  return next(req).pipe(

    catchError((error: HttpErrorResponse) => {

      if (error.status === 401) {
        alert('Unauthorized! Redirecting to Home...');
        router.navigate(['/']);
      }

      if (error.status === 500) {
        alert('Internal Server Error!');
      }

      return throwError(() => error);

    })

  );

};