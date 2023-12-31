public class ScannerManager {
    private Resource scanners;

    public ScannerManager(int totalScanners) {
        this.scanners = new Resource(totalScanners);
    }

    public boolean requestScanners(int count) {
        return scanners.requestUnits(count);
    }

    public void releaseScanners(int count) {
        scanners.releaseUnits(count);
    }
}
