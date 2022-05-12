package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitCommand implements ICommand {
    static Logger LOGGER = Logger.getLogger(ExitCommand.class.getName());

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {

        return "exit                    | Завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        env.getOut().writeln("Завершение работы клиентской части");
        env.turnOff();
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        LOGGER.log(Level.WARNING,"Был вызван exit клиентом, что запрещено");
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
        ICommand cmd = new ExitCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
