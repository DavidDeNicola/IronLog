package org.ironlog.app.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatoreAdattamentoProgressivoTest {

    private final CalcolatoreAdattamentoProgressivo calcolatore = new CalcolatoreAdattamentoProgressivo();

    @Test
    void restaSulBaseSeNonSonoAncoraTrascorsiGiorniSufficienti() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("2000"), 14, new BigDecimal("75"), -1, 13, new BigDecimal("1200"));
        assertEquals(new BigDecimal("2000"), risultato);
    }

    @Test
    void riduceIlTargetDopoUnIntervalloCompletoPerLaCut() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("2000"), 14, new BigDecimal("75"), -1, 14, new BigDecimal("1200"));
        assertEquals(new BigDecimal("1925"), risultato);
    }

    @Test
    void applicaPiuCicliQuandoSonoTrascorsiPiuIntervalli() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("2000"), 14, new BigDecimal("75"), -1, 30, new BigDecimal("1200"));
        assertEquals(new BigDecimal("1850"), risultato);
    }

    @Test
    void aumentaIlTargetPerLaBulk() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("2800"), 21, new BigDecimal("75"), 1, 42, new BigDecimal("1400"));
        assertEquals(new BigDecimal("2950"), risultato);
    }

    @Test
    void restaStaticoQuandoLIntervalloENonPositivo() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("2400"), 0, new BigDecimal("75"), -1, 100, new BigDecimal("1200"));
        assertEquals(new BigDecimal("2400"), risultato);
    }

    @Test
    void nonScendeMaiSottoLaSogliaMinimaDiSicurezzaSuUnaCutMoltoLunga() {
        // Cut lunga 189 giorni (~13 cicli da 14 giorni): senza floor il target
        // crollerebbe a 1727.16 - 13*75 = 752.16 kcal, sotto qualsiasi soglia sicura.
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("1727.16"), 14, new BigDecimal("75"), -1, 189, new BigDecimal("1200"));
        assertEquals(new BigDecimal("1200"), risultato);
    }

    @Test
    void applicaLaSogliaMaschileQuandoPiuAltaDiQuellaFemminile() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("1727.16"), 14, new BigDecimal("75"), -1, 189, new BigDecimal("1400"));
        assertEquals(new BigDecimal("1400"), risultato);
    }

    @Test
    void nonApplicaAlcunFloorSeCalorieMinimeENullo() {
        BigDecimal risultato = calcolatore.calcolaTargetAttuale(
                new BigDecimal("1727.16"), 14, new BigDecimal("75"), -1, 189, null);
        assertEquals(new BigDecimal("752.16"), risultato);
    }
}
