Simple USER-PROJECT Access Management
===

REST API Design
---
The assignment is to design a REST API for managing access grants to external projects.
This means a many-to-many relationship between users and projects. Contrary to what
might be the first impression, correctly designing N-N relationships in REST APIs is not trivial.  

### Problem Statement
The assignment itself leaves several questions open, which would ideally need to be answered before designing the API.

1) Should "external project" be a resource on its own?  
    - Do we expect the need to store some metadata about projects?
2) How do we want to query access grants?
    1. list all projects accessible by a user
    2. list all users with access to a project
    3. list all access grants (regardless of specific user or project) 


Especially the second point is important. If we want only 
`2i` or `2ii`  we can get away with modeling project access as subresource of either user or project.

- For project access grants of user `{id}`:
  - `GET /users/{id}/access` list all access grants
  - `GET /users/{id}/access/{project_id}` get single access grant
  - `PUT /users/{id}/access/{project_id}`create access grant
  - `DELETE /users/{id}/access/{project_id}` delete access grant

Or

- Project access grants to project `{id}`:
  - `GET /projects/{id}/access` list all access grants
  - `GET /projects/{id}/access/{user_id}` get single access grant
  - `PUT /projects/{id}/access/{user_id}`  create access grant
  - `DELETE /projects/{id}/access/{user_id}` delete access grant

However, we can't have both (at least not without trade-offs), otherwise, we would have two different URLs for the same resource.
- `GET /users/{id}/access/{project_id}` 
- `GET /projects/{id}/access/{user_id}`



Point `2iii` would require a separate resource for access grants and once again 
we would get to similar problems as above.

### Proposed Design
Since the assignment doesn't specify whether project should be a resource on its own, we chose not 
to model it as one. The project access grants will however be modeled as a top level resource.

This allows us to decouple the access grants from the user and project (should we decide to introduce it in the future) 
resources. Thus, the REST API for project access grants is designed as follows:

- `GET /access` list all access grants
- `GET /access/users/{id}` list access grants for user
- `GET /access/projects/{id}` list access grants to project
- `GET /access/users/{user_id}/projects/{project_id}` get single access grant
- `DELETE /access/users/{user_id}/projects/{project_id}` delete single access grant
- `PUT /access/users/{user_id}/projects/{project_id}` create single access grant

This URI scheme assumes that the access grants are unique for each user-project pair. 
An alternative would be to introduce a surrogate id for access resource and use `/access/{id}` as resource URI. 
An argument can be made that the chosen scheme provides better developer experience. The caller is likely to 
know the user and project ids which means that 
- access can be verified by simply calling `GET /access/user/{user_id}/project/{project_id}`
- access can be deleted by calling `DELETE /access/user/{user_id}/project/{project_id}`

In case of the alternative the operations above would require two calls to the API.

__Note: The additional benefit of this design is the idempotence of PUT and DELETE operations__

Security
---
Spring Security and Spring Security OAuth2 Server are used to secure the API. When considering the type of authentication
the following options were considered:

1) Api Key
2) Basic Auth
3) OAuth2
4) Some form of JWT based authentication

__Note: The following analysis assumes a proper uses of TLS, which is not the case in this demo__

### Api Key
For many APIs, authenticating the client application is enough, an API key is a good choice. However, in this 
particular case it is not clear whether such assumption would hold. As the system deals with some form of users 
anyway I decided to authenticate users. 

### Http Basic Auth 
while simple to implement, HTTP basic auth is not suitable for APIs as it requires the credentials to be sent with every request.
This is a security risk as the credentials can be intercepted (however this is less of a concern when using TLS). The greater
risk in my opinion lies elsewhere -- since the credentials are sent with every request, they are more likely to leak 
somewhere in the system (e.g. in logs).


### OAuth2 
This would clearly be the better choice. However, using 3rd party OAuth2 server didn't seem as a good fit for the demo
project and implementing OAuth2 authentication in the demo project would be too complex

### JWT based Authentication
This is the choice I went with. In a sense this would be similar to Oauth2 password grant (which is deprecated, however
in this case the developed application also acts as the authentication server, so the argument against it is invalid). 

The API exposes and endpoint `/auth/token` which accepts a POST request with HTTP Basic Auth credentials and returns
a JWT token which can be used to authenticate further requests. The token is encoded and signed with a secret key (HS256).
For simplicity the Secret key is part of application configuration. 

This approach is simple to implement and provides a good balance between security and complexity.
- The token is sent with every request, but it is not the actual credentials
- The token has a limited lifetime (10 minutes by default)
  - Even if the token leaks (e.g. through logs) it's likely to be expired by the time it's intercepted
- The token is signed and can be verified by the server
- The token can be used to store some user information (e.g. roles) which can be used to authorize the user*

__\*Note: For simplicity roles are not used and only authentication is performed`__

## User Accounts
The passwords are stored in the database as BCrypt hashes.



