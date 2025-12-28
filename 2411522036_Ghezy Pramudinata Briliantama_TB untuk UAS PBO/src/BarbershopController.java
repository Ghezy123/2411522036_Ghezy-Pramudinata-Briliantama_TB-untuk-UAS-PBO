import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList; // Poin G: Collection Framework
import java.util.Date;      // Poin E: Date Manipulation
import java.util.List;

// Poin B: Implementasi Interface
public class BarbershopController implements BarbershopInterface {
    private Connection conn;

    public BarbershopController(Connection conn) {
        this.conn = conn;
    }

    // --- FITUR 1: CREATE (Tambah Layanan Baru) ---
    @Override
    public void tambahLayanan(String nama, double harga) {
        try {
            String sql = "INSERT INTO services (service_name, price) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nama);
            pstmt.setDouble(2, harga);
            pstmt.executeUpdate();
            System.out.println(">> Sukses: Layanan '" + nama + "' berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal tambah data: " + e.getMessage());
        }
    }

    // --- FITUR 2: READ (Lihat Daftar Layanan) ---
    @Override
    public void lihatLayanan() {
        List<Service> listData = new ArrayList<>(); // Poin G: ArrayList
        String sql = "SELECT * FROM services";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Memasukkan data DB ke Object Service (Inheritance)
                listData.add(new Service(
                    rs.getInt("id"),
                    rs.getString("service_name"),
                    rs.getDouble("price")
                ));
            }

            System.out.println("\n=== DAFTAR HARGA OHYRA HAIR CUT ===");
            if (listData.isEmpty()) {
                System.out.println("(Belum ada data layanan)");
            } else {
                // Poin D: Perulangan (For Loop)
                for (Service s : listData) {
                    System.out.println(s.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- FITUR 3: UPDATE (Ubah Harga) ---
    @Override
    public void updateHarga(int id, double hargaBaru) {
        try {
            String sql = "UPDATE services SET price = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, hargaBaru);
            pstmt.setInt(2, id);
            
            int row = pstmt.executeUpdate();
            if (row > 0) System.out.println(">> Sukses: Harga berhasil diupdate!");
            else System.out.println(">> Gagal: ID Layanan tidak ditemukan.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- FITUR 4: DELETE (Hapus Layanan) ---
    @Override
    public void hapusLayanan(int id) {
        try {
            String sql = "DELETE FROM services WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int row = pstmt.executeUpdate();
            if (row > 0) System.out.println(">> Sukses: Data berhasil dihapus!");
            else System.out.println(">> Gagal: ID Layanan tidak ditemukan.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- FITUR 5: TRANSAKSI (Kasir & Struk Otomatis) ---
    @Override
    public void transaksi(String pelanggan, int idLayanan, double bayar) {
        try {
            // 1. Cek Data Layanan (Ambil harga dari DB)
            String sqlGet = "SELECT * FROM services WHERE id = ?";
            PreparedStatement pGet = conn.prepareStatement(sqlGet);
            pGet.setInt(1, idLayanan);
            ResultSet rs = pGet.executeQuery();

            if (rs.next()) {
                String namaLayanan = rs.getString("service_name");
                double harga = rs.getDouble("price");

                // Poin D: Validasi & Matematika
                if (bayar < harga) {
                    System.out.println(">> MAAF, UANG KURANG! Total: Rp" + harga);
                    return; // Batalkan proses
                }
                double kembalian = bayar - harga;

                // 2. Simpan Transaksi & AMBIL ID OTOMATIS (Auto-Generated Key)
                String sqlSimpan = "INSERT INTO transactions (customer_name, service_id, total_price) VALUES (?, ?, ?)";
                
                // Parameter 'Statement.RETURN_GENERATED_KEYS' wajib agar bisa ambil ID struk
                PreparedStatement pSimpan = conn.prepareStatement(sqlSimpan, Statement.RETURN_GENERATED_KEYS);
                pSimpan.setString(1, pelanggan);
                pSimpan.setInt(2, idLayanan);
                pSimpan.setDouble(3, harga);
                pSimpan.executeUpdate();

                // Mengambil ID Transaksi yang baru saja dibuat MySQL
                ResultSet rsTrx = pSimpan.getGeneratedKeys();
                int noStruk = 0;
                if (rsTrx.next()) {
                    noStruk = rsTrx.getInt(1); // Ini ID unik dari database
                }

                // Poin E: Manipulasi Tanggal & String untuk Struk
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                System.out.println("\n============================");
                System.out.println("   STRUK OHYRA HAIR CUT");
                System.out.println("============================");
                System.out.println("No. Struk: #" + noStruk); 
                System.out.println("Waktu    : " + sdf.format(now));
                System.out.println("Customer : " + pelanggan.toUpperCase()); // String Manipulation
                System.out.println("----------------------------");
                System.out.println("Service  : " + namaLayanan);
                System.out.println("Harga    : Rp" + harga);
                System.out.println("Bayar    : Rp" + bayar);
                System.out.println("Kembali  : Rp" + kembalian);
                System.out.println("============================");
                System.out.println("  Terima Kasih Datang Lagi  ");
                System.out.println("============================");

            } else {
                System.out.println(">> ERROR: ID Layanan tidak ditemukan di database.");
            }

        } catch (SQLException e) { // Poin F: Exception Handling
            System.out.println("Terjadi Error Database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- FITUR 6: REKAPITULASI OMZET (Jawaban Solusi Nomor 1) ---
    @Override
    public void lihatLaporanOmzet() {
        // Query SQL Agregasi (SUM & COUNT)
        String sql = "SELECT SUM(total_price) AS total_omzet, COUNT(*) AS total_trx FROM transactions";
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                double omzet = rs.getDouble("total_omzet");
                int jumlahTrx = rs.getInt("total_trx");
                
                System.out.println("\n=== LAPORAN KEUANGAN OHYRA HAIR CUT ===");
                System.out.println("Total Transaksi : " + jumlahTrx + " Pelanggan");
                System.out.printf("Total Pendapatan: Rp%,.0f\n", omzet);
                System.out.println("=======================================");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}