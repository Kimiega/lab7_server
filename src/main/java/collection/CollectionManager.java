package collection;
import connection.SQLManagerDB;
import ioManager.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

public class CollectionManager implements Serializable {
    private static final long serialVersionUID = 1L;
    static Logger LOGGER = Logger.getLogger(CollectionManager.class.getName());
    private ConcurrentSkipListSet<City> cityCollection;
    private Date initializationDate;
    private Integer idOrder;
    private final transient SQLManagerDB managerDB;
    public CollectionManager(SQLManagerDB managerDB){
        cityCollection = new ConcurrentSkipListSet<>(new CustomComp());
        initializationDate = new Date();
        idOrder = 1;
        this.managerDB = managerDB;
    }
    public void add(IWritable out, City o,UserToken user) {
        if (!cityCollection.contains(o)) {
            try {
                managerDB.addWithoutID(o,user);
            } catch (SQLException e) {
                LOGGER.warning("Ошибка добавления в ДБ\n" + e);
                out.writeln("Ошибка добавления в ДБ");
                return;
            }
            o.setId(idOrder++);
            cityCollection.add(o);
            out.writeln("Объект добавлен");
        }
        else
            out.writeln("Объект уже существует");
    }
    public void addIfMax(IWritable out, City o, UserToken user){
        if (cityCollection.size()==0 || o.compareTo(cityCollection.stream().max(Comparator.naturalOrder()).get())>0){
            try {
                managerDB.addWithoutID(o,user);
            } catch (SQLException e) {
                LOGGER.warning("Ошибка добавления в ДБ\n"+e);
                out.writeln("Ошибка добавления в ДБ");
                return;
            }
            add(out,o, user);
        }
        else
            out.writeln("Объект не был добавлен");
    }
    public void addIfMin(IWritable out, City o, UserToken user){
        if (cityCollection.size()==0 || o.compareTo(cityCollection.stream().min(Comparator.naturalOrder()).get())<0){
            try {
                managerDB.addWithoutID(o,user);
            } catch (SQLException e) {
                LOGGER.warning("Ошибка добавления в ДБ\n"+e);
                out.writeln("Ошибка добавления в ДБ");
                return;
            }
            add(out, o,user);
        }
        else out.writeln("Объект не был добавлен");
    }
    public void clear(IWritable out, UserToken user){
        try {
            managerDB.clear(user);
        } catch (SQLException e) {
            LOGGER.warning("Ошибка удаления данных ДБ\n"+e);
            out.writeln("Ошибка удаления данных ДБ");
            return;
        }
        cityCollection.removeIf(city -> city.getOwner().equals(user.getLogin()));
        out.writeln("Коллекция очищена");
    }

    public void info(IWritable out,UserToken user) {
        out.writeln("Type: " + cityCollection.getClass().toString());
        out.writeln("Date of initialization: " + initializationDate.toString());
        out.writeln("Amount of elements: " + cityCollection.size());
        out.writeln("Amount of your elements: " + cityCollection.stream().filter(city -> city.getOwner().equals(user.getLogin())).count());
    }
    public void printAscending(IWritable out){
        cityCollection.forEach((s)-> out.writeln(s.toString()));
    }
    public void printDescending(IWritable out){
        cityCollection.descendingSet().forEach((s)-> out.writeln(s.toString()));
    }
    public void removeAllByTimezone(IWritable out,int timezone,UserToken user){
        cityCollection.stream().filter(city -> city.getTimezone()==timezone).filter(city -> city.getOwner().equals(user.getLogin())).forEach((o)-> {
            try {
                managerDB.remove(o,user);
            } catch (SQLException e) {
                LOGGER.warning("Ошибка удаления объекта из ДБ\n"+e);
                out.writeln("Ошибка удаления объекта из ДБ");
            }
        });
        cityCollection.removeIf(city -> city.getTimezone()==timezone && city.getOwner().equals(user.getLogin()));
        out.writeln("Объекты удалены");
    }
    public void removeById(IWritable out, int id,UserToken user){
            try {
                City city = cityCollection.stream().filter(c -> c.getId()==id).findFirst().get();
                if (managerDB.remove(city,user)==0) {
                    out.writeln("Access DENIED");
                    return;
                }
                cityCollection.removeIf(c -> c.getId() == id && c.getOwner().equals(user.getLogin()));
                out.writeln("Объект удален");
            } catch (SQLException e) {
                LOGGER.warning("Ошибка удаления объекта из ДБ\n"+e);
                out.writeln("Ошибка удаления объекта из ДБ");
            }
            catch (NoSuchElementException e){
                out.writeln("Объект не найден");
            }
    }
    public void removeGreater(IWritable out, City o,UserToken user){
        cityCollection.stream().filter((s)->o.compareTo(s)>0 && s.getOwner().equals(user.getLogin())).forEach((s)-> {
            try {
                managerDB.remove(o,user);
                cityCollection.removeIf((c)->o.compareTo(c)>0&&c.getOwner().equals(user.getLogin()));
                out.writeln("Объекты удалены");
            } catch (SQLException e) {
                LOGGER.warning("Ошибка удаления объекта из ДБ\n"+e);
                out.writeln("Ошибка удаления объекта из ДБ");
            }
        });
    }
    public void show(IWritable out){
        cityCollection.forEach((s)->out.writeln(s.toString()));
    }
    public void updateById(IWritable out, int id, City o,UserToken user){

        if (cityCollection.stream().anyMatch((s)->s.getId()==id && s.getOwner().equals(user.getLogin()))){
            o.setId(id);
            try {
                managerDB.remove(o,user);
                managerDB.addWithID(o,user);
            } catch (SQLException e) {
                LOGGER.warning("Ошибка замены объекта в ДБ\n"+e);
                out.writeln("Ошибка замены объекта в ДБ");
                return;
            }
            cityCollection.removeIf((s)->s.getId()==id);
            cityCollection.add(o);
            out.writeln("Объект был изменен");
        }
        else
            out.writeln("Объект не найден или принадлежит не вам");
    }
    public void load(IWritable out){
        try {
            this.cityCollection = managerDB.getCollection();
            this.initializationDate = new Date();
            int lastId = managerDB.getLastID();
            managerDB.restartSeq(lastId+1);
            this.idOrder = lastId+1;
        } catch (SQLException e) {
            LOGGER.warning("Не удалось получить доступ к БД\n"+e);
            return;
        }
        out.writeln("Коллекция была загружена из DB");
    }


}
