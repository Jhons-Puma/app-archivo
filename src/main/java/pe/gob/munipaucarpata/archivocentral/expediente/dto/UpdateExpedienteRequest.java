package pe.gob.munipaucarpata.archivocentral.expediente.dto;

import jakarta.validation.constraints.*;
import pe.gob.munipaucarpata.archivocentral.expediente.TipoDocumento;

import java.time.LocalDate;

/**
 * DTO de entrada para corregir datos de un expediente ya registrado.
 *
 * No incluye id ni anio — la clave primaria no se puede modificar.
 * Solo se corrigen los datos del documento.
 */
public record UpdateExpedienteRequest(

                @NotNull(message = "Fecha de Registro es requerida") LocalDate fechaRegistro,

                @NotNull(message = "Tipo de Documento es requerido") TipoDocumento tipoDocumento,

                @NotBlank(message = "N° de Documento es requerido") @Size(max = 50, message = "N° de Documento no puede superar 50 caracteres") String nroDocumento,

                @NotBlank(message = "Nombre del Solicitante es requerido") @Size(max = 200, message = "Nombre del Solicitante no puede superar 200 caracteres") String nombreSolicitante,

                @NotBlank(message = "DNI es requerido") @Size(min = 8, max = 8, message = "DNI debe tener exactamente 8 dígitos") @Pattern(regexp = "\\d{8}", message = "DNI debe contener solo dígitos") String dni,

                @NotBlank(message = "Asunto es requerido") String asunto,

                @NotBlank(message = "Dirigido A es requerido") @Size(max = 200, message = "Dirigido A no puede superar 200 caracteres") String dirigidoA,

                @NotNull(message = "Folios es requerido") @Min(value = 1, message = "Folios debe ser al menos 1") Integer folios,

                // Opcionales
                @Size(max = 300, message = "Archivado Con no puede superar 300 caracteres") String archivadoCon,

                String observaciones

) {
}
