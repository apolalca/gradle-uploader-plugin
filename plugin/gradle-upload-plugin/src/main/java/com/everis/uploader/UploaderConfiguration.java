package com.everis.uploader;

public class UploaderConfiguration {
    private ConnectionType connection;
    private String host;
    private String port;
    private String user;
    private String pass;
    private String into;
    private String deployPath;
    private String tomcatPath;
    private String liferay;
    private String keyPath;
    private String keyStorePass;
    private String command;

    public UploaderConfiguration() {
        this.host = "localhost";
        this.user = "test";
        this.pass = "test";
        this.port = "22";
        this.into = "/";
    }

    public ConnectionType getConnection() {
        return connection;
    }

    public void setConnection(ConnectionType connection) {
        this.connection = connection;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    public String getTomcatPath() {
        return tomcatPath;
    }

    public void setTomcatPath(String tomcatPath) {
        this.tomcatPath = tomcatPath;
    }

    public String getLiferay() {
        return liferay;
    }

    public void setLiferay(String liferay) {
        this.liferay = liferay;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getInto() {
        return into;
    }

    public void setInto(String into) {
        this.into = into;
    }

    @Override
    public String toString() {
        return "UploaderConfiguration{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", into='" + into + '\'' +
                ", pathBuild='" + '\'' +
                '}';
    }
}