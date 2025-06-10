import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Badge, Alert, Spinner } from 'react-bootstrap';
import axios from 'axios';

const TransactionHistory = () => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/trades/history', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setTransactions(response.data.transactions || []);
    } catch (error) {
      setError('Failed to load transaction history');
      console.error('Transactions fetch error:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
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

  return (
    <Container fluid>
      <Row>
        <Col>
          <h1 className="mb-4">Transaction History</h1>
          
          <Card>
            <Card.Header>
              <h5 className="mb-0">All Transactions</h5>
            </Card.Header>
            <Card.Body>
              {transactions.length > 0 ? (
                <Table responsive striped hover>
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Type</th>
                      <th>Symbol</th>
                      <th>Company</th>
                      <th>Quantity</th>
                      <th>Price per Share</th>
                      <th>Total Amount</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.map((transaction) => (
                      <tr 
                        key={transaction.id}
                        className={transaction.type === 'BUY' ? 'buy-transaction' : 'sell-transaction'}
                      >
                        <td>{formatDate(transaction.transactionDate)}</td>
                        <td>
                          <Badge 
                            bg={transaction.type === 'BUY' ? 'success' : 'danger'}
                          >
                            {transaction.type}
                          </Badge>
                        </td>
                        <td><strong>{transaction.stockSymbol}</strong></td>
                        <td>{transaction.stockName}</td>
                        <td>{transaction.quantity}</td>
                        <td>${transaction.pricePerShare?.toFixed(2)}</td>
                        <td>${transaction.totalAmount?.toFixed(2)}</td>
                        <td>
                          <Badge bg="success">Completed</Badge>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <div className="text-center py-4">
                  <p className="text-muted">No transactions yet.</p>
                  <p>Start trading to see your transaction history!</p>
                </div>
              )}
            </Card.Body>
          </Card>

          {/* Summary Statistics */}
          {transactions.length > 0 && (
            <Row className="mt-4">
              <Col md={4}>
                <Card>
                  <Card.Body className="text-center">
                    <h5>Total Transactions</h5>
                    <h3 className="text-primary">{transactions.length}</h3>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card>
                  <Card.Body className="text-center">
                    <h5>Buy Orders</h5>
                    <h3 className="text-success">
                      {transactions.filter(t => t.type === 'BUY').length}
                    </h3>
                  </Card.Body>
                </Card>
              </Col>
              <Col md={4}>
                <Card>
                  <Card.Body className="text-center">
                    <h5>Sell Orders</h5>
                    <h3 className="text-danger">
                      {transactions.filter(t => t.type === 'SELL').length}
                    </h3>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default TransactionHistory; 