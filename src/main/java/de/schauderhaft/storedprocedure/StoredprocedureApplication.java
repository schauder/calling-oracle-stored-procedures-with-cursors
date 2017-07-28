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
		callStoredProcedureWithTwoCursorOut();
		callStoredFunctionWithOneCursorReturned();
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

	private void callStoredFunctionWithOneCursorReturned() {

		// cursor gets returned as ArrayList
		StoredProcedure returnOne = new ReturnOne(template);

		Map<String, Object> result = returnOne.execute();

		System.out.println(result);
	}

	private void callStoredProcedureWithTwoCursorOut() {

		// cursor gets returned as ArrayList
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template).withProcedureName("callTwo");

		jdbcCall.declareParameters( //
				new SqlOutParameter("rs1", OracleTypes.CURSOR), //
				new SqlOutParameter("rs2", OracleTypes.CURSOR) //
		);

		Map<String, Object> result = jdbcCall.execute();

		Object rs1 = result.get("rs1");
		Object rs2 = result.get("rs2");

		System.out.println(rs1.getClass());
		System.out.println(rs1);
		System.out.println(rs2.getClass());
		System.out.println(rs2);
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


	private static class ReturnOne extends StoredProcedure {

		ReturnOne(JdbcTemplate template) {
			super(template, "returnOne");
			setFunction(true);
			declareParameter(new SqlOutParameter("out", OracleTypes.CURSOR, new RowMapper<Object>() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					System.out.println("converting row " + rowNum);
					return "blah";
				}
			}));
			compile();

		}
	}
}
