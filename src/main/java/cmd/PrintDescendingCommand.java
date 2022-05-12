package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;

public class PrintDescendingCommand implements ICommand {

    @Override
    public String getName() {
        return "print_descending";
    }

    @Override
    public String getDescription() {

        return "print_descending        | Вывести элементы коллекции в порядке убывания";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getCollectionManager().printDescending(env.getOut());
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().printDescending(env.getOut());
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
        ICommand cmd = new PrintDescendingCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
