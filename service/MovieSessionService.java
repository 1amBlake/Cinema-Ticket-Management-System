package com.cinema.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.MovieSessionDao;
import com.cinema.entity.MovieSession;

public class MovieSessionService {

	private final MovieSessionDao movieSessionDao;

	public MovieSessionService() {
		this(new MovieSessionDao());
	}

	public MovieSessionService(MovieSessionDao movieSessionDao) {
		if (movieSessionDao == null) {
			throw new IllegalArgumentException("movieSessionDao không được null!");
		}
		this.movieSessionDao = movieSessionDao;
	}

	public List<MovieSession> getAll() throws SQLException {
		return movieSessionDao.getAllMovieSessions();
	}

	public MovieSession findById(int movieSessionId) throws SQLException {
		return movieSessionDao.findById(movieSessionId);
	}

	public List<MovieSession> search(Integer movieId, Integer screenId, LocalDate showDate) throws SQLException {
		validateOptionalId(movieId, "movieId");
		validateOptionalId(screenId, "screenId");

		List<MovieSession> baseSessions = getBaseSessions(movieId, screenId, showDate);
		List<MovieSession> filteredSessions = new ArrayList<>();

		for (MovieSession movieSession : baseSessions) {
			if (matches(movieSession, movieId, screenId, showDate)) {
				filteredSessions.add(movieSession);
			}
		}

		return filteredSessions;
	}

	public List<MovieSession> filter(Integer movieId, Integer screenId, LocalDate showDate) throws SQLException {
		return search(movieId, screenId, showDate);
	}

	public List<MovieSession> getSessionsByMovie(int movieId) throws SQLException {
		return movieSessionDao.searchByMovieId(movieId);
	}

	public List<MovieSession> getTodaySessions() throws SQLException {
		return movieSessionDao.searchByDate(LocalDate.now());
	}

	public boolean add(MovieSession movieSession) throws SQLException {
		return movieSessionDao.addMovieSession(movieSession);
	}

	public boolean update(MovieSession movieSession) throws SQLException {
		return movieSessionDao.updateMovieSession(movieSession);
	}

	public boolean delete(int movieSessionId) throws SQLException {
		return movieSessionDao.deleteMovieSessionById(movieSessionId);
	}

	private void validateOptionalId(Integer id, String fieldName) {
		if (id != null && id <= 0) {
			throw new IllegalArgumentException(fieldName + " phai lon hon 0!");
		}
	}

	private List<MovieSession> getBaseSessions(Integer movieId, Integer screenId, LocalDate showDate) throws SQLException {
		if (movieId != null) {
			return movieSessionDao.searchByMovieId(movieId);
		}
		if (screenId != null) {
			return movieSessionDao.searchByScreenId(screenId);
		}
		if (showDate != null) {
			return movieSessionDao.searchByDate(showDate);
		}
		return movieSessionDao.getAllMovieSessions();
	}

	private boolean matches(MovieSession movieSession, Integer movieId, Integer screenId, LocalDate showDate) {
		if (movieSession == null) {
			return false;
		}

		if (movieId != null) {
			if (movieSession.getMovie() == null || movieSession.getMovie().getMovieId() != movieId) {
				return false;
			}
		}

		if (screenId != null) {
			if (movieSession.getScreen() == null || movieSession.getScreen().getScreenId() != screenId) {
				return false;
			}
		}

		if (showDate != null) {
			if (movieSession.getStartsAt() == null || !showDate.equals(movieSession.getStartsAt().toLocalDate())) {
				return false;
			}
		}

		return true;
	}
}
