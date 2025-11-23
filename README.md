# Springdemo — Spring Boot Application with Authentication & CI/CD

## Overview
A production-ready Spring Boot application featuring:
- **User Authentication & Authorization** with Spring Security
- **Role-based Access Control** (ADMIN and USER roles)
- **H2 In-Memory Database** for development (easily switchable to MySQL/PostgreSQL)
- **RESTful API** with Spring Data JPA
- **Thymeleaf Templates** for server-side rendering
- **Spring Boot Actuator** for monitoring and health checks
- **Responsive UI** with Bootstrap 4
- **Jenkins CI/CD** ready deployment
- **WAR packaging** for Tomcat deployment

## Features

### Authentication & Security
- Login/Logout functionality
- User registration with validation
- Password encryption using BCrypt
- Session management
- CSRF protection
- Role-based access control

### User Management
- User entity with JPA
- Custom UserDetailsService
- Pre-configured demo users:
  - **Admin**: `admin` / `admin123` (ROLE_ADMIN, ROLE_USER)
  - **User**: `user` / `user123` (ROLE_USER)

### Dashboard
- Personalized user dashboard
- Role-based content display
- Activity tracking
- Quick access to system metrics

### Development Features
- H2 Console for database inspection (`/h2-console`)
- Hot reload with Spring DevTools
- Comprehensive logging
- Profile-based configuration (dev/prod)
- Global exception handling

## Tech Stack
- **Java 17**
- **Spring Boot 3.3.3**
- **Spring Security 6**
- **Spring Data JPA**
- **H2 Database** (dev) / MySQL/PostgreSQL (prod)
- **Thymeleaf** with Spring Security integration
- **Bootstrap 4.6.2**
- **Lombok** for cleaner code
- **Maven** for dependency management

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)

### Run Locally

1. **Clone and navigate to the project**
```bash
cd springdemo
```

2. **Build the project**
```bash
chmod +x ./mvnw
./mvnw clean package -DskipTests
```

3. **Run the application**
```bash
./mvnw spring-boot:run
```

4. **Access the application**
- Home: `http://localhost:8090/springdemo`
- Login: `http://localhost:8090/springdemo/login`
- Dashboard: `http://localhost:8090/springdemo/dashboard` (requires login)
- H2 Console: `http://localhost:8090/springdemo/h2-console`
  - JDBC URL: `jdbc:h2:mem:springdemo`
  - Username: `sa`
  - Password: (leave empty)

### Demo Credentials
```
Admin User:
Username: admin
Password: admin123

Regular User:
Username: user
Password: user123
```

## Project Structure
```
src/main/java/com/avizway/Springdemo/
├── config/
│   ├── SecurityConfig.java          # Spring Security configuration
│   └── DataInitializer.java         # Demo data loader
├── controller/
│   ├── Controller.java               # Home controller
│   ├── AuthController.java           # Login/Register controller
│   └── DashboardController.java      # Dashboard controller
├── model/
│   └── User.java                     # User entity
├── repository/
│   └── UserRepository.java           # User data access
├── service/
│   ├── UserService.java              # User business logic
│   └── CustomUserDetailsService.java # Spring Security integration
├── exception/
│   └── GlobalExceptionHandler.java   # Global error handling
└── SpringdemoApplication.java        # Main application class

src/main/resources/
├── templates/
│   ├── welcome.html                  # Home page
│   ├── login.html                    # Login page
│   ├── register.html                 # Registration page
│   ├── dashboard.html                # User dashboard
│   └── error.html                    # Error page
├── static/
│   ├── css/main.css                  # Custom styles
│   └── images/                       # Static images
├── application.properties            # Development config
└── application-prod.properties       # Production config
```

## Configuration

### Development Profile (default)
- H2 in-memory database
- Debug logging enabled
- H2 console enabled
- Thymeleaf cache disabled
- All actuator endpoints exposed

### Production Profile
To run with production settings:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
Or set environment variable:
```bash
export SPRING_PROFILES_ACTIVE=prod
```

### Database Configuration
For production, update `application-prod.properties`:
```properties
# MySQL Example
spring.datasource.url=jdbc:mysql://localhost:3306/springdemo
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate
```

## Deployment

### Tomcat Deployment (EC2 - Recommended)

#### Prerequisites on EC2 (Amazon Linux 2023)
```bash
# Install Java 17
sudo yum install -y java-17-amazon-corretto-headless

# Install Tomcat 9/10
sudo yum install -y tomcat
sudo systemctl enable tomcat
sudo systemctl start tomcat
```

#### Deploy WAR file
```bash
# Build the WAR
./mvnw clean package -DskipTests

# Copy to Tomcat
sudo cp target/*.war /var/lib/tomcat/webapps/springdemo.war

# Restart Tomcat
sudo systemctl restart tomcat
```

Access at: `http://your-server:8080/springdemo`

### Jenkins CI/CD Pipeline

#### Build Configuration
```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git 'your-repo-url'
            }
        }
        
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        
        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [tomcat9(
                    credentialsId: 'tomcat-credentials',
                    path: '',
                    url: 'http://your-server:8080'
                )], 
                contextPath: 'springdemo',
                war: 'target/*.war'
            }
        }
    }
}
```

#### Jenkins Plugins Required
- Maven Integration
- Deploy to container
- Git plugin

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /login` - Login page
- `POST /login` - Login processing
- `GET /register` - Registration page
- `POST /register` - User registration
- `GET /actuator/health` - Health check
- `GET /actuator/info` - Application info

### Protected Endpoints (Requires Authentication)
- `GET /dashboard` - User dashboard
- `POST /logout` - Logout

### Admin Only (Requires ROLE_ADMIN)
- `GET /h2-console` - Database console (dev only)

## Monitoring & Health Checks

### Actuator Endpoints
- Health: `http://localhost:8090/springdemo/actuator/health`
- Info: `http://localhost:8090/springdemo/actuator/info`
- Metrics: `http://localhost:8090/springdemo/actuator/metrics` (dev only)

### Logging
Logs are configured in `application.properties`:
- Application logs: `DEBUG` level
- Spring Security: `DEBUG` level
- Root: `INFO` level

## Security Features

### Implemented Security Measures
- Password encryption with BCrypt
- CSRF protection enabled
- Session management
- Remember-me functionality
- Secure headers (X-Frame-Options, etc.)
- SQL injection prevention (JPA)
- XSS protection (Thymeleaf auto-escaping)

### Security Best Practices
- Change default credentials in production
- Use HTTPS in production
- Configure proper CORS policies
- Implement rate limiting for login attempts
- Regular security updates
- Use environment variables for sensitive data

## Testing

### Run Tests
```bash
./mvnw test
```

### Run with Coverage
```bash
./mvnw clean test jacoco:report
```

## Troubleshooting

### Common Issues

**Port already in use**
```bash
# Change port in application.properties
server.port=8091
```

**Database connection issues**
- Check H2 console URL matches configuration
- Verify database credentials in properties file

**Login not working**
- Clear browser cache and cookies
- Check logs for authentication errors
- Verify user exists in database via H2 console

**404 errors after deployment**
- Ensure context path is correct
- Check Tomcat deployment logs
- Verify WAR file name matches context path

## Future Enhancements

- [ ] Email verification for registration
- [ ] Password reset functionality
- [ ] OAuth2 integration (Google, GitHub)
- [ ] API documentation with Swagger/OpenAPI
- [ ] Integration tests
- [ ] Docker containerization
- [ ] Kubernetes deployment manifests
- [ ] Redis session management
- [ ] Elasticsearch for logging
- [ ] Prometheus metrics

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License.

## Contact
**Avinash Reddy**
- For deployment help or feature requests, open an issue
- Email: admin@avizway.com

## Acknowledgments
- Spring Boot Team
- Jenkins Community
- Bootstrap Team
