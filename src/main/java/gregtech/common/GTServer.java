package gregtech.common;

import net.minecraft.entity.player.EntityPlayer;

import gregtech.api.covers.CoverRegistry;

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

    @Override
    public void onPostLoad() {
        super.onPostLoad();

        // Cover GUI needs to know text color which can be configured with resource packs. In theory it's not needed on
        // server, but it's just convenient to be able to write GUI code without side check. This will be reworked with
        // MUI2, but for the time being it stays here. -- miozune
        CoverRegistry.reloadCoverColorOverrides();
    }
}
