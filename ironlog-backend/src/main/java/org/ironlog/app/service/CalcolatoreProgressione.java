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

    /**
     * Aumenta il carico del 2,5% arrotondando al disco piu' vicino.
     * Sotto i 25 kg il 2,5% e' inferiore a mezzo disco e l'arrotondamento
     * riporterebbe al peso di partenza: in quel caso si sale di un disco pieno,
     * cosi' la progressione non resta mai bloccata.
     */
    public BigDecimal aumenta(BigDecimal pesoAttuale) {
        BigDecimal aumentato = arrotondaAlDisco(pesoAttuale.multiply(INCREMENTO));

        if (aumentato.compareTo(pesoAttuale) <= 0) {
            return arrotondaAlDisco(pesoAttuale.add(DISCO));
        }
        return aumentato;
    }

    /**
     * Riduce il carico del 10% arrotondando al disco piu' vicino, con la
     * simmetrica garanzia di scendere di almeno un disco (senza mai andare
     * sotto lo zero).
     */
    public BigDecimal diminuisci(BigDecimal pesoAttuale) {
        BigDecimal diminuito = arrotondaAlDisco(pesoAttuale.multiply(DECREMENTO));

        if (diminuito.compareTo(pesoAttuale) >= 0) {
            diminuito = arrotondaAlDisco(pesoAttuale.subtract(DISCO));
        }
        return diminuito.max(BigDecimal.ZERO.setScale(2));
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
