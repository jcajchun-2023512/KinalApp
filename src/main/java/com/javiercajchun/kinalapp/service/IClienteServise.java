package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteServise {
    //Interfaz: es un contrato que dice QUÉ metodos debe tener cualquier servicio de clientes, no tiene implementacion ,solo la definicion de los metodos

    //Metodo que devuelve una lista de todos los clientes.
    List<Cliente> listarTodos();
    //List<Cliebte> devuelve una lista de objetos de la entidad clientes

    //Metodo para devolver una lista de todos los clientes activos
    List<Cliente> listarEstadosActivos();
    //Metodo que guarda un cliente en la base de datos

    Cliente guardar(Cliente cliente);
    //Parametros - Recibe un objeto de tipo cliente con los datos a guardar

    //Optional - contenedor que puede o no tener un valor, evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un cliente
    Cliente actualizar(String dpi, Cliente cliente);
    //Parametros - dpi: DPI del cliente a actualizar|
    //Cliente cliente: Objeto con los datos nuevos y retorna un objeto de tipo cliente ya actualizado

    //Metodode tipo void para eliminar a un cliente
    //Void no retorna ningun dato, elimina el cliente por su dpi
    void eliminar(String dpi);

    //boolean - retorna true or false
    boolean existeDPI(String dpi);

    //Metodo de busqueda de estado
}
