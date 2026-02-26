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
   * Busca por nombre del solicitante (parcial, insensible a mayúsculas).
   */
  Page<ExpedienteEntity> findByNombreSolicitanteContainingIgnoreCase(
      String nombre, Pageable pageable);

  /**
   * Búsqueda combinada flexible (anio, numero, dni, nombre simultáneo).
   * Cualquier parámetro null actúa como "sin filtro".
   *
   * Usa native query con CAST porque Hibernate no resuelve el tipo de
   * parámetros Integer null en JPQL con "IS NULL".
   */
  @Query(value = """
      SELECT * FROM archivo.expedientes e
      WHERE (CAST(:anio AS INTEGER) IS NULL OR e.anio = :anio)
        AND (CAST(:numero AS INTEGER) IS NULL OR e.id = :numero)
        AND (CAST(:dni AS VARCHAR) IS NULL OR e.dni = :dni)
        AND (CAST(:nombre AS VARCHAR) IS NULL OR UPPER(e.nombre_solicitante) LIKE UPPER(CONCAT('%', :nombre, '%')))
      ORDER BY e.anio DESC, e.id ASC
      """, countQuery = """
      SELECT COUNT(*) FROM archivo.expedientes e
      WHERE (CAST(:anio AS INTEGER) IS NULL OR e.anio = :anio)
        AND (CAST(:numero AS INTEGER) IS NULL OR e.id = :numero)
        AND (CAST(:dni AS VARCHAR) IS NULL OR e.dni = :dni)
        AND (CAST(:nombre AS VARCHAR) IS NULL OR UPPER(e.nombre_solicitante) LIKE UPPER(CONCAT('%', :nombre, '%')))
      """, nativeQuery = true)
  Page<ExpedienteEntity> buscarCombinado(
      @Param("anio") Integer anio,
      @Param("numero") Integer numero,
      @Param("dni") String dni,
      @Param("nombre") String nombre,
      Pageable pageable);
}
