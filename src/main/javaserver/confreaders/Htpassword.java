package main.javaserver.confreaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    public static String[] getUserNamePass(String userPassEncoded) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // TODO: take in Authorization Type such as 'Basic', 'Bearer', etc. See mdn on Authorization.
        
        String userPassDecoded = new String(Base64.getDecoder().decode(userPassEncoded), "UTF-8");
        String[] userPassPair = userPassDecoded.split(":");
        String username = userPassPair[0].trim();
        String password = userPassPair[1].trim();
        userPassPair[0] = username;
        userPassPair[1] = password;

        return userPassPair;
    }

    public boolean isAuthorized(String userPassEncoded) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String[] userPassPair = getUserNamePass(userPassEncoded);
        String username = userPassPair[0];
        String password = userPassPair[1];

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