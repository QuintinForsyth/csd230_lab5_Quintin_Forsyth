import {useEffect, useState} from 'react';
import './App.css'

function Book() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch('http://localhost:8080/rest/book');
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const json = await response.json();
                setData(json);
            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, []);

    if (loading) {
        return (<p>Loading...</p>);
    }
    if (error) {
        return (<p>Error: {error.message}</p>);
    }

    return (
        <ul>
            {data.map(item => (
                <li key={item.id}>{item.title} by {item.author}, a story about {item.description}</li>
            ))}
        </ul>

    );
}

export default Book