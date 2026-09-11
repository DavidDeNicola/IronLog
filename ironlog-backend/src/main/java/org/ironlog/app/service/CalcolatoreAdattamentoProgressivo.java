package org.ironlog.app.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalcolatoreAdattamentoProgressivo {

    /**
     * targetAttuale = base + direction * step * floor(giorniTrascorsi / intervalDays).
     * Con intervalDays &lt;= 0 il target resta statico (fasi senza adattamento progressivo).
     * Il risultato non scende mai sotto {@code calorieMinime}: su una CUT molto lunga il
     * decadimento progressivo non può portare l'atleta sotto la soglia di sicurezza,
     * indipendentemente da quanti cicli di riduzione siano trascorsi.
     */
    public BigDecimal calcolaTargetAttuale(BigDecimal baseTargetCalorico, int adjustmentIntervalDays,
                                            BigDecimal adjustmentStepKcal, int adjustmentDirection,
                                            long giorniTrascorsi, BigDecimal calorieMinime) {
        BigDecimal target;

        if (adjustmentIntervalDays <= 0 || giorniTrascorsi < 0) {
            target = baseTargetCalorico;
        } else {
            long cicli = giorniTrascorsi / adjustmentIntervalDays;
            BigDecimal variazione = adjustmentStepKcal
                    .multiply(BigDecimal.valueOf(adjustmentDirection))
                    .multiply(BigDecimal.valueOf(cicli));
            target = baseTargetCalorico.add(variazione);
        }

        return calorieMinime != null ? target.max(calorieMinime) : target;
    }
}
