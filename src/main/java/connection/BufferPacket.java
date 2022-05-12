package connection;

import java.io.Serializable;
import java.net.SocketAddress;

//5120
public class BufferPacket implements Serializable {
    private final byte[] byteBuffer;
    private SocketAddress remote_address;
    private final boolean finish;
    public BufferPacket(byte[] a, boolean finish){
        byteBuffer =  a;
        remote_address = null;
        this.finish = finish;
    }

    public void setRemote_address(SocketAddress remote_address) {
        this.remote_address = remote_address;
    }

    public SocketAddress getRemote_address() {
        return remote_address;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public boolean isFinish() {
        return finish;
    }
}
