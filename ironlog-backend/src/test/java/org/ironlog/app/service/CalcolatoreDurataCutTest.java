package org.ironlog.app.service;

import org.ironlog.app.config.NutrizioneProperties;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatoreDurataCutTest {

    private final CalcolatoreDurataCut calcolatore = new CalcolatoreDurataCut();

    private final NutrizioneProperties.Cut cfg = new NutrizioneProperties.Cut(
            new BigDecimal("12"), new BigDecimal("18"), 2, 3, 4,
            14, new BigDecimal("75"), new BigDecimal("15"), new BigDecimal("25"));

    @Test
    void suggerisceCutBreveSottoLaSogliaBassa() {
        assertEquals(2, calcolatore.suggerisciDurataMesi(new BigDecimal("10"), cfg));
    }

    @Test
    void suggerisceCutMediaTraLeDueSoglie() {
        assertEquals(3, calcolatore.suggerisciDurataMesi(new BigDecimal("15"), cfg));
    }

    @Test
    void suggerisceCutLungaSopraLaSogliaAlta() {
        assertEquals(4, calcolatore.suggerisciDurataMesi(new BigDecimal("20"), cfg));
    }
}
