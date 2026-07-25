import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideStore, provideState } from '@ngrx/store';
import { of } from 'rxjs';

import { CourseList } from './course-list';
import { EnrollmentService } from '../../services/enrollment';
import { courseReducer } from '../../store/course/course.reducer';

describe('CourseList', () => {

  let component: CourseList;
  let fixture: ComponentFixture<CourseList>;

  const enrollmentServiceMock = {

    getStudentsByCourse: () => of([])

  };

  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [CourseList],

      providers: [

        provideStore({}),

        provideState({
          name: 'course',
          reducer: courseReducer
        }),

        {
          provide: EnrollmentService,
          useValue: enrollmentServiceMock
        }

      ]

    }).compileComponents();

    fixture = TestBed.createComponent(CourseList);

    component = fixture.componentInstance;

    fixture.detectChanges();

  });

  it('should create', () => {

    expect(component).toBeTruthy();

  });

});