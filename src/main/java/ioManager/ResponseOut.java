package ioManager;

import connection.CommunicationUDP;
import connection.NetResponse;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseOut implements IWritable{
    private static final Logger LOGGER = Logger.getLogger(ResponseOut.class.getName());
    CommunicationUDP communication;
    SocketAddress remote_address;
    public ResponseOut(CommunicationUDP communication, SocketAddress remote_address){
        this.communication = communication;
        this.remote_address = remote_address;
    }
    @Override
    public void write(String s) {
        try {
            communication.send(new NetResponse(s,false),remote_address);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Ошибка отправки пакета клиенту");
        }
    }

    @Override
    public void writeln(String s) {
        write(s+"\n");
    }
}
