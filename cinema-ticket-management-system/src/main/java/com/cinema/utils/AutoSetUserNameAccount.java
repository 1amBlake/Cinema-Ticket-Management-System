package com.cinema.utils;

import com.cinema.entity.Employee;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Tiện ích hỗ trợ tạo Username tự động cho nhân viên.
 * Quy tắc: [CHUCVU_INHOA][ID]@[ten_khong_dau_chu_thuong]
 * Ví dụ: QTV1@huy
 */
public class AutoSetUserNameAccount {

    /**
     * Tạo username tự động dựa trên quy định mới.
     * @param employee Đối tượng nhân viên (Lấy từ Employee Entity)
     * @return String username chuẩn hóa
     */
    public static String generateUsername(Employee employee) {
    	if (employee.getEmployeeId() <= 0) {
    	    throw new IllegalStateException("Employee ID chưa được khởi tạo");
    	}
    	
        if (employee == null || employee.getJobTitle() == null) {
            return "";
        }

        //Lấy mã chức vụ (Dựa vào getJobTitleName() trong JobTitle Entity)
        String rolePrefix = getRolePrefix(employee.getJobTitle().getJobTitleName());

        //Lấy ID nhân viên từ database (Dựa vào getEmployeeId() trong Employee Entity)
        int id = employee.getEmployeeId();

        //Xử lý tên riêng (Dựa vào getEmployeeName() trong Employee Entity)
        // Lấy tên cuối, bỏ dấu, viết thường
        String fullName = employee.getEmployeeName();
        String firstName = extractFirstName(fullName);
        String normalizedFirstName = removeAccents(firstName).toLowerCase();

        //ghép chuỗi hoàn chỉnh: QTV1@huy
        return String.format("%s%d@%s", rolePrefix, id, normalizedFirstName);
    }

    /**
     * Chuyển đổi tên chức vụ sang mã viết tắt in hoa.
     */
    private static String getRolePrefix(String jobTitleName) {
        if (jobTitleName == null) return "NV";
        
        String title = jobTitleName.trim().toLowerCase();
        
        // Kiểm tra từ khóa trong tên chức vụ để gán mã
        if (title.contains("quản trị viên")) return "QTV";
        if (title.contains("quản lý")) return "QL";
        
        return "NV"; // Mặc định cho các chức vụ khác là Nhân viên
    }

    /**
     * Trích xuất tên (từ cuối cùng trong chuỗi họ tên).
     */
    private static String extractFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "user";
        
        // Tách chuỗi dựa trên khoảng trắng và lấy phần tử cuối
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    /**
     * Loại bỏ dấu tiếng Việt và chuyển đổi ký tự đặc biệt.
     */
    private static String removeAccents(String input) {
        if (input == null) return "";
        
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(nfdNormalizedString).replaceAll("");
        
        // Xử lý riêng chữ đ/Đ vì Normalizer không tách được dấu của chữ này
        return result.replace("đ", "d").replace("Đ", "D");
    }
}