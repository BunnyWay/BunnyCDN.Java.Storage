package bunnycdn;

import com.fasterxml.jackson.annotation.*;

// Gracefully handle new paramaters (unlike before)
@JsonIgnoreProperties(ignoreUnknown = true)

public class BCDNObject {

    private long failIndex;
    private String guid;
    private String storageZoneName;
    private String path;
    private String objectName;
    private long length;
    private String lastChanged;
    private String serverID;
    private boolean isDirectory;
    private String userID;
    private String dateCreated;
    private String storageZoneID;
    private String checksum;
    private String replicatedZones;
    
    @JsonProperty("FailIndex")
    public long getFailIndex() {
        return failIndex;
    }

    @JsonProperty("Guid")
    public String getGUID() {
        return guid;
    }

    @JsonProperty("StorageZoneName")
    public String getStorageZoneName() {
        return storageZoneName;
    }

    @JsonProperty("Path")
    public String getPath() {
        return path;
    }

    @JsonProperty("ObjectName")
    public String getObjectName() {
        return objectName;
    }

    @JsonProperty("Length")
    public long getLength() {
        return length;
    }

    @JsonProperty("LastChanged")
    public String getLastChanged() {
        return lastChanged;
    }

    @JsonProperty("ServerId")
    public String getServerID() {
        return serverID;
    }

    @JsonProperty("IsDirectory")
    public boolean getIsDirectory() {
        return isDirectory;
    }

    @JsonProperty("UserId")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("DateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("StorageZoneId")
    public String getStorageZoneID() {
        return storageZoneID;
    }
    @JsonProperty("Checksum")
    public String getChecksum() {
        return checksum;
    }
    @JsonProperty("ReplicatedZones")
    public String getReplicatedZones() {
        return replicatedZones;
    }

}
