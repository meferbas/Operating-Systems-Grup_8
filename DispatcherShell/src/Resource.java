public class Resource {
    private int totalUnits;
    private int availableUnits;

    public Resource(int totalUnits) {
        this.totalUnits = totalUnits;
        this.availableUnits = totalUnits;
    }

    public boolean requestUnits(int units) {
        if (units <= availableUnits) {
            availableUnits -= units;
            return true;
        }
        return false;
    }

    public void releaseUnits(int units) {
        // Negatif birim sayısının serbest bırakılmasını engelle
        if (units < 0) {
            throw new IllegalArgumentException("Serbest bırakılan birim sayısı negatif olamaz.");
        }
        
        // Serbest bırakılan birimlerin toplam birim sayısını aşmasını engelle
        availableUnits += units;
        if (availableUnits > totalUnits) {
            availableUnits = totalUnits;
        }
    }

    public int getAvailableUnits() {
        return availableUnits;
    }
}
