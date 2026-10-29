# Phần G – Bài 9: quan hệ Student – Major

- `Majors` lưu `id`, `code`, tên tiếng Việt (`name`) và tên tiếng Anh (`name_en`).
- `Students.major_id` là khóa ngoại đến `Majors.id`; không cascade xóa ngành từ sinh viên.
- Form gửi ID ngành. Bộ lọc URL tiếp tục dùng mã, ví dụ `?major=SE`.
- Danh sách ngành, tên ngành và thống kê lấy từ database. Ngành thêm trong database tự xuất hiện trên form.

## Chạy với SQL Server hiện tại

Chạy lại ứng dụng như bình thường. Spring SQL init chạy `db/major-relation.sql` trước Hibernate:

1. Tạo bảng và bổ sung các ngành mẫu còn thiếu.
2. Tạo ngành tương ứng với mã cũ ngoài danh sách mẫu, nếu có.
3. Thêm `major_id` và điền quan hệ cho sinh viên hiện có.
4. Kiểm tra dữ liệu, đặt ràng buộc bắt buộc và khóa ngoại.

Script chạy trong transaction, có thể chạy lại và không xóa bảng hay sinh viên. Cột `major` cũ được giữ, cho phép NULL và không được ứng dụng mới sử dụng. Nếu sinh viên cũ có mã ngành rỗng hoặc NULL, migration dừng để sửa dữ liệu đó trước.

`DataInitializer` chỉ thêm 5 sinh viên mẫu khi bảng Students rỗng; chạy lại không thêm trùng ngành hay sinh viên.

## Quay lại phiên bản trước bài 9

Dừng ứng dụng, chạy `docs/major-relation-rollback.sql` trong SSMS, rồi quay lại code/cấu hình trước bài 9. Script điền lại mã ngành từ quan hệ hiện tại và cho phép `major_id` NULL để code cũ tiếp tục thêm sinh viên. Bảng Majors và các quan hệ hiện có vẫn được giữ.
