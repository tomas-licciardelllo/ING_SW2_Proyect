package clases.manager;

import clases.dao.AutoDAO;
import clases.model.auto;


import java.util.List;

public class autoManager {

    public List<auto> traerAutos(){
        AutoDAO a = new AutoDAO();
        List<auto> listaAutos = a.getAll();
        return listaAutos;
    }

    public boolean entregarAuto(auto a){
        AutoDAO aux = new AutoDAO();
        return aux.create(a);
    }

}
