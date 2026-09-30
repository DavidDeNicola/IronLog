package org.ironlog.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test di avvio del contesto Spring.
 * Usa il profilo "test" (H2 in memoria), quindi non richiede MySQL
 * ne' variabili d'ambiente: gira in locale e su GitHub Actions.
 */
@SpringBootTest
@ActiveProfiles("test")
class IronlogApplicationTests {

	@Test
	void contextLoads() {
	}

}