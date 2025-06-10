# TradeAgent Deployment Guide

This guide provides step-by-step instructions for deploying and running the TradeAgent stock trading application.

## Prerequisites

Ensure you have the following installed:
- **Java 17+** (OpenJDK or Oracle JDK)
- **Node.js 16+** and npm
- **Maven 3.6+**
- **MongoDB Atlas account** (or local MongoDB instance)

## Quick Deployment Steps

### 1. Database Setup

1. **MongoDB Atlas Setup:**
   - Create a MongoDB Atlas account at https://www.mongodb.com/atlas
   - Create a new cluster
   - Create a database user with read/write permissions
   - Get your connection string

2. **Configure Database Connection:**
   ```bash
   cd backend/src/main/resources
   # Edit application.properties
   # Replace <db_username> with your actual MongoDB username
   ```

### 2. Backend Deployment

```bash
# Navigate to backend directory
cd backend

# Build the application
mvn clean package

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

**Expected Output:**
```
Mock stock data initialized successfully
Admin user created successfully
Admin credentials - Username: admin, Password: admin123
```

### 3. Frontend Deployment

```bash
# Navigate to frontend directory (in a new terminal)
cd frontend

# Install dependencies
npm install

# Start the development server
npm start
```

The frontend will start on `http://localhost:3000`

### 4. Verification

1. **Access the Application:**
   - Open your browser and navigate to `http://localhost:3000`
   - You should see the TradeAgent login page

2. **Test Login:**
   - Use admin credentials: `admin` / `admin123`
   - Or create a new trader account via signup

3. **Test Trading:**
   - Navigate to the Trading page
   - View available stocks (AAPL, GOOGL, MSFT, etc.)
   - Try buying some stocks
   - Check your portfolio and transaction history

## Production Deployment

### Backend (Spring Boot)

1. **Build Production JAR:**
   ```bash
   cd backend
   mvn clean package -DskipTests
   ```

2. **Run Production JAR:**
   ```bash
   java -jar target/tradeagent-backend-1.0.0.jar
   ```

3. **Environment Variables:**
   ```bash
   export SPRING_DATA_MONGODB_URI="your-mongodb-connection-string"
   export APP_JWT_SECRET="your-secure-jwt-secret-key"
   export SERVER_PORT=8080
   ```

### Frontend (React)

1. **Build Production Bundle:**
   ```bash
   cd frontend
   npm run build
   ```

2. **Serve Static Files:**
   ```bash
   # Using serve (install globally: npm install -g serve)
   serve -s build -l 3000
   
   # Or using any web server (nginx, apache, etc.)
   ```

## Docker Deployment (Optional)

### Backend Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/tradeagent-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Frontend Dockerfile
```dockerfile
FROM node:16-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
```

### Docker Compose
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATA_MONGODB_URI=your-mongodb-uri
      - APP_JWT_SECRET=your-jwt-secret
  
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
```

## Troubleshooting

### Common Issues

1. **Port Already in Use:**
   ```bash
   # Check what's using the port
   lsof -i :8080  # for backend
   lsof -i :3000  # for frontend
   
   # Kill the process or use different ports
   ```

2. **MongoDB Connection Issues:**
   - Verify your connection string is correct
   - Check if your IP is whitelisted in MongoDB Atlas
   - Ensure database user has proper permissions

3. **CORS Errors:**
   - Verify backend CORS configuration includes frontend URL
   - Check if both servers are running on expected ports

4. **JWT Token Issues:**
   - Ensure JWT secret is properly configured
   - Check token expiration settings

### Logs and Debugging

1. **Backend Logs:**
   ```bash
   # View application logs
   tail -f logs/spring.log
   
   # Enable debug logging
   # Add to application.properties:
   logging.level.com.tradeagent=DEBUG
   ```

2. **Frontend Logs:**
   - Open browser developer tools (F12)
   - Check Console tab for JavaScript errors
   - Check Network tab for API call failures

3. **Database Logs:**
   - Check MongoDB Atlas logs in the web interface
   - Monitor connection and query performance

## Performance Optimization

### Backend
- Enable JVM optimizations for production
- Configure connection pooling for MongoDB
- Implement caching for frequently accessed data
- Use production profiles

### Frontend
- Enable gzip compression
- Implement code splitting
- Optimize bundle size
- Use CDN for static assets

## Security Considerations

1. **Environment Variables:**
   - Never commit sensitive data to version control
   - Use environment-specific configuration files
   - Rotate JWT secrets regularly

2. **HTTPS:**
   - Use HTTPS in production
   - Configure SSL certificates
   - Update CORS settings for HTTPS URLs

3. **Database Security:**
   - Use strong database passwords
   - Enable MongoDB authentication
   - Restrict database access by IP

## Monitoring and Maintenance

1. **Health Checks:**
   - Backend: `GET /actuator/health`
   - Frontend: Monitor application availability

2. **Metrics:**
   - Monitor API response times
   - Track user activity and trading volume
   - Monitor database performance

3. **Backups:**
   - Regular MongoDB backups
   - Application configuration backups
   - User data export capabilities

## Support

For deployment issues:
1. Check the troubleshooting section above
2. Review application logs
3. Verify all prerequisites are met
4. Check network connectivity and firewall settings

## Next Steps

After successful deployment:
1. Set up monitoring and alerting
2. Configure automated backups
3. Implement CI/CD pipeline
4. Set up staging environment
5. Plan for scaling and load balancing 