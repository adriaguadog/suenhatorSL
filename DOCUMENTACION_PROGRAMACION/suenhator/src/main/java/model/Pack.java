package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Pack {

    private int idPack;
    private String nombre;
    private String descripcion;
    private String tipoPack;
    private int duracion;
    private double precio;
    private boolean premium;
    private int aforo;
    private boolean mas18;

    public Pack(String nombre, String descripcion, String tipoPack, int duracion,
                double precio, boolean Premium, int aforo, boolean mas18) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoPack = tipoPack;
        this.duracion = duracion;
        this.precio = precio;
        this.premium = Premium;
        this.aforo = aforo;
        this.mas18 = mas18;
    }

    @Override
    public String toString() {
        return nombre + " - " + String.format("%.2f", precio) + " €";
    }



}