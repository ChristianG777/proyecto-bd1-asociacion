CREATE OR REPLACE FUNCTION fn_bitacora()
RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE
    datos_old JSONB;
    datos_new JSONB;
BEGIN
    IF TG_OP = 'INSERT' THEN
        datos_new := to_jsonb(NEW);

    ELSIF TG_OP = 'UPDATE' THEN
        datos_old := to_jsonb(OLD);
        datos_new := to_jsonb(NEW);

    ELSIF TG_OP = 'DELETE' THEN
        datos_old := to_jsonb(OLD);
    END IF;

    INSERT INTO Bitacora(tabla, accion, usuario_bd, datos_antes, datos_despues)
    VALUES (TG_TABLE_NAME, TG_OP, current_user, datos_old, datos_new);

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$$;