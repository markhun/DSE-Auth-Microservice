# Add authorizations

Add authorizations to the active User.

**URL** : `/authorization/`

**Method** : `PUT`

**Auth required** : YES

**Data constraints** :  
```json
[
	"testing1-5345-fake-UUID-7971ef3232cf",
	"testing2-fake-UUID-83b3-9563d9812051",
	"testing3-1f17-fake-UUID-6d279df69943",
	"testing4-fake-UUID-9f87-e94e60a93f99"
]
```

It is not possible to add an authorization more than once. 
Already existing authorizations are ignored during the process.


## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicHJpdmlsZWdlcyI6WyJ0ZXN0aW5nMy0xZjE3LWZha2UtVVVJRC02ZDI3OWRmNjk5NDMiLCJkMmRiNmM1NS01MzQ1LTRjZDYtYjdkYy03OTcxZWYzMjMyY2YiLCJ0ZXN0aW5nNC1mYWtlLVVVSUQtOWY4Ny1lOTRlNjBhOTNmOTkiLCJ0ZXN0aW5nMS01MzQ1LWZha2UtVVVJRC03OTcxZWYzMjMyY2YiLCJhYTA1MTM5ZC0yNDA1LTQ1YzYtODNiMy05NTYzZDk4MTIwNTEiLCJhMGFlZWMxYi0xZjE3LTRlYTEtYTRmZS02ZDI3OWRmNjk5NDMiLCI2N2Q2OTMzMS0wZmMyLTQ1MjMtOWY4Ny1lOTRlNjBhOTNmOTkiLCJ0ZXN0aW5nMi1mYWtlLVVVSUQtODNiMy05NTYzZDk4MTIwNTEiXSwidXNlcm5hbWUiOiJUZXN0dXNlcjMiLCJpYXQiOjE1MTUxMDE1ODAsImV4cCI6MTUxNTEwNDI4MCwianRpIjoic24zSXI3R2ZMS1dUZnhBYk1sbWRMZyJ9.u6CXbkNfC0eRNJG_h5tDPBxSt3n2X0-yJmr9ep0ZM8g"
}
```
the new token should be used after adding authorizations

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

**Condition** : User not found.

**Code** : `404 NOT FOUND` with Body `User not found!`

**Condition** : Unable to create new token.

**Code** : `500 Internal Server Error`



