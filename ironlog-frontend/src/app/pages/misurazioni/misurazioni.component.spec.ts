import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { MisurazioniComponent } from './misurazioni.component';

describe('MisurazioniComponent', () => {
  let component: MisurazioniComponent;
  let fixture: ComponentFixture<MisurazioniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisurazioniComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisurazioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
