

public class Program {
    public static void main(String[] args) {
    	System.out.printf("%-5s %-6s %-4s %-7s %-6s %-3s %-3s %-6s %-2s %s%n", 
		        "Pid", "Varış", "Önc.", "CPU", "MB", "PRN", "SCN", "MODEM", "CD", "Durum");
    	System.out.println("===============================================================");
        MemoryManager memoryManager = new MemoryManager(); // MemoryManager nesnesi oluşturuluyor
        IProcessReader processReader = new ProcessReader(args[0], memoryManager); // MemoryManager geçiliyor
        IProcessor processor = new Processor();
        IDispatcher dispatcher = new Dispatcher(processor, processReader);
        

        dispatcher.start();
    }
}
