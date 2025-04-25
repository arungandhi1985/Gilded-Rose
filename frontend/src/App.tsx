import React, { useEffect, useState } from 'react';
import { getInventory, updateInventory } from './api';
import { Item } from './types/Item';
import InventoryList from './components/InventoryList';
import UpdateButton from './components/UpdateButton';
import './App.css';

const App: React.FC = () => {
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchInventory = async () => {
    setLoading(true);
    try {
      const data = await getInventory();
      setItems(data);
      setError(null);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to fetch inventory. Please try again.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async () => {
    setLoading(true);
    try {
      const updatedItems = await updateInventory(items);
      setItems(updatedItems);
      setError(null);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to update inventory. Please try again.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInventory();
  }, []);

  return (
    <div className="app">
      <header className="app-header">
        <h1>Gilded Rose Inventory Management</h1>
      </header>
      <main className="app-content">
        {error ? (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={fetchInventory}>Retry</button>
          </div>
        ) : (
          <>
            <UpdateButton onUpdate={handleUpdate} isLoading={loading} />
            {loading && !items.length ? (
              <p className="loading">Loading inventory...</p>
            ) : (
              <InventoryList items={items} />
            )}
          </>
        )}
      </main>
    </div>
  );
};

export default App;