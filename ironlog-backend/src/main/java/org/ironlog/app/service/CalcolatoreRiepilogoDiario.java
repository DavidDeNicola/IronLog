package org.ironlog.app.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalcolatoreRiepilogoDiario {

    public TargetMacro somma(List<VoceDiarioCalcolata> voci) {
        BigDecimal calorie = BigDecimal.ZERO;
        BigDecimal proteine = BigDecimal.ZERO;
        BigDecimal grassi = BigDecimal.ZERO;
        BigDecimal carboidrati = BigDecimal.ZERO;

        for (VoceDiarioCalcolata voce : voci) {
            calorie = calorie.add(voce.calorie());
            proteine = proteine.add(voce.proteineG());
            grassi = grassi.add(voce.grassiG());
            carboidrati = carboidrati.add(voce.carboidratiG());
        }

        return new TargetMacro(calorie, proteine, grassi, carboidrati);
    }
}
