CREATE OR REPLACE FUNCTION fn_insertar_generico(
    p_tabla TEXT,
    p_columnas TEXT[],
    p_valores TEXT[]
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    sql TEXT;
BEGIN
    IF array_length(p_columnas, 1) IS DISTINCT FROM array_length(p_valores, 1) THEN
        RAISE EXCEPTION 'Columnas y valores no coinciden';
    END IF;

    sql :=
        'INSERT INTO ' || quote_ident(p_tabla) || ' (' ||
        (
            SELECT string_agg(quote_ident(col), ',')
            FROM unnest(p_columnas) col
        ) ||
        ') VALUES (' ||
        (
            SELECT string_agg(
                CASE
                    WHEN val IS NULL OR val = '' THEN 'NULL'
                    ELSE quote_literal(val)
                END,
                ','
            )
            FROM unnest(p_valores) val
        ) ||
        ')';

    EXECUTE sql;
    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error insertando en % → %', p_tabla, SQLERRM;
END;
$$;