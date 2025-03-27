import React, { useState, useEffect } from 'react';
import axios from 'axios';

// --- MagazineList Component ---
function MagazineList({ onSelect }) {
    const [magazines, setMagazines] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMagazines = async () => {
            try {
                const response = await axios.get('http://localhost:8080/rest/magazine');
                setMagazines(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to fetch magazines.');
                setLoading(false);
            }
        };

        fetchMagazines();
    }, []);

    if (loading) return <p>Loading magazines...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <ul>
            {magazines.map(magazine => (
                <li key={magazine.id} onClick={() => onSelect(magazine.id)}>
                    {magazine.title} (Issue: {magazine.currIssue})
                </li>
            ))}
        </ul>
    );
}

// --- MagazineDetail Component ---
function MagazineDetail({ magazineId }) {
    const [magazine, setMagazine] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMagazine = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/rest/magazine/${magazineId}`);
                setMagazine(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || `Failed to fetch magazine with ID ${magazineId}.`);
                setLoading(false);
            }
        };

        if (magazineId) fetchMagazine();
    }, [magazineId]);

    if (!magazineId) return <p>Select a magazine to view details.</p>;
    if (loading) return <p>Loading details...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div>
            <h2>{magazine.title}</h2>
            <p>Order Quantity: {magazine.orderQty}</p>
            <p>Current Issue: {magazine.currIssue}</p>
        </div>
    );
}

// --- MagazineForm Component ---
function MagazineForm({ onSubmit, initialValues }) {
    const [formData, setFormData] = useState(initialValues || { title: '', orderQty: 0, currIssue: '' });
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
            const url = initialValues?.id ? `http://localhost:8080/rest/magazine/${initialValues.id}` : 'http://localhost:8080/rest/magazine';
            const method = initialValues?.id ? 'put' : 'post';

            await axios[method](url, formData);
            onSubmit(); // Refresh list
            setFormData({ title: '', orderQty: 0, currIssue: '' });
        } catch (err) {
            setError(err.message || 'Failed to submit magazine.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}
            <div>
                <label>Title:</label>
                <input type="text" name="title" value={formData.title} onChange={handleChange} />
            </div>
            <div>
                <label>Order Quantity:</label>
                <input type="number" name="orderQty" value={formData.orderQty} onChange={handleChange} />
            </div>
            <div>
                <label>Current Issue:</label>
                <input type="date" name="currIssue" value={formData.currIssue} onChange={handleChange} />
            </div>
            <button type="submit" disabled={isSubmitting}>{isSubmitting ? 'Submitting...' : 'Save Magazine'}</button>
        </form>
    );
}

// --- DeleteMagazine Component ---
function DeleteMagazine({ magazineId, onDelete }) {
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState(null);

    const handleDelete = async () => {
        setIsDeleting(true);
        setError(null);

        try {
            await axios.delete(`http://localhost:8080/rest/magazine/${magazineId}`);
            onDelete();
        } catch (err) {
            setError(err.message || `Failed to delete magazine with ID ${magazineId}.`);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <button onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? 'Deleting...' : 'Delete Magazine'}
        </button>
    );
}

// --- Main App Component ---
function App() {
    const [selectedMagazineId, setSelectedMagazineId] = useState(null);
    const [refresh, setRefresh] = useState(false);

    return (
        <div>
            <h1>Magazine Management</h1>
            <div style={{ display: 'flex' }}>
                <div style={{ width: '300px', marginRight: '20px' }}>
                    <h2>Magazine List</h2>
                    <MagazineList key={refresh} onSelect={setSelectedMagazineId} />
                </div>
                <div style={{ width: '400px', marginRight: '20px' }}>
                    <h2>Magazine Details</h2>
                    <MagazineDetail magazineId={selectedMagazineId} />
                    {selectedMagazineId && <DeleteMagazine magazineId={selectedMagazineId} onDelete={() => setRefresh(!refresh)} />}
                </div>
                <div style={{ width: '400px' }}>
                    <h2>Add/Update Magazine</h2>
                    <MagazineForm onSubmit={() => setRefresh(!refresh)} initialValues={selectedMagazineId ? { id: selectedMagazineId } : null} />
                </div>
            </div>
        </div>
    );
}

export default App;
