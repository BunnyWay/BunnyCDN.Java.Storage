package BCDNTests;

import static org.junit.jupiter.api.Assertions.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.File;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import BCDNStorageAPI.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class BCDNStorageTest {
	
	private String apiKey = "";
	private String zone = "";
	
	private BCDNStorage client = new BCDNStorage(zone, apiKey);
	
	void uploadSetup() throws Exception {
		URL url = getClass().getResource("dummy.txt");
		client.uploadObject(url.getPath(), "toDelete/dummy.txt");
		
		if (client.getStorageObjects("toDelete/").length != 1) {
			fail("Could not upload test directory.");
		}
	}
	
	@Test
    @Order(1)
	void testFileList() {
		try {
			uploadSetup();
			
			BCDNObject[] data = client.getStorageObjects("toDelete/");
			
			for (BCDNObject item : data) {
				if (item.getObjectName().equals("dummy.txt")) {
					return;
				}
			}
			
			fail("File list does not contain the correct file.");
			
		} catch (Exception e) {
			fail("Exception: " + e);
		}
	}
	
	@Test 
	@Order(2)
	void testDownloadFIle() {
		try {
			URL url = getClass().getResource("");
			client.downloadObject("toDelete/dummy.txt", url.getPath() + "dummy2.txt");
		    Path path = FileSystems.getDefault().getPath(url.getPath(), "dummy2.txt");
		    boolean flag = false;
			List<String> temp = Files.readAllLines(path);
			
			for (String line : temp) {
				if (line.contains("Hello, world")) {
					flag = true;
				}
			}
			
			if (!flag) {
				fail("File downloaded does not match uploaded file.");
			}
			
			return;

		} catch (Exception e) {
			fail("Exception: " + e);
		}
	}
	
	
	@Test
	@Order(3)
	void testDelete() {
		try {				
			client.deleteObject("toDelete/");
			
			BCDNObject[] files = client.getStorageObjects("/");
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].getIsDirectory() && files[i].getObjectName() == "toDelete") {
					fail("Folder delete failed.");
				}
			}
			
			return;
			
		} catch (Exception e) {
			fail("Exception: " + e);
		}
		
	}
	
	// more unit tests TBD

}
