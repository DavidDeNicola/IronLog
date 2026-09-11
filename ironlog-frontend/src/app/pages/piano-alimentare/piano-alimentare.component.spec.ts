import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { PianoAlimentareComponent } from './piano-alimentare.component';

describe('PianoAlimentareComponent', () => {
  let component: PianoAlimentareComponent;
  let fixture: ComponentFixture<PianoAlimentareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PianoAlimentareComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PianoAlimentareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
