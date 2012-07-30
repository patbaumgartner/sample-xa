package com.patbaumgartner.sample.xa;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

public class MultipleDatasourceRollbackTests extends AbstractMultipleDatasourceTests {

	@AfterTransaction
	public void checkPostConditions() {

		int count = jdbcTemplate.queryForInt("select count(*) from T_FOOS");
		// This change was rolled back by the test framework
		assertEquals(0, count);

		count = otherJdbcTemplate.queryForInt("select count(*) from T_AUDITS");
		// This rolled back as well because of the XA
		assertEquals(0, count);
	}

	@Test
	@Transactional
	public void testInsertIntoTwoDataSources() throws Exception {

		int count = jdbcTemplate.update("INSERT into T_FOOS (id,name,foo_date) values (?,?,null)", 0, "foo");
		assertEquals(1, count);

		count = otherJdbcTemplate.update("INSERT into T_AUDITS (id,operation,name,audit_date) values (?,?,?,?)", 0, "INSERT", "foo", new Date());
		assertEquals(1, count);
	}

}
