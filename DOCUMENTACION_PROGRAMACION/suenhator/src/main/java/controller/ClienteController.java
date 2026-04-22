package controller;

import model.Cliente;
import org.example.suenhator.data.Dataset;

import java.util.ArrayList;

public class ClienteController {

    public ClienteController() {
    }

    public Cliente darDeAlta(Cliente cliente) {
        if (cliente==null){
            System.out.println("el cliente es nulo");
        } else if (cliente.getDni()==null ||cliente.getDni().isBlank()) {
            System.out.println("el dni del cliente introducido no existe");
        } else if (Dataset.listaClientes.stream().anyMatch(cliente1 -> cliente1.getDni().equalsIgnoreCase(cliente.getDni()))) {
            System.out.println("cliente ya registrado");
        }else {
            Dataset.listaClientes.add(cliente);
            System.out.println("cliente registrado correctamente");
            return cliente;
        }
        return null;
    }

    public Cliente modificarDatos(Cliente cliente) {
        //cliente es nulo?
        if (cliente == null) {
            System.out.println("el cliente introducido es nulo");
            return null;
        }
        Cliente clienteEncontrado = buscarPorDni(cliente.getDni());
        //no se ha encontrado?
        if (clienteEncontrado == null) {
            System.out.println("cliente no encontrado para el dni introducido");
            return null;
        }
        //llego aqui: todo bien
        //recorro y sustituyo
        for (int i = 0; i < Dataset.listaClientes.size(); i++) {
            if (cliente.getDni().equalsIgnoreCase(Dataset.listaClientes.get(i).getDni())) {
                Dataset.listaClientes.set(i, cliente);
                System.out.println("Cliente modificado correctamente");
                return cliente;
            }
        }
        return null;
    }

    public boolean darDeBaja(Cliente cliente) {
        if (cliente == null) {
            System.out.println("no se ha encontrado el dni del cliente");
            return false;
        }
        //busco el cliente con ese dni
        Cliente clienteEncontrado = buscarPorDni(cliente.getDni());

        //no se ha encontrado?
        if (clienteEncontrado == null) {
            return false;
        } else {
            //elimino el cliente encontrado
            Dataset.listaClientes.remove(clienteEncontrado);
            System.out.println("eliminado correctamente");
            return true;
        }
    }

    public Cliente buscarPorDni(String dni) {
        if (dni==null||dni.isBlank()) {
            System.out.println("el dni esta vacio");
        }else if (!Dataset.listaClientes.stream().anyMatch(cliente1 -> cliente1.getDni().equalsIgnoreCase(dni))){
            System.out.println("el cliente no esta registrado");
        }else {
            Cliente cliente= Dataset.listaClientes.stream().filter(cliente1 -> cliente1.getDni().equalsIgnoreCase(dni)).findFirst().orElse(null);
            return cliente;
        }
        return null;
    }

    public ArrayList<Cliente> listarTodos() {
        //compruebo si esta vacio
        if (Dataset.listaClientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
        } else {
            //imprimo clientes
            Dataset.listaClientes.forEach(System.out::println);
        }
        return new ArrayList<>(Dataset.listaClientes);
    }
}