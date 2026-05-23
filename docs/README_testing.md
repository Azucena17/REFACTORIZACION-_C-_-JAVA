# QWERYS — Suite de Pruebas JUnit 5
**Responsable:** Joshua Eduardo García Reyes — 1890-22-5831  
**Rol:** QA / Testing  
**Rama:** `feature/joshua-garcia-testing`

---

## Archivos en este PR

```
src/test/java/com/qwerys/compiler/
├── LexerTest.java       → Pruebas del Analizador Léxico
├── ParserTest.java      → Pruebas del Analizador Sintáctico
└── SemanticTest.java    → Pruebas del Analizador Semántico
```

---

## Cómo ejecutar las pruebas

```bash
# Asegúrate de tener Java 17 y Maven instalados
cd REFACTORIZACION-_C-_-JAVA
mvn test
```

**Resultado esperado:**
```
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Endpoints REST de QWERYS (sin Swagger)

| Método | Ruta | Descripción | Auth requerida |
|---|---|---|---|
| POST | `/api/queries/analyze` | Analizar query SQL/NoSQL | No (invitado) |
| GET | `/api/queries/engines` | Listar motores soportados | No |
| POST | `/api/auth/register` | Registro de usuario | No |
| POST | `/api/auth/login` | Inicio de sesión | No |
| GET | `/api/history` | Historial de queries | Sí |
| POST | `/api/schema/connect` | Conectar base de datos local | Sí |
| GET | `/api/suggestions` | Sugerencias IA | Sí |

**Base URL local:** `http://localhost:8080`  
**Base URL Docker:** `http://localhost:8081`

### Ejemplo de request — Analizar query:
```json
POST /api/queries/analyze
{
  "query": "SELECT id, nombre FROM usuarios WHERE edad > 18",
  "engine": "MySQL"
}
```

### Ejemplo de response:
```json
{
  "valid": true,
  "tokens": [...],
  "ast": {...},
  "semanticErrors": [],
  "suggestions": ["Considera agregar índice en columna edad"],
  "executionTime": "12ms"
}
```

---

## Motores SQL soportados nativamente
- MySQL
- PostgreSQL
- SQLite
- Oracle
- SQL Server

## Motores NoSQL soportados nativamente
- MongoDB
- Redis
- Cassandra
- DynamoDB
- Elasticsearch

---

## Correspondencia C++ → Java

| C++ (compilador-sql-final) | Java (QWERYS) |
|---|---|
| `lexer_test.cpp` | `LexerTest.java` |
| `make test` | `mvn test` |
| Solo SELECT | SQL completo + NoSQL |
| Sin framework | JUnit 5 |

---

*QWERYS · Compiladores UMG 2026 · Joshua Eduardo García Reyes*
