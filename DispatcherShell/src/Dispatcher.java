

import java.util.List;
import java.util.ArrayList;
public class Dispatcher implements IDispatcher {
	private IProcessor processor;
	private IProcessReader processReader;
	private IProcessQueue realTimeQueue;
	private IProcessQueue highestQueue;
	private IProcessQueue mediumQueue;
	private IProcessQueue lowestQueue;
	private int currentTime;
    private List<ProcessInfo> processLog = new ArrayList<>();
    private Printer printerManager;
    private Scanner scannerManager;
    private Modem modemManager;
    private CDDrive cdDriveManager;
    
    private boolean allQueuesAndReaderAreEmpty() {
        return realTimeQueue.isEmpty() && highestQueue.isEmpty() &&
               mediumQueue.isEmpty() && lowestQueue.isEmpty() && 
               processReader.isEmpty();
    }
	

	public Dispatcher(IProcessor processor, IProcessReader processReader) {
		this.processor = processor;
		this.processReader = processReader;
		this.currentTime = 0;
		this.realTimeQueue = new ProcessQueue();
		this.highestQueue = new ProcessQueue();
		this.mediumQueue = new ProcessQueue();
		this.lowestQueue = new ProcessQueue();
		
        this.printerManager = new Printer(2); // 2 Yazıcı
        this.scannerManager = new Scanner(1); // 1 Tarayıcı
        this.modemManager = new Modem(1);    // 1 Modem
        this.cdDriveManager = new CDDrive(2); // 2 CD Sürücü
	}

	// Önceliği en yüksek olan prosesi döndürür.
	private ISpecialProcess getAppropriateProcess() {
		if (!realTimeQueue.isEmpty())
			return realTimeQueue.getFirstItem();
		else if (!highestQueue.isEmpty())
			return highestQueue.dequeue();
		else if (!mediumQueue.isEmpty())
			return mediumQueue.dequeue();
		else if (!lowestQueue.isEmpty())
			return lowestQueue.dequeue();
		else
			return null;
	}

	// Verilen prosesi kendisine uygun olan kuyruğa yerleştirir.
	private void queueProcess(ISpecialProcess process) {
		if (process.getPriority() == Priority.One)
			realTimeQueue.enqueue(process);
		else if (process.getPriority() == Priority.Two)
			highestQueue.enqueue(process);
		else if (process.getPriority() == Priority.Three)
			mediumQueue.enqueue(process);
		else // (process.getPriority()==Priority.Lowest)
			lowestQueue.enqueue(process);
	}

	// Bütün kuyrukları gezip 20 saniye bekleme süresini aşan prosesleri
	// sonlandırır.
	private void terminateTimeOut() {
	    terminateTimeOutForQueue(highestQueue);
	    terminateTimeOutForQueue(mediumQueue);
	    terminateTimeOutForQueue(lowestQueue);
	}
	private void terminateTimeOutForQueue(IProcessQueue queue) {
	    var timeOutProcesses = queue.increaseWaitingTime();
	    while (!timeOutProcesses.isEmpty()) {
	        var process = timeOutProcesses.dequeue();
	        process.setStatement(Statement.TimeOut);
	        processor.run(process, currentTime);
	        queue.delete(process);
	    }
	}
	private MemoryManager memoryManager = new MemoryManager();

	private boolean allocateMemoryForProcess(ISpecialProcess process) {
	    return memoryManager.allocateMemory(process.getMemoryRequired());
	}

	private void releaseMemoryForProcess(ISpecialProcess process) {
	    memoryManager.releaseMemory(process.getMemoryRequired());
	}

	@Override
	public void start() {
	    while (true) {
	        IProcessQueue receivedProcesses = processReader.getProcesses(currentTime);
	        ISpecialProcess process;

	        // Yeni gelen prosesleri kontrol ediyor.
	        while (!receivedProcesses.isEmpty()) {
	            process = receivedProcesses.dequeue();
	            // Bellek ve diğer kaynakları tahsis etmeye çalış
	            if (allocateResourcesForProcess(process)) {
	                process.setStatement(Statement.Ready);
	                queueProcess(process);
	            } else {
	                // Kaynak yetersizse, prosesi askıya al
	                process.suspend();
	            }
	        }

	        // Uygun prosesi seç ve işle
	        process = getAppropriateProcess();
	        if (process != null) {
	            handleProcessExecution(process);
	        }

	        // Zamanı ve zaman aşımını güncelle
	        ++currentTime;
	        terminateTimeOut();

	        // Eğer tüm kuyruklar ve giriş kuyruğu boşsa döngüyü sonlandır
	        if (allQueuesAndReaderAreEmpty()) {
	            System.out.println("\nBütün prosesler işlendi.");
	            break;
	        }
	    }
	}



	  private boolean isMemoryAvailableFor(ISpecialProcess process) {
	        // Bellek durumunu kontrol et
	        return memoryManager.getAvailableMemory() >= process.getMemoryRequired();
	    }

	    private void updateProcessState(ISpecialProcess process) {
	        // Proses durumunu ve belleği güncelle
	        process.decreaseBurstTime();
	        if (process.getBurstTime() == 0) {
	            process.setStatement(Statement.Terminated);
	            memoryManager.releaseMemory(process.getMemoryRequired()); // Belleği serbest bırak
	        } else {
	            process.setStatement(Statement.Ready);
	            if (process.getPriority() != Priority.One) {
	                process.decreasePriority();
	                queueProcess(process); // Prosesi tekrar kuyruğa ekle
	            }
	        }
	    }

	    	
	    private void handleProcessExecution(ISpecialProcess process) {
	        if (process.getStatement() == Statement.Suspended) {
	            // Askıya alınmış prosesi devam ettir
	            process.resume();
	        }

	        process.setStatement(Statement.Running);
	        processor.run(process, currentTime); // Prosesi çalıştır

	        process.decreaseBurstTime();

	        if (process.getBurstTime() <= 0) {
	            process.setStatement(Statement.Terminated);
	            releaseResourcesForProcess(process); // Kaynakları serbest bırak
	            removeFromQueue(process); 
	        }else {
	            // Proses henüz bitmediyse ve tekrar kuyruğa eklenmesi gerekiyorsa
	            process.setStatement(Statement.Ready);
	            if (process.getPriority() != Priority.One) {
	                process.decreasePriority();
	                queueProcess(process); // Prosesi tekrar kuyruğa ekle
	            }
	        }
	    }
	    
	    private void removeFromQueue(ISpecialProcess process) {
	        // Prosesin önceliğine göre uygun kuyruktan çıkar
	        switch (process.getPriority()) {
	            case One:
	                realTimeQueue.delete(process);
	                break;
	            case Two:
	                highestQueue.delete(process);
	                break;
	            case Three:
	                mediumQueue.delete(process);
	                break;
	            case Four:
	                lowestQueue.delete(process);
	                break;
	        }
	    }
	    
	    private void releaseResourcesForProcess(ISpecialProcess process) {
	        // Belleği serbest bırak
	        memoryManager.releaseMemory(process.getMemoryRequired());

	        // Diğer kaynakları serbest bırakma işlemleri
	        // Eğer Printer, Scanner, Modem ve CDDrive yöneticileriniz varsa, burada bu kaynakları serbest bırakın.
	        // Örneğin:
	        printerManager.releaseUnits(process.getRequiredPrinters());
	        scannerManager.releaseUnits(process.getRequiredScanners());
	        modemManager.releaseUnits(process.getRequiredModems());
	        cdDriveManager.releaseUnits(process.getRequiredCDDrives());
	    }

	    
	    private boolean allocateResourcesForProcess(ISpecialProcess process) {
	        // Bellek tahsisi
	        if (!memoryManager.allocateMemory(process.getMemoryRequired())) {
	            System.out.println(process.getPid() + "    Hata: Gerçek zamanlı process 64MB'tan daha fazla bellek talep ediyor. Process Silindi");
	            return false;
	        }
	        

	        // Yazıcı tahsisi
	        if (!printerManager.requestUnits(process.getRequiredPrinters())) {
	            System.out.println(process.getPid() + "    Hata: Yeterli yazıcı yok. Process Silindi");
	            memoryManager.releaseMemory(process.getMemoryRequired());
	            return false;
	        }

	        // Tarayıcı tahsisi
	        if (!scannerManager.requestUnits(process.getRequiredScanners())) {
	            System.out.println(process.getPid() + "    Hata: Yeterli taryıcı yok. Process Silindi");
	            printerManager.releaseUnits(process.getRequiredPrinters());
	            memoryManager.releaseMemory(process.getMemoryRequired());
	            return false;
	        }

	        // Modem tahsisi
	        if (!modemManager.requestUnits(process.getRequiredModems())) {
	            System.out.println(process.getPid() +("    HATA - Proses (960 MB) tan daha fazla bellek talep ediyor – proses silindi"));
	            scannerManager.releaseUnits(process.getRequiredScanners());
	            printerManager.releaseUnits(process.getRequiredPrinters());
	            memoryManager.releaseMemory(process.getMemoryRequired());
	            return false;
	        }

	        // CD sürücü tahsisi
	        if (!cdDriveManager.requestUnits(process.getRequiredCDDrives())) {
	            System.out.println("Hata: Yeterli CD sürücü yok. Proses ID: " + process.getPid());
	            modemManager.releaseUnits(process.getRequiredModems());
	            scannerManager.releaseUnits(process.getRequiredScanners());
	            printerManager.releaseUnits(process.getRequiredPrinters());
	            memoryManager.releaseMemory(process.getMemoryRequired());
	            return false;
	        }

	        // Tüm kaynaklar başarıyla tahsis edildi
	        return true;
	    }





}
