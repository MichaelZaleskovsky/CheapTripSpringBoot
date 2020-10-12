package mz.project.cheaptrip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheaptripApplicationTests {

	@LocalServerPort
	private int port;

	private static String domen;


	@Before
	public void setUp() {
		domen = "http://localhost:"+port;

	}

	@Test
	void contextLoads() {

	}

}
