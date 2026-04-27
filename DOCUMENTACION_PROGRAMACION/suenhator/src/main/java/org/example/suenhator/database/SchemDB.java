package org.example.suenhator.database;

public interface SchemDB {
    //firmas de metodos abstractos o constantes

    // =========================
// TABLA CLIENTE
// =========================
    String TAB_CLIENTE = "cliente";
    String COL_CLIENTE_ID = "id_cliente";
    String COL_CLIENTE_NOMBRE = "nombre";
    String COL_CLIENTE_APELLIDOS = "apellidos";
    String COL_CLIENTE_DNI = "dni";
    String COL_CLIENTE_TELEFONO = "telefono";
    String COL_CLIENTE_EMAIL = "email";
    String COL_CLIENTE_FECHA_ALTA = "fecha_alta";
    String COL_CLIENTE_FECHA_NAC = "fecha_nac";


    // =========================
// TABLA INVITADO
// =========================
    String TAB_INVITADO = "invitado";
    String COL_INVITADO_ID = "id_invitado";
    String COL_INVITADO_NOMBRE = "nombre";
    String COL_INVITADO_APELLIDOS = "apellidos";
    String COL_INVITADO_DNI = "dni";
    String COL_INVITADO_TELEFONO = "telefono";
    String COL_INVITADO_EMAIL = "email";
    String COL_INVITADO_FECHA_NAC = "fecha_nac";


    // =========================
// TABLA PACK
// =========================
    String TAB_PACK = "pack";
    String COL_PACK_ID = "id_pack";
    String COL_PACK_NOMBRE = "nombre";
    String COL_PACK_DESCRIPCION = "descripcion";
    String COL_PACK_TIPO_PACK = "tipo_pack";
    String COL_PACK_DURACION = "duracion";
    String COL_PACK_PRECIO = "precio";
    String COL_PACK_ES_PREMIUM = "es_premium";
    String COL_PACK_AFORO = "aforo";
    String COL_PACK_ES_18 = "es_18";


    // =========================
// TABLA SALA
// =========================
    String TAB_SALA = "sala";
    String COL_SALA_ID = "id_sala";
    String COL_SALA_NOMBRE = "nombre";
    String COL_SALA_CAPACIDAD = "capacidad";


    // =========================
// TABLA SUPERVISOR
// =========================
    String TAB_SUPERVISOR = "supervisor";
    String COL_SUPERVISOR_ID = "id_supervisor";
    String COL_SUPERVISOR_NOMBRE = "nombre";
    String COL_SUPERVISOR_APELLIDOS = "apellidos";
    String COL_SUPERVISOR_DNI = "dni";
    String COL_SUPERVISOR_TELEFONO = "telefono";
    String COL_SUPERVISOR_EMAIL = "email";


    // =========================
// TABLA COMPRA
// =========================
    String TAB_COMPRA = "compra";
    String COL_COMPRA_ID = "id_compra";
    String COL_COMPRA_FECHA = "fecha";
    String COL_COMPRA_TOTAL = "total";
    String COL_COMPRA_ESTADO = "estado";
    String COL_COMPRA_ID_CLIENTE = "id_cliente";


    // =========================
// TABLA PAGO
// =========================
    String TAB_PAGO = "pago";
    String COL_PAGO_ID = "id_pago";
    String COL_PAGO_FECHA_PAGO = "fecha_pago";
    String COL_PAGO_IMPORTE = "importe";
    String COL_PAGO_METODO = "metodo";
    String COL_PAGO_ID_COMPRA = "id_compra";


    // =========================
// TABLA RESERVA
// =========================
    String TAB_RESERVA = "reserva";
    String COL_RESERVA_ID = "id_reserva";
    String COL_RESERVA_FECHA = "fecha";
    String COL_RESERVA_HORA = "hora";
    String COL_RESERVA_ESTADO = "estado";
    String COL_RESERVA_ES_CONFIRMADO = "es_confirmado";
    String COL_RESERVA_ID_CLIENTE = "id_cliente";
    String COL_RESERVA_ID_SALA = "id_sala";
    String COL_RESERVA_ID_PACK = "id_pack";
    String COL_RESERVA_ID_SUPERVISOR = "id_supervisor";


    // =========================
// TABLA LINEA_COMPRA
// =========================
    String TAB_LINEA_COMPRA = "linea_compra";
    String COL_LINEA_COMPRA_CANTIDAD = "cantidad";
    String COL_LINEA_COMPRA_PRECIO_UNITARIO = "precio_unitario";
    String COL_LINEA_COMPRA_SUBTOTAL = "subtotal";
    String COL_LINEA_COMPRA_ID_COMPRA = "id_compra";
    String COL_LINEA_COMPRA_ID_PACK = "id_pack";


    // =========================
// TABLA RESERVA_INVITADO
// =========================
    String TAB_RESERVA_INVITADO = "reserva_invitado";
    String COL_RESERVA_INVITADO_ES_CONFIRMADO = "es_confirmado";
    String COL_RESERVA_INVITADO_ID_RESERVA = "id_reserva";
    String COL_RESERVA_INVITADO_ID_INVITADO = "id_invitado";


    // =========================
// TABLA PERSONALIZACION
// =========================
    String TAB_PERSONALIZACION = "personalizacion";
    String COL_PERSONALIZACION_ID = "id_personalizacion";
    String COL_PERSONALIZACION_VIDEO_REF = "video_ref";
    String COL_PERSONALIZACION_DESCRIPCION = "descripcion";
    String COL_PERSONALIZACION_FECHA_SOLICITUD = "fecha_solicitud";
    String COL_PERSONALIZACION_FECHA_APROBACION = "fecha_aprobacion";
    String COL_PERSONALIZACION_ESTADO = "estado";
    String COL_PERSONALIZACION_ID_RESERVA = "id_reserva";
}