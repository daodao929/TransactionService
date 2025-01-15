# Transaction Service

## How to start application
NOTE: [Front end application](https://github.com/daodao929/Banking-Transaction-Manager) provide a simple UI for this service.
```shell
docker-compose up transaction-app
```
Then can access application via http://localhost:8080

## Major function
### List Transactions
  #### Endpoint: GET /transactions
  Description: Retrieves a paginated list of transactions
  
**Request Parameters**
- page (optional): int, default=0
- size (optional): int, default=10

**Response**
  - Status: 200 OK
  - Body: Page<Transaction>

**Example**
  
GET http://localhost:8080/transactions?page=0&size=10
  
Response:
```json
  {
   "content": [
      {
         "transactionId": "TX001",
         "transactionType": "PAYMENT",
         "amount": 100.00,
         "currency": "USD",
         "result": "SUCCESS",
         "created": "2025-01-15T14:15:38.196346Z",
         "lastUpdated": "2025-01-15T14:15:38.196346Z"
      }
   ],
   "pageable": {
      "pageNumber": 0,
      "pageSize": 1,
      "sort": {
         "empty": true,
         "sorted": false,
         "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
   },
   "last": false,
   "totalPages": 30,
   "totalElements": 30,
   "size": 1,
   "number": 0,
   "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
   },
   "first": true,
   "numberOfElements": 1,
   "empty": false
}
```

### Create Transaction
#### Endpoint: POST /transactions
Description: Creates a new transaction

**Request Body**
```json
{
   "transactionId": "test111",
   "transactionType": "PAYMENT",
   "amount": 199.99,
   "currency": "AUD",
   "result": "FAILURE"
}
```
**Response**
  - Status: 201 Created
  - Body: Transaction
  
**Example**
 
 POST http://localhost:8080/transactions
  
Response body: 
```json
  {
   "transactionId": "test111",
   "transactionType": "PAYMENT",
   "amount": 199.99,
   "currency": "AUD",
   "result": "FAILURE",
   "created": "2025-01-15T14:44:55.461957675Z",
   "lastUpdated": "2025-01-15T14:44:55.461958717Z"
  }
```


### Delete Transaction
#### Endpoint: DELETE /transactions/{transactionId}
  Description: Deletes a specific transaction

**Path Parameters**
  - transactionId: string

**Response**
  - Status: 200 OK
  - Body: "Transaction deleted successfully"
  - Status: 404 Not Found (if transaction doesn't exist)

**Example**

  DELETE http://localhost:8080/transactions/TX001

### Update Transaction
#### Endpoint: PUT /transactions/{transactionId}
Description: Updates specific fields of an existing transaction

**Path Parameters**
  - transactionId: string

**Query Parameters (all optional)**
  - amount: BigDecimal
  - transactionResult: TransactionResult enum
  - currency: string

**Response**
  - Status: 200 OK
  - Body: Updated Transaction
  - Status: 404 Not Found (if transaction doesn't exist)

**Example**
  
PUT http://localhost:8080/transactions/TX001?amount=19999.00&currency=USD

Response body:
```json
  {
   "transactionId": "TX002",
   "transactionType": "REFUND",
   "amount": 19999.00,
   "currency": "USD",
   "result": "FAILURE",
   "created": "2025-01-15T14:15:38.196346Z",
   "lastUpdated": "2025-01-15T14:15:38.196346Z"
  }
```
**Error Responses**
  - 400 Bad Request: Invalid input data
  - 404 Not Found: Transaction not found
  - 500 Internal Server Error: Server-side error


## Stress test
This script performs load testing for the Transaction API endpoints.
1. Test Scenarios:
 * Ramp-up: Gradually increase load from 0 to 20 users over 30 seconds
 * Steady load: Maintain 20 concurrent users for 30 seconds 
2. Success Criteria:
 * 95% of requests should complete under 200ms
 * Error rate should be less than 1%
3. How to run:
 * Start the application: 
   ```shell
    docker-compose up transaction-app
    ```
 * Run the test: docker-compose run stress-test
    ```shell
    docker-compose run stress-test
    ```
FYI there is a stress test report named stressTestReport.txt under folder stressTest/,
you also could run command to do it again.


## Dependency Purposes

### Core Framework
- **spring-boot-starter-web**: Provides core Spring MVC capabilities for building RESTful APIs
- **spring-boot-starter-data-jpa**: Enables JPA-based data access and ORM functionality
- **spring-boot-starter-validation**: Adds validation support for request/response data

### Database Management
- **h2**: In-memory database for development and testing
- **flyway-core**: Database migration and version control

### Caching Solution
- **caffeine**: High-performance caching library
- **spring-boot-starter-cache**: Spring's caching abstraction

### Development Tools
- **lombok**: Reduces boilerplate code through annotations
- **jetbrains-annotations**: Additional code annotations for better IDE support

### Testing and Quality
- **spring-boot-starter-test**: Testing framework integration