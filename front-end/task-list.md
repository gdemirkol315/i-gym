
# Frontend Task List (Angular with Angular Material)

## 1. Application Setup
- Initialize Angular project and integrate **Angular Material** for UI components.
- Configure routing for different modules (Dashboard, Customer Registration, Inventory Management).

## 2. Authentication & Authorization
- Implement a login screen with role-based routing.
- Use JWT tokens to authenticate requests and store tokens in local storage.
- Role-based access to routes:
  - Only `MANAGER` role can access employee management and pricing screens.
  - Employees have restricted access based on role. 

## 3. Main Screen for Employee Actions
- Create a main screen with QR code input:
  - Integrate a barcode scanner component.
  - Display customer’s photo, subscription status, credits, and a history of usage.
- Implement a UI to show a green check or red “no credit” indication after scanning QR code.
- Add an option for manually adding new customers from this screen.

## 4. Customer Registration Form Integration
- Create a form with fields for customer info and consents.
- Capture and upload customer photo using webcam API.
- Send form data to backend upon submission.

## 5. Product and Inventory Management UI
- Build a list view for products, allowing employees to manage stock levels.
- Integrate a purchase interface where customers can buy products using their balance.

## 6. Subscription and Payment Management
- Display available subscription plans on a modal/dialog.
- Allow customers to load money onto their accounts:
  - Provide UI to view current account balance.
  - Implement a credit loading option with dropdown for amounts.

## 7. Navigation & Layout
- Implement responsive layout with Angular Material’s toolbar, sidebar, and grid components.
- Organize navigation routes for quick access to Dashboard, Customer Management, and Product Management.

## 8. Styling & UX Enhancements
- Use Angular Material themes to style the app.
- Optimize button placements and UX for faster operation at reception.
- Add icons for a clean, intuitive look.