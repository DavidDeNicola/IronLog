import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatisticheClientiComponent } from './statistiche-clienti.component';

describe('StatisticheClientiComponent', () => {
  let component: StatisticheClientiComponent;
  let fixture: ComponentFixture<StatisticheClientiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatisticheClientiComponent]
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
