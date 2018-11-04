# microservice1 - Authentication and Authorization

## How to start the ms1 application

1. Run `mvn clean install` to build your application
1. before your first run a database schema has to be created!  
start the application with  `java jar target/ms1-01.jar db migrate ms1/config.yml`  
A run configuration for IntelliJ can be found at: `../.idea/runConfigurations/ms1_db_migrate_ms1_config_yml.xml`  
The schema definition can be found in `ms1/src/main/resources/migrations.xml`
1. Start application with `java -jar target/ms1-0.1.jar server ms1/config.yml`  
A run configuration for IntelliJ can be found at: `../.idea/runConfigurations/ms1_server_ms1_config_yml.xml` 
1. To check that your application is running enter url `http://localhost:8081` - that's the admin panel
Be aware that the running ports have to be configured via the config.yml. If you changed those the url have to changed 
accordingly.

Check `config.yml` for details!  
There are several other configuration parameters. Especially important are the ones listed under **dse2017**  
```yaml
dse2017:
  secret: gyswgQL+9ts5xc1XX47Y0xxI7ReNuh/w
  # can be jws for unencrypted jwt's with signature or
  # can be jwe for fully encrypted tokens
  tokenType: jws
  # API Endpoints starting with /debug/... are only available if debugMode is true
  debugMode: true
```

## Health Check
No custom health checks are implemented!

To see your applications health enter url `http://localhost:8081/` to access the admin menu

## Testing

An [Insomnia REST Client](<https://insomnia.rest/>) configuration file with the following APIrequests 
can be found [here](ms1/src/test/Insomnia_requests.json) at `ms1/src/test/Insomnia_requests.json`. 
You just have to import it.  
Using Insomnia instead of cURL to test this application is recommended.

If you start the service for the first time you can use the API "[Create User](ms1/doc/user/post.md)" to create user accounts.

you can use Basic Authentication to generate a JWT-token  
```
curl -u YourUsername:YourPassword -X GET --header 'Accept: application/json' 'https://localhost:8085/authentication' --insecure
``` 
or with already encoded header  
```
curl --request GET \  
--url http://localhost:8080/authentication \  
--header 'authorization: Basic VGVzdHVzZXI6dGVzdHB3'
```

#### Example Response:  
```
{  
"token":  
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicHJpdmlsZWdlcyI6IjAwMDAwMDAiLCJ1c2VybmFtZSI6IlNvbWVVc2VyIiwiaWF0IjoxNTEwMDA2Njc2LCJqdGkiOiJOMTlOamdDX2Z6YktsNDBrcWJrVkJBIn0.jryS11x0DaISrUCP8qT_-WAJ_AI3f1MmsYn2DQl38LA"   
}
```

if you set `tokenType` to `jws` in `config.yml` you can verify the signature at <https://jwt.io/> by using the secret you defined in 
`config.yml`. If you set the  `tokenType` to `jwe` you can only use this application to verify tokens, as jwt.io does not support
encrypted tokens.

you can verify generated tokens by calling the corresponding API endpoint with the generated token:  
```
curl -X GET \  
 -H "Authorization: Bearer [TOKEN-GOES-HERE]" \  
 -H "Cache-Control: no-cache" 'http://localhost:8080/debug/verifyToken'
```  
should the token be valid the User Object matching the token's subject will be returned in JSON from the database.  
Be aware that all API endpoints starting with `/debug` are only available if you set `debugMode: true` in `config.yml`
#### Example Response:  
```
{
	"username": "Testuser2",
	"id": 2,
	"email": "test@example.com",
	"password": "testpw",
	"privileges": [],
	"name": "Testuser2"
}
```

### Endpoints to acquire initial tokens

* [Login](ms1/doc/authentication/get.md) : `GET /authentication/`

### Endpoints requiring a valid token
* [Refresh expiring token](ms1/doc/authentication/refresh/get.md) : `GET /authentication/refresh`

* [Show authorizations](ms1/doc/authorization/get.md) : `GET /authorization/`
* [Add authorizations](ms1/doc/authorization/put.md) : `PUT /authorization/`
* [Remove authorizations](ms1/doc/authorization/delete.md) : `DELETE /authorization/`
  
  
* [Delete User](ms1/doc/user/delete.md) : `DELETE /user/`
* [Update User](ms1/doc/user/put.md) : `PUT /user/`

### Endpoints not requiring authentication

* [Create User](ms1/doc/user/post.md) : `POST /user/`

### Endpoints only available in Debug Mode

* [Get Example Body for /user-Endpoint](ms1/doc/debug/signUpExample/get.md) : `GET /debug/signUpExample`
* [Get all stored Information for a given token](ms1/doc/debug/verifyToken/get.md) : `GET /debug/verifyToken`
* [Get all stored users](ms1/doc/debug/allUsers/get.md) : `GET /debug/allUsers`
