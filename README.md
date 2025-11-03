# 🖌️ Artify – Ứng dụng Cửa hàng Dụng cụ Mỹ thuật 🎨  

![Language](https://img.shields.io/badge/Language-Java-orange)
![Database](https://img.shields.io/badge/Database-SQLite-blue)
![UI](https://img.shields.io/badge/UI-Material%203-ff69b4)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> Ứng dụng Android giúp người dùng duyệt, mua và quản lý dụng cụ vẽ — dành cho cả người yêu nghệ thuật và cửa hàng kinh doanh nhỏ.  
> Xây dựng bằng **Java + SQLite + Material Design 3**.

---

## 🌟 Tính năng chính

| Nhóm | Tính năng |
|------|------------|
| 🛍️ **Sản phẩm** | Xem danh sách sản phẩm, tìm kiếm, lọc theo loại, sắp xếp theo giá. |
| 🔍 **Chi tiết sản phẩm** | Hiển thị mô tả, ảnh, giá, **đánh giá sao (RatingBar)**. |
| 🧺 **Giỏ hàng** | Thêm sản phẩm, tăng/giảm số lượng, tính tổng tiền, xóa sản phẩm. |
| 💳 **Thanh toán (Checkout)** | Nhập thông tin nhận hàng, xác nhận đặt đơn — lưu vào bảng `orders`. |
| 📦 **Quản lý đơn hàng** | Lưu lịch sử đơn hàng & chi tiết từng món. |
| 📈 **Doanh thu (Revenue)** | Hiển thị tổng doanh thu & danh sách giao dịch. |
| 🧑‍🎨 **Tài khoản (User)** | Đăng ký / đăng nhập cơ bản lưu local DB. |

---

## 🖼️ Giao diện chính

| Màn hình | Ảnh minh họa |
|-----------|--------------|
| 🏠 Danh sách sản phẩm | ![Product List](docs/screens/product_list.png) |
| 📄 Chi tiết sản phẩm | ![Product Detail](docs/screens/product_detail.png) |
| 🧺 Giỏ hàng | ![Cart](docs/screens/cart.png) |
| 💳 Thanh toán | ![Checkout](docs/screens/checkout.png) |
| 📈 Doanh thu | ![Revenue](docs/screens/revenue.png) |

> ⚠️ *Ảnh demo: đặt file screenshot thật của bạn vào `docs/screens/` để README hiển thị hoàn chỉnh.*

---

## 🗂️ Cấu trúc dự án

app/
├── java/com/example/artify/
│ ├── activities/ # Màn hình chính: ProductList, Detail, Cart, Checkout, Revenue…
│ ├── adapter/ # RecyclerView Adapters
│ ├── dao/ # SQLite Data Access Objects
│ ├── database/ # AppDbHelper – khởi tạo bảng, version control
│ └── models/ # Lớp dữ liệu: Product, CartItem, Order, Review…
└── res/
├── layout/ # Layout XML
├── drawable/ # Icon, background, gradient toolbar
├── values/ # colors.xml, styles.xml, strings.xml

yaml
Copy code

---

## 🧱 Cấu trúc cơ sở dữ liệu

| Bảng | Mục đích | Cột chính |
|------|-----------|-----------|
| `users` | Lưu tài khoản người dùng | id, full_name, email, password_hash |
| `products` | Danh sách sản phẩm | id, name, description, price, image_url |
| `cart_items` | Giỏ hàng hiện tại | product_id, name, price, quantity |
| `orders` | Đơn hàng đã đặt | id, customer_name, total, created_at |
| `order_items` | Chi tiết đơn hàng | order_id, product_id, quantity |
| `reviews` | Đánh giá sản phẩm | user_name, rating, comment |

---

## ⚙️ Luồng hoạt động chính

### 🧭 1. Danh sách sản phẩm
- App khởi tạo `ProductDao.seedIfEmpty()` → tạo dữ liệu mẫu nếu trống  
- Gọi `ProductDao.list()` → trả `List<Product>`  
- Gắn vào `ProductAdapter` → `RecyclerView` hiển thị danh sách.

SQLite → ProductDao.list() → ProductAdapter → RecyclerView

yaml
Copy code

---

### 🖼️ 2. Chi tiết sản phẩm
- Nhận `product_id` từ `Intent`
- Gọi `ProductDao.getById(id)`
- Hiển thị thông tin + `RatingBar` review + nút “Thêm vào giỏ hàng”.

---

### 🛒 3. Giỏ hàng & Thanh toán
- `CartDao` xử lý thêm/xóa/tăng/giảm sản phẩm.
- Tổng tiền tính bằng `CartDao.getTotal()`.
- Khi thanh toán (`CheckoutActivity`), lưu vào:
  - `orders` (thông tin đơn)
  - `order_items` (chi tiết từng sản phẩm)
- Sau khi xác nhận → `CartDao.clear()` để làm trống giỏ.

---

### 💰 4. Doanh thu
- `RevenueActivity` tổng hợp `SUM(total)` từ bảng `orders`.
- Hiển thị tổng doanh thu và lịch sử đơn hàng bằng `OrderAdapter`.

---

## 💡 Công nghệ sử dụng

| Thành phần | Công nghệ |
|-------------|------------|
| Ngôn ngữ | Java |
| Database | SQLite (native Android) |
| UI Framework | Material Components 3 |
| Thư viện hình ảnh | Glide |
| IDE | Android Studio Hedgehog+ |
| Mục tiêu | Android API 24+ (7.0 Nougat trở lên) |

---

## 🎨 Điểm nổi bật UI/UX

- 🟣 **Header Gradient** dùng `bg_toolbar_art.xml` tạo dải màu tím–hồng thương hiệu  
- 🧁 **CardView + Shadow nhẹ** tạo cảm giác mềm mại cho mỗi sản phẩm  
- ⭐ **Đánh giá RatingBar** nửa sao, màu tùy chỉnh `@color/rose_500`  
- 💬 **Snackbar + Toast** phản hồi trực quan khi thêm giỏ / thanh toán  
- 💰 **Format giá** theo locale Việt Nam (VD: `85.000 ₫`)  
- 🔁 **Reload tự động** sau khi thêm, sửa, xóa sản phẩm  

---

## 🚀 Cách chạy dự án

1. Clone dự án  
   ```bash
   git clone https://github.com/yourusername/ArtifyApp.git
   cd ArtifyApp
Mở bằng Android Studio

Chọn Build → Make Project

Nhấn Run ▶️ để chạy app trên thiết bị hoặc emulator

Lần đầu chạy, ứng dụng sẽ tự tạo dữ liệu mẫu (sản phẩm, ảnh, mô tả).

🧑‍💻 Tác giả & đóng góp
Artify Team / [Your Name]
📧 Liên hệ: your.email@example.com
🌐 Dự án phục vụ học tập Android cơ bản – SQLite + UI + RecyclerView

PRs và ý tưởng mới luôn được chào đón 💖
Hãy giúp Artify trở thành một trải nghiệm mua sắm sáng tạo hơn 🎨

📄 Giấy phép
css
Copy code
MIT License © 2025 Artify Team
Bạn được phép sao chép, chỉnh sửa, và phân phối lại cho mục đích học tập và phát triển.
🌈 Preview
“Artify – Nơi nghệ thuật bắt đầu từ cảm hứng.
Từ cọ vẽ, giấy vẽ đến màu nước – tất cả trong tầm tay bạn.”
