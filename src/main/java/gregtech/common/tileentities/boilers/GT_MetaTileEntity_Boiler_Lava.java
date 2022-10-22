package gregtech.common.tileentities.boilers;

import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;

public class GT_MetaTileEntity_Boiler_Lava extends GT_MetaTileEntity_Boiler {

    public static final int COOLDOWN_INTERVAL = 20;
    public static final int ENERGY_PER_LAVA = 1;
    public static final int CONSUMPTION_PER_HEATUP = 3;
    public static final int PRODUCTION_PER_SECOND = 600;

    public GT_MetaTileEntity_Boiler_Lava(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, new String[]{
                "A Boiler running off Lava",
                "Produces " + PRODUCTION_PER_SECOND + "L of Steam per second",
                "Causes " + Integer.toString(GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond) + " Pollution per second",
                "Consumes " + ((double) CONSUMPTION_PER_HEATUP / ENERGY_PER_LAVA) + "L of Lava every " + COOLDOWN_INTERVAL + " ticks when fully heat up"});
    }

    public GT_MetaTileEntity_Boiler_Lava(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Lava(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        final ITexture[]
                texBottom = {TextureFactory.of(MACHINE_STEELBRICKS_BOTTOM)},
                texTop = {TextureFactory.of(MACHINE_STEELBRICKS_TOP), TextureFactory.of(OVERLAY_PIPE)},
                texSide = {TextureFactory.of(MACHINE_STEELBRICKS_SIDE), TextureFactory.of(OVERLAY_PIPE)},
                texFront = {
                        TextureFactory.of(MACHINE_STEELBRICKS_SIDE),
                        TextureFactory.of(BOILER_LAVA_FRONT),
                        TextureFactory.of(BOILER_LAVA_FRONT_GLOW)},
                texFrontActive = {
                        TextureFactory.of(MACHINE_STEELBRICKS_SIDE),
                        TextureFactory.of(BOILER_LAVA_FRONT_ACTIVE),
                        TextureFactory.builder().addIcon(BOILER_LAVA_FRONT_ACTIVE_GLOW).glow().build()};
        for (byte i = 0; i < 17; i++) {
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
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "SteelBoiler.png");
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Lava(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected int getPollution() {
        return GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond;
    }

    @Override
    protected int getProductionPerSecond() {
        return PRODUCTION_PER_SECOND;
    }

    @Override
    protected int getMaxTemperature() {
        return 1000;
    }

    @Override
    protected int getEnergyConsumption() {
        return CONSUMPTION_PER_HEATUP;
    }

    @Override
    protected int getCooldownInterval() {
        return COOLDOWN_INTERVAL;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.bucket.get(Materials.Lava))) {
            this.mProcessingEnergy += 1000 * ENERGY_PER_LAVA;
            aBaseMetaTileEntity.decrStackSize(2, 1);
            aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L));
        } else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.bucketClay.get(Materials.Lava))) {
            this.mProcessingEnergy += 1000 * ENERGY_PER_LAVA;
            aBaseMetaTileEntity.decrStackSize(2, 1);
            // Clay lava buckets break, so you don't get it back.
        }
    }

    @Override
    public final int fill(FluidStack aFluid, boolean doFill) {
        if ((GT_ModHandler.isLava(aFluid)) && (this.mProcessingEnergy < 50)) {
            int tFilledAmount = Math.min(50, aFluid.amount);
            if (doFill) {
                this.mProcessingEnergy += tFilledAmount * ENERGY_PER_LAVA;
            }
            return tFilledAmount;
        }
        return super.fill(aFluid, doFill);
    }
}
