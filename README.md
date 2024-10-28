# Products & Achat Microservices
 
This project consists of two Spring Boot microservices: 
**Products** and **Achat**. 
The system is designed to store products in EUR (Euro) currency and enables shopping for one or multiple products in different currencies. 
The **Achat** microservice calculates real-time exchange rates using the Exchange Rate API. 
The services communicate with each other using **WebClient**.



### 1. Products Microservice
- **Port**: 8080
- **Description**: This microservice handles CRUD (Create, Read, Update, Delete) operations on products.
- **Entity**: Product
- **Endpoints**:
    - `POST /api/products`: Create a new product
    - `GET /api/products`: Retrieve all products
    - `GET /api/products/{id}`: Retrieve a product by ID
    - `PUT /api/products/{id}`: Update an existing product
    - `DELETE /api/products/{id}`: Delete a product by ID
     

### 2. Achat Microservice
- **Port**: 8081
- **Description**: This microservice manages shopping operations. It allows users to purchase one or more products, with support for different currencies. The total amount is calculated in real-time using the exchange rate data from the Exchange Rate API.
- **Endpoints**:
    - `POST /api/achat`: Make a purchase by providing product IDs and the desired currency. The service fetches the product details from the Products microservice and uses the Exchange Rate API to convert prices as needed.
### Exemple d'Utilisation avec Postman

#### Création d'un produit
![POST Product Test](/screens/addNewProduct.PNG)

#### Récupération de tous les produits
![GET All Products Test](/screens/getAllProduct.PNG)

#### Récupération des achats
![GET Achat Test](/screens/getAchat.PNG)
### Base de données

#### Création d'un produit
![POST Product Test](/screens/db.PNG)
#### API Request Quota Usage
![GET All Products Test](/screens/quotasapi.jpg)