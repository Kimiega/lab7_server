import client.Client;
import client.Environment;
import client.Server;
import cmd.*;
import collection.CollectionManager;
import connection.CommunicationUDP;
import connection.SQLManagerDB;
import ioManager.ConsoleManager;
import ioManager.EmptyOut;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Main.class.getName());
        }catch (IOException ex){
            LOGGER.log(Level.WARNING,"Файл с настройками логгера не найден или недоступен\nИспользуются стартовые настройки");
        }
    }



    public static void main(String[] args){
        int SERVER_PORT;
        try{
            ServerSocket servSoc = new ServerSocket(0);
            SERVER_PORT = servSoc.getLocalPort();
        }
        catch (IOException ex)
        {
            LOGGER.log(Level.SEVERE,"Не удалось найти свободный порт");
            return;
        }
        InetAddress SERVER_ADDRESS;
        try {
            SERVER_ADDRESS = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOGGER.severe("Не удалось задать адрес сервера");
            return;
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        InetSocketAddress SERVER_SOCKET;
        CommunicationUDP communication;
        try {
            SERVER_SOCKET = new InetSocketAddress(SERVER_ADDRESS,SERVER_PORT);
            communication = new CommunicationUDP(SERVER_SOCKET);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Не удалось установить адрес сервера");
            return;
        }
        catch (IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE,"Порт указан неверно");
            return;
        }
        SQLManagerDB managerDB;
        try {
            System.out.print("Enter database login:");
            String login = ConsoleManager.getInstance().readline();
            System.out.print("Enter database password:");
            String password = ConsoleManager.getInstance().readPassword();
            managerDB = new SQLManagerDB(login,password);
            LOGGER.info("Подключение к БД успешно");
        } catch (SQLException e) {
            LOGGER.severe("Database isn't available");
            return;
        }
        CollectionManager myCollection = new CollectionManager(managerDB);
        HashMap<String, ICommand> localCommandMap = new HashMap<>();
        {
            AddCommand.register(localCommandMap);
            AddIfMaxCommand.register(localCommandMap);
            AddIfMinCommand.register(localCommandMap);
            ClearCommand.register(localCommandMap);
            ExitCommand.register(localCommandMap);
            HelpCommand.register(localCommandMap);
            InfoCommand.register(localCommandMap);
            PrintAscendingCommand.register(localCommandMap);
            PrintDescendingCommand.register(localCommandMap);
            RemoveAllByTimezoneCommand.register(localCommandMap);
            RemoveByIdCommand.register(localCommandMap);
            RemoveGreaterCommand.register(localCommandMap);
            ShowCommand.register(localCommandMap);
            UpdateIdCommand.register(localCommandMap);
            LoadCommand.register(localCommandMap);
            ExecuteScriptCommand.register(localCommandMap);
        } //local commandMap
        HashMap<String, ICommand> remoteCommandMap = new HashMap<>();
        {
            AddCommand.register(remoteCommandMap);
            AddIfMaxCommand.register(remoteCommandMap);
            AddIfMinCommand.register(remoteCommandMap);
            ClearCommand.register(remoteCommandMap);
            InfoCommand.register(remoteCommandMap);
            PrintAscendingCommand.register(remoteCommandMap);
            PrintDescendingCommand.register(remoteCommandMap);
            RemoveAllByTimezoneCommand.register(remoteCommandMap);
            RemoveByIdCommand.register(remoteCommandMap);
            RemoveGreaterCommand.register(remoteCommandMap);
            ShowCommand.register(remoteCommandMap);
            UpdateIdCommand.register(remoteCommandMap);
        } //remote commandMap

        myCollection.load(ConsoleManager.getInstance());

        LOGGER.log(Level.INFO,"Server started on "+SERVER_SOCKET.getAddress().getHostAddress()+":"+SERVER_SOCKET.getPort());
        Environment serverEnv = new Environment(myCollection,remoteCommandMap,managerDB,ConsoleManager.getInstance(),new EmptyOut(),false);
        Server server = new Server(serverEnv,communication);
        Thread serverConsole = new Thread(server::init);
        serverConsole.start();

        Environment localEnv = new Environment(myCollection,localCommandMap,managerDB, ConsoleManager.getInstance(), ConsoleManager.getInstance(), false);
        Client client = new Client(localEnv);
        client.init();
        LOGGER.log(Level.INFO,"Завершение работы серверной части");
        serverEnv.turnOff();
        LOGGER.log(Level.INFO,"Завершение работы программы");
    }
}
