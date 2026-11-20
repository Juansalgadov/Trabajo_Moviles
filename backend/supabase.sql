-- tablas del proyecto hectaria
-- pegar esto completo en el sql editor de supabase
-- (no activamos RLS para que la app pueda usar la anon key sin login de supabase)

create table usuarios (
  correo text primary key,
  nombre text not null,
  clave text not null,
  tipo text not null default 'productor'
);

create table prestadores (
  id int primary key,
  nombre text not null,
  zona text not null,
  servicios text not null,
  cultivos text not null,
  capacidad int not null,
  disponible boolean not null default true,
  rating real not null default 4
);

create table necesidades (
  id bigint generated always as identity primary key,
  correo_usuario text not null,
  zona text not null,
  cultivo text not null,
  servicio text not null,
  superficie int not null,
  fecha timestamp default now()
);

create table vinculaciones (
  id bigint generated always as identity primary key,
  correo_usuario text not null,
  prestador_id int not null references prestadores(id),
  compatibilidad int not null,
  fecha timestamp default now()
);

insert into prestadores (id, nombre, zona, servicios, cultivos, capacidad, disponible, rating) values
(1, 'AgroDron Maule', 'Linares', 'Fumigación con dron, Monitoreo de cultivo', 'Maíz, Trigo', 150, true, 4.8),
(2, 'Servicios Aéreos del Sur', 'Talca', 'Fumigación con dron, Aplicación foliar', 'Maíz, Arándanos', 100, true, 4.5),
(3, 'DroneCampo SpA', 'Parral', 'Fumigación con dron, Siembra con dron', 'Trigo, Uva de mesa', 80, false, 4.2),
(4, 'AgroTec Parral', 'Parral', 'Monitoreo de cultivo, Aplicación foliar', 'Maíz, Manzanos', 60, true, 4.0),
(5, 'Fumigadora Ñuble', 'Chillán', 'Fumigación con dron', 'Arándanos, Uva de mesa', 40, true, 3.9);
