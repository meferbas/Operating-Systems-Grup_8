import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class ProcessReader implements IProcessReader {
	private IProcessQueue processes;
	private MemoryManager memoryManager;
	   public ProcessReader(String path, MemoryManager memoryManager) {
	        this.processes = new ProcessQueue();
	        this.memoryManager = memoryManager; // MemoryManager atanması
	        readFile(path);
	    }
	// Metin dosyasını okur ve bütün prosesleri oluşturup bir kuyruğa atar.
	   private void readFile(String path) {
		    try {
		        File file = new File(path);
		        if (file.exists()) {
		            Scanner scanner = new Scanner(file);
		            int count = 0;
		            while (scanner.hasNextLine()) {
		                String line = scanner.nextLine();
		                line = line.replaceAll("\\s+", "");
		                String[] processInformations = line.split(",");

		                int destinationTime = Integer.parseInt(processInformations[0]);
		                Priority priority = Priority.values()[Integer.parseInt(processInformations[1])];
		                int burstTime = Integer.parseInt(processInformations[2]);
		                int memoryRequired = Integer.parseInt(processInformations[3]);
		                int requiredPrinters = Integer.parseInt(processInformations[4]);
		                int requiredScanners = Integer.parseInt(processInformations[5]);
		                int requiredModems = Integer.parseInt(processInformations[6]);
		                int requiredCDDrives = Integer.parseInt(processInformations[7]);

		                ISpecialProcess process = new SpecialProcess(count, destinationTime, priority, burstTime, 
		                        memoryRequired, requiredPrinters, 
		                        requiredScanners, requiredModems, 
		                        requiredCDDrives, this.memoryManager); // Örnek SpecialProcess kurucusu
		                process.setMemory(memoryRequired);
		                process.setArrivalTime(destinationTime);
		                process.setCPUTime(burstTime); // burstTime değerini ayarlayın

		                this.processes.enqueue(process);
		                count++;
		            }
		            scanner.close();
		        }
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}


	// Verilen varış zamanında gelen prosesleri döndürür. Ayrıca prosesleri içindeki
	// kuyruktan çıkarır.
	@Override
	public IProcessQueue getProcesses(int destinationTime) {
		IProcessQueue foundedProcesses = new ProcessQueue();
		IProcessQueue tmpQueue = this.processes.search(destinationTime);
		ISpecialProcess tmpProcess;
		while (!tmpQueue.isEmpty()) {
			tmpProcess = tmpQueue.dequeue();
			this.processes.delete(tmpProcess);
			foundedProcesses.enqueue(tmpProcess);
		}
		return foundedProcesses;
	}

	@Override
	public boolean isEmpty() {
		return processes.isEmpty();
	}

}
