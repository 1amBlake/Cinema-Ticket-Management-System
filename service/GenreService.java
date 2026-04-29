package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.GenreDao;
import com.cinema.entity.Genre;

public class GenreService {

	private final GenreDao genreDao;

	public GenreService() {
		this(new GenreDao());
	}

	public GenreService(GenreDao genreDao) {
		if (genreDao == null) {
			throw new IllegalArgumentException("genreDao không được null!");
		}
		this.genreDao = genreDao;
	}

	public List<Genre> getAll() throws SQLException {
		return genreDao.getAllGenres();
	}

	public boolean add(Genre genre) throws SQLException {
		return genreDao.addGenre(genre);
	}

	public boolean update(Genre genre) throws SQLException {
		return genreDao.updateGenre(genre);
	}

	public boolean delete(int genreId) throws SQLException {
		return genreDao.deleteGenreById(genreId);
	}

	public List<Genre> searchByName(String keyword) throws SQLException {
		return genreDao.searchByName(keyword);
	}
}
