package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadCommand implements ICommand {
    static Logger LOGGER = Logger.getLogger(LoadCommand.class.getName());
    @Override
    public String getName() {
        return "load";
    }

    @Override
    public String getDescription() {

        return "load                    | Загрузить коллекцию из файла";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getCollectionManager().load(env.getOut());
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        LOGGER.log(Level.WARNING,"Был вызван load клиентом, что запрещено");
    }

    @Override
    public boolean hasObject() {
        return false;
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    public static void register( HashMap<String, ICommand> commandMap) {
        ICommand cmd = new LoadCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
