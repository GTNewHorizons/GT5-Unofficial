package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
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

import gregtech.GTMod;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IFluidLockable;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.FluidLockWidget;

public class MTEHatchOutput extends MTEHatch implements IFluidStore, IFluidLockable, IAddUIWidgets {

    protected String lockedFluidName = null;
    private WeakReference<EntityPlayer> playerThatLockedfluid = null;
    public byte mMode = 0;

    public MTEHatchOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "Fluid Output for Multiblocks", "Capacity: " + formatNumber(8000L * (1L << aTier)) + "L",
                "Right click with screwdriver to restrict output",
                "Can be restricted to put out Items and/or Steam/No Steam/1 specific Fluid",
                "Restricted Output Hatches are given priority for Multiblock Fluid output" });
    }

    public MTEHatchOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public MTEHatchOutput(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        int inventorySize) {
        super(aID, aName, aNameRegional, aTier, inventorySize, aDescription);
    }

    public MTEHatchOutput(String name, int tier, int slots, String[] description, ITexture[][][] textures) {
        super(name, tier, slots, description, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchOutput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && mFluid != null) {
            IFluidHandler tTileEntity = aBaseMetaTileEntity
                .getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
            if (tTileEntity != null) {
                GTUtility.moveFluid(
                    aBaseMetaTileEntity,
                    tTileEntity,
                    aBaseMetaTileEntity.getFrontFacing(),
                    Math.max(1, mFluid.amount),
                    null);
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mMode", mMode);
        if (isFluidLocked() && lockedFluidName != null && !lockedFluidName.isEmpty())
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
        lockedFluidName = GTUtility.isStringInvalid(lockedFluidName) ? null : lockedFluidName;
    }

    @Override
    public boolean doesFillContainers() {
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        if (aPlayer.isSneaking()) {
            mMode = (byte) ((mMode + 9) % 10);
        } else {
            mMode = (byte) ((mMode + 1) % 10);
        }
        final String inBrackets;
        switch (mMode) {
            case 0 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.fluid_steam_item");
                this.setLockedFluidName(null);
            }
            case 1 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.steam_item");
                this.setLockedFluidName(null);
            }
            case 2 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.steam_fluid");
                this.setLockedFluidName(null);
            }
            case 3 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.steam");
                this.setLockedFluidName(null);
            }
            case 4 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.fluid_item");
                this.setLockedFluidName(null);
            }
            case 5 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.item");
                this.setLockedFluidName(null);
            }
            case 6 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.fluid");
                this.setLockedFluidName(null);
            }
            case 7 -> {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.nothing");
                this.setLockedFluidName(null);
            }
            case 8 -> {
                playerThatLockedfluid = new WeakReference<>(aPlayer);
                if (mFluid == null) {
                    this.setLockedFluidName(null);
                    inBrackets = "GT5U.chat.hatch.output.in_brackets.none";
                } else {
                    this.setLockedFluidName(
                        this.getDrainableStack()
                            .getFluid()
                            .getName());
                    // FIXME: localize this
                    inBrackets = this.getDrainableStack()
                        .getLocalizedName();
                }
                GTUtility.sendChatTrans(
                    aPlayer,
                    "GT5U.chat.hatch.output.items_and_specific_fluid",
                    new ChatComponentTranslation(inBrackets));
            }
            case 9 -> {
                playerThatLockedfluid = new WeakReference<>(aPlayer);
                if (mFluid == null) {
                    this.setLockedFluidName(null);
                    inBrackets = "GT5U.chat.hatch.output.in_brackets.none";
                } else {
                    this.setLockedFluidName(
                        this.getDrainableStack()
                            .getFluid()
                            .getName());
                    // FIXME: localize it
                    inBrackets = this.getDrainableStack()
                        .getLocalizedName();
                }
                GTUtility.sendChatTrans(
                    aPlayer,
                    "GT5U.chat.hatch.output.specific_fluid",
                    new ChatComponentTranslation(inBrackets));
            }
        }
    }

    private boolean tryToLockHatch(EntityPlayer aPlayer, ForgeDirection side) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
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
                // FIXME: localize it
                String fluidName = getLockedFluidName();
                // FluidStack fluidStack = FluidRegistry.getFluidStack(fluidName, 1);
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.hatch.output.fluid.already_locked", fluidName);
            } else {
                setLockedFluidName(
                    tFluid.getFluid()
                        .getName());
                if (mMode == 8) GTUtility.sendChatTrans(
                    aPlayer,
                    "GT5U.chat.hatch.output.items_and_specific_fluid",
                    // FIXME: localize it
                    tFluid.getLocalizedName());
                else GTUtility.sendChatTrans(
                    aPlayer,
                    "GT5U.chat.hatch.output.specific_fluid",
                    // FIXME: localize it
                    tFluid.getLocalizedName());
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
        if (mFluid != null && !GTUtility.areFluidsEqual(mFluid, fluidStack)) {
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
        if (GTModHandler.isSteam(fluidStack)) {
            return outputsSteam();
        }
        return outputsLiquids();
    }

    @Override
    protected void onEmptyingContainerWhenEmpty() {
        if (this.lockedFluidName == null && this.mFluid != null && isFluidLocked()) {
            this.setLockedFluidName(
                this.mFluid.getFluid()
                    .getName());
            final EntityPlayer player;
            if (playerThatLockedfluid == null || (player = playerThatLockedfluid.get()) == null) return;
            // FIXME: localize it
            GTUtility.sendChatTrans(player, "GT5U.chat.hatch.output.fluid.locked_to", mFluid.getLocalizedName());
            playerThatLockedfluid = null;
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.hatch.output")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.hatch.output.fluid",
                EnumChatFormatting.GOLD
                    + (mFluid == null ? StatCollector.translateToLocal("GT5U.infodata.hatch.output.fluid.none")
                        : mFluid.getLocalizedName())
                    + EnumChatFormatting.RESET),
            EnumChatFormatting.GREEN + formatNumber(mFluid == null ? 0 : mFluid.amount)
                + " L"
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + formatNumber(getCapacity())
                + " L"
                + EnumChatFormatting.RESET,
            (!isFluidLocked() || lockedFluidName == null)
                ? StatCollector.translateToLocal("GT5U.infodata.hatch.output.fluid.locked_to.none")
                : (StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.hatch.output.fluid.locked_to",
                    StatCollector.translateToLocal(
                        FluidRegistry.getFluidStack(lockedFluidName, 1)
                            .getUnlocalizedName()))) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(98, 16)
                .setSize(71, 45))
            .widget(new FluidLockWidget(this).setPos(149, 41))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.machines.hatch_output.lockfluid.label"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(101, 20))
            .widget(TextWidget.dynamicString(() -> {
                FluidStack fluidStack = FluidRegistry.getFluidStack(lockedFluidName, 1);
                return fluidStack != null ? fluidStack.getLocalizedName()
                    : StatCollector.translateToLocal("GT5U.machines.hatch_output.lockfluid.empty");
            })
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxWidth(65)
                .setPos(101, 30))
            .widget(new FakeSyncWidget.ByteSyncer(() -> mMode, val -> mMode = val));
    }
}
