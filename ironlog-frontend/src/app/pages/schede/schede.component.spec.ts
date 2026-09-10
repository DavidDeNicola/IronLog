import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { SchedeComponent } from './schede.component';

describe('SchedeComponent', () => {
  let component: SchedeComponent;
  let fixture: ComponentFixture<SchedeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedeComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
