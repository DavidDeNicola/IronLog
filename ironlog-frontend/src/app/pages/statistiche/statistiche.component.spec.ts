import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { StatisticheComponent } from './statistiche.component';

describe('StatisticheComponent', () => {
  let component: StatisticheComponent;
  let fixture: ComponentFixture<StatisticheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticheComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StatisticheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
