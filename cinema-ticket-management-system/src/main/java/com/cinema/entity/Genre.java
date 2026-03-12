package com.cinema.entity; //TODO: chưa làm validate

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đại diện cho thể loại phim trong hệ thống.
 * Chứa các thông tin cơ bản của một thể loại phim.
 *
 * @author minhhuy
 */
public class Genre {
	private int genreId; //mã thể loại
	private String genreName; //tên thể loại
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc định, không truyền dữ liệu
	 */
	public Genre() {
		super();
	}
	
	/**
	 * Constructor dùng để tạo một thể loại mới
	 * Dùng cho SQL tự động gia tăng Id
	 * @param genreName tên thể loại
	 */
	public Genre(String genreName) {
		super();
		setGenreName(genreName);
	}
	
	/**
	 * Constructor với các dữ liệu bắt buộc
	 * @param genreId mã thể loại
	 * @param genreName tên thể loại
	 */
	public Genre(int genreId, String genreName) {
		super();
		this.genreId = genreId;
		setGenreName(genreName);
	}

	/**
	 * Constructor khởi tạo đầy đủ thông tin thể loại.
	 *
	 * @param genreId mã thể loại
	 * @param genreName tên thể loại
	 * @param createdAt thời điểm tạo dữ liệu
	 * @param updatedAt thời điểm cập nhật dữ liệu
	 */
	public Genre(int genreId, String genreName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.genreId = genreId;
		setGenreName(genreName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getGenreId() {
		return genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	/**
	 * Trả về hashcode dựa trên genreId
	 */
	@Override
	public int hashCode() {
		return Objects.hash(genreId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (this.genreId <= 0 || other.genreId <= 0)
			return false;
		return genreId == other.genreId;
	}

	@Override
	public String toString() {
		return "Genre [genreId=" + genreId + ", genreName=" + genreName + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
	
}
