package org.formation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.formation.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jdbc")
public class JdbcMovieDao implements MovieDao {
	final String SELECT_ALL_SQL = "select * from movies";

	@Autowired
	JdbcTemplate jdbcTemplate;
	RowMapper<Movie> mapper;

	public JdbcMovieDao() {
		mapper = new RowMapper<Movie>() {

			public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
				Movie movie = new Movie();
				movie.setTitle(rs.getString("title"));
				movie.setDirector(rs.getString("director"));
				movie.setDuration(rs.getInt("duration"));
				return movie;
			}
		};

	}

	public List<Movie> findAll() {

		return jdbcTemplate.query(SELECT_ALL_SQL, mapper);
	}

}
