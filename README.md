# JWT AUTH SERVICE

## DEPENDENCIES: 
Spring web, Data JPA, oAuth2 Resource server, Validation, lombok, postgresql support, docker compose support, 
flyway migration.

## Steps:

### 1. Initial config check 
- Docker compose postgresql boilerplate
- Add Create table sql commands to db.migration.

### 2. User entity and Repo
- User entity class to define all the columns
- UserRepository Interface to handle methods like findByUsername
- Enable JPA Auditing config to automatically set createdAt and updatedAt 
when the entity is saved or updated.

### 3. Generate RSA Keys for JWT
```cmd
cd src/main/resources/jwt
openssl genpkey -algorithm RSA -out app.key -outform PEM
openssl rsa -pubout -in app.key -out app.pub
```

Add KEY config to application.yaml

### 4. SecurityConfig
| Part                                                     | Purpose                                                                           |
| -------------------------------------------------------- | --------------------------------------------------------------------------------- |
| `.csrf().disable()`                                      | CSRF disabled for stateless REST APIs                                             |
| `.authorizeHttpRequests()`                               | Allow unauthenticated access to `/api/auth/**`; require authentication for others |
| `.sessionManagement()`                                   | No HTTP session, fully stateless                                                  |
| `.oauth2ResourceServer().jwt()`                          | Enable JWT-based security                                                         |
| `.authenticationEntryPoint()` / `.accessDeniedHandler()` | Proper handling of 401/403 errors                                                 |

### 5. JwtService and JwtConfig
Jwt Properties
- @Configuration
JwtService
- generateToken(): generates token using JwtEncoder and claimSet params [subject(username), issuer(appnam), duration(ttl)]
JwtConfig
- @EnableConfiguration, @RequiredArgsConstructor
- Encoder(), Decoder(), JwtService()

### 6. 