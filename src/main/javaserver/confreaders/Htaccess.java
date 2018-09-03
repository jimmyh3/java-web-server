package main.javaserver.confreaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.javaserver.confreaders.Htpassword;

public class Htaccess extends ConfigurationReader {
    
    private Htpassword userFile;
    private String authType;
    private String authName;
    private String require; 

    public Htaccess(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public String getAuthType() {
        return authType;
    }

    public String getAuthName() {
        return authName;
    }

    public String getRequire() {
        return require;
    }

    @Override
    public void load() throws IOException {
        String readLine = null;

        while ((readLine = fileReader.readLine()) != null) {
            readLine = readLine.trim();

            if (readLine.charAt(0) != '#') {
                String[] readLineSplit = parseLine(readLine);
                String fieldName = readLineSplit[0];
                String fieldValue = readLineSplit[1];
                    
                if (fieldName.equals("AuthUserFile")) {
                    userFile = initializeHtpassword(fieldValue);
                } else if (fieldName.equals("AuthType")) {
                    authType = fieldValue;
                } else if (fieldName.equals("AuthName")) {
                    authName = fieldValue;
                } else if (fieldName.equals("Require")) {
                    require = fieldValue;
                }
            }
        }

    }

    private Htpassword initializeHtpassword(String htpasswordFileName) {
        //TODO: Implement this.
        Htpassword htpassword = new Htpassword(htpasswordFileName);
        htpassword.load();
        
        return htpassword;
    }

}