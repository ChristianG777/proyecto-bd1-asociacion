create trigger tg_bitacora_miembro
after insert or update or delete on Miembro
for each row
execute function fn_bitacora();

create trigger tg_bitacora_proveedor
after insert or update or delete on Proveedor
for each row
execute function fn_bitacora();

create trigger tg_bitacora_insumo
after insert or update or delete on Insumo
for each row
execute function fn_bitacora();

create trigger tg_bitacora_maquinaria
after insert or update or delete on Maquinaria
for each row
execute function fn_bitacora();

create trigger tg_bitacora_reservamaquinaria
after insert or update or delete on ReservaMaquinaria
for each row
execute function fn_bitacora();

create trigger tg_bitacora_capacitacion
after insert or update or delete on Capacitacion
for each row
execute function fn_bitacora();

create trigger tg_bitacora_InscripcionCapacitacion
after insert or update or delete on InscripcionCapacitacion
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Membresia
after insert or update or delete on Membresia
for each row
execute function fn_bitacora();

create trigger tg_bitacora_PagoMembresia
after insert or update or delete on PagoMembresia
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Cultivo
after insert or update or delete on Cultivo
for each row
execute function fn_bitacora();

create trigger tg_bitacora_MiembroCultivo
after insert or update or delete on MiembroCultivo
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Producto
after insert or update or delete on Producto
for each row
execute function fn_bitacora();

create trigger tg_bitacora_EntidadExterna
after insert or update or delete on EntidadExterna
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Mandato
after insert or update or delete on Mandato
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Contrato
after insert or update or delete on Contrato
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Convenio
after insert or update or delete on Convenio
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Venta
after insert or update or delete on Venta
for each row
execute function fn_bitacora();

create trigger tg_bitacora_DetalleVenta
after insert or update or delete on DetalleVenta
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Gasto
after insert or update or delete on Gasto
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Cargo
after insert or update or delete on Cargo
for each row
execute function fn_bitacora();

create trigger tg_bitacora_DetallesMembresia
after insert or update or delete on DetallesMembresia
for each row
execute function fn_bitacora();

create trigger tg_bitacora_MembresiaInsumo
after insert or update or delete on MembresiaInsumo
for each row
execute function fn_bitacora();

create trigger tg_bitacora_Pago
after insert or update or delete on Pago
for each row
execute function fn_bitacora();