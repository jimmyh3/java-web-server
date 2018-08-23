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
            if (readLine.charAt(0) != '#') {
                String[] configKeyVal = readLine.split(" ", 2);
                String   configKey = configKeyVal[0];
                String   configVal = configKeyVal[1];

                storeHandleConfigAliasTypes(configKey, configVal);
                storeHandleConfigDefaultTypes(configKey, configVal);
            }
        }

    }

    private void storeHandleConfigAliasTypes(String configKey, String configVal) {
        if (configKey.equals("Alias") || configKey.equals("ScriptAlias")) {
            Map<String, String> aliasTypeMap = (configKey.equals("Alias")) ? aliases : scriptedAliases;
            String[] aliasDirectoryPair = configVal.split(" ", 2);
            String alias = aliasDirectoryPair[0];
            String directory = aliasDirectoryPair[1];

            aliasTypeMap.put(alias, directory);
        }
    }

    private void storeHandleConfigDefaultTypes(String configKey, String configVal) {
        if (!configKey.equals("Alias") && !configKey.equals("ScriptAlias")) {
            settings.put(configKey, configVal);
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
            valueSplit = valueStr.split(" ");
        }

        return valueSplit;
    }
}