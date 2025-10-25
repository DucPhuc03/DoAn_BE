# Hướng dẫn sử dụng API Đăng ký User - TraoDoiDo

## Tổng quan

API đăng ký user đã được hoàn thiện với JWT token authentication sử dụng thư viện Nimbus JOSE.

## API Endpoints

### 1. Đăng ký tài khoản mới

**POST** `/api/auth/register`

**Request Body:**

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Validation Rules:**

- **username**: 3-50 ký tự, không được trống
- **email**: Định dạng email hợp lệ, không được trống
- **password**: Tối thiểu 6 ký tự, không được trống
- **role**: Mặc định là "USER" (tùy chọn)

### 2. Đăng nhập

**POST** `/api/auth/login`

**Request Body:**

```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## Ví dụ sử dụng với curl

### Đăng ký user mới:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

### Test username đã tồn tại (sẽ trả về lỗi 409):

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "another@example.com",
    "password": "password123"
  }'
```

### Đăng nhập:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## Ví dụ sử dụng với JavaScript/Fetch

```javascript
// Đăng ký user
const registerUser = async (userData) => {
  try {
    const response = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    });

    const result = await response.json();
    console.log("Registration successful:", result);
    return result.accessToken;
  } catch (error) {
    console.error("Registration failed:", error);
  }
};

// Sử dụng
const userData = {
  username: "john_doe",
  email: "john@example.com",
  password: "password123",
};

registerUser(userData);
```

## Cấu trúc Database

### Bảng `users`:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER'
);
```

## Tính năng bảo mật

1. **Password Encryption**: Sử dụng BCrypt để mã hóa password
2. **JWT Token**: Token được tạo bằng Nimbus JOSE với thuật toán HS256
3. **Validation**: Sử dụng Bean Validation để kiểm tra dữ liệu đầu vào
4. **Unique Constraints**: Username và email phải duy nhất

## Xử lý lỗi

### Lỗi validation:

```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "username",
      "message": "Username must be between 3 and 50 characters"
    }
  ]
}
```

### Lỗi username đã tồn tại:

```json
{
  "timestamp": "2024-01-15T10:30:00.000+00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Username 'john_doe' đã tồn tại. Vui lòng chọn username khác."
}
```

## Cấu hình JWT

```properties
# application.properties
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000  # 24 giờ (milliseconds)
```

## Lưu ý quan trọng

1. **Secret Key**: Trong production, hãy sử dụng secret key mạnh hơn
2. **HTTPS**: Luôn sử dụng HTTPS trong production
3. **Token Storage**: Lưu trữ token an toàn ở client (localStorage, httpOnly cookies)
4. **Password Policy**: Có thể thêm validation phức tạp hơn cho password

## Testing

Bạn có thể test API bằng:

- **Postman**: Import collection và test các endpoints
- **curl**: Sử dụng các command trên
- **Frontend**: Tích hợp vào ứng dụng React/Vue/Angular
- **Unit Tests**: Viết test cases cho AuthService

## Các bước tiếp theo

1. Thêm refresh token mechanism
2. Implement logout functionality
3. Thêm role-based authorization
4. Tạo API endpoints cho các chức năng khác của ứng dụng
5. Thêm email verification cho đăng ký
6. Implement password reset functionality
