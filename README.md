# Employee Management Service

This project is a Spring Boot application that provides an Employee Management API with functionalities for creating, reading, updating, and deleting employee records. It integrates Swagger for API documentation and uses Maven for dependency management. The service interacts with an external employee management API hosted at `http://localhost:8082/api/employees` for all operations, redirecting API calls accordingly.

## Features

- **Employee CRUD Operations**:
    - Create a new employee (calls external API)
    - Retrieve an employee by ID (calls external API)
    - Retrieve all employees (calls external API)
    - Update employee details (calls external API)
    - Delete an employee (calls external API)

- **Role-Based Access Control (RBAC)**:
    - Admin role: Can create, delete employees.
    - User role: Can view and update employees.

- **Redirect Handling**: Each CRUD operation redirects to the corresponding endpoint of the external API for employee management (`http://localhost:8082/api/employees`).

- **Swagger Integration**: API documentation is available through Swagger UI.

- **Retry Mechanism**: The application uses Spring Retry for handling server errors and retries for REST API calls.

## Technologies Used

- **Java**: Programming language used for the backend service.
- **Spring Boot**: Framework for building the application.
- **Maven**: Dependency management and build tool.
- **Spring Security**: For securing the API with roles.
- **Swagger**: For API documentation.
- **Spring Retry**: For retrying HTTP calls in case of server errors.
- **H2 Database**: In-memory database for storing employee data (if applicable).
- **Spring Data JPA**: For data access.

## Prerequisites

To run this application locally, you need the following:

- **Java 17
- **Maven 3.8+**
- **Spring Boot 3.1.x or later**

## Installation

1. Clone the repository from GitHub:

   ```bash
   git clone https://github.com/vashisthsoni27/employee-service-api.git
    ```

2. Clone the repository from GitHub:

    ```bash
   cd employee-management-service
    ```
    
3. Build the project with Maven:
    ```bash
   mvn clean install
    ```
4. Run the Spring Boot application:

    ```bash
   mvn spring-boot:run
    ```
   
5. The application will start on http://localhost:8080 by default.
6. Ensure that the external API at http://localhost:8082/api/employees is running before using this service. This is where all CRUD operations for employees are delegated.

## API Documentation (Swagger UI)
Once the application is running, you can access the Swagger UI documentation by visiting:

http://localhost:8080/swagger-ui.html
This will provide you with a graphical interface to interact with the API.

---

## Code Coverage Report

The code coverage report can be found here (target/site/index.html).

Current code coverage report is here: https://github.com/vashisthsoni27/employee-service-api/blob/main/Code_Coverage.png

To generate the report yourself, run:

```bash
mvn clean test
```

---

## API Endpoints
Each CRUD operation redirects to an endpoint on the external API (http://localhost:8082/api/employees). Below are the details of how the operations are handled:

### 1. Get All Employees
   - URL: /api/employees
   - Method: GET
   - Roles Allowed: ADMIN, USER
   - Description: Retrieves a list of all employees by calling the external API (http://localhost:8082/api/employees).
   - Redirection: The request is forwarded to http://localhost:8082/api/employees and a list of all employees is returned.
   - Response: List of employees
### 2. Get Employee by ID
   - URL: /api/employees/{id}
   - Method: GET
   - Roles Allowed: ADMIN, USER
   - Description: Retrieves an employee by their unique ID by calling the external API (http://localhost:8082/api/employees/{id}).
   - Redirection: The request is forwarded to http://localhost:8082/api/employees/{id} and returns the employee data if found, or an error message if not found.
   - Response: Employee data
### 3. Create Employee
   - URL: /api/employees
   - Method: POST
   - Roles Allowed: ADMIN
   - Description: Creates a new employee in the external employee service (http://localhost:8082/api/employees).
   - Redirection: The request is forwarded to http://localhost:8082/api/employees with the employee data, and the created employee details are returned.
   - Request Body: Employee details
   - Response: Created employee data
### 4. Update Employee
   - URL: /api/employees/{id}
   - Method: PUT
   - Roles Allowed: ADMIN, USER
   - Description: Updates an existing employee in the external service (http://localhost:8082/api/employees/{id}).
   - Redirection: The request is forwarded to http://localhost:8082/api/employees/{id} with the updated employee data.
   - Request Body: Updated employee details
   - Response: Updated employee data
### 5. Delete Employee
   - URL: /api/employees/{id}
   - Method: DELETE
   - Roles Allowed: ADMIN
   - Description: Deletes an employee by calling the external API (http://localhost:8082/api/employees/{id}).
   - Redirection: The request is forwarded to http://localhost:8082/api/employees/{id} to delete the employee.
   - Response: Success or failure message 

## Redirect Logic
   For all the above operations, the EmployeeService makes use of RestTemplate to forward the requests to the external API at http://localhost:8082/api/employees. This means that any action performed in the current service (such as creating, updating, or deleting an employee) will trigger a corresponding request to the external service.

### Example Workflow:
#### 1. Create Employee:

- When a POST request is made to /api/employees, the EmployeeService takes the employee data and forwards it to the external service at http://localhost:8082/api/employees.
- If the external API responds successfully, the employee is created and the response is sent back to the client.

#### 2. Get Employees:

- When a GET request is made to /api/employees, the EmployeeService forwards the request to http://localhost:8082/api/employees.
- The external API returns the list of employees, which is then returned by the service.

#### 3. Update Employee:

- When a PUT request is made to /api/employees/{id}, the EmployeeService forwards the request to http://localhost:8082/api/employees/{id} with the updated employee data.
#### 4. Delete Employee:

- When a DELETE request is made to /api/employees/{id}, the request is forwarded to http://localhost:8082/api/employees/{id} to delete the employee.

## Configuration
### Security
**Roles**: ADMIN and USER

- ADMIN role can create and delete employees.
- USER role can view and update employee records.

### Swagger URLs:

- Swagger UI is accessible at /swagger-ui.html.
- API documentation is available at /v3/api-docs.

#### Retry Configuration
- The application uses Spring Retry to automatically retry HTTP requests to the external employee API in case of server errors (HttpServerErrorException, ResourceAccessException).




