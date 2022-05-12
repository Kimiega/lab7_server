package collection;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long x; //Поле не может быть null
    private final Float y; //Максимальное значение поля: 960, Поле не может быть

    public Coordinates(Long x, Float y){
        this.x = x;
        this.y = y;
    }

    public int compareTo(Coordinates o) {
        if (o==null){
            return 1;
        }
        int r = this.x.compareTo(o.x);
        if (r==0) {
            r = this.y.compareTo(o.y);
        }
            return r;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Float getY() {
        return y;
    }

    public Long getX() {
        return x;
    }
}
