package main.javaserver.confreaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpdConf extends ConfigurationReader {

    private Map<String, String> otherDirectives;
    private String serverRoot;
    private int listen;
    private String documentRoot;
    private String logFile;
    private Map<String, String> aliases;
    private Map<String, String> scriptedAliases;
    private String accessFileName;
    private List<String> directoryIndex;

    public HttpdConf (String fileName) throws FileNotFoundException {
        super(fileName);

        serverRoot = null;
        listen = 8080;
        documentRoot = null;
        logFile = null;
        aliases = new HashMap<>();
        scriptedAliases = new HashMap<>();
        accessFileName = ".htaccess";
        directoryIndex = new ArrayList<>();
        otherDirectives = new HashMap<>();
    }


    @Override
    public void load() throws IOException {
        String readLine = "";

        while ((readLine = fileReader.readLine()) != null) {
            readLine = readLine.trim();

            if (!readLine.equals("") && readLine.charAt(0) != '#') {
                storeHandleConfigDirective(parseLine(readLine));
            }
        }
    }

    private void storeHandleConfigDirective(String[] configKeyVal) {
        String directive = configKeyVal[0];
        
        switch (directive) {
            case "ServerRoot":
                serverRoot = configKeyVal[1];
            case "Listen":
                listen = Integer.parseInt(configKeyVal[1]);
            case "DocumentRoot":
                documentRoot = configKeyVal[1];
            case "LogFile":
                logFile = configKeyVal[1];
            case "Alias":
                aliases.put(configKeyVal[1], configKeyVal[2]);
            case "ScriptAlias":
                scriptedAliases.put(configKeyVal[1], configKeyVal[2]);
            case "AccessFileName":
                accessFileName = configKeyVal[1];
            case "DirectoryIndex":
                for (int i = 1; i < configKeyVal.length; i++) {
                    directoryIndex.add(configKeyVal[i]);
                }
            default:
                otherDirectives.put(directive, configKeyVal[1]);
        }
    }

    /**
     * @param configFieldName The field name to look for in order to obtain its value.
     * @return null or a string array of values mapped to the given field name. Null indicates
     * that the field name does not exist in the file. Should it return a string array, the caller
     * must check the length of the array to check if the number of arguments match, if needed.
     */
    public String[] getConfigValue(String configFieldName) {
        String valueStr = null;
        String[] valueSplit = null;

        if (aliases.containsKey(configFieldName)) {
            valueStr = aliases.get(configFieldName);
        } else if (scriptedAliases.containsKey(configFieldName)) {
            valueStr = scriptedAliases.get(configFieldName);
        }

        if (valueStr != null) {
            valueSplit = parseLine(valueStr.trim());
        }

        return valueSplit;
    }
}