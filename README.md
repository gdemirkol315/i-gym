# i-gym
# Bouldering Gym Reception App

## Project Overview
This application serves as a reception management system for bouldering gyms. Key features include customer registration, entry tracking, subscription management, product purchases, and credit handling.

## Features

1. **Customer Registration**
   - A registration form, accessible on a tablet, allows customers to input information, sign consents, and have a photo taken.
   - Each new customer receives a unique QR/barcode for entry.

2. **Entry Management**
   - Reception staff can manage customer entries, including one-time, subscription, and multi-pass options.
   - Entry usage is logged automatically in **EntryConsumption** with each customer visit.

3. **Subscription and Multi-Pass Tracking**
   - The app supports various subscription plans and multi-pass options.
   - **SubscriptionPlans** offers flexible management of different subscription types.

4. **Payment & Credit Management**
   - Customers can load money into their accounts to cover future entries or purchases.
   - All transactions are logged in **AccountTransactions** for clear tracking.

5. **Product Management**
   - Reception staff can manage inventory for items like snacks, drinks, and climbing supplies.
   - **ProductConsumption** tracks individual product purchases, and stock adjusts automatically.

6. **Consent Management**
   - **ConsentForms** tracks multiple customer agreements (e.g., liability waivers).
   - Allows viewing of customer consent history for legal compliance.

7. **Employee Accountability**
   - Employees log in with unique credentials.
   - **EmployeeActions** records each employee’s actions for transparency and accountability.

## Tech Stack

- **Frontend**: React with a component-based UI.
- **Backend**: Java Spring with Spring Hibernate for database interactions.
- **Database**: PostgreSQL, storing customer, product, transaction, and entry information.
- **QR/Barcode Generation**: Library TBD.

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd bouldering-gym-reception-app
        