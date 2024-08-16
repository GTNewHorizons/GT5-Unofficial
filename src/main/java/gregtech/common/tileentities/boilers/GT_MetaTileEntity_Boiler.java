package gregtech.common.tileentities.boilers;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.GT_Pollution;

public abstract class GT_MetaTileEntity_Boiler extends GT_MetaTileEntity_BasicTank
    implements IGetTitleColor, IAddUIWidgets {

    public static final byte SOUND_EVENT_LET_OFF_EXCESS_STEAM = 1;
    public int mTemperature = 20;
    public int mProcessingEnergy = 0;
    public int mLossTimer = 0;
    public FluidStack mSteam = null;
    public boolean mHadNoWater = false;
    private int mExcessWater = 0;

    public GT_MetaTileEntity_Boiler(int aID, String aName, String aNameRegional, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, 0, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler(int aID, String aName, String aNameRegional, String[] aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, 0, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture[] tmp;
        if ((sideDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0) { // Horizontal
            if (sideDirection != facingDirection) tmp = mTextures[2][colorIndex + 1];
            else tmp = mTextures[(byte) (active ? 4 : 3)][colorIndex + 1];
        } else {
            tmp = mTextures[sideDirection.ordinal()][colorIndex + 1];
        }
        if (sideDirection != facingDirection && tmp.length == 2) {
            tmp = new ITexture[] { tmp[0] };
        }
        return tmp;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facingDirection) {
        return (facingDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public int getProgresstime() {
        return this.mTemperature;
    }

    @Override
    public int maxProgresstime() {
        return 500;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        if (aPlayer != null) {
            if (GT_Utility.areStacksEqual(aPlayer.getCurrentEquippedItem(), new ItemStack(Items.water_bucket, 1))) {
                fill(Materials.Water.getFluid(1000L * (long) aPlayer.getCurrentEquippedItem().stackSize), true);

                if (!aPlayer.capabilities.isCreativeMode) {
                    aPlayer.getCurrentEquippedItem()
                        .func_150996_a(Items.bucket);
                }
            } else {
                GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
            }
        }
        return true;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return GT_ModHandler.isWater(aFluid);
    }

    @Override
    public FluidStack getDrainableStack() {
        return this.mSteam;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack aFluid) {
        this.mSteam = aFluid;
        return this.mSteam;
    }

    @Override
    public boolean isDrainableStackSeparate() {
        return true;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCover) {
        return GregTech_API.getCoverBehaviorNew(aCover.toStack())
            .isSimpleCover();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mLossTimer", this.mLossTimer);
        aNBT.setInteger("mTemperature", this.mTemperature);
        aNBT.setInteger("mProcessingEnergy", this.mProcessingEnergy);
        aNBT.setInteger("mExcessWater", this.mExcessWater);
        if (this.mSteam == null) {
            return;
        }
        try {
            aNBT.setTag("mSteam", this.mSteam.writeToNBT(new NBTTagCompound()));
        } catch (Throwable ignored) {}
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mLossTimer = aNBT.getInteger("mLossTimer");
        this.mTemperature = aNBT.getInteger("mTemperature");
        this.mProcessingEnergy = aNBT.getInteger("mProcessingEnergy");
        this.mExcessWater = aNBT.getInteger("mExcessWater");
        this.mSteam = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mSteam"));
    }

    /**
     * Produce some steam. Assume water is present.
     */
    protected void produceSteam(int aAmount) {
        mExcessWater -= aAmount;
        if (mExcessWater < 0) {
            int tWaterToConsume = -mExcessWater / GT_Values.STEAM_PER_WATER;
            mFluid.amount -= tWaterToConsume;
            mExcessWater += GT_Values.STEAM_PER_WATER * tWaterToConsume;
        }
        if (GT_ModHandler.isSteam(this.mSteam)) {
            this.mSteam.amount += aAmount;
        } else {
            this.mSteam = GT_ModHandler.getSteam(aAmount);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        pollute(aTick);

        if (isNotAllowedToWork(aBaseMetaTileEntity, aTick)) return;

        calculateCooldown();
        pushSteamToInventories(aBaseMetaTileEntity);

        if (canNotCreateSteam(aBaseMetaTileEntity, aTick)) {
            pollute(aTick);
            return;
        }

        ventSteamIfTankIsFull();
        updateFuelTimed(aBaseMetaTileEntity, aTick);
        calculateHeatUp(aBaseMetaTileEntity, aTick);
    }

    private boolean isNotAllowedToWork(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        return (!aBaseMetaTileEntity.isServerSide()) || (aTick <= 20L);
    }

    private void pollute(long aTick) {
        if (this.mProcessingEnergy > 0 && (aTick % 20L == 0L)) {
            GT_Pollution.addPollution(getBaseMetaTileEntity(), getPollution());
        }
    }

    private void calculateHeatUp(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((this.mTemperature < getMaxTemperature()) && (this.mProcessingEnergy > 0)
            && (aTick % getHeatUpRate() == 0L)) {
            this.mProcessingEnergy -= getEnergyConsumption();
            this.mTemperature += getHeatUpAmount();
        }
        aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
    }

    private void updateFuelTimed(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()))
            updateFuel(aBaseMetaTileEntity, aTick);
    }

    protected void ventSteamIfTankIsFull() {
        if ((this.mSteam != null) && (this.mSteam.amount > getSteamCapacity())) {
            sendSound(SOUND_EVENT_LET_OFF_EXCESS_STEAM);
            this.mSteam.amount = getSteamCapacity() * 3 / 4;
        }
    }

    private boolean canNotCreateSteam(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 10L != 0L) {
            return false;
        }

        if (this.mTemperature > 100) {
            if ((!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                this.mHadNoWater = true;
            } else {
                if (this.mHadNoWater) {
                    GT_Log.exp.println("Boiler " + this.mName + " had no Water!");
                    onDangerousWaterLack(aBaseMetaTileEntity, aTick);
                    return true;
                }
                produceSteam(getProductionPerSecond() / 2);
            }
        } else {
            this.mHadNoWater = false;
        }
        return false;
    }

    protected void onDangerousWaterLack(IGregTechTileEntity tile, long ignoredTicks) {
        tile.doExplosion(2048L);
    }

    /**
     * Pushes Steam to a Side of this Boiler
     *
     * @param aBaseMetaTileEntity The tile-entity instance of this Boiler
     * @param side                The direction of the side to push Steam to
     */
    protected final void pushSteamToSide(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side) {
        if (mSteam == null || mSteam.amount == 0) return;
        final IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(side);
        if (tTileEntity == null) return;
        GT_Utility.moveFluid(aBaseMetaTileEntity, tTileEntity, side, Math.max(1, this.mSteam.amount / 2), null);
    }

    /**
     * Pushes steam to Fluid inventories at all sides except Front and Bottom.
     *
     * @param aBaseMetaTileEntity The tile-entity instance of this Boiler
     */
    protected void pushSteamToInventories(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mSteam == null || mSteam.amount == 0) return;
        for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            if (direction == aBaseMetaTileEntity.getFrontFacing() || direction == ForgeDirection.DOWN) continue;
            if (this.mSteam == null) break;
            pushSteamToSide(aBaseMetaTileEntity, direction);
        }
    }

    private void calculateCooldown() {
        if (this.mTemperature <= 20) {
            this.mTemperature = 20;
            this.mLossTimer = 0;
        } else if (++this.mLossTimer > getCooldownInterval()) {
            // only loss temperature if hot
            this.mTemperature -= 1;
            this.mLossTimer = 0;
        }
    }

    protected boolean isAutomatable() {
        return GT_Mod.gregtechproxy.mAllowSmallBoilerAutomation;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return isAutomatable() && aIndex == 1 || aIndex == 3;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return isAutomatable() && (aIndex == 0 && isItemValidFluidFilledItem(aStack))
            || (aIndex == 2 && isItemValidFuel(aStack));
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == GT_MetaTileEntity_Boiler.SOUND_EVENT_LET_OFF_EXCESS_STEAM) {
            GT_Utility.doSoundAtClient(SoundResource.RANDOM_FIZZ, 2, 1.0F, aX, aY, aZ);

            new ParticleEventBuilder().setIdentifier(ParticleFX.CLOUD)
                .setWorld(getBaseMetaTileEntity().getWorld())
                .setMotion(0D, 0D, 0D)
                .<ParticleEventBuilder>times(
                    8,
                    x -> x.setPosition(aX - 0.5D + XSTR_INSTANCE.nextFloat(), aY, aZ - 0.5D + XSTR_INSTANCE.nextFloat())
                        .run());
        }
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

    protected abstract int getPollution();

    @Override
    public int getCapacity() {
        return 16000;
    }

    protected int getSteamCapacity() {
        return getCapacity();
    }

    protected abstract int getProductionPerSecond();

    protected abstract int getMaxTemperature();

    protected abstract int getEnergyConsumption();

    protected abstract int getCooldownInterval();

    protected int getHeatUpRate() {
        return 12;
    }

    protected int getHeatUpAmount() {
        return 1;
    }

    protected abstract void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick);

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    protected IDrawable[] getFuelSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot(),
            GT_UITextures.OVERLAY_SLOT_COAL_STEAM.get(getSteamVariant()) };
    }

    protected IDrawable[] getAshSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot(),
            GT_UITextures.OVERLAY_SLOT_DUST_STEAM.get(getSteamVariant()) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setFilter(this::isItemValidFluidFilledItem)
                .setPos(43, 25)
                .setBackground(getGUITextureSet().getItemSlot(), getOverlaySlotIn()))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setPos(43, 61)
                    .setBackground(getGUITextureSet().getItemSlot(), getOverlaySlotOut()))
            .widget(createFuelSlot())
            .widget(createAshSlot())
            .widget(
                new ProgressBar().setProgress(() -> mSteam == null ? 0 : (float) mSteam.amount / getSteamCapacity())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_STEAM, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(70, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar().setProgress(() -> mFluid == null ? 0 : (float) mFluid.amount / getCapacity())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_WATER, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(83, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar().setProgress(() -> (float) mTemperature / maxProgresstime())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_HEAT, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(96, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar()
                    // cap minimum so that one can easily see there's fuel remaining
                    .setProgress(() -> mProcessingEnergy > 0 ? Math.max((float) mProcessingEnergy / 1000, 1f / 5) : 0)
                    .setTexture(getProgressbarFuel(), 14)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(116, 45)
                    .setSize(14, 14))
            .widget(
                new DrawableWidget().setDrawable(getOverlaySlotCanister())
                    .setPos(43, 43)
                    .setSize(18, 18));
    }

    private boolean isItemValidFluidFilledItem(@NotNull ItemStack stack) {
        return isFluidInputAllowed(GT_Utility.getFluidForFilledItem(stack, true));
    }

    protected Widget createFuelSlot() {
        return new SlotWidget(inventoryHandler, 2).setFilter(this::isItemValidFuel)
            .setPos(115, 61)
            .setBackground(getFuelSlotBackground());
    }

    protected boolean isItemValidFuel(@NotNull ItemStack stack) {
        return true;
    }

    protected SlotWidget createAshSlot() {
        return (SlotWidget) new SlotWidget(inventoryHandler, 3).setAccess(true, false)
            .setPos(115, 25)
            .setBackground(getAshSlotBackground());
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.STEAM.apply(getSteamVariant());
    }

    @Override
    public int getTitleColor() {
        return getSteamVariant() == SteamVariant.BRONZE ? COLOR_TITLE.get() : COLOR_TITLE_WHITE.get();
    }

    // for GT++

    protected IDrawable getOverlaySlotIn() {
        return GT_UITextures.OVERLAY_SLOT_IN_STEAM.get(getSteamVariant());
    }

    protected IDrawable getOverlaySlotOut() {
        return GT_UITextures.OVERLAY_SLOT_OUT_STEAM.get(getSteamVariant());
    }

    protected IDrawable getOverlaySlotCanister() {
        return GT_UITextures.OVERLAY_SLOT_CANISTER_STEAM.get(getSteamVariant());
    }

    protected UITexture getProgressbarEmpty() {
        return GT_UITextures.PROGRESSBAR_BOILER_EMPTY_STEAM.get(getSteamVariant());
    }

    protected UITexture getProgressbarFuel() {
        return GT_UITextures.PROGRESSBAR_FUEL_STEAM.get(getSteamVariant());
    }
}
