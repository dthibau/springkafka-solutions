package org.formation.dao;

import org.formation.model.Movie;

import java.util.List;

public interface MovieDao {
    public List<Movie> findAll();
}
