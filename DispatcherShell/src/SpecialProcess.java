

public class SpecialProcess implements ISpecialProcess {
	private ProcessBuilder process;
	private int pid;
	private int destinationTime;
	private Priority priority;
	private int burstTime;
	private int waitingTime;
	private Statement statement;
	private int memoryRequired; // Bellek ihtiyacı
	  	private int requiredPrinters;
	    private int requiredScanners;
	    private int requiredModems;
	    private int requiredCDDrives;
	    private MemoryManager memoryManager;
	    
	    private int arrivalTime;
	    private String status;
	    private int cpuTime;
	    private int memory;
	    private int printers;
	    private int scanners;
	    private int modems;
	    private int cdDrives;
	    private Statement lastStatement;

	    
	    public SpecialProcess(int pid, int destinationTime, Priority priority, int burstTime, 
                int memoryRequired, int requiredPrinters, int requiredScanners, 
                int requiredModems, int requiredCDDrives, MemoryManager memoryManager) {
		this.pid = pid;
		this.destinationTime = destinationTime;
		this.priority = priority;
		this.burstTime = burstTime;
		this.waitingTime = 0;
		this.statement = Statement.New;
		this.requiredPrinters = requiredPrinters;
        this.requiredScanners = requiredScanners;
        this.requiredModems = requiredModems;
        this.requiredCDDrives = requiredCDDrives;
        this.memoryRequired = memoryRequired;
        this.memoryManager = memoryManager;

		process = new ProcessBuilder("java", "-jar", "./process.jar");
	}
    @Override
    public int getRequiredPrinters() {
        return requiredPrinters;
    }
    @Override
    public void decreaseBurstTime() {
        if (this.burstTime > 0) {
            this.burstTime--; // burstTime değerini 1 azalt
        }
    }
    @Override
    public int getRequiredScanners() {
        return requiredScanners;
    }

    @Override
    public int getRequiredModems() {
        return requiredModems;
    }
    
    public void setMemory(int memory) {
        this.memory = memory;
    }
    
    

    @Override
    public int getRequiredCDDrives() {
        return requiredCDDrives;
    }

    @Override
    public int getMemoryRequired() {
        return memoryRequired;
    }

	@Override
	public ProcessBuilder getProcessBuilder() {
		return this.process;
	}

	@Override
	public int getPid() {
		return this.pid;
	}

	@Override
	public int getDestinationTime() {
		return this.destinationTime;
	}

	@Override
	public Priority getPriority() {
		return this.priority;
	}

	// Prosesin önceliğini bir derece düşürür.
	@Override
	public void decreasePriority() {
		if (this.priority == Priority.Two) {
			this.priority = Priority.Three;
		} else if (this.priority == Priority.Three) {
			this.priority = Priority.Four;
		}
	}

	@Override
	public int getBurstTime() {
		return this.burstTime;
	}
	
	@Override
	public void setCPUTime(int burstTime) {
	    this.cpuTime = burstTime;
	}
	  @Override
	    public void setStatus(String status) {
	        this.status = status;
	    }


	// Prosesin bekleme süresini bir artırır. Eğer 20 olmuşsa true döndürür.
	@Override
	public boolean increaseWaitingTime() {
		this.waitingTime++;
		return this.waitingTime < 20 ? false : true;
	}
	
	public void setArrivalTime(int arrivalTime) {
	    this.arrivalTime = arrivalTime;
	}
	// Prosesin bekleme süresini sıfırlar.
	@Override
	public void resetWaitingTime() {
		this.waitingTime = 0;
	}

	@Override
	public Statement getStatement() {
		return this.statement;
	}

	@Override
	 public void setStatement(Statement statement) {
        if (this.statement != statement) {
            this.lastStatement = this.statement; // Mevcut durumu kaydet
            this.statement = statement;
            // Durum değişikliği olduğunda gerekli işlemler...
        }
    }
	public Statement getLastStatement() {
        return lastStatement;
    }
	

	public void suspend() {
	    this.setStatement(Statement.Suspended);
	    // Belleği serbest bırak
	    memoryManager.releaseMemory(this.getMemoryRequired());
	}
	public void resume() {
	    this.setStatement(Statement.Resumed);
	    // Belleği yeniden tahsis et
	    boolean isMemoryAllocated = memoryManager.allocateMemory(this.getMemoryRequired());
	    if (!isMemoryAllocated) {
	        // Bellek tahsis edilemezse prosesi askıya al
	        this.setStatement(Statement.Suspended);
	        System.out.println("Hata: Yeterli bellek yok. Proses askıya alındı. ID: " + this.getPid());
	    }
	}
	
    @Override
    public int getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getCpuTime() {
        return cpuTime;
    }

    @Override
    public int getMemory() {
        return this.memory;
    }

    @Override
    public int getPrinters() {
        return printers;
    }

    @Override
    public int getScanners() {
        return scanners;
    }

    @Override
    public int getModems() {
        return modems;
    }

    @Override
    public int getCdDrives() {
        return cdDrives;
    }
	


}
