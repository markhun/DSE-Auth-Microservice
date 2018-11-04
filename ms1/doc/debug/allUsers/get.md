# Get all users stored in database

here you can retrieve all information stored in the database

**URL** : `/debug/allUsers/`

**Method** : `GET`

**Auth required** : NO

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
[
	{
		"id": 1,
		"username": "newUsername",
		"email": "new@changed.com",
		"password": "testpw",
		"privileges": [
			"testing3-1f17-fake-UUID-6d279df69943",
			"testing4-fake-UUID-9f87-e94e60a93f99",
			"testing1-5345-fake-UUID-7971ef3232cf",
			"testing2-fake-UUID-83b3-9563d9812051"
		],
		"name": "newUsername"
	}
]
```

This Endpoint simply calls findAll() on the UserDAO and returns its output.

