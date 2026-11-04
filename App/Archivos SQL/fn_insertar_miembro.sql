CREATE OR REPLACE FUNCTION fn_insertar_miembro(
    pid VARCHAR,
    pnombres VARCHAR,
    papellidos VARCHAR,
    pcedula VARCHAR,
    ptelefono VARCHAR,
    pdireccion VARCHAR,
    pfecha_ingreso DATE,
    pestado VARCHAR
)
RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO Miembro(idMiembro, nombres, apellidos, cedula, telefono, direccion, fechaIngreso, estado)
    VALUES (pid, pnombres, papellidos, pcedula, ptelefono, pdireccion, pfecha_ingreso, pestado);

    RETURN TRUE;
EXCEPTION
    WHEN others THEN
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;