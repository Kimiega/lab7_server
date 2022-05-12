package ioManager;
import collection.*;
import utils.DateAdapter;

import java.util.Date;
import java.util.Objects;

public class RequestElement {
    private final IReadable in;
    private final IWritable out;
    private final boolean interactive;

    public RequestElement(IReadable in, IWritable out, boolean interactive){
        this.in = in;
        this.out = out;
        this.interactive = interactive;
    }

    private interface ICondition<T>{
        boolean check(T o);
    }

    private interface IExpression<T>{
        T exec();
    }
    private String readStr(){
        return in.readline();
    }
    private Integer readInt(){
        try {
            return Integer.parseInt(in.readline());
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    private Float readFloat(){
        try {
            return Float.parseFloat(in.readline());
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    private Long readLong(){
        try {
            return Long.parseLong(in.readline());
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }

    private <T> T readArg(String message, IExpression<T> query){
        if (interactive)
            out.write(message);
        return query.exec();
    }
    private <T> T readArgWhile(String message, String hint, ICondition<T> condition, IExpression<T> query){
        if (interactive)
            out.writeln(message);
        T o =  readArg(">>>",query);
        while (interactive && !condition.check(o)) {
            if (interactive)
                out.writeln(hint);
            o = readArg(">>>", query);
        }
        return o;
    }
    private String readName(){
        return readArgWhile("Введите название города: ", "Название не может быть пустым и длина должна быть меньше или равна 128 символов",
                (s) -> s!=null && !s.isEmpty() && s.length()<=128, this::readStr);
    }

    private Coordinates readCoords(){
        Long x = (Long)readArgWhile("Введите координаты города по Х: ", "Значение должно быть целым числом",
                Objects::nonNull, this::readLong);
        Float y = (Float)readArgWhile("Введите координаты города по Y: ", "Значение должно быть действительным числом",
                Objects::nonNull, this::readFloat);
        return new Coordinates(x,y);
    }

    private int readArea(){
        return readArgWhile("Введите зону: ", "Значение поля должно быть больше 0",
                (s) -> s!=null && s >0, this::readInt);
    }

    private Long readPopulation(){
        return readArgWhile("Введите количество населения: ", "Значение должно быть целым числом",
                Objects::nonNull, this::readLong);
    }
    private Float readMetersAboveSeaLevel(){
        return readArgWhile("Введите высоту над уровнем моря: ", "Значение должно быть действительным числом",
                Objects::nonNull, this::readFloat);
    }
    private int readTimezone(){
        return readArgWhile("Введите часовой пояс: ", "Значение поля должно быть целочисленным, больше -13 и меньше 15",
                (s) -> s!= null && s>-13 && s <=15, this::readInt);
    }
    private Long readAgglomeration(){
        return readArgWhile("Введите аггломерацию: ", "Значение должно быть целым числом",
                Objects::nonNull, this::readLong);
    }
    private Climate readClimate(){
        String climateStr = readArgWhile("Введите климат: ", "Значение должно быть пустым или одним из: "+ Climate.enumToStr(),
                (s) -> s!=null && (s.isEmpty() || Climate.isClimate(s)), this::readStr);
        if (climateStr.isEmpty())
            return null;
        else return Climate.valueOf(climateStr);
    }
    private Human readGovernor(){
        String governorName = readArgWhile("Введите имя мэра: ", "Имя должно состоять из символов и его длина должна быть меньше или равна 128 символов",
                (s) -> s!=null && s.length()<=128, this::readStr);
        if (governorName.isEmpty())
            return null;
        else {
            String dataPattern = "dd-mm-yyyy";
            Date birthday = DateAdapter.adapt(readArgWhile("Введите дату его рождения: ", "Дата должна быть в формате: " + dataPattern,
                    (s) -> s!=null && DateAdapter.isAdapting(s, dataPattern), this::readStr), dataPattern);
            return new Human(governorName,birthday);
        }
    }
    public City readElement(UserToken user){
        return new City(readName(), readCoords(),new Date(),
                readArea(), readPopulation(), readMetersAboveSeaLevel(),
                readTimezone(),readAgglomeration(),readClimate(),readGovernor(), user.getLogin());
    }
    public static City readElement(String[] values){
        int id = Integer.parseInt(values[0]);
        String name = values[1];
        Long coordinateX = Long.parseLong(values[2]);
        Float coordinateY = Float.parseFloat(values[3]);
        Date creationDate =DateAdapter.dateFromString(values[4]);
        int area = Integer.parseInt(values[5]);
        Long population = Long.parseLong(values[6]);
        Float metersAboveSeaLevel = Float.parseFloat(values[7]);
        int timezone = Integer.parseInt(values[8]);
        Long agglomeration = Long.parseLong(values[9]);
        Climate climate;
        if (values[10] == null)
            climate = null;
        else
            climate = Climate.valueOf(values[10]);
        Human governor;
        if (values[11] == null)
            governor = null;
        else{
            governor = new Human(values[11],DateAdapter.dateFromString(values[13]));
        }
        String owner = values[14];
        City city = new City(name,new Coordinates(coordinateX,coordinateY),creationDate,area,population,metersAboveSeaLevel,timezone,agglomeration,climate,governor,owner);
        city.setId(id);
        return city;
    }
}
