package de.schauderhaft.storedprocedure;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import oracle.jdbc.OracleTypes;

@SpringBootApplication
public class StoredprocedureApplication {

	private final JdbcTemplate template;

	public StoredprocedureApplication(JdbcTemplate template) {
		this.template = template;
	}

	public static void main(String[] args) {
		SpringApplication.run(StoredprocedureApplication.class, args);
	}

	@PostConstruct
	public void run() {
		selftest();
		createStoredProcedure();
		callStoredProcedureWithOneCursorOut();
		// doesn't seem to work
		// callStoredProcedureWithTwoCursorOut();
//		callStoredFunctionWithOneCursorReturned();
	}

	private void callStoredProcedureWithOneCursorOut() {

		// cursor gets returned as ArrayList
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template).withProcedureName("callOne");

		jdbcCall.declareParameters(new SqlOutParameter("p_recordset", OracleTypes.CURSOR));


		Map<String, Object> result = jdbcCall.execute();

		Object recordset = result.get("p_recordset");

		System.out.println(recordset.getClass());
		System.out.println(recordset);
	}
	private void callStoredProcedureWithTwoCursorOut() {

		// cursor gets returned as ArrayList
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template).withProcedureName("callTwo");

		jdbcCall.declareParameters(new SqlOutParameter("rs1", OracleTypes.CURSOR));
		jdbcCall.declareParameters(new SqlOutParameter("rs2", OracleTypes.CURSOR));


		Map<String, Object> result = jdbcCall.execute();

		Object recordset = result.get("p_recordset");

		System.out.println(recordset.getClass());
		System.out.println(recordset);
	}
	private void createStoredProcedure() {

		String storedProcedureScript = readFile("callOne.sql", Charset.defaultCharset());

		template.execute(storedProcedureScript);
	}

	private void selftest() {
		System.out.println("running self test");
		String x = template.queryForObject("select * from dual", String.class);
	}

	static String readFile(String path, Charset encoding) {

		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI()));
			return new String(encoded, encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
