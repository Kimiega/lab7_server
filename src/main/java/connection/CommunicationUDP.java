package connection;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommunicationUDP {
    static Logger LOGGER = Logger.getLogger(CommunicationUDP.class.getName());

    private final int PACKET_MAX_LENGTH = 5120;
    private final DatagramChannel channel;
    public CommunicationUDP(InetSocketAddress SERVER_SOCKET) throws IOException {
        channel = DatagramChannel.open();
        channel.socket().bind(SERVER_SOCKET);
        channel.configureBlocking(false);
    }
    public BufferPacket receive() throws IOException, IllegalArgumentException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(PACKET_MAX_LENGTH);
        SocketAddress remote_address =  channel.receive(byteBuffer);
        byteBuffer.flip();
        BufferPacket bp = (BufferPacket) SerializationUtils.deserialize(byteBuffer.array());
        LOGGER.log(Level.INFO,"Получен пакет от "+remote_address.toString()+"\nРазмер пакета = " + byteBuffer.array().length);
        bp.setRemote_address(remote_address);
        return bp;
    }

    public void send(Serializable obj,SocketAddress remote_address) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(SerializationUtils.serialize(obj));
        channel.send(byteBuffer, remote_address);
        LOGGER.log(Level.FINE,"Пакет отправлен клиенту "+remote_address.toString()+" Размер пакета = "+byteBuffer.array().length);
    }
    public void close() throws IOException {
        channel.close();
        LOGGER.log(Level.FINE,"Канал закрыт");
    }
}
