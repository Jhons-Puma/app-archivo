-- ============================================================
-- ARCHIVO CENTRAL MDP — Setup de Base de Datos
-- Ejecutar en PgAdmin 4 conectado a: archivo_central_mdp
-- ============================================================

-- PASO 1: Crear la base de datos (ejecutar conectado a postgres)
-- CREATE DATABASE archivo_central_mdp
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'es_PE.UTF-8'
--     LC_CTYPE = 'es_PE.UTF-8'
--     TEMPLATE = template0;

-- PASO 2: Conectarse a archivo_central_mdp y ejecutar lo siguiente:

-- ============================================================
-- SCHEMA
-- ============================================================
CREATE SCHEMA IF NOT EXISTS archivo;

ALTER DATABASE archivo_central_mdp
    SET search_path TO archivo, public;

-- ============================================================
-- TABLA: expedientes
-- ============================================================
DROP TABLE IF EXISTS archivo.expedientes;

CREATE TABLE archivo.expedientes
(
    -- IDENTIFICACIÓN
    id                  INTEGER         NOT NULL,

    -- AÑO DEL EXPEDIENTE
    anio                INTEGER         NOT NULL,

    -- DATOS DEL DOCUMENTO
    fecha_registro      DATE            NOT NULL,
    tipo_documento      VARCHAR(50)     NOT NULL,
    nro_documento       VARCHAR(50)     NOT NULL,

    -- DATOS DEL SOLICITANTE
    nombre_solicitante  VARCHAR(200)    NOT NULL,
    dni                 VARCHAR(8)      NOT NULL,

    -- CONTENIDO Y DESTINO
    asunto              TEXT            NOT NULL,
    dirigido_a          VARCHAR(200)    NOT NULL,
    folios              INTEGER         NOT NULL DEFAULT 1,

    -- INFORMACIÓN DE ARCHIVO (opcionales)
    archivado_con       VARCHAR(300),
    observaciones       TEXT,

    -- AUDITORÍA
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    version             INTEGER         NOT NULL DEFAULT 0,

    CONSTRAINT pk_expedientes PRIMARY KEY (id),
    CONSTRAINT chk_id_positivo CHECK (id > 0),
    CONSTRAINT chk_anio CHECK (anio BETWEEN 1900 AND 2100),
    CONSTRAINT chk_folios CHECK (folios >= 1),
    CONSTRAINT chk_dni_length CHECK (LENGTH(dni) = 8),
    CONSTRAINT chk_tipo_documento CHECK (
        tipo_documento IN (
            'OFICIO', 'MEMORANDO', 'INFORME', 'CARTA', 'SOLICITUD',
            'RESOLUCION', 'EXPEDIENTE_ADMINISTRATIVO', 'CONTRATO',
            'CONVENIO', 'OTRO'
        )
    )
);

-- ============================================================
-- ÍNDICES para búsquedas frecuentes
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_exp_anio
    ON archivo.expedientes (anio);

CREATE INDEX IF NOT EXISTS idx_exp_anio_id
    ON archivo.expedientes (anio, id);

CREATE INDEX IF NOT EXISTS idx_exp_dni
    ON archivo.expedientes (dni);

CREATE INDEX IF NOT EXISTS idx_exp_tipo
    ON archivo.expedientes (tipo_documento);

CREATE INDEX IF NOT EXISTS idx_exp_fecha
    ON archivo.expedientes (fecha_registro DESC);

CREATE INDEX IF NOT EXISTS idx_exp_nombre
    ON archivo.expedientes (nombre_solicitante);

-- ============================================================
-- COMENTARIOS de tabla y columnas
-- ============================================================
COMMENT ON TABLE archivo.expedientes
    IS 'Inventario de expedientes físicos del Archivo Central - MDP';

COMMENT ON COLUMN archivo.expedientes.id
    IS 'Número del expediente físico (NO autoincremental). Ingresado manualmente al registrar.';

COMMENT ON COLUMN archivo.expedientes.anio
    IS 'Año al que pertenece el expediente. Se usa para filtrar por período.';

COMMENT ON COLUMN archivo.expedientes.tipo_documento
    IS 'Tipo: OFICIO, MEMORANDO, INFORME, CARTA, SOLICITUD, RESOLUCION, EXPEDIENTE_ADMINISTRATIVO, CONTRATO, CONVENIO, OTRO';

COMMENT ON COLUMN archivo.expedientes.version
    IS 'Control de concurrencia optimista (Hibernate @Version)';

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SELECT
    table_schema,
    table_name,
    (SELECT COUNT(*) FROM archivo.expedientes) AS total_registros
FROM information_schema.tables
WHERE table_schema = 'archivo'
  AND table_name = 'expedientes';
