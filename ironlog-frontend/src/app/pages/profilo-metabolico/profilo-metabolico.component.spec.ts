import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { ProfiloMetabolicoComponent } from './profilo-metabolico.component';

describe('ProfiloMetabolicoComponent', () => {
  let component: ProfiloMetabolicoComponent;
  let fixture: ComponentFixture<ProfiloMetabolicoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfiloMetabolicoComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfiloMetabolicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
