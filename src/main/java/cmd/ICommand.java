package cmd;

import client.Environment;
import collection.UserToken;
import connection.NetPackage;

public interface ICommand {
    String getName();
    String getDescription();
    void execute(Environment env, String arg, UserToken user);
    void execute (Environment env, NetPackage netPackage);
    boolean hasObject();
    boolean hasArg();
}
