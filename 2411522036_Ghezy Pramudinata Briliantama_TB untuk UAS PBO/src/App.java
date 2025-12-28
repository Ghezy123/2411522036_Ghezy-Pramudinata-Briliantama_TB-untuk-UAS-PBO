import java.sql.Connection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // 1. Cek Koneksi Database
        Connection conn = DatabaseConnection.connect();
        if (conn == null) {
            System.out.println("Gagal terhubung ke database. Cek XAMPP!");
            return;
        }

        // 2. Inisialisasi Controller & Scanner
        BarbershopController app = new BarbershopController(conn);
        Scanner sc = new Scanner(System.in);
        boolean jalan = true;

        // 3. Loop Menu Utama
        while (jalan) {
            System.out.println("\n--- SISTEM MANAJEMEN BARBERSHOP ---");
            System.out.println("1. Transaksi (Kasir)");
            System.out.println("2. Kelola Layanan (Admin)");
            System.out.println("3. Keluar");
            System.out.print("Pilih Menu: ");
            
            int menu = sc.nextInt();
            sc.nextLine(); // Membersihkan sisa baris baru (buffer)

            switch (menu) {
                case 1:
                    // --- FITUR KASIR ---
                    app.lihatLayanan();
                    System.out.println("\n(Ketik '0' jika ingin BATAL/KEMBALI)");
                    System.out.print("Nama Pelanggan: ");
                    String nama = sc.nextLine();

                    // Logika Back (Jika user mengetik 0)
                    if (nama.equals("0")) {
                        System.out.println(">> Transaksi Dibatalkan. Kembali ke menu utama...");
                        break; 
                    }
                    
                    System.out.print("Pilih ID Layanan: ");
                    int idLayanan = sc.nextInt();
                    
                    System.out.print("Uang Bayar: ");
                    double uang = sc.nextDouble();
                    
                    // Panggil proses transaksi di Controller
                    app.transaksi(nama, idLayanan, uang);
                    break;
                
                case 2:
                    // --- FITUR ADMIN (CRUD + OMZET) ---
                    System.out.println("\n--- MENU ADMIN OHYRA HAIR CUT ---");
                    System.out.println("1. Tambah Layanan Baru");
                    System.out.println("2. Edit Harga Layanan");
                    System.out.println("3. Hapus Layanan");
                    System.out.println("4. LIHAT TOTAL OMZET (Laporan)"); // Menu Baru
                    System.out.println("0. KEMBALI (BACK)");
                    System.out.print("Pilih Aksi: ");
                    
                    int crud = sc.nextInt();
                    sc.nextLine();

                    // Logika Back
                    if (crud == 0) {
                        System.out.println(">> Kembali ke menu utama...");
                        break;
                    }

                    if (crud == 1) {
                        // Create
                        System.out.print("Nama Layanan Baru: ");
                        String n = sc.nextLine();
                        System.out.print("Harga: ");
                        double h = sc.nextDouble();
                        app.tambahLayanan(n, h);
                    } else if (crud == 2) {
                        // Update
                        app.lihatLayanan();
                        System.out.print("ID yang mau diedit: ");
                        int id = sc.nextInt();
                        System.out.print("Harga Baru: ");
                        double hb = sc.nextDouble();
                        app.updateHarga(id, hb);
                    } else if (crud == 3) {
                        // Delete
                        app.lihatLayanan();
                        System.out.print("ID yang mau dihapus: ");
                        int id = sc.nextInt();
                        app.hapusLayanan(id);
                    } else if (crud == 4) {
                        // Laporan Omzet (Sesuai Solusi Nomor 1)
                        app.lihatLaporanOmzet();
                    } else {
                        System.out.println("Pilihan tidak valid.");
                    }
                    break;

                case 3:
                    // --- KELUAR ---
                    jalan = false;
                    System.out.println("Aplikasi Ditutup. Terima Kasih!");
                    break;

                default:
                    System.out.println("Menu tidak tersedia!");
            }
        }
        sc.close();
    }
}