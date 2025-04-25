import axios, { AxiosError } from 'axios';
import { Item } from './types/Item';

const API_URL = 'http://localhost:8080/api/inventory';

interface ErrorResponse {
  message: string;
  details: string;
  status: number;
  timestamp: string;
}

export const getInventory = async (): Promise<Item[]> => {
  try {
    const response = await axios.get<Item[]>(API_URL);
    return response.data;
  } catch (error) {
    const axiosError = error as AxiosError<ErrorResponse>;
    if (axiosError.response?.data) {
      throw new Error(`${axiosError.response.data.message} (${axiosError.response.data.details})`);
    }
    throw new Error('Failed to fetch inventory');
  }
};

export const updateInventory = async (items: Item[]): Promise<Item[]> => {
  try {
    const response = await axios.post<Item[]>(`${API_URL}/update`, items);
    return response.data;
  } catch (error) {
    const axiosError = error as AxiosError<ErrorResponse>;
    if (axiosError.response?.data) {
      throw new Error(`${axiosError.response.data.message} (${axiosError.response.data.details})`);
    }
    throw new Error('Failed to update inventory');
  }
};

export const checkHealth = async (): Promise<boolean> => {
  try {
    const response = await axios.get(`${API_URL}/health`);
    return response.status === 200;
  } catch (error) {
    return false;
  }
};