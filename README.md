# Quản Lý Cửa Hàng (Store Management System)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Status: Completed](https://img.shields.io/badge/Status-Completed-success.svg)]()

Hệ thống phần mềm **Quản Lý Cửa Hàng** được thiết kế nhằm tối ưu hóa quy trình vận hành, quản lý hàng hóa, nhân viên, hóa đơn và doanh thu cho các cửa hàng bán lẻ. Dự án hướng tới sự đơn giản, trực quan và dễ sử dụng cho chủ cửa hàng hoặc nhân viên thu ngân.

---

## 🚀 Tính năng chính

- **Quản lý sản phẩm & Hàng tồn kho:** 
  - Thêm, sửa, xoá và tìm kiếm thông tin sản phẩm.
  - Quản lý danh mục, số lượng tồn kho và cảnh báo hết hàng.
- **Quản lý bán hàng & Hóa đơn:**
  - Tạo hóa đơn bán hàng nhanh chóng.
  - Tự động tính tiền, tiền thừa và in hóa đơn.
- **Quản lý nhân viên & Phân quyền:**
  - Phân quyền tài khoản theo chức vụ (Quản lý, Thu ngân).
  - Quản lý thông tin và lịch làm việc của nhân viên.
- **Thống kê & Báo cáo:**
  - Thống kê doanh thu theo ngày, tháng, năm.
  - Báo cáo sản phẩm bán chạy và tình hình tài chính.

---

## 🛠️ Công nghệ sử dụng

*(Hãy điều chỉnh lại phần này cho phù hợp với tech stack thực tế của bạn)*

- **Ngôn ngữ lập trình:** Java / C# / Python / JavaScript (chọn một hoặc cập nhật)
- **Giao diện (UI):** Swing / WinForms / ReactJS / WPF
- **Cơ sở dữ liệu:** MySQL / SQL Server / SQLite

---

## 📂 Cấu trúc thư mục

```text
QuanLyCuaHang/
│
├── database/         # File script SQL hoặc cấu trúc CSDL
├── src/              # Mã nguồn chính của ứng dụng
├── assets/           # Hình ảnh, icon, tài liệu thiết kế
├── README.md         # Tài liệu hướng dẫn sử dụng
└── ...
```

---

## ⚙️ Hướng dẫn cài đặt và Chạy ứng dụng

Để chạy dự án trên máy tính cá nhân, hãy thực hiện theo các bước sau:

### 1. Yêu cầu chuẩn bị
- Đã cài đặt môi trường phù hợp (ví dụ: JDK, .NET Framework, hoặc Node.js tùy thuộc vào ngôn ngữ của bạn).
- Hệ quản trị cơ sở dữ liệu (MySQL / SQL Server).

### 2. Các bước cài đặt
1. **Clone repository về máy:**
   ```bash
   git clone https://github.com/huyphanquoc1208/QuanLyCuaHang.git
   ```
2. **Cấu hình Cơ sở dữ liệu:**
   - Mở công cụ quản lý CSDL (như SSMS, MySQL Workbench, XAMPP).
   - Tạo cơ sở dữ liệu mới và import file script SQL có sẵn trong thư mục `database/`.
3. **Cấu hình kết nối:**
   - Mở dự án bằng IDE tương ứng (IntelliJ, Visual Studio, VS Code...).
   - Cập nhật thông tin kết nối CSDL (host, username, password) trong file cấu hình (nếu có).
4. **Chạy ứng dụng:**
   - Build và chạy file khởi chạy chính của chương trình.

---

## 👥 Tác giả

- **Huy Phan Quoc** - [huyphanquoc1208](https://github.com/huyphanquoc1208)

---
