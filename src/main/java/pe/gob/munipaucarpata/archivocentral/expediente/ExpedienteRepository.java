package pe.gob.munipaucarpata.archivocentral.expediente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio Spring Data JPA para expedientes.
 *
 * El ID es el número del expediente (Integer, no autoincremental).
 *
 * Métodos de búsqueda:
 * - Por año (listar todos los expedientes de un año)
 * - Por DNI (exacto — 8 dígitos)
 * - Por nombre del solicitante (ILIKE — insensible a mayúsculas)
 * - Búsqueda combinada flexible
 */
public interface ExpedienteRepository extends JpaRepository<ExpedienteEntity, Integer> {

        /**
         * Lista todos los expedientes de un año específico.
         */
        Page<ExpedienteEntity> findByAnio(Integer anio, Pageable pageable);

        /**
         * Busca todos los expedientes asociados a un DNI exacto.
         */
        Page<ExpedienteEntity> findByDni(String dni, Pageable pageable);

        /**
         * Busca por nombre del solicitante (búsqueda parcial, insensible a mayúsculas).
         */
        Page<ExpedienteEntity> findByNombreSolicitanteContainingIgnoreCase(
                        String nombre, Pageable pageable);

        /**
         * Búsqueda combinada flexible (anio, numero, dni, nombre simultáneo).
         * Cualquier parámetro null actúa como "sin filtro".
         */
        @Query("""
                        SELECT e FROM ExpedienteEntity e
                        WHERE (:anio IS NULL OR e.anio = :anio)
                          AND (:numero IS NULL OR e.id = :numero)
                          AND (:dni IS NULL OR e.dni = :dni)
                          AND (:nombre IS NULL OR UPPER(e.nombreSolicitante) LIKE UPPER(CONCAT('%', :nombre, '%')))
                        ORDER BY e.anio DESC, e.id ASC
                        """)
        Page<ExpedienteEntity> buscarCombinado(
                        @Param("anio") Integer anio,
                        @Param("numero") Integer numero,
                        @Param("dni") String dni,
                        @Param("nombre") String nombre,
                        Pageable pageable);
}
