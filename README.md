# Banking Application Service

This is the application layer for a three-tier banking system. It provides business logic and connects to the database tier at https://databasetier.onrender.com.

## Features

- REST API for account and transaction operations
- RMI service for backward compatibility with existing clients
- Connection to the database tier via HTTP/REST
- Caching for improved performance
- Ready for deployment to Render

## Project Structure

```
banking-application-service/
├── pom.xml                                  # Maven configuration
├── Dockerfile                               # Docker configuration for Render
├── render.yaml                              # Render blueprint configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── banking/
│   │   │           └── application/
│   │   │               ├── BankingApplicationService.java    # Main Spring Boot class
│   │   │               ├── config/                           # Configuration classes
│   │   │               │   ├── RmiServerConfig.java          # RMI service config
│   │   │               │   └── WebClientConfig.java          # WebClient config for HTTP
│   │   │               ├── controller/                       # REST controllers
│   │   │               │   └── AccountController.java        # Account REST API
│   │   │               ├── dto/                              # Data transfer objects
│   │   │               ├── exception/                        # Exception classes
│   │   │               ├── model/                            # Domain models
│   │   │               │   ├── Account.java                  # Account model
│   │   │               │   └── Transaction.java              # Transaction model
│   │   │               └── service/                          # Service layer
│   │   │                   ├── AccountService.java           # Account service interface
│   │   │                   ├── impl/                         # Service implementations
│   │   │                   │   └── AccountServiceImpl.java   # Account service implementation
│   │   │                   └── rmi/                          # RMI service
│   │   │                       ├── AccountRegistry.java      # RMI registry interface
│   │   │                       ├── AccountRegistryImpl.java  # RMI registry implementation
│   │   │                       ├── RemoteAccount.java        # Remote account interface
│   │   │                       └── RemoteAccountImpl.java    # Remote account implementation
│   │   └── resources/
│   │       └── application.properties                        # Application configuration
│   └── test/                                                 # Test directory
```

## API Endpoints

### REST API:

- `POST /api/accounts` - Create a new account
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{accountNumber}` - Get a specific account
- `POST /api/accounts/{accountNumber}/deposit` - Deposit money
- `POST /api/accounts/{accountNumber}/withdraw` - Withdraw money
- `POST /api/accounts/{accountNumber}/transfer` - Transfer money
- `GET /api/accounts/{accountNumber}/transactions` - Get transaction history

### RMI Services:

- `AccountRegistry` - Service for retrieving and creating accounts
- `RemoteAccount` - Interface for account operations

## Running Locally

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/banking-application-service.jar

# Or with custom properties
java -DDATABASE_TIER_URL=https://your-db-tier-url.com -jar target/banking-application-service.jar
```

## Deploying to Render

1. Push this project to a Git repository
2. Log in to your Render account
3. Create a new Web Service using the "Blueprint" option
4. Connect your repository
5. The service will be automatically deployed based on render.yaml
