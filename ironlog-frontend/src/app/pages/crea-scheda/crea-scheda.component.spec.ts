import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { CreaSchedaComponent } from './crea-scheda.component';

describe('CreaSchedaComponent', () => {
  let component: CreaSchedaComponent;
  let fixture: ComponentFixture<CreaSchedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreaSchedaComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreaSchedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
