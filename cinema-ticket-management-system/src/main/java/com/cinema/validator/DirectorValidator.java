package com.cinema.validator;

import com.cinema.entity.Director;

/**
 * Validator cho thực thể Director.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Director
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class DirectorValidator {
	
	/**
	 * Constructor private để ngăn tạo đối tượng từ lớp DirectorValidator.
	 */
	private DirectorValidator() {
	}
	
	/**
	 * Kiểm tra dữ liệu của Director trong trường hợp thêm mới.
	 *
	 * @param director - Đối tượng Director cần kiểm tra
	 */
	public static void validateForCreate(Director director) {
		validateCommon(director);
	}
	
	/**
	 * Kiểm tra dữ liệu của Director trong trường hợp cập nhật.
	 *
	 * @param director - Đối tượng Director cần kiểm tra
	 */
	public static void validateForUpdate(Director director) {
		validateCommon(director);
		
		if (director.getDirectorId() <= 0) {
			throw new IllegalArgumentException("directorId phải lớn hơn 0!");
		}
	}
	
	/**
	 * Kiểm tra các ràng buộc chung của thực thể Director.
	 *
	 * @param director - Đối tượng Director cần kiểm tra
	 */
	private static void validateCommon(Director director) {
		if (director == null) {
			throw new IllegalArgumentException("director không được null!");
		}
		
		if (director.getDirectorName() == null || director.getDirectorName().trim().isEmpty()) {
			throw new IllegalArgumentException("directorName không được rỗng!");
		}
		
		if (director.getDirectorName().trim().length() > 255) {
			throw new IllegalArgumentException("directorName không được vượt quá 255 ký tự!");
		}
	}
}