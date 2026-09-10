import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { RegistrazioneComponent } from './registrazione.component';

describe('RegistrazioneComponent', () => {
  let component: RegistrazioneComponent;
  let fixture: ComponentFixture<RegistrazioneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrazioneComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('parte con il ruolo atleta preselezionato', () => {
    expect(component.atleta).toBeTrue();
  });

  it('azzera il coach quando si passa al ruolo coach', () => {
    component.dati.coachId = 3;
    component.selezionaRuolo('COACH');
    expect(component.dati.coachId).toBeNull();
  });

  it('segnala le password non coincidenti', () => {
    component.dati.password = 'password123';
    component.confermaPassword = 'password124';
    component.onSubmit();
    expect(component.errore).toContain('non coincidono');
  });
});
