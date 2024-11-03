# Task List for Bouldering Gym Reception App

## 1. Database Design and Setup
- Define tables:
  - **Customers**: Basic customer information, consent status, photo, account balance.
  - **Entries**: Manages gym entry records, including subscription and multi-pass tracking.
  - **Products**: Inventory items (e.g., snacks, drinks, climbing supplies).
  - **EntryConsumption**: Logs each gym entry, tracking usage and subscription status.
  - **ProductConsumption**: Records product purchases made by customers.
  - **EmployeeActions**: Tracks employee actions (e.g., processing entries, handling purchases).
  - **Employees**: Stores employee login credentials and basic details.
  - **AccountTransactions**: Logs all financial transactions, such as deposits, purchases, and refunds.
  - **SubscriptionPlans**: Manages available subscription types, allowing for flexible updates.
  - **ConsentForms**: Stores various consents signed by customers (e.g., liability waiver, photo consent).
- Use **Spring Hibernate** for ORM and database management.

## 2. Authentication & Authorization
- Implement employee login with role-based permissions.
- Log all actions tied to specific employees for accountability.
- Users can change their passwords and contact information in the system

## 3. Customer Registration
- Create a tablet-accessible web form for customer registration.
- Collect customer information, required consents, and capture a photo via a webcam.
- Store consents in the **ConsentForms** table for each customer.

## 4. QR/Barcode Generation
- Generate a unique QR for each new customer.
- Enable email delivery of the QR/barcode for entry purposes.

## 5. Main screen
- Main Screen of the app should allow employee to scan a barcode and after scanning the barcode:
  - View current customer subscription, credits, foto and usage history.
  - Manage purchases of one-time entries, subscriptions, or multi-passes.
  - Track and mark usage for multi-pass entries.
- Automatically deduct from subscriptions or multi-pass credits with each entry.
- So the aim is after scanning customers qr code, employee can see customers foto on the main screen, and automatically check customers subscription or entry right, and show a green check or red no credit. To make this process of entering gym faster.
- app should also allow employee to manually enter a new customer instead of a customer registration form mentioned above in case that is not working bc of some reason employee should be able to enter a new customer.

## 6. Payment & Credit System
- Allow customers to load money onto their accounts for future entries or purchases.
- Record all transactions in the **AccountTransactions** table. 

## 7. Product & Inventory Management
- Set up a simple UI for tracking inventory (snacks, drinks, climbing supplies).
- Deduct from inventory in the **Products** table upon each purchase, logged in **ProductConsumption**.

## 8. Subscription Management
- Use **SubscriptionPlans** to manage different subscription types (e.g., monthly, 10-pass).
- Display subscription details on the dashboard for easy customer reference.
- Automatically update usage counts for multi-pass subscriptions.

## 9. Consent Handling
- Store signed consents in **ConsentForms** to handle multiple agreements per customer.
- Display consent records for easy review by staff as needed.

## 10. UI/UX Development
- Design a basic, responsive UI using predefined components (similar to Angular Material).
- Ensure the interface is intuitive and optimized for quick use at the reception.

## 11. Roles
- MANAGER, EMPLOYEE
- Managers will be able to set prices, create new employee accounts
- Employees cannot change prices, cannot create new employee accounts

