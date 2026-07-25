import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';

import { enrollInCourse, unenrollFromCourse } from '../../store/enrollment/enrollment.actions';
import { selectEnrolledIds } from '../../store/enrollment/enrollment.selectors';

@Component({
  selector: 'app-course-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './course-card.html',
  styleUrl: './course-card.css'
})
export class CourseCard {

  @Input() course: any;

  enrolledIds: number[] = [];

  constructor(private store: Store) {
    this.store.select(selectEnrolledIds).subscribe(ids => {
      this.enrolledIds = ids;
    });
  }

  isEnrolled(): boolean {
    return this.enrolledIds.includes(this.course.id);
  }

  toggleEnrollment(): void {

    if (this.isEnrolled()) {
      this.store.dispatch(
        unenrollFromCourse({
          courseId: this.course.id
        })
      );
    } else {
      this.store.dispatch(
        enrollInCourse({
          courseId: this.course.id
        })
      );
    }

  }

}