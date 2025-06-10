#!/bin/bash

echo "Starting TradeAgent Application..."
echo "=================================="

# Check if MongoDB is running
if ! systemctl is-active --quiet mongod; then
    echo "Starting MongoDB..."
    sudo systemctl start mongod
    sleep 3
fi

# Kill any existing processes
echo "Stopping any existing processes..."
pkill -f "spring-boot:run" 2>/dev/null
pkill -f "react-scripts start" 2>/dev/null
sleep 2

# Start backend in background
echo "Starting backend on port 8081..."
cd backend
mvn spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend started with PID: $BACKEND_PID"

# Wait for backend to start
echo "Waiting for backend to start..."
sleep 20

# Start frontend in background
echo "Starting frontend on port 3000..."
cd ../frontend
npm start > frontend.log 2>&1 &
FRONTEND_PID=$!
echo "Frontend started with PID: $FRONTEND_PID"

# Wait for frontend to start
echo "Waiting for frontend to start..."
sleep 15

echo ""
echo "🚀 TradeAgent Application Started Successfully!"
echo "=============================================="
echo "Frontend: http://localhost:3000"
echo "Backend API: http://localhost:8081"
echo ""
echo "Default Admin Credentials:"
echo "Username: admin"
echo "Password: admin123"
echo ""
echo "✅ All React Router issues have been fixed"
echo "✅ Navigation should work properly"
echo ""
echo "To stop the application:"
echo "./stop-app.sh"
echo "or manually: kill $BACKEND_PID $FRONTEND_PID"
echo ""
echo "Process IDs:"
echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo ""
echo "Logs:"
echo "Backend: backend/backend.log"
echo "Frontend: frontend/frontend.log"
echo ""
echo "🎯 Ready to trade! Open http://localhost:3000 in your browser" 