package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.ConfigCategories.machineconfig;

public class GT_MetaTileEntity_Boiler_Solar_Steel extends GT_MetaTileEntity_Boiler_Solar {
    private final GT_MetaTileEntity_Boiler_Solar_Steel.Config config = new GT_MetaTileEntity_Boiler_Solar_Steel.Config();

    public GT_MetaTileEntity_Boiler_Solar_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        config.onConfigLoad();
    }

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        config.onConfigLoad();
    }

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        config.onConfigLoad();
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {

        ITexture[][][] rTextures = new ITexture[4][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color, Dyes._NULL.mRGBa);
            rTextures[0][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_STEELBRICKS_BOTTOM, colorModulation)};
            rTextures[1][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_STEELBRICKS_TOP, colorModulation),
                    new GT_RenderedTexture(BlockIcons.BOILER_SOLAR)};
            rTextures[2][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation)};
            rTextures[3][i] = new ITexture[]{
                    new GT_RenderedTexture(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation),
                    new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE)};
        }
        return rTextures;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "SolarHPBoiler.png", getCapacity());
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar_Steel(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    protected class Config extends GT_MetaTileEntity_Boiler_Solar.Config {

        @Override
        protected String getConfigCategory() {
            return machineconfig + ".boiler.solar.steel";
        }

        @Override
        protected int getCoolDownTicks() {
            return get("CoolDownTicks", 75,
                    "Number of ticks it takes to loose 1°C (Cools down slower than a normal boiler).");
        }

        @Override
        protected int getMaxOutputPerSecond() {
            return get("MaxOutputPerSecond", 360);
        }

        @Override
        protected int getMinOutputPerSecond() {
            return get("MinOutputPerSecond", 120);
        }

    }

}
