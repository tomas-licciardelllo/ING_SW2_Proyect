package clases.Manager;
import clases.dao.AutoDAO;
import clases.model.auto;

import java.util.List;

public class autoManager {
    private final AutoDAO car;
    public autoManager(){
        car = new AutoDAO();
    }
    public auto traerAutoId(int id) { return car.read(id); }
    public List<auto> traerAutos(int id){
        return  car.getAutosByClienteId(id);
    }
    public List<auto> getAll() { return car.getAll(); }
    public int crearOtraerAutoXpatente(auto a){
        return car.obtenerOcrearAutoPorPatente(a);
    }
    public boolean actualizarAuto(auto a) { return car.update(a); }
}
