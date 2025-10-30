package clases.Manager;
import clases.dao.ClienteDAO;
import clases.model.cliente;

import java.util.List;

public class clienteManager {
    private final ClienteDAO cli;
    public clienteManager(){
        cli = new ClienteDAO();
    }

    public cliente subirCliente(cliente c){
       return cli.createMOD(c);
    }

    public List<cliente> traerCLientes(){
        return cli.getAll();
    }

    public boolean actualizarCliente(cliente c){
        return cli.update(c);
    }

    public cliente traerCLiente(int id){
        return cli.read(id);
    }
}
