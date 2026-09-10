import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { ProfiloCoachComponent } from './profilo-coach.component';

describe('ProfiloCoachComponent', () => {
  let component: ProfiloCoachComponent;
  let fixture: ComponentFixture<ProfiloCoachComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfiloCoachComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfiloCoachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
