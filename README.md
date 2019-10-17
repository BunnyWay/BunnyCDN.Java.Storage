# BunnyCDN.Java.Storage
The official Java library used for interacting with the BunnyCDN Storage API.

## Usage

This client requires Java 7+. Import the JAR and you're good to go!

Having said that, before you delve into any of the examples, the BCDNStorage object must be initialized: 

	BCDNStorage test = new BCDNStorage("YOUR_ZONE_NAME", "YOUR_API_KEY");

(If you enter an invalid key or zone, an exception will be called and it must be caught.)

### (BCDNObject) .getStorageObjects(String remotePath)

A new data type is created called "BCDNObject." In order to use it, follow the format below:

	BCDNObject newArray[] = test.getStorageObjects("test/");

The code above will store the file size, etc. For example, once called, you can check how many items there are in the directory specified with the following:

	newArray.length

With this information, you can easily list every file/folder with the following:

	for (int i = 0; i < newArray.length; i++) {
	    if (newArray[i].getIsDirectory()) {
	        System.out.println("Folder: " + newArray[i].getObjectName());
	    } else {
	        System.out.println("File: " + newArray[i].getObjectName());
	    }
	    System.out.println("- Size: " + newArray[i].getLength() + " bytes");
	    System.out.println("- Stored on server: " + newArray[i].getServerID());
	    System.out.println("- Last Modified: " + newArray[i].getLastChanged());
	    System.out.println("- Created on: " + newArray[i].getDateCreated());
	    System.out.println("");
	}


This should, assuming you specify a valid path and API key, output something similar to the output below:

	File: file_name.txt
	- Size: 1000 bytes
	- Stored on server: 39
	- Last Modified: 2019-10-15T23:09:39.081
	- Created on: 2019-10-15T23:09:39.081

	Folder: test
	- Size: 0 bytes
	- Stored on server: 0
	- Last Modified: 2019-10-15T23:09:50.368
	- Created on: 2019-10-15T23:09:50.368

_Note: if you speficy an invailid directory, an Exception will be thrown ("File/Folder Not Found")._

### (void) .uploadObject(String localPath, String remotePath)

This function allows you to upload any local file to your storage zone. For example, the following code:

	test.uploadObject("C:\\Users\\Username\\Desktop\\logo.png", "logo.png");

will upload "logo.png" to the root directory of your storage zone.

_Note: if you speficy an invailid directory or file, an Exception will be thrown ("File/Folder Not Found")._

### (void) .downloadObject(String remotePath, String localPath)

This function is self explanatory once you've used the uploadObject() call. 

	test.downloadObject("style.css", "C:\\Users\\Username\\Desktop\\style.css");

The code above will download "style.css" from the root directory of your storage zone to your computer.

_Note: if you speficy an invailid directory or file, an Exception will be thrown ("File/Folder Not Found")._

### (void) .deleteObject(String remotePath)

This function will delete a file/folder regardless of whether it exists or not. It is the equivelant of running "rm -rf" on a file/folder.

	test.deleteObject("style.css")

The code above will remove the file we uploaded previously ("style.css") from the root directory of our storage zone.
