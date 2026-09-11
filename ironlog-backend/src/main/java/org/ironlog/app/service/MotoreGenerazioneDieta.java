package org.ironlog.app.service;

import org.ironlog.app.exception.NessunAlimentoDisponibileException;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.IngredienteRicetta;
import org.ironlog.app.model.Piatto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Motore di generazione dieta basato su piatti reali: per ogni pasto sceglie,
 * tra i piatti idonei non ancora usati nella stessa giornata, quello il cui
 * profilo macro (proporzione kcal da proteine/grassi/carboidrati) è più
 * vicino al target del pasto, poi lo scala proporzionalmente (entro un
 * intervallo realistico) per avvicinarsi al target calorico residuo.
 * Nessuna libreria esterna di ottimizzazione: logica scritta da zero.
 */
@Component
public class MotoreGenerazioneDieta {

    private static final BigDecimal CENTO = BigDecimal.valueOf(100);
    private static final BigDecimal QUATTRO = BigDecimal.valueOf(4);
    private static final BigDecimal NOVE = BigDecimal.valueOf(9);
    private static final BigDecimal TOLLERANZA = new BigDecimal("0.10");

    /** Un piatto non viene mai scalato sotto il 40% o sopra il 250% della sua porzione base. */
    private static final BigDecimal SCALA_MIN = new BigDecimal("0.40");
    private static final BigDecimal SCALA_MAX = new BigDecimal("2.50");

    public PianoGenerato genera(List<Piatto> piattiEligibili, TargetMacro targetPasto, Set<Piatto> giaUsatiOggi) {
        if (piattiEligibili.isEmpty()) {
            throw new NessunAlimentoDisponibileException("Nessun piatto disponibile per questo pasto");
        }

        List<Piatto> candidati = piattiEligibili.stream()
                .filter(p -> !giaUsatiOggi.contains(p))
                .toList();
        if (candidati.isEmpty()) {
            // Meglio ripetere un piatto già usato oggi che far fallire la generazione del piano.
            candidati = piattiEligibili;
        }

        Piatto scelto = null;
        TargetMacro baseScelto = null;
        BigDecimal migliorScarto = null;

        for (Piatto candidato : candidati) {
            TargetMacro base = totaleBase(candidato);
            if (base.calorie().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal scarto = scartoRatioMacro(base, targetPasto);
            if (migliorScarto == null || scarto.compareTo(migliorScarto) < 0) {
                migliorScarto = scarto;
                scelto = candidato;
                baseScelto = base;
            }
        }

        if (scelto == null) {
            throw new NessunAlimentoDisponibileException("Nessun piatto valido disponibile per questo pasto");
        }

        BigDecimal scala = targetPasto.calorie().compareTo(BigDecimal.ZERO) > 0
                ? targetPasto.calorie().divide(baseScelto.calorie(), 6, RoundingMode.HALF_UP)
                : SCALA_MIN;
        scala = scala.max(SCALA_MIN).min(SCALA_MAX);

        List<VocePianoGenerata> voci = new ArrayList<>();
        BigDecimal calorieRaggiunte = BigDecimal.ZERO;
        BigDecimal proteineRaggiunte = BigDecimal.ZERO;
        BigDecimal grassiRaggiunti = BigDecimal.ZERO;
        BigDecimal carboRaggiunti = BigDecimal.ZERO;

        for (IngredienteRicetta ingrediente : scelto.getIngredienti()) {
            BigDecimal quantita = ingrediente.getQuantitaGrammi().multiply(scala)
                    .setScale(0, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            if (quantita.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            Alimento alimento = ingrediente.getAlimento();
            BigDecimal fattore = quantita.divide(CENTO, 6, RoundingMode.HALF_UP);

            BigDecimal calorieVoce = alimento.getCalorie100g().multiply(fattore).setScale(2, RoundingMode.HALF_UP);
            BigDecimal proteineVoce = alimento.getProteineG().multiply(fattore).setScale(2, RoundingMode.HALF_UP);
            BigDecimal grassiVoce = alimento.getGrassiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP);
            BigDecimal carboVoce = alimento.getCarboidratiG().multiply(fattore).setScale(2, RoundingMode.HALF_UP);

            voci.add(new VocePianoGenerata(alimento, quantita));

            calorieRaggiunte = calorieRaggiunte.add(calorieVoce);
            proteineRaggiunte = proteineRaggiunte.add(proteineVoce);
            grassiRaggiunti = grassiRaggiunti.add(grassiVoce);
            carboRaggiunti = carboRaggiunti.add(carboVoce);
        }

        TargetMacro raggiunto = new TargetMacro(calorieRaggiunte, proteineRaggiunte, grassiRaggiunti, carboRaggiunti);
        boolean vincoliSoddisfatti = entroTolleranza(raggiunto.calorie(), targetPasto.calorie())
                && entroTolleranza(raggiunto.proteineG(), targetPasto.proteineG())
                && entroTolleranza(raggiunto.grassiG(), targetPasto.grassiG())
                && entroTolleranza(raggiunto.carboidratiG(), targetPasto.carboidratiG());

        return new PianoGenerato(voci, raggiunto, vincoliSoddisfatti, scelto);
    }

    private TargetMacro totaleBase(Piatto piatto) {
        BigDecimal calorie = BigDecimal.ZERO;
        BigDecimal proteine = BigDecimal.ZERO;
        BigDecimal grassi = BigDecimal.ZERO;
        BigDecimal carbo = BigDecimal.ZERO;

        for (IngredienteRicetta ingrediente : piatto.getIngredienti()) {
            BigDecimal fattore = ingrediente.getQuantitaGrammi().divide(CENTO, 6, RoundingMode.HALF_UP);
            Alimento alimento = ingrediente.getAlimento();
            calorie = calorie.add(alimento.getCalorie100g().multiply(fattore));
            proteine = proteine.add(alimento.getProteineG().multiply(fattore));
            grassi = grassi.add(alimento.getGrassiG().multiply(fattore));
            carbo = carbo.add(alimento.getCarboidratiG().multiply(fattore));
        }

        return new TargetMacro(calorie, proteine, grassi, carbo);
    }

    /**
     * Distanza euclidea al quadrato tra le proporzioni kcal (proteine/grassi/carboidrati)
     * del piatto e quelle del target: più è bassa, più il piatto è "azzeccato" per il pasto.
     */
    private BigDecimal scartoRatioMacro(TargetMacro base, TargetMacro target) {
        BigDecimal[] ratioBase = ratioMacro(base);
        BigDecimal[] ratioTarget = ratioMacro(target);

        BigDecimal scarto = BigDecimal.ZERO;
        for (int i = 0; i < ratioBase.length; i++) {
            BigDecimal diff = ratioBase[i].subtract(ratioTarget[i]);
            scarto = scarto.add(diff.multiply(diff));
        }
        return scarto;
    }

    private BigDecimal[] ratioMacro(TargetMacro macro) {
        BigDecimal kcalProteine = macro.proteineG().multiply(QUATTRO);
        BigDecimal kcalGrassi = macro.grassiG().multiply(NOVE);
        BigDecimal kcalCarbo = macro.carboidratiG().multiply(QUATTRO);
        BigDecimal totale = kcalProteine.add(kcalGrassi).add(kcalCarbo);

        if (totale.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        }

        return new BigDecimal[]{
                kcalProteine.divide(totale, 6, RoundingMode.HALF_UP),
                kcalGrassi.divide(totale, 6, RoundingMode.HALF_UP),
                kcalCarbo.divide(totale, 6, RoundingMode.HALF_UP)
        };
    }

    private boolean entroTolleranza(BigDecimal raggiunto, BigDecimal target) {
        if (target.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        BigDecimal relativo = raggiunto.subtract(target).abs().divide(target, 6, RoundingMode.HALF_UP);
        return relativo.compareTo(TOLLERANZA) <= 0;
    }
}
