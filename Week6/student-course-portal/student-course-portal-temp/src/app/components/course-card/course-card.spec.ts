import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideStore } from '@ngrx/store';
import { provideState } from '@ngrx/store';

import { CourseCard } from './course-card';
import { enrollmentReducer } from '../../store/enrollment/enrollment.reducer';

describe('CourseCard', () => {

  let component: CourseCard;
  let fixture: ComponentFixture<CourseCard>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [CourseCard],

      providers: [

        provideStore({}),

        provideState({
          name: 'enrollment',
          reducer: enrollmentReducer
        })

      ]

    }).compileComponents();

    fixture = TestBed.createComponent(CourseCard);

    component = fixture.componentInstance;

    component.course = {
      id: 1,
      name: 'Java',
      code: 'CS101',
      credits: 4
    };

    fixture.detectChanges();

  });

  it('should create', () => {

    expect(component).toBeTruthy();

  });

});