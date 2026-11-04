    package org.asociacion;

    import java.time.LocalDate;

    public class PagoMembresia {
        public String idPago;
        public String idMembresia;
        public LocalDate fechaPago;
        public double monto;
        public String metodo;

        public PagoMembresia(String idPago, String idMembresia, LocalDate fechaPago, double monto, String metodo) {
            this.idPago = idPago;
            this.idMembresia = idMembresia;
            this.fechaPago = fechaPago;
            this.monto = monto;
            this.metodo = metodo;
        }
    }