import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { DiarioAlimentareComponent } from './diario-alimentare.component';

describe('DiarioAlimentareComponent', () => {
  let component: DiarioAlimentareComponent;
  let fixture: ComponentFixture<DiarioAlimentareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiarioAlimentareComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiarioAlimentareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
