package com.everis.uploader;

import com.everis.uploader.lib.Manager;
import com.everis.uploader.lib.ManagerException;

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

public class UploaderTask extends DefaultTask {
    private static final String LIB_DIR = "/libs/";

    private File fileBuild;
    private Logger log;
    private UploaderConfiguration uploaderConfiguration = new UploaderConfiguration();

    public UploaderTask() {

    }

    public void uploaderConfiguration(Action<? super UploaderConfiguration> action) {
        action.execute(uploaderConfiguration);
    }

    public void setLogger(Logger logger) {
        this.log = logger;
    }

    public void setFileBuild(File fileBuild) {
        this.fileBuild = fileBuild;
    }

    @TaskAction
    public void run() {
        boolean resultUpload;
        Manager manager;
        File file;

        fileBuild = new File(fileBuild + LIB_DIR);

        if (isValidConnect(fileBuild)) {
            log.debug("Detected " + fileBuild + " for scan.");

            file = fileBuild.listFiles()[0];
            log.debug("Found " + file.getName() + " to upload.");

            manager = Manager.instance(uploaderConfiguration, log);

            try {
                manager.connect();
                log.info("Connected...");
                resultUpload = manager.upload(file, uploaderConfiguration.getInto());

                if (uploaderConfiguration.getLiferay() != null) {
                    log.info("Detect Liferay version: " + uploaderConfiguration.getLiferay());
                    if (Integer.parseInt(uploaderConfiguration.getLiferay()) >= 7) {
                        resultUpload = manager.upload(file, uploaderConfiguration.getDeployPath());
                    } else {
                        resultUpload = manager.upload(file, detectPath(file));
                    }
                }

                if (resultUpload) {
                    log.lifecycle("Uploaded " + file.getName() + " correctly.");
                } else {
                    log.lifecycle("Can't upload " + file.getName() + ".");
                }

            } catch (ManagerException ex) {
                manager.disconnect();
                log.error(ex.getMessage());
                throw new TaskExecutionException(this, ex.getCause());
            } finally {
                manager.disconnect();
            }

        } else {
            log.lifecycle("Not found file into " + fileBuild);
        }

    }

    private String detectPath(File file) {
        String path;

        if (file.getName().endsWith(".jar")) {
            path = uploaderConfiguration.getTomcatPath() + "/lib/ext/";
        } else if (file.getName().endsWith(".war")) {
            path = uploaderConfiguration.getDeployPath();
        } else {
            throw new RuntimeException("File " + file.getName() + " is not compatible");
        }

        return path;
    }

    private boolean isValidConnect(File buildPath) {

        if (!buildPath.exists()) {
            return false;
        }

        if (buildPath.listFiles() == null) {
            return false;
        }

        return true;
    }
}
