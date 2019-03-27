package com.everis.uploader.lib;

import com.everis.uploader.UploaderConfiguration;
import com.jcraft.jsch.*;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SSHManager extends Manager {
    private Session session;
    private Channel channel;
    private static final String SFTP = "sftp";
    private static final String EXEC = "exec";


    public SSHManager(UploaderConfiguration uploaderConfiguration, Logger log) {
        super(uploaderConfiguration, log);
    }

    @Override
    public void connect() throws ManagerException {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(uploaderConfiguration.getUser(),
                    uploaderConfiguration.getHost(), Integer.parseInt(uploaderConfiguration.getPort()));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            if (uploaderConfiguration.getKeyPath() != null) {
                log.debug("Connecting by keyPath...");
                jSch.addIdentity(uploaderConfiguration.getKeyPath());
                session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            } else {
                log.debug("Connecting without keyPath...");
                session.setPassword(uploaderConfiguration.getPass());
            }

            session.connect();
        } catch (JSchException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public boolean upload(File file, String remotePath) throws ManagerException {
        log.debug("Uploading file " + file.getName() + " to " + remotePath);
        try {
            channel = session.openChannel(SFTP);
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.put(file.getAbsolutePath(), remotePath + file.getName());
            return true;

        } catch (JSchException | SftpException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public void command() throws ManagerException {
        String command = uploaderConfiguration.getCommand();
        StringBuffer string = new StringBuffer();

        try {
            Channel channel = session.openChannel(EXEC);
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                string.append((char) readByte);
                readByte = commandOutput.read();
            }

        } catch (JSchException | IOException ex) {
            throw new ManagerException(ex);
        }
    }

    @Override
    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
