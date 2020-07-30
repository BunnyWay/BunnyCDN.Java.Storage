# BunnyCDN.Java.Storage

The official Java library used for interacting with the BunnyCDN Storage API. 
We would like to thank [@doghouch](https://github.com/doghouch) for the development.

## Latest Release

Current Version: 1.0.3

- [Click here](https://github.com/BunnyWay/BunnyCDN.Java.Storage/releases/download/1.0.3/BCDN.jar) to download the latest release (with Jackson bundled).
- [Click here](https://github.com/BunnyWay/BunnyCDN.Java.Storage/releases/download/1.0.3/BCDN-NoDependencies.jar) to download the latest release (without Jackson bundled). You will need to import it later yourself (the version above includes Jackson 2.9.9.3).

## Usage

### Table of Contents

- [Intro](#initialization)
- [Get Storage Objects](#bcdnobject-getstorageobjectsstring-remotepath)
- [Upload Directory](#void-uploadfolderstring-localpath-string-remotepath)
- [Upload Object](#void-uploadobjectstring-localpath-string-remotepath)
- [Download Object](#void-downloadobjectstring-remotepath-string-localpath)
- [Delete Object](#void-deleteobjectstring-remotepath)

### Initialization

This client requires Java 7+. The JAR comes bundled with all the required dependencies (Jackson Core/Databind/Annotations @ https://github.com/FasterXML).

Having said that, before you delve into any of the examples, the BCDNStorage object must be initialized. The following constructor will be depreciated eventually; it exists merely for compatibility reasons.

	BCDNStorage test = new BCDNStorage("YOUR_ZONE_NAME", "YOUR_API_KEY");

**NEW**: You can now specify the "main" region of your zone. By default, when no location is specified, this module assumes that your zone is part of the `de` region (if your zone isn't replicated or you've enabled the replication feature on an old storage zone -> there is no need to specify a location).

With that said, the general format is below:

	BCDNStorage test = new BCDNStorage("YOUR_ZONE_NAME", "YOUR_API_KEY", null);

Example - if the main region on your replicated zone is in New York, use the following:

	BCDNStorage test = new BCDNStorage("YOUR_ZONE_NAME", "YOUR_API_KEY", "ny");

_Note: If you enter an invalid key or zone, an exception will be thrown and it must be caught._

[Return to top &lsh;](#bunnycdnjavastorage)

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

[Return to top &lsh;](#bunnycdnjavastorage)

### (void) .uploadFolder(String localPath, String remotePath) 

This function is still being tested. With that said, it allows you to upload any local folder to your storage zone. For example, the following code:

	test.uploadObject("C:\\Users\\Username\\Desktop\\YourFolder\\", "/");

will upload "YourFolder/" to the root directory of your storage zone.

[Return to top &lsh;](#bunnycdnjavastorage)

### (void) .uploadObject(String localPath, String remotePath)

This function allows you to upload any local file to your storage zone. For example, the following code:

	test.uploadObject("C:\\Users\\Username\\Desktop\\logo.png", "logo.png");

will upload "logo.png" to the root directory of your storage zone.

[Return to top &lsh;](#bunnycdnjavastorage)

### (void) .downloadObject(String remotePath, String localPath)

This function is self explanatory once you've used the uploadObject() call. 

	test.downloadObject("style.css", "C:\\Users\\Username\\Desktop\\style.css");

The code above will download "style.css" from the root directory of your storage zone to your computer.

[Return to top &lsh;](#bunnycdnjavastorage)

### (void) .deleteObject(String remotePath)

This function will delete a file/folder regardless of whether it exists or not. It is the equivelant of running `rm -rf` on a file/folder.

	test.deleteObject("style.css")

The code above will remove the file we uploaded previously ("style.css") from the root directory of our storage zone. This action is **irreversible**.

[Return to top &lsh;](#bunnycdnjavastorage)
