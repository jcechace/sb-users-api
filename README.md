Simple Access Management API
===
This demo project is a simple implementation of a REST API for managing access grants to external projects. It served as a SpringBoot learning excersise.

## How to run
The easiest way to run the project is to use the provided docker-compose file. Only docker or podman (together with compose) is required (the project is built and packaged inside container).
From the project root directory run:

```bash
# using podman
podman compose -f src/docker/compose.yaml up
# using docker
docker compose -f src/docker/compose.yaml up
```

## How to Use
The rest API runs on port 8080 at base path `/api/v1`. 

The following endpoints are available:

```text
# > Authentication
# Accepts no body, requires basic authentication, returns a JWT token
POST /auth/token

# > Users
GET     /users      # List all users
GET     /users/{id} # Get user by id
POST    /users      # Create user (json body: name, email, password)
DELETE  /users/{id} # Delete user, authentication required (JWT as Bearer Token)
 
# > Access Grants, authentication required (JWT as Bearer Token)
GET     /access                                         # List all access grants
GET     /access/users/{id}                              # List access grants for user
GET     /access/projects/{id}                           # List access grants to project
GET     /access/users/{user_id}/projects/{project_id}   # Get access grant
PUT     /access/users/{user_id}/projects/{project_id}   # Create access grant (json body: projectName)
DELETE  /access/users/{user_id}/projects/{project_id}   # Delete access grant
```

## Design and Implementation notes
Refer to the [design document](DESIGN.md) for more information on the design and implementation of the project.

### Things that would be nice to have
- Proper API documentation (OpenAPI/Swagger)
- Proper error handling (correct HTTP status codes are not enough)
- Pagination support for list endpoints
- HATEOAS support (especially handy for relationships, like access grants)
- Better security (e.g. rate limiting, proper TLS, etc.)
- Better test coverage

Why are those things not implemented? Time constraints. I attempted to demonstrate my ability to design and write maintainable code, and to show that I can learn new technologies quickly (considering that my last encunter with Spring was about 10 years ago). Perfect implementation was not the goal. 

__Note:__ Test development was as far from TDD as imagnable. This was due to me learning Spring boot on the way.