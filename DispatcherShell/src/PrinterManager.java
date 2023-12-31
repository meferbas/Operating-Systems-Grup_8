public class PrinterManager {
    private Resource printers;

    public PrinterManager(int totalPrinters) {
        this.printers = new Resource(totalPrinters);
    }

    public boolean requestPrinters(int count) {
        return printers.requestUnits(count);
    }

    public void releasePrinters(int count) {
        printers.releaseUnits(count);
    }
}