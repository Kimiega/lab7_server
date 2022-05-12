package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;

public class ShowCommand implements ICommand {

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {

        return "show                    | Вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getCollectionManager().show(env.getOut());
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().show(env.getOut());
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
        ICommand cmd = new ShowCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
