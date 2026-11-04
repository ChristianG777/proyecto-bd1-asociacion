package org.asociacion;

import javax.swing.*;
import java.awt.*;

public class PagoDialog extends JDialog {

    public PagoDialog(JFrame parent) {
        super(parent, "Pagar Membresía", true);
        setSize(520, 300);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.WEST;

        p.add(new JLabel("Cédula del miembro:"), c);
        c.gridx = 1; JTextField tfCed = new JTextField(16); p.add(tfCed, c);
        c.gridx = 2; JButton btnBuscar = new JButton("Buscar"); p.add(btnBuscar, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Miembro:"), c);
        c.gridx = 1; c.gridwidth = 2; JLabel lblMiembro = new JLabel("-"); p.add(lblMiembro, c);

        c.gridwidth = 1; c.gridx = 0; c.gridy++; p.add(new JLabel("Tipo Membresía:"), c);
        c.gridx = 1; JComboBox<String> cbTipo = new JComboBox<>(new String[] {"BASIC","PREMIUM"}); p.add(cbTipo, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Costo (USD):"), c);
        c.gridx = 1; JTextField tfCosto = new JTextField(10); tfCosto.setEditable(false); p.add(tfCosto, c);

        c.gridx = 0; c.gridy++; p.add(new JLabel("Método de pago:"), c);
        c.gridx = 1; JComboBox<String> cbMetodo = new JComboBox<>(new String[] {"EFECTIVO","TRANSFERENCIA"}); p.add(cbMetodo, c);

        c.gridx = 0; c.gridy++; c.gridwidth = 3; JButton btnConfirm = new JButton("Confirmar pago"); p.add(btnConfirm, c);

        java.util.Map<String, Double> precios = new java.util.HashMap<>();
        precios.put("BASIC", 60.0);
        precios.put("PREMIUM", 120.0);
        tfCosto.setText(String.valueOf(precios.get("BASIC")));

        final Miembro[] miembroEncontrado = new Miembro[1];

        cbTipo.addActionListener(e -> tfCosto.setText(String.valueOf(precios.get((String)cbTipo.getSelectedItem()))));

        btnBuscar.addActionListener(e -> {
            String ced = tfCed.getText().trim();
            Miembro m = DB.buscarMiembroPorCedula(ced);
            if (m == null) {
                JOptionPane.showMessageDialog(this, "Miembro no encontrado.");
                lblMiembro.setText("-");
                miembroEncontrado[0] = null;
            } else {
                lblMiembro.setText(m.nombres + " " + m.apellidos + " (" + m.id + ")");
                miembroEncontrado[0] = m;
            }
        });

        btnConfirm.addActionListener(e -> {
            if (miembroEncontrado[0] == null) { JOptionPane.showMessageDialog(this, "Busque primero al miembro."); return; }
            String tipo = (String) cbTipo.getSelectedItem();
            double costo = Double.parseDouble(tfCosto.getText());
            String metodo = (String) cbMetodo.getSelectedItem();
            boolean ok = DB.registrarPagoCompletoPorCedula(miembroEncontrado[0].cedula, tipo, costo, metodo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Pago y membresía registrados correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar pago. Revisa consola.");
            }
        });

        getContentPane().add(p);
    }
}