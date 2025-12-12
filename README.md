# Mercado de Trueque y Subastas Locales

Aplicación de escritorio desarrollada en Java para facilitar el intercambio de bienes mediante trueques y subastas entre usuarios locales. Este sistema permite a los usuarios publicar artículos, realizar ofertas, pujar en subastas y comunicarse mediante un chat integrado.

## 🚀 Características Principales

*   **Gestión de Usuarios**:
    *   Registro e inicio de sesión seguro.
    *   Roles diferenciados: Usuario y Administrador.
    *   Perfil de usuario editable.
*   **Publicaciones**:
    *   **Subasta**: Publicación de artículos con precio base y tiempo límite. Sistema de pujas en tiempo real.
    *   **Trueque**: Intercambio de bienes. Los usuarios pueden ofertar sus propios artículos a cambio.
*   **Sistema de Mensajería**: Chat integrado para la comunicación entre comprador y vendedor.
*   **Panel de Administración**: Herramientas para moderar usuarios, eliminar publicaciones y gestionar reportes.
*   **Persistencia de Datos**: Almacenamiento local mediante serialización de objetos y JSON.

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue el patrón de diseño **MVC (Modelo-Vista-Controlador)** para garantizar un código modular y mantenible.

*   **Lenguaje**: Java (JDK 17+)
*   **Interfaz Gráfica**: Java Swing
*   **Arquitectura**:
    *   **Model**: POJOs que representan las entidades (User, Publicacion, Oferta).
    *   **View**: Interfaz de usuario (JFrames, JPanels).
    *   **Controller**: Lógica de control y orquestación de eventos.
    *   **Service**: Reglas de negocio y validaciones.
    *   **Persistence**: Capa de acceso a datos (Repository Pattern).

## 📂 Estructura del Proyecto

El código fuente principal se encuentra en `proyectoFinalPOO/src`:

*   `src/main`: Punto de entrada de la aplicación (`MainApp.java`).
*   `src/model`: Definición de objetos del dominio (`User`, `PublicacionTrueque`, `PublicacionSubasta`, etc.).
*   `src/view`: Componentes visuales (`MainWindow`, `AuthView`, `DetallePublicacionView`).
*   `src/controller`: Controladores que conectan la vista con la lógica de negocio.
*   `src/service`: Lógica pura de negocio (`UserService`, `PublicacionService`).
*   `src/persistence`: Gestión de archivos y almacenamiento de datos.

## ⚙️ Instalación y Ejecución

### Requisitos Previos
*   Tener instalado Java Development Kit (JDK) versión 17 o superior.

### Pasos para Ejecutar

1.  **Clonar el repositorio**:
    ```bash
    git clone [https://github.com/herrerawilfer32-rgb/Mercado-De-Trueque-Y-Subastas-Locales.git](https://github.com/herrerawilfer32-rgb/Mercado-De-Trueque-Y-Subastas-Locales.git)
    cd Mercado-De-Trueque-Y-Subastas-Locales/proyectoFinalPOO
    ```

2.  **Compilar el proyecto**:
    Desde la carpeta `proyectoFinalPOO`, ejecuta:
    ```bash
    javac -d bin -cp "lib/*;src" src/main/MainApp.java
    ```

3.  **Ejecutar la aplicación**:
    ```bash
    java -cp "bin;lib/*" main.MainApp
    ```

## 📄 Manuales de Referencia

Puedes consultar la documentación completa haciendo clic en los siguientes enlaces:

*   [📘 Manual de Usuario](proyectoFinalPOO/Manual_Usuario_MercadoTrueque.pdf) - Guía para el usuario final.
*   [📗 Manual Técnico](proyectoFinalPOO/Manual_Tecnico_MercadoTrueque_Final.pdf) - Documentación para desarrolladores y arquitectura.
*   [📙 Manual de Instalación](proyectoFinalPOO/Manual_Instalacion_MercadoTrueque.pdf) - Guía de configuración y despliegue.

## 👥 Autores
Anggel Leal
Wilfer Herrera
David Santos
