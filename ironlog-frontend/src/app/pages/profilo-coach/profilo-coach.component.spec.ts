import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfiloCoachComponent } from './profilo-coach.component';

describe('ProfiloCoachComponent', () => {
  let component: ProfiloCoachComponent;
  let fixture: ComponentFixture<ProfiloCoachComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfiloCoachComponent]
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
