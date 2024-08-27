package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.config.machinestats.ConfigSteelSolarBoiler;

public class GT_MetaTileEntity_Boiler_Solar_Steel extends GT_MetaTileEntity_Boiler_Solar {

    public GT_MetaTileEntity_Boiler_Solar_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initBoilerStats();
    }

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        initBoilerStats();
    }

    public GT_MetaTileEntity_Boiler_Solar_Steel(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        initBoilerStats();
    }

    protected void initBoilerStats() {
        calcificationTicks = ConfigSteelSolarBoiler.calcificationTicks;
        cooldownTicks = ConfigSteelSolarBoiler.cooldownTicks;
        maxOutputPerSecond = ConfigSteelSolarBoiler.maxOutputPerSecond;
        minOutputPerSecond = ConfigSteelSolarBoiler.minOutputPerSecond;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {

        ITexture[][][] rTextures = new ITexture[4][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color, Dyes._NULL.mRGBa);
            rTextures[0][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_BOTTOM, colorModulation) };
            rTextures[1][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR) };
            rTextures[2][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation) };
            rTextures[3][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(BlockIcons.OVERLAY_PIPE) };
        }
        return rTextures;
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.STEEL;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Solar_Steel(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }
}
