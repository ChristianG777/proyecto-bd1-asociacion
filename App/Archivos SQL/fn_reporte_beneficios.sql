CREATE OR REPLACE FUNCTION fn_reporte_beneficios(
    pdesde DATE,
    phasta DATE
)
RETURNS TABLE(
    idMiembro VARCHAR,
    nombres VARCHAR,
    apellidos VARCHAR,
    fecha DATE,
    beneficio VARCHAR
)
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM (
        SELECT m.idMiembro, m.nombres, m.apellidos,
               ic.fechaInscripcion,
               CAST('CAPACITACION' AS VARCHAR)
        FROM InscripcionCapacitacion ic
        JOIN Miembro m ON ic.idMiembro = m.idMiembro
        WHERE ic.fechaInscripcion BETWEEN pdesde AND phasta

        UNION ALL

        SELECT mm.idMiembro, mm.nombres, mm.apellidos,
               mi.fechaEntrega,
               CAST('INSUMO' AS VARCHAR)
        FROM MembresiaInsumo mi
        JOIN Membresia mb ON mi.idMembresia = mb.idMembresia
        JOIN Miembro mm ON mb.idMiembro = mm.idMiembro
        WHERE mi.fechaEntrega BETWEEN pdesde AND phasta

        UNION ALL

        SELECT m2.idMiembro, m2.nombres, m2.apellidos,
               mb2.fechaInicio,
               CAST('MEMBRESIA' AS VARCHAR)
        FROM Membresia mb2
        JOIN Miembro m2 ON mb2.idMiembro = m2.idMiembro
        WHERE mb2.fechaInicio BETWEEN pdesde AND phasta

        UNION ALL

        SELECT m3.idMiembro, m3.nombres, m3.apellidos,
               rm.fechaInicio,
               CAST('MAQUINARIA' AS VARCHAR)
        FROM ReservaMaquinaria rm
        JOIN Miembro m3 ON rm.idMiembro = m3.idMiembro
        WHERE rm.fechaInicio BETWEEN pdesde AND phasta);
END;
$$ LANGUAGE plpgsql;