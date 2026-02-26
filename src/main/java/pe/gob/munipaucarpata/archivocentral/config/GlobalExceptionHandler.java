package pe.gob.munipaucarpata.archivocentral.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones.
 *
 * Usa RFC 7807 Problem Details (ProblemDetail de Spring 6+) para
 * respuestas de error consistentes en todos los endpoints.
 *
 * Formatos de error:
 *   400 Bad Request  → errores de validación (campo → mensaje)
 *   404 Not Found    → expediente no encontrado
 *   409 Conflict     → nro_expediente duplicado
 *   500 Server Error → errores inesperados
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Errores de validación @Valid — devuelve mapa campo → mensaje.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Valor inválido",
                        (msg1, msg2) -> msg1  // Si hay 2 errores en el mismo campo, toma el primero
                ));

        var problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Error de validación en los datos enviados");
        problem.setProperty("errores", errores);
        return problem;
    }

    /**
     * Expediente no encontrado (ID o búsqueda sin resultados).
     */
    @ExceptionHandler(NoSuchElementException.class)
    ProblemDetail handleNotFound(NoSuchElementException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Reglas de negocio violadas (ej: nro_expediente duplicado).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleConflict(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Catch-all: errores internos no esperados.
     */
    @ExceptionHandler(Exception.class)
    ProblemDetail handleGeneral(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor. Contacte al administrador.");
    }
}
