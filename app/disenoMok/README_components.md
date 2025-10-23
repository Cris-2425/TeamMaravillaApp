# Componentes de la aplicación

Este documento describe los componentes que se usarán en la aplicación, sus parámetros principales y su función general.  
Sirve como guía base después del diseño o mockup.

---

## Pantalla de inicio / mis listas

**Tarjeta de lista**  
Muestra el nombre de la lista (por ejemplo "Compra semanal") y cuántos productos contiene.  
**Parámetros:** nombre, cantidad de elementos, foto de cada elemento de la lista (max 5), acción al pulsar.

**Tarjeta "Crear nueva lista"**  
Permite añadir una lista nueva.  
**Parámetros:** texto escrito, imagen de fondo, acción al pulsar.

**Barra inferior de navegación**  
Incluye los iconos de Inicio, Perfil, Cámara, Recetas y Salir.  
**Parámetros:** pestaña seleccionada, funciones que se ejecutan al pulsar cada icono.

**Título de sección**  
Texto grande que separa bloques dentro de la pantalla.

---

## Pantalla de detalle de lista

**Cabecera de la lista**  
Muestra el nombre de la lista y pequeños botones para ordenar o mostrar/ocultar categorías.

**Producto**  
Cada línea representa un producto.  
**Parámetros:** nombre del producto, si está comprado o no, función al marcar o desmarcar.

**Sección de categoría**  
Agrupa productos por tipo (por ejemplo "Fruta", "Carne", "Limpieza").  
**Parámetros:** nombre de la categoría, lista de productos, acción al pulsar el encabezado si se quiere colapsar o expandir.

**Barra de búsqueda / añadir producto**  
Campo donde se puede escribir un producto nuevo o buscar uno existente.  
**Parámetros:** texto por defecto, acción al pulsar.

**Fila de productos usados recientemente**  
Muestra productos usados recientemente para volver a añadirlos.  
**Parámetros:** lista de elementos, acción al pulsar cada uno.

---

## Pantalla de crear lista

**Campo de nombre de lista**  
Texto donde se escribe el nombre de la nueva lista.

**Fondos de ejemplo**  
Varios círculos con fondos o colores diferentes para elegir uno.  
**Parámetros:** nombre o tipo del fondo, si está seleccionado, acción al pulsar.

**Botón "Guardar lista"**  
Guarda la nueva lista creada.

**Sugerencias de listas**  
Fila con ejemplos de listas ya hechas (por ejemplo "Compra semanal", "Cumpleaños", "Barbacoa").  
**Parámetros:** lista de sugerencias, acción al pulsar una.

---

## Pantalla de recetas

**Tarjeta de receta**  
Muestra la imagen de la receta, su nombre y algunos ingredientes.  
**Parámetros:** nombre, lista de ingredientes, acción al pulsar.

**Botones de filtro**  
Permiten alternar entre “Todas” y “Mis recetas”.  
**Parámetros:** filtro seleccionado, acción al pulsar cada botón.

**Formulario para añadir receta**  
Campos para nombre, lista de ingredientes y botón guardar.  
**Parámetros:** texto del nombre, lista de ingredientes, acción al guardar.

---

## Pantalla de perfil

**Cabecera del perfil**  
Muestra la foto (o inicial), el nombre y un botón para cambiar la imagen.  
**Parámetros:** nombre del usuario, acción al pulsar la imagen.

**Cuadrícula de opciones**  
Botones circulares con distintas opciones (por ejemplo “Ajustes”, “Historial”).  
**Parámetros:** lista de opciones, acción al pulsar una.

**Campo para editar nombre**  
Permite cambiar el nombre del usuario.  
**Parámetros:** texto actual, función al escribir, acción al guardar.

---

## Pantalla de login o registro

**Formulario de acceso**  
Dos campos (usuario y contraseña) y un botón “Entrar”.  
**Parámetros:** usuario, contraseña, función al pulsar.

**Logo redondo con inicial**  
Elemento decorativo, muestra la inicial del usuario o un icono de perfil.

---

## Componentes generales reutilizables

**Botón circular grande**  
Usado en varios lugares, muestra texto o icono y puede estar seleccionado.  
**Parámetros:** texto o icono, estado seleccionado, acción al pulsar.

**Botón principal rectangular**  
Para acciones importantes como “Guardar”, “Entrar” o “Confirmar”.  
**Parámetros:** texto, estado habilitado, acción al pulsar.

**Campo de texto genérico**  
Input donde el usuario escribe.  
**Parámetros:** texto actual, etiqueta, si es contraseña, acción al cambiar el texto.

**Barra superior**  
Muestra el título de la pantalla y una flecha de volver.  
**Parámetros:** texto del título, acción al pulsar la flecha.

**Mensaje de vacío**  
Aparece cuando una lista o sección no tiene elementos.  
**Parámetros:** título y subtítulo del mensaje.
