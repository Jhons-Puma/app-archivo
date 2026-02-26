-- ============================================================
-- ARCHIVO CENTRAL MDP — Datos de prueba (5 registros)
-- Ejecutar en PgAdmin 4 conectado a: archivo_central_mdp
-- ============================================================
-- Nota: Los IDs son los números reales de los expedientes
--       físicos, ingresados manualmente.
-- ============================================================

INSERT INTO archivo.expedientes (
    id,
    anio,
    fecha_registro,
    tipo_documento,
    nro_documento,
    nombre_solicitante,
    dni,
    asunto,
    dirigido_a,
    folios,
    archivado_con,
    observaciones,
    created_at,
    updated_at,
    version
) VALUES

-- Registro 1: OFICIO
(
    1,
    2024,
    '2024-03-10',
    'OFICIO',
    'OFICIO N° 045-2024-MDP/GM',
    'MUNICIPALIDAD DISTRITAL DE PAUCARPATA',
    '29104567',
    'REMISION DE INFORME DE ACTIVIDADES DEL PRIMER TRIMESTRE 2024 A LA GERENCIA MUNICIPAL',
    'GERENCIA MUNICIPAL - MDP',
    12,
    'CAJA N° 14 / FÓLDER AZUL',
    'Documento recibido con cargo de recepción. Contiene anexos fotográficos.',
    NOW(),
    NOW(),
    0
),

-- Registro 2: MEMORANDO
(
    2,
    2024,
    '2024-04-05',
    'MEMORANDO',
    'MEMORANDO N° 023-2024-MDP/RRHH',
    'QUISPE MAMANI JUAN CARLOS',
    '43215678',
    'COMUNICACIÓN SOBRE CAMBIO DE HORARIO DE TRABAJO PARA EL PERSONAL DE LA GERENCIA DE DESARROLLO SOCIAL',
    'GERENCIA DE DESARROLLO SOCIAL - MDP',
    3,
    'CAJA N° 07 / FÓLDER VERDE',
    NULL,
    NOW(),
    NOW(),
    0
),

-- Registro 3: SOLICITUD ciudadana
(
    3,
    2024,
    '2024-05-20',
    'SOLICITUD',
    'SOLICITUD S/N',
    'HUANCA CONDORI ROSA ELVIRA',
    '47382910',
    'SOLICITUD DE CONSTANCIA DE POSESION DEL PREDIO UBICADO EN AV. KENNEDY MZ. F LOTE 12, URBANIZACION LAS FLORES, PAUCARPATA',
    'SUB GERENCIA DE CATASTRO Y HABILITACIONES URBANAS - MDP',
    5,
    'CAJA N° 22 / FÓLDER AMARILLO',
    'Adjunta copia de DNI, plano de ubicación y recibo de pago por S/ 35.00.',
    NOW(),
    NOW(),
    0
),

-- Registro 4: INFORME técnico (año 2000, para probar filtro por año)
(
    4521,
    2000,
    '2000-07-15',
    'INFORME',
    'INFORME TÉCNICO N° 018-2000-MDP/OPI',
    'CCAMA FLORES EDGAR ANTONIO',
    '29847563',
    'EVALUACION TECNICA DEL PERFIL DE PROYECTO: MEJORAMIENTO DEL SERVICIO DE AGUA POTABLE EN AA.HH. PRIMAVERA SECTOR III, PAUCARPATA',
    'GERENCIA DE INFRAESTRUCTURA Y DESARROLLO URBANO - MDP',
    28,
    'CAJA N° 31 / FÓLDER ROJO',
    'Informe con observaciones subsanables. Devuelto para levantamiento. Expediente completo incluye planos.',
    NOW(),
    NOW(),
    0
),

-- Registro 5: RESOLUCIÓN de Alcaldía (año 2000)
(
    5500,
    2000,
    '2000-09-03',
    'RESOLUCION',
    'RESOLUCION DE ALCALDIA N° 312-2000-MDP/A',
    'ALCALDIA MUNICIPALIDAD DISTRITAL DE PAUCARPATA',
    '00000001',
    'APROBACION DEL PLAN OPERATIVO INSTITUCIONAL (POI) CORRESPONDIENTE AL EJERCICIO FISCAL 2000',
    'GERENCIA MUNICIPAL / TODAS LAS UNIDADES ORGANICAS - MDP',
    47,
    'CAJA N° 05 / ARCHIVADOR NEGRO',
    'Resolución publicada en el portal de transparencia de la MDP. Firmada por Alcalde y Secretaria General.',
    NOW(),
    NOW(),
    0
);

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SELECT
    id,
    anio,
    fecha_registro,
    tipo_documento,
    nombre_solicitante,
    dni,
    folios,
    archivado_con
FROM archivo.expedientes
ORDER BY anio DESC, id ASC;
