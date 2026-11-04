CREATE OR REPLACE FUNCTION fn_crear_membresia(
    pid_membresia VARCHAR,
    pid_miembro VARCHAR,
    ptipo VARCHAR,
    pcosto DECIMAL,
    pfecha_inicio DATE,
    pfecha_fin DATE
)
RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO Membresia(idMembresia, idMiembro, tipo, costo, fechaInicio, fechaFin, estado)
    VALUES (pid_membresia, pid_miembro, ptipo, pcosto, pfecha_inicio, pfecha_fin, 'ACTIVA');

    RETURN TRUE;
EXCEPTION
    WHEN others THEN
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;