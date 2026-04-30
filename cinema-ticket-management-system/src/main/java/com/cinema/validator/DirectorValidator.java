package com.cinema.validator;

import java.util.regex.Pattern;

import com.cinema.entity.Director;

/**
 * Validator cho thực thể Director.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Director
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class DirectorValidator {

    private static final int MAX_DIRECTOR_NAME_LENGTH = 255;

    private static final Pattern DIRECTOR_NAME_PATTERN =
            Pattern.compile("^[\\p{L} .'-]+$");

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

        String directorName = director.getDirectorName();

        if (directorName == null || directorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đạo diễn không được để trống!");
        }

        directorName = directorName.trim();

        if (directorName.length() > MAX_DIRECTOR_NAME_LENGTH) {
            throw new IllegalArgumentException("Tên đạo diễn không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(directorName);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Director.
     * 
     * Quy tắc hiện tại:
     * - Tên đạo diễn chỉ được chứa chữ cái, khoảng trắng, dấu chấm,
     *   dấu nháy đơn hoặc dấu gạch nối.
     *
     * @param directorName - Tên đạo diễn cần kiểm tra
     */
    private static void validateBusinessRule(String directorName) {
        if (!DIRECTOR_NAME_PATTERN.matcher(directorName).matches()) {
            throw new IllegalArgumentException(
                    "Tên đạo diễn chỉ được chứa chữ cái, khoảng trắng, dấu chấm, dấu nháy đơn hoặc dấu gạch nối!"
            );
        }
    }
}