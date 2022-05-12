package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

import java.util.HashMap;

public class RemoveAllByTimezoneCommand implements ICommand {

    @Override
    public String getName() {
        return "remove_all_by_timezone";
    }

    @Override
    public String getDescription() {

        return "remove_all_by_timezone  | Удалить из коллекции все элементы, значение поля timezone которого эквивалентно заданному";
    }

    @Override
    public void execute(Environment env, String arg, UserToken user) {
        int timezone;
        if (!arg.isEmpty()) {
            try {
                timezone = Integer.parseInt(arg);
            } catch (NumberFormatException ex) {
                env.getOut().writeln("Аргумент должен быть целым числом в диапазоне от -13 до 15");
                return;
            }
            if (timezone > 15 || timezone < -13) {
                env.getOut().writeln("Аргумент должен быть целым числом в диапазоне от -13 до 15");
                return;
            }
            env.getCollectionManager().removeAllByTimezone(env.getOut(), timezone,user);
        }
        else {
            env.getOut().writeln("Требуется ввести аргумент");
        }
    }

    @Override
    public void execute(Environment env, NetPackage netPackage) {
        env.getCollectionManager().removeAllByTimezone(env.getOut(), Integer.parseInt(netPackage.getArg()), netPackage.getUser());
    }

    @Override
    public boolean hasObject() {
        return false;
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    public static void register(HashMap<String, ICommand> commandMap) {
        ICommand cmd = new RemoveAllByTimezoneCommand();
        commandMap.put(cmd.getName(), cmd);
    }
}
