#EStoreBackEnd
A Java web application using spring boot 3.4.0 and running with JRE 17.
[[TOC]]

#Project Overview:

This project leverages the power of Spring WebFlux to build a modern, reactive web application with asynchronous APIs, making it fast, scalable, and resilient. The backend is powered by an H2 database, offering an in-memory solution for rapid development and testing. The project follows a well-structured multilayered architecture with a clean separation of concerns, ensuring maintainability and scalability.

#Key Features:
Spring WebFlux for Asynchronous APIs: Utilizing Spring WebFlux, the project provides non-blocking, asynchronous APIs that can efficiently handle large volumes of requests. This allows the application to scale seamlessly, providing high performance even under heavy load.

Resilient External API Calls with WebClient: The application includes a robust LoadProducts API that fetches product data from an external website using WebClient Builder. This call is optimized for resilience, ensuring reliability even when interacting with external services. The API call is designed to handle intermittent failures gracefully, minimizing disruption to the user experience.

Data Validation: To ensure that incoming data is valid, various validation mechanisms are applied at different layers of the application. The project ensures that the data used in operations is consistent, preventing any issues caused by invalid inputs.

Centralized Exception Handling with @ControllerAdvice: Exception handling is streamlined using @ControllerAdvice, providing a centralized location to manage and customize error responses. This ensures that exceptions are caught and handled appropriately, offering clear, user-friendly error messages when things go wrong.

Well-Defined Architecture: The application follows a multilayered architecture with clear boundaries between DAO, DTO, and the Application Layer. This promotes separation of concerns, making the codebase easier to maintain and extend.

Controller Layer: Manages incoming requests and delegates to service layer.
Service Layer: Contains the business logic and interacts with the repository layer.
Repository Layer: Handles the persistence logic and data access operations.
Modularized and Scalable Design: The application is modularized into distinct packages:

Controller: Handles HTTP requests and responses.
Service: Contains the core business logic.
Service Interface: Defines service methods for abstraction.
Repository: Interacts with the database for CRUD operations.
Available APIs: The following APIs are exposed to interact with the product data:

Get all products: Retrieves a list of all available products in the system.
Filter products by category: Fetches products based on their category, enabling users to narrow down their search.
Sort products by price: Sorts products by price in either ascending or descending order, providing flexible sorting options for users.
Find product by ID or SKU: Allows users to search for a specific product by its unique ID or SKU, ensuring fast and direct product retrieval.
JUnit Testing and Code Coverage: Comprehensive JUnit tests have been implemented across the application, ensuring that key features and business logic work as expected. Code coverage is ensured to maintain quality and reliability, providing confidence in the application's robustness and stability.

#Overall:
This project combines modern, reactive technologies with a well-thought-out architecture to deliver a scalable, maintainable, and resilient solution. By using Spring WebFlux and WebClient, the application ensures optimal performance and seamless integration with external systems. Coupled with extensive validation, exception handling, and a modular design, the project stands as a high-quality backend solution for managing product data. Whether it's loading products from external sources, filtering them by category or price, or providing easy access to product details, this application is designed to provide both flexibility and reliability in handling large-scale product data.

#How to run this project
#Software requirements
List of tools to be installed:
IDE - we recommend IntelliJ Community Edition
JDK 17 (can be downloaded by IntelliJ)
Maven v3.8 - build tool (can be downloaded by IntelliJ and embedded in the repo mvnw)

#How to start developing
Clone the repo, and open it in IntelliJ. Select the correct SDK (JDK 17) in File → project structure.

#How to run locally

H2_DATABASE_URL=jdbc:h2:mem:testdb
H2_DATABASE_USERNAME=sa
H2_DATABASE_PASSWORD=password
PRODUCTS_LOAD_URL=https://dummyjson.com/products

Add above properties as env varialbes in your IDE before starting the application.