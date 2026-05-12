package com.javiercajchun.kinalapp.service;

import com.javiercajchun.kinalapp.entity.Cliente;
import com.javiercajchun.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Anotacion que registra un bean como un Bean de Spring
//Que la clase contiene la logica del negocio
@Service

//Por defecto todos los metodos de esta clase seran transccionales
//Esto es que puede o no ocurrir algo.
@Transactional

public class ClienteService implements IClienteServise{

    /* private solo accesible solo en la base
    * ClienteRepositort: es el repositorio para acceder a la base de datos
    * Inyeccion de dependencias Spring nos da el repositorio
    */
    private final ClienteRepository clienteRepository;

    /*
    * Constructor: Este se ejecuta al crear el objeto
    * Parametros: Spring pasa el repositorio automaticamente y a esto se le conoce como inyeccion de dependencias
    * Asignamos: el repositorio a nuestra variable de clase.
    */

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /*
    * @Override: Indicamos que estamos implementando un metodo de la interfaz
    */
    @Override
    /*
    * reafOnlu = true: optimiza la consulta y no bloquea la base de datos
    */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        /*
        * Llama al metodo findAll() del repositorio de Spring Data JPA
        * Este metodo hace exactamente el selct * from clientes
        */
    }

    //
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarEstadosActivos() {
        return clienteRepository.findAll()
                .stream() //se usa para pasar una coleccion de datos en una secuencia para que sea más facil el filtrar
                //datos
                .filter(cliente -> cliente.getEstado() == 1) //este filtra la secuencia de datos que nos da el Stream
                //para que si el cliente esta en activo pase y si no pues se quede afuera del JSON
                .toList();// Convierte la secuencia de datos en una lista nuevamente.
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
        * Metodo de Guardar crea un cliente
        * Aca es donde colocamos la logica del negocio antes de guardar
        * Primer: Validamos el dato
        * */
        validarCliente(cliente);
        if (cliente.getEstado() == 0)
            cliente.setEstado(1);

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    //Optional: nos evita el NullPointerException
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Busca un cliente por DPI
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        /*
        * Actualiza un cliente existente
        * */
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("Cliente no encontrado con DPI" + dpi);
            //Si no existe se lanza una excepcion (error controlado)
        }
        /*
        * 1. Se asegura que el DPI del objeto coincida con el de la url
        * 2. Por seguridad se usa el DPI de la URL y no el de JSON
        * */
        cliente.setDPICliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        //Eliminar un cliente por DPI
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro con el DPI" + dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeDPI(String dpi) {
        //Verifica si el cliente existe
        return clienteRepository.existsById(dpi);
        // retorna true o false w
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
        * Validaciones: este metodo se hara privado por que es algo interno del servicio
        */
        if(cliente.getDPICliente() == null  || cliente.getDPICliente().trim().isEmpty()){
            //si el DPI es null o esta vacio despues de quitar espacios lanza una exception con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }

        if(cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }

        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El Apellido es un dato obligatorio");
        }

    }

}
