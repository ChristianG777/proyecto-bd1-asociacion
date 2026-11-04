CREATE OR REPLACE FUNCTION fn_contratos_por_monto(
    pmonto DOUBLE PRECISION,
    pcomparacion VARCHAR
)
RETURNS TABLE(
    nombreEntidad VARCHAR,
    fechaInicio DATE,
    fechaFin DATE,
    monto DOUBLE PRECISION
)
AS $$
BEGIN
    IF pcomparacion = 'MAYOR_O_IGUAL' THEN
        RETURN QUERY
        SELECT ee.nombre, c.fechaInicio, c.fechaFin, c.monto
        FROM Contrato c
        JOIN EntidadExterna ee ON c.idEntidadExterna = ee.idEntidadExterna
        WHERE c.monto >= pmonto;

    ELSIF pcomparacion = 'MENOR_O_IGUAL' THEN
        RETURN QUERY
        SELECT ee.nombre, c.fechaInicio, c.fechaFin, c.monto
        FROM Contrato c
        JOIN EntidadExterna ee ON c.idEntidadExterna = ee.idEntidadExterna
        WHERE c.monto <= pmonto;

    ELSIF pcomparacion = 'EXACTO' THEN
        RETURN QUERY
        SELECT ee.nombre, c.fechaInicio, c.fechaFin, c.monto
        FROM Contrato c
        JOIN EntidadExterna ee ON c.idEntidadExterna = ee.idEntidadExterna
        WHERE c.monto = pmonto;
    END IF;
END;
$$ LANGUAGE plpgsql;