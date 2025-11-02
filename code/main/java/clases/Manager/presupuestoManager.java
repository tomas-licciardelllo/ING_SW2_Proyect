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
        //Aca llamamos al DAO y le ponemos
        //el método para obtener los que deben
        List<presupuesto> presupuestosImpagos = new ArrayList<>();
        return  presupuestosImpagos;
    }
}
