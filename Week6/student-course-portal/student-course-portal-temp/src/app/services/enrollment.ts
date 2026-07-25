import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Student {
  id: number;
  name: string;
  courseId: number;
}

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {

  private apiUrl = 'http://localhost:3000/students';

  constructor(private http: HttpClient) {}

  getStudentsByCourse(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiUrl}?courseId=${courseId}`);
  }
}