package org.asociacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class ReporteContratosDialog extends JDialog {

    private DefaultTableModel model;
    private JTable tabla;
    private JTextField tfMonto;
    private JComboBox<String> cbComparacion;
    private final String[] COMPARACION_OPCIONES = new String[]{"Mayor o Igual", "Menor o Igual", "Exacto"};

    public ReporteContratosDialog(JFrame parent) {
        super(parent, "Reporte por Monto de Contrato", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        JPanel top = new JPanel();

        tfMonto = new JTextField("10000.00", 10);
        cbComparacion = new JComboBox<>(COMPARACION_OPCIONES);

        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generarReporte());

        JButton btnExportar = new JButton("Exportar a PDF");
        btnExportar.addActionListener(e -> exportarPDF());

        top.add(new JLabel("Monto:"));
        top.add(tfMonto);
        top.add(new JLabel("Comparación:"));
        top.add(cbComparacion);
        top.add(btnGenerar);
        top.add(btnExportar);

        model = new DefaultTableModel(new String[]{"Entidad Externa", "Fecha Inicio", "Fecha Fin", "Monto"}, 0);
        tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void generarReporte() {
        try {
            double monto = Double.parseDouble(tfMonto.getText().trim());
            String comparacion = (String) cbComparacion.getSelectedItem();
            String comparacionDB = "";

            switch (comparacion) {
                case "Mayor o Igual":
                    comparacionDB = "MAYOR_O_IGUAL";
                    break;
                case "Menor o Igual":
                    comparacionDB = "MENOR_O_IGUAL";
                    break;
                case "Exacto":
                    comparacionDB = "EXACTO";
                    break;
            }

            List<String[]> resultados = DB.obtenerContratosPorMonto(monto, comparacionDB);

            model.setRowCount(0);
            for (String[] fila : resultados) {
                model.addRow(fila);
            }

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron contratos con los criterios especificados.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un monto válido (ej: 10000.00).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPDF() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Primero debe generar el reporte.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("ReporteContratos.pdf"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            try {
                generarPDF(file);
                JOptionPane.showMessageDialog(this, "Reporte exportado a PDF con éxito en: " + file.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al exportar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generarPDF(File file) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);

        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float xStart = margin;
        float leading = 14f;
        float[] colWidths = {200, 100, 100, 80};
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
        cs.newLineAtOffset(xStart, yStart);
        cs.showText("Reporte por Monto de Contrato");
        cs.endText();

        String monto = tfMonto.getText().trim();
        String comparacion = (String) cbComparacion.getSelectedItem();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 10);
        yStart -= leading * 2;
        cs.newLineAtOffset(xStart, yStart);
        cs.showText("Criterios: Monto " + comparacion + " a $" + monto);
        cs.endText();


        float y = yStart - leading * 2;

        cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
        cs.beginText();
        cs.newLineAtOffset(xStart, y);
        int cols = model.getColumnCount();

        float currentX = 0;
        for (int j = 0; j < cols; j++) {
            cs.showText(model.getColumnName(j));
            cs.newLineAtOffset(colWidths[j], 0);
        }
        cs.endText();

        cs.setFont(PDType1Font.HELVETICA, 10);
        y -= leading;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (y < margin + leading) {
                cs.close();
                page = new PDPage(PDRectangle.LETTER);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);

                y = page.getMediaBox().getHeight() - margin;
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.beginText();
                cs.newLineAtOffset(xStart, y);
                for (int j = 0; j < cols; j++) {
                    cs.showText(model.getColumnName(j));
                    cs.newLineAtOffset(colWidths[j], 0);
                }
                cs.endText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                y -= leading;
            }

            cs.beginText();
            cs.newLineAtOffset(xStart, y);
            for (int j = 0; j < cols; j++) {
                String text = model.getValueAt(i, j).toString();
                cs.showText(text);
                cs.newLineAtOffset(colWidths[j], 0);
            }
            cs.endText();
            y -= leading;
        }

        cs.close();
        doc.save(file);
        doc.close();
    }
}