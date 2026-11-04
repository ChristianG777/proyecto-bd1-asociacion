CREATE OR REPLACE FUNCTION fn_registrar_pago_completo(
    pcedula VARCHAR,
    ptipo VARCHAR,
    pcosto DOUBLE PRECISION,
    pmetodo VARCHAR
)
RETURNS BOOLEAN AS $$
DECLARE
    vMiembroId VARCHAR;
    vIdMembresia VARCHAR;
    vIdPago VARCHAR;
BEGIN
    SELECT idMiembro INTO vMiembroId
    FROM Miembro
    WHERE cedula = pcedula;

    IF vMiembroId IS NULL THEN
        RETURN FALSE;
    END IF;

    vIdMembresia := 'MBR-' || substring(md5(random()::text), 1, 4);
    vIdPago := 'PAY-' || substring(md5(random()::text), 1, 4);

    INSERT INTO Membresia
    VALUES (vIdMembresia, vMiembroId, ptipo, pcosto,
            CURRENT_DATE, CURRENT_DATE + INTERVAL '1 year', 'ACTIVA');

    INSERT INTO PagoMembresia
    VALUES (vIdPago, vIdMembresia, CURRENT_DATE, pcosto, pmetodo);

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;