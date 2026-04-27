package com.cinema.validator;

import com.cinema.entity.Genre;

/**
 * Validator cho thực thể Genre.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Genre
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa)
 */
public final class GenreValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp GenreValidator.
     */
    private GenreValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Genre trong trường hợp thêm mới.
     *
     * @param genre - Đối tượng Genre cần kiểm tra
     */
    public static void validateForCreate(Genre genre) {
        validateCommon(genre);
    }

    /**
     * Kiểm tra dữ liệu của Genre trong trường hợp cập nhật.
     *
     * @param genre - Đối tượng Genre cần kiểm tra
     */
    public static void validateForUpdate(Genre genre) {
        validateCommon(genre);

        if (genre.getGenreId() <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Genre.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param genre - Đối tượng Genre cần kiểm tra
     */
    private static void validateCommon(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("genre không được null!");
        }

        if (genre.getGenreName() == null || genre.getGenreName().trim().isEmpty()) {
            throw new IllegalArgumentException("genreName không được để trống!");
        }

        if (genre.getGenreName().trim().length() > 100) {
            throw new IllegalArgumentException("genreName không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(genre);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Genre.
     *
     * @param genre - Đối tượng Genre cần kiểm tra
     */
    private static void validateBusinessRule(Genre genre) {
    }
}