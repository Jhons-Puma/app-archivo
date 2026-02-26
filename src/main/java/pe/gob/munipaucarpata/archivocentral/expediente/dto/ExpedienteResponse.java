package pe.gob.munipaucarpata.archivocentral.expediente.dto;

import pe.gob.munipaucarpata.archivocentral.expediente.ExpedienteEntity;
import pe.gob.munipaucarpata.archivocentral.expediente.TipoDocumento;

import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO de salida para los endpoints de expedientes.
 * Mapea todos los campos de ExpedienteEntity al cliente.
 */
public record ExpedienteResponse(

        Integer id,
        Integer anio,
        LocalDate fechaRegistro,
        TipoDocumento tipoDocumento,
        String nroDocumento,
        String nombreSolicitante,
        String dni,
        String asunto,
        String dirigidoA,
        Integer folios,
        String archivadoCon,
        String observaciones,
        Instant createdAt,
        Instant updatedAt,
        int version

) {
    /**
     * Factory method estático: convierte entidad JPA → DTO de respuesta.
     */
    public static ExpedienteResponse from(ExpedienteEntity entity) {
        return new ExpedienteResponse(
                entity.getId(),
                entity.getAnio(),
                entity.getFechaRegistro(),
                entity.getTipoDocumento(),
                entity.getNroDocumento(),
                entity.getNombreSolicitante(),
                entity.getDni(),
                entity.getAsunto(),
                entity.getDirigidoA(),
                entity.getFolios(),
                entity.getArchivadoCon(),
                entity.getObservaciones(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion());
    }
}
