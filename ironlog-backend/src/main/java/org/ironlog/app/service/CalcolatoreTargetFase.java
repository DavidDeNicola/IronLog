package org.ironlog.app.service;

import org.ironlog.app.config.NutrizioneProperties;
import org.ironlog.app.model.Sesso;
import org.ironlog.app.model.TipoFase;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalcolatoreTargetFase {

    private static final BigDecimal DUE = BigDecimal.valueOf(2);
    private static final BigDecimal CENTO = BigDecimal.valueOf(100);
    private static final BigDecimal QUATTRO = BigDecimal.valueOf(4);
    private static final BigDecimal NOVE = BigDecimal.valueOf(9);

    /**
     * Le fasi in deficit (CUT, MINI_CUT) non possono mai scendere sotto la
     * soglia minima di sicurezza configurata per il sesso dell'atleta, anche
     * se il punto medio del deficit configurato lo porterebbe più in basso
     * (es. TDEE basso combinato a un deficit aggressivo).
     */
    public BigDecimal calcolaCalorieTarget(BigDecimal tdee, TipoFase tipo, Sesso sesso, NutrizioneProperties properties) {
        return switch (tipo) {
            case BULK -> applicaPercentuale(tdee, puntoMedio(properties.bulk().surplusMinPercent(), properties.bulk().surplusMaxPercent()));
            case CUT -> applicaPercentuale(tdee, puntoMedio(properties.cut().deficitMinPercent(), properties.cut().deficitMaxPercent()).negate())
                    .max(calorieMinime(sesso, properties.sicurezza()));
            case MINI_CUT -> applicaPercentuale(tdee, puntoMedio(properties.miniCut().deficitMinPercent(), properties.miniCut().deficitMaxPercent()).negate())
                    .max(calorieMinime(sesso, properties.sicurezza()));
            case MAINTENANCE, DIET_BREAK, REVERSE_DIET -> tdee.setScale(2, RoundingMode.HALF_UP);
        };
    }

    private BigDecimal calorieMinime(Sesso sesso, NutrizioneProperties.Sicurezza sicurezza) {
        BigDecimal minimo = sesso == Sesso.FEMMINA ? sicurezza.calorieMinimeFemmina() : sicurezza.calorieMinimeMaschio();
        return minimo.setScale(2, RoundingMode.HALF_UP);
    }

    public TargetMacro calcolaMacro(BigDecimal calorieTarget, BigDecimal pesoKg, NutrizioneProperties.Macro macroCfg) {
        BigDecimal proteineG = puntoMedio(macroCfg.proteinaGPerKgMin(), macroCfg.proteinaGPerKgMax())
                .multiply(pesoKg).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grassiG = puntoMedio(macroCfg.grassiGPerKgMin(), macroCfg.grassiGPerKgMax())
                .multiply(pesoKg).setScale(2, RoundingMode.HALF_UP);

        BigDecimal calorieDaProteineEGrassi = proteineG.multiply(QUATTRO).add(grassiG.multiply(NOVE));
        BigDecimal carboidratiG = calorieTarget.subtract(calorieDaProteineEGrassi)
                .divide(QUATTRO, 2, RoundingMode.HALF_UP)
                .max(BigDecimal.ZERO.setScale(2));

        return new TargetMacro(calorieTarget.setScale(2, RoundingMode.HALF_UP), proteineG, grassiG, carboidratiG);
    }

    private BigDecimal applicaPercentuale(BigDecimal tdee, BigDecimal percentuale) {
        BigDecimal fattore = BigDecimal.ONE.add(percentuale.divide(CENTO, 6, RoundingMode.HALF_UP));
        return tdee.multiply(fattore).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal puntoMedio(BigDecimal min, BigDecimal max) {
        return min.add(max).divide(DUE, 6, RoundingMode.HALF_UP);
    }
}
