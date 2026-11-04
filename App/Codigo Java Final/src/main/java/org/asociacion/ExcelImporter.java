package org.asociacion;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelImporter {

    private static final Map<String, String> MAP_TABLAS = Map.ofEntries(
            Map.entry("miembro", "miembro"),
            Map.entry("proveedor", "proveedor"),
            Map.entry("insumo", "insumo"),
            Map.entry("maquinaria", "maquinaria"),
            Map.entry("reservamaquinaria", "reservamaquinaria"),
            Map.entry("capacitacion", "capacitacion"),
            Map.entry("inscripcioncapacitacion", "inscripcioncapacitacion"),
            Map.entry("membresia", "membresia"),
            Map.entry("pagomembresia", "pagomembresia"),
            Map.entry("cultivo", "cultivo"),
            Map.entry("miembrocultivo", "miembrocultivo"),
            Map.entry("producto", "producto"),
            Map.entry("entidadexterna", "entidadexterna"),
            Map.entry("mandato", "mandato"),
            Map.entry("contrato", "contrato"),
            Map.entry("convenio", "convenio"),
            Map.entry("venta", "venta"),
            Map.entry("detalleventa", "detalleventa"),
            Map.entry("pago", "pago"),
            Map.entry("gasto", "gasto"),
            Map.entry("cargo", "cargo"),
            Map.entry("detallesmembresia", "detallesmembresia"),
            Map.entry("membresiainsumo", "membresiainsumo")
    );

    public static void importExcel(File file) throws Exception {

        int total = 0;
        int errores = 0;

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(file))) {

            FormulaEvaluator evaluator =
                    wb.getCreationHelper().createFormulaEvaluator();

            for (Sheet sheet : wb) {

                String hoja = sheet.getSheetName().toLowerCase().replace(" ", "");
                String tabla = MAP_TABLAS.get(hoja);

                if (tabla == null) continue;

                Iterator<Row> it = sheet.iterator();
                if (!it.hasNext()) continue;

                Row header = it.next();
                List<String> columnas = new ArrayList<>();

                int lastCol = header.getLastCellNum();

                for (int i = 0; i < lastCol; i++) {
                    Cell c = header.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (c == null) break;

                    String col = c.getStringCellValue().trim().toLowerCase();
                    if (col.isEmpty()) break;

                    columnas.add(col);
                }

                while (it.hasNext()) {
                    Row row = it.next();
                    List<String> valores = new ArrayList<>();

                    boolean filaVacia = true;

                    for (int i = 0; i < columnas.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        String val = getCellValue(cell, evaluator);
                        if (val != null && !val.isBlank()) filaVacia = false;
                        valores.add(val);
                    }

                    if (filaVacia) continue;

                    boolean ok = DB.insertarGenerico(tabla, columnas, valores);
                    total++;

                    if (!ok) errores++;
                }
            }
        }

        if (errores > 0) {
            throw new Exception(
                    "Importación incompleta\n" +
                            "Filas procesadas: " + total + "\n" +
                            "Filas con error: " + errores
            );
        }
    }

    private static String getCellValue(Cell cell, FormulaEvaluator evaluator) {

        if (cell == null) return null;

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue()
                            .toLocalDate()
                            .toString();
                }
                double n = cell.getNumericCellValue();
                if (n == Math.floor(n)) {
                    return Long.toString((long) n);
                }
                return Double.toString(n);

            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());

            case FORMULA:
                CellValue cv = evaluator.evaluate(cell);
                if (cv == null) return null;

                switch (cv.getCellType()) {
                    case STRING:
                        return cv.getStringValue();
                    case NUMERIC:
                        double d = cv.getNumberValue();
                        if (d == Math.floor(d)) {
                            return Long.toString((long) d);
                        }
                        return Double.toString(d);
                    case BOOLEAN:
                        return Boolean.toString(cv.getBooleanValue());
                    default:
                        return null;
                }

            default:
                return null;
        }
    }
}