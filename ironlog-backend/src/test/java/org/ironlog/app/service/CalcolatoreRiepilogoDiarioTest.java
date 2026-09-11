package org.ironlog.app.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatoreRiepilogoDiarioTest {

    private final CalcolatoreRiepilogoDiario calcolatore = new CalcolatoreRiepilogoDiario();

    @Test
    void sommaLeVociDelGiorno() {
        List<VoceDiarioCalcolata> voci = List.of(
                new VoceDiarioCalcolata(new BigDecimal("200"), new BigDecimal("20"), new BigDecimal("5"), new BigDecimal("15")),
                new VoceDiarioCalcolata(new BigDecimal("300"), new BigDecimal("10"), new BigDecimal("10"), new BigDecimal("40"))
        );

        TargetMacro totali = calcolatore.somma(voci);

        assertEquals(new BigDecimal("500"), totali.calorie());
        assertEquals(new BigDecimal("30"), totali.proteineG());
        assertEquals(new BigDecimal("15"), totali.grassiG());
        assertEquals(new BigDecimal("55"), totali.carboidratiG());
    }

    @Test
    void restituisceZeroSenzaVoci() {
        TargetMacro totali = calcolatore.somma(List.of());

        assertEquals(BigDecimal.ZERO, totali.calorie());
        assertEquals(BigDecimal.ZERO, totali.proteineG());
        assertEquals(BigDecimal.ZERO, totali.grassiG());
        assertEquals(BigDecimal.ZERO, totali.carboidratiG());
    }
}
