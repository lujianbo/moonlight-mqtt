package io.github.lujianbo.engine.common;

/**
 * Created by lujianbo on 2016/4/20.
 */
public class AuthMessage {

    private String clientId;

    private String username;

    private byte[] password;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
