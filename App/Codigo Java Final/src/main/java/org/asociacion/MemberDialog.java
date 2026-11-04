package org.asociacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class MemberDialog extends JDialog {

    private static final String[] PROVINCIAS = new String[] {
            "Azuay","Bolívar","Cañar","Carchi","Chimborazo","Cotopaxi","El Oro","Esmeraldas",
            "Galápagos","Guayas","Imbabura","Loja","Los Ríos","Manabí","Morona Santiago","Napo",
            "Orellana","Pastaza","Pichincha","Santa Elena","Santo Domingo","Sucumbíos","Tungurahua","Zamora Chinchipe"
    };

    private static final Pattern TEN_DIGITS = Pattern.compile("^\\d{10}$");

    public MemberDialog(JFrame parent) {
        super(parent, "Registrar nuevo miembro", true);
        setSize(480, 360);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;

        p.add(new JLabel("Nombres:"), c);
        c.gridx = 1; JTextField tfNombres = new JTextField(20); p.add(tfNombres, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Apellidos:"), c);
        c.gridx = 1; JTextField tfApellidos = new JTextField(20); p.add(tfApellidos, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Cédula (10 dígitos):"), c);
        c.gridx = 1; JTextField tfCedula = new JTextField(20); p.add(tfCedula, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Teléfono (10 dígitos):"), c);
        c.gridx = 1; JTextField tfTelefono = new JTextField(20); p.add(tfTelefono, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Dirección (calle, ciudad):"), c);
        c.gridx = 1; JTextField tfDireccion = new JTextField(20); p.add(tfDireccion, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Provincia:"), c);
        c.gridx = 1; JComboBox<String> cbProv = new JComboBox<>(PROVINCIAS); p.add(cbProv, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Estado:"), c);
        c.gridx = 1; JComboBox<String> cbEstado = new JComboBox<>(new String[] {"ACTIVO","INACTIVO","SUSPENDIDO"}); p.add(cbEstado, c);

        JButton btnGuardar = new JButton("Guardar");
        c.gridx = 0; c.gridy++; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL; p.add(btnGuardar, c);

        btnGuardar.addActionListener(e -> {
            String nombres = tfNombres.getText().trim();
            String apellidos = tfApellidos.getText().trim();
            String cedulaStr = tfCedula.getText().trim();
            String telefonoStr = tfTelefono.getText().trim();
            String direccion = tfDireccion.getText().trim();
            String provincia = (String) cbProv.getSelectedItem();
            String estado = (String) cbEstado.getSelectedItem();

            if (nombres.isEmpty() || apellidos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombres y apellidos son obligatorios.");
                return;
            }

            if (!TEN_DIGITS.matcher(cedulaStr).matches()) {
                JOptionPane.showMessageDialog(this, "La cédula debe tener exactamente 10 dígitos numéricos.");
                return;
            }

            if (!TEN_DIGITS.matcher(telefonoStr).matches()) {
                JOptionPane.showMessageDialog(this, "El teléfono debe tener exactamente 10 dígitos numéricos.");
                return;
            }

            String cedula = cedulaStr;
            String telefono = telefonoStr;

            Miembro existente = DB.buscarMiembroPorCedula(cedula);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un miembro con esa cédula: " + existente.id);
                return;
            }

            String id = IDGenerator.generarID("MEM");
            String direccionCompleta = provincia + " - " + direccion;
            Miembro m = new Miembro(id, nombres, apellidos, cedula, telefono, direccionCompleta, LocalDate.now(), estado);

            boolean ok = DB.insertarMiembro(m);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Miembro registrado: " + id);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar miembro. Revisa la consola.");
            }
        });

        getContentPane().add(p);
    }
}