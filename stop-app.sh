#!/bin/bash

echo "Stopping TradeAgent Application..."

# Stop Spring Boot backend
echo "Stopping backend..."
pkill -f "spring-boot:run"
pkill -f "tradeagent-backend"

# Stop React frontend
echo "Stopping frontend..."
pkill -f "react-scripts start"

# Clean up log files
if [ -f "backend/backend.log" ]; then
    rm backend/backend.log
fi

if [ -f "frontend/frontend.log" ]; then
    rm frontend/frontend.log
fi

echo "TradeAgent Application stopped successfully!" 