# Gilded Rose Inventory Management System

This project implements an automated inventory management system for the Gilded Rose inn, handling various types of items with different quality adjustment rules.

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.3
- JUnit 5 for testing
- Jackson for JSON processing

### Frontend
- React 18
- TypeScript 5
- Axios for API calls

## Prerequisites

- JDK 17 or later
- Node.js 18 or later
- Maven 3.8 or later

## Running the Application

### Backend

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   npm start
   ```

   The frontend will be available at `http://localhost:3000`

## API Endpoints

### Get Inventory
- **Endpoint**: `GET /api/inventory`
- **Description**: Get the current inventory
- **Success Response**: 
  - **Code**: 200 OK
  - **Content**: Array of Item objects
- **Error Responses**:
  - **Code**: 500 INTERNAL SERVER ERROR
    - When file I/O error occurs
    - When JSON parsing fails
  
### Update Inventory  
- **Endpoint**: `POST /api/inventory/update`
- **Description**: Update the inventory for one day
- **Request Body**: JSON array of Item objects
  ```json
  [
    {"name": "Aged Brie", "sellIn": 1, "quality": 1},
    {"name": "Normal Item", "sellIn": 2, "quality": 2}
  ]
  ```
- **Success Response**:
  - **Code**: 200 OK
  - **Content**: Array of updated Item objects
- **Error Responses**:
  - **Code**: 400 BAD REQUEST
    - When inventory is empty or null
  - **Code**: 500 INTERNAL SERVER ERROR
    - When file I/O error occurs
    - When unexpected errors occur

### Health Check
- **Endpoint**: `GET /api/inventory/health`
- **Description**: Check if the service is running
- **Success Response**: 
  - **Code**: 200 OK
  - **Content**: "Service is running"

## Assumptions and Interpretations

Based on the requirements and test data, the following interpretations were made:

1. **"Once the sell by date has passed, Quality degrades twice as fast"** - This general rule is interpreted to mean:
   - Normal items: Quality decreases by 2 after expiration (normally decrease by 1)
   - Conjured items: Quality decreases by 4 after expiration (normally decrease by 2)
   - Aged Brie: Quality increases by 2 after expiration (normally increases by 1)
        While not explicitly stated in the requirements, I have implemented Aged Brie to increase in quality twice as fast (by 2) after its sell-by date passes, following the general rule.

2. **Edge cases** - When an item is not recognized by the system, it is marked as "NO SUCH ITEM" as shown in the test data. The current implementation preserves the original SellIn and Quality values while changing the name, as the API returns structured JSON objects. If the requirement is to completely omit these values, the implementation can be adjusted accordingly.

## Testing

Run backend tests with:
```
cd backend
mvn test
```

Run frontend tests with:
```
cd frontend
npm test
```