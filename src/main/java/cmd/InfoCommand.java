package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;

public class InfoCommand implements ICommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {

        return "info                    | Вывести в стандартный поток вывода информацию о коллекции";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getCollectionManager().info(env.getOut(),user);
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().info(env.getOut(),netPackage.getUser());
    }

    @Override
    public boolean hasObject() {
        return false;
    }

    @Override
    public boolean hasArg() {
        return false;
    }

    public static void register(HashMap<String, ICommand> commandMap) {
        ICommand cmd = new InfoCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
