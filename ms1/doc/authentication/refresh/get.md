# Refresh expiring token

user your existing token to get a new one if your token is short before expiration.
TTL usually is 45 minutes.

**URL** : `/authentication/refresh`

**Method** : `GET`

**Auth required** : YES

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

**Condition** : "TokenHandler was unable to provide a token:"

**Code** : `500 INTERNAL SERVER ERROR`

