import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';
import * as api from './api';

jest.mock('./api');

describe('App Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();
  });

  afterEach(async () => {
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
    await new Promise(resolve => setTimeout(resolve, 0));
  });

  test('renders the app header', async () => {
    (api.getInventory as jest.Mock).mockResolvedValue([]);
    
    render(<App />);
    const headerElement = screen.getByText(/Gilded Rose Inventory Management/i);
    expect(headerElement).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByText(/Loading inventory.../i)).not.toBeInTheDocument();
    });
  });

  test('displays loading state initially', () => {
    const mockGetInventory = jest.fn(() => new Promise(() => {}));
    (api.getInventory as jest.Mock).mockImplementation(mockGetInventory);

    render(<App />);
    expect(screen.getByText(/Loading inventory.../i)).toBeInTheDocument();
  });

  test('displays inventory data when loaded successfully', async () => {
    const mockInventory = [
      { name: 'Aged Brie', sellIn: 1, quality: 1 },
      { name: 'Normal Item', sellIn: 2, quality: 2 }
    ];

    (api.getInventory as jest.Mock).mockResolvedValue(mockInventory);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Aged Brie')).toBeInTheDocument();
    });
    
    expect(screen.getByText('Normal Item')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByText(/Loading inventory.../i)).not.toBeInTheDocument();
    });
  });

  test('handles inventory update button click', async () => {
    const mockInventory = [
      { name: 'Aged Brie', sellIn: 1, quality: 1 }
    ];
    const updatedInventory = [
      { name: 'Aged Brie', sellIn: 0, quality: 2 }
    ];

    (api.getInventory as jest.Mock).mockResolvedValue(mockInventory);
    (api.updateInventory as jest.Mock).mockResolvedValue(updatedInventory);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Aged Brie')).toBeInTheDocument();
    });

    const updateButton = screen.getByText(/Update Inventory/i);
    fireEvent.click(updateButton);
    
    await waitFor(() => {
      expect(api.updateInventory).toHaveBeenCalledWith(mockInventory);
    });

    await waitFor(() => {
      expect(screen.getByText('0')).toBeInTheDocument(); // Updated sellIn value
    });
    
    await waitFor(() => {
      expect(screen.getByText('2')).toBeInTheDocument(); // Updated quality value
    });
    
    await waitFor(() => {
      expect(screen.queryByText(/Updating.../i)).not.toBeInTheDocument();
    });
  });

  test('displays error message when fetch fails', async () => {
    const errorMessage = 'API connection failed (NETWORK_ERROR)';
    (api.getInventory as jest.Mock).mockRejectedValue(new Error(errorMessage));

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });
    
    expect(screen.getByText(/Retry/i)).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByText(/Loading inventory.../i)).not.toBeInTheDocument();
    });
  });

  test('displays generic error message when error is not an Error instance', async () => {
    (api.getInventory as jest.Mock).mockRejectedValue('some string error');

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Failed to fetch inventory. Please try again./i)).toBeInTheDocument();
    });
    
    expect(screen.getByText(/Retry/i)).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByText(/Loading inventory.../i)).not.toBeInTheDocument();
    });
  });

  test('retry button calls fetch inventory again', async () => {
    const errorMessage = 'API Error';
    (api.getInventory as jest.Mock).mockRejectedValueOnce(new Error(errorMessage))
      .mockResolvedValueOnce([{ name: 'Aged Brie', sellIn: 1, quality: 1 }]);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(errorMessage)).toBeInTheDocument();
    });

    const retryButton = screen.getByText(/Retry/i);
    fireEvent.click(retryButton);

    await waitFor(() => {
      expect(screen.getByText('Aged Brie')).toBeInTheDocument();
    });
    
    await waitFor(() => {
      expect(screen.queryByText(/Loading inventory.../i)).not.toBeInTheDocument();
    });
  });

  test('displays loading state during update', async () => {
    const mockInventory = [{ name: 'Normal Item', sellIn: 2, quality: 2 }];
    
    (api.getInventory as jest.Mock).mockResolvedValue(mockInventory);
    (api.updateInventory as jest.Mock).mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve(mockInventory), 100))
    );

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Normal Item')).toBeInTheDocument();
    });

    const updateButton = screen.getByText(/Update Inventory/i);
    fireEvent.click(updateButton);

    expect(screen.getByText(/Updating.../i)).toBeInTheDocument();
    
    jest.runAllTimers();
    
    await waitFor(() => {
      expect(screen.queryByText(/Updating.../i)).not.toBeInTheDocument();
    });
  });
});