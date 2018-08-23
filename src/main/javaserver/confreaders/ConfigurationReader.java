package main.javaserver.confreaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class ConfigurationReader {

    private File file;
    protected BufferedReader fileReader;

    public ConfigurationReader (String fileName) throws FileNotFoundException {
        file = new File(fileName);
        fileReader = new BufferedReader(new FileReader(file));
    }

    public abstract void load() throws IOException;

}