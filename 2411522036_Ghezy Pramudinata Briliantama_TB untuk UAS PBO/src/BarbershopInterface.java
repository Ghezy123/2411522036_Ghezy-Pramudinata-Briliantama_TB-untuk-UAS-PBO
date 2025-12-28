// Poin B: Memiliki Interface
public interface BarbershopInterface {
    
    void tambahLayanan(String nama, double harga);
    
    void lihatLayanan();
    
    void updateHarga(int id, double hargaBaru);
    
    void hapusLayanan(int id);
    
    void transaksi(String pelanggan, int idLayanan, double bayar);

    void lihatLaporanOmzet();
}