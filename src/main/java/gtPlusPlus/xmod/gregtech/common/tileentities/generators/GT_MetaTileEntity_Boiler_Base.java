package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;

public class GT_MetaTileEntity_Boiler_Base extends GT_MetaTileEntity_Boiler {

    private final int steamPerSecond;
    private final int tier;

    public GT_MetaTileEntity_Boiler_Base(int aID, String aNameRegional, int tier) {
        super(
                aID,
                "electricboiler." + tier + ".tier.single",
                aNameRegional,
                "Produces " + (CORE.ConfigSwitches.boilerSteamPerSecond * tier) + "L of Steam per second");
        this.steamPerSecond = (CORE.ConfigSwitches.boilerSteamPerSecond * tier);
        this.tier = tier;
    }

    public GT_MetaTileEntity_Boiler_Base(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        this.steamPerSecond = (CORE.ConfigSwitches.boilerSteamPerSecond * aTier);
        this.tier = aTier;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
                this.mDescriptionArray,
                "Produces " + getPollution() + " pollution/sec",
                "Consumes fuel only when temperature is less than 100C",
                "Fuel with burn time greater than 500 is more efficient.",
                "Doesn't explode if there's no water",
                CORE.GT_Tooltip.get());
    }

    public ITexture getOverlayIcon() {
        return new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT);
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    protected GT_RenderedTexture getCasingTexture() {
        if (this.tier == 1) {
            return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_LV_SIDE);
        } else if (this.tier == 2) {
            return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_MV_SIDE);
        } else {
            return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_HV_SIDE);
        }
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0)
                + (side == facing ? 0
                        : side == facing.getOpposite() ? 1
                                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex
                                        + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.tier][aColor + 1], this.getCasingTexture() };
    }

    public ITexture[] getBack(final byte aColor) {
        return this.getSides(aColor);
    }

    public ITexture[] getBottom(final byte aColor) {
        return this.getSides(aColor);
    }

    public ITexture[] getTop(final byte aColor) {
        return this.getSides(aColor);
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.tier][aColor + 1], this.getCasingTexture() };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return this.getFront(aColor);
    }

    public ITexture[] getBackActive(final byte aColor) {
        return this.getSides(aColor);
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return this.getBottom(aColor);
    }

    public ITexture[] getTopActive(final byte aColor) {
        return this.getTop(aColor);
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return this.getSides(aColor);
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side != this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isFacingValid(final ForgeDirection side) {
        return side.offsetY == 0;
    }

    // Please find out what I do.
    // I do stuff within the GUI.
    // this.mTemperature = Math.min(54, Math.max(0, this.mTemperature * 54 / (((GT_MetaTileEntity_Boiler)
    // this.mTileEntity.getMetaTileEntity()).maxProgresstime() - 10)));
    @Override
    public int maxProgresstime() {
        return 1000 + (250 * tier);
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public int getCapacity() {
        return (16000 + (16000 * tier));
    }

    // This type of machine can have different water and steam capacities.
    @Override
    public int getSteamCapacity() {
        return 2 * getCapacity();
    }

    @Override
    protected int getProductionPerSecond() {
        return steamPerSecond;
    }

    @Override
    protected int getMaxTemperature() {
        return maxProgresstime();
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
    protected int getHeatUpRate() {
        return 10;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity tile, long ticks) {
        ItemStack fuelStack = this.mInventory[2];
        if (fuelStack == null) return;

        int burnTime = getBurnTime(fuelStack);
        if (burnTime > 0 && this.mTemperature <= 101) {
            consumeFuel(tile, fuelStack, burnTime);
        }
    }

    @Override
    // Since this type of machine can have different water and steam capacities, we need to override getTankInfo() to
    // support returning those different capacities.
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        return new FluidTankInfo[] { new FluidTankInfo(this.mFluid, getCapacity()),
                new FluidTankInfo(this.mSteam, getSteamCapacity()) };
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return aIndex == 1 || aIndex == 3;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return aIndex == 2;
    }

    @Override
    protected int getPollution() {
        return (int) (CORE.ConfigSwitches.basePollutionPerSecondBoiler
                * CORE.ConfigSwitches.pollutionReleasedByTierBoiler[this.tier]);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Base(this.mName, tier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected void onDangerousWaterLack(IGregTechTileEntity tile, long ticks) {
        // Smart boilers don't explode!
    }

    /**
     * Returns burn time if the stack is a valid fuel, otherwise return 0.
     */
    private static int getBurnTime(ItemStack stack) {
        int burnTime = GameRegistry.getFuelValue(stack);
        if (burnTime <= 0) {
            burnTime = TileEntityFurnace.getItemBurnTime(stack);
        }

        return burnTime;
    }

    public void consumeFuel(IGregTechTileEntity tile, ItemStack fuel, int burnTime) {
        this.mProcessingEnergy += burnTime / 10;
        this.mTemperature += burnTime / 500; // will add bonus temperature points if the burn time is pretty high

        tile.decrStackSize(2, 1);
        if (tile.getRandomNumber(3) == 0) {
            if (fuel.getDisplayName().toLowerCase().contains("charcoal")
                    || fuel.getDisplayName().toLowerCase().contains("coke")) {
                tile.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1L));
            } else {
                tile.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L));
            }
        }
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCover) {
        if (side != this.getBaseMetaTileEntity().getFrontFacing()) {
            return true;
        }
        return super.allowCoverOnSide(side, aCover);
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.DEFAULT;
    }

    @Override
    protected IDrawable[] getFuelSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_COAL };
    }

    @Override
    protected IDrawable[] getAshSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_DUST };
    }

    @Override
    public int getTitleColor() {
        return COLOR_TITLE.get();
    }

    @Override
    protected IDrawable getOverlaySlotIn() {
        return GT_UITextures.OVERLAY_SLOT_IN;
    }

    @Override
    protected IDrawable getOverlaySlotOut() {
        return GT_UITextures.OVERLAY_SLOT_OUT;
    }

    @Override
    protected IDrawable getOverlaySlotCanister() {
        return GTPP_UITextures.OVERLAY_SLOT_CANISTER_DARK;
    }

    @Override
    protected UITexture getProgressbarEmpty() {
        return GTPP_UITextures.PROGRESSBAR_BOILER_EMPTY;
    }

    @Override
    protected UITexture getProgressbarFuel() {
        return GTPP_UITextures.PROGRESSBAR_FUEL;
    }
}
