package org.ironlog.app.service;

import org.ironlog.app.exception.NessunAlimentoDisponibileException;
import org.ironlog.app.model.Alimento;
import org.ironlog.app.model.FonteAlimento;
import org.ironlog.app.model.IngredienteRicetta;
import org.ironlog.app.model.Piatto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MotoreGenerazioneDietaTest {

    private final MotoreGenerazioneDieta motore = new MotoreGenerazioneDieta();

    @Test
    void generaEspandeGliIngredientiDelPiattoScaltoSulTargetCalorico() {
        Piatto piatto = piatto("Pollo e riso",
                ingrediente(alimento("Pollo", "165", "31", "3.6", "0"), "150"),
                ingrediente(alimento("Riso", "130", "2.7", "0.3", "28"), "100"));
        // Base: 150g pollo (247.5 kcal) + 100g riso (130 kcal) = 377.5 kcal.
        TargetMacro target = new TargetMacro(new BigDecimal("755"), new BigDecimal("62"), new BigDecimal("7"), new BigDecimal("56"));

        PianoGenerato risultato = motore.genera(List.of(piatto), target, Set.of());

        assertEquals(piatto, risultato.piattoScelto());
        assertEquals(2, risultato.voci().size());
        // Target calorico e' il doppio della base: scala attesa 2.0 -> 300g pollo, 200g riso.
        assertEquals(new BigDecimal("300.00"), risultato.voci().get(0).quantitaGrammi());
        assertEquals(new BigDecimal("200.00"), risultato.voci().get(1).quantitaGrammi());
    }

    @Test
    void sceglieIlPiattoConIlProfiloMacroPiuVicinoAlTarget() {
        Piatto piattoProteico = piatto("Solo proteine",
                ingrediente(alimento("Fonte proteica", "100", "25", "0", "0"), "100"));
        Piatto piattoGlucidico = piatto("Solo carboidrati",
                ingrediente(alimento("Fonte glucidica", "100", "0", "0", "25"), "100"));

        // Target quasi tutto proteico: il piatto proteico deve avere uno scarto di ratio minore.
        TargetMacro target = new TargetMacro(new BigDecimal("100"), new BigDecimal("24"), new BigDecimal("0"), new BigDecimal("1"));

        PianoGenerato risultato = motore.genera(List.of(piattoProteico, piattoGlucidico), target, Set.of());

        assertEquals(piattoProteico, risultato.piattoScelto());
    }

    @Test
    void nonRiutilizzaUnPiattoGiaUsatoOggiSeCEUnAlternativa() {
        Piatto piattoA = piatto("Piatto A", ingrediente(alimento("A", "150", "10", "5", "10"), "100"));
        Piatto piattoB = piatto("Piatto B", ingrediente(alimento("B", "150", "10", "5", "10"), "100"));
        TargetMacro target = new TargetMacro(new BigDecimal("150"), new BigDecimal("10"), new BigDecimal("5"), new BigDecimal("10"));

        PianoGenerato risultato = motore.genera(List.of(piattoA, piattoB), target, Set.of(piattoA));

        assertEquals(piattoB, risultato.piattoScelto());
    }

    @Test
    void ripeteUnPiattoSeNonCiSonoAlternativeNonUsateOggi() {
        Piatto unicoPiatto = piatto("Unico", ingrediente(alimento("X", "150", "10", "5", "10"), "100"));
        TargetMacro target = new TargetMacro(new BigDecimal("150"), new BigDecimal("10"), new BigDecimal("5"), new BigDecimal("10"));

        PianoGenerato risultato = motore.genera(List.of(unicoPiatto), target, Set.of(unicoPiatto));

        assertEquals(unicoPiatto, risultato.piattoScelto());
    }

    @Test
    void nonScalaMaiSottoIlLimiteMinimoRealistico() {
        Piatto piatto = piatto("Piatto abbondante",
                ingrediente(alimento("Ricco", "500", "40", "20", "40"), "100"));
        // Target molto piu' basso della base (500 kcal): la scala non deve scendere sotto 0.40.
        TargetMacro target = new TargetMacro(new BigDecimal("50"), new BigDecimal("4"), new BigDecimal("2"), new BigDecimal("4"));

        PianoGenerato risultato = motore.genera(List.of(piatto), target, Set.of());

        assertEquals(new BigDecimal("40.00"), risultato.voci().get(0).quantitaGrammi());
    }

    @Test
    void nonScalaMaiSopraIlLimiteMassimoRealistico() {
        Piatto piatto = piatto("Piatto leggero",
                ingrediente(alimento("Leggero", "50", "4", "2", "4"), "100"));
        // Target molto piu' alto della base (50 kcal): la scala non deve superare 2.50.
        TargetMacro target = new TargetMacro(new BigDecimal("500"), new BigDecimal("40"), new BigDecimal("20"), new BigDecimal("40"));

        PianoGenerato risultato = motore.genera(List.of(piatto), target, Set.of());

        assertEquals(new BigDecimal("250.00"), risultato.voci().get(0).quantitaGrammi());
    }

    @Test
    void lanciaEccezioneSeNonCiSonoPiattiDisponibili() {
        TargetMacro target = new TargetMacro(new BigDecimal("500"), new BigDecimal("40"), new BigDecimal("15"), new BigDecimal("51.25"));
        assertThrows(NessunAlimentoDisponibileException.class, () -> motore.genera(List.of(), target, Set.of()));
    }

    private Piatto piatto(String nome, IngredienteRicetta... ingredienti) {
        Piatto piatto = new Piatto();
        piatto.setNome(nome);
        piatto.setIngredienti(new ArrayList<>(List.of(ingredienti)));
        return piatto;
    }

    private IngredienteRicetta ingrediente(Alimento alimento, String quantitaGrammi) {
        IngredienteRicetta ingrediente = new IngredienteRicetta();
        ingrediente.setAlimento(alimento);
        ingrediente.setQuantitaGrammi(new BigDecimal(quantitaGrammi));
        return ingrediente;
    }

    private Alimento alimento(String nome, String calorie100g, String proteineG, String grassiG, String carboidratiG) {
        Alimento a = new Alimento();
        a.setNome(nome);
        a.setFonte(FonteAlimento.PERSONALIZZATO);
        a.setCalorie100g(new BigDecimal(calorie100g));
        a.setProteineG(new BigDecimal(proteineG));
        a.setGrassiG(new BigDecimal(grassiG));
        a.setCarboidratiG(new BigDecimal(carboidratiG));
        return a;
    }
}
