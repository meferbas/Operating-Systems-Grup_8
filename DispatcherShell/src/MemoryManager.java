public class MemoryManager {
    private static final int TOTAL_MEMORY = 1024;
    private int availableMemory;

    public MemoryManager() {
        this.availableMemory = TOTAL_MEMORY;
    }

    public boolean allocateMemory(int memoryRequired) {
        if (memoryRequired <= availableMemory) {
            availableMemory -= memoryRequired;
            return true;
        }
        return false;
    }

    public void releaseMemory(int memoryReleased) {
        availableMemory += memoryReleased;
    }

    public int getAvailableMemory() {
        return availableMemory;
    }
}
