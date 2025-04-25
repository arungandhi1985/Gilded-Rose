import React from 'react';

interface UpdateButtonProps {
  onUpdate: () => void;
  isLoading: boolean;
}

const UpdateButton: React.FC<UpdateButtonProps> = ({ onUpdate, isLoading }) => {
  return (
    <div className="update-button-container">
      <button 
        className="update-button" 
        onClick={onUpdate}
        disabled={isLoading}
      >
        {isLoading ? 'Updating...' : 'Update Inventory (Advance One Day)'}
      </button>
    </div>
  );
};

export default UpdateButton;