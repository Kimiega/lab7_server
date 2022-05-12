package ioManager;
import java.io.Console;

public class ConsoleManager implements IReadable,IWritable{
    private final Console cons;
    private static ConsoleManager instance;


    private ConsoleManager() {
        cons = System.console();
    }
    public static ConsoleManager getInstance(){
        if (instance == null)
            instance = new ConsoleManager();

        return instance;
    }
    @Override
    public void write(String s){
        System.out.print(s);
    }

    @Override
    public void writeln(String s) {
        System.out.println(s);
    }

    @Override
    public String readline() {
        return cons.readLine();
    }

    public String readPassword(){
        return String.valueOf(cons.readPassword());
    }
}
