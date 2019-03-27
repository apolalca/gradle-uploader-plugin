package com.everis.uploader.lib;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.gradle.api.logging.Logger;

import com.everis.uploader.UploaderConfiguration;

import java.io.*;

public class FTPManager extends Manager {
    private FTPSClient client;

    public FTPManager(UploaderConfiguration uploaderConfiguration, Logger log) {
        super(uploaderConfiguration, log);
    }


    public void connect() throws ManagerException {
        int reply;

        try {
            client = new FTPSClient(false);
            log.lifecycle("Connecting to " + uploaderConfiguration.getHost() + ":" + uploaderConfiguration.getPort());
            client.connect(uploaderConfiguration.getHost(), Integer.parseInt(uploaderConfiguration.getPort()));
            reply = client.getReply();

            if (!FTPReply.isPositiveCompletion(reply)) {
                disconnect();
                throw new ManagerException("Can't connect to FTP Server... Reply: " + reply);
            }

            client.login(uploaderConfiguration.getUser(), uploaderConfiguration.getPass());

        } catch (IOException ex) {
            throw new ManagerException("Could not connect to server " + uploaderConfiguration.getHost() + ": " + ex.getMessage());
        }

    }

    @Override
    public boolean upload(File file, String remotePath) throws ManagerException {
        log.debug("Uploading file " + file.getName() + " to " + remotePath);

        InputStream inputStream;
        OutputStream outputStream;

        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        client.setBufferSize(1000);
        client.enterLocalPassiveMode();

        try {
            inputStream = new FileInputStream(file);

            outputStream = client.storeFileStream(remotePath);
            byte[] bytesIn = new byte[4096];
            int read = 0;

            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();

             return client.completePendingCommand();

        } catch (IOException ex) {
            throw new ManagerException(ex);
        }
    }

    public void disconnect() {
        if (client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
                log.lifecycle("Disconnect successfully");
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
    }

    @Override
    public void command() throws ManagerException {
        throw new ManagerException("Command action is not applicable for FTPS");
    }
}
