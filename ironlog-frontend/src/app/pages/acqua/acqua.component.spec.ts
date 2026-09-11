import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { AcquaComponent } from './acqua.component';

describe('AcquaComponent', () => {
  let component: AcquaComponent;
  let fixture: ComponentFixture<AcquaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcquaComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcquaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
