package org.asociacion;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ExcelImportDialog extends JDialog {

    private JButton btnSeleccionar;
    private JLabel lblEstado;

    public ExcelImportDialog(Frame parent) {
        super(parent, "Importar datos desde Excel", true);
        initUI();
    }

    private void initUI() {
        setSize(400, 180);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        lblEstado = new JLabel("Seleccione un archivo Excel (.xlsx)");
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

        btnSeleccionar = new JButton("Seleccionar archivo");

        btnSeleccionar.addActionListener(e -> seleccionarArchivo());

        add(lblEstado, BorderLayout.CENTER);
        add(btnSeleccionar, BorderLayout.SOUTH);
    }

    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            lblEstado.setText("Importando datos...");

            btnSeleccionar.setEnabled(false);

            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    ExcelImporter.importExcel(archivo);
                    return null;
                }

                @Override
                protected void done() {
                    btnSeleccionar.setEnabled(true);
                    try {
                        get();
                        lblEstado.setText("Importación completada");
                        JOptionPane.showMessageDialog(
                                ExcelImportDialog.this,
                                "Importación realizada correctamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } catch (Exception ex) {
                        lblEstado.setText("Error en la importación");
                        JOptionPane.showMessageDialog(
                                ExcelImportDialog.this,
                                "Error durante la importación:\n" + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }.execute();
        }
    }
}