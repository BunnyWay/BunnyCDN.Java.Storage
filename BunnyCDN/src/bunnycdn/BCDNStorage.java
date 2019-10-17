package bunnycdn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import bunnycdn.Converter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BCDNStorage extends Exception {

    private String VERSION = "1.0.0";
    private String BASE_URL = "https://storage.bunnycdn.com";
    private String nameOfZone;
    private String apiKey;

    public BCDNStorage(String nameOfZone, String apiKey) {
        this.apiKey = apiKey;
        this.nameOfZone = nameOfZone;
    }

    public void uploadObject(String localPath, String remotePath) throws Exception {
        String toReturn = "";
        try {
            // Send request
            File file = new File(localPath);
            toReturn = sendRequest(normalizePath(remotePath), "PUT", file, false, "");
        } catch (Exception e) {
            if ((e + "").contains("FileNotFound")) {
                // Sub. old exception with the exception below
                throw new Exception("File/Folder Not Found");
            } else if ((e + "").contains("Invalid Path")) {
                // Sub. old exception
                throw new Exception("Invalid Path (remote)");
            } else {
                // Forward any other exceptions
                throw new Exception(e);
            }
        }
    }

    public BCDNObject[] getStorageObjects(String remotePath) throws Exception {
        String toReturn = "";
        try {
            // Send request
            toReturn = sendRequest(normalizePath(remotePath), "GET", null, false, "");
        } catch (Exception e) {
            if ((e + "").contains("FileNotFound")) {
                // Sub. old exception with the exception below
                throw new Exception("File/Folder Not Found");
            } else {
                // Forward any other exceptions
                throw new Exception(e);
            }
        }
        return Converter.fromJsonString(toReturn);
    }

    public void deleteObject(String remotePath) throws Exception {
        String toReturn = "";
        try {
            toReturn = sendRequest(normalizePath(remotePath), "DELETE", null, false, "");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void downloadObject(String remotePath, String localPath) throws Exception {
        String toReturn = "";
        try {
            toReturn = sendRequest(normalizePath(remotePath), "GET", null, true, localPath);
        } catch (Exception e) {
            if ((e + "").contains("FileNotFound")) {
                throw new Exception("File/Folder Not Found");
            } else {
                throw new Exception(e);
            }
        }
    }

    private String normalizePath(String path) {
        if (path.length() > 0) {
            if ((path.charAt(0) + "").equals("/")) {
                return path.replaceFirst("/", "");
            }
        }
        return path;
    }

    private String sendRequest(String url, String method, File arg, boolean arg2, String arg3) throws Exception {
        // Declarations
        String inputLine;
        BufferedReader in;
        StringBuffer resp = new StringBuffer();
        HttpsURLConnection req = (HttpsURLConnection) (new URL(BASE_URL + "/" + nameOfZone + "/" + url)).openConnection();
        req.setRequestMethod(method);
        // For analytical purposes
        req.setRequestProperty("User-Agent", "Java-BCDN-Client-" + VERSION);
        // Authentication Bearer
        req.setRequestProperty("AccessKey", apiKey);
        // Handle upload/delete
        req.setDoOutput(true);
        switch (method) {
            case "PUT":
                req.setRequestProperty("Accept", "*/*");
                req.setDoInput(true);
                BufferedOutputStream os = new BufferedOutputStream(req.getOutputStream());
                BufferedInputStream is = new BufferedInputStream(new FileInputStream(arg));
                int i;
                // read byte by byte until end of stream
                while ((i = is.read()) > 0) {
                    os.write(i);
                }
                is.close();
                os.close();
                resp.append("");
                break;
            case "DELETE":
                req.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                resp.append("");
                break;
            case "GET":
                if (arg2) {
                    FileOutputStream fs = new FileOutputStream(arg3);
                    int bytesRead = -1;
                    byte[] buffer = new byte[4096];
                    while ((bytesRead = req.getInputStream().read(buffer)) != -1) {
                        fs.write(buffer, 0, bytesRead);
                    }
                } else {
                    in = new BufferedReader(new InputStreamReader(req.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        resp.append(inputLine);
                    }
                    in.close();
                }
                break;
        }
        // 400 -> Bad Request, 401 -> Authentication Failed, 500 -> Server Error
        switch (req.getResponseCode()) {
            case 400:
                throw new Exception("Invalid Path");
            case 401:
                throw new Exception("Unauthorized");
            case 500:
                throw new Exception("Server Error");
        }
        return resp.toString();
    }

}
