# User API Spec

### Table of Contents:
```
1. User Register
2. Auth Login
3. User Info
4. User Update
5. Auth Logout
```
_Note: **User** for Business, **Auth** for Technical_

### Glossary:
```
0 = Module
0.1 = Endpoint
0.2 = Request Header
0.3 = Request Body
0.4 = Response Body (Success)
0.5 = Response Body (Business Error)
0.6 = Response Body (Technical Error)
```

---

### 1. User Register
1.1. Endpoint: **POST** `/api/user/register`

1.2. Request Header:
```json
{
  "X-API-TOKEN": null
}
```
1.3. Request Body:

```json
{
  "username": "john",
  "password": "secret",
  "name": "John Doe"
}
```

1.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "User Register Success",
  "data": []
}
```

1.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "User Register Password must not blank!",
  "data": []
}
```

1.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "User Register conflict by technical issues! Please to call Admin.",
  "data": []
}
```


## 2. Auth Login
2.1. Endpoint: **POST** `/api/auth/login`

2.2. Request Header:
```json
{
  "X-API-TOKEN": null
}
```

2.3. Request Body:
```json
{
  "username": "john",
  "password": "secret"
}
```

2.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "Auth Login Success",
  "data": {
    "token": "TOKEN",
    "expiredAt": 234234234
  }
}
```

2.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Auth Login Username or Password is wrong!",
  "data": []
}
```

2.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Auth Login conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 3. User Info
3.1. Endpoint: **GET** `/api/user/info`

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

3.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "User Info Success",
  "data": {
    "username": "john",
    "name": "John Doe"
  }
}
```

3.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "User Info is not authorised for you!",
  "data": []
}
```

3.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "User Info conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 4. Update User
4.1. Endpoint: **PATCH** `/api/user/update`

4.2. Request Header: 
```json
{
  "X-API-TOKEN": "TOKEN"
}
```
4.3. Request Body:
```json
{
  "name": "Jeremy Doe",
  "password": "more_secret"
}
```

4.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "User Update Success",
  "data": {
    "name": "Jeremy Doe"
  }
}
```

4.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "User Update Name must not blank!",
  "data": []
}
```

4.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "User Update conflict by technical issues! Please to call Admin.",
  "data": []
}
```

### 5. Auth Logout
5.1. Endpoint: **DELETE** `/api/auth/logout`

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

5.4. Response Body (Success):
```json
{
  "code": 1,
  "message": "User Update Success",
  "data": {
    "name": "Jeremy Doe"
  }
}
```

5.5. Response Body (Business Error):
```json
{
  "code": 0,
  "message": "Auth Logout Name must not blank!",
  "data": []
}
```

5.6. Response Body (Technical Error):
```json
{
  "code": -1,
  "message": "Auth Logout conflict by technical issues! Please to call Admin.",
  "data": []
}
```
