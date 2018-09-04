package main.javaserver.confreaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MimeTypes extends ConfigurationReader {
    
    private Map<String, String> mimeTypes;

    public MimeTypes (String fileName) throws FileNotFoundException {
        super(fileName);

        mimeTypes = new HashMap<>();
    }

    @Override
    public void load() throws IOException {
        String readLine = null;

        while ((readLine = fileReader.readLine()) != null ) {
            readLine = readLine.trim();

            if (!readLine.equals("") && readLine.charAt(0) != '#') {
                storeHandleMimeTypes(mimeTypes, readLine);
            }
        }
    }

    /**
     * This takes in the line read from the mime type file, splits it by whitespace,
     * maps each file extension to mime type using the given map.
     * 
     * <p><b>Note that any mime type without a file extension is IGNORED.</b></p>
     * 
     * @param _mimeTypes Map to store the mapping of file extensions to mime types in, 
     * which are extrapolated from the given string argument.
     * @param readLine String line read from mime type file.
     */
    private void storeHandleMimeTypes(Map<String, String> _mimeTypes, String readLine) {
        String[] readLineSplit = parseLine(readLine);
        String mimeType = readLineSplit[0];

        for (int i = 1; i < readLineSplit.length; i++) {
            String mimeFileExt = readLineSplit[i];

            _mimeTypes.put(mimeFileExt, mimeType);
        }
    }

    /**
     * Returns the mime type associated with the file type.
     * @param fileName The file to determine the mime type of.
     * @return The mime type mapped to this file.
     */
    public String getMimeType(String fileName) {
        String[] fileSplit = fileName.split("\\.");
        String fileExtension = fileSplit[fileSplit.length-1];
        
        return mimeTypes.get(fileExtension);
    }

}