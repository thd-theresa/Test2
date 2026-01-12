# Docker Setup für Development

## Entwicklungsumgebung (nur Datenbank)

Wenn Sie die Applikation lokal entwickeln möchten und nur die PostgreSQL-Datenbank in Docker laufen lassen wollen:

### 1. Datenbank starten

```bash
docker compose -f docker-compose.dev.yml up -d
```

### 2. Datenbank-Status prüfen

```bash
docker compose -f docker-compose.dev.yml ps
```

### 3. Spring Boot Applikation lokal starten

```bash
./mvnw spring-boot:run
```

Die Applikation ist dann erreichbar unter: http://localhost:8080

### 4. Datenbank stoppen

```bash
docker compose -f docker-compose.dev.yml down
```

### 5. Datenbank stoppen und Daten löschen

```bash
docker compose -f docker-compose.dev.yml down -v
```

## Produktionsumgebung (Datenbank + Applikation)

Wenn Sie die gesamte Applikation in Docker laufen lassen möchten:

### 1. Applikation und Datenbank starten

```bash
docker compose up -d
```

### 2. Status prüfen

```bash
docker compose ps
```

### 3. Logs anzeigen

```bash
docker compose logs -f app
```

### 4. Stoppen

```bash
docker compose down
```

## Häufige Probleme

### Problem: "Port 5432 already in use"

Wenn Sie eine lokale PostgreSQL-Installation haben, können Sie den Port ändern:

In `docker-compose.dev.yml`:
```yaml
ports:
  - "5433:5432"  # Ändert externen Port zu 5433
```

Dann in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/attendance_db
```

### Problem: "Cannot connect to database"

1. Prüfen Sie, ob der Container läuft: `docker compose -f docker-compose.dev.yml ps`
2. Prüfen Sie die Logs: `docker compose -f docker-compose.dev.yml logs postgres`
3. Testen Sie die Verbindung: `docker compose -f docker-compose.dev.yml exec postgres pg_isready -U attendance_user`

### Problem: Docker läuft aber ich kann nicht zugreifen

Wenn der Docker-Container läuft aber Sie sich nicht mit der Datenbank verbinden können:

1. Prüfen Sie die Portzuordnung:
   ```bash
   docker port attendance-postgres
   ```

2. Stellen Sie sicher, dass keine Firewall den Port blockiert

3. Versuchen Sie, die Verbindung direkt zu testen:
   ```bash
   docker compose -f docker-compose.dev.yml exec postgres psql -U attendance_user -d attendance_db
   ```

## Datenbankzugriff

### Mit Docker exec

```bash
docker compose -f docker-compose.dev.yml exec postgres psql -U attendance_user -d attendance_db
```

### Mit lokalem psql Client

```bash
psql -h localhost -p 5432 -U attendance_user -d attendance_db
```

Passwort: `attendance_password`
