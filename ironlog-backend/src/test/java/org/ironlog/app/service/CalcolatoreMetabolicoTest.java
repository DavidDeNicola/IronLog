package org.ironlog.app.service;

import org.ironlog.app.model.Sesso;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatoreMetabolicoTest {

    private final CalcolatoreMetabolico calcolatore = new CalcolatoreMetabolico();

    @Test
    void calcolaBmrMifflinPerUnUomo() {
        BigDecimal bmr = calcolatore.calcolaBmrMifflin(Sesso.MASCHIO,
                new BigDecimal("80"), new BigDecimal("180"), 30);
        assertEquals(new BigDecimal("1780.00"), bmr);
    }

    @Test
    void calcolaBmrMifflinPerUnaDonna() {
        BigDecimal bmr = calcolatore.calcolaBmrMifflin(Sesso.FEMMINA,
                new BigDecimal("60"), new BigDecimal("165"), 25);
        assertEquals(new BigDecimal("1345.25"), bmr);
    }

    @Test
    void calcolaBmrKatchMcArdleDaMassaMagra() {
        BigDecimal bmr = calcolatore.calcolaBmrKatchMcArdle(new BigDecimal("80"), new BigDecimal("15"));
        assertEquals(new BigDecimal("1838.80"), bmr);
    }

    @Test
    void calcolaTdeeApplicandoIlFattoreDiAttivita() {
        BigDecimal tdee = calcolatore.calcolaTdee(new BigDecimal("1780.00"), new BigDecimal("1.55"));
        assertEquals(new BigDecimal("2759.00"), tdee);
    }
}
