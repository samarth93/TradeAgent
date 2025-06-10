# TradeAgent - Stock Trading Web Application

A full-stack stock trading application built with React.js frontend, Spring Boot backend, and MongoDB database.

## Tech Stack

- **Frontend**: React.js with Bootstrap UI components
- **Backend**: Spring Boot (Java) with RESTful APIs
- **Database**: MongoDB (Local installation)
- **Authentication**: JWT-based authentication with role-based access
- **Testing**: JUnit for backend, Jest for frontend

## Project Structure

```
TradeAgent/
├── frontend/          # React.js application
├── backend/           # Spring Boot application
├── start-app.sh       # Application startup script
├── stop-app.sh        # Application stop script
└── README.md         # This file
```

## Prerequisites

- Node.js (v16 or higher)
- Java 17 or higher
- Maven 3.6+
- MongoDB (installed locally)

## Quick Start

### Option 1: Using Startup Scripts (Recommended)

```bash
# Start the application
./start-app.sh

# Stop the application
./stop-app.sh
```

### Option 2: Manual Setup

#### 1. Start MongoDB
```bash
sudo systemctl start mongod
sudo systemctl enable mongod
```

#### 2. Start Backend
```bash
cd backend
mvn spring-boot:run
```
The backend will run on `http://localhost:8081`

#### 3. Start Frontend
```bash
cd frontend
npm install
npm start
```
The frontend will run on `http://localhost:3000`

## Application URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081

## Default Credentials

The application automatically creates an admin user on first startup:

- **Admin User**: 
  - Username: `admin`
  - Password: `admin123`
  - Role: Administrator with full access

- **Test Trader**: You can create trader accounts through the signup page

## Features

### Authentication & Authorization
- User signup and login with JWT tokens
- Role-based access control (Admin, Trader)
- Secure logout functionality
- Protected routes and API endpoints

### Trading Dashboard
- Real-time stock price display (mock data)
- Portfolio summary with holdings and balance
- Buy/sell stock functionality with validation
- Account balance tracking

### Portfolio Management
- View stock holdings with quantities and average prices
- Portfolio value calculation
- Transaction history with detailed records
- Performance tracking

### User Profile
- Personal information management
- Account balance display
- Role and membership information
- Security settings

### Admin Panel (Admin Only)
- User management and oversight
- Balance modification capabilities
- System statistics and monitoring
- User role management

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Get current user info

### Trading
- `GET /api/stocks` - Get available stocks
- `GET /api/stocks/{symbol}` - Get stock by symbol
- `POST /api/trades/buy` - Buy stocks
- `POST /api/trades/sell` - Sell stocks
- `GET /api/trades/history` - Get transaction history

### Portfolio
- `GET /api/portfolio` - Get user portfolio
- `GET /api/portfolio/summary` - Get portfolio summary

### Admin (Admin Only)
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/{id}` - Get user by ID
- `PUT /api/admin/users/{id}/balance` - Update user balance
- `DELETE /api/admin/users/{id}` - Delete user

## Mock Data

The application includes mock stock data for the following companies:
- Apple Inc. (AAPL)
- Alphabet Inc. (GOOGL)
- Microsoft Corporation (MSFT)
- Amazon.com Inc. (AMZN)
- Tesla Inc. (TSLA)
- Meta Platforms Inc. (META)
- NVIDIA Corporation (NVDA)
- JPMorgan Chase & Co. (JPM)
- Johnson & Johnson (JNJ)
- Visa Inc. (V)

Stock prices are randomly generated with realistic variations for simulation purposes.

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Manual Testing
1. Start the application using `./start-app.sh`
2. Navigate to `http://localhost:3000`
3. Sign up for a new account or use admin credentials
4. Test trading functionality with mock stocks
5. Verify portfolio updates and transaction history

## Security Features

- JWT token-based authentication
- Password encryption using BCrypt
- CORS configuration for cross-origin requests
- Input validation and sanitization
- Role-based access control
- Protected API endpoints

## Development

### Running in Development Mode

1. **Using Scripts**: `./start-app.sh`
2. **Manual**: Start MongoDB, then backend (`mvn spring-boot:run`), then frontend (`npm start`)
3. **Access**: Navigate to `http://localhost:3000`

### Building for Production

```bash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build
```

### Environment Configuration

The application uses the following configuration:
- Backend Port: 8081
- Frontend Port: 3000
- MongoDB: Local instance on port 27017
- JWT Secret: Configured in application.properties
- CORS: Enabled for localhost:3000

## Troubleshooting

### Common Issues

1. **Port 8080 Conflict**: The application uses port 8081 for backend to avoid conflicts with Jenkins
2. **MongoDB Connection**: Ensure MongoDB is running (`sudo systemctl status mongod`)
3. **Port Conflicts**: Make sure ports 3000 and 8081 are available
4. **CORS Errors**: Verify the backend CORS configuration includes your frontend URL
5. **JWT Errors**: Check that the JWT secret is properly configured

### Logs

- Backend logs: Check console output when running `mvn spring-boot:run`
- Frontend logs: Check browser console for React errors
- Network requests: Use browser developer tools to inspect API calls

### Process Management

```bash
# Check running processes
ps aux | grep -E "(spring-boot|react-scripts)" | grep -v grep

# Stop specific processes
pkill -f "spring-boot:run"
pkill -f "react-scripts start"
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support and questions:
- Check the troubleshooting section above
- Review the API documentation
- Create an issue in the repository 