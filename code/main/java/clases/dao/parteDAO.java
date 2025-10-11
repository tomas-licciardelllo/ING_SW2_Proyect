package clases.dao;

import clases.model.parte;

import java.util.List;

public class parteDAO implements  dao<parte>{
    @Override
    public boolean create(parte parte) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
    public boolean update(parte parte) {
        return false;
    }

    @Override
    public parte read(int id) {
        return null;
    }

    @Override
    public List<parte> getAll() {
        return List.of();
    }

    public float obtenerPrecio(String nombre){
        return 10;
    }
}
