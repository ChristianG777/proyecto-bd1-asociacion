CREATE TABLE Miembro (
    idMiembro VARCHAR(40) PRIMARY KEY,
    nombres VARCHAR(200),
    apellidos VARCHAR(200),
    cedula VARCHAR(40) UNIQUE,
    telefono VARCHAR(40),
    direccion VARCHAR(300),
    fechaIngreso DATE,
    estado VARCHAR(40)
);

CREATE TABLE Proveedor (
    idProveedor VARCHAR(40) PRIMARY KEY,
    nombre VARCHAR(200),
    telefono VARCHAR(40),
    direccion VARCHAR(300),
    email VARCHAR(200),
    tipo VARCHAR(80)
);

CREATE TABLE Insumo (
    idInsumo VARCHAR(40) PRIMARY KEY,
    idProveedor VARCHAR(40),
    nombre VARCHAR(200),
    tipo VARCHAR(80),
    unidad VARCHAR(40),
    stock INTEGER,
    precio DOUBLE PRECISION,
    FOREIGN KEY (idProveedor) REFERENCES Proveedor(idProveedor)
);

CREATE TABLE Maquinaria (
    idMaquinaria VARCHAR(40) PRIMARY KEY,
    nombre VARCHAR(200),
    tipo VARCHAR(80),
    marca VARCHAR(80),
    modelo VARCHAR(80),
    descripcion TEXT,
    capacidad INTEGER,
    fechaAdquisicion DATE,
    estado VARCHAR(80)
);

CREATE TABLE ReservaMaquinaria (
    idReserva VARCHAR(40) PRIMARY KEY,
    idMiembro VARCHAR(40),
    idMaquinaria VARCHAR(40),
    fechaInicio DATE,
    fechaFin DATE,
    motivoUso VARCHAR(200),
    estado VARCHAR(80),
    observaciones TEXT,
    FOREIGN KEY (idMiembro) REFERENCES Miembro(idMiembro),
    FOREIGN KEY (idMaquinaria) REFERENCES Maquinaria(idMaquinaria)
);

CREATE TABLE Capacitacion (
    idCapacitacion VARCHAR(40) PRIMARY KEY,
    nombre VARCHAR(200),
    tema VARCHAR(200),
    categoria VARCHAR(80),
    fechaInicio DATE,
    fechaFin DATE,
    duracion DOUBLE PRECISION,
    modalidad VARCHAR(80),
    lugar VARCHAR(200),
    entidadOrganizadora VARCHAR(200),
    cupoMaximo INTEGER,
    descripcion TEXT
);

CREATE TABLE InscripcionCapacitacion (
    idInscripcion VARCHAR(40) PRIMARY KEY,
    idMiembro VARCHAR(40),
    idCapacitacion VARCHAR(40),
    fechaInscripcion DATE,
    estado VARCHAR(80),
    observaciones TEXT,
    FOREIGN KEY (idMiembro) REFERENCES Miembro(idMiembro),
    FOREIGN KEY (idCapacitacion) REFERENCES Capacitacion(idCapacitacion)
);

CREATE TABLE Membresia (
    idMembresia VARCHAR(40) PRIMARY KEY,
    idMiembro VARCHAR(40),
    tipo VARCHAR(80),
    costo DOUBLE PRECISION,
    fechaInicio DATE,
    fechaFin DATE,
    estado VARCHAR(80),
    FOREIGN KEY (idMiembro) REFERENCES Miembro(idMiembro)
);

CREATE TABLE PagoMembresia (
    idPago VARCHAR(40) PRIMARY KEY,
    idMembresia VARCHAR(40),
    fechaPago DATE,
    monto DOUBLE PRECISION,
    metodo VARCHAR(80),
    FOREIGN KEY (idMembresia) REFERENCES Membresia(idMembresia),
	CONSTRAINT c_monto_valido CHECK (monto > 0)
);

CREATE TABLE Cultivo (
    idCultivo VARCHAR(40) PRIMARY KEY,
    nombre VARCHAR(200),
    area VARCHAR(40),
    fechaSiembra DATE,
    fechaCosecha DATE
);

CREATE TABLE MiembroCultivo (
    idMiembro VARCHAR(40),
    idCultivo VARCHAR(40),
    PRIMARY KEY (idMiembro, idCultivo),
    FOREIGN KEY (idMiembro) REFERENCES Miembro(idMiembro),
    FOREIGN KEY (idCultivo) REFERENCES Cultivo(idCultivo)
);

CREATE TABLE Producto (
    idProducto VARCHAR(40) PRIMARY KEY,
    idCultivo VARCHAR(40),
    nombre VARCHAR(200),
    tipo VARCHAR(200),
    unidadMedida VARCHAR(80),
    precioReferencial DOUBLE PRECISION,
    FOREIGN KEY (idCultivo) REFERENCES Cultivo(idCultivo)
);

CREATE TABLE EntidadExterna (
    idEntidadExterna VARCHAR(40) PRIMARY KEY,
    nombre VARCHAR(200),
    tipo VARCHAR(80),
    direccion VARCHAR(300),
    telefono VARCHAR(40),
    correo VARCHAR(200)
);

CREATE TABLE Mandato (
    idMandato VARCHAR(40) PRIMARY KEY,
    cargo VARCHAR(200),
    fechaInicio DATE,
    fechaFin DATE,
    estado VARCHAR(80)
);

CREATE TABLE Contrato (
    idContrato VARCHAR(40) PRIMARY KEY,
    idEntidadExterna VARCHAR(40),
    idMandato VARCHAR(40),
    fechaFirma DATE,
    fechaInicio DATE,
    fechaFin DATE,
    tipo VARCHAR(80),
    descripcion TEXT,
    monto DOUBLE PRECISION,
    FOREIGN KEY (idEntidadExterna) REFERENCES EntidadExterna(idEntidadExterna),
    FOREIGN KEY (idMandato) REFERENCES Mandato(idMandato)
);

CREATE TABLE Convenio (
    idConvenio VARCHAR(40) PRIMARY KEY,
    idEntidadExterna VARCHAR(40),
    nombre VARCHAR(200),
    fechaInicio DATE,
    fechaFin DATE,
    descripcion TEXT,
    estado VARCHAR(80),
    FOREIGN KEY (idEntidadExterna) REFERENCES EntidadExterna(idEntidadExterna)
);

CREATE TABLE Venta (
    idVenta VARCHAR(40) PRIMARY KEY,
    idContrato VARCHAR(40),
    fecha DATE,
    total DOUBLE PRECISION,
    estado VARCHAR(80),
    observaciones TEXT,
    FOREIGN KEY (idContrato) REFERENCES Contrato(idContrato)
);

CREATE TABLE DetalleVenta (
    idProducto VARCHAR(40),
    idVenta VARCHAR(40),
    cantidad DOUBLE PRECISION,
    PRIMARY KEY (idProducto, idVenta),
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto),
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta)
);

CREATE TABLE Pago (
    idPago VARCHAR(40) PRIMARY KEY,
    idVenta VARCHAR(40),
    cuotas INTEGER,
    fecha DATE,
    monto DOUBLE PRECISION,
    metodo VARCHAR(80),
    estado VARCHAR(80),
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta)
);

CREATE TABLE Gasto (
    idGasto VARCHAR(40) PRIMARY KEY,
    idMandato VARCHAR(40),
    fecha DATE,
    tipo VARCHAR(80),
    descripcion TEXT,
    monto DOUBLE PRECISION,
    comprobante TEXT,
    FOREIGN KEY (idMandato) REFERENCES Mandato(idMandato)
);

CREATE TABLE Cargo (
    idCargo VARCHAR(40) PRIMARY KEY,
    idMiembro VARCHAR(40),
    idMandato VARCHAR(40),
    nombre VARCHAR(200),
    descripcion TEXT,
    duracion DOUBLE PRECISION,
    FOREIGN KEY (idMiembro) REFERENCES Miembro(idMiembro),
    FOREIGN KEY (idMandato) REFERENCES Mandato(idMandato)
);

CREATE TABLE DetallesMembresia (
    idMembresia VARCHAR(40),
    idMaquinaria VARCHAR(40),
    idCapacitacion VARCHAR(40),
    PRIMARY KEY (idMembresia, idMaquinaria, idCapacitacion),
    FOREIGN KEY (idMembresia) REFERENCES Membresia(idMembresia),
    FOREIGN KEY (idMaquinaria) REFERENCES Maquinaria(idMaquinaria),
    FOREIGN KEY (idCapacitacion) REFERENCES Capacitacion(idCapacitacion)
);

CREATE TABLE MembresiaInsumo (
    idMembresia VARCHAR(40),
    idInsumo VARCHAR(40),
    fechaEntrega DATE,
    cantidadEntregada DOUBLE PRECISION,
    PRIMARY KEY (idMembresia, idInsumo, fechaEntrega),
    FOREIGN KEY (idMembresia) REFERENCES Membresia(idMembresia),
    FOREIGN KEY (idInsumo) REFERENCES Insumo(idInsumo)
);

CREATE TABLE Bitacora(
	idBitacora SERIAL PRIMARY KEY,
	tabla VARCHAR(100),
	accion VARCHAR(20),
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	usuario_bd VARCHAR(100),
	datos_antes TEXT,
	datos_despues TEXT
);