# Department_Layers_Improvement_Task

This task should be quite simple for you.

Your task is to implement the `updateDepartment()` method in the `DepartmentDaoImpl` class and also implement the `updateDepartment()` and `updateDepartmentLocation()` methods in the `DepartmentServiceImpl` class. After completing the task, run the code in the `Main` class, where the code for testing is already written. There, you can verify the functionality of your methods.

## Database configuration

The default connection targets `jdbc:mysql://localhost:3306/testdatabase` with user `root` and no password. Override these settings without committing credentials by defining `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables. Hibernate uses `update` schema management so starting the application does not recreate existing tables.

## Run the REST API with Quarkus

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/testdatabase"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-password"
mvn quarkus:dev
```

The API starts at `http://localhost:8080`. Check it with `Invoke-RestMethod http://localhost:8080/api`.

If Maven reports `PKIX path building failed` on Windows, first set `$env:MAVEN_OPTS = "-Djavax.net.ssl.trustStoreType=Windows-ROOT"`.

| Method | Path | Purpose |
|---|---|---|
| GET | `/api` | Application status |
| GET, POST | `/api/departments` | List or create departments |
| GET, PUT | `/api/departments/{id}` | Read or replace a department |
| PUT | `/api/departments/{id}/location` | Update location with `{"location":"HQ"}` |
| GET, POST | `/api/roles` | List or create roles |
| GET, PUT | `/api/roles/{id}` | Read or replace a role |
| GET, POST | `/api/employees` | List or create employees |
| GET, PUT | `/api/employees/{id}` | Read or replace an employee |
| PUT | `/api/employees/{id}/department/{departmentId}` | Assign a department |
| PUT | `/api/employees/{id}/role/{roleId}` | Assign a role |
| GET, POST | `/api/tasks` | List or create tasks |
| GET, PUT | `/api/tasks/{id}` | Read or replace a task |
| PUT | `/api/tasks/{id}/status` | Update status with `{"status":"DONE"}` |
| PUT | `/api/tasks/{id}/employee/{employeeId}` | Assign an employee |

Filter employees with `/api/employees?minimumSalary=50000` or `/api/employees?hiredFrom=2022-01-01&hiredTo=2022-12-31`.

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/departments `
  -ContentType application/json -Body '{"name":"Engineering","location":"HQ"}'
```
