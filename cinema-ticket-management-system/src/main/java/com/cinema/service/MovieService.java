package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.MovieDao;
import com.cinema.entity.Movie;
import com.cinema.enums.MovieStatus;

public class MovieService {

	private final MovieDao movieDao;

	public MovieService() {
		this(new MovieDao());
	}

	public MovieService(MovieDao movieDao) {
		if (movieDao == null) {
			throw new IllegalArgumentException("movieDao không được null!");
		}
		this.movieDao = movieDao;
	}

	public List<Movie> getAll() throws SQLException {
		return movieDao.getAllMovies();
	}

	public Movie findById(int movieId) throws SQLException {
		return movieDao.findById(movieId);
	}

	public List<Movie> searchByName(String keyword) throws SQLException {
		return movieDao.searchByName(keyword);
	}

	public boolean add(Movie movie) throws SQLException {
		return movieDao.addMovie(movie);
	}

	public boolean update(Movie movie) throws SQLException {
		return movieDao.updateMovie(movie);
	}

	public boolean delete(int movieId) throws SQLException {
		return movieDao.deleteMovieById(movieId);
	}

	public List<Movie> getShowingMovies() throws SQLException {
		List<Movie> showingMovies = new ArrayList<>();

		for (Movie movie : movieDao.getAllMovies()) {
			if (movie != null && movie.getMovieStatus() == MovieStatus.SHOWING) {
				showingMovies.add(movie);
			}
		}

		return showingMovies;
	}
}