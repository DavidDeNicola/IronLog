package org.ironlog.app;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test di avvio del contesto Spring.
 * Richiede un'istanza MySQL attiva e le variabili d'ambiente DB_PASS,
 * JWT_SECRET e JWT_EXPIRATION_MS: e' disabilitato di default per non far
 * fallire la build su un clone pulito. Rimuovi @Disabled per eseguirlo
 * in locale con il database avviato.
 */
@SpringBootTest
@Disabled("Richiede MySQL attivo e le variabili d'ambiente di configurazione")
class IronlogApplicationTests {

	@Test
	void contextLoads() {
	}

}
