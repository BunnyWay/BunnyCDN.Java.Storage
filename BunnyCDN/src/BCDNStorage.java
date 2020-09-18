import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BCDNStorage extends Exception {

	private String VERSION = "1.0.3";
	private String BASE_URL = "https://storage.bunnycdn.com";
	private String nameOfZone;
	private String apiKey;
	private static LinkedList<String> tempFiles = new LinkedList<String>();

	public BCDNStorage(String nameOfZone, String apiKey) {
		this(nameOfZone, apiKey, "de");
	}

	public BCDNStorage(String nameOfZone, String apiKey, String region) {
		this.apiKey = apiKey;
		this.nameOfZone = nameOfZone;
		region = region.toLowerCase();
		if (region != null && !region.equals("de")) {
			BASE_URL = "https://" + region + ".storage.bunnycdn.com";
		}
	}

	private static void listAllFiles(String localPath) throws Exception {
		try {
			File directory = new File(localPath);
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					listAllFiles(file.getCanonicalPath());
				} else {
					tempFiles.add(file.getCanonicalPath().replace("\\", "/"));
				}
			}
		} catch (Exception e) {
			throw new IOException("Directory specified does not exist");
		}
	}

	public void uploadFolder(String localPath, String remotePath) throws Exception {
		String toReturn = "";
		// Replacing backward slashes with forward ones
		String temp = localPath.replace("\\", "/");
		// Getting the "main directory"
		String[] temp_2 = temp.split("/");
		Collections.reverse(Arrays.asList(temp_2));
		tempFiles.clear();
		listAllFiles(temp);
		// Looping through files
		for (String file : tempFiles) {
			String finalRemotePath = "";
			String tempUploadPath = file.substring(file.indexOf(temp_2[0]));
			// Normalizing directory naming
			if (remotePath.length() == 0) {
				remotePath = "/";
			}
			if ((remotePath.charAt(remotePath.length() - 1) + "").equals("/")) {
				finalRemotePath = remotePath;
			} else {
				finalRemotePath = remotePath + "/";
			}
			// Upload object
			uploadObject(file, finalRemotePath + tempUploadPath);
		}
	}

	public void uploadObject(String localPath, String remotePath) throws Exception {
		String toReturn = "";
		// Send request
		File file = new File(localPath);
		toReturn = sendRequest(normalizePath(remotePath), "PUT", file, false, "");
	}

	public BCDNObject[] getStorageObjects(String remotePath) throws Exception {
		String toReturn = "";
		toReturn = sendRequest(normalizePath(remotePath), "GET", null, false, "");
		return Converter.fromJsonString(toReturn);
	}

	public void deleteObject(String remotePath) throws Exception {
		String toReturn = "";
		toReturn = sendRequest(normalizePath(remotePath), "DELETE", null, false, "");
	}

	public void downloadObject(String remotePath, String localPath) throws Exception {
		String toReturn = "";
		toReturn = sendRequest(normalizePath(remotePath), "GET", null, true, localPath);
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
		String temp[] = url.split("/");
		for (int a = 0; a < temp.length; a++) {
			temp[a] = java.net.URLEncoder.encode(temp[a], "UTF-8").replace("+", "%20");
		}
		url = String.join("/", temp);
		HttpsURLConnection req = (HttpsURLConnection) (new URL(BASE_URL + "/" + nameOfZone + "/" + url))
				.openConnection();
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
			byte[] buffer1 = new byte[4096];
			// read byte by byte until end of stream
			while ((i = is.read(buffer1)) >= 0) {
				os.write(buffer1, 0, i);
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
				fs.close();
			} else {
				in = new BufferedReader(new InputStreamReader(req.getInputStream()));
				while ((inputLine = in.readLine()) != null) {
					resp.append(inputLine);
				}
				in.close();
			}
			break;
		}
		req.getResponseCode();
		return resp.toString();
	}

}
