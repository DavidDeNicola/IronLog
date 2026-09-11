package org.ironlog.app.service;

import org.ironlog.app.model.Alimento;

import java.math.BigDecimal;

public record VocePianoGenerata(Alimento alimento, BigDecimal quantitaGrammi) {
}
