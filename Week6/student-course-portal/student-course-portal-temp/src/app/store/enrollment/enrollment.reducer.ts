import { createReducer, on } from '@ngrx/store';
import * as EnrollmentActions from './enrollment.actions';

export interface EnrollmentState {
  enrolledCourseIds: number[];
}

export const initialState: EnrollmentState = {
  enrolledCourseIds: []
};

export const enrollmentReducer = createReducer(

  initialState,

  on(EnrollmentActions.enrollInCourse, (state, { courseId }) => ({
    ...state,
    enrolledCourseIds: [...state.enrolledCourseIds, courseId]
  })),

  on(EnrollmentActions.unenrollFromCourse, (state, { courseId }) => ({
    ...state,
    enrolledCourseIds: state.enrolledCourseIds.filter(id => id !== courseId)
  })),

  on(EnrollmentActions.setEnrolledCourses, (state, { courseIds }) => ({
    ...state,
    enrolledCourseIds: courseIds
  }))

);