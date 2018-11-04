# Verify token

here you can retrieve all the information stored about the user associated with the provided token

**URL** : `/debug/verifyToken/`

**Method** : `GET`

**Auth required** : YES

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"username": "Testuser2",
	"id": 1,
	"email": "test@example.com",
	"password": "testpw",
	"privileges": [
		"8ccdc96e-2a9a-462b-b625-36dd1181a9db",
		"57c65efe-bcfe-493a-8f06-af0faf6fd276"
	],
	"name": "Testuser2"
}
```

This shows the data stored for the user associated with the provided token.
In the case of a bug this data could be different from the data stored in the token itself.

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

