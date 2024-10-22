package gregtech.common.entities;

public enum EntityID {

    LargeItem(0);

    public final int ID;

    private EntityID(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException("id");
        ID = id;
    }
}
