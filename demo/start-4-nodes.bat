@echo off

rem Elimina las carpetas "caches" y "database" y el archivo "DATABASE_METADATA.json"
rem dentro de los directorios ".\Node-1\data", ".\Node-2\data", ".\Node-3\data" y ".\Node-4\data"

if exist ".\Node-1\data\caches" rd /s /q ".\Node-1\data\caches"
if exist ".\Node-1\data\database" rd /s /q ".\Node-1\data\database"
if exist ".\Node-1\data\DATABASE_METADATA.json" del /q ".\Node-1\data\DATABASE_METADATA.json"

if exist ".\Node-2\data\caches" rd /s /q ".\Node-2\data\caches"
if exist ".\Node-2\data\database" rd /s /q ".\Node-2\data\database"
if exist ".\Node-2\data\DATABASE_METADATA.json" del /q ".\Node-2\data\DATABASE_METADATA.json"

if exist ".\Node-3\data\caches" rd /s /q ".\Node-3\data\caches"
if exist ".\Node-3\data\database" rd /s /q ".\Node-3\data\database"
if exist ".\Node-3\data\DATABASE_METADATA.json" del /q ".\Node-3\data\DATABASE_METADATA.json"

if exist ".\Node-4\data\caches" rd /s /q ".\Node-4\data\caches"
if exist ".\Node-4\data\database" rd /s /q ".\Node-4\data\database"
if exist ".\Node-4\data\DATABASE_METADATA.json" del /q ".\Node-4\data\DATABASE_METADATA.json"

rem Abre tres terminales en las rutas ".\Node-1", ".\Node-2", ".\Node-3" y ".\Node-4"

start cmd /k cd ".\Node-1"
start cmd /k cd ".\Node-2"
start cmd /k cd ".\Node-3"
start cmd /k cd ".\Node-4"
