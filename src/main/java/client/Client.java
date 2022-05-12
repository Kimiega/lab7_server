package client;

import cmd.LoginCommand;
import collection.UserToken;

import java.sql.SQLException;

public class Client {
    private UserToken user;
    private final Environment env;
    public Client(Environment env){
        this.env = env;
        user = null;
    }
    public void init() {
        while (env.isRunning()) {

            String s = env.getIn().readline();
            if (s == null)
                if (env.isScript())
                    break;
                else
                    continue;

            String cmd = "";
            String arg = "";

            String[] sArr = s.split("\\s");
            if (sArr.length==1)
                cmd = sArr[0];
            if (sArr.length>1)
            {
                cmd = sArr[0];
                arg = sArr[1];
            }
            try {
            if (cmd.equals("login"))
                user = LoginCommand.login(env);
            else if (cmd.equals("register"))
                LoginCommand.register(env.getManagerDB());
            else
                if (user!=null && env.getManagerDB().checkUser(user))
                if (env.getCommandMap().containsKey(cmd)) {
                    env.getCommandMap().get(cmd).execute(env, arg, user);
                }
                else {
                    env.getOut().writeln("Command not found (type \"help\" to get information about available commands)");
                }
                else{
                    env.getOut().writeln("Вы не авторизированный пользователь");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка доступа к Базе данных!");}
        }
    }
}

