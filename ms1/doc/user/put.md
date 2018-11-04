# Update User Account

update password and/or email of user account.  
It is not possible to change usernames!

**URL** : `/user/`

**Method** : `PUT`

**Auth required** : YES

**Data constraints** :  
```json
{
	"email": "new@changed.com",
	"username": "Username",
	"password": "newPassword"
}
```
the username provided in the body must match the username associated with the sent bearer token

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicHJpdmlsZWdlcyI6WyJhYmY1NWZhNy03ZjViLTQ3MDItODVlMy0xMzkxYjNiMzgxZTgiLCI4YWJmYzI5OS04OGZjLTRmODQtYjk5Yy1iM2EyOGY4NDQ4MGEiLCI4OTVjNWE4NS05MWFiLTQ4OTMtOWMxOC05MTk5MzhhM2ExZGUiLCIzZDRjOTJiMC00ZDhmLTQ2ZDMtOGY2ZC04YTVjYzNjNTcwY2QiXSwidXNlcm5hbWUiOiJUZXN0dXNlcjMiLCJpYXQiOjE1MTUyNTc2MDksImV4cCI6MTUxNTI2MDMwOSwianRpIjoiTy1YZWI3cnIwX1lYQmJJSWhuSlI0dyJ9.Hf2quUknWLMaObqRvhCtvMHPrPOQDJ8_PQrIuygw02A"
}
```
the new token should be used after changing user data

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

**Condition** : Username provided in body doesn't match username associated with token.

**Code** : `400 BAD REQUEST` with Body `Usernames are permanent and can't be changed!`

**Condition** : Unable to create new token.

**Code** : `500 Internal Server Error`



