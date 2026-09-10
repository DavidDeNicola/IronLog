import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { StatisticheClientiComponent } from './statistiche-clienti.component';

describe('StatisticheClientiComponent', () => {
  let component: StatisticheClientiComponent;
  let fixture: ComponentFixture<StatisticheClientiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticheClientiComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StatisticheClientiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
