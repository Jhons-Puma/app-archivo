package pe.gob.munipaucarpata.archivocentral.expediente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.CreateExpedienteRequest;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.ExpedienteResponse;
import pe.gob.munipaucarpata.archivocentral.expediente.dto.UpdateExpedienteRequest;

/**
 * REST Controller — Archivo Central MDP.
 *
 * Endpoints:
 * POST /api/expedientes - Registrar expediente
 * GET /api/expedientes - Listar (paginado)
 * GET /api/expedientes/{id} - Obtener por N° de expediente
 * GET /api/expedientes/anio/{anio} - Listar por año
 * GET /api/expedientes/buscar?anio=&numero=&dni=&nombre= - Búsqueda flexible
 * PUT /api/expedientes/{id} - Corregir datos
 * GET /api/expedientes/tipos - Listar tipos de documento
 *
 * No hay DELETE.
 */
@RestController
@RequestMapping("/api/expedientes")
@Tag(name = "Expedientes", description = "Inventario de expedientes del Archivo Central")
public class ExpedienteController {

    private final ExpedienteService service;

    ExpedienteController(ExpedienteService service) {
        this.service = service;
    }

    // -------------------------------------------------------------------------
    // POST /api/expedientes
    // -------------------------------------------------------------------------

    @PostMapping
    @Operation(summary = "Registrar expediente", description = "Registra un expediente físico existente. El N° de expediente se ingresa manualmente (no es autoincremental).")
    ResponseEntity<ExpedienteResponse> registrar(
            @Valid @RequestBody CreateExpedienteRequest request) {
        var response = service.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------------------------------
    // GET /api/expedientes
    // -------------------------------------------------------------------------

    @GetMapping
    @Operation(summary = "Listar expedientes", description = "Devuelve todos los expedientes paginados.")
    Page<ExpedienteResponse> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listar(pageable);
    }

    // -------------------------------------------------------------------------
    // GET /api/expedientes/{id}
    // -------------------------------------------------------------------------

    @GetMapping("/{id}")
    @Operation(summary = "Obtener por N° de expediente", description = "Busca un expediente por su número.")
    ExpedienteResponse obtenerPorId(
            @Parameter(description = "Número del expediente") @PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    // -------------------------------------------------------------------------
    // GET /api/expedientes/anio/{anio}
    // -------------------------------------------------------------------------

    @GetMapping("/anio/{anio}")
    @Operation(summary = "Listar por año", description = "Devuelve todos los expedientes de un año específico.")
    Page<ExpedienteResponse> listarPorAnio(
            @Parameter(description = "Año del expediente (ej: 2000)") @PathVariable Integer anio,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listarPorAnio(anio, pageable);
    }

    // -------------------------------------------------------------------------
    // GET /api/expedientes/buscar?anio=&numero=&dni=&nombre=
    // -------------------------------------------------------------------------

    @GetMapping("/buscar")
    @Operation(summary = "Búsqueda flexible", description = "Busca expedientes por año, número, DNI y/o nombre del solicitante. "
            + "Los parámetros pueden combinarse o usarse individualmente.")
    Page<ExpedienteResponse> buscar(
            @Parameter(description = "Año del expediente") @RequestParam(required = false) Integer anio,

            @Parameter(description = "N° de expediente exacto") @RequestParam(required = false) Integer numero,

            @Parameter(description = "DNI del solicitante (8 dígitos exactos)") @RequestParam(required = false) String dni,

            @Parameter(description = "Nombre del solicitante (parcial, insensible a mayúsculas)") @RequestParam(required = false) String nombre,

            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.buscar(anio, numero, dni, nombre, pageable);
    }

    // -------------------------------------------------------------------------
    // PUT /api/expedientes/{id}
    // -------------------------------------------------------------------------

    @PutMapping("/{id}")
    @Operation(summary = "Corregir expediente", description = "Actualiza los datos de un expediente para corregir errores de registro. No se puede cambiar el N° de expediente ni el año.")
    ExpedienteResponse actualizar(
            @Parameter(description = "Número del expediente a corregir") @PathVariable Integer id,
            @Valid @RequestBody UpdateExpedienteRequest request) {
        return service.actualizar(id, request);
    }

    // -------------------------------------------------------------------------
    // GET /api/expedientes/tipos
    // -------------------------------------------------------------------------

    @GetMapping("/tipos")
    @Operation(summary = "Listar tipos de documento", description = "Devuelve todos los valores válidos para el campo tipoDocumento.")
    TipoDocumento[] listarTipos() {
        return TipoDocumento.values();
    }
}
