package com.everis.uploader.lib;

import org.gradle.api.logging.Logger;

import com.everis.uploader.ConnectionType;
import com.everis.uploader.UploaderConfiguration;

import java.io.File;

public abstract class Manager {
    protected UploaderConfiguration uploaderConfiguration;
    protected Logger log;

    public static Manager instance(UploaderConfiguration uploaderConfiguration, Logger log) {
        Manager manager;

        if (uploaderConfiguration.getConnection().equals(ConnectionType.SFTP)) {
            manager = new SSHManager(uploaderConfiguration, log);
        } else {
            manager = new FTPManager(uploaderConfiguration, log);
        }

        return manager;
    }

    protected Manager(UploaderConfiguration uploaderConfiguration, Logger log) {
        this.uploaderConfiguration = uploaderConfiguration;
        this.log = log;
    }

    public abstract void connect() throws ManagerException;

    public abstract boolean upload(File file, String remotePath) throws ManagerException;

    public abstract void disconnect();

    public abstract void command() throws ManagerException;
}
