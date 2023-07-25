package org.formation;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile("jdbc")
@PropertySource("classpath:/jdbc.properties")
public class JdbcConfiguration {

	@Value("${jdbc.url}")
	String url;
	@Value("${jdbc.driverClassName}")
	String driverClassName;
	@Value("${jdbc.username}")
	String username;
	@Value("${jdbc.password}")
	String password;

	@Bean
	DataSource datasource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(url);
		ds.setDriverClassName(driverClassName);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(datasource());
	}
}
