package clases.dao;

import java.util.List;

public interface dao<T> {
    public boolean create(T t);
    public boolean update(T t);
    public boolean delete(int id);
    public T read(int id);
    public List<T> getAll();
}
