package clases.Manager;
import clases.dao.AutoDAO;
import clases.model.auto;

import java.util.ArrayList;
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
    public List<auto> getAll(){List<auto> au = new ArrayList<>();return au = car.getAll();}
    public auto read(int id){return car.read(id);}
    public void update(auto aux){car.update(aux);}
}
