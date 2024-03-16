# Contact API Spec

### Table of Content
```
1. Contact Add
2. Contact Update
3. Contact List
4. Contact Detail
5. Contact Search
6. Contact Remove
```

### 1. Contact Add
1.1. Endpoint: **POST** `/api/contact/add`
1.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
1.3. Request Body:
```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@mail.com",
  "phone": 628000123456
}
```
1.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "Add Contact Success!",
  "data": {
    "id": "RANDOM STRING",
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@mail.com",
    "phone": 628000123456
  }
}
```
1.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "Add Contact email format invalid!",
  "data": []
}
```
1.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "Add Contact conflict by technical issues! Please to call Admin.",
  "data": []
}
```
### 2. Contact Update
2.1. Endpoint: **PUT** `/api/contact/update-{id}`
2.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
2.3. Request Body:
```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john@mail.com",
  "phone": 628000123456
}
```
2.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "Update Contact Success!",
  "data": {
    "id": "RANDOM STRING",
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@mail.com",
    "phone": 628000123456
  }
}
```
2.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "Update Contact email format is invalid!",
  "data": []
}
```
2.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "Update Contact conflict by technical issues! Please to call Admin.",
  "data": []
}
```
### 3. Contact List
3.1. Endpoint: **GET** `/api/contact/list`
3.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
3.3. Request Body:
```json
{}
```
3.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "Contact List Success!",
  "data": [
    {
      "first_name": "John",
      "last_name": "Doe",
      "email": "john@mail.com",
      "phone": 628000123456
    },
    {
      "first_name": "Elkan",
      "last_name": "Bagot",
      "email": "elkan@mail.com",
      "phone": 628000123457
    },
    {
      "first_name": "Olivia",
      "last_name": "Rodrigo",
      "email": "olivia@mail.com",
      "phone": 628000123458
    }
  ]
}
```
3.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "Contact List request wrong!",
  "data": []
}
```
3.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "Contact List conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 4. Contact Detail
4.1. Endpoint: **GET** `/api/contact/detail-{id}`
4.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
4.3. Request Body:
```json
{}
```
4.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "",
  "data": [
    {
      "first_name": "John",
      "last_name": "Doe",
      "email": "john@mail.com",
      "phone": 628000123456
    }
  ]
}
```
4.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "Contact Detail not found!",
  "data": []
}
```
.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "Contact Detail conflict by technical issues! Please to cal Admin.",
  "data": []
}
```

### 5. Contact Search
5.1. Endpoint: **GET** `/api/contact/search-{query}`

5.1.2. Query Params:
```
- name: String
- phone: Big Integer
- email: String
```
5.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
5.3. Request Body:
```json
{}
```
5.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "",
  "data": [
    {
      "first_name": "Elkan",
      "last_name": "Doe",
      "email": "john@mail.com",
      "phone": 628000123456
    },
    {
      "first_name": "Elkan",
      "last_name": "Bagot",
      "email": "elkan@mail.com",
      "phone": 628000123457
    },
    {
      "first_name": "Elkan",
      "last_name": "Rodrigo",
      "email": "olivia@mail.com",
      "phone": 628000123458
    }
  ],
  "pages": [
    {
      "current_page": 1,
      "page_size": 20,
      "total_page": 100
    }
  ]
}
```
5.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "",
  "data": []
}
```
5.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "",
  "data": []
}
```

### 6. Contact Remove
6.1. Endpoint: **DELETE** `/api/contact/remove-{id}`
6.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
6.3. Request Body:
```json
{}
```
6.4. Response Body (Success)

```json
{
  "code": 1,
  "message": "Remove Contact Success!",
  "data": [
    {
      "first_name": "John",
      "last_name": "Doe",
      "email": "john@mail.com",
      "phone": 628000123456
    }
  ]
}
```
6.5. Response Body (Business Error)

```json
{
  "code": 0,
  "message": "Remove Contact ID not found!",
  "data": []
}
```
6.6. Response Body (Technical Error)
```json
{
  "code": -1,
  "message": "Remove Contact conflict by technical issues! Please to call Admin.",
  "data": []
}
```