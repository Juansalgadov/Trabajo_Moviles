# Hectar-IA

App movil para el caso de TECMUNDO, de la asignatura DSY1105 Desarrollo de
Aplicaciones Moviles (Duoc UC).

TECMUNDO trabaja con drones y agricultura de precision. Lo que nos pidieron es un
MVP que conecte a los agricultores que necesitan un servicio (fumigacion,
aplicacion con dron, monitoreo) con los prestadores que lo ofrecen. La app no hace
contratos ni pagos, llega hasta la vinculacion simulada.

## Nombre del proyecto

Hectar-IA

## Integrantes

- Juan Salgado Vargas (Juansalgadov)
- Lino Requena (LiinooRF)

## Funcionalidades

- registro y login de usuarios (productor o prestador) contra el backend
- el productor publica una necesidad: cultivo, servicio, hectareas, zona y fecha
- el matching calcula un porcentaje de compatibilidad comparando zona (30),
  servicio (30), cultivo (20), capacidad (10) y disponibilidad (10)
- el productor compara los resultados, ve el detalle de cada prestador y
  manifiesta interes (vinculacion que queda registrada con su porcentaje)
- el clima de la zona del predio con una api externa, para saber si el dia sirve
  para fumigar
- el prestador edita su perfil: nombre, cobertura, capacidad y experiencia
- historial de necesidades y vinculaciones por usuario
- notificaciones cuando el matching encuentra prestadores, y GPS para llenar la
  zona del predio (los dos recursos nativos)
- persistencia local con Room para la sesion activa y de respaldo si no hay internet

## Arquitectura

MVVM: las pantallas (ui/) no tocan datos, hablan con los viewmodel/, que validan
y deciden, y los datos viven en model/ (Room), repository/ y la capa nueva
data/ (retrofit + modelos del api).

## Endpoints utilizados

Backend propio en Supabase (REST automatico sobre las tablas, base
hectaria-ep3). La llave publica va en el RetrofitInstance porque es la que
corresponde exponer en una app cliente; la secret nunca sale del dashboard.

| metodo | endpoint | para que |
|---|---|---|
| GET | /prestadores?select=*&order=id | lista de prestadores |
| GET | /usuarios?correo=..&clave=.. | login |
| POST | /usuarios | registro |
| PATCH | /usuarios?correo=.. | editar perfil |
| POST | /necesidades | guardar necesidad |
| GET | /necesidades?correo_usuario=.. | necesidades del usuario |
| POST | /vinculaciones | guardar vinculacion |
| GET | /vinculaciones?correo_usuario=.. | historial de vinculaciones |

API externa: Open-Meteo (gratis, sin llave)
- GET geocoding-api.open-meteo.com/v1/search?name=.. -> coordenadas de la zona
- GET api.open-meteo.com/v1/forecast?latitude=..&longitude=.. -> clima actual

## Pruebas unitarias

JUnit, en app/src/test: CompatibilidadTest (5 tests del matching, 100% de la
logica del repository) y ClimaTest (5 tests de la evaluacion del clima).
Se corren con ./gradlew testDebugUnitTest.

## Como ejecutar

1. abrir el proyecto en Android Studio (o ./gradlew assembleDebug con
   ANDROID_HOME configurado)
2. correr en un emulador o dispositivo con internet
3. registrarse con cualquier correo y clave de 6 o mas caracteres

## APK firmado y llave

El apk release se firma con hectaria.jks (alias hectaria). La llave y su clave
quedan en firmas/ del repo, y el apk compilado en entrega/. Comando:

./gradlew assembleRelease
$ANDROID_HOME/build-tools/37.0.0/apksigner sign --ks firmas/hectaria.jks \
  --ks-key-alias hectaria --ks-pass pass:hectaria2026 --out entrega/hectaria.apk \
  app/build/outputs/apk/release/app-release-unsigned.apk

## Planificacion

El trabajo se planifico en el GitHub Project "ep3 backend y api" (12 tarjetas,
6 y 6 entre los dos) y el milestone "Backend y API" en los issues del repo.

## Datos

Todos los datos que se usan son ficticios. No hay nombres, rut, telefonos ni
ubicaciones reales de personas o empresas.
