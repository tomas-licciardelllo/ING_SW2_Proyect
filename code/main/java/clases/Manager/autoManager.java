package clases.Manager;
import clases.dao.AutoDAO;
import clases.model.auto;

import java.util.List;

public class autoManager {
    private final AutoDAO car;
    public autoManager(){
        car = new AutoDAO();
    }
    public List<auto> traerAutos(int id){
        return  car.getAutosByClienteId(id);
    }
    public int crearOtraerAutoXpatente(auto a){
        return car.obtenerOcrearAutoPorPatente(a);
    }
}
