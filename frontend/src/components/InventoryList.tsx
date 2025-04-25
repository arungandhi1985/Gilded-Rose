import React from 'react';
import { Item } from '../types/Item';

interface InventoryListProps {
  items: Item[];
}

const InventoryList: React.FC<InventoryListProps> = ({ items }) => {
  return (
    <div className="inventory-list">
      <h2>Current Inventory</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Sell In (days)</th>
            <th>Quality</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item, index) => (
            <tr key={index} className={item.name === 'NO SUCH ITEM' ? 'invalid-item' : ''}>
              <td>{item.name}</td>
              <td>{item.sellIn}</td>
              <td>{item.quality}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default InventoryList;