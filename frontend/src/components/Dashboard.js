import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { useAuth } from '../App';

const Dashboard = () => {
  const { user } = useAuth();

  return (
    <Container fluid>
      <Row>
        <Col>
          <h1 className="mb-4">Welcome back, {user?.firstName}!</h1>
          
          <Row>
            <Col md={4} className="mb-4">
              <Card className="h-100">
                <Card.Body>
                  <Card.Title>Account Balance</Card.Title>
                  <h3 className="text-success">${user?.balance?.toLocaleString() || '0.00'}</h3>
                </Card.Body>
              </Card>
            </Col>
            
            <Col md={4} className="mb-4">
              <Card className="h-100">
                <Card.Body>
                  <Card.Title>Portfolio Value</Card.Title>
                  <h3 className="text-primary">$0.00</h3>
                </Card.Body>
              </Card>
            </Col>
            
            <Col md={4} className="mb-4">
              <Card className="h-100">
                <Card.Body>
                  <Card.Title>Total Value</Card.Title>
                  <h3 className="text-info">${user?.balance?.toLocaleString() || '0.00'}</h3>
                </Card.Body>
              </Card>
            </Col>
          </Row>
          
          <Row>
            <Col>
              <Card>
                <Card.Header>
                  <h5 className="mb-0">Quick Actions</h5>
                </Card.Header>
                <Card.Body>
                  <p>Welcome to TradeAgent! Use the navigation menu to:</p>
                  <ul>
                    <li>View and manage your portfolio</li>
                    <li>Buy and sell stocks</li>
                    <li>Review transaction history</li>
                    <li>Update your profile</li>
                  </ul>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard; 