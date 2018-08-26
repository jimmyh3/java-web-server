package main.javaserver.confreaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Htpassword extends ConfigurationReader {

    private Map<String, String> users;

    public Htpassword(String fileName) throws FileNotFoundException {
        super(fileName);

        users = new HashMap();
    }

    @Override
    public void load() throws IOException {
        String readLine = "";

        while ((readLine = fileReader.readLine()) != null) {
            readLine = readLine.trim();

            if (readLine.charAt(0) != '#' && readLine.contains("{SHA}")) {
                String[] userPassPair= readLine.replace("{SHA}", "").split(":");
                String username = userPassPair[0].trim();
                String passEncoded = userPassPair[1].trim();
                
                users.put(username, passEncoded);
            }
        }
    }

    public boolean isAuthorized(String userPassEncoded) throws NoSuchAlgorithmException {
        String userPassDecoded = new String(Base64.getDecoder().decode(userPassEncoded), "UTF-8");
        String[] userPassPair = userPassDecoded.split(":");
        String username = userPassPair[0].trim();
        String password = userPassPair[1].trim();

        return isAuthorized(username, password);
    }

    public boolean isAuthorized(String username, String password) throws NoSuchAlgorithmException {
        boolean isAuthorized = false;

        if (users.containsKey(username)) {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            String passwordHashed = Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
            String passwordStored = users.get(username);

            isAuthorized = passwordHashed.equals(passwordStored);
        } else {
            System.out.println("Htpassword: no such user found.");
        }

        return isAuthorized;
    }

}