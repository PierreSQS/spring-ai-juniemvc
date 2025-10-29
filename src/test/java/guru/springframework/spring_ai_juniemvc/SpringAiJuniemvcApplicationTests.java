package guru.springframework.spring_ai_juniemvc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringAiJuniemvcApplicationTests {

	@Test
	void contextLoads(ApplicationContext appCtx) {
        assertThat(appCtx).isNotNull();
	}

}
