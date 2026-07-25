import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { provideStore, provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';

import { routes } from './app.routes';

import { authInterceptor } from './interceptors/auth-interceptor';
import { errorHandlerInterceptor } from './interceptors/error-handler-interceptor';
import { loadingInterceptor } from './interceptors/loading-interceptor';

import { courseReducer } from './store/course/course.reducer';
import { CourseEffects } from './store/course/course.effects';

import { enrollmentReducer } from './store/enrollment/enrollment.reducer';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        loadingInterceptor,
        errorHandlerInterceptor
      ])
    ),

    provideStore({}),

    provideState({
      name: 'course',
      reducer: courseReducer
    }),

    provideState({
      name: 'enrollment',
      reducer: enrollmentReducer
    }),

    provideEffects([CourseEffects]),

    provideStoreDevtools({
      maxAge: 25
    })
  ]
};