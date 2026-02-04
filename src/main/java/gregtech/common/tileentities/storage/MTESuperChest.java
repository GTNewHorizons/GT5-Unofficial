package gregtech.common.tileentities.storage;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTESuperChest extends MTEQuantumChest {

    public MTESuperChest(Args args) {
        super(args);
    }

    @Deprecated
    public MTESuperChest(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Deprecated
    public MTESuperChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    protected String localizedChestName() {
        return StatCollector.translateToLocal("GT5U.infodata.super_chest.name");
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperChest(getPrototype());
    }
}
