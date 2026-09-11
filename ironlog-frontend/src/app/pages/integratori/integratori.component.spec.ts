import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { IntegratoriComponent } from './integratori.component';

describe('IntegratoriComponent', () => {
  let component: IntegratoriComponent;
  let fixture: ComponentFixture<IntegratoriComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IntegratoriComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntegratoriComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
