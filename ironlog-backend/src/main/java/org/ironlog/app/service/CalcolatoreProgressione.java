package org.ironlog.app.service;

import org.ironlog.app.model.SerieEseguita;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CalcolatoreProgressione {

    private static final BigDecimal INCREMENTO = BigDecimal.valueOf(1.025);
    private static final BigDecimal DECREMENTO = BigDecimal.valueOf(0.9);
    private static final BigDecimal DISCO = BigDecimal.valueOf(1.25);

    public BigDecimal aumenta(BigDecimal pesoAttuale) {
        BigDecimal aumentato = pesoAttuale.multiply(INCREMENTO);
        return arrotondaAlDisco(aumentato);
    }

    public BigDecimal diminuisci(BigDecimal pesoAttuale) {
        BigDecimal diminuito = pesoAttuale.multiply(DECREMENTO);
        return arrotondaAlDisco(diminuito);
    }

    private BigDecimal arrotondaAlDisco(BigDecimal peso) {
        return peso.divide(DISCO, 0, RoundingMode.HALF_UP)
                .multiply(DISCO)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isCompletato(List<SerieEseguita> serie, int serieAttese, int ripetizioniAttese) {
        if (serie.size() < serieAttese) {
            return false;
        }
        return serie.stream().allMatch(s -> s.getRipetizioni() >= ripetizioniAttese);
    }
}
