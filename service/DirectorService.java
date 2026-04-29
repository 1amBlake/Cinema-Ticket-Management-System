package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.DirectorDao;
import com.cinema.entity.Director;

public class DirectorService {

	private final DirectorDao directorDao;

	public DirectorService() {
		this(new DirectorDao());
	}

	public DirectorService(DirectorDao directorDao) {
		if (directorDao == null) {
			throw new IllegalArgumentException("directorDao không được null!");
		}
		this.directorDao = directorDao;
	}

	public List<Director> getAll() throws SQLException {
		return directorDao.getAllDirectors();
	}

	public boolean add(Director director) throws SQLException {
		return directorDao.addDirector(director);
	}

	public boolean update(Director director) throws SQLException {
		return directorDao.updateDirector(director);
	}

	public boolean delete(int directorId) throws SQLException {
		return directorDao.deleteDirectorById(directorId);
	}

	public List<Director> searchByName(String keyword) throws SQLException {
		return directorDao.searchByName(keyword);
	}
}
