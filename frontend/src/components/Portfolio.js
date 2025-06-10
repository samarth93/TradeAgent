import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Alert, Spinner } from 'react-bootstrap';
import { useAuth } from '../App';
import axios from 'axios';

const Portfolio = () => {
  const [portfolio, setPortfolio] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    fetchPortfolio();
  }, []);

  const fetchPortfolio = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/portfolio', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setPortfolio(response.data);
    } catch (error) {
      setError('Failed to load portfolio data');
      console.error('Portfolio fetch error:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  const calculateTotalValue = () => {
    if (!portfolio?.holdings) return 0;
    return Object.entries(portfolio.holdings).reduce((total, [symbol, quantity]) => {
      const avgPrice = portfolio.averagePrices?.[symbol] || 0;
      return total + (quantity * avgPrice);
    }, 0);
  };

  return (
    <Container fluid>
      <Row>
        <Col>
          <h1 className="mb-4">My Portfolio</h1>
          
          {/* Portfolio Summary */}
          <Row className="mb-4">
            <Col md={4}>
              <Card className="portfolio-summary">
                <Card.Body>
                  <h5>Cash Balance</h5>
                  <h3>${user?.balance?.toLocaleString() || '0.00'}</h3>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4}>
              <Card className="portfolio-summary">
                <Card.Body>
                  <h5>Portfolio Value</h5>
                  <h3>${calculateTotalValue().toLocaleString()}</h3>
                </Card.Body>
              </Card>
            </Col>
            <Col md={4}>
              <Card className="portfolio-summary">
                <Card.Body>
                  <h5>Total Value</h5>
                  <h3>${((user?.balance || 0) + calculateTotalValue()).toLocaleString()}</h3>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          {/* Holdings Table */}
          <Card>
            <Card.Header>
              <h5 className="mb-0">Stock Holdings</h5>
            </Card.Header>
            <Card.Body>
              {portfolio?.holdings && Object.keys(portfolio.holdings).length > 0 ? (
                <Table responsive striped hover>
                  <thead>
                    <tr>
                      <th>Symbol</th>
                      <th>Quantity</th>
                      <th>Avg. Price</th>
                      <th>Current Value</th>
                      <th>P&L</th>
                    </tr>
                  </thead>
                  <tbody>
                    {Object.entries(portfolio.holdings).map(([symbol, quantity]) => {
                      const avgPrice = portfolio.averagePrices?.[symbol] || 0;
                      const currentValue = quantity * avgPrice;
                      const pnl = 0; // Would need current market price to calculate
                      
                      return (
                        <tr key={symbol}>
                          <td><strong>{symbol}</strong></td>
                          <td>{quantity}</td>
                          <td>${avgPrice.toFixed(2)}</td>
                          <td>${currentValue.toFixed(2)}</td>
                          <td className={pnl >= 0 ? 'text-success' : 'text-danger'}>
                            ${pnl.toFixed(2)}
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </Table>
              ) : (
                <div className="text-center py-4">
                  <p className="text-muted">No stock holdings yet.</p>
                  <p>Start trading to build your portfolio!</p>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Portfolio; 