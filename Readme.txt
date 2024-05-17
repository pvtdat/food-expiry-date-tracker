===========================================
Môi trường để chạy ứng dụng:

- IDE: Android Studio Iguana | 2023.2.1
- Máy áo Android:
	+ MinSDK: 29
	+ TargetSDK: 34
	+ Đã test trên máy ảo với API 34, release name là UpsideDownCake
- Tài khoản truy cập vào ứng dụng: 
	+ Vì đồ án sử dụng Firebase Authentication và Google Sign-In cho chức năng đăng nhập,
nên người dùng sử dụng tài khoản Google của mình để đăng nhập.

===========================================
Lưu ý:
- Ứng dụng sử dụng Firestore - một giải pháp lưu trữ đám mây trên Firebase.
- Thế nên, đối với một số thiết bị, để chạy ứng dụng trong môi trường debug, cần thiết
xác định keystore của thiết bị nhằm có quyền truy cập vào Firebase.
- Các bước xác định keystore:
	+ Mở dự án trong Android Studio
	+ Chọn File -> Project Structure -> Modules -> Signing Configs
	+ Tại đây, nhấn vào ô input của mục "Store File", chọn file chứa debug keystore
được đính kèm trong thư mục "source" và nhấn Apply.