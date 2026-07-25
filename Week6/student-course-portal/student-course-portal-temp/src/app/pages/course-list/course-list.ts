import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';

import { EnrollmentService, Student } from '../../services/enrollment';
import { ShortNamePipe } from '../../pipes/short-name-pipe';
import { CourseCard } from '../../components/course-card/course-card';

import { loadCourses } from '../../store/course/course.actions';
import { selectAllCourses } from '../../store/course/course.selectors';

import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [
    CommonModule,
    ShortNamePipe,
    CourseCard
  ],
  templateUrl: './course-list.html',
  styleUrl: './course-list.css'
})
export class CourseList implements OnInit {

  today = new Date();

  courses: any[] = [];
  students: Student[] = [];
  errorMessage = '';

  constructor(
    private store: Store,
    private enrollmentService: EnrollmentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.store.dispatch(loadCourses());

    this.store.select(selectAllCourses).pipe(

      switchMap((courses) => {

        this.courses = courses;

        if (courses.length > 0) {
          return this.enrollmentService.getStudentsByCourse(courses[0].id);
        }

        return this.enrollmentService.getStudentsByCourse(0);

      })

    ).subscribe({

      next: (students) => {

        this.students = students;

        this.cdr.detectChanges();

      },

      error: (err) => {

        console.error(err);

        this.errorMessage = err.message;

      }

    });

  }

}