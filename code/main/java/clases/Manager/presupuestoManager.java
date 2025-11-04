package clases.Manager;

import clases.dao.PresupuestoDAO;
import clases.model.presupuesto;

import java.util.ArrayList;
import java.util.List;

public class presupuestoManager {

    private final PresupuestoDAO pres;

    public presupuestoManager(){
        pres = new PresupuestoDAO();
    }

    public int crearYobtenerID(presupuesto p){
        return pres.createAndGetID(p);
    }

    public List<presupuesto> obtenerDeudas(){
        List<presupuesto> presupuestosImpagos = new ArrayList<>();
        return  presupuestosImpagos;
    }

    public List<presupuesto> obtenerTodos(){
        return pres.getAll();
    }
}
