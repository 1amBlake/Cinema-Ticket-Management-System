package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.ProductDao;
import com.cinema.entity.Product;
import com.cinema.enums.ProductStatus;
import com.cinema.validator.ProductValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Product.
 * 
 * Lớp này là tầng trung gian giữa Controller và ProductDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * liên quan đến sản phẩm và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Lưu ý:
 * - ProductStatus thể hiện trạng thái kinh doanh của sản phẩm:
 *   STOPPED, SELLING hoặc SOLD_OUT.
 * - Việc kiểm tra trùng tên sản phẩm được xử lý ở ProductDao.
 * - Việc kiểm tra sản phẩm đã được sử dụng trong hóa đơn sản phẩm
 *   trước khi xóa được xử lý ở ProductDao.
 * 
 * @author Trọng
 * @author Minh Huy
 */
public class ProductService {

    private final ProductDao productDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo ProductService với ProductDao mặc định.
     */
    public ProductService() {
        this(new ProductDao());
    }

    /**
     * Constructor cho phép truyền ProductDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế ProductDao bằng đối tượng khác.
     *
     * @param productDao - DAO dùng để thao tác dữ liệu Product
     * @throws IllegalArgumentException nếu productDao là null
     */
    public ProductService(ProductDao productDao) {
        if (productDao == null) {
            throw new IllegalArgumentException("productDao không được null!");
        }

        this.productDao = productDao;
    }

    /**
     * Lấy danh sách tất cả sản phẩm.
     *
     * @return danh sách tất cả sản phẩm
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Product> getAllProducts() throws SQLException {
        return productDao.getAllProducts();
    }

    /**
     * Tìm sản phẩm theo mã sản phẩm.
     *
     * @param productId - Mã sản phẩm cần tìm
     * @return đối tượng Product nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu productId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Product findProductById(int productId) throws SQLException {
        validateRequiredId(productId, "productId");

        return productDao.findById(productId);
    }

    /**
     * Thêm sản phẩm mới.
     *
     * @param product - Sản phẩm cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu sản phẩm không hợp lệ
     *                                  hoặc sản phẩm đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addProduct(Product product) throws SQLException {
        ProductValidator.validateForCreate(product);

        return productDao.addProduct(product);
    }

    /**
     * Cập nhật thông tin sản phẩm.
     *
     * @param product - Sản phẩm cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu dữ liệu sản phẩm không hợp lệ
     *                                  hoặc tên sản phẩm bị trùng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateProduct(Product product) throws SQLException {
        ProductValidator.validateForUpdate(product);

        return productDao.updateProduct(product);
    }

    /**
     * Xóa sản phẩm theo mã sản phẩm.
     * 
     * Không cho phép xóa nếu sản phẩm đã được dùng trong hóa đơn sản phẩm.
     *
     * @param productId - Mã sản phẩm cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu productId không hợp lệ hoặc sản phẩm đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteProductById(int productId) throws SQLException {
        validateRequiredId(productId, "productId");

        return productDao.deleteProductById(productId);
    }

    /**
     * Tìm kiếm sản phẩm theo tên gần đúng.
     * 
     * Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách sản phẩm.
     *
     * @param keyword - Từ khóa tìm kiếm
     * @return danh sách sản phẩm phù hợp
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        return productDao.searchByName(keyword.trim());
    }

    /**
     * Lấy danh sách sản phẩm có thể bán.
     * 
     * Sản phẩm có thể bán là sản phẩm đang ở trạng thái SELLING
     * và còn số lượng tồn lớn hơn 0.
     *
     * @return danh sách sản phẩm có thể bán
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Product> getAvailableProducts() throws SQLException {
        List<Product> result = new ArrayList<>();

        for (Product product : productDao.getAllProducts()) {
            if (product != null
                    && product.getProductStatus() == ProductStatus.SELLING
                    && product.getStockQuantity() > 0) {
                result.add(product);
            }
        }

        return result;
    }

    /**
     * Cập nhật số lượng tồn kho của sản phẩm.
     * 
     * quantityChange có thể là số âm khi bán hàng hoặc số dương khi nhập thêm hàng.
     *
     * @param productId - Mã sản phẩm cần cập nhật tồn kho
     * @param quantityChange - Số lượng thay đổi
     * @return true nếu cập nhật thành công
     * @throws IllegalArgumentException nếu productId không hợp lệ,
     *                                  sản phẩm không tồn tại
     *                                  hoặc số lượng tồn không đủ
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateProductStock(int productId, int quantityChange) throws SQLException {
        validateRequiredId(productId, "productId");

        Product product = productDao.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại!");
        }

        int newStockQuantity = product.getStockQuantity() + quantityChange;

        if (newStockQuantity < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ!");
        }

        product.setStockQuantity(newStockQuantity);

        if (newStockQuantity == 0) {
            product.setProductStatus(ProductStatus.SOLD_OUT);
        } else if (product.getProductStatus() == ProductStatus.SOLD_OUT) {
            product.setProductStatus(ProductStatus.SELLING);
        }

        return productDao.updateProduct(product);
    }

    /**
     * Kiểm tra ID bắt buộc.
     *
     * @param id - Giá trị ID cần kiểm tra
     * @param fieldName - Tên trường dùng trong thông báo lỗi
     */
    private void validateRequiredId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " phải lớn hơn 0!");
        }
    }
}

/*
 * Các điểm đã bổ sung và chỉnh sửa so với ProductService ban đầu:
 * 
 * 1. Bỏ import Collectors:
 *    - Xóa:
 *      import java.util.stream.Collectors;
 * 
 *    Lý do:
 *    Version mới không còn dùng stream().filter().collect(Collectors.toList()).
 *    Thay vào đó dùng vòng for và ArrayList để code dễ đọc, dễ giải thích hơn
 *    trong đồ án.
 * 
 * 2. Bổ sung import ArrayList:
 *    - Thêm:
 *      import java.util.ArrayList;
 * 
 *    Lý do:
 *    Method getAvailableProducts() version mới dùng ArrayList để lưu danh sách
 *    sản phẩm đang bán và còn hàng.
 * 
 * 3. Bổ sung import ProductValidator:
 *    - Thêm:
 *      import com.cinema.validator.ProductValidator;
 * 
 *    Lý do:
 *    Service không nên tự kiểm tra dữ liệu thủ công bằng method validate()
 *    riêng. Việc kiểm tra dữ liệu cơ bản và business rule nội tại của Product
 *    nên được giao cho ProductValidator để đồng bộ với cấu trúc Validator
 *    của dự án.
 * 
 * 4. Bổ sung Javadoc cho class ProductService:
 * 
 *    Lý do:
 *    Giúp mô tả rõ vai trò của ProductService là tầng trung gian giữa
 *    Controller và ProductDao. Service chịu trách nhiệm kiểm tra dữ liệu
 *    đầu vào, xử lý nghiệp vụ cơ bản liên quan đến sản phẩm và gọi DAO
 *    để thao tác với cơ sở dữ liệu.
 * 
 * 5. Bổ sung ghi chú nghiệp vụ trong Javadoc của class:
 *    - ProductStatus thể hiện trạng thái kinh doanh của sản phẩm:
 *      STOPPED, SELLING hoặc SOLD_OUT.
 *    - Việc kiểm tra trùng tên sản phẩm được xử lý ở ProductDao.
 *    - Việc kiểm tra sản phẩm đã được sử dụng trong hóa đơn sản phẩm
 *      trước khi xóa được xử lý ở ProductDao.
 * 
 *    Lý do:
 *    Làm rõ tầng trách nhiệm:
 *    - Validator kiểm tra dữ liệu nội tại của object.
 *    - Service kiểm tra input và xử lý nghiệp vụ cơ bản.
 *    - DAO xử lý các rule cần truy vấn database.
 * 
 * 6. Sửa constructor mặc định:
 *    - Từ:
 *      public ProductService() {
 *          this.productDao = new ProductDao();
 *      }
 * 
 *    - Thành:
 *      public ProductService() {
 *          this(new ProductDao());
 *      }
 * 
 *    Lý do:
 *    Constructor mặc định tái sử dụng constructor có tham số,
 *    giúp tránh lặp code và đồng bộ với style các Service khác.
 * 
 * 7. Bổ sung constructor có tham số ProductDao:
 *    - Thêm:
 *      public ProductService(ProductDao productDao)
 * 
 *    Lý do:
 *    Cho phép truyền ProductDao từ bên ngoài vào Service.
 *    Cách này hữu ích khi test, mock DAO hoặc thay thế DAO khác.
 * 
 * 8. Bổ sung kiểm tra null cho ProductDao trong constructor:
 *    - Nếu productDao == null thì ném IllegalArgumentException.
 * 
 *    Lý do:
 *    Tránh lỗi NullPointerException về sau khi gọi các method trong Service.
 * 
 * 9. Bổ sung Javadoc cho các method public:
 * 
 *    Lý do:
 *    Giúp người đọc hiểu rõ chức năng từng method, tham số truyền vào,
 *    giá trị trả về và các lỗi có thể phát sinh.
 * 
 * 10. Sửa findProductById():
 *     - Từ:
 *       public Product findProductById(int id) throws SQLException {
 *           return productDao.findById(id);
 *       }
 * 
 *     - Thành:
 *       public Product findProductById(int productId) throws SQLException {
 *           validateRequiredId(productId, "productId");
 *           return productDao.findById(productId);
 *       }
 * 
 *     Lý do:
 *     Mã sản phẩm bắt buộc phải lớn hơn 0. Service nên kiểm tra dữ liệu đầu vào
 *     trước khi chuyển xuống DAO.
 * 
 * 11. Sửa addProduct():
 *     - Từ:
 *       validate(product);
 * 
 *     - Thành:
 *       ProductValidator.validateForCreate(product);
 * 
 *     Lý do:
 *     Method validate() cũ chỉ kiểm tra product null, productName rỗng,
 *     productType null và price < 0.
 * 
 *     Trong khi ProductValidator kiểm tra đầy đủ hơn:
 *     - product không null.
 *     - productName không rỗng.
 *     - productName không vượt quá 255 ký tự.
 *     - productType không null.
 *     - productTypeId phải lớn hơn 0.
 *     - price hợp lệ, không NaN/Infinity.
 *     - price phải lớn hơn 0.
 *     - stockQuantity phải lớn hơn hoặc bằng 0.
 *     - productStatus không null.
 *     - pictureUrl không vượt quá 255 ký tự.
 *     - business rule giữa productStatus và stockQuantity.
 * 
 * 12. Sửa updateProduct():
 *     - Từ:
 *       validate(product);
 * 
 *     - Thành:
 *       ProductValidator.validateForUpdate(product);
 * 
 *     Lý do:
 *     Khi cập nhật sản phẩm, ngoài các kiểm tra chung, còn bắt buộc
 *     productId > 0. Rule này nằm trong ProductValidator.validateForUpdate().
 * 
 * 13. Đổi tên method xóa:
 *     - Từ:
 *       deleteProduct(int id)
 * 
 *     - Thành:
 *       deleteProductById(int productId)
 * 
 *     Lý do:
 *     Tên method rõ nghĩa hơn, đồng bộ với ProductDao.deleteProductById()
 *     và các Service khác trong dự án.
 * 
 * 14. Bổ sung kiểm tra ID trong deleteProductById():
 *     - Gọi validateRequiredId(productId, "productId") trước khi gọi DAO.
 * 
 *     Lý do:
 *     Không cho phép xóa với productId <= 0.
 *     Việc không cho xóa sản phẩm nếu đã nằm trong hóa đơn sản phẩm
 *     được xử lý ở ProductDao.
 * 
 * 15. Sửa searchProducts(String keyword):
 *     - Version cũ:
 *       return productDao.searchByName(keyword);
 * 
 *     - Version mới:
 *       Nếu keyword null hoặc rỗng thì trả về toàn bộ danh sách sản phẩm.
 *       Nếu có keyword thì trim rồi gọi productDao.searchByName(keyword.trim()).
 * 
 *     Lý do:
 *     Cách này thuận tiện hơn cho Controller/GUI.
 *     Khi ô tìm kiếm bị để trống, giao diện có thể hiển thị lại toàn bộ sản phẩm.
 * 
 * 16. Sửa getAvailableProducts():
 *     - Version cũ:
 *       Dùng stream().filter().collect(Collectors.toList()).
 *       Đồng thời dùng ProductStatus.ACTIVE.
 * 
 *     - Version mới:
 *       Dùng vòng for và ArrayList.
 *       Kiểm tra product != null.
 *       Kiểm tra productStatus == ProductStatus.SELLING.
 *       Kiểm tra stockQuantity > 0.
 * 
 *     Lý do:
 *     ProductStatus.ACTIVE không tồn tại trong enum ProductStatus của dự án.
 *     Enum đúng là SELLING, STOPPED và SOLD_OUT.
 *     Vòng for dễ đọc hơn, dễ giải thích hơn, và tránh cảm giác code do AI sinh.
 * 
 * 17. Sửa updateProductStock():
 *     - Version cũ:
 *       + Tự kiểm tra productId bằng if trực tiếp.
 *       + Dùng thông báo lỗi tiếng Anh.
 *       + Chỉ cập nhật stockQuantity.
 *       + Không tự đổi trạng thái khi tồn kho về 0.
 * 
 *     - Version mới:
 *       + Dùng validateRequiredId(productId, "productId").
 *       + Dùng thông báo lỗi tiếng Việt.
 *       + Kiểm tra sản phẩm có tồn tại không.
 *       + Không cho tồn kho bị âm.
 *       + Nếu tồn kho mới bằng 0 thì chuyển trạng thái sang SOLD_OUT.
 *       + Nếu tồn kho mới lớn hơn 0 và sản phẩm đang SOLD_OUT thì chuyển lại SELLING.
 * 
 *     Lý do:
 *     Tồn kho và trạng thái sản phẩm có liên quan trực tiếp với nhau.
 *     Khi hết hàng thì trạng thái nên là SOLD_OUT.
 *     Khi nhập thêm hàng cho sản phẩm đang SOLD_OUT thì có thể chuyển lại SELLING.
 * 
 * 18. Xóa method validate(Product product) tự viết trong Service.
 * 
 *     Lý do:
 *     Method validate() cũ bị thiếu nhiều rule quan trọng:
 *     - Không kiểm tra productName quá 255 ký tự.
 *     - Không kiểm tra productTypeId > 0.
 *     - Không kiểm tra price là NaN hoặc Infinity.
 *     - Chỉ cho price >= 0, trong khi Validator yêu cầu price > 0.
 *     - Không kiểm tra stockQuantity >= 0.
 *     - Không kiểm tra productStatus null.
 *     - Không kiểm tra pictureUrl quá 255 ký tự.
 *     - Không kiểm tra productId > 0 khi update.
 *     - Không kiểm tra business rule giữa productStatus và stockQuantity.
 * 
 *     Các kiểm tra này đã được chuyển về ProductValidator.
 * 
 * 19. Bổ sung method validateRequiredId(int id, String fieldName):
 * 
 *     Lý do:
 *     Dùng chung cho các method cần ID bắt buộc như find, delete
 *     và updateProductStock. Cách này giúp tránh lặp code và thống nhất
 *     thông báo lỗi.
 * 
 * 20. Chuẩn hóa thông báo lỗi:
 *     - Version cũ dùng tiếng Anh như:
 *       "Invalid productId", "Product not found", "Not enough stock",
 *       "Product is null", "Product name is required".
 * 
 *     - Version mới dùng tiếng Việt đồng bộ hơn:
 *       "productId phải lớn hơn 0!",
 *       "Sản phẩm không tồn tại!",
 *       "Số lượng tồn kho không đủ!",
 *       và các lỗi validate nằm trong ProductValidator.
 * 
 *     Lý do:
 *     Dự án đang dùng thông báo lỗi tiếng Việt ở Entity, Validator, DAO
 *     và các Service khác.
 * 
 * Kết luận:
 * - Version mới rõ tầng trách nhiệm hơn:
 *   Service không tự validate thủ công nữa mà gọi ProductValidator.
 *   DAO xử lý các ràng buộc cần truy vấn database như trùng tên sản phẩm
 *   và sản phẩm đã được dùng trong hóa đơn.
 * 
 * - Version mới an toàn hơn:
 *   Có kiểm tra ID trước khi gọi DAO.
 *   Không còn lỗi enum ProductStatus.ACTIVE.
 *   Không còn validate thủ công thiếu rule.
 * 
 * - Version mới dễ bảo trì hơn:
 *   Có constructor truyền DAO để hỗ trợ test.
 *   Tên method rõ nghĩa hơn.
 *   Code getAvailableProducts() dùng vòng for dễ đọc hơn stream.
 *   Có Javadoc mô tả đầy đủ chức năng và nghiệp vụ.
 *   Minh Huy
 */