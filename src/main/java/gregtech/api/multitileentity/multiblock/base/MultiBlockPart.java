package gregtech.api.multitileentity.multiblock.base;

import static com.google.common.math.LongMath.log2;
import static gregtech.api.enums.GT_Values.B;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.InventoryType;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.gui.GUIHost;
import gregtech.api.gui.GUIProvider;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.NullPowerLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.WeakTargetRef;
import gregtech.api.multitileentity.base.NonTickableMultiTileEntity;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;
import gregtech.common.gui.PartGUIProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MultiBlockPart extends NonTickableMultiTileEntity
    implements IMultiBlockPart, PowerLogicHost, GUIHost {

    public static final int NOTHING = 0, ENERGY_IN = B[0], ENERGY_OUT = B[1], FLUID_IN = B[2], FLUID_OUT = B[3],
        ITEM_IN = B[4], ITEM_OUT = B[5];

    protected final List<Integer> BASIC_MODES = new ArrayList<>(
        Arrays.asList(NOTHING, ENERGY_IN, ENERGY_OUT, FLUID_IN, FLUID_OUT, ITEM_IN, ITEM_OUT));

    protected Set<MultiTileCasingPurpose> registeredPurposes = new HashSet<>();

    protected final WeakTargetRef<IMultiBlockController> controller = new WeakTargetRef<>(
        IMultiBlockController.class,
        false);

    protected int allowedModes = NOTHING; // BITMASK - Modes allowed for this part
    protected int mode = 0; // Mode selected for this part

    protected UUID lockedInventory;
    protected int mLockedInventoryIndex = 0;
    protected FluidTankGT configurationTank = new FluidTankGT();

    @Nonnull
    protected final GUIProvider<?> guiProvider = createGUIProvider();

    /**
     * What Part Tier is this part? All Basic Casings are Tier 1, and will allow: Energy, Item, Fluid input/output. Some
     * of the more advanced modes can be set to require a higher tier part.
     */
    public int getPartTier() {
        return 1;
    }

    @Override
    public UUID getLockedInventory() {
        return lockedInventory;
    }

    public void setTarget(IMultiBlockController newController, int aAllowedModes) {
        final IMultiBlockController currentController = getTarget(false);
        if (currentController != null && currentController != newController) {
            for (MultiTileCasingPurpose purpose : registeredPurposes) {
                unregisterPurpose(purpose);
            }
        }

        allowedModes = aAllowedModes;
        if (newController != currentController) {
            registerCovers(newController);
            registerPurposes();
        }
    }

    protected void registerPurpose(MultiTileCasingPurpose purpose) {
        IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.registerCaseWithPurpose(purpose, this);
            registeredPurposes.add(purpose);
        }
    }

    protected void unregisterPurpose(MultiTileCasingPurpose purpose) {
        IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.unregisterCaseWithPurpose(purpose, this);
        }
        registeredPurposes.remove(purpose);
    }

    @Override
    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            tList.add("Has controller");
        } else {
            tList.add("No Controller");
        }
        tList.add("Casing Mode: " + getModeName(mode));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(String.format("Mode: %s", getModeName(mode)));
        if (modeSelected(FLUID_OUT)) {
            if (configurationTank != null && configurationTank.get() != null) {
                currentTip.add(
                    String.format(
                        "Locked to: %s",
                        configurationTank.get()
                            .getLocalizedName()));
            } else {
                currentTip.add("Locked to: Nothing");
            }
        }
    }

    public IMultiBlockController getTarget(boolean aCheckValidity) {
        final IMultiBlockController res = controller.get();
        if (res != null && aCheckValidity) {
            return res.checkStructure(false) ? res : null;
        }
        return res;
    }

    public void registerCovers(IMultiBlockController controller) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
                controller.registerCoveredPartOnSide(side, this);
            }
        }
    }

    protected void registerPurposes() {
        for (MultiTileCasingPurpose purpose : registeredPurposes) {
            registerPurpose(purpose);
        }
    }

    @Override
    public void setCoverItemAtSide(ForgeDirection side, ItemStack aCover) {
        super.setCoverItemAtSide(side, aCover);
        // TODO: Filter on tickable covers
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget == null) {
            return;
        }

        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
            tTarget.registerCoveredPartOnSide(side, this);
        }

    }

    public void unregisterCovers(IMultiBlockController controller) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (getCoverInfoAtSide(side).isValid()) {
                controller.unregisterCoveredPartOnSide(side, this);
            }
        }
    }

    @Override
    public boolean dropCover(ForgeDirection side, ForgeDirection droppedSide, boolean aForced) {
        final boolean res = super.dropCover(side, droppedSide, aForced);
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget != null) {
            tTarget.unregisterCoveredPartOnSide(side, this);
        }
        return res;
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        if (aNBT.hasKey(NBT.ALLOWED_MODES)) allowedModes = aNBT.getInteger(NBT.ALLOWED_MODES);
        if (aNBT.hasKey(NBT.MODE)) setMode(aNBT.getByte(NBT.MODE));
        if (aNBT.hasKey(NBT.TARGET)) {
            controller
                .setPosition(aNBT.getInteger(NBT.TARGET_X), aNBT.getShort(NBT.TARGET_Y), aNBT.getInteger(NBT.TARGET_Z));
            controller.setWorld(worldObj);
        }
        if (aNBT.hasKey(NBT.LOCKED_INVENTORY)) {
            lockedInventory = UUID.fromString(aNBT.getString(NBT.LOCKED_INVENTORY));
        }
        if (aNBT.hasKey(NBT.LOCKED_INVENTORY_INDEX)) {
            mLockedInventoryIndex = aNBT.getInteger(NBT.LOCKED_INVENTORY_INDEX);
        }
        if (aNBT.hasKey(NBT.LOCKED_FLUID)) {
            configurationTank.readFromNBT(aNBT, NBT.LOCKED_FLUID);
        }
        if (modeSelected(ITEM_OUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.ItemOutput);
        }
        if (modeSelected(FLUID_OUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.FluidOutput);
        }
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        if (allowedModes != NOTHING) nbt.setInteger(NBT.ALLOWED_MODES, allowedModes);
        if (mode != 0) nbt.setInteger(NBT.MODE, mode);

        final ChunkCoordinates pos = controller.getPosition();
        if (pos.posY >= 0) {
            // Valid position
            nbt.setBoolean(NBT.TARGET, true);
            nbt.setInteger(NBT.TARGET_X, pos.posX);
            nbt.setShort(NBT.TARGET_Y, (short) pos.posY);
            nbt.setInteger(NBT.TARGET_Z, pos.posZ);
        }

        if (lockedInventory != null) {
            nbt.setString(NBT.LOCKED_INVENTORY, lockedInventory.toString());
        }
        if (mLockedInventoryIndex != 0) {
            nbt.setInteger(NBT.LOCKED_INVENTORY_INDEX, mLockedInventoryIndex);
        }
        configurationTank.writeToNBT(nbt, NBT.LOCKED_FLUID);
    }

    @Override
    public void setLockedInventoryIndex(int index) {
        mLockedInventoryIndex = index;
    }

    @Override
    public int getLockedInventoryIndex() {
        return mLockedInventoryIndex;
    }

    @Override
    public ChunkCoordinates getTargetPos() {
        return controller.getPosition();
    }

    @Override
    public void setMode(int mode) {
        if (this.mode == mode) return;
        if (modeSelected(FLUID_OUT)) {
            unregisterPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUT)) {
            unregisterPurpose(MultiTileCasingPurpose.ItemOutput);
        }
        this.mode = mode;
        if (modeSelected(FLUID_OUT)) {
            registerPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUT)) {
            registerPurpose(MultiTileCasingPurpose.ItemOutput);
        }
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public int getAllowedModes() {
        return allowedModes;
    }

    @Override
    public void setAllowedModes(int aAllowedModes) {
        allowedModes = aAllowedModes;
    }

    /**
     * True if `mode` is one of the allowed modes
     */
    public boolean hasMode(int mode) {
        // This is not sent to the client
        return (allowedModes & mode) != 0;
    }

    /**
     * Returns true if the part has any of the modes provided, and that mode is the currently selected mode
     */
    public boolean modeSelected(int... modes) {
        for (int mode : modes) {
            if (hasMode(mode) && this.mode == getModeOrdinal(mode)) return true;
        }
        return false;
    }

    @Override
    public boolean onBlockBroken() {
        final IMultiBlockController tTarget = getTarget(false);
        if (tTarget != null) {
            unregisterCovers(tTarget);
            tTarget.onStructureChange();
        }
        return false;
    }

    @Override
    public void onBlockAdded() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity te = getTileEntityAtSide(side);
            if (te instanceof MultiBlockPart part) {
                final IMultiBlockController tController = part.getTarget(false);
                if (tController != null) tController.onStructureChange();
            } else if (te instanceof IMultiBlockController controller) {
                controller.onStructureChange();
            }
        }
    }

    @Override
    public ITexture getTexture(ForgeDirection side) {
        ITexture texture = super.getTexture(side);
        if (mode != 0 && side == facing) {
            if (mode == getModeOrdinal(ITEM_IN)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_IN),
                    TextureFactory.of(ITEM_IN_SIGN),
                    getCoverTexture(side));
            }
            if (mode == getModeOrdinal(ITEM_OUT)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_OUT),
                    TextureFactory.of(ITEM_OUT_SIGN),
                    getCoverTexture(side));
            }
            if (mode == getModeOrdinal(FLUID_IN)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_IN),
                    TextureFactory.of(FLUID_IN_SIGN),
                    getCoverTexture(side));
            }
            if (mode == getModeOrdinal(FLUID_OUT)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_OUT),
                    TextureFactory.of(FLUID_OUT_SIGN),
                    getCoverTexture(side));
            }
            if (mode == getModeOrdinal(ENERGY_IN)) {
                return TextureFactory.of(texture, TextureFactory.of(OVERLAY_ENERGY_IN_MULTI), getCoverTexture(side));
            }
            if (mode == getModeOrdinal(ENERGY_OUT)) {
                return TextureFactory.of(texture, TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI), getCoverTexture(side));
            }
        }

        return TextureFactory.of(texture, getCoverTexture(side));
    }

    protected String getModeName(int aMode) {
        if (aMode == NOTHING) return "Nothing";
        if (aMode == getModeOrdinal(ITEM_IN)) return "Item Input";
        if (aMode == getModeOrdinal(ITEM_OUT)) return "Item Output";
        if (aMode == getModeOrdinal(FLUID_IN)) return "Fluid Input";
        if (aMode == getModeOrdinal(FLUID_OUT)) return "Fluid Output";
        if (aMode == getModeOrdinal(ENERGY_IN)) return "Energy Input";
        if (aMode == getModeOrdinal(ENERGY_OUT)) return "Energy Output";
        return "Unknown";
    }

    protected byte getModeOrdinal(int aMode) {
        // log2 returns the bit position of the only bit set, add 1 to account for 0 being NOTHING
        // NOTE: Must be a power of 2 (single bit)
        return (byte) (log2(aMode, RoundingMode.UNNECESSARY) + 1);
    }

    protected byte getNextAllowedMode(List<Integer> allowedModes) {
        if (this.allowedModes == NOTHING) return NOTHING;

        final int numModes = allowedModes.size();
        for (byte i = 1; i <= numModes; i++) {
            final byte curMode = (byte) ((mode + i) % numModes);
            if (curMode == NOTHING || hasMode(1 << (curMode - 1))) return curMode;
        }
        // Nothing valid found
        return 0;
    }

    @Override
    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        if (allowedModes == NOTHING) return true;
        if (mode == NOTHING) {
            facing = wrenchSide;
        }
        setMode(getNextAllowedMode(BASIC_MODES));
        if (aPlayer.isSneaking()) {
            facing = wrenchSide;
        }
        GT_Utility.sendChatToPlayer(aPlayer, "Mode set to `" + getModeName(mode) + "' (" + mode + ")");
        sendClientData((EntityPlayerMP) aPlayer);
        return true;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.part";
    }

    @Override
    public boolean shouldTick(long tickTimer) {
        return modeSelected(ITEM_OUT, FLUID_OUT);
    }

    /**
     * TODO: Make sure the energy/item/fluid hatch is facing that way! or has that mode enabled on that side Check
     * SIDE_UNKNOWN for or coverbehavior
     */

    // #region Fluid - Depending on the part type - proxy it to the multiblock controller, if we have one
    @Override
    @Nullable
    public FluidInventoryLogic getFluidLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type) {
        if (side != facing && side != ForgeDirection.UNKNOWN) return null;

        if (!modeSelected(FLUID_IN, FLUID_OUT)) return null;

        IMultiBlockController controller = getTarget(false);
        if (controller == null) return null;
        return controller
            .getFluidLogic(modeSelected(FLUID_IN) ? InventoryType.Input : InventoryType.Output, lockedInventory);
    }

    // #endregion Fluid

    // #region Energy - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    @Nonnull
    public PowerLogic getPowerLogic(@Nonnull ForgeDirection side) {
        if (side != facing && side != ForgeDirection.UNKNOWN) {
            return new NullPowerLogic();
        }

        if (!modeSelected(ENERGY_IN, ENERGY_OUT)) {
            return new NullPowerLogic();
        }

        final IMultiBlockController controller = getTarget(true);
        if (controller == null) {
            return new NullPowerLogic();
        }
        return controller.getPowerLogic();
    }

    @Override
    public boolean isEnetInput() {
        return modeSelected(ENERGY_IN);
    }

    @Override
    public boolean isEnetOutput() {
        return modeSelected(ENERGY_OUT);
    }

    // #endregion Energy

    // #region Item - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    @Nullable
    public ItemInventoryLogic getItemLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType unused) {
        if (side != facing && side != ForgeDirection.UNKNOWN) return null;

        if (!modeSelected(ITEM_IN, ITEM_OUT)) return null;

        final IMultiBlockController controller = getTarget(false);
        if (controller == null) return null;

        return controller
            .getItemLogic(modeSelected(ITEM_IN) ? InventoryType.Input : InventoryType.Output, lockedInventory);
    }

    @Override
    @Nullable
    public InventoryType getItemInventoryType() {
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return InventoryType.Both;
        return modeSelected(ITEM_IN) ? InventoryType.Input : InventoryType.Output;
    }

    // #endregion Item

    // === Modular UI ===
    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public String getLocalName() {
        if (modeSelected(ITEM_IN)) return "Input Inventory";
        if (modeSelected(ITEM_OUT)) return "Output Inventory";
        if (modeSelected(FLUID_IN)) return "Fluid Input Hatch";
        if (modeSelected(FLUID_OUT)) return "Fluid Output Hatch";

        return "Unknown";
    }

    @Override
    public boolean hasGui(ForgeDirection side) {
        if (modeSelected(ENERGY_IN, ENERGY_OUT) && facing == side) {
            return false;
        }
        return getTarget(true) != null;
    }

    protected boolean isWrongFluid(Fluid fluid) {
        if (fluid == null) {
            return true;
        }
        Fluid lockedFluid = getLockedFluid();
        if (lockedFluid != null) {
            return !fluid.equals(lockedFluid);
        }
        return false;
    }

    protected Fluid getLockedFluid() {
        if (configurationTank.get() != null && configurationTank.get()
            .getFluid() != null) {
            return configurationTank.get()
                .getFluid();
        }
        return null;
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        IMultiBlockController controller = getTarget(false);
        if (controller == null) {
            return;
        }
        if ((modeSelected(ITEM_IN, ITEM_OUT))) {
            builder.widget(
                controller
                    .getItemLogic(modeSelected(ITEM_IN) ? InventoryType.Input : InventoryType.Output, lockedInventory)
                    .getGuiPart()
                    .setSize(18 * 4 + 4, 18 * 5)
                    .setPos(52, 7));
        }

        if ((modeSelected(FLUID_IN, FLUID_OUT))) {
            builder.widget(
                controller
                    .getFluidLogic(modeSelected(FLUID_IN) ? InventoryType.Input : InventoryType.Output, lockedInventory)
                    .getGuiPart()
                    .setSize(18 * 4 + 4, 18 * 5)
                    .setPos(52, 7));
        }
    }

    protected boolean canOpenControllerGui() {
        return true;
    }

    @Override
    protected int getGUIHeight() {
        return super.getGUIHeight() + 20;
    }

    @Override
    public void addGregTechLogo(Builder builder) {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 74));
        } else if (modeSelected(FLUID_IN, FLUID_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 82));
        } else {
            super.addGregTechLogo(builder);
        }
    }

    @Override
    public void addToolTips(List<String> list, ItemStack stack, boolean f3_h) {
        list.add("A MultiTileEntity Casing");
    }

    public String getInventoryName() {
        IMultiBlockController controller = getTarget(false);
        if (controller == null) return "";
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            InventoryType type = modeSelected(ITEM_IN) ? InventoryType.Input : InventoryType.Output;
            ItemInventoryLogic itemLogic = controller.getItemLogic(type, lockedInventory);
            return itemLogic.getDisplayName();
        }
        if (modeSelected(FLUID_IN, FLUID_OUT)) {
            InventoryType type = modeSelected(FLUID_IN) ? InventoryType.Input : InventoryType.Output;
            FluidInventoryLogic fluidLogic = controller.getFluidLogic(type, lockedInventory);
            return fluidLogic.getDisplayName();
        }
        return "";
    }

    @Override
    @Nonnull
    public ForgeDirection getPowerOutputSide() {
        if (!modeSelected(ENERGY_OUT)) return ForgeDirection.UNKNOWN;
        return facing;
    }

    @Nonnull
    protected GUIProvider<?> createGUIProvider() {
        return new PartGUIProvider<>(this);
    }

    @Override
    @Nonnull
    public GUIProvider<?> getGUI(@Nonnull UIBuildContext uiContext) {
        IMultiBlockController controller = getTarget(false);
        if (controller == null) return guiProvider;
        if (!modeSelected(NOTHING, ENERGY_IN, ENERGY_OUT)) return guiProvider;
        if (!canOpenControllerGui()) return guiProvider;
        if (uiContext.getPlayer()
            .isSneaking()) return guiProvider;
        GUIProvider<?> controllerGUI = controller.getGUI(uiContext);
        return controllerGUI;
    }

    @Override
    public ItemStack getAsItem() {
        return MultiTileEntityRegistry.getRegistry(getMultiTileEntityRegistryID())
            .getItem(getMultiTileEntityID());
    }

    @Override
    public String getMachineName() {
        return StatCollector.translateToLocal(getAsItem().getUnlocalizedName());
    }

}
