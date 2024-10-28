# Aplikasi Bank Client-Server dengan Java RMI
oleh:
1. Bainul Dwi Tri Putra (320220401003)
2. Fhatur Robby Tanzil Herris (320220401005)
3. Khaerul Imam Fathoni (320220401014)
4. Nisrina Labiba Sarwoko (320220401020)
5. Zerusealtin David Naibaho (320220401025)

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


- `Bank`: Mendefinisikan antarmuka untuk layanan perbankan yang dapat diakses secara remote.
- `BankImpl`: Mengimplementasikan logika dari antarmuka Bank, menangani operasi dasar perbankan dengan menyimpan data akun dalam HashMap.
- `BankServer`: Menjalankan server RMI, membuat objek BankImpl, dan mendaftarkannya ke registry agar dapat diakses oleh klien.
- `BankClient`: Bertindak sebagai klien yang berinteraksi dengan server RMI, melakukan operasi perbankan seperti memeriksa saldo dan mentransfer dana.
