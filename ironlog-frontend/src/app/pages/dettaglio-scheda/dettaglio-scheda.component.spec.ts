import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { DettaglioSchedaComponent } from './dettaglio-scheda.component';

describe('DettaglioSchedaComponent', () => {
  let component: DettaglioSchedaComponent;
  let fixture: ComponentFixture<DettaglioSchedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DettaglioSchedaComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
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
