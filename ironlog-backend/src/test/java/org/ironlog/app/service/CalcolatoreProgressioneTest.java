package org.ironlog.app.service;

import org.ironlog.app.model.SerieEseguita;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalcolatoreProgressioneTest {

    private final CalcolatoreProgressione calcolatore = new CalcolatoreProgressione();

    @Test
    void aumentaDelDueEMezzoPerCentoArrotondandoAlDisco() {
        BigDecimal risultato = calcolatore.aumenta(new BigDecimal("60.00"));
        assertEquals(new BigDecimal("61.25"), risultato);
    }

    @Test
    void diminuisceDelDieciPerCentoArrotondandoAlDisco() {
        BigDecimal risultato = calcolatore.diminuisci(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("90.00"), risultato);
    }

    @Test
    void arrotondaAlDiscoPiuVicinoQuandoIlRisultatoNonEMultiploDi125() {
        BigDecimal risultato = calcolatore.aumenta(new BigDecimal("82.50"));
        assertEquals(new BigDecimal("85.00"), risultato);
    }

    @Test
    void aumentaDiUnDiscoQuandoLaPercentualeNonBastaASuperareLArrotondamento() {
        // 20 kg + 2,5% = 20,5 kg: arrotondato al disco tornerebbe a 20 kg.
        BigDecimal risultato = calcolatore.aumenta(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("21.25"), risultato);
    }

    @Test
    void aumentaSempreAncheSuiCarichiMoltoLeggeri() {
        BigDecimal risultato = calcolatore.aumenta(new BigDecimal("5.00"));
        assertEquals(new BigDecimal("6.25"), risultato);
    }

    @Test
    void diminuisceDiAlmenoUnDiscoSuiCarichiLeggeri() {
        // 5 kg - 10% = 4,5 kg: arrotondato al disco tornerebbe a 5 kg.
        BigDecimal risultato = calcolatore.diminuisci(new BigDecimal("5.00"));
        assertEquals(new BigDecimal("3.75"), risultato);
    }

    @Test
    void nonScendeMaiSottoLoZero() {
        BigDecimal risultato = calcolatore.diminuisci(new BigDecimal("0.00"));
        assertEquals(new BigDecimal("0.00"), risultato);
    }

    @Test
    void completatoQuandoTutteLeSerieRaggiungonoLObiettivo() {
        List<SerieEseguita> serie = List.of(serieCon(8), serieCon(8), serieCon(9), serieCon(8));
        assertTrue(calcolatore.isCompletato(serie, 4, 8));
    }

    @Test
    void nonCompletatoQuandoUnaSerieNonRaggiungeLObiettivo() {
        List<SerieEseguita> serie = List.of(serieCon(8), serieCon(8), serieCon(6), serieCon(8));
        assertFalse(calcolatore.isCompletato(serie, 4, 8));
    }

    @Test
    void nonCompletatoQuandoLeSerieSonoMenoDiQuelleAttese() {
        List<SerieEseguita> serie = List.of(serieCon(8), serieCon(8), serieCon(8));
        assertFalse(calcolatore.isCompletato(serie, 4, 8));
    }

    @Test
    void nonCompletatoQuandoNonCiSonoSerie() {
        assertFalse(calcolatore.isCompletato(List.of(), 4, 8));
    }

    private SerieEseguita serieCon(int ripetizioni) {
        SerieEseguita s = new SerieEseguita();
        s.setRipetizioni(ripetizioni);
        return s;
    }
}
