
public class ModemManager {
    private Resource modems;

    public ModemManager(int totalModems) {
        this.modems = new Resource(totalModems);
    }

    public boolean requestModems(int count) {
        return modems.requestUnits(count);
    }

    public void releaseModems(int count) {
        modems.releaseUnits(count);
    }
}
