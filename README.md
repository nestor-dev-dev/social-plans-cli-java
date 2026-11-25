# Social Plans Manager (CLI)

Aplicación de línea de comandos (CLI) desarrollada en Java para la creación, búsqueda, suscripción y valoración de planes sociales compuestos por múltiples actividades secuenciales.

## Stack Tecnológico
- **Lenguaje:** Java
- **Gestión de Dependencias:** Maven
- **Paradigma y Diseño:** Programación Orientada a Objetos (POO), arquitectura modular y patrones de diseño

## Funcionalidades y Reglas de Negocio
- **Gestión de Usuarios y Sesiones:** Registro con validación de unicidad (nombre de usuario y teléfono), restricciones de edad y control de sesiones (`login` / `logout`).
- **Actividades y Tarifas Dinámicas:** Soporte para actividades genéricas, cine y teatro, aplicando descuentos automáticos según franjas de edad.
- **Integridad de Planes:**
  - Cálculo dinámico del aforo máximo del plan en función del límite más restrictivo de sus actividades.
  - Cálculo automático de la duración total (incluyendo tiempos de desplazamiento) y del coste por participante.
  - Prevención de solapamientos horarios, suscripciones duplicadas y modificaciones sobre planes pasados.
- **Consultas y Valoraciones:** Listados filtrados y ordenación por fecha o puntuación histórica del propietario.

## Ejecución Local
1. Clonar el repositorio e importarlo como proyecto Maven en IntelliJ IDEA o Visual Studio Code.
2. Ejecutar la clase principal `Main` e introducir los comandos a través de la consola.

---
*Proyecto académico — Grado en Ingeniería del Software (ETSISI - UPM).*
