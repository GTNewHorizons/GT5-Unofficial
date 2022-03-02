package gregtech.api.graphs;

import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class Lock {
    protected ArrayList<TileEntity> tiles = new ArrayList<>();

    public void addTileEntity(TileEntity tileEntity) {
        int i = contains(tileEntity);
        if (i == -1) {
            tiles.add(tileEntity);
        }
    }

    public void removeTileEntity(TileEntity tileEntity) {
        int i = contains(tileEntity);
        if (i > -1) {
            tiles.remove(i);
        }
    }

    public boolean isLocked() {
        return !tiles.isEmpty();
    }

    //i want to check for the exact object not equals
    protected int contains(TileEntity tileEntity) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == tileEntity) {
                return i;
            }
        }
        return -1;
    }
}
