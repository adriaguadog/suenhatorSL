package controller;

import model.Cliente;
import model.Pack;
import model.Reserva;
import model.Sala;
import model.Supervisor;
import model.enums.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaController {


    public ReservaController() {
        if (listaReservas == null) {
            listaReservas = new ArrayList<>();
        }
        if (listaPacks == null) {
            listaPacks = new ArrayList<>();
        }
        if (listaSalas == null) {
            listaSalas = new ArrayList<>();
        }
        if (listaSupervisores == null) {
            listaSupervisores = new ArrayList<>();
        }
    }


    public ArrayList<Pack> listarPacks() {
        if (listaPacks.isEmpty()){
            System.out.println("no hay packs registrados");
        }else {
            listaPacks.forEach(System.out::println);
            return listaPacks;
        }
        return null;
    }

    public List<Sala> listarSalasDisponibles(LocalDate fecha, LocalTime hora) {
        if (listaSalas.isEmpty()) {
            System.out.println("No hay salas registradas");
        } else {
            List<Sala> listaSalasDisponibles = listaSalas.stream()
                    .filter(sala -> listaReservas.stream()
                            .filter(reserva -> reserva.getFecha().equals(fecha))
                            .filter(reserva -> reserva.getHora().equals(hora))
                            .filter(reserva -> reserva.getEstado() != EstadoReserva.CANCELADA)
                            .noneMatch(reserva -> reserva.getSala().equals(sala)))
                    .toList();

            if (listaSalasDisponibles.isEmpty()) {
                System.out.println("No hay ninguna sala disponible para esa fecha y hora");
            } else {
                listaSalasDisponibles.forEach(System.out::println);
                return listaSalasDisponibles;
            }
        }
        return null;
    }

    public List<Supervisor> listarSupervisores() {
        if (listaSupervisores.isEmpty()){
            System.out.println("no hay supervisores registrados");
        }else {
            listaSupervisores.forEach(System.out::println);
            return listaSupervisores;
        }
        return null;
    }

    public boolean consultarDetallePack(Pack pack) {
        //compruebo que no es nulo
        if (pack==null){
            System.out.println("pack no existente");
        } else if (listaPacks.stream().anyMatch(pack1 -> pack1.getNombre().equalsIgnoreCase(pack.getNombre()))) {
            //compruebo que exista en los packs disponibles
            System.out.println(pack);
            return true;
        }
        //si no coincide con ningun pack existente
        System.out.println("pack no encontrado");
        return false;
    }

    public Reserva crearReserva(Cliente cliente, Pack pack, Supervisor supervisor, LocalDate fecha, LocalTime hora) {
        if (cliente==null||pack==null||supervisor==null){
            System.out.println("no se pudo crear la reserva");
        } else {
            List<Sala> listaSalasDispo=listarSalasDisponibles(fecha,hora);
            if (listaSalasDispo==null) {
                System.out.println("no hay salas disponibles");
            }else {
                Sala sala=listaSalasDispo.stream().findFirst().orElse(null);
                Reserva reserva = new Reserva(cliente, sala, pack, supervisor, fecha, hora);
                System.out.println("reserva creada correctamente");
                listaReservas.add(reserva);
                return reserva;
            }

        }
        return null;
    }

    public List<Reserva> consultarReservasPorFecha(LocalDate fecha) {
        return listaReservas.stream().filter(reserva -> reserva.getFecha().equals(fecha)).toList();
    }

    public List<Reserva> consultarReservasPorCliente(Cliente cliente) {
        return listaReservas.stream().filter(reserva -> reserva.getCliente().getDni().equalsIgnoreCase(cliente.getDni())).toList();
    }

    public boolean cambiarEstadoReserva(String dni, LocalDate fecha, LocalTime hora, EstadoReserva estado) {
        if (dni == null || fecha == null || hora == null || estado == null) {
            System.out.println("no se pudo cambiar el estado");
        } else {
            for (int i = 0; i < listaReservas.size(); i++) {
                if (listaReservas.get(i).getCliente().getDni().equalsIgnoreCase(dni)
                        && listaReservas.get(i).getFecha().equals(fecha)
                        && listaReservas.get(i).getHora().equals(hora)) {
                    listaReservas.get(i).setEstado(estado);
                    System.out.println("estado cambiado correctamente");
                    return true;
                }
            }
        }
        System.out.println("reserva no encontrada");
        return false;
    }

    public boolean anularReserva(String dni, LocalDate fecha, LocalTime hora) {
        System.out.println("reserva cancelada correctamente");
        return cambiarEstadoReserva(dni, fecha, hora, EstadoReserva.CANCELADA);
    }
}