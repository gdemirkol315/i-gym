# Frontend Registration Form Task List (Angular Material)

## 1. Form Structure and Fields
- Create a form with:
  - Basic fields: Name, contact info, email, and consent checkboxes.
  - Subscription options for quick selection.
  - Photo capture section using webcam API.

## 2. Photo Capture and Consent Handling
- Integrate the webcam API for photo capture.
- Display photo preview and allow for re-takes before submission.
- Store photo in a temporary file before uploading to backend.

## 3. Validation and Error Handling
- Add validation for required fields (e.g., email format, required consents).
- Implement error messages for invalid inputs or missing information.

## 4. QR Code Generation and Email
- Show a success message with customer details after form submission.
- Trigger backend call to generate QR code and send to customer’s email.

## 5. Backend Integration
- Connect form submission to the backend **/customers** endpoint.
- Include data binding for all form fields to send a comprehensive payload to the backend.

## 6. Responsive Design
- Ensure the form layout is responsive and works well on tablet screens.
- Use Angular Material’s grid layout to adjust the form based on screen size.

## 7. Error Handling and Retry Mechanism
- Implement error notifications if the form submission fails.
- Allow employees to retry form submission in case of network errors.

## 8. Best Practices
- Use reactive forms for better control over validation.
- Use Angular Material’s dialog for confirmation of form submission.
- Optimize for performance with onPush change detection strategy.
