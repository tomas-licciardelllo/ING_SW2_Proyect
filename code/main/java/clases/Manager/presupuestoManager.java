package clases.Manager;

import clases.dao.PresupuestoDAO;
import clases.model.presupuesto;

public class presupuestoManager {

    private final PresupuestoDAO pres;

    public presupuestoManager(){
        pres = new PresupuestoDAO();
    }

    public int crearYobtenerID(presupuesto p){
        return pres.createAndGetID(p);
    }
}
