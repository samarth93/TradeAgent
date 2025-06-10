import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { toast } from 'react-toastify';
import { useAuth } from '../App';

const Profile = () => {
  const { user } = useAuth();
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    username: user?.username || ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // TODO: Implement profile update API call
    toast.success('Profile updated successfully!');
    setEditing(false);
  };

  const handleCancel = () => {
    setFormData({
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
      email: user?.email || '',
      username: user?.username || ''
    });
    setEditing(false);
  };

  return (
    <Container fluid>
      <Row>
        <Col md={8} className="mx-auto">
          <h1 className="mb-4">My Profile</h1>
          
          <Card>
            <Card.Header>
              <div className="d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Account Information</h5>
                {!editing && (
                  <Button variant="outline-primary" onClick={() => setEditing(true)}>
                    Edit Profile
                  </Button>
                )}
              </div>
            </Card.Header>
            <Card.Body>
              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>First Name</Form.Label>
                      <Form.Control
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        disabled={!editing}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Last Name</Form.Label>
                      <Form.Control
                        type="text"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        disabled={!editing}
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    name="username"
                    value={formData.username}
                    disabled={true}
                  />
                  <Form.Text className="text-muted">
                    Username cannot be changed
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    disabled={!editing}
                  />
                </Form.Group>

                {editing && (
                  <div className="d-flex gap-2">
                    <Button type="submit" variant="primary">
                      Save Changes
                    </Button>
                    <Button type="button" variant="secondary" onClick={handleCancel}>
                      Cancel
                    </Button>
                  </div>
                )}
              </Form>
            </Card.Body>
          </Card>

          {/* Account Summary */}
          <Card className="mt-4">
            <Card.Header>
              <h5 className="mb-0">Account Summary</h5>
            </Card.Header>
            <Card.Body>
              <Row>
                <Col md={6}>
                  <div className="mb-3">
                    <strong>Account Balance:</strong>
                    <div className="h4 text-success">${user?.balance?.toLocaleString() || '0.00'}</div>
                  </div>
                </Col>
                <Col md={6}>
                  <div className="mb-3">
                    <strong>Account Type:</strong>
                    <div className="h6">
                      {user?.roles?.includes('ROLE_ADMIN') ? (
                        <span className="badge bg-warning">Administrator</span>
                      ) : (
                        <span className="badge bg-primary">Trader</span>
                      )}
                    </div>
                  </div>
                </Col>
              </Row>
              
              <Row>
                <Col md={6}>
                  <div className="mb-3">
                    <strong>Member Since:</strong>
                    <div>{user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}</div>
                  </div>
                </Col>
                <Col md={6}>
                  <div className="mb-3">
                    <strong>Last Updated:</strong>
                    <div>{user?.updatedAt ? new Date(user.updatedAt).toLocaleDateString() : 'N/A'}</div>
                  </div>
                </Col>
              </Row>
            </Card.Body>
          </Card>

          {/* Security Section */}
          <Card className="mt-4">
            <Card.Header>
              <h5 className="mb-0">Security</h5>
            </Card.Header>
            <Card.Body>
              <Button variant="outline-warning" className="me-2">
                Change Password
              </Button>
              <Button variant="outline-info">
                Two-Factor Authentication
              </Button>
              <div className="mt-3">
                <small className="text-muted">
                  Keep your account secure by using a strong password and enabling two-factor authentication.
                </small>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Profile; 