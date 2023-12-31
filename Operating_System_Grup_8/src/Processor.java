

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;


public class Processor implements IProcessor {
	private ISpecialProcess currentProcess;

	public Processor() {
		this.currentProcess = null;
	}

	private void runProcess(ISpecialProcess process, int currentTime, String sendedMessage) {
	    // İşlem bilgilerini içeren mesajı oluştur
	    String message = generateProcessMessage(process);

	    // İşlem sonlandırıldıysa ve gönderilen mesaj varsa, bu mesajı yazdır
	    if (process.getStatement() == Statement.Terminated && sendedMessage != null && !sendedMessage.isEmpty()) {
	        System.out.println(message);
	    }

	    try {
	        // İşlemi ProcessBuilder ile çalıştır
	        executeProcessBuilder(process, message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // İşlem süresini azalt
	    process.decreaseBurstTime();

	    // İşlem süresi bittiğinde işlemi sonlandır
	    if (process.getBurstTime() <= 0) {
	        process.setStatement(Statement.Terminated);
	        // Gerekli diğer sonlandırma işlemleri
	    }
	}


	// Gönderilen prosesi çalıştırır.
	@Override
	public void run(ISpecialProcess process, int currentTime) {
        if (process == null) {
            return;
        }

        // Mevcut işlemi güncelle ve çalıştır
        this.currentProcess = process;
        process.setStatement(Statement.Running);
        process.setStatus("RUNNING"); // İşlem çalıştığında durumu güncelle

        // İşlem süresini kontrol et ve gerekirse işlemi sonlandır
        process.decreaseBurstTime();
        if (process.getBurstTime() <= 0) {
            process.setStatement(Statement.Terminated);
            process.setStatus("COMPLETED"); // İşlem tamamlandığında durumu güncelle
            runProcess(process, currentTime, "sonlandi");
        } else {
            // İşlem çalışıyorsa durumu ve süreyi güncelle
            process.setStatus("RUNNING");
            runProcess(process, currentTime, "basladi");
        }
    }

	private String generateProcessMessage(ISpecialProcess process) {
	    // Format stringini oluştururken tüm bilgileri içerecek şekilde ayarlayın
		 
	    String format = "%-5d %-6d %-9s %-7d %-6d %-3d %-3d %-6d %-2d %s";
	    
	    // ProcessInfo'dan gelen verileri kullanarak formatlanmış stringi oluşturun
	    String message = String.format(format,
	        process.getPid(),
	        process.getArrivalTime(), // getArrivalTime metodunun tanımlı olduğundan emin olun
	        process.getPriority().toString(), // Enum değerini String'e çevirin
	        process.getCpuTime(),
	        process.getMemory(),
	        process.getRequiredPrinters(),
	        process.getRequiredScanners(),
	        process.getRequiredModems(),
	        process.getRequiredCDDrives(),
	        process.getStatus() != null ? process.getStatus().toString() : "N/A" // getStatus null kontrolü
	    );

	    // Nihai mesajı döndür
	    return message;
	}


	private void executeProcessBuilder(ISpecialProcess process, String message) throws Exception {
	    ProcessBuilder processBuilder = process.getProcessBuilder();
	    processBuilder.command().add(message);
	    Process p = processBuilder.start();
	    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String s;
	    while ((s = in.readLine()) != null) {
	        System.out.println(s);
	    }
	    processBuilder.command().remove(3);
	    p.destroyForcibly();
	}

}
