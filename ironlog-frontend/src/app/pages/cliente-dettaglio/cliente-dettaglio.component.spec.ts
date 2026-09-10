import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { ClienteDettaglioComponent } from './cliente-dettaglio.component';

describe('ClienteDettaglioComponent', () => {
  let component: ClienteDettaglioComponent;
  let fixture: ComponentFixture<ClienteDettaglioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteDettaglioComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClienteDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
