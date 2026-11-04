package org.asociacion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DB {

    private static final String URL = "jdbc:postgresql://localhost:5432/proyecto_bd1";
    private static final String USER = "postgres";
    private static final String PASS = "6354";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static boolean insertarMiembro(Miembro m) {
        String sql = "SELECT fn_insertar_miembro(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.id);
            ps.setString(2, m.nombres);
            ps.setString(3, m.apellidos);
            ps.setString(4, m.cedula);
            ps.setString(5, m.telefono);
            ps.setString(6, m.direccion);
            ps.setDate(7, Date.valueOf(m.fechaIngreso));
            ps.setString(8, m.estado);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Miembro buscarMiembroPorCedula(String cedula) {
        String sql = "SELECT * FROM fn_buscar_miembro_por_cedula(?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Miembro(
                            rs.getString("idMiembro"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("cedula"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getDate("fechaIngreso") == null ? null : rs.getDate("fechaIngreso").toLocalDate(),
                            rs.getString("estado")
                    );
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String crearMembresia(String idMiembro, String tipo, double costo, LocalDate inicio, LocalDate fin) {
        String idM = IDGenerator.generarID("MBR");
        String sql = "SELECT fn_crear_membresia(?, ?, ?, ?, ?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idM);
            ps.setString(2, idMiembro);
            ps.setString(3, tipo);
            ps.setDouble(4, costo);
            ps.setDate(5, Date.valueOf(inicio));
            ps.setDate(6, Date.valueOf(fin));

            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getBoolean(1)) return idM;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean registrarPago(String idMembresia, double monto, String metodo) {
        String idPago = IDGenerator.generarID("PAY");
        String sql = "SELECT fn_registrar_pago_membresia(?, ?, ?, ?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idPago);
            ps.setString(2, idMembresia);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setDouble(4, monto);
            ps.setString(5, metodo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    public static boolean registrarPagoCompletoPorCedula(String cedula, String tipoM, double costo, String metodo) {
        String sql = "SELECT fn_registrar_pago_completo(?, ?, ?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, tipoM);
            ps.setDouble(3, costo);
            ps.setString(4, metodo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    public static List<String[]> reporte(LocalDate desde, LocalDate hasta, String beneficio) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM fn_reporte_beneficios(?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[]{
                            rs.getString("idMiembro"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getDate("fecha") == null ? "" : rs.getDate("fecha").toString(),
                            rs.getString("beneficio")
                    };
                    lista.add(row);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (!beneficio.equalsIgnoreCase("TODOS")) {
            List<String[]> filtrado = new ArrayList<>();
            for (String[] r : lista)
                if (r[4].equalsIgnoreCase(beneficio))
                    filtrado.add(r);
            return filtrado;
        }

        return lista;
    }

    public static List<String[]> obtenerContratosPorMonto(double monto, String comparacion) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM fn_contratos_por_monto(?, ?)";

        try (Connection c = get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, monto);
            ps.setString(2, comparacion);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                            rs.getString("nombreEntidad"),
                            rs.getDate("fechaInicio") == null ? "" : rs.getDate("fechaInicio").toString(),
                            rs.getDate("fechaFin") == null ? "" : rs.getDate("fechaFin").toString(),
                            String.format("%.2f", rs.getDouble("monto"))
                    });
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public static boolean insertarGenerico(String tabla, List<String> columnas, List<String> valores) {

        String sql = "SELECT fn_insertar_generico(?, ?, ?)";

        try (Connection c = get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tabla);
            ps.setArray(2, c.createArrayOf("text", columnas.toArray()));
            ps.setArray(3, c.createArrayOf("text", valores.toArray()));

            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getBoolean(1);

        } catch (SQLException e) {
            System.err.println("Error en tabla " + tabla);
            e.printStackTrace();
            return false;
        }
    }
}