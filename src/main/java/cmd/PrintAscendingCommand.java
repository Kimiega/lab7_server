package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;

public class PrintAscendingCommand implements ICommand {

    @Override
    public String getName() {
        return "print_ascending";
    }

    @Override
    public String getDescription() {

        return "print_ascending         | Вывести элементы коллекции в порядке возрастания";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getCollectionManager().printAscending(env.getOut());
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().printAscending(env.getOut());
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
        ICommand cmd = new PrintAscendingCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
