public class BaseEntity {
    protected int id;
    protected String nama;

    public BaseEntity(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}