# Show authorizations

Show all authorizations the active User holds.

**URL** : `/authorization/`

**Method** : `GET`

**Auth required** : YES

**Data constraints** : `{}`

## Success Responses

**Code** : `200 OK`

**Content** :  
```json  
[
    "testing3-1f17-fake-UUID-6d279df69943",  
    "d2db6c55-5345-4cd6-b7dc-7971ef3232cf",  
    "testing4-fake-UUID-9f87-e94e60a93f99",  
    "testing1-5345-fake-UUID-7971ef3232cf",  
    "aa05139d-2405-45c6-83b3-9563d9812051",  
    "a0aeec1b-1f17-4ea1-a4fe-6d279df69943",  
    "67d69331-0fc2-4523-9f87-e94e60a93f99",  
    "testing2-fake-UUID-83b3-9563d9812051"  
]
```

### OR

**Condition** : Token invalid.

**Code** : `401 NOT AUTHORIZED`

