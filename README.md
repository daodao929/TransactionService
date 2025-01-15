# Transaction Service

## How to start application
```shell
docker-compose up transaction-app
```
Then can access application via http://localhost:8080

## Major function
### list all transactions
GET http://localhost:8080/transactions?page=0&size=10
```shell
curl --location 'http://localhost:8080/transactions?page=0&size=33'
```

### insert a new transaction
POST http://localhost:8080/transactions

400 - invalid information

```shell
curl --location 'http://localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data '{
    "transactionId": "test111",
    "transactionType": "PAYMENT",
    "amount": 199.99,
    "currency": "AUD",
    "result": "FAILURE"
}'
```

### delete a new transaction
DELETE http://localhost:8080/transactions/TX001
```shell
curl --location --request DELETE 'http://localhost:8080/transactions/TX001' 
```
404 - transaction not found

### update a transaction
Could update amount, transactionResult or currency of an existing transaction
PUT http://localhost:8080/transactions/transactions/TX002?amount=19999.00&

```shell
curl --location --request PUT 'http://localhost:8080`/transactions/TX002?amount=19999.00`' 
```
404 - transaction not found

## How to run stress test
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