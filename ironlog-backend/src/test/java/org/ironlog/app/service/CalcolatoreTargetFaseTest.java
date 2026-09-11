package org.ironlog.app.service;

import org.ironlog.app.config.NutrizioneProperties;
import org.ironlog.app.model.Sesso;
import org.ironlog.app.model.TipoFase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcolatoreTargetFaseTest {

    private final CalcolatoreTargetFase calcolatore = new CalcolatoreTargetFase();

    private final NutrizioneProperties properties = new NutrizioneProperties(
            new NutrizioneProperties.Cut(
                    new BigDecimal("12"), new BigDecimal("18"), 2, 3, 4,
                    14, new BigDecimal("75"), new BigDecimal("15"), new BigDecimal("25")),
            new NutrizioneProperties.Bulk(21, new BigDecimal("75"), new BigDecimal("10"), new BigDecimal("20")),
            new NutrizioneProperties.ReverseDiet(7, new BigDecimal("75")),
            new NutrizioneProperties.MiniCut(new BigDecimal("25"), new BigDecimal("30"), 2, 4),
            new NutrizioneProperties.DietBreak(1, 2),
            new NutrizioneProperties.Macro(new BigDecimal("1.6"), new BigDecimal("2.2"), new BigDecimal("0.8"), new BigDecimal("1.0")),
            new NutrizioneProperties.Pasti(new BigDecimal("25"), new BigDecimal("35"), new BigDecimal("10"), new BigDecimal("30")),
            new NutrizioneProperties.Sicurezza(new BigDecimal("1200"), new BigDecimal("1400"))
    );

    @Test
    void calcolaCalorieTargetPerLaCutAlPuntoMedioDelDeficit() {
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("2500"), TipoFase.CUT, Sesso.FEMMINA, properties);
        assertEquals(new BigDecimal("2000.00"), calorie);
    }

    @Test
    void calcolaCalorieTargetPerLaBulkAlPuntoMedioDelSurplus() {
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("2500"), TipoFase.BULK, Sesso.MASCHIO, properties);
        assertEquals(new BigDecimal("2875.00"), calorie);
    }

    @Test
    void calcolaCalorieTargetPerIlMantenimentoParialTdee() {
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("2500"), TipoFase.MAINTENANCE, Sesso.FEMMINA, properties);
        assertEquals(new BigDecimal("2500.00"), calorie);
    }

    @Test
    void calcolaCalorieTargetPerLaMiniCutConDeficitPiuAggressivo() {
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("2500"), TipoFase.MINI_CUT, Sesso.FEMMINA, properties);
        assertEquals(new BigDecimal("1812.50"), calorie);
    }

    @Test
    void nonScendeMaiSottoLaSogliaFemminileSuUnaCutConTdeeBasso() {
        // TDEE 1400: il 20% di deficit darebbe 1120 kcal, sotto la soglia minima femminile (1200).
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("1400"), TipoFase.CUT, Sesso.FEMMINA, properties);
        assertEquals(new BigDecimal("1200.00"), calorie);
    }

    @Test
    void nonScendeMaiSottoLaSogliaMaschileSuUnaMiniCutConTdeeBasso() {
        // TDEE 1500: il 27.5% di deficit darebbe ~1087 kcal, sotto la soglia minima maschile (1400).
        BigDecimal calorie = calcolatore.calcolaCalorieTarget(new BigDecimal("1500"), TipoFase.MINI_CUT, Sesso.MASCHIO, properties);
        assertEquals(new BigDecimal("1400.00"), calorie);
    }

    @Test
    void calcolaMacroRipartendoLeCalorieResidueSuiCarboidrati() {
        TargetMacro macro = calcolatore.calcolaMacro(new BigDecimal("2000"), new BigDecimal("80"), properties.macro());
        assertEquals(new BigDecimal("152.00"), macro.proteineG());
        assertEquals(new BigDecimal("72.00"), macro.grassiG());
        assertEquals(new BigDecimal("186.00"), macro.carboidratiG());
    }
}
