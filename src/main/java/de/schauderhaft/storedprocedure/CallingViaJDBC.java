/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.schauderhaft.storedprocedure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

import oracle.jdbc.OracleTypes;

/**
 * @author Jens Schauder
 */
@Component
class CallingViaJDBC {

	private final JdbcTemplate template;

	CallingViaJDBC(JdbcTemplate template) {
		this.template = template;
	}

	void execute() {
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
