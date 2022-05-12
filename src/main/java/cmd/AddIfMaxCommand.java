package cmd;

import client.Environment;
import collection.City;
import collection.UserToken;
import connection.NetPackage;
import ioManager.RequestElement;

import java.util.HashMap;

public class AddIfMaxCommand implements ICommand {

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return "add_if_max              | Добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        RequestElement reqEl = new RequestElement(env.getIn(), env.getOut(), !env.isScript());
        City o = reqEl.readElement(user);
        env.getCollectionManager().addIfMax(env.getOut(), o,user);
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().addIfMax(env.getOut(), netPackage.getCity(),netPackage.getUser());
    }

    @Override
    public boolean hasObject() {
        return true;
    }

    @Override
    public boolean hasArg() {
        return false;
    }

    public static void register(HashMap<String, ICommand> commandMap) {
        ICommand cmd = new AddIfMaxCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
