package gregtech.common.tileentities.boilers;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.config.Configuration;

import static gregtech.api.enums.ConfigCategories.machineconfig;

public class GT_MetaTileEntity_Boiler_Solar_Steel extends GT_MetaTileEntity_Boiler_Solar {

    public GT_MetaTileEntity_Boiler_Solar_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        onConfigLoad();
    }

    @Override
    protected void onConfigLoad() {
        final Configuration config = GregTech_API.sMachineFile.mConfig;
        final String configCategory = machineconfig + ".boiler.solar.steel";

        final int defaultCalcificationTicks = 1080000; // 15 hours
        final int defaultMinOutputPerSecond = 120;
        final int defaultMaxOutputPerSecond = 360;
        final int defaultCoolDownTicks = 75;

        calcificationTicks = config.get(configCategory, "CalcificationTicks", defaultCalcificationTicks,
                "Number of run-time ticks before boiler starts calcification.\n" +
                        "100% calcification and minimal output will be reached at 2 times this.\n" +
                        DEFAULT_STR + defaultCalcificationTicks).getInt();
        minOutputPerSecond = config.get(configCategory, "MinOutputPerSecond", defaultMinOutputPerSecond,
                DEFAULT_STR + defaultMinOutputPerSecond).getInt();
        maxOutputPerSecond = config.get(configCategory, "MaxOutputPerSecond", defaultMaxOutputPerSecond,
                DEFAULT_STR + defaultMaxOutputPerSecond).getInt();
        coolDownTicks = config.get(configCategory, "CoolDownTicks", defaultCoolDownTicks,
                "Number of ticks it takes to loose 1°C (Cools down slower than a normal boiler).\n" +
                        DEFAULT_STR + defaultCoolDownTicks).getInt();

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

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad();
    }

}
