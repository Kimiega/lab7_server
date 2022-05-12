package connection;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderThread extends Thread{
    static Logger LOGGER = Logger.getLogger(SenderThread.class.getName());
    private CommunicationUDP communication;
    private Serializable object;
    private SocketAddress remote_address;

    public SenderThread(CommunicationUDP communication, Serializable object, SocketAddress remote_address){
        this.communication = communication;
        this.object = object;
        this.remote_address = remote_address;
    }
    @Override
    public void run(){
        try {
            communication.send(object,remote_address);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Ошибка отправки пакета клиенту");
        }
    }
}
