package com.cinema.validator;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.cinema.entity.Movie;
import com.cinema.enums.MovieStatus;

/**
 * Validator cho thực thể Movie.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Movie
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class MovieValidator {

    private static final int MAX_MOVIE_NAME_LENGTH = 255;
    private static final int MAX_MOVIE_LANGUAGE_LENGTH = 100;
    private static final int MAX_PICTURE_URL_LENGTH = 255;
    private static final int MAX_MOVIE_DURATION = 600;
    private static final int MIN_RELEASE_YEAR = 1800;
    private static final int MAX_RELEASE_YEAR_OFFSET = 2;
    private static final int MAX_SHOWING_RELEASE_FUTURE_DAYS = 7;

    private static final Pattern MOVIE_NAME_PATTERN =
            Pattern.compile("^[\\p{L}\\p{N}\\s:'’.,!?&()\\-+/]+$");

    private static final Pattern MOVIE_LANGUAGE_PATTERN =
            Pattern.compile("^[\\p{L}\\s]+$");

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp MovieValidator.
     */
    private MovieValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Movie trong trường hợp thêm mới.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    public static void validateForCreate(Movie movie) {
        validateCommon(movie);
    }

    /**
     * Kiểm tra dữ liệu của Movie trong trường hợp cập nhật.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    public static void validateForUpdate(Movie movie) {
        validateCommon(movie);

        if (movie.getMovieId() <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Movie.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    private static void validateCommon(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("movie không được null!");
        }

        String movieName = movie.getMovieName();

        if (movieName == null || movieName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phim không được để trống!");
        }

        movieName = movieName.trim();

        if (movieName.length() > MAX_MOVIE_NAME_LENGTH) {
            throw new IllegalArgumentException("Tên phim không được vượt quá 255 ký tự!");
        }

        if (movie.getMovieDuration() <= 0 || movie.getMovieDuration() > MAX_MOVIE_DURATION) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0 và không vượt quá 600 phút!");
        }

        if (movie.getMovieReleaseDate() != null
                && (movie.getMovieReleaseDate().getYear() < MIN_RELEASE_YEAR
                || movie.getMovieReleaseDate().getYear()
                        > LocalDate.now().getYear() + MAX_RELEASE_YEAR_OFFSET)) {
            throw new IllegalArgumentException("Ngày phát hành phim không hợp lệ!");
        }

        if (movie.getMovieStatus() == null) {
            throw new IllegalArgumentException("Trạng thái phim không được null!");
        }

        if (movie.getMovieAgeRating() == null) {
            throw new IllegalArgumentException("Giới hạn tuổi của phim không được null!");
        }

        if (movie.getMovieLanguage() != null
                && movie.getMovieLanguage().trim().length() > MAX_MOVIE_LANGUAGE_LENGTH) {
            throw new IllegalArgumentException("Ngôn ngữ phim không được vượt quá 100 ký tự!");
        }

        if (movie.getPictureUrl() != null
                && movie.getPictureUrl().trim().length() > MAX_PICTURE_URL_LENGTH) {
            throw new IllegalArgumentException("Đường dẫn ảnh phim không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(movie);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Movie.
     * 
     * Quy tắc hiện tại:
     * - Tên phim chỉ được chứa chữ cái, số, khoảng trắng và một số ký tự
     *   phổ biến thường xuất hiện trong tên phim.
     * - Ngôn ngữ phim là ngôn ngữ gốc của phim, chỉ cho phép một ngôn ngữ,
     *   ví dụ: "Tiếng Việt", "Tiếng Đức", "English".
     * - Phim đang chiếu không nên có ngày phát hành quá xa trong tương lai.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    private static void validateBusinessRule(Movie movie) {
        String movieName = movie.getMovieName().trim();

        if (!MOVIE_NAME_PATTERN.matcher(movieName).matches()) {
            throw new IllegalArgumentException(
                    "Tên phim chỉ được chứa chữ cái, số, khoảng trắng và các ký tự phổ biến như :, ', ., ,, !, ?, &, (), -, +, /!"
            );
        }

        if (movie.getMovieLanguage() != null && !movie.getMovieLanguage().trim().isEmpty()) {
            String movieLanguage = movie.getMovieLanguage().trim();

            if (!MOVIE_LANGUAGE_PATTERN.matcher(movieLanguage).matches()) {
                throw new IllegalArgumentException(
                        "Ngôn ngữ phim chỉ được chứa chữ cái và khoảng trắng, ví dụ: Tiếng Việt, Tiếng Đức, English!"
                );
            }
        }

        if (movie.getMovieStatus() == MovieStatus.SHOWING
                && movie.getMovieReleaseDate() != null
                && movie.getMovieReleaseDate().isAfter(
                        LocalDate.now().plusDays(MAX_SHOWING_RELEASE_FUTURE_DAYS))) {
            throw new IllegalArgumentException(
                    "Phim đang chiếu không nên có ngày phát hành quá xa trong tương lai!"
            );
        }
    }
}