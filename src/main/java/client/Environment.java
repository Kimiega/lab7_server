package client;

import cmd.ICommand;
import collection.CollectionManager;
import connection.SQLManagerDB;
import ioManager.IReadable;
import ioManager.IWritable;

import java.util.HashMap;

public class Environment {
    private final CollectionManager collectionManager;
    private final HashMap<String, ICommand> commandMap;
    private final IReadable in;
    private IWritable out;
    private final SQLManagerDB managerDB;
    private boolean running;
    private final boolean script;
    public Environment(CollectionManager collectionManager, HashMap<String, ICommand> commandMap,SQLManagerDB managerDB, IReadable in, IWritable out,boolean isScript){
        this.collectionManager = collectionManager;
        this.commandMap = commandMap;
        this.managerDB = managerDB;
        this.in = in;
        this.out = out;
        this.script = isScript;
        running = true;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public HashMap<String, ICommand> getCommandMap() {
        return commandMap;
    }

    public IReadable getIn(){
        return in;
    }

    public IWritable getOut(){
        return out;
    }
    public boolean isRunning(){
        return running;
    }
    public void turnOff(){
        running = false;
    }
    public boolean isScript(){
        return script;
    }
    public void setOut(IWritable out) {
        this.out = out;
    }

    public SQLManagerDB getManagerDB() {
        return managerDB;
    }
}
