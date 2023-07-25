package org.formation.service;

import org.formation.dao.MovieDao;
import org.formation.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }
    public List<Movie> moviesDirectedBy(String director) {
        return movieDao.findAll().stream()
            .filter(m -> m.getDirector().equalsIgnoreCase(director))
            .collect(Collectors.toList());
    }
}
