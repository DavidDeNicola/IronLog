import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { AllenamentoComponent } from './allenamento.component';

describe('AllenamentoComponent', () => {
  let component: AllenamentoComponent;
  let fixture: ComponentFixture<AllenamentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AllenamentoComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllenamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
