import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button, Form, Modal, Alert, Spinner, Badge } from 'react-bootstrap';
import { toast } from 'react-toastify';
import axios from 'axios';

const Trading = () => {
  const [stocks, setStocks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [tradeType, setTradeType] = useState('buy');
  const [quantity, setQuantity] = useState(1);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchStocks();
    // Refresh stock data every 30 seconds for real-time updates
    const interval = setInterval(fetchStocks, 30000);
    return () => clearInterval(interval);
  }, []);

  const fetchStocks = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/stocks', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setStocks(response.data);
    } catch (error) {
      toast.error('Failed to load stocks');
      console.error('Stocks fetch error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleTrade = (stock, type) => {
    setSelectedStock(stock);
    setTradeType(type);
    setQuantity(1);
    setShowModal(true);
  };

  const executeTrade = async () => {
    if (!selectedStock || quantity < 1) {
      toast.error('Please enter a valid quantity');
      return;
    }

    setSubmitting(true);
    try {
      const token = localStorage.getItem('token');
      const endpoint = tradeType === 'buy' ? '/api/trades/buy' : '/api/trades/sell';
      
      await axios.post(endpoint, {
        stockSymbol: selectedStock.symbol,
        quantity: parseInt(quantity)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      toast.success(`${tradeType === 'buy' ? 'Purchase' : 'Sale'} successful!`);
      setShowModal(false);
      setQuantity(1);
      // Refresh stocks after trade
      fetchStocks();
    } catch (error) {
      toast.error(error.response?.data?.error || `${tradeType} failed`);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  return (
    <Container fluid>
      <Row>
        <Col>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h1>Stock Trading</h1>
            <div>
              <Badge bg="info" className="me-2">Real-time Data</Badge>
              <Button variant="outline-primary" size="sm" onClick={fetchStocks}>
                <i className="bi bi-arrow-clockwise me-1"></i>
                Refresh
              </Button>
            </div>
          </div>
          
          <Card>
            <Card.Header>
              <h5 className="mb-0">Available Stocks - Live Market Data</h5>
            </Card.Header>
            <Card.Body>
              {stocks.length > 0 ? (
                <Table responsive striped hover>
                  <thead>
                    <tr>
                      <th>Symbol</th>
                      <th>Company</th>
                      <th>Current Price</th>
                      <th>Change</th>
                      <th>Open</th>
                      <th>High</th>
                      <th>Low</th>
                      <th>Sector</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stocks.map((stock) => {
                      const change = stock.currentPrice - stock.previousClose;
                      const changePercent = (change / stock.previousClose) * 100;
                      
                      return (
                        <tr key={stock.symbol}>
                          <td><strong>{stock.symbol}</strong></td>
                          <td>{stock.companyName}</td>
                          <td>
                            <strong>${stock.currentPrice?.toFixed(2)}</strong>
                          </td>
                          <td className={change >= 0 ? 'text-success' : 'text-danger'}>
                            <strong>
                              {change >= 0 ? '+' : ''}${change.toFixed(2)}
                            </strong>
                            <br />
                            <small>({changePercent.toFixed(2)}%)</small>
                          </td>
                          <td>
                            {stock.openPrice ? `$${stock.openPrice.toFixed(2)}` : '-'}
                          </td>
                          <td className="text-success">
                            {stock.dayHigh ? `$${stock.dayHigh.toFixed(2)}` : '-'}
                          </td>
                          <td className="text-danger">
                            {stock.dayLow ? `$${stock.dayLow.toFixed(2)}` : '-'}
                          </td>
                          <td>
                            <small className="text-muted">{stock.sector}</small>
                          </td>
                          <td>
                            <Button
                              variant="success"
                              size="sm"
                              className="me-2"
                              onClick={() => handleTrade(stock, 'buy')}
                            >
                              Buy
                            </Button>
                            <Button
                              variant="danger"
                              size="sm"
                              onClick={() => handleTrade(stock, 'sell')}
                            >
                              Sell
                            </Button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </Table>
              ) : (
                <div className="text-center py-4">
                  <p className="text-muted">No stocks available for trading.</p>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Trading Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>
            {tradeType === 'buy' ? 'Buy' : 'Sell'} {selectedStock?.symbol}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedStock && (
            <>
              <Row>
                <Col md={6}>
                  <div className="mb-3">
                    <h5>{selectedStock.companyName}</h5>
                    <p className="text-muted mb-2">{selectedStock.sector} - {selectedStock.industry}</p>
                    
                    <div className="mb-3">
                      <div className="d-flex justify-content-between">
                        <span>Current Price:</span>
                        <strong>${selectedStock.currentPrice?.toFixed(2)}</strong>
                      </div>
                      <div className="d-flex justify-content-between">
                        <span>Previous Close:</span>
                        <span>${selectedStock.previousClose?.toFixed(2)}</span>
                      </div>
                      {selectedStock.openPrice && (
                        <div className="d-flex justify-content-between">
                          <span>Open:</span>
                          <span>${selectedStock.openPrice.toFixed(2)}</span>
                        </div>
                      )}
                      {selectedStock.dayHigh && (
                        <div className="d-flex justify-content-between">
                          <span>Day High:</span>
                          <span className="text-success">${selectedStock.dayHigh.toFixed(2)}</span>
                        </div>
                      )}
                      {selectedStock.dayLow && (
                        <div className="d-flex justify-content-between">
                          <span>Day Low:</span>
                          <span className="text-danger">${selectedStock.dayLow.toFixed(2)}</span>
                        </div>
                      )}
                    </div>
                  </div>
                </Col>
                <Col md={6}>
                  <Form.Group className="mb-3">
                    <Form.Label>Quantity</Form.Label>
                    <Form.Control
                      type="number"
                      min="1"
                      value={quantity}
                      onChange={(e) => setQuantity(e.target.value)}
                    />
                  </Form.Group>
                  
                  <div className="mb-3">
                    <div className="d-flex justify-content-between">
                      <span>Price per share:</span>
                      <span>${selectedStock.currentPrice?.toFixed(2)}</span>
                    </div>
                    <div className="d-flex justify-content-between">
                      <span>Quantity:</span>
                      <span>{quantity}</span>
                    </div>
                    <hr />
                    <div className="d-flex justify-content-between">
                      <strong>Total:</strong>
                      <strong>${(selectedStock.currentPrice * quantity).toFixed(2)}</strong>
                    </div>
                  </div>
                </Col>
              </Row>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button
            variant={tradeType === 'buy' ? 'success' : 'danger'}
            onClick={executeTrade}
            disabled={submitting}
          >
            {submitting ? (
              <>
                <Spinner animation="border" size="sm" className="me-2" />
                Processing...
              </>
            ) : (
              `${tradeType === 'buy' ? 'Buy' : 'Sell'} ${quantity} shares`
            )}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default Trading; 