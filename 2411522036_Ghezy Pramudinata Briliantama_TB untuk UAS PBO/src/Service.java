public class Service extends BaseEntity {
    private double harga;

    public Service(int id, String nama, double harga) {
        super(id, nama);
        this.harga = harga;
    }

    public double getHarga() {
        return harga;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-15s : Rp%,.0f", id, nama, harga);
    }
}