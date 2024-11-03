# Backend Task List (Spring Boot, Hibernate, Spring Security)

## 1. Database Schema Design
- Define entities with Hibernate:
  - **customers**: Fields for id, name, last_name, email, photo_url.
  - **consent_forms**: Fields for id, customer_id (FK), consent_type, date_signed.
  - **entry_products**: Fields for id, name, duration, max_entries, price, entry_type.
  - **entry_consumption**: Fields for id, entry_id (FK), customer_id(FK), date.
  - **products**: Fields for id, name, category, price, quantity_in_stock.
  - **product_consumption**: Fields for id, customer_id (FK), product_id (FK), quantity, date.
  - **account_transactions**: Fields for id, customer_id (FK), transaction_type, amount, date.
  - **employee_actions**: Fields for id, employee_id (FK), action_type, target_id, date_time.
  - **employees**: Fields for ID, name, position, role.
  - **credentials**: Fields for employee_id, hashed_pass
  - **salt**: Fields for employee_id, salt
  customers -> constent_forms OneToMany
  entry_products -> entry_consumption OneToMany
  customers -> entry_consumption OneToMany
  products -> product_consumption OneToMany
  customers -> product_consumption OneToMany
  customers -> account_transactions OneToMany
  employees -> employee_actions OneToMany
  employees - credentials - salt OneToOne

## 2. Authentication and Authorization (Spring Security)
- Set up **role-based access control**:
  - Define roles `MANAGER` and `EMPLOYEE`.
  - Restrict permissions so only `MANAGER` can create employees, set prices, and access sensitive data.
- Implement password encryption and secure storage using BCrypt and with static salting. salt will be some random string that will be created upon user creation and will be stored in db.
- Create endpoints to manage:
  - Employee login, password reset, and role-based permissions.
  - Role-based access to modify product prices and create new employee accounts.
 - After the user submits their login credentials, the backend verifies them.If the credentials are valid, the backend generates a JWT token, embedding user information and roles (e.g., MANAGER, EMPLOYEE). This token is then sent back to the frontend as part of the login response.

## 3. Customer Registration Logic
- Create REST endpoints:
  - **POST /customers**: Register new customer, including consent handling and photo storage.
  - **GET /customers/{id}**: Retrieve customer data.
  - **PUT /customers/{id}/consents**: Update consent records.
- Implement QR code generation and email service to send QR codes.

## 4. Customer Management Endpoints
- Create CRUD endpoints for:
  - Customer entries and subscriptions (automatically deducting from multi-pass credits).
  - Managing subscriptions, credit loading, and purchase records.
- Implement transaction logging in **AccountTransactions**.

## 5. Product and Inventory Management
- Define CRUD endpoints for:
  - Product management (`/products` endpoint), including quantity updates upon purchase.
- Log all purchases in **ProductConsumption**.

## 6. Subscription Management
- Endpoints for creating and updating subscription plans, handled by `MANAGER` role.
- Logic for tracking subscription status (remaining uses, expiry date).

## 7. Employee Actions Tracking
- Track each employee action using **EmployeeActions** table.
- Capture events such as new customer registrations, purchases, entry checks.

## 8. Best Practices
- Implement exception handling with global exception handler.
- Add validation for all DTOs.
- Use `@Transactional` for atomic transactions.
- Structure services and repositories according to domain-driven design.

