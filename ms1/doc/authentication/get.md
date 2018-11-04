# Login

Login and acquire bearer token in response

**URL** : `/authentication/`

**Method** : `GET`

**Auth required** : BASIC AUTH in HTTP-Header

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicHJpdmlsZWdlcyI6WyI5ODQxMjUxMC02ZGNjLTQxNzEtYjNjOS05Y2YzNjYzNDYwZGQiLCJhYTkzZjNhNS0wMzE5LTQzMGItYjMwNS0yMzYwZjU4YzA1M2EiXSwidXNlcm5hbWUiOiJUZXN0dXNlcjIiLCJpYXQiOjE1MTUyNTg0NDQsImV4cCI6MTUxNTI2MTE0NCwianRpIjoiQzdXQmZlUkg2NDJ5WjlHRXVmTndIZyJ9.Fe_QvRD2sP3UWP7dRvU-NAQAYsfUpIcdMyc7Lt9r_n0"
}
```

### OR

**Condition** : Credentials invalid.

**Code** : `401 NOT AUTHORIZED`

