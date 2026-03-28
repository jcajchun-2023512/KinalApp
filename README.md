# KinalApp
 
Documentación del Proyecto KinalApp: 

Lo que hicimos fue una API REST construida con tecnologías modernas del ecosistema Java. Su función principal es gestionar el ciclo de ventas de un negocio, permitiendo realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre entidades fundamentales como clientes, productos, usuarios y las transacciones de venta.

Análisis de los Componentes
* Stack Tecnológico: Utiliza las versiones más recientes y estables del mercado. Java 21 ofrece las últimas mejoras de rendimiento, mientras que Spring Boot 4 (que en el contexto actual de 2026 es el estándar) facilita la configuración automática y el despliegue rápido.
* Arquitectura de Datos: Se apoya en MySQL para el almacenamiento persistente y Maven para manejar todas las librerías necesarias.
* Estructura de Endpoints: La aplicación sigue una lógica de recursos muy clara:
* Gestión de Inventario: Control de productos y monitoreo de stock.
* Gestión de Ventas: Registro detallado de transacciones (Venta y DetalleVenta), lo cual indica que puede manejar múltiples productos por cada factura.
* Seguridad/Control: Gestión de usuarios y estados de actividad (filtros para ver solo registros "activos").

Flujo de Trabajo (Instalación)
El proceso descrito es el estándar para un desarrollador:

* Preparación del entorno: Asegurar que el motor de base de datos y el kit de desarrollo (JDK) estén listos.
* Sincronización: Clonar el código desde el repositorio de GitHub.
* Configuración: Es vital el paso de revisar el archivo application.properties, ya que ahí se define la conexión a la base de datos y el puerto de red (en este caso, el 8081).
* Pruebas: Se sugiere el uso de Postman o el navegador para interactuar con los datos a través de las URLs (por ejemplo, para ver la lista de clientes).

Una pequeña observación técnica
En la sección de Endpoints, mencionas que se usa /{dpi} para buscar, eliminar y actualizar.

* Dato curioso: Aunque usas la etiqueta {dpi} (que es el identificador único en Guatemala), en el desarrollo de software se suele llamar genéricamente {id}. Es genial que esté personalizado para el contexto local.
* Diferenciación de Métodos: Para que esos 3 endpoints funcionen en la misma URL (/{dpi}), Spring Boot utiliza diferentes métodos HTTP: GET para buscar, DELETE para eliminar y PUT/PATCH para actualizar.

## Tecnologias utilizadas
* **Java 21**
* **Spring Boot 4.0.2**
* **Maven** (Gestor de dependencias)
* **MySQL** (Sistema gestor de Base de Datos)

## Requisitos Previos
Antes de ejecutar la aplicación debe tener instalado:
* JDK 17 o superior
* Maven instalado
* Una instancia activa en MySQL

Instalaciones opcionales
* Postman

## Instalación y Ejecución 
1. Clonar repositorio https://github.com/jcajchun-2023512/KinalApp.git.
2. Abrir Intellij IDEA.
3. Abrir la carpeta que clono.
4. Abrir MySQL en su ordenador.
5. Ingresar a la instancia activa en MySQL.
6. Regresar a Intellij IDEA.
7. Dirigirse a la carpeta "src\main\java\com\javiercajchun".
11. Dirirgirse a KinalAppApplication y ejecutar la aplicación.
12. Abrir la carpeta "resources/application.properties".
13. Verificar que puerto esta utilizando la aplicación.
12. Abrir el navegador y poner el puerto http://localhost:8081/clientes.

## Endpoints 
* Cliente: 
1. "/clientes": Esto nos lista los clientes y agrega el cliente. 
2. "/estado": Esto lista los clientes que están activos.
3. "/{dpi}": Esto busca el cliente mediante él id.
4. "/{dpi}": Esto elimina el cliente mediante él id.
5. "/{dpi}": Esto actualiza el cliente mediante él id.

* Usuario:
1. "/usuarios": Esto nos lista los usuarios y agrega el usuario.
2. "/estado": Esto lista los usuarios que están activos.
3. "/{dpi}": Esto busca el usuario mediante él id.
4. "/{dpi}": Esto elimina el usuario mediante él id.
5. "/{dpi}": Esto actualiza el usuario mediante él id.

* Producto:
1. "/productos": Esto nos lista los productos y agrega el producto.
2. "/estado": Esto lista los productos que están activos.
3. "/{dpi}": Esto busca el producto mediante él id.
4. "/{dpi}": Esto elimina el producto mediante él id.
5. "/{dpi}": Esto actualiza el producto mediante él id.
6. "/stock": Esto lista el nombre del producto y la cantidad que hay en stock.

* Venta:
1. "/ventas": Esto nos lista las ventas y agrega la venta.
2. "/estado": Esto lista las ventas que están activos.
3. "/{dpi}": Esto busca la venta mediante él id.
4. "/{dpi}": Esto elimina la venta mediante él id.
5. "/{dpi}": Esto actualiza la venta mediante él id.

* DetalleVenta:
1. "/detalleVentas": Esto nos lista los detalles de venta y agrega el detalle venta.
2. "/estado": Esto lista los detalles de venta que están activos.
3. "/{dpi}": Esto busca los detalles de venta mediante él id.
4. "/{dpi}": Esto elimina los detalles de venta mediante él id.
5. "/{dpi}": Esto actualiza los detalles de venta mediante él id.