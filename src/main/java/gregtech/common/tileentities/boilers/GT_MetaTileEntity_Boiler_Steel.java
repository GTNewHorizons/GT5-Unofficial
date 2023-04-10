package gregtech.common.tileentities.boilers;

import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;

import gregtech.GT_Mod;
import gregtech.api.enums.SteamVariant;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_Boiler_Steel extends GT_MetaTileEntity_Boiler_Bronze {

    public GT_MetaTileEntity_Boiler_Steel(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            new String[] { "Faster than the Bronze Boiler", "Produces 300L of Steam per second",
                "Causes " + GT_Mod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond + " Pollution per second" });
    }

    public GT_MetaTileEntity_Boiler_Steel(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Steel(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        final ITexture[] texBottom = { TextureFactory.of(MACHINE_STEELBRICKS_BOTTOM) },
            texTop = { TextureFactory.of(MACHINE_STEELBRICKS_TOP), TextureFactory.of(OVERLAY_PIPE) },
            texSide = { TextureFactory.of(MACHINE_STEELBRICKS_SIDE), TextureFactory.of(OVERLAY_PIPE) },
            texFront = { TextureFactory.of(MACHINE_STEELBRICKS_SIDE), TextureFactory.of(BOILER_FRONT),
                TextureFactory.builder()
                    .addIcon(BOILER_FRONT_GLOW)
                    .glow()
                    .build() },
            texFrontActive = { TextureFactory.of(MACHINE_STEELBRICKS_SIDE), TextureFactory.of(BOILER_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(BOILER_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
        for (int i = 0; i < 17; i++) {
            rTextures[0][i] = texBottom;
            rTextures[1][i] = texTop;
            rTextures[2][i] = texSide;
            rTextures[3][i] = texFront;
            rTextures[4][i] = texFrontActive;
        }
        return rTextures;
    }

    @Override
    public int maxProgresstime() {
        return 1000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Steel(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected int getPollution() {
        return GT_Mod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond;
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    protected int getProductionPerSecond() {
        return 300;
    }

    @Override
    protected int getMaxTemperature() {
        return 1000;
    }

    @Override
    protected int getEnergyConsumption() {
        return 2;
    }

    @Override
    protected int getCooldownInterval() {
        return 40;
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.STEEL;
    }
}
