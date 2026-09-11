package org.ironlog.app.model;

import java.math.BigDecimal;

public enum LivelloAttivita {

    SEDENTARIO(BigDecimal.valueOf(1.2)),
    LEGGERMENTE_ATTIVO(BigDecimal.valueOf(1.375)),
    MODERATAMENTE_ATTIVO(BigDecimal.valueOf(1.55)),
    MOLTO_ATTIVO(BigDecimal.valueOf(1.725)),
    ESTREMAMENTE_ATTIVO(BigDecimal.valueOf(1.9));

    private final BigDecimal fattore;

    LivelloAttivita(BigDecimal fattore) {
        this.fattore = fattore;
    }

    public BigDecimal getFattore() {
        return fattore;
    }
}
