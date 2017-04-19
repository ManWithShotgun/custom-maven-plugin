package ru.ilia.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by ILIA on 17.04.2017.
 */
@Mojo(name = "copy")
public final class CopyFileBetweenDirectories extends AbstractMojo {

    @Parameter
    private String input;

    @Parameter
    private String output;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(String.format("Copy file from %s to %s", input, output));
        this.copyFile(new File(input), new File(output));
        getLog().info("Copy done!");
    }

    private void copyFile(File inputFile, File outputFile){
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(inputFile).getChannel();
                destination = new FileOutputStream(outputFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        } catch (FileNotFoundException e) {
            getLog().error(e);
        } catch (IOException e) {
            getLog().error(e);
        }
    }
}
