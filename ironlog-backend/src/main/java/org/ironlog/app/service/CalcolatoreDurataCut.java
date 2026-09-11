package org.ironlog.app.service;

import org.ironlog.app.config.NutrizioneProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalcolatoreDurataCut {

    public int suggerisciDurataMesi(BigDecimal bfPercentFinaleBulk, NutrizioneProperties.Cut cfg) {
        if (bfPercentFinaleBulk.compareTo(cfg.sogliaBfBassaPercent()) < 0) {
            return cfg.durataMesiBreve();
        }
        if (bfPercentFinaleBulk.compareTo(cfg.sogliaBfAltaPercent()) <= 0) {
            return cfg.durataMesiMedia();
        }
        return cfg.durataMesiLunga();
    }
}
