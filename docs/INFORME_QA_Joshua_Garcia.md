# Informe de QA / Testing — QWERYS Compilador SQL
**Proyecto:** QWERYS — Analizador/Compilador de Queries SQL y NoSQL  
**Responsable QA:** Joshua Eduardo García Reyes — 1890-22-5831  
**Curso:** Compiladores 2026 — UMG — Ing. Richard Ortiz  
**Fecha:** Mayo 2026  

---

## 1. Introducción

Este informe documenta la suite de pruebas JUnit 5 desarrollada para el compilador QWERYS, que realiza la migración de un compilador SQL de C++ a Java. Como responsable de QA/Testing, mi rol fue verificar que cada fase del compilador (léxico, sintáctico y semántico) funcione correctamente mediante pruebas unitarias y de integración.

El compilador sigue el siguiente flujo:

```
Texto SQL → Lexer → Tokens → Parser → AST → SemanticAnalyzer → Resultado
```

---

## 2. Archivos de Prueba Entregados

| Archivo | Fase que prueba | Ubicación |
|---|---|---|
| `LexerTest.java` | Analizador Léxico | `src/test/java/com/qwerys/compiler/` |
| `ParserTest.java` | Analizador Sintáctico | `src/test/java/com/qwerys/compiler/` |
| `SemanticTest.java` | Analizador Semántico | `src/test/java/com/qwerys/compiler/` |

---

## 3. Descripción de Pruebas por Módulo

### 3.1 LexerTest.java — Analizador Léxico

**¿Qué prueba?**  
Verifica que el Lexer convierte correctamente texto SQL en tokens (palabras clave, identificadores, operadores, literales).

**Casos cubiertos:**
- Reconocimiento de palabras clave SQL (`SELECT`, `FROM`, `WHERE`, `INSERT`, `UPDATE`, `DELETE`)
- Tokenización de identificadores (nombres de tablas y columnas)
- Reconocimiento de operadores (`=`, `>`, `<`, `>=`, `<=`, `<>`)
- Manejo de literales numéricos y de cadena
- Tokenización de consultas completas

**Ejemplo de query probada:**
```sql
SELECT nombre FROM usuarios WHERE edad > 18
```

**Resultado esperado:** Lista de tokens con tipo, valor, línea y columna correctos.

**Correspondencia C++ → Java:**

| C++ (Lexer.cpp) | Java (Lexer.java) |
|---|---|
| Keywords: SELECT, FROM, WHERE | Keywords: SQL completo multi-dialecto |
| Token simple (tipo, valor) | Token con línea y columna |
| Solo SQL básico | SQL + NoSQL |

---

### 3.2 ParserTest.java — Analizador Sintáctico

**¿Qué prueba?**  
Verifica que el Parser construye correctamente el Árbol de Sintaxis Abstracta (AST) a partir de los tokens del Lexer.

**Casos cubiertos:**
- Parsing de SELECT básico
- Parsing con condiciones WHERE
- Manejo de errores sintácticos con línea y columna
- Construcción correcta de nodos AST (SelectNode, ConditionNode, ExpressionNode)

**Ejemplo de query probada:**
```sql
SELECT id, nombre FROM usuarios WHERE id = 1
```

**Resultado esperado:** AST válido con nodos correctamente enlazados.

**Correspondencia C++ → Java:**

| C++ (Parser.cpp) | Java (Parser.java) |
|---|---|
| Solo SELECT simple | SELECT, INSERT, UPDATE, DELETE, DDL |
| AST.h separado | AST construido directamente por Parser |
| Sin ASTBuilder | Sin ASTBuilder (parser lo arma) |

---

### 3.3 SemanticTest.java — Analizador Semántico

**¿Qué prueba?**  
Verifica que el analizador semántico valida correctamente el contexto de las queries (tablas existentes, columnas válidas, tipos de datos compatibles).

**Casos cubiertos:**
- Query válida con tabla y columnas existentes
- Error por tabla inexistente
- Error por columna inexistente
- Error por tipos incompatibles en WHERE
- Detección de patrones SQL Injection

**Ejemplos de queries probadas:**

| Query | Resultado esperado |
|---|---|
| `SELECT id FROM usuarios WHERE id = 1` | Válida |
| `SELECT id FROM clientes WHERE id = 1` | Error: tabla 'clientes' no existe |
| `SELECT email FROM usuarios WHERE id = 1` | Error: columna 'email' no existe |
| `SELECT id FROM usuarios WHERE edad > 'hola'` | Error: tipo incompatible |
| `SELECT * FROM usuarios WHERE id = '' OR '1'='1'` | Advertencia: SQL Injection detectado |

---

## 4. Suite de Pruebas en QWERYS Completo (Referencia)

Además de los 3 archivos académicos, el proyecto QWERYS cuenta con una suite amplia de pruebas JUnit 5 en el backend:

| Archivo de Test | Área | Tests aproximados |
|---|---|---|
| `SqlLexerTest.java` | Léxico SQL | 1 @Test |
| `SqlParserTest.java` | Sintáctico SQL | 7 @Test |
| `SchemaAwareSemanticAnalyzerTest.java` | Semántica con schema | Varios |
| `OptimizationEngineSchemaColumnsTest.java` | 18 reglas optimización | Varios |
| `RuleBasedAiFallbackTest.java` | IA offline | Varios |
| `AdaptersDay27Test.java` | Adaptadores BD | Varios |
| `MySQLAdapterTest.java` | Adaptador MySQL | Varios |
| `MongoTransactionsTest.java` | MongoDB NoSQL | Varios |
| Otros (~55 archivos) | NoSQL, procedural, adapters | Varios |

**Total aproximado:** ~63 archivos de test cubriendo todos los módulos del sistema.

> **Nota:** No se afirma cobertura del 70% con número exacto ya que no se generó reporte JaCoCo. Se puede confirmar que la suite amplia JUnit 5 pasa con `mvn test → BUILD SUCCESS`.

---

## 5. Comparación C++ vs Java en Testing

| Aspecto | C++ (compilador-sql-final) | Java QWERYS |
|---|---|---|
| Framework de pruebas | Manual / Makefile | JUnit 5 |
| Archivos de test | `lexer_test.cpp` | 63+ archivos .java |
| Cobertura | Solo SELECT básico | SQL completo + 5 NoSQL |
| Automatización | `make test` | `mvn test` |
| Reportes | Sin reportes | Compatible JaCoCo |
| Integración CI | No | Compatible GitHub Actions |

---

## 6. Beneficios de la Migración a Java para QA

1. **JUnit 5** — framework estándar de la industria con anotaciones claras (`@Test`, `@BeforeEach`, `assertThrows`)
2. **Maven** — gestión de dependencias y ejecución de tests automatizada con `mvn test`
3. **Portabilidad** — los tests corren en cualquier OS con Java 17
4. **Integración continua** — compatible con GitHub Actions, Jenkins, etc.
5. **Mejor reporte de errores** — excepciones con stack trace completo vs errores de compilación C++

---

## 7. Desviaciones del Plan Original

| Plan original | Realidad implementada | Impacto en QA |
|---|---|---|
| `IntegrationTest.java` único | ~63 archivos de test separados | Mayor cobertura por módulo |
| 10 reglas optimización | 18 reglas | Más tests de optimización |
| 13 patrones SQL Injection | 5 patrones (SE007) | Tests ajustados |
| WebSocket tiempo real | REST POST `/api/queries/analyze` | Tests REST en lugar de WS |
| Swagger | No implementado | Endpoints documentados en README |

---

## 8. Cómo Ejecutar las Pruebas

### Módulo académico (docs/java-compiler):
```bash
cd qwerys-compiladores-2026/docs/java-compiler
mvn test
```

### Backend completo (QWERYS):
```bash
cd qwerys-compiladores-2026/backend/qwerys-backend
mvn test
```

**Resultado esperado:**
```
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 9. Endpoints REST Documentados (sin Swagger)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/queries/analyze` | Analizar query SQL/NoSQL |
| GET | `/api/queries/engines` | Listar motores soportados |
| POST | `/api/auth/register` | Registro de usuario |
| POST | `/api/auth/login` | Inicio de sesión |
| GET | `/api/history` | Historial de queries (usuario registrado) |
| POST | `/api/schema/connect` | Conectar base de datos local |

---

## 10. Conclusión

La suite de pruebas JUnit 5 del proyecto QWERYS cubre satisfactoriamente las tres fases principales del compilador (léxico, sintáctico y semántico). La migración de C++ a Java permitió implementar pruebas más robustas, automatizadas y mantenibles. Los tests académicos entregados (`LexerTest.java`, `ParserTest.java`, `SemanticTest.java`) demuestran el correcto funcionamiento del compilador standalone del módulo `docs/java-compiler`, mientras que la suite completa del backend valida el producto QWERYS en su totalidad.

---

*Documento generado por Joshua Eduardo García Reyes — QA/Testing — QWERYS Compiladores UMG 2026*
