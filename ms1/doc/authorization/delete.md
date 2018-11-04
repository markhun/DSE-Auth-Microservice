# Remove authorizations

remove authorizations from the active User.

**URL** : `/authorization/`

**Method** : `DELETE`

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

It is not possible to remove an authorization more than once. 
non existing authorizations are ignored during the process.


## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicHJpdmlsZWdlcyI6WyJ0ZXN0aW5nMy0xZjE3LWZha2UtVVVJRC02ZDI3OWRmNjk5NDMiLCJkMmRiNmM1NS01MzQ1LTRjZDYtYjdkYy03OTcxZWYzMjMyY2YiLCJhYTA1MTM5ZC0yNDA1LTQ1YzYtODNiMy05NTYzZDk4MTIwNTEiLCJhMGFlZWMxYi0xZjE3LTRlYTEtYTRmZS02ZDI3OWRmNjk5NDMiLCI2N2Q2OTMzMS0wZmMyLTQ1MjMtOWY4Ny1lOTRlNjBhOTNmOTkiLCJ0ZXN0aW5nMi1mYWtlLVVVSUQtODNiMy05NTYzZDk4MTIwNTEiXSwidXNlcm5hbWUiOiJUZXN0dXNlcjMiLCJpYXQiOjE1MTUxMDE3NTIsImV4cCI6MTUxNTEwNDQ1MiwianRpIjoiUkh5cUEtNHhUWEpadTg4QWI1bHE4dyJ9.L4fdrJRAG1NCz1Y4FOl-3BHe5tzcWnwTzs9BPKEmzIU"
}
```
the new token should be used after removing authorizations

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

**Condition** : User not found.

**Code** : `404 NOT FOUND` with Body `User not found!`

**Condition** : Unable to create new token.

**Code** : `500 Internal Server Error`



