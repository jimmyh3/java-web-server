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
    private List<String> directoryIndexes;

    public HttpdConf (String fileName) throws FileNotFoundException {
        super(fileName);

        serverRoot = null;
        listen = 8080;
        documentRoot = null;
        logFile = null;
        aliases = new HashMap<>();
        scriptedAliases = new HashMap<>();
        accessFileName = ".htaccess";
        directoryIndexes = new ArrayList<>();
        otherDirectives = new HashMap<>();
    }

    public String getServerRoot() { return serverRoot; }
    public int getListenPort() { return listen; }
    public String getDocumentRoot() { return documentRoot; }
    public String getLogFile() { return logFile; }
    public String getAlias(String alias) { return aliases.get(alias); }
    public String getScriptedAlias(String scriptedAlias) { return scriptedAliases.get(scriptedAlias); }
    public String getAccessFileName() { return accessFileName; }
    public List<String> getDirectoryIndexes() { return directoryIndexes; }
    public String getOtherDirectives(String directive) { return otherDirectives.get(directive); }

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
                    directoryIndexes.add(configKeyVal[i]);
                }
            default:
                otherDirectives.put(directive, configKeyVal[1]);
        }
    }
}