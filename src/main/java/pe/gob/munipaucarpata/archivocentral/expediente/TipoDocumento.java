package pe.gob.munipaucarpata.archivocentral.expediente;

/**
 * Tipos de documento válidos para el Archivo Central MDP.
 * Se almacena como STRING en la columna tipo_documento.
 */
public enum TipoDocumento {
    OFICIO,
    MEMORANDO,
    INFORME,
    CARTA,
    SOLICITUD,
    RESOLUCION,
    EXPEDIENTE_ADMINISTRATIVO,
    CONTRATO,
    CONVENIO,
    OTRO
}
