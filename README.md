## Dokumentering af Medical Appointment API
Project: Eksamen - Backend - 3. semester
Date: 04.11.2024

## Beskrivelse:
-- API NAME -- 
API er en RESTful webtjeneste, der giver mulighed for at oprette, læse, opdatere og slette
læger og deres aftaler. API'et understøtter også brugerregistrering og godkendelse med JWT-token-baseret
autentificering. API'et er bygget med Java og Javalin og bruger en PostgreSQL-database til at gemme data.

## Task 1 - Setup:
- Create a new Java Project for javalin and JPA.

- Made a README.md file.

## Task 2 - JPA and DAOs:
- Made a HibernateConfig class
- Implemented trip entity
- Implemented guide entity

- Implemented DAOs for trip and guide
- Implemented DTOs for trip and guide

- Created IDAO with CRUD operations (create, getAll, getById, update, delete)
- Created TripDAO and GuideDAO classes that implement IDAO

- Made TripDAO also implement another interface: ITripGuideDAO

- Created a populate class to populate the database with some data

## Task 3 - Building a REST Service Provider with Javalin:

- Created TripController class
- Create a TripRoutes file that uses the TripController to handle the API requests.

- Implemented TripRoutes - Testing in DB.http
- Populate:
{
  "Message": "The Database has been populated"
  }
- Get all:
  [
  {
  "id": 1,
  "name": "Trip to Miami Beach",
  "price": "1200",
  "starttime": "2024-06-01 08:00",
  "endtime": "2024-06-07 20:00",
  "longitude": "-80.1918",
  "latitude": "25.7617",
  "category": "BEACH",
  "guides": []
  },
  {
  "id": 2,
  "name": "Trip to New York City",
  "price": "1500",
  "starttime": "2024-07-01 09:00",
  "endtime": "2024-07-10 21:00",
  "longitude": "-74.0060",
  "latitude": "40.7128",
  "category": "CITY",
  "guides": []
  },
  {
  "id": 3,
  "name": "Trip to Lake Tahoe",
  "price": "1800",
  "starttime": "2024-09-01 11:00",
  "endtime": "2024-09-10 23:00",
  "longitude": "-120.044",
  "latitude": "39.0968",
  "category": "LAKE",
  "guides": []
  },
  {
  "id": 4,
  "name": "Trip to Amazon Forest",
  "price": "2000",
  "starttime": "2024-08-01 10:00",
  "endtime": "2024-08-15 22:00",
  "longitude": "-60.0258",
  "latitude": "-3.4653",
  "category": "FOREST",
  "guides": []
  },
  {
  "id": 5,
  "name": "Trip to Paris",
  "price": "1600",
  "starttime": "2024-11-01 13:00",
  "endtime": "2024-11-10 18:00",
  "longitude": "-112.115",
  "latitude": "36.1069",
  "category": "CITY",
  "guides": []
  },
  {
  "id": 6,
  "name": "Trip to Mediterranean Sea",
  "price": "1400",
  "starttime": "2024-10-01 12:00",
  "endtime": "2024-10-07 19:00",
  "longitude": "18.4241",
  "latitude": "33.9249",
  "category": "SEA",
  "guides": []
  }
  ]

- Get by id:
  {
  "id": 3,
  "name": "Trip to Lake Tahoe",
  "price": "1800",
  "starttime": "2024-09-01 11:00",
  "endtime": "2024-09-10 23:00",
  "longitude": "-120.044",
  "latitude": "39.0968",
  "category": "LAKE",
  "guides": []
  }

- Create: (Worked first time and secondtime it did not work)
  {
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid Trip data",
  "timestamp": "2024-11-04 12:21:23"
  }

- Update:
OLD:
  {
  "id": 6,
  "name": "Trip to Paris",
  "price": "1600",
  "starttime": "2024-11-01 13:00",
  "endtime": "2024-11-10 18:00",
  "longitude": "-112.115",
  "latitude": "36.1069",
  "category": "CITY",
  "guides": []
  }

UPDATED:
{
"id": 6,
"name": "Tur til Australien",
"price": "300",
"starttime": "2023-10-01T10:00:00",
"endtime": "2023-10-01T18:00:00",
"longitude": "133.775136",
"latitude": "-25.274398",
"category": "BEACH",
"guides": []
}

- Delete: (Worked first time and secondtime it did not work)
  {
  "status": 500,
  "error": "Internal Server Error",
  "message": "Internal server error",
  "timestamp": "2024-11-04 12:22:19"
  }

- ### Her henter vi trips ud fra type
[
{
"id": 1,
"name": "Trip to Miami Beach",
"price": "1200",
"starttime": "2024-06-01 08:00",
"endtime": "2024-06-07 20:00",
"longitude": "-80.1918",
"latitude": "25.7617",
"category": "BEACH",
"guides": []
}
]


## Task 4 - REST Error Handling:
- Trip id not exist:
  {
  "status": 404,
  "error": "Not Found",
  "message": "Trip with ID 10 not found",
  "timestamp": "2024-11-04 12:28:09"
  }

## Task 5 - Streams and queries:
- See trip controller for the implementation of filter by category

## Task 6 - Getting additional data from API:
- Made getPackingItems in TripController
- And it fetches the items bades on the category of the trip: Test for Beach:
  {
  "items": [
  {
  "name": "Beach Umbrella",
  "weightInGrams": 1200,
  "quantity": 1,
  "description": "Sunshade umbrella for beach outings.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Sunny Store",
  "shopUrl": "https://shop3.com",
  "price": 50
  },
  {
  "shopName": "Beach Essentials",
  "shopUrl": "https://shop4.com",
  "price": 55
  }
  ]
  },
  {
  "name": "Beach Water Bottle",
  "weightInGrams": 500,
  "quantity": 1,
  "description": "High-capacity water bottle for hot climates.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Hydration Depot",
  "shopUrl": "https://shop6.com",
  "price": 25
  }
  ]
  },
  {
  "name": "Beach Cooler",
  "weightInGrams": 3000,
  "quantity": 1,
  "description": "Insulated cooler to keep beverages cold at the beach.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Beach Supplies",
  "shopUrl": "https://shop21.com",
  "price": 70
  }
  ]
  },
  {
  "name": "Beach Towel",
  "weightInGrams": 300,
  "quantity": 1,
  "description": "Large, quick-drying beach towel.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Beach Essentials",
  "shopUrl": "https://shop1.com",
  "price": 15
  }
  ]
  },
  {
  "name": "Beach Ball",
  "weightInGrams": 100,
  "quantity": 1,
  "description": "Inflatable beach ball for games.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Beach Fun Shop",
  "shopUrl": "https://shop2.com",
  "price": 5
  }
  ]
  },
  {
  "name": "Sunscreen SPF 50",
  "weightInGrams": 200,
  "quantity": 1,
  "description": "High-SPF sunscreen for beach days.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Sunny Shop",
  "shopUrl": "https://shop3.com",
  "price": 10
  }
  ]
  },
  {
  "name": "Beach Chair",
  "weightInGrams": 2000,
  "quantity": 1,
  "description": "Foldable, lightweight beach chair.",
  "category": "beach",
  "createdAt": "2024-10-30T17:44:58.547Z",
  "updatedAt": "2024-10-30T17:44:58.547Z",
  "buyingOptions": [
  {
  "shopName": "Beach Supplies",
  "shopUrl": "https://shop4.com",
  "price": 25
  }
  ]
  }
  ]
  }
## Task 7 - Testing REST Endpoints:
- Created test class
- And created test for CRUD operations
- Tying to make the test object in beforEach and trought a populate class in the test but somthing goes wrong in creating the object in the test class.

## Task 8 - Security:
- Added a security layer to the API using JWT
- Creating user , logging in , and adding role to the user all implemtend.













