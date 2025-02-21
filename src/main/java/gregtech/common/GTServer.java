package gregtech.common;

import net.minecraft.entity.player.EntityPlayer;

public class GTServer extends GTProxy {

    @Override
    public boolean isServerSide() {
        return true;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public boolean isBukkitSide() {
        return false;
    }

    @Override
    public int addArmor(String aPrefix) {
        return 0;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return null;
    }
}
