CREATE OR REPLACE FUNCTION fn_registrar_pago_membresia(
    pid_pago VARCHAR,
    pid_membresia VARCHAR,
    pfecha_pago DATE,
    pmonto DECIMAL,
    pmetodo VARCHAR
)
RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO PagoMembresia(idPago, idMembresia, fechaPago, monto, metodo)
    VALUES (pid_pago, pid_membresia, pfecha_pago, pmonto, pmetodo);

    RETURN TRUE;
EXCEPTION
    WHEN others THEN
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;