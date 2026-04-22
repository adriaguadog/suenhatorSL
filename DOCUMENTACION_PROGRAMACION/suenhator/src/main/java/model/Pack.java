package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private boolean esPremium;
    private int aforo;
    private boolean es18;

    public Pack(String nombre, String descripcion, String tipoPack, int duracion,
                double precio, boolean esPremium, int aforo, boolean es18) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoPack = tipoPack;
        this.duracion = duracion;
        this.precio = precio;
        this.esPremium = esPremium;
        this.aforo = aforo;
        this.es18 = es18;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "idPack=" + idPack +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoPack='" + tipoPack + '\'' +
                ", duracion=" + duracion +
                ", precio=" + precio +
                ", esPremium=" + esPremium +
                ", aforo=" + aforo +
                ", es18=" + es18 +
                '}';
    }
}