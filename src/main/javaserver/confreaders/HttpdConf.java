package main.javaserver.confreaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpdConf extends ConfigurationReader {

    private Map<String, String> settings;
    private Map<String, String> aliases;
    private Map<String, String> scriptedAliases;

    public HttpdConf (String fileName) throws FileNotFoundException {
        super(fileName);

        settings = new HashMap<>();
        aliases = new HashMap<>();
        scriptedAliases = new HashMap<>();
    }


    @Override
    public void load() throws IOException {
        String readLine = "";

        while ((readLine = fileReader.readLine()) != null) {
            readLine = readLine.trim();

            // Ignore comment lines
            if (!readLine.equals("") && readLine.charAt(0) != '#') {
                String[] configKeyVal = parseLine(readLine);

                storeHandleConfigAliasTypes(configKeyVal);
                storeHandleConfigDefaultTypes(configKeyVal);
            }
        }

    }

    private void storeHandleConfigAliasTypes(String[] configKeyVal) {
        String configKey = configKeyVal[0];

        if (configKey.equals("Alias") || configKey.equals("ScriptAlias")) {
            Map<String, String> aliasTypeMap = (configKey.equals("Alias")) ? aliases : scriptedAliases;
            String alias = configKeyVal[1];
            String directory = configKeyVal[2];

            aliasTypeMap.put(alias, directory);
        }
    }

    private void storeHandleConfigDefaultTypes(String[] configKeyVal) {
        String configFieldName = configKeyVal[0];

        if (!configFieldName.equals("Alias") && !configFieldName.equals("ScriptAlias")) {
            String configFieldValue = configKeyVal[1];
            settings.put(configFieldName, configFieldValue);
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

        if (settings.containsKey(configFieldName)) {
            valueStr = settings.get(configFieldName);
        } else if (aliases.containsKey(configFieldName)) {
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