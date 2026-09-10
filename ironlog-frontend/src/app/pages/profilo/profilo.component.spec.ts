import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { ProfiloComponent } from './profilo.component';

describe('ProfiloComponent', () => {
  let component: ProfiloComponent;
  let fixture: ComponentFixture<ProfiloComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfiloComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfiloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
