# Get Example Body for /user-Endpoint

here you can retrieve an example of how a body for the /user/-Endpoint `POST` and `PUT` should look like for a given user.

**URL** : `/debug/signUpExample/`

**Method** : `GET`

**Auth required** : YES

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"email": "test@example.com",
	"username": "Testuser3",
	"password": "testpw"
}
```

This shows the data for the user associated with the provided token

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

