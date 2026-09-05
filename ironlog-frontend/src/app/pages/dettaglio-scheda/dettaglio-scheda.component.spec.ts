import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioSchedaComponent } from './dettaglio-scheda.component';

describe('DettaglioSchedaComponent', () => {
  let component: DettaglioSchedaComponent;
  let fixture: ComponentFixture<DettaglioSchedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DettaglioSchedaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DettaglioSchedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
