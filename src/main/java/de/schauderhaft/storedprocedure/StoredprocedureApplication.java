package de.schauderhaft.storedprocedure;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;

import oracle.jdbc.OracleTypes;

@SpringBootApplication
public class StoredprocedureApplication {

	private final JdbcTemplate template;

	private final CallingViaJDBC callingViaJDBC;

	public StoredprocedureApplication(JdbcTemplate template) {
		this.template = template;
		this.callingViaJDBC = new CallingViaJDBC(template);
	}

	public static void main(String[] args) {
		SpringApplication.run(StoredprocedureApplication.class, args);
	}

	@PostConstruct
	public void run() {
		selftest();

		createStoredProcedure();

		callingViaJDBC.execute();
	}

	private void createStoredProcedure() {

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
