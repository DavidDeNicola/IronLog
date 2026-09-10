import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { EserciziComponent } from './esercizi.component';

describe('EserciziComponent', () => {
  let component: EserciziComponent;
  let fixture: ComponentFixture<EserciziComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EserciziComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EserciziComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
