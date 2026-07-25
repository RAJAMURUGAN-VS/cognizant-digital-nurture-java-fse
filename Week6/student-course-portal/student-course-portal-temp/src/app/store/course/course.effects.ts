import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { switchMap, map, catchError, tap } from 'rxjs/operators';

import * as CourseActions from './course.actions';
import { CourseService } from '../../services/course';

@Injectable()
export class CourseEffects {

  private actions$ = inject(Actions);
  private courseService = inject(CourseService);

  loadCourses$ = createEffect(() =>
    this.actions$.pipe(

      ofType(CourseActions.loadCourses),

      tap(() => console.log('NgRx Effect Executed')),

      switchMap(() =>
        this.courseService.getCourses().pipe(

          map(courses =>
            CourseActions.loadCoursesSuccess({ courses })
          ),

          catchError(error =>
            of(
              CourseActions.loadCoursesFailure({
                error: error.message }
              )
            )
          )

        )
      )

    )
  );

}