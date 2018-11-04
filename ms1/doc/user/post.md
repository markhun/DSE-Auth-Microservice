# Create new User Account

create a new user account

**URL** : `/user/`

**Method** : `POST`

**Auth required** : NO

**Data constraints** : 
```json
{
	"email": "newly@created.com",
	"username": "newUser",
	"password": "password"
}
```

## Success Responses

**Code** : `200 OK`

You can try to log into the new account.

### OR

**Condition** : Email malformed.

**Code** : `422 NUnknown` with Body 
```json
{
	"errors": [
		"email not a well-formed email address"
	]
}
```
**Condition** : Username already in use.

**Code** : `409 CONFLICT` with Body
```json
{
	Username already in use!
}
```
