# Guía rápida — Levantar el proyecto y la base

Requisitos ya instalados: **Java 21**, **Maven 3.9**, **MySQL 8**.

## 1. Levantar MySQL

```bash
# Iniciar el servicio (pide contraseña de sudo)
sudo systemctl start mysql

# Verificar que esté corriendo
systemctl is-active mysql
```

## 2. Crear la base de datos (solo la primera vez)

```bash
# Crear la base vacia. Las TABLAS las crea Hibernate solo (hbm2ddl.auto=update)
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS tp_grupo14;"

# Verificar que exista
mysql -u root -proot -e "SHOW DATABASES LIKE 'tp_grupo14';"
```

> La conexión está configurada en `src/main/resources/hibernate.cfg.xml`
> (base `tp_grupo14`, usuario `root`, contraseña `root`).

## 3. Compilar el proyecto

```bash
# Compila el codigo principal
mvn compile

# Compila tambien los tests
mvn test-compile
```

## 4. Correr los casos de uso (tests)

```bash
# Corre TODOS los tests
mvn test

# Corre un test especifico
mvn test -Dtest=NachoTest
mvn test -Dtest=JoaquinTest
mvn test -Dtest=LeandroTest
```

## 5. Ver el output limpio de un test (para presentacion)

```bash
# Filtra los logs de Hibernate y deja solo el resultado del caso de uso
mvn test -Dtest=NachoTest 2>&1 | grep -v "Hibernate:\|org.hibernate\|INFO\|WARN"
```

## 6. Consultar la base directamente (opcional, para mostrar los datos)

```bash
# Ver todas las tablas
mysql -u root -proot tp_grupo14 -e "SHOW TABLES;"

# Contar registros por tabla
mysql -u root -proot tp_grupo14 -e "
SELECT 'festivales' t, COUNT(*) n FROM festival
UNION ALL SELECT 'personal', COUNT(*) FROM personal
UNION ALL SELECT 'pedidos',  COUNT(*) FROM pedido;"
```

## 7. Limpiar la base (empezar de cero)

```bash
# Borra la base y la vuelve a crear vacia
mysql -u root -proot -e "DROP DATABASE tp_grupo14; CREATE DATABASE tp_grupo14;"
```

---

## Resumen ultra rapido

```bash
sudo systemctl start mysql
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS tp_grupo14;"
mvn test
```
