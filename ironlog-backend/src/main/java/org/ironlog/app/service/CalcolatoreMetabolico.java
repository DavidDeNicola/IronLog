package org.ironlog.app.service;

import org.ironlog.app.model.Sesso;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalcolatoreMetabolico {

    private static final BigDecimal DIECI = BigDecimal.valueOf(10);
    private static final BigDecimal SEI_VIRGOLA_25 = BigDecimal.valueOf(6.25);
    private static final BigDecimal CINQUE = BigDecimal.valueOf(5);
    private static final BigDecimal CENTO_SESSANTUNO = BigDecimal.valueOf(161);
    private static final BigDecimal TRECENTOSETTANTA = BigDecimal.valueOf(370);
    private static final BigDecimal VENTUNO_VIRGOLA_6 = BigDecimal.valueOf(21.6);
    private static final BigDecimal CENTO = BigDecimal.valueOf(100);

    public BigDecimal calcolaBmrMifflin(Sesso sesso, BigDecimal pesoKg, BigDecimal altezzaCm, int eta) {
        BigDecimal base = DIECI.multiply(pesoKg)
                .add(SEI_VIRGOLA_25.multiply(altezzaCm))
                .subtract(CINQUE.multiply(BigDecimal.valueOf(eta)));

        BigDecimal bmr = sesso == Sesso.MASCHIO ? base.add(CINQUE) : base.subtract(CENTO_SESSANTUNO);
        return bmr.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcolaBmrKatchMcArdle(BigDecimal pesoKg, BigDecimal percentualeMassaGrassa) {
        BigDecimal frazioneMassaMagra = BigDecimal.ONE.subtract(
                percentualeMassaGrassa.divide(CENTO, 6, RoundingMode.HALF_UP));
        BigDecimal massaMagraKg = pesoKg.multiply(frazioneMassaMagra);

        return TRECENTOSETTANTA.add(VENTUNO_VIRGOLA_6.multiply(massaMagraKg))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcolaTdee(BigDecimal bmr, BigDecimal fattoreAttivita) {
        return bmr.multiply(fattoreAttivita).setScale(2, RoundingMode.HALF_UP);
    }
}
