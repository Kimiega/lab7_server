package connection;

import java.io.Serializable;

public class CommandPackage implements Serializable {
    private final String name;
    private final String description;
    private final boolean hasArg;
    private final boolean hasObject;
    private final boolean finish;

    public CommandPackage(String name, String description, boolean hasArg, boolean hasObject, boolean finish){
        this.name = name;
        this.description = description;
        this.hasArg = hasArg;
        this.hasObject = hasObject;
        this.finish = finish;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHasArg() {
        return hasArg;
    }

    public boolean isHasObject() {
        return hasObject;
    }

    public boolean isFinish() {
        return finish;
    }
}
