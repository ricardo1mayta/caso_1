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



TipoCambioController
Este controlador maneja solicitudes relacionadas con los tipos de cambio de divisas y audita estas acciones. Proporciona endpoints para recuperar, guardar y eliminar tipos de cambio, así como para realizar intercambios de divisas.

Endpoints:
Obtener Todos los Tipos de Cambio

URL: /api/tipocambio
Método: GET
Produce: application/json
Descripción: Recupera todos los tipos de cambio de divisas.
Respuesta: Un Flux<TipoCambio> que contiene la lista de todos los tipos de cambio.
java
Copiar código
@GetMapping(produces = "application/json")
public Flux<TipoCambio> getAllRates() {
    return tipoCambioService.getAllRates();
}
Obtener Tipo de Cambio por ID

URL: /api/tipocambio/{id}
Método: GET
Produce: application/json
Descripción: Recupera un tipo de cambio específico por su ID.
Variable de Ruta: id (tipo: Long)
Respuesta: Un Mono<ResponseEntity<TipoCambio>> que contiene el tipo de cambio si se encuentra, de lo contrario una respuesta 404 Not Found.
java
Copiar código
@GetMapping("/{id}")
public Mono<ResponseEntity<TipoCambio>> getRateById(@PathVariable Long id) {
    return tipoCambioService.getRateById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
}
Guardar Tipo de Cambio

URL: /api/tipocambio
Método: POST
Consume: application/json
Produce: application/json
Descripción: Guarda un nuevo tipo de cambio de divisa.
Cuerpo de la Solicitud: TipoCambio (tipo: TipoCambio)
Respuesta: Un Mono<TipoCambio> que contiene el tipo de cambio guardado.
java
Copiar código
@PostMapping(consumes = "application/json", produces = "application/json")
public Mono<TipoCambio> saveRate(@RequestBody TipoCambio tipoCambio) {
    return tipoCambioService.saveRate(tipoCambio);
}
Eliminar Tipo de Cambio

URL: /api/tipocambio/{id}
Método: DELETE
Descripción: Elimina un tipo de cambio específico por su ID.
Variable de Ruta: id (tipo: Long)
Respuesta: Un Mono<Void> que indica la finalización de la eliminación.
java
Copiar código
@DeleteMapping("/{id}")
public Mono<Void> deleteRate(@PathVariable Long id) {
    return tipoCambioService.deleteRate(id);
}
Intercambio de Divisas

URL: /api/tipocambio/intercambio
Método: POST
Produce: application/json
Descripción: Intercambia una cantidad de dinero de una divisa a otra y audita la transacción.
Parámetros de la Solicitud:
amount (tipo: double) - La cantidad a intercambiar.
fromCurrency (tipo: String) - La divisa de origen.
toCurrency (tipo: String) - La divisa de destino.
username (tipo: String) - El nombre de usuario de la persona que realiza el intercambio.
Respuesta: Un Mono<ResponseEntity<Double>> que contiene la cantidad intercambiada.
java
Copiar código
@PostMapping("/intercambio")
public Mono<ResponseEntity<Double>> exchange(@RequestParam double amount, @RequestParam String fromCurrency, @RequestParam String toCurrency, @RequestParam String username) {
    return tipoCambioService.getAllRates()
            .filter(rate -> rate.getFromCurrency().equals(fromCurrency) && rate.getToCurrency().equals(toCurrency))
            .next()
            .flatMap(rate -> {
                double exchangedAmount = amount * rate.getRate();
                Audit audit = new Audit();
                audit.setUsername(username);
                audit.setAction("intercambio");
                audit.setDetails("intercambios " + amount + " " + fromCurrency + " a " + exchangedAmount + " " + toCurrency);
                return auditService.saveAudit(audit)
                        .thenReturn(ResponseEntity.ok(exchangedAmount));
            });
}
Servicios Utilizados:
TipoCambioService

Proporciona métodos para interactuar con los datos de los tipos de cambio de divisas.
Métodos: getAllRates(), getRateById(Long id), saveRate(TipoCambio tipoCambio), deleteRate(Long id).
AuditService

Proporciona métodos para guardar registros de auditoría de las acciones realizadas.
Método: saveAudit(Audit audit).
Esta documentación proporciona una visión general de los endpoints y sus funcionalidades dentro de la clase TipoCambioController
