package com.javiercajchun.kinalapp.controller;

import com.javiercajchun.kinalapp.entity.Cliente;
import com.javiercajchun.kinalapp.repository.ClienteRepository;
import com.javiercajchun.kinalapp.service.ClienteService;
import com.javiercajchun.kinalapp.service.IClienteServise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController: El servicio de controller + @ResponseBody
@RequestMapping("/clientes")
//@RequestMapping: Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {

    //Inyectamos el servicio y no el repositorio
    //El controlador solo debe tener coneccion con el servicio
    private final IClienteServise clienteService;

    //Como buena practica la inyeccion de dependencias debe hacerse por el constructor
    public ClienteController(IClienteServise clienteService) {
        this.clienteService = clienteService;
    }

    //Responde a peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo http y el cuerpo
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.listarTodos();
        //Denegamos al servicio y ademas el estatus 200 que es el Ok con el estado de Clientes
        return ResponseEntity.ok(clientes);
    }

    //Lista todos los clientes que estan activos
    @GetMapping("/estado")
    public ResponseEntity<List<Cliente>> listarEstadosActivos(){
        return ResponseEntity.ok(clienteService.listarEstadosActivos());
    }

    //{dpi} es una variable de ruta (valor a buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //El @PathVariable toma el valor de la URL y lo asigna al DPI
        return clienteService.buscarPorDPI(dpi)
                //Si el Opcional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si opcional esta vacio devuelve 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    //Post crea un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo cliente
        //<?> Es un tipo generico, puede ser un cliente o un String
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Integramos guardar el cliente pero puede lanzar una excepcion de Illegal
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED
        }catch (IllegalArgumentException e){
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 Bad Request
        }
    }

    //Delet elimina un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try {
            if(!clienteService.existeDPI(dpi)) {
                return ResponseEntity.notFound().build();
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 se ejecuto correctamente
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Actualizar cliente a traves de DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try{
            if(!clienteService.existeDPI(dpi)){
                //Verifica si existe antes de actualizar
                //404 Not Found
                return ResponseEntity.notFound().build();
            }
            //Actualizar el cliente pero puede lanzar una excepcion
            Cliente clienteActualizar = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizar);
            //200 ok con el cliente ya actualizado
        }catch(IllegalArgumentException e){
            //Error cuando los datos son incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            //Posiblemente cualquier otro error como: Cliente no encontrado, etc
            //Error 404 not found
            return ResponseEntity.notFound().build();
        }
    }
}