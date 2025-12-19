package gregtech.common.tileentities.boilers;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.common.config.MachineStats;

public class MTEBoilerSolarSteel extends MTEBoilerSolar {

    public MTEBoilerSolarSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initBoilerStats();
    }

    public MTEBoilerSolarSteel(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        initBoilerStats();
    }

    protected void initBoilerStats() {
        calcificationTicks = MachineStats.steelSolarBoiler.calcificationTicks;
        cooldownTicks = MachineStats.steelSolarBoiler.cooldownTicks;
        maxOutputPerSecond = MachineStats.steelSolarBoiler.maxOutputPerSecond;
        minOutputPerSecond = MachineStats.steelSolarBoiler.minOutputPerSecond;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        for (int color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color);
            rTextures[0][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_BOTTOM, colorModulation) };
            rTextures[1][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR) };
            rTextures[2][i] = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation) };
            rTextures[3][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(BlockIcons.OVERLAY_PIPE) };
            rTextures[4][i] = new ITexture[] { TextureFactory.of(BlockIcons.MACHINE_STEELBRICKS_TOP, colorModulation),
                TextureFactory.of(BlockIcons.BOILER_SOLAR) };
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
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.STEEL;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoilerSolarSteel(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }
}
