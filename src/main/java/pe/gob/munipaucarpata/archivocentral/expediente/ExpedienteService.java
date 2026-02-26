package pe.gob.munipaucarpata.archivocentral.expediente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.CreateExpedienteRequest;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.ExpedienteResponse;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.UpdateExpedienteRequest;

import java.util.NoSuchElementException;

/**
 * Lógica de negocio para el registro y consulta de expedientes.
 *
 * Reglas:
 * - No se eliminan expedientes (solo registro y consulta/corrección)
 * - El id (número de expediente) lo ingresa el usuario, no es autoincremental
 * - No pueden existir dos expedientes con el mismo id
 */
@Service
@Transactional(readOnly = true)
public class ExpedienteService {

    private final ExpedienteRepository repository;

    ExpedienteService(ExpedienteRepository repository) {
        this.repository = repository;
    }

    // -------------------------------------------------------------------------
    // REGISTRO
    // -------------------------------------------------------------------------

    /**
     * Registra un expediente físico existente.
     * Valida que el id (número de expediente) no esté ya registrado.
     */
    @Transactional
    public ExpedienteResponse registrar(CreateExpedienteRequest request) {
        if (repository.existsById(request.id())) {
            throw new IllegalArgumentException(
                    "Ya existe un expediente registrado con el N° %d".formatted(request.id()));
        }

        var entity = ExpedienteEntity.crear(request);
        var saved = repository.save(entity);
        return ExpedienteResponse.from(saved);
    }

    // -------------------------------------------------------------------------
    // CONSULTA
    // -------------------------------------------------------------------------

    /**
     * Lista todos los expedientes paginados.
     */
    public Page<ExpedienteResponse> listar(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ExpedienteResponse::from);
    }

    /**
     * Obtiene un expediente por su número (id).
     */
    public ExpedienteResponse obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(ExpedienteResponse::from)
                .orElseThrow(() -> new NoSuchElementException(
                        "Expediente no encontrado con N° " + id));
    }

    /**
     * Lista todos los expedientes de un año específico.
     */
    public Page<ExpedienteResponse> listarPorAnio(Integer anio, Pageable pageable) {
        return repository.findByAnio(anio, pageable)
                .map(ExpedienteResponse::from);
    }

    /**
     * Búsqueda flexible por anio, numero, dni y/o nombre_solicitante.
     *
     * Los parámetros son opcionales y pueden combinarse.
     * Si ninguno se proporciona, devuelve todos (equivale a listar).
     */
    public Page<ExpedienteResponse> buscar(
            Integer anio, Integer numero, String dni, String nombre, Pageable pageable) {

        // Normalizar: blancos → null para que el JPQL los ignore
        String dniParam = isBlank(dni) ? null : dni.trim();
        String nombreParam = isBlank(nombre) ? null : nombre.trim().toUpperCase();

        return repository.buscarCombinado(anio, numero, dniParam, nombreParam, pageable)
                .map(ExpedienteResponse::from);
    }

    // -------------------------------------------------------------------------
    // ACTUALIZACIÓN
    // -------------------------------------------------------------------------

    /**
     * Corrige los datos de un expediente ya registrado.
     * El id (número de expediente) y año no se pueden cambiar.
     */
    @Transactional
    public ExpedienteResponse actualizar(Integer id, UpdateExpedienteRequest request) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Expediente no encontrado con N° " + id));

        entity.actualizar(request);
        var saved = repository.save(entity);
        return ExpedienteResponse.from(saved);
    }

    // -------------------------------------------------------------------------
    // Utilidad privada
    // -------------------------------------------------------------------------

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
