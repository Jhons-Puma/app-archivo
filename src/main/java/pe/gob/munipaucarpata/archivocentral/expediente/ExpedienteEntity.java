package pe.gob.munipaucarpata.archivocentral.expediente;

import jakarta.persistence.*;
import lombok.*;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.CreateExpedienteRequest;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.UpdateExpedienteRequest;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Entidad JPA que mapea la tabla archivo.expedientes.
 *
 * <p>
 * El campo {@code id} es el número del expediente físico (no autoincremental).
 * Se ingresa manualmente al registrar un expediente existente del archivo.
 * </p>
 *
 * <p>
 * Lombok: {@code @Getter} y {@code @NoArgsConstructor} únicamente.
 * No se exponen setters — la mutación solo ocurre a través del factory
 * method {@link #crear} y {@link #actualizar}.
 * </p>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = { "asunto", "observaciones" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "expedientes", schema = "archivo")
public class ExpedienteEntity {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 50)
    private TipoDocumento tipoDocumento;

    @Column(name = "nro_documento", nullable = false, length = 50)
    private String nroDocumento;

    @Column(name = "nombre_solicitante", nullable = false, length = 200)
    private String nombreSolicitante;

    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Column(name = "asunto", nullable = false, columnDefinition = "TEXT")
    private String asunto;

    @Column(name = "dirigido_a", nullable = false, length = 200)
    private String dirigidoA;

    @Column(name = "folios", nullable = false)
    private Integer folios;

    @Column(name = "archivado_con", length = 300)
    private String archivadoCon;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    // -------------------------------------------------------------------------
    // Factory Method
    // -------------------------------------------------------------------------

    /**
     * Crea un nuevo registro de expediente desde el DTO de creación.
     * El id (número de expediente) viene del request — no se genera
     * automáticamente.
     */
    public static ExpedienteEntity crear(CreateExpedienteRequest request) {
        var entity = new ExpedienteEntity();
        entity.id = request.id();
        entity.anio = request.anio();
        entity.fechaRegistro = request.fechaRegistro();
        entity.tipoDocumento = request.tipoDocumento();
        entity.nroDocumento = request.nroDocumento().trim().toUpperCase();
        entity.nombreSolicitante = request.nombreSolicitante().trim().toUpperCase();
        entity.dni = request.dni().trim();
        entity.asunto = request.asunto().trim();
        entity.dirigidoA = request.dirigidoA().trim().toUpperCase();
        entity.folios = request.folios();
        entity.archivadoCon = request.archivadoCon() != null ? request.archivadoCon().trim() : null;
        entity.observaciones = request.observaciones() != null ? request.observaciones().trim() : null;
        return entity;
    }

    // -------------------------------------------------------------------------
    // Método de actualización (corregir errores de registro)
    // -------------------------------------------------------------------------

    /**
     * Aplica las correcciones del DTO de actualización sobre este expediente.
     * No se puede cambiar el id (número de expediente) ni el año.
     */
    public void actualizar(UpdateExpedienteRequest request) {
        this.fechaRegistro = request.fechaRegistro();
        this.tipoDocumento = request.tipoDocumento();
        this.nroDocumento = request.nroDocumento().trim().toUpperCase();
        this.nombreSolicitante = request.nombreSolicitante().trim().toUpperCase();
        this.dni = request.dni().trim();
        this.asunto = request.asunto().trim();
        this.dirigidoA = request.dirigidoA().trim().toUpperCase();
        this.folios = request.folios();
        this.archivadoCon = request.archivadoCon() != null ? request.archivadoCon().trim() : null;
        this.observaciones = request.observaciones() != null ? request.observaciones().trim() : null;
    }

    // -------------------------------------------------------------------------
    // Lifecycle Hooks - Auditoría
    // -------------------------------------------------------------------------

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
