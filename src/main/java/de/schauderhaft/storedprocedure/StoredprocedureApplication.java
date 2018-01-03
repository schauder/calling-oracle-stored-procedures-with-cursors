package de.schauderhaft.storedprocedure;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableJpaRepositories
public class StoredprocedureApplication {

	private final JdbcTemplate template;

	private final CallingViaJDBC callingViaJDBC;
	private final CallingViaJPA callingViaJPA;
	private final CallingViaDataJPA callingViaDataJPA;

	public StoredprocedureApplication(JdbcTemplate template, CallingViaJDBC callingViaJDBC, CallingViaJPA callingViaJPA, CallingViaDataJPA callingViaDataJPA) {

		this.template = template;
		this.callingViaJDBC = callingViaJDBC;
		this.callingViaJPA = callingViaJPA;
		this.callingViaDataJPA = callingViaDataJPA;
	}

	public static void main(String[] args) {
		SpringApplication.run(StoredprocedureApplication.class, args);
	}

	@PostConstruct
	public void run() {
		selftest();

		createStoredProcedure();

		callingViaJDBC.execute();
		callingViaJPA.execute();
		callingViaDataJPA.execute();
	}

	private void createStoredProcedure() {

		template.execute(readFile("callString.sql", Charset.defaultCharset()));
		template.execute(readFile("callOne.sql", Charset.defaultCharset()));
		template.execute(readFile("callTwo.sql", Charset.defaultCharset()));
		template.execute(readFile("returnOne.sql", Charset.defaultCharset()));
	}

	private void selftest() {

		System.out.println("running self test");
		String x = template.queryForObject("select * from dual", String.class);
	}

	private static String readFile(String path, Charset encoding) {

		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI()));
			return new String(encoded, encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
