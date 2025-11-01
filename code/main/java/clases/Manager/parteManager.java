package clases.Manager;

import clases.dao.parteDAO;
import clases.model.parte;

import java.util.List;

public class parteManager {
    private final parteDAO parte;
    public parteManager(){
        parte = new parteDAO();
    }

    public parte obtenerParte(int id){
        return parte.read(id);
    }

    public boolean actParte(parte p){
        return parte.update(p);
    }

    public List<parte> traerTodas(){
        return parte.getAll();
    }

    public boolean insertarParte(parte p){
        return parte.create(p);
    }

    public boolean modificarParte(parte p) {
        if (p == null || p.getIdDB() == 0) {
            return false; // No se puede modificar una parte sin ID
        }
        return this.actParte(p);
    }

    public boolean eliminarParte(parte p) {
        if (p == null || p.getIdDB() == 0) {
            return false; // No se puede eliminar una parte sin ID
        }
        return parte.delete(p.getIdDB());
    }
}
