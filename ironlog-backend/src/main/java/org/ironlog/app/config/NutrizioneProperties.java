package org.ironlog.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "nutrizione")
public record NutrizioneProperties(
        Cut cut,
        Bulk bulk,
        ReverseDiet reverseDiet,
        MiniCut miniCut,
        DietBreak dietBreak,
        Macro macro,
        Pasti pasti,
        Sicurezza sicurezza
) {

    public record Cut(
            BigDecimal sogliaBfBassaPercent,
            BigDecimal sogliaBfAltaPercent,
            int durataMesiBreve,
            int durataMesiMedia,
            int durataMesiLunga,
            int adjustmentIntervalDays,
            BigDecimal adjustmentStepKcal,
            BigDecimal deficitMinPercent,
            BigDecimal deficitMaxPercent
    ) {}

    public record Bulk(
            int adjustmentIntervalDays,
            BigDecimal adjustmentStepKcal,
            BigDecimal surplusMinPercent,
            BigDecimal surplusMaxPercent
    ) {}

    public record ReverseDiet(
            int adjustmentIntervalDays,
            BigDecimal adjustmentStepKcal
    ) {}

    public record MiniCut(
            BigDecimal deficitMinPercent,
            BigDecimal deficitMaxPercent,
            int durataSettimaneMin,
            int durataSettimaneMax
    ) {}

    public record DietBreak(
            int durataSettimaneMin,
            int durataSettimaneMax
    ) {}

    public record Macro(
            BigDecimal proteinaGPerKgMin,
            BigDecimal proteinaGPerKgMax,
            BigDecimal grassiGPerKgMin,
            BigDecimal grassiGPerKgMax
    ) {}

    /**
     * Ripartizione del target giornaliero residuo tra i pasti, in ordine
     * cronologico. Le percentuali devono sommare a 100.
     */
    public record Pasti(
            BigDecimal percentualeColazione,
            BigDecimal percentualePranzo,
            BigDecimal percentualeSpuntino,
            BigDecimal percentualeCena
    ) {}

    /**
     * Soglie minime di sicurezza: il target calorico di una fase in deficit
     * (CUT, MINI_CUT) e il suo adattamento progressivo non possono mai
     * scendere sotto questi valori, indipendentemente da TDEE o durata della fase.
     */
    public record Sicurezza(
            BigDecimal calorieMinimeFemmina,
            BigDecimal calorieMinimeMaschio
    ) {}
}
