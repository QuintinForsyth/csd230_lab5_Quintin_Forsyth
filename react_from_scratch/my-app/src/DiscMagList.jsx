import React, { useState, useEffect } from 'react';
import axios from 'axios';

// --- DiscMagList Component ---
function DiscMagList({ onSelect }) {
    const [discMags, setDiscMags] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchDiscMags = async () => {
            try {
                const response = await axios.get('http://localhost:8080/rest/discmag');
                setDiscMags(response.data);
            } catch (err) {
                setError(err.message || 'Failed to fetch magazines.');
            } finally {
                setLoading(false);
            }
        };

        fetchDiscMags();
    }, []);

    if (loading) return <p>Loading magazines...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <ul>
            {discMags.map(mag => (
                <li key={mag.id} onClick={() => onSelect(mag.id)}>
                    {mag.title} - {mag.hasDisc ? 'Includes Disc' : 'No Disc'}
                </li>
            ))}
        </ul>
    );
}

// --- DiscMagDetail Component ---
function DiscMagDetail({ discMagId }) {
    const [discMag, setDiscMag] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!discMagId) return;

        const fetchDiscMag = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/rest/discmag/${discMagId}`);
                setDiscMag(response.data);
            } catch (err) {
                setError(err.message || `Failed to fetch magazine with ID ${discMagId}.`);
            } finally {
                setLoading(false);
            }
        };

        fetchDiscMag();
    }, [discMagId]);

    if (!discMagId) return <p>Please select a magazine to view details.</p>;
    if (loading) return <p>Loading magazine details...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!discMag) return <p>Magazine not found.</p>;

    return (
        <div>
            <h2>{discMag.title}</h2>
            <p>Has Disc: {discMag.hasDisc ? 'Yes' : 'No'}</p>
        </div>
    );
}

// --- DiscMagForm Component ---
function DiscMagForm({ onSubmit, initialValues }) {
    const [formData, setFormData] = useState(initialValues || { title: '', hasDisc: false });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            const url = initialValues?.id ? `http://localhost:8080/rest/discmag/${initialValues.id}` : 'http://localhost:8080/rest/discmag';
            const method = initialValues?.id ? 'put' : 'post';
            await axios[method](url, formData);
            onSubmit();
            setFormData({ title: '', hasDisc: false });
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
                <input type="text" name="title" value={formData.title} onChange={handleChange} required />
            </div>
            <div>
                <label>Has Disc:</label>
                <input type="checkbox" name="hasDisc" checked={formData.hasDisc} onChange={handleChange} />
            </div>
            <button type="submit" disabled={isSubmitting}>{isSubmitting ? 'Submitting...' : 'Save Magazine'}</button>
        </form>
    );
}

// --- DeleteDiscMag Component ---
function DeleteDiscMag({ discMagId, onDelete }) {
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState(null);

    const handleDelete = async () => {
        setIsDeleting(true);
        setError(null);

        try {
            await axios.delete(`http://localhost:8080/rest/discmag/${discMagId}`);
            onDelete();
        } catch (err) {
            setError(err.message || `Failed to delete magazine with ID ${discMagId}.`);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <button onClick={handleDelete} disabled={isDeleting}>{isDeleting ? 'Deleting...' : 'Delete Magazine'}</button>
    );
}

// --- Main App Component ---
function App() {
    const [selectedDiscMagId, setSelectedDiscMagId] = useState(null);
    const [refreshList, setRefreshList] = useState(false);

    return (
        <div>
            <h1>Disc Magazine Management</h1>
            <div style={{ display: 'flex' }}>
                <div style={{ width: '300px', marginRight: '20px' }}>
                    <h2>Magazines</h2>
                    <DiscMagList key={refreshList} onSelect={setSelectedDiscMagId} />
                </div>
                <div style={{ width: '400px', marginRight: '20px' }}>
                    <h2>Magazine Details</h2>
                    <DiscMagDetail discMagId={selectedDiscMagId} />
                    {selectedDiscMagId && <DeleteDiscMag discMagId={selectedDiscMagId} onDelete={() => { setSelectedDiscMagId(null); setRefreshList(!refreshList); }} />}
                </div>
                <div style={{ width: '400px' }}>
                    <h2>Add/Update Magazine</h2>
                    <DiscMagForm onSubmit={() => setRefreshList(!refreshList)} initialValues={selectedDiscMagId ? { id: selectedDiscMagId } : null} />
                </div>
            </div>
        </div>
    );
}

export default App;
