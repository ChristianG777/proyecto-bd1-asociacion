Reporte 1: total de producción por agricultor

CREATE OR REPLACE VIEW vw_produccion_por_agricultor AS
SELECT 
    m.idMiembro,
    m.nombres,
    m.apellidos,
    SUM(dv.cantidad) AS total_produccion
FROM Miembro m
JOIN MiembroCultivo mc ON mc.idMiembro = m.idMiembro
JOIN Cultivo c ON c.idCultivo = mc.idCultivo
JOIN Producto p ON p.idCultivo = c.idCultivo
JOIN DetalleVenta dv ON dv.idProducto = p.idProducto
JOIN Venta v ON v.idVenta = dv.idVenta
GROUP BY m.idMiembro, m.nombres, m.apellidos;


SELECT * FROM vw_produccion_por_agricultor;



Reporte 2: total de ventas (dinero) por agricultor


CREATE OR REPLACE VIEW vw_ventas_por_agricultor AS
SELECT 
    m.idMiembro,
    m.nombres,
    m.apellidos,
    SUM(dv.subtotal) AS total_ventas
FROM Miembro m
JOIN MiembroCultivo mc ON mc.idMiembro = m.idMiembro
JOIN Cultivo c ON c.idCultivo = mc.idCultivo
JOIN Producto p ON p.idCultivo = c.idCultivo
JOIN DetalleVenta dv ON dv.idProducto = p.idProducto
JOIN Venta v ON v.idVenta = dv.idVenta
GROUP BY m.idMiembro, m.nombres, m.apellidos;


SELECT * FROM vw_ventas_por_agricultor;