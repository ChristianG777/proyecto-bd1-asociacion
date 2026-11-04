CREATE OR REPLACE FUNCTION fn_buscar_miembro_por_cedula(pcedula VARCHAR)
RETURNS TABLE(
    idMiembro VARCHAR,
    nombres VARCHAR,
    apellidos VARCHAR,
    cedula VARCHAR,
    telefono VARCHAR,
    direccion VARCHAR,
    fechaIngreso DATE,
    estado VARCHAR
)
AS $$
BEGIN
    RETURN QUERY
    SELECT m.idMiembro, m.nombres, m.apellidos, m.cedula,
           m.telefono, m.direccion, m.fechaIngreso, m.estado
    FROM Miembro m
    WHERE m.cedula = pcedula;
END;
$$ LANGUAGE plpgsql;