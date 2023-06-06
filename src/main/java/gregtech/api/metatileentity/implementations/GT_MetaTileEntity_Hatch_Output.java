package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IFluidLockable;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.widget.FluidLockWidget;

public class GT_MetaTileEntity_Hatch_Output extends GT_MetaTileEntity_Hatch
    implements IFluidStore, IFluidLockable, IAddUIWidgets {

    private String lockedFluidName = null;
    private WeakReference<EntityPlayer> playerThatLockedfluid = null;
    public byte mMode = 0;

    public GT_MetaTileEntity_Hatch_Output(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "Fluid Output for Multiblocks",
                "Capacity: " + GT_Utility.formatNumbers(8000L * (1L << aTier)) + "L",
                "Right click with screwdriver to restrict output",
                "Can be restricted to put out Items and/or Steam/No Steam/1 specific Fluid",
                "Restricted Output Hatches are given priority for Multiblock Fluid output" });
    }

    public GT_MetaTileEntity_Hatch_Output(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Output(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Output(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        int inventorySize) {
        super(aID, aName, aNameRegional, aTier, inventorySize, aDescription);
    }

    public GT_MetaTileEntity_Hatch_Output(String name, int tier, int slots, String[] description,
        ITexture[][][] textures) {
        super(name, tier, slots, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Output(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && mFluid != null) {
            IFluidHandler tTileEntity = aBaseMetaTileEntity
                .getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (tTileEntity != null) {
                FluidStack tDrained = aBaseMetaTileEntity
                    .drain(aBaseMetaTileEntity.getFrontFacing(), Math.max(1, mFluid.amount), false);
                if (tDrained != null) {
                    int tFilledAmount = tTileEntity.fill(aBaseMetaTileEntity.getBackFacing(), tDrained, false);
                    if (tFilledAmount > 0) {
                        tTileEntity.fill(
                            aBaseMetaTileEntity.getBackFacing(),
                            aBaseMetaTileEntity.drain(aBaseMetaTileEntity.getFrontFacing(), tFilledAmount, true),
                            true);
                    }
                }
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mMode", mMode);
        if (isFluidLocked() && lockedFluidName != null && lockedFluidName.length() != 0)
            aNBT.setString("lockedFluidName", lockedFluidName);
        else aNBT.removeTag("lockedFluidName");
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mMode = aNBT.getByte("mMode");
        if (isFluidLocked()) {
            lockedFluidName = aNBT.getString("lockedFluidName");
        }
        lockedFluidName = GT_Utility.isStringInvalid(lockedFluidName) ? null : lockedFluidName;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
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
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    public int getLockedDisplaySlot() {
        return 3;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        // Because getStackDisplaySlot() only allow return one int, this place I only can manually set.
        return aIndex != getStackDisplaySlot() && aIndex != getLockedDisplaySlot();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0;
    }

    @Override
    public int getCapacity() {
        return 8000 * (1 << mTier);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .isGUIClickable()) return;
        if (aPlayer.isSneaking()) {
            mMode = (byte) ((mMode + 9) % 10);
        } else {
            mMode = (byte) ((mMode + 1) % 10);
        }
        final String inBrackets;
        switch (mMode) {
            case 0 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("108", "Outputs misc. Fluids, Steam and Items"));
                this.setLockedFluidName(null);
            }
            case 1 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("109", "Outputs Steam and Items"));
                this.setLockedFluidName(null);
            }
            case 2 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("110", "Outputs Steam and misc. Fluids"));
                this.setLockedFluidName(null);
            }
            case 3 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("111", "Outputs Steam"));
                this.setLockedFluidName(null);
            }
            case 4 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("112", "Outputs misc. Fluids and Items"));
                this.setLockedFluidName(null);
            }
            case 5 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("113", "Outputs only Items"));
                this.setLockedFluidName(null);
            }
            case 6 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("114", "Outputs only misc. Fluids"));
                this.setLockedFluidName(null);
            }
            case 7 -> {
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("115", "Outputs nothing"));
                this.setLockedFluidName(null);
            }
            case 8 -> {
                playerThatLockedfluid = new WeakReference<>(aPlayer);
                if (mFluid == null) {
                    this.setLockedFluidName(null);
                    inBrackets = GT_Utility.trans(
                        "115.3",
                        "currently none, will be locked to the next that is put in (or use fluid cell to lock)");
                } else {
                    this.setLockedFluidName(
                        this.getDrainableStack()
                            .getFluid()
                            .getName());
                    inBrackets = this.getDrainableStack()
                        .getLocalizedName();
                }
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "%s (%s)",
                        GT_Utility.trans("151.1", "Outputs items and 1 specific Fluid"),
                        inBrackets));
            }
            case 9 -> {
                playerThatLockedfluid = new WeakReference<>(aPlayer);
                if (mFluid == null) {
                    this.setLockedFluidName(null);
                    inBrackets = GT_Utility.trans(
                        "115.3",
                        "currently none, will be locked to the next that is put in (or use fluid cell to lock)");
                } else {
                    this.setLockedFluidName(
                        this.getDrainableStack()
                            .getFluid()
                            .getName());
                    inBrackets = this.getDrainableStack()
                        .getLocalizedName();
                }
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format("%s (%s)", GT_Utility.trans("151.2", "Outputs 1 specific Fluid"), inBrackets));
            }
        }
    }

    private boolean tryToLockHatch(EntityPlayer aPlayer, ForgeDirection side) {
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .isGUIClickable()) return false;
        if (!isFluidLocked()) return false;
        final ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
        if (tCurrentItem == null) return false;
        FluidStack tFluid = FluidContainerRegistry.getFluidForFilledItem(tCurrentItem);
        if (tFluid == null && tCurrentItem.getItem() instanceof IFluidContainerItem)
            tFluid = ((IFluidContainerItem) tCurrentItem.getItem()).getFluid(tCurrentItem);
        if (tFluid != null) {
            if (getLockedFluidName() != null && !getLockedFluidName().equals(
                tFluid.getFluid()
                    .getName())) {
                GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "%s %s",
                        GT_Utility.trans(
                            "151.3",
                            "Hatch is locked to a different fluid. To change the locking, empty it and made it locked to the next fluid with a screwdriver. Currently locked to"),
                        StatCollector.translateToLocal(getLockedFluidName())));
            } else {
                setLockedFluidName(
                    tFluid.getFluid()
                        .getName());
                if (mMode == 8) GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "%s (%s)",
                        GT_Utility.trans("151.1", "Outputs items and 1 specific Fluid"),
                        tFluid.getLocalizedName()));
                else GT_Utility.sendChatToPlayer(
                    aPlayer,
                    String.format(
                        "%s (%s)",
                        GT_Utility.trans("151.2", "Outputs 1 specific Fluid"),
                        tFluid.getLocalizedName()));
            }
            return true;
        }
        return false;
    }

    public byte getMode() {
        return mMode;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (tryToLockHatch(aPlayer, side)) return true;
        return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
    }

    public boolean outputsSteam() {
        return mMode < 4;
    }

    public boolean outputsLiquids() {
        return mMode % 2 == 0 || mMode == 9;
    }

    public boolean outputsItems() {
        return mMode % 4 < 2 && mMode != 9;
    }

    @Override
    public String getLockedFluidName() {
        return lockedFluidName;
    }

    @Override
    public void setLockedFluidName(String lockedFluidName) {
        this.lockedFluidName = lockedFluidName;
        markDirty();
    }

    @Override
    public void lockFluid(boolean lock) {
        if (lock) {
            if (!isFluidLocked()) {
                this.mMode = 9;
                markDirty();
            }
        } else {
            this.mMode = 0;
            setLockedFluidName(null);
            markDirty();
        }
    }

    @Override
    public boolean isFluidLocked() {
        return mMode == 8 || mMode == 9;
    }

    @Override
    public boolean acceptsFluidLock(String name) {
        return true;
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return mMode == 0 && getFluidAmount() == 0;
    }

    @Override
    public boolean canStoreFluid(@Nonnull FluidStack fluidStack) {
        if (mFluid != null && !GT_Utility.areFluidsEqual(mFluid, fluidStack)) {
            return false;
        }
        if (isFluidLocked()) {
            if (lockedFluidName == null) {
                return true;
            }
            return lockedFluidName.equals(
                fluidStack.getFluid()
                    .getName());
        }
        if (GT_ModHandler.isSteam(fluidStack)) {
            return outputsSteam();
        }
        return outputsLiquids();
    }

    @Override
    public int getTankPressure() {
        return +100;
    }

    @Override
    protected void onEmptyingContainerWhenEmpty() {
        if (this.lockedFluidName == null && this.mFluid != null && isFluidLocked()) {
            this.setLockedFluidName(
                this.mFluid.getFluid()
                    .getName());
            final EntityPlayer player;
            if (playerThatLockedfluid == null || (player = playerThatLockedfluid.get()) == null) return;
            GT_Utility.sendChatToPlayer(
                player,
                String.format(GT_Utility.trans("151.4", "Successfully locked Fluid to %s"), mFluid.getLocalizedName()));
            playerThatLockedfluid = null;
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { EnumChatFormatting.BLUE + "Output Hatch" + EnumChatFormatting.RESET, "Stored Fluid:",
            EnumChatFormatting.GOLD + (mFluid == null ? "No Fluid" : mFluid.getLocalizedName())
                + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mFluid == null ? 0 : mFluid.amount)
                + " L"
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getCapacity())
                + " L"
                + EnumChatFormatting.RESET,
            (!isFluidLocked() || lockedFluidName == null) ? "Not Locked"
                : ("Locked to " + StatCollector.translateToLocal(
                    FluidRegistry.getFluidStack(lockedFluidName, 1)
                        .getUnlocalizedName())) };
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(98, 16)
                .setSize(71, 45))
            .widget(new FluidLockWidget(this).setPos(149, 41))
            .widget(
                new TextWidget("Locked Fluid").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(101, 20))
            .widget(TextWidget.dynamicString(() -> {
                FluidStack fluidStack = FluidRegistry.getFluidStack(lockedFluidName, 1);
                return fluidStack != null ? fluidStack.getLocalizedName() : "None";
            })
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxWidth(65)
                .setPos(101, 30))
            .widget(new FakeSyncWidget.ByteSyncer(() -> mMode, val -> mMode = val));
    }
}
