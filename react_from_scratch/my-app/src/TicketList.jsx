import React, { useState, useEffect } from 'react';
import axios from 'axios';

// --- TicketList Component ---
function TicketList() {
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTickets = async () => {
            try {
                const response = await axios.get('http://localhost:8080/rest/ticket');
                setTickets(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to fetch tickets.');
                setLoading(false);
            }
        };

        fetchTickets();
    }, []); // Empty dependency array means this effect runs only once on mount

    if (loading) return <p>Loading tickets...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <ul>
            {tickets.map(ticket => (
                <li key={ticket.id}>
                    {ticket.description} - {ticket.text} (Price: ${ticket.price})
                </li>
            ))}
        </ul>
    );
}

// --- TicketDetail Component ---
function TicketDetail({ ticketId }) {
    const [ticket, setTicket] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTicket = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/rest/ticket/${ticketId}`);
                setTicket(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || `Failed to fetch ticket with ID ${ticketId}.`);
                setLoading(false);
            }
        };

        if (ticketId) { // Only fetch if a ticketId is provided
            fetchTicket();
        } else {
            setLoading(false); // If no ticketId, just set loading to false
        }
    }, [ticketId]); // Effect runs when ticketId changes

    if (!ticketId) return <p>Please select a ticket to view details.</p>;
    if (loading) return <p>Loading ticket details...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!ticket) return <p>Ticket not found.</p>;

    return (
        <div>
            <h2>{ticket.description}</h2>
            <p>Text: {ticket.text}</p>
            <p>Price: ${ticket.price}</p>
        </div>
    );
}

// --- TicketForm Component (Create/Update) ---
function TicketForm({ onSubmit, initialValues }) {
    const [formData, setFormData] = useState(initialValues || { description: '', text: '', price: '', quantity: '' });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            const url = initialValues?.id ? `http://localhost:8080/rest/ticket/${initialValues.id}` : 'http://localhost:8080/rest/ticket';
            const method = initialValues?.id ? 'put' : 'post';

            const response = await axios[method](url, formData); // Use axios.post or axios.put dynamically
            onSubmit(response.data); // Call the parent component's onSubmit function
            setFormData({ description: '', text: '', price: '', quantity: '' }); // Clear form after successful submission
        } catch (err) {
            setError(err.message || 'Failed to submit ticket.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}
            <div>
                <label htmlFor="description">Description:</label>
                <input type="text" id="description" name="description" value={formData.description} onChange={handleChange} />
            </div>
            <div>
                <label htmlFor="text">Text:</label>
                <input type="text" id="text" name="text" value={formData.text} onChange={handleChange} />
            </div>
            <div>
                <label htmlFor="price">Price:</label>
                <input type="number" id="price" name="price" value={formData.price} onChange={handleChange} />
            </div>
            <div>
                <label htmlFor="quantity">Quantity:</label>
                <input type="number" id="quantity" name="quantity" value={formData.quantity} onChange={handleChange} />
            </div>
            <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Submitting...' : initialValues ? 'Update Ticket' : 'Add Ticket'}
            </button>
        </form>
    );
}

// --- DeleteTicket Component ---
function DeleteTicket({ ticketId, onDelete }) {
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState(null);

    const handleDelete = async () => {
        setIsDeleting(true);
        setError(null);

        try {
            await axios.delete(`http://localhost:8080/rest/ticket/${ticketId}`);
            onDelete(); // Call the parent component's onDelete function to refresh the list
        } catch (err) {
            setError(err.message || `Failed to delete ticket with ID ${ticketId}.`);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <button onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? 'Deleting...' : 'Delete Ticket'}
        </button>
    );
}

// --- Main App Component ---
function App() {
    const [selectedTicketId, setSelectedTicketId] = useState(null);
    const [ticketsUpdated, setTicketsUpdated] = useState(false); // State to trigger re-fetch of tickets

    const handleTicketSelect = (id) => {
        setSelectedTicketId(id);
    };

    const handleTicketSubmit = (newTicket) => {
        // Handle the newly created or updated ticket (e.g., update the list)
        setTicketsUpdated(!ticketsUpdated);  // Toggle the state to trigger a re-fetch
    };

    const handleTicketDelete = () => {
        setSelectedTicketId(null);
        setTicketsUpdated(!ticketsUpdated);  // Toggle the state to trigger a re-fetch
    };

    return (
        <div>
            <h1>Ticket Management</h1>

            <div style={{display: 'flex'}}>
                <div style={{width: '300px', marginRight: '20px'}}>
                    <h2>Ticket List</h2>
                    <TicketList key={ticketsUpdated} />  {/* Pass key to force re-render after update*/}
                </div>

                <div style={{width: '400px', marginRight: '20px'}}>
                    <h2>Ticket Details</h2>
                    <TicketDetail ticketId={selectedTicketId} />
                    {selectedTicketId && <DeleteTicket ticketId={selectedTicketId} onDelete={handleTicketDelete} />}
                </div>

                <div style={{width: '400px'}}>
                    <h2>Add/Update Ticket</h2>
                    <TicketForm onSubmit={handleTicketSubmit} initialValues={selectedTicketId ? {id: selectedTicketId} : null}/>
                </div>
            </div>
        </div>
    );
}

export default App;
