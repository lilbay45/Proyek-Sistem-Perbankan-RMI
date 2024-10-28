# Aplikasi Bank Client-Server dengan Java RMI

## Pengantar RMI (Remote Method Invocation)
RMI di Java memungkinkan pemanggilan metode pada objek di JVM lain. Ini sangat berguna untuk aplikasi terdistribusi, seperti aplikasi client-server ini, yang membutuhkan komunikasi antar komponen di jaringan.

## Tentang Proyek
Proyek ini mengimplementasikan Aplikasi Bank Client-Server dengan Java RMI. Admin dapat melakukan cek saldo dan transfer dana antar akun dengan login yang aman.

### Struktur Proyek
- Antarmuka Bank (Bank.java): Mendefinisikan metode checkBalance dan transferFunds yang dapat diakses dari jarak jauh.
- Implementasi Bank (BankImpl.java): Mengimplementasikan logika cek saldo dan transfer dana.
- Server Bank (BankServer.java): Mendaftarkan layanan BankImpl ke registry RMI.
- Client Bank (BankClient.java): Antarmuka untuk login admin, cek saldo, dan transfer dana.

## Cara Menggunakan Proyek Ini

### Prasyarat
- JDK terinstal
- Pastikan javac dan java ada di PATH sistem.

### Langkah Pengaturan

1. Kompilasi Semua File
      javac Bank.java BankImpl.java BankServer.java BankClient.java
   

2. Jalankan RMI Registry
   Di terminal baru, jalankan:
      rmiregistry
   
   Biarkan terminal ini tetap terbuka.

3. Jalankan Server Bank
   Di terminal lain:
      java BankServer
   
   Server siap melayani permintaan client.

4. Jalankan Client Bank
   Di terminal berbeda:
      java BankClient
   
   Client akan meminta password admin ("informatika") untuk mengakses.

### Fitur
- Login Admin: Akses dibatasi hanya untuk admin.
- Cek Saldo: Admin dapat memeriksa saldo akun.
- Transfer Dana: Admin dapat melakukan transfer antar akun dengan pembaruan saldo langsung.

### Catatan
- Pastikan rmiregistry berjalan di port yang benar (default 1099).
- Untuk penggunaan jarak jauh, sesuaikan pengaturan registry dan IP di BankClient.java dan BankServer.java.

Tentu! Berikut adalah penjelasan tujuan dari keempat bagian kode yang Anda berikan, termasuk antarmuka Bank, implementasi BankImpl, server BankServer, dan klien BankClient.

### 1. Antarmuka `Bank`
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    double checkBalance(String accountNumber) throws RemoteException;
    String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException;
}
#### Tujuan:
- Definisi Layanan Remote: Antarmuka ini mendefinisikan kontrak untuk layanan perbankan yang dapat diakses dari klien secara remote.
- Metode yang Dapat Dipanggil:
  - checkBalance: Untuk memeriksa saldo akun tertentu.
  - transferFunds: Untuk mentransfer dana antara dua akun.

### 2. Implementasi `BankImpl`
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class BankImpl extends UnicastRemoteObject implements Bank {
    private HashMap<String, Double> accounts;

    public BankImpl() throws RemoteException {
        super();
        accounts = new HashMap<>();
        accounts.put("ACC123", 5000.00);
        accounts.put("ACC456", 3000.00);
        accounts.put("ACC789", 7000.00);
    }

    public double checkBalance(String accountNumber) throws RemoteException {
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber);
        } else {
            throw new RemoteException("Account not found.");
        }
    }

    public String transferFunds(String fromAccount, String toAccount, double amount) throws RemoteException {
        if (!accounts.containsKey(fromAccount) || !accounts.containsKey(toAccount)) {
            return "One or both accounts not found.";
        }

        if (accounts.get(fromAccount) < amount) {
            return "Insufficient funds in the account.";
        }

        accounts.put(fromAccount, accounts.get(fromAccount) - amount);
        accounts.put(toAccount, accounts.get(toAccount) + amount);
        return "Transfer of $" + amount + " from " + fromAccount + " to " + toAccount + " successful.";
    }
}
#### Tujuan:
- Implementasi Layanan Remote: Kelas ini mengimplementasikan antarmuka Bank, menyediakan logika untuk metode yang telah didefinisikan.
- Data Akun: Menggunakan HashMap untuk menyimpan data akun dan saldo, membuat simulasi sistem perbankan.
- Konstruktor: Menginisialisasi beberapa akun dengan saldo awal.
- Metode `checkBalance`: Mengembalikan saldo dari akun yang diberikan, atau melemparkan pengecualian jika akun tidak ditemukan.
- Metode `transferFunds`: Melakukan transfer dana antara dua akun, memeriksa keberadaan akun dan saldo yang cukup sebelum melakukan transfer.

### 3. Server `BankServer`
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BankServer {
    public static void main(String[] args) {
        try {
            BankImpl bank = new BankImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/BankService", bank);
            System.out.println("Bank Server is ready.");
        } catch (Exception e) {
            System.out.println("Bank Server failed: " + e);
        }
    }
}
#### Tujuan:
- Menjalankan Server RMI: Kelas ini memulai server yang mendaftarkan layanan perbankan di registry RMI.
- Membuat Instance `BankImpl`: Membuat objek dari kelas BankImpl, yang menyediakan logika untuk operasi perbankan.
- Registry RMI: Menciptakan registry pada port 1099, yang merupakan port default untuk layanan RMI.
- Mendaftarkan Layanan: Menggunakan Naming.rebind untuk mengikat objek bank ke nama "BankService" di registry, sehingga dapat diakses oleh klien.
- Status Server: Mencetak pesan bahwa server siap menerima permintaan.


### 4. Klien `BankClient`
import java.rmi.Naming;

public class BankClient {
    public static void main(String[] args) {
        try {
            Bank bank = (Bank) Naming.lookup("rmi://localhost/BankService");

            System.out.println("Checking balance for ACC123:");
            double balance = bank.checkBalance("ACC123");
            System.out.println("Balance: $" + balance);

            System.out.println("\nTransferring $1000 from ACC123 to ACC456:");
            String transferResult = bank.transferFunds("ACC123", "ACC456", 1000.00);
            System.out.println(transferResult);

            System.out.println("\nChecking balance for ACC123 after transfer:");
            balance = bank.checkBalance("ACC123");
            System.out.println("Balance: $" + balance);

            System.out.println("\nChecking balance for ACC456 after receiving transfer:");
            balance = bank.checkBalance("ACC456");
            System.out.println("Balance: $" + balance);

        } catch (Exception e) {
            System.out.println("Bank Client failed: " + e);
        }
    }
}
#### Tujuan:
- Mengakses Layanan Bank: Kelas ini berfungsi sebagai klien yang berinteraksi dengan layanan perbankan yang disediakan oleh server RMI.
- Mencari Layanan Remote: Menggunakan Naming.lookup untuk mendapatkan referensi objek Bank yang terdaftar di registry RMI.
- Memeriksa Saldo: Menggunakan metode checkBalance untuk mendapatkan saldo dari akun tertentu dan mencetak hasilnya.
- Melakukan Transfer Dana: Menggunakan metode transferFunds untuk mentransfer dana antar akun, dan mencetak hasil transfer.
- Memeriksa Saldo Setelah Transfer: Memeriksa saldo dari kedua akun setelah melakukan transfer untuk memastikan operasi berhasil.

### Rangkuman Keseluruhan
- `Bank`: Mendefinisikan antarmuka untuk layanan perbankan yang dapat diakses secara remote.
- `BankImpl`: Mengimplementasikan logika dari antarmuka Bank, menangani operasi dasar perbankan dengan menyimpan data akun dalam HashMap.
- `BankServer`: Menjalankan server RMI, membuat objek BankImpl, dan mendaftarkannya ke registry agar dapat diakses oleh klien.
- `BankClient`: Bertindak sebagai klien yang berinteraksi dengan server RMI, melakukan operasi perbankan seperti memeriksa saldo dan mentransfer dana.
