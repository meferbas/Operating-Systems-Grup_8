

public enum Statement {
    New,        // Prosesin oluştuğu durumu
    Ready,      // Prosesin hazır olduğu durum
    Running,    // Proses çalıştığı durum
    Suspended,  // Prosesin askıya alındığı durum
    Resumed,    // Prosesin devam ettirildiği durum
    Terminated, // Prosesin sonlandırıldığı durum
    TimeOut;    // Prosesin zamanaşımına uğradığı durum
}