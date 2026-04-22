package controller;

import javafx.collections.ObservableList;
import model.Cliente;
import model.Pack;
import model.Reserva;
import model.Sala;
import model.Supervisor;
import model.enums.EstadoReserva;
import org.example.suenhator.data.Dataset;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaController {

    public ReservaController() {
    }

    public ObservableList<Pack> listarPacks() {
        //compruebo si esta vacio
        if (Dataset.listaPacks.isEmpty()){
            System.out.println("no hay packs registrados");
        }else {
            //imprimo packs
            Dataset.listaPacks.forEach(System.out::println);
            return Dataset.listaPacks;
        }
        return null;
    }

    public List<Sala> listarSalasDisponibles(LocalDate fecha, LocalTime hora) {
        //compruebo si hay salas registradas
        if (Dataset.listaSalas.isEmpty()) {
            System.out.println("No hay salas registradas");
        } else {
            //filtro las salas que no tengan una reserva activa en esa fecha y hora
            List<Sala> listaSalasDisponibles = Dataset.listaSalas.stream()
                    .filter(sala -> Dataset.listaReservas.stream()
                            .filter(reserva -> reserva.getFecha().equals(fecha))
                            .filter(reserva -> reserva.getHora().equals(hora))
                            .filter(reserva -> reserva.getEstado() != EstadoReserva.CANCELADA)
                            .noneMatch(reserva -> reserva.getSala().equals(sala)))
                    .toList();

            //no hay salas disponibles?
            if (listaSalasDisponibles.isEmpty()) {
                System.out.println("No hay ninguna sala disponible para esa fecha y hora");
            } else {
                //imprimo salas disponibles
                listaSalasDisponibles.forEach(System.out::println);
                return listaSalasDisponibles;
            }
        }
        return null;
    }

    public ObservableList<Supervisor> listarSupervisores() {
        //compruebo si esta vacio
        if (Dataset.listaSupervisores.isEmpty()){
            System.out.println("no hay supervisores registrados");
        }else {
            //imprimo supervisores
            Dataset.listaSupervisores.forEach(System.out::println);
            return Dataset.listaSupervisores;
        }
        return null;
    }

    public boolean consultarDetallePack(Pack pack) {
        //compruebo que no es nulo
        if (pack==null){
            System.out.println("pack no existente");
        } else if (Dataset.listaPacks.stream().anyMatch(pack1 -> pack1.getNombre().equalsIgnoreCase(pack.getNombre()))) {
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
            //busco las salas disponibles
            List<Sala> listaSalasDispo = listarSalasDisponibles(fecha,hora);

            //no hay salas disponibles?
            if (listaSalasDispo==null) {
                System.out.println("no hay salas disponibles");
            }else {
                //cojo la primera sala disponible
                Sala sala = listaSalasDispo.stream().findFirst().orElse(null);
                Reserva reserva = new Reserva(cliente, sala, pack, supervisor, fecha, hora);
                System.out.println("reserva creada correctamente");
                Dataset.listaReservas.add(reserva);
                return reserva;
            }

        }
        return null;
    }

    public List<Reserva> consultarReservasPorFecha(LocalDate fecha) {
        return Dataset.listaReservas.stream()
                .filter(reserva -> reserva.getFecha().equals(fecha))
                .toList();
    }

    public List<Reserva> consultarReservasPorCliente(Cliente cliente) {
        return Dataset.listaReservas.stream()
                .filter(reserva -> reserva.getCliente().getDni().equalsIgnoreCase(cliente.getDni()))
                .toList();
    }

    public boolean cambiarEstadoReserva(String dni, LocalDate fecha, LocalTime hora, EstadoReserva estado) {
        if (dni == null || fecha == null || hora == null || estado == null) {
            System.out.println("no se pudo cambiar el estado");
        } else {
            //recorro las reservas
            for (int i = 0; i < Dataset.listaReservas.size(); i++) {
                if (Dataset.listaReservas.get(i).getCliente().getDni().equalsIgnoreCase(dni)
                        && Dataset.listaReservas.get(i).getFecha().equals(fecha)
                        && Dataset.listaReservas.get(i).getHora().equals(hora)) {
                    Dataset.listaReservas.get(i).setEstado(estado);
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