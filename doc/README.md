# Spring Boot API

## 📌 Project Overview

A **RESTful API** built with **Spring Boot** that provides **authentication** and **user management**. The API
integrates **JWT for authentication**.

## ⚙️ Prerequisites

- **Java 11**
- **Gradle 7.4**

## 🚀 Installation & Setup

### 📂 Clone the repository:

```sh
git clone https://github.com/maximogiordano/users.git
cd users
```

### ▶️ Run the application:

Using **Gradle**:

```sh
./gradlew bootRun
```

### 📌 Example Endpoint:

Run the following command to create a user:

```shell
curl --location 'localhost:9090/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "MAXIMO GIORDANO",
    "email": "maximogiordano@hotmail.com",
    "password": "P44ssword",
    "phones": [
        {
            "number": 58949037,
            "citycode": 11,
            "countrycode": "AR"
        }
    ]
}'
```

Response:

```json
{
  "id": "12143e56-fbbf-4676-a526-f08dcc8d6b7c",
  "name": "MAXIMO GIORDANO",
  "email": "maximogiordano@hotmail.com",
  "password": "$2a$10$7JfzhRByHLUXVkVhsASdHeUJ7E8XruQltJMQzkM1x0UD7IWPODmS.",
  "phones": [
    {
      "id": "c2b504f5-007d-43db-844a-5efe027f32d6",
      "number": 58949037,
      "citycode": 11,
      "countrycode": "AR"
    }
  ],
  "created": "2025-02-12T17:12:31.6998257-03:00",
  "lastLogin": "2025-02-12T17:12:31.6998257-03:00",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW9naW9yZGFub0Bob3RtYWlsLmNvbSIsImlhdCI6MTczOTM5MTE1MSwiZXhwIjoxNzM5MzkyMDUxfQ.cjnV_U6UAHIvb0z6o5XK_xP82MoSfTAMSsdZ769ULGc",
  "isActive": true
}
```

## 🔑 Authentication & Security

The API uses **JWT Authentication**.

Use the previously returned token to login as follows:

```shell
curl --location --request POST 'localhost:9090/login' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW9naW9yZGFub0Bob3RtYWlsLmNvbSIsImlhdCI6MTczOTM5MTE1MSwiZXhwIjoxNzM5MzkyMDUxfQ.cjnV_U6UAHIvb0z6o5XK_xP82MoSfTAMSsdZ769ULGc'
```

Response:

```json
{
  "id": "12143e56-fbbf-4676-a526-f08dcc8d6b7c",
  "name": "MAXIMO GIORDANO",
  "email": "maximogiordano@hotmail.com",
  "password": "$2a$10$7JfzhRByHLUXVkVhsASdHeUJ7E8XruQltJMQzkM1x0UD7IWPODmS.",
  "phones": [
    {
      "id": "c2b504f5-007d-43db-844a-5efe027f32d6",
      "number": 58949037,
      "citycode": 11,
      "countrycode": "AR"
    }
  ],
  "created": "2025-02-12T17:12:31.699826-03:00",
  "lastLogin": "2025-02-12T17:20:52.2095084-03:00",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbW9naW9yZGFub0Bob3RtYWlsLmNvbSIsImlhdCI6MTczOTM5MTY1MiwiZXhwIjoxNzM5MzkyNTUyfQ.bTLijflm52_P7uquY1HsnLN9cm78yXPAwyWRn5VW9vs",
  "isActive": true
}
```

A new token will be returned each time, invalidating previous ones.
