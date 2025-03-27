// https://medium.com/@wdswy/react-useeffect-hook-in-5-minutes-f4f9fc95fb6a
// get user data from a public API
import React, { useState, useEffect } from 'react';

const UserDataFetcher = () => {
    // State variables to store user data, loading status, and errors
    const [userData, setUserData] = useState(null); // Stores user data
    const [isLoading, setIsLoading] = useState(true); // Tracks loading status
    const [error, setError] = useState(null); // Stores error message, if any

    useEffect(() => {
        // Function to fetch user data
        const fetchUserData = async () => {
            try {
                // 🌐 Fetching user data from a mock API (replace with your API endpoint)
                const response = await fetch('https://jsonplaceholder.typicode.com/users/1');

                // Check if response is not successful
                if (!response.ok) {
                    throw new Error('Failed to fetch user data');
                }

                // Parse response data to JSON and set user data state
                const userData = await response.json();
                setUserData(userData);
            } catch (error) {
                // Set error state if there's an error during fetching
                setError(error.message);
            } finally {
                // Update loading status after fetch operation completes
                setIsLoading(false);
            }
        };

        // Call the function to fetch user data when the component mounts
        fetchUserData();
    }, []); // Empty dependency array ensures the effect runs only once (on mount)

    // Render UI based on loading status and fetched data
    return (
        <div>
            <h2>User Data Fetching Example</h2>
            <p><a href='https://medium.com/@wdswy/react-useeffect-hook-in-5-minutes-f4f9fc95fb6a'>from article...</a></p>
            {isLoading ? (
                // Display a loading message while fetching data
                <p>Loading...</p>
            ) : error ? (
                // Display an error message if there's an error during fetching
                <p>Error: {error}</p>
            ) : (
                // Display the fetched user data once available
                <div>
                    <h3>User Details:</h3>
                    <p>Name: {userData.name}</p>
                    <p>Email: {userData.email}</p>
                    {/* Display other user data as needed */}
                </div>
            )}
        </div>
    );
};

export default UserDataFetcher;