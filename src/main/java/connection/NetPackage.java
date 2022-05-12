package connection;

import collection.City;
import collection.UserToken;

import java.io.Serializable;
import java.net.SocketAddress;


public class NetPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cmd = "";
    private String arg = "";
    private City city = null;
    private UserToken user = null;
    SocketAddress remote_address = null;
    public String getCmd() {
        return cmd;
    }

    public String getArg() {
        return arg;
    }

    public City getCity() {
        return city;
    }

    public UserToken getUser() {
        return user;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setUser(UserToken user) {
        this.user = user;
    }

    public void setRemote_address(SocketAddress remote_address) {
        this.remote_address = remote_address;
    }

    public SocketAddress getRemote_address() {
        return remote_address;
    }
}