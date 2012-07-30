package com.patbaumgartner.sample.xa;

import java.io.File;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/xa-context.xml")
public class AbstractMultipleDatasourceTests {

	protected JdbcTemplate jdbcTemplate;
	protected JdbcTemplate otherJdbcTemplate;

	@Autowired
	public void setDataSources(@Qualifier("dataSource") DataSource dataSource, @Qualifier("otherDataSource") DataSource otherDataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.otherJdbcTemplate = new JdbcTemplate(otherDataSource);
	}

	@BeforeClass
	@AfterClass
	public static void clearLog() {
		// Ensure that Atomikos logging directory exists
		File dir = new File("atomikos");
		if (!dir.exists()) {
			dir.mkdir();
		}
		// ...and delete any stale locks (this would be a sign of a crash)
		File tmlog = new File("atomikos/tmlog.lck");
		if (tmlog.exists()) {
			tmlog.delete();
		}
	}

}
