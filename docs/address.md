# Address API Spec

### Table of Content
```
1. Address Add
2. Address Update
3. Address List
4. Address Detail
5. Address Search
6. Address Remove
```

### 1. Address Add
1.1. Endpoint: **POST** `/api/contact-{contact_id}/address`

1.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
1.3. Request Body
```json
{
  "street": "Jalan Raya",
  "city": "Kabupaten Bekasi",
  "province": "Jawa Barat",
  "country": "Indonesia",
  "zip_code": 17540
}
```
1.4. Response Body (Success):

```json
{
  "code": 1,
  "message": "Add Address Success",
  "data": [
    {
      "id": "RANDOM STRING",
      "street": "Jalan Raya",
      "city": "Kabupaten Bekasi",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "zip_code": 17540
    }
  ]
}
```
1.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Add Address Contact ID is not found!",
  "data": []
}
```
1.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Add Address conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 2. Address Update
2.1. Endpoint: **PUT** `/api/contact-{contact_id}/address-{address_id}`
2.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
2.3. Request Body
```json
{
  "street": "Jalan Raya",
  "city": "Kabupaten Bekasi",
  "province": "Jawa Barat",
  "country": "Indonesia",
  "zip_code": 17540
}
```
2.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "Update Address Success",
  "data": [
    {
      "id": "RANDOM STRING",
      "street": "Jalan Pacing",
      "city": "Kabupaten Chicago",
      "province": "Guangzhou",
      "country": "New Zeeland",
      "zip_code": 17542
    }
  ]
}
```
2.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Update Address ID is not found!",
  "data": []
}
```
2.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Update Address conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 3. Address List
3.1. Endpoint: **GET** `/api/contact-{contact_id}/address`
3.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
3.3. Request Body
```json
{}
```
3.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "List Address Success",
  "data": [
    {
      "id": "RANDOM STRING",
      "street": "Jalan Pacing",
      "city": "Kabupaten Chicago",
      "province": "Guangzhou",
      "country": "New Zeeland",
      "zip_code": 17542
    },
    {
      "id": "RANDOM STRING",
      "street": "Jalan Sudirman",
      "city": "Jakarta Selatan",
      "province": "DKI Jakarta",
      "country": "United State",
      "zip_code": 10000
    },
    {
      "id": "RANDOM STRING",
      "street": "Jalan Pantura",
      "city": "Tegal",
      "province": "Jawa Tengah",
      "country": "Zimbabwe",
      "zip_code": 20100
    }
  ]
}
```
3.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "List Address Contact ID is not found",
  "data": []
}
```
3.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "List Address conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 4. Address Detail
4.1. Endpoint: **GET** `/api/contact-{contact_id}/address-{address_id}`
4.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
4.3. Request Body
```json
{}
```
4.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "Detail Address Success",
  "data": [
    {
      "id": "RANDOM STRING",
      "street": "Jalan Raya",
      "city": "Kabupaten Bekasi",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "zip_code": 17540
    }
  ]
}
```
4.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Detail Address Contact ID is not found",
  "data": []
}
```
4.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Detail Address conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 5. Address Search
5.1. Endpoint: **GET** `/api/contact-{contact_id}/address/search`
5.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
5.3. Request Body
```json

```
5.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "Success",
  "data": []
}
```
5.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Search Address Contact ID is not found!",
  "data": []
}
```
5.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Search Contact conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 6. Address Remove
6.1. Endpoint: **DELETE** `/api/contact-{contact_id}/address-{address-contact}`
6.2. Request Header:
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
6.3. Request Body
```json
{}
```
6.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "Remove Address Success",
  "data": [
    {
      "id": "RANDOM STRING",
      "street": "Jalan Raya",
      "city": "Kabupaten Bekasi",
      "province": "Jawa Barat",
      "country": "Indonesia",
      "zip_code": 17540
    }
  ]
}
```
6.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Remove Address ID is not found!",
  "data": []
}
```
6.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Remove Address conflict by technical issues! Please to call Admin.",
  "data": []
}
```

