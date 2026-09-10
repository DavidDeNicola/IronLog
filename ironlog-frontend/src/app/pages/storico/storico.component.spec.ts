import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { StoricoComponent } from './storico.component';

describe('StoricoComponent', () => {
  let component: StoricoComponent;
  let fixture: ComponentFixture<StoricoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StoricoComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StoricoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
