package main.javaserver.confreaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ConfigurationReader {

    private File file;
    protected BufferedReader fileReader;

    public ConfigurationReader (String fileName) throws FileNotFoundException {
        file = new File(fileName);
        fileReader = new BufferedReader(new FileReader(file));
    }

    protected String[] parseLine(String readLine) {
        List<String> matches = new ArrayList<>();

        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(readLine);
        while (m.find()) {
            matches.add(m.group(1).replace("\"", ""));
        }

        return matches.toArray(new String[0]);
    }

    public abstract void load() throws IOException;

}