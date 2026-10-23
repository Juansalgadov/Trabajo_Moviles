# Hectar-IA

App movil para el caso de TECMUNDO, de la asignatura DSY1105 Desarrollo de
Aplicaciones Moviles (Duoc UC).

TECMUNDO trabaja con drones y agricultura de precision. Lo que nos pidieron es un
MVP que conecte a los agricultores que necesitan un servicio (fumigacion,
aplicacion con dron, monitoreo) con los prestadores que lo ofrecen. La app no hace
contratos ni pagos, llega hasta la vinculacion simulada.

## Integrantes

- Juan Salgado (Juansalgadov)
- Lino Requena (LiinooRF)

## Que hace la app

- el productor se registra y publica una necesidad: cultivo, servicio, hectareas,
  zona y fecha
- la app le muestra los prestadores compatibles con un porcentaje, que sale de
  comparar zona, cultivo, servicio, capacidad y disponibilidad
- el productor compara las alternativas, entra al detalle de un prestador y
  manifiesta interes
- el prestador tiene su propio perfil con los servicios que ofrece, su cobertura,
  su capacidad maxima y desde cuando esta disponible

## Funcionalidades

- formularios de login, registro, publicar necesidad y perfil del prestador
- validacion de los campos con mensaje y marca en el campo que esta malo
- persistencia local con Room: el usuario del login, las necesidades publicadas y
  las vinculaciones
- navegacion entre las vistas con la barra de abajo
- modal de confirmacion de la vinculacion
- animaciones en el logo del login y en la entrada de las tarjetas de resultados
- recursos nativos: GPS para llenar la zona del predio y notificaciones para avisar
  cuando el matching encuentra prestadores
- la app esta organizada con MVVM, la logica de validacion va en los viewmodel y no
  dentro de las pantallas

## Datos

Todos los datos que se usan son ficticios. No hay nombres, rut, telefonos ni
ubicaciones reales de personas o empresas.
