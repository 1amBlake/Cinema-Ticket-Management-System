package com.cinema.validator;

import java.util.regex.Pattern;

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


	private static final int MAX_GENRE_NAME_LENGTH = 100;

	private static final Pattern GENRE_NAME_PATTERN =
	        Pattern.compile("^[\\p{L}\\s()&'/-]+$");
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

        String genreName = genre.getGenreName();

        if (genreName == null || genreName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thể loại không được để trống!");
        }

        genreName = genreName.trim();

        if (genreName.length() > MAX_GENRE_NAME_LENGTH) {
            throw new IllegalArgumentException("Tên thể loại không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(genreName);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Genre.
     * 
     * Quy tắc hiện tại:
     * - Tên thể loại chỉ được chứa chữ cái, khoảng trắng,
     *   dấu ngoặc tròn, dấu &, dấu nháy đơn, dấu gạch ngang hoặc dấu gạch chéo.
     *
     * @param genreName - Tên thể loại cần kiểm tra
     */
    private static void validateBusinessRule(String genreName) {
        if (!GENRE_NAME_PATTERN.matcher(genreName).matches()) {
            throw new IllegalArgumentException(
                    "Tên thể loại chỉ được chứa chữ cái, khoảng trắng, dấu ngoặc tròn, dấu &, dấu nháy đơn, dấu gạch ngang hoặc dấu gạch chéo!"
            );
        }
    }
}

