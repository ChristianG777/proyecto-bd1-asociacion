package org.asociacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class ReporteBeneficiosDialog extends JDialog {

    private DefaultTableModel model;

    public ReporteBeneficiosDialog(JFrame parent) {
        super(parent, "Reporte beneficios", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);

        JPanel top = new JPanel();

        JTextField tfDesde = new JTextField(LocalDate.now().minusMonths(1).toString(), 10);
        JTextField tfHasta = new JTextField(LocalDate.now().toString(), 10);

        JComboBox<String> cb = new JComboBox<>(new String[]{"TODOS","CAPACITACION","INSUMO","MEMBRESIA","MAQUINARIA"});

        JButton btn = new JButton("Generar");

        top.add(new JLabel("Desde:"));
        top.add(tfDesde);
        top.add(new JLabel("Hasta:"));
        top.add(tfHasta);
        top.add(new JLabel("Beneficio:"));
        top.add(cb);
        top.add(btn);

        model = new DefaultTableModel(new String[]{"ID", "Nombres", "Apellidos", "Fecha", "Beneficio"}, 0);
        JTable table = new JTable(model);

        JButton btnExportPdf = new JButton("Exportar PDF");

        btn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                List<String[]> rows = DB.reporte(LocalDate.parse(tfDesde.getText()), LocalDate.parse(tfHasta.getText()), cb.getSelectedItem().toString());
                for (String[] r : rows) model.addRow(r);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error generando reporte. " + ex.getMessage());
            }
        });

        btnExportPdf.addActionListener(e -> {
            if (model.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "No hay datos para exportar."); return; }
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pdf")) f = new File(f.getAbsolutePath() + ".pdf");
                try {
                    exportToPdf(model, f);
                    JOptionPane.showMessageDialog(this, "PDF exportado: " + f.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error exportando PDF: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnExportPdf, BorderLayout.SOUTH);
    }

    private void exportToPdf(DefaultTableModel model, File output) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);

        PDPageContentStream cs = new PDPageContentStream(doc, page);
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);

        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float xStart = margin;
        float leading = 14f;

        cs.beginText();
        cs.newLineAtOffset(xStart, yStart);
        cs.showText("Reporte de Beneficios");
        cs.endText();

        float y = yStart - leading * 2;
        cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
        cs.beginText();
        cs.newLineAtOffset(xStart, y);
        int cols = model.getColumnCount();
        for (int j = 0; j < cols; j++) {
            cs.showText(model.getColumnName(j) + (j < cols-1 ? " | " : ""));
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
                cs.setFont(PDType1Font.HELVETICA, 10);
                y = page.getMediaBox().getHeight() - margin;
            }
            cs.beginText();
            cs.newLineAtOffset(xStart, y);
            for (int j = 0; j < cols; j++) {
                Object val = model.getValueAt(i, j);
                cs.showText((val == null ? "" : val.toString()) + (j < cols-1 ? " | " : ""));
            }
            cs.endText();
            y -= leading;
        }

        cs.close();
        doc.save(output);
        doc.close();
    }
}