<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   QUẢN LÝ SINH VIÊN BẰNG RMI
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 1. Giới thiệu hệ thống
Ứng dụng **Quản lý Sinh viên** được xây dựng dựa trên công nghệ **Java RMI** cho phép **Client** (Java Swing) và **Server** (RMI Service) trao đổi dữ liệu qua mạng.  
- **Server** chịu trách nhiệm quản lý dữ liệu sinh viên (thêm, sửa, xóa, tìm kiếm, hiển thị danh sách).  
- **Client** cung cấp giao diện trực quan cho người dùng thực hiện các chức năng quản lý.  
- Dữ liệu có thể được lưu trữ trên **File** hoặc **Cơ sở dữ liệu (MySQL, SQLite,...)** tại Server.  

Ứng dụng phù hợp cho việc học tập, nghiên cứu lập trình mạng và phân tán trong Java.

---

## 2. Công nghệ sử dụng
- **Ngôn ngữ lập trình:** Java 8+  
- **Giao diện:** Java Swing  
- **Truyền thông mạng:** Java RMI (Remote Method Invocation)  
- **Lưu trữ dữ liệu:** 
  - Phiên bản cơ bản: ArrayList lưu tạm trong bộ nhớ
  - Phiên bản nâng cao: Lưu vào MySQL hoặc SQLite  
- **Công cụ phát triển:** IntelliJ IDEA / Eclipse / NetBeans  

---

## 3. Hình ảnh các chức năng

### 🔹 Giao diện chính (Java Swing)
- Hiển thị danh sách sinh viên
- Nút tải dữ liệu từ Server  
![Main GUI](https://via.placeholder.com/600x300.png?text=Student+Management+GUI)

### 🔹 Thêm sinh viên mới
- Nhập thông tin và gửi lên Server qua RMI  
![Add Student](https://via.placeholder.com/600x300.png?text=Add+Student+Form)

### 🔹 Quản lý dữ liệu
- Cập nhật, xóa sinh viên
- Tìm kiếm sinh viên theo ID  

*(Hình ảnh demo có thể thay bằng screenshot thực tế sau khi chạy ứng dụng)*

---

## 4. Các bước cài đặt

### 🔹 1. Cài đặt môi trường
- Cài **Java JDK 8+**  
- Cài đặt **MySQL** (nếu dùng Database)  

### 🔹 2. Biên dịch project
Mở terminal tại thư mục dự án:
```sh
javac Server/*.java Client/*.java
