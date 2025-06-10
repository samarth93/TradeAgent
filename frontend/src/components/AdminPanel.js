import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button, Modal, Form, Alert, Spinner, Badge } from 'react-bootstrap';
import { toast } from 'react-toastify';
import { useAuth } from '../App';
import axios from 'axios';

const AdminPanel = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [newBalance, setNewBalance] = useState('');
  const { user: currentUser } = useAuth();

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('token');
      const response = await axios.get('/api/admin/users', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setUsers(response.data);
    } catch (error) {
      toast.error('Failed to load users');
      console.error('Users fetch error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateBalance = (user) => {
    setSelectedUser(user);
    setNewBalance(user.balance.toString());
    setShowModal(true);
  };

  const updateUserBalance = async () => {
    if (!selectedUser || !newBalance) {
      toast.error('Please enter a valid balance');
      return;
    }

    try {
      const token = localStorage.getItem('token');
      await axios.put(`/api/admin/users/${selectedUser.id}/balance`, {
        balance: parseFloat(newBalance)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      toast.success('User balance updated successfully');
      setShowModal(false);
      fetchUsers();
    } catch (error) {
      toast.error('Failed to update user balance');
    }
  };

  const deleteUser = async (userId) => {
    if (!window.confirm('Are you sure you want to delete this user?')) {
      return;
    }

    try {
      const token = localStorage.getItem('token');
      await axios.delete(`/api/admin/users/${userId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });

      toast.success('User deleted successfully');
      fetchUsers();
    } catch (error) {
      toast.error('Failed to delete user');
    }
  };

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '400px' }}>
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  const totalUsers = users.length;
  const totalBalance = users.reduce((sum, user) => sum + (user.balance || 0), 0);
  const adminUsers = users.filter(user => user.roles?.includes('ROLE_ADMIN')).length;

  return (
    <Container fluid>
      <Row>
        <Col>
          <h1 className="mb-4">Admin Panel</h1>
          
          {/* Statistics Cards */}
          <Row className="mb-4">
            <Col md={3}>
              <Card className="text-center">
                <Card.Body>
                  <h5>Total Users</h5>
                  <h3 className="text-primary">{totalUsers}</h3>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="text-center">
                <Card.Body>
                  <h5>Total Balance</h5>
                  <h3 className="text-success">${totalBalance.toLocaleString()}</h3>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="text-center">
                <Card.Body>
                  <h5>Admin Users</h5>
                  <h3 className="text-warning">{adminUsers}</h3>
                </Card.Body>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="text-center">
                <Card.Body>
                  <h5>Trader Users</h5>
                  <h3 className="text-info">{totalUsers - adminUsers}</h3>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          {/* Users Table */}
          <Card>
            <Card.Header>
              <h5 className="mb-0">User Management</h5>
            </Card.Header>
            <Card.Body>
              <Table responsive striped hover>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Balance</th>
                    <th>Roles</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td><strong>{user.username}</strong></td>
                      <td>{user.firstName} {user.lastName}</td>
                      <td>{user.email}</td>
                      <td>${user.balance?.toLocaleString() || '0.00'}</td>
                      <td>
                        {user.roles?.map(role => (
                          <Badge 
                            key={role}
                            bg={role === 'ROLE_ADMIN' ? 'warning' : 'primary'}
                            className="me-1"
                          >
                            {role.replace('ROLE_', '')}
                          </Badge>
                        ))}
                      </td>
                      <td>
                        <Badge bg={user.enabled ? 'success' : 'danger'}>
                          {user.enabled ? 'Active' : 'Disabled'}
                        </Badge>
                      </td>
                      <td>
                        <Button
                          variant="outline-primary"
                          size="sm"
                          className="me-2"
                          onClick={() => handleUpdateBalance(user)}
                        >
                          Update Balance
                        </Button>
                        {user.id !== currentUser?.id && (
                          <Button
                            variant="outline-danger"
                            size="sm"
                            onClick={() => deleteUser(user.id)}
                          >
                            Delete
                          </Button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Update Balance Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update User Balance</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedUser && (
            <>
              <p><strong>User:</strong> {selectedUser.firstName} {selectedUser.lastName} ({selectedUser.username})</p>
              <p><strong>Current Balance:</strong> ${selectedUser.balance?.toLocaleString()}</p>
              
              <Form.Group>
                <Form.Label>New Balance</Form.Label>
                <Form.Control
                  type="number"
                  step="0.01"
                  min="0"
                  value={newBalance}
                  onChange={(e) => setNewBalance(e.target.value)}
                  placeholder="Enter new balance"
                />
              </Form.Group>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={updateUserBalance}>
            Update Balance
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default AdminPanel; 