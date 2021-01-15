# REST LDAP API

Simple Rest API to manage users in an LDAP server. 

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 4](https://maven.apache.org)


This application has two services (**OPENLDAP Server** and **User management API**)


In order to follow these steps, you'll need to have [Docker](https://www.docker.com/) installed.

### Create  OPENLDAP server

Run this image:

``` shell
docker run --name ldap-service --hostname ldap-service --env LDAP_ORGANISATION="My Company" --env LDAP_DOMAIN="techinterview.com" --env LDAP_ADMIN_PASSWORD="123456" -p 389:389 --detach osixia/openldap:1.4.0
```

**Create the OU user under domain:**
<p>Create the file "create_ou_users.ldif" with the following content:  </p>

```  
dn: ou=Users,dc=techinterview,dc=com
changetype: add
objectClass: organizationalUnit
objectClass: top
ou: Users
<blank line at the end of the file>
```

**The following command will create a OU(organizationalUnit) to hold your users:**
ldapmodify -h localhost -p 389 -w '123456' -D 'cn=admin,dc=techinterview,dc=com'  -f  create_ou_users.ldif

### Administrate your ldap server

To manage your LDAP server, I recommend this image:

```
osixia/phpldapadmin
```

Run this image:

```
docker run --name phpldapadmin-service --hostname phpldapadmin-service -p 6443:443 --link ldap-service:localhost --env PHPLDAPADMIN_LDAP_HOSTS=localhost --detach osixia/phpldapadmin:0.9.0
```

URL:

```
https://localhost:6443/
```

Authenticate to server localhost:

```
Login DN: cn=admin,dc=techinterview,dc=com
Password: 123456
```


## Run LDAP REST API

### Running the application locally
To run a Spring Boot application on your local machine you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Or 

```shell
mvn clean package spring-boot:repackage 
java -jar target/ldapapi-1.0.jar
```

## Test API

The REST API is described below.
### Request

`GET /Users/`

    curl -X GET "http://localhost:8080/Users" -H "accept: application/json;charset=UTF-8"
    
### Response

    HTTP/1.1 200 OK
    
    [  {  "cn":  "string",  "sn":  "string",  "uid":  "string"  }  ]

## Create a new user

### Request

`POST /Users`

    curl -X POST "http://localhost:8080/Users" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/json" -d "{ \"cn\": \"Nome\", \"sn\": \"Nome\", \"uid\": \"teste\"}"
    
### Response

    HTTP/1.1 201 Created
    
	{"uid":"teste","cn":"Nome","sn":"Nome"}

## Get a specific user

### Request

`GET /Users/uid`

    curl -X GET "http://localhost:8080/Users/sabrina" -H "accept: application/json;charset=UTF-8"
    
### Response

    HTTP/1.1 200 OK
    
    {"uid":"sabrina","cn":"Sabrina","sn":"Queiroz"}

## Get a non-existent user

### Request

`GET /Users/id`

    curl -X GET "http://localhost:8080/Users/jose" -H "accept: application/json;charset=UTF-8"

### Response

    HTTP/1.1 404 Not Found
   
    {"message":"User Not Found","details":["User not found : jose"]}
    
## Delete a user

### Request

`DELETE /Users/uid`

    curl -X DELETE "http://localhost:8080/Users/teste" -H "accept: application/json;charset=UTF-8"

### Response

    HTTP/1.1 200 OK
    
    {  "message":  "User deleted successfully"  }


## Try to delete same user again

### Request

`DELETE /Users/id`

    curl -X DELETE "http://localhost:8080/Users/teste" -H "accept: application/json;charset=UTF-8"
### Response

    HTTP/1.1 404 Not Found
    
    {  "message":  "User Not Found",  "details":  [  "User not found"  ]  }

## Swagger 

[swagger.json](http://localhost:8080/v2/api-docs)

[swagger-ui](http://localhost:8080/swagger-ui.html)
