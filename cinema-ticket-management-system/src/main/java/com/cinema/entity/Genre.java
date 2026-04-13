package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho thể loại phim trong hệ thống.
 * Chứa các thông tin cơ bản của một thể loại phim.
 *
 * @author Minh Huy (chính, ràng buộc)
 */
public class Genre {
	
	private int genreId; //Do database tự sinh
	private String genreName; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh
	
	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Genre() {
		super();
	}
	
	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param genreId - Mã thể loại phim
	 */
	public Genre(int genreId) {
		super();
		this.genreId = genreId;
	}
	
	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param genreName - Tên thể loại phim
	 */
	public Genre(String genreName) {
		super();
		setGenreName(genreName);
	}
	
	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param genreId - Mã thể loại phim
	 * @param genreName - Tên thể loại phim
	 */
	public Genre(int genreId, String genreName) {
		super();
		this.genreId = genreId;
		setGenreName(genreName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 *
	 * @param genreId - Mã thể loại phim
	 * @param genreName - Tên thể loại phim
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
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
		if (genreName == null || genreName.trim().isEmpty())
			throw new IllegalArgumentException("genreName không được để trống");
		else if (genreName.trim().length() > 100)
			throw new IllegalArgumentException("genreName không được vượt quá 100 ký tự");
		this.genreName = genreName.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính Object
	 */
	@Override
	public int hashCode() {
		return (genreId > 0) ? Integer.hashCode(genreId) : System.identityHashCode(this);
	}
	
	/**
	 * Hai Object Genre được xem là bằng nhau khi có cùng genreId hợp lệ
	 */
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
