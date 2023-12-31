public class CDDriveManager {
    private Resource cdDrives;

    public CDDriveManager(int totalCDDrives) {
        this.cdDrives = new Resource(totalCDDrives);
    }

    public boolean requestCDDrives(int count) {
        return cdDrives.requestUnits(count);
    }

    public void releaseCDDrives(int count) {
        cdDrives.releaseUnits(count);
    }
}