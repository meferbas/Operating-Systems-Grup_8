public class ProcessInfo {
	private int pid;
    private String status;
    private int cpuTime;
    private int memory;
    private int printers;
    private int scanners;
    private int modems;
    private int cdDrives;
    private int arrivalTime; // Varsa bu özellik için
    private String priority; // Varsa bu özellik için

    public ProcessInfo(int pid, String status, int cpuTime, int memory, int printers, int scanners, int modems, int cdDrives, int arrivalTime, String priority) {
        this.pid = pid;
        this.status = status;
        this.cpuTime = cpuTime;
        this.memory = memory;
        this.printers = printers;
        this.scanners = scanners;
        this.modems = modems;
        this.cdDrives = cdDrives;
        this.arrivalTime = arrivalTime; // Yeni alan
        this.priority = priority;
    }
    
    public int getPrinters() {
        return printers;
    }

    public void setPrinters(int printers) {
        this.printers = printers;
    }

    public int getScanners() {
        return scanners;
    }

    public void setScanners(int scanners) {
        this.scanners = scanners;
    }

    public int getModems() {
        return modems;
    }

    public void setModems(int modems) {
        this.modems = modems;
    }

    public int getCdDrives() {
        return cdDrives;
    }

    public void setCdDrives(int cdDrives) {
        this.cdDrives = cdDrives;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
