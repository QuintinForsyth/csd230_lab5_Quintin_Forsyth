import React, { useState, useEffect } from 'react';
import axios from 'axios';

// --- BookList Component ---
function BookList() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const response = await axios.get('http://localhost:8080/rest/book');
                setBooks(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to fetch books.');
                setLoading(false);
            }
        };

        fetchBooks();
    }, []); // Empty dependency array means this effect runs only once on mount

    if (loading) return <p>Loading books...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <ul>
            {books.map(book => (
                <li key={book.id}>
                    {book.title} by {book.author} (ISBN: {book.isbn})
                </li>
            ))}
        </ul>
    );
}

// --- BookDetail Component ---
function BookDetail({ bookId }) {
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/rest/book/${bookId}`);
                setBook(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message || `Failed to fetch book with ID ${bookId}.`);
                setLoading(false);
            }
        };

        if (bookId) { // Only fetch if a bookId is provided
            fetchBook();
        } else {
            setLoading(false); // If no bookId, just set loading to false
        }
    }, [bookId]); // Effect runs when bookId changes

    if (!bookId) return <p>Please select a book to view details.</p>;
    if (loading) return <p>Loading book details...</p>;
    if (error) return <p>Error: {error}</p>;
    if (!book) return <p>Book not found.</p>;

    return (
        <div>
            <h2>{book.title}</h2>
            <p>Author: {book.author}</p>
            <p>ISBN: {book.isbn}</p>
        </div>
    );
}

// --- BookForm Component (Create/Update) ---
function BookForm({ onSubmit, initialValues }) {
    const [formData, setFormData] = useState(initialValues || { title: '', author: '', isbn: '' });
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
            const url = initialValues?.id ? `http://localhost:8080/rest/book/${initialValues.id}` : 'http://localhost:8080/rest/book';
            const method = initialValues?.id ? 'put' : 'post';

            const response = await axios[method](url, formData); // Use axios.post or axios.put dynamically
            onSubmit(response.data); // Call the parent component's onSubmit function
            setFormData({ title: '', author: '', isbn: '' }); // Clear form after successful submission
        } catch (err) {
            setError(err.message || 'Failed to submit book.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}
            <div>
                <label htmlFor="title">Title:</label>
                <input type="text" id="title" name="title" value={formData.title} onChange={handleChange} />
            </div>
            <div>
                <label htmlFor="author">Author:</label>
                <input type="text" id="author" name="author" value={formData.author} onChange={handleChange} />
            </div>
            <div>
                <label htmlFor="isbn">ISBN:</label>
                <input type="text" id="isbn" name="isbn" value={formData.isbn} onChange={handleChange} />
            </div>
            <button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Submitting...' : initialValues ? 'Update Book' : 'Add Book'}
            </button>
        </form>
    );
}

// --- DeleteBook Component ---
function DeleteBook({ bookId, onDelete }) {
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState(null);

    const handleDelete = async () => {
        setIsDeleting(true);
        setError(null);

        try {
            await axios.delete(`http://localhost:8080/rest/book/${bookId}`);
            onDelete(); // Call the parent component's onDelete function to refresh the list
        } catch (err) {
            setError(err.message || `Failed to delete book with ID ${bookId}.`);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <button onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? 'Deleting...' : 'Delete Book'}
        </button>
    );
}

// --- Main App Component ---
function App() {
    const [selectedBookId, setSelectedBookId] = useState(null);
    const [booksUpdated, setBooksUpdated] = useState(false); // State to trigger re-fetch of books

    const handleBookSelect = (id) => {
        setSelectedBookId(id);
    };

    const handleBookSubmit = (newBook) => {
        // Handle the newly created or updated book (e.g., update the list)
        setBooksUpdated(!booksUpdated);  // Toggle the state to trigger a re-fetch
    };

    const handleBookDelete = () => {
        setSelectedBookId(null);
        setBooksUpdated(!booksUpdated);  // Toggle the state to trigger a re-fetch
    };

    return (
        <div>
            <h1>Book Management</h1>

            <div style={{display: 'flex'}}>
                <div style={{width: '300px', marginRight: '20px'}}>
                    <h2>Book List</h2>
                    <BookList key={booksUpdated} />  {/* Pass key to force re-render after update*/}
                </div>

                <div style={{width: '400px', marginRight: '20px'}}>
                    <h2>Book Details</h2>
                    <BookDetail bookId={selectedBookId} />
                    {selectedBookId && <DeleteBook bookId={selectedBookId} onDelete={handleBookDelete} />}
                </div>

                <div style={{width: '400px'}}>
                    <h2>Add/Update Book</h2>
                    <BookForm onSubmit={handleBookSubmit} initialValues={selectedBookId ? {id: selectedBookId} : null}/>
                </div>
            </div>
        </div>
    );
}

export default App;