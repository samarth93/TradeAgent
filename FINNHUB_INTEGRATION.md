# Finnhub API Integration - TradeAgent

## 🚀 Real-Time Stock Data Integration

TradeAgent now uses **Finnhub.io** API to provide real-time stock market data instead of mock data. This integration brings live market prices, daily highs/lows, and other essential trading information directly into the application.

## 🔑 API Configuration

### API Key Setup
- **API Key:** `d0ti6i1r01qlvahca0v0d0ti6i1r01qlvahca0vg`
- **Base URL:** `https://finnhub.io/api/v1`
- **Configuration File:** `backend/src/main/resources/application.properties`

```properties
# Finnhub API Configuration
finnhub.api.key=d0ti6i1r01qlvahca0v0d0ti6i1r01qlvahca0vg
finnhub.api.base-url=https://finnhub.io/api/v1
```

## 🏗️ Architecture Changes

### Backend Components

#### 1. FinnhubService (`backend/src/main/java/com/tradeagent/service/FinnhubService.java`)
- **Purpose:** Handles all API calls to Finnhub.io
- **Key Methods:**
  - `getStockQuote(String symbol)` - Fetches real-time stock quotes
  - `getCompanyProfile(String symbol)` - Retrieves company information
- **Features:**
  - Error handling with fallback data
  - Automatic retry logic
  - Data validation and formatting
  - Logging for monitoring API calls

#### 2. Enhanced StockService (`backend/src/main/java/com/tradeagent/service/StockService.java`)
- **Integration:** Uses FinnhubService for real-time data
- **Fallback:** Maintains mock data if API fails
- **Auto-refresh:** Updates stock prices automatically
- **New Features:**
  - Real-time price updates
  - Day high/low tracking
  - Opening price data
  - Enhanced error handling

#### 3. Updated Stock Model (`backend/src/main/java/com/tradeagent/model/Stock.java`)
- **New Fields:**
  - `openPrice` - Stock opening price
  - `dayHigh` - Highest price of the day
  - `dayLow` - Lowest price of the day
- **Enhanced:** Better data representation for trading decisions

### Frontend Enhancements

#### 1. Enhanced Trading Component (`frontend/src/components/Trading.js`)
- **Real-time Display:** Shows live market data
- **Auto-refresh:** Updates every 30 seconds
- **Enhanced UI:**
  - Day high/low columns
  - Opening price information
  - Real-time data badge
  - Manual refresh button
- **Improved Modal:** Detailed stock information in trading modal

## 📊 Data Points Available

### Stock Quote Data
- **Current Price** - Live market price
- **Previous Close** - Yesterday's closing price
- **Change Amount** - Price change in dollars
- **Change Percent** - Price change percentage
- **Day High** - Highest price today
- **Day Low** - Lowest price today
- **Opening Price** - Today's opening price

### Company Information
- **Company Name** - Full company name
- **Ticker Symbol** - Stock symbol
- **Exchange** - Trading exchange
- **Industry** - Business industry
- **Market Cap** - Market capitalization

## 🔄 Real-Time Features

### Automatic Updates
- **Frontend Refresh:** Every 30 seconds
- **Backend Caching:** Optimized API calls
- **Live Trading:** Real-time price updates during trades

### Manual Refresh
- **Refresh Button:** Manual data update
- **Post-Trade Refresh:** Automatic refresh after trades
- **Error Recovery:** Automatic fallback to cached data

## 🛡️ Error Handling & Fallback

### API Failure Scenarios
1. **Network Issues:** Falls back to cached data
2. **Rate Limiting:** Uses exponential backoff
3. **Invalid Symbols:** Provides default pricing
4. **Service Outage:** Maintains functionality with mock data

### Fallback Data
```java
// Default prices for major stocks
AAPL: $150.00
GOOGL: $2500.00
MSFT: $300.00
AMZN: $3200.00
TSLA: $800.00
// ... and more
```

## 📈 Supported Stocks

### Current Portfolio
- **AAPL** - Apple Inc.
- **GOOGL** - Alphabet Inc.
- **MSFT** - Microsoft Corporation
- **AMZN** - Amazon.com Inc.
- **TSLA** - Tesla Inc.
- **META** - Meta Platforms Inc.
- **NVDA** - NVIDIA Corporation
- **NFLX** - Netflix Inc.
- **AMD** - Advanced Micro Devices Inc.
- **INTC** - Intel Corporation

## 🔧 Configuration Options

### API Rate Limits
- **Free Tier:** 60 calls/minute
- **Current Usage:** ~10 calls/minute (10 stocks)
- **Optimization:** Caching and batching implemented

### Refresh Intervals
- **Frontend:** 30 seconds
- **Backend Cache:** 15 seconds
- **Configurable:** Can be adjusted in application properties

## 🚨 Monitoring & Logging

### Backend Logs
```bash
# View real-time logs
tail -f backend/backend.log | grep Finnhub
```

### Key Log Messages
- `Fetching stock quote for symbol: AAPL`
- `Successfully fetched data for AAPL: Current Price = 150.25`
- `Using fallback data for symbol: AAPL`
- `Updated 10 stocks with real-time prices`

## 🔍 Testing the Integration

### Verification Steps
1. **Start Application:** `./start-app.sh`
2. **Login:** Use admin/admin123
3. **Navigate:** Go to Trading page
4. **Verify:** Check for "Real-time Data" badge
5. **Observe:** Stock prices should show live data
6. **Test Refresh:** Click refresh button
7. **Check Logs:** Monitor backend logs for API calls

### Expected Behavior
- ✅ Live stock prices displayed
- ✅ Day high/low information shown
- ✅ Real-time updates every 30 seconds
- ✅ Fallback data if API fails
- ✅ Enhanced trading modal with detailed info

## 🎯 Benefits

### For Users
- **Real Market Data:** Live stock prices
- **Better Decisions:** Day high/low information
- **Current Information:** Up-to-date market data
- **Professional Feel:** Real trading experience

### For Development
- **Scalable:** Easy to add more data sources
- **Reliable:** Robust error handling
- **Maintainable:** Clean service architecture
- **Extensible:** Ready for additional features

## 🔮 Future Enhancements

### Potential Additions
- **Historical Data:** Price charts and trends
- **Market News:** Company-specific news feeds
- **Technical Indicators:** RSI, MACD, moving averages
- **Watchlists:** Custom stock monitoring
- **Alerts:** Price movement notifications
- **Extended Hours:** Pre/post-market data

### API Expansion
- **More Symbols:** Support for additional stocks
- **Crypto Support:** Cryptocurrency trading
- **Forex Data:** Currency exchange rates
- **Commodities:** Gold, oil, and other commodities

## 📞 Support & Troubleshooting

### Common Issues
1. **API Key Invalid:** Check configuration in application.properties
2. **Rate Limit Exceeded:** Reduce refresh frequency
3. **Network Issues:** Check internet connectivity
4. **Fallback Data:** Normal behavior when API unavailable

### Debug Commands
```bash
# Check API connectivity
curl "https://finnhub.io/api/v1/quote?symbol=AAPL&token=d0ti6i1r01qlvahca0v0d0ti6i1r01qlvahca0vg"

# Monitor backend logs
tail -f backend/backend.log

# Check application status
ps aux | grep java
```

---

**🎉 TradeAgent now provides a professional trading experience with real-time market data powered by Finnhub.io!** 