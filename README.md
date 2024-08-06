Test desde Postman
localhost:8080/](http://localhost:8080/api/auth/login
Content-Type:application/json
{
  "username": "user",
  "password": "password"
}

listar  
Get :  http://localhost:8080/api/tipocambio
Content-Type:application/json

Registrar

Post : http://localhost:8080/api/tipocambio
Content-Type:application/json
{
    "rate": 13,
    "fromCurrency":"3.8",
    "toCurrency":"3.75",
    "username":"ricardo"
}

Buscar 
Get  :  http://localhost:8080/api/tipocambio/1
Content-Type:application/json




AuthController
Este controlador maneja las solicitudes de autenticación y registro de usuarios, generando y devolviendo un token JWT (JSON Web Token) para los usuarios autenticados.

Endpoints
Inicio de Sesión
URL: /api/auth/login
Método: POST
Consume: application/json
Produce: application/json
Descripción: Autentica a un usuario basado en las credenciales proporcionadas y genera un token JWT si la autenticación es exitosa.
Cuerpo de la Solicitud:

json
Copiar código
{
  "username": "string",
  "password": "string"
}
Respuesta:

Éxito: Un Mono<ResponseEntity<Map<String, String>>> que contiene el token JWT.
Error: 401 Unauthorized si falla.
Código:

java
Copiar código
@PostMapping("/login")
public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody AuthRequest authRequest) {
    return Mono.just(authRequest)
        .flatMap(request -> authenticate(request.getUsername(), request.getPassword()))
        .map(user -> {
            String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        })
        .onErrorResume(e -> Mono.just(ResponseEntity.status(401).body(Map.of("error", e.getMessage()))));
}

private Mono<User> authenticate(String username, String password) {
    // Implementa aquí tu lógica de autenticación
    if ("user".equals(username) && "password".equals(password)) {
        return Mono.just(new User(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    } else {
        return Mono.error(new AuthenticationException("Invalid username or password") {});
    }
}
Registro de Usuario
URL: /api/auth/register
Método: POST
Consume: application/json
Produce: application/json
Descripción: Registra un nuevo usuario basado en las credenciales proporcionadas y genera un token JWT si el registro es exitoso.
Cuerpo de la Solicitud:

json
Copiar código
{
  "username": "string",
  "password": "string"
}
Respuesta:

Éxito: Un Mono<ResponseEntity<Map<String, String>>> que contiene el token JWT.
Error: 400 Bad Request si falla.
Código:

java
Copiar código
@PostMapping("/register")
public Mono<ResponseEntity<Map<String, String>>> register(@RequestBody AuthRequest authRequest) {
    return Mono.just(authRequest)
        .flatMap(request -> registerUser(request.getUsername(), request.getPassword()))
        .map(user -> {
            String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        })
        .onErrorResume(e -> Mono.just(ResponseEntity.status(400).body(Map.of("error", e.getMessage()))));
}

private Mono<User> registerUser(String username, String password) {
    // Implementa aquí tu lógica de registro
    return Mono.just(new User(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
}
Configuración y Variables
SECRET_KEY: La clave secreta utilizada para firmar los tokens JWT.
Código:

java
Copiar código
private static final String SECRET_KEY = "123456789";
Clases Utilizadas
AuthRequest: Clase de configuración que contiene las credenciales del usuario (username y password).
User: Clase de Spring Security que representa a un usuario con su nombre, contraseña y roles (authorities).
AuthenticationException: Excepción lanzada cuando la autenticación falla.
