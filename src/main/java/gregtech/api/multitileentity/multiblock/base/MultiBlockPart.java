package gregtech.api.multitileentity.multiblock.base;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.enums.Textures.BlockIcons.VOID;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.NOTHING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import cpw.mods.fml.common.FMLLog;
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
import gregtech.api.multitileentity.base.NonTickableMultiTileEntity;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.enums.PartMode;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.net.data.CasingData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldHelper;
import gregtech.common.gui.PartGUIProvider;

public abstract class MultiBlockPart extends NonTickableMultiTileEntity
    implements IMultiBlockPart, PowerLogicHost, GUIHost {

    @Nonnull
    private static final ITexture OVERLAY_PIPE_IN_TEXTURE = TextureFactory.of(OVERLAY_PIPE_IN);
    @Nonnull
    private static final ITexture OVERLAY_PIPE_OUT_TEXTURE = TextureFactory.of(OVERLAY_PIPE_OUT);

    protected final List<PartMode> BASIC_MODES = new ArrayList<>(
        Arrays.asList(NOTHING, ENERGY_INPUT, ENERGY_OUTPUT, FLUID_INPUT, FLUID_OUTPUT, ITEM_INPUT, ITEM_OUTPUT));

    protected Set<MultiTileCasingPurpose> registeredPurposes = new HashSet<>();

    protected ChunkCoordinates targetPosition = null;

    protected int allowedModes = NOTHING.getValue(); // BITMASK - Modes allowed for this part
    protected PartMode mode = NOTHING; // Mode selected for this part

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

    public void setTarget(final IMultiBlockController newTarget, final int aAllowedModes) {
        final IMultiBlockController currentTarget = getTarget(false);
        if (currentTarget != null && currentTarget != newTarget) {
            for (final MultiTileCasingPurpose purpose : registeredPurposes) {
                unregisterPurpose(purpose);
            }
        }
        targetPosition = (newTarget == null ? null : newTarget.getCoords());
        allowedModes = aAllowedModes;
        if (newTarget != null) {
            // registerCovers(newTarget);
            registerPurposes();
        }
    }

    protected void registerPurpose(final MultiTileCasingPurpose purpose) {
        final IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.registerCaseWithPurpose(purpose, this);
            registeredPurposes.add(purpose);
        }
    }

    protected void unregisterPurpose(final MultiTileCasingPurpose purpose) {
        final IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.unregisterCaseWithPurpose(purpose, this);
        }
        registeredPurposes.remove(purpose);
    }

    @Nullable
    public IMultiBlockController getTarget(final boolean checkValidity) {
        if (targetPosition == null) {
            return null;
        }

        if (!worldObj.blockExists(targetPosition.posX, targetPosition.posY, targetPosition.posZ)) {
            return null;
        }
        final TileEntity te = worldObj.getTileEntity(targetPosition.posX, targetPosition.posY, targetPosition.posZ);
        IMultiBlockController target = null;
        if (te instanceof final IMultiBlockController targetFound) {
            target = targetFound;
        } else {
            targetPosition = null;
            return null;
        }

        if (checkValidity) {
            return target != null && target.checkStructure(false) ? target : null;
        }
        return target;
    }

    protected void registerPurposes() {
        for (final MultiTileCasingPurpose purpose : registeredPurposes) {
            registerPurpose(purpose);
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        if (aNBT.hasKey(NBT.ALLOWED_MODES)) allowedModes = aNBT.getInteger(NBT.ALLOWED_MODES);
        if (aNBT.hasKey(NBT.MODE)) setMode(PartMode.values()[aNBT.getInteger(NBT.MODE)]);
        if (aNBT.hasKey(NBT.TARGET)) {
            targetPosition = new ChunkCoordinates(
                aNBT.getInteger(NBT.TARGET_X),
                aNBT.getShort(NBT.TARGET_Y),
                aNBT.getInteger(NBT.TARGET_Z));
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
        if (modeSelected(ITEM_OUTPUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.ItemOutput);
        }
        if (modeSelected(FLUID_OUTPUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.FluidOutput);
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        if (allowedModes != NOTHING.getValue()) aNBT.setInteger(NBT.ALLOWED_MODES, allowedModes);
        if (mode.getValue() != 0) aNBT.setInteger(NBT.MODE, mode.ordinal());
        if (targetPosition != null) {
            aNBT.setBoolean(NBT.TARGET, true);
            aNBT.setInteger(NBT.TARGET_X, targetPosition.posX);
            aNBT.setShort(NBT.TARGET_Y, (short) targetPosition.posY);
            aNBT.setInteger(NBT.TARGET_Z, targetPosition.posZ);
        }
        if (lockedInventory != null) {
            aNBT.setString(NBT.LOCKED_INVENTORY, lockedInventory.toString());
        }
        if (mLockedInventoryIndex != 0) {
            aNBT.setInteger(NBT.LOCKED_INVENTORY_INDEX, mLockedInventoryIndex);
        }
        configurationTank.writeToNBT(aNBT, NBT.LOCKED_FLUID);
    }

    @Override
    public void setTargetPos(final ChunkCoordinates aTargetPos) {
        targetPosition = aTargetPos;
        final IMultiBlockController target = getTarget(false);
        setTarget(target, allowedModes);
    }

    @Override
    public ChunkCoordinates getTargetPos() {
        return targetPosition;
    }

    public void setMode(final PartMode mode) {
        if (this.mode == mode) return;
        if (modeSelected(FLUID_OUTPUT)) {
            unregisterPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUTPUT)) {
            unregisterPurpose(MultiTileCasingPurpose.ItemOutput);
        }
        this.mode = mode;
        markDirty();
        if (modeSelected(FLUID_OUTPUT)) {
            registerPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUTPUT)) {
            registerPurpose(MultiTileCasingPurpose.ItemOutput);
        }
    }

    @Nonnull
    protected PartMode getMode() {
        return mode;
    }

    public int getAllowedModes() {
        return allowedModes;
    }

    public void setAllowedModes(final int aAllowedModes) {
        allowedModes = aAllowedModes;
    }

    /**
     * True if `mode` is one of the allowed modes
     */
    public boolean hasMode(@Nonnull final PartMode mode) {
        // This is not sent to the client
        return (allowedModes & mode.getValue()) != 0;
    }

    /**
     * Returns true if the part has any of the modes provided, and that mode is the currently selected mode
     */
    public boolean modeSelected(@Nonnull final PartMode... modes) {
        for (final PartMode mode : modes) {
            if (hasMode(mode)) return true;
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        super.onNeighborBlockChange(block);
    }

    @Override
    public void onBlockBroken() {
        final IMultiBlockController target = getTarget(false);
        if (target != null) {
            // unregisterCovers(tTarget);
            target.onStructureChange();
        }
    }

    @Override
    public void onBlockPlaced() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity te = WorldHelper.getTileEntityAtSide(side, getWorldObj(), getXCoord(), getYCoord(), getZCoord());
            if (te instanceof final MultiBlockPart part) {
                final IMultiBlockController target = part.getTarget(false);
                if (target != null) target.onStructureChange();
            } else if (te instanceof final IMultiBlockController controller) {
                controller.onStructureChange();
            }
        }
    }

    // MultiTileBasicRender methods
    @Override
    @Nonnull
    protected ITexture getFrontTexture() {
        return TextureFactory.of(super.getFrontTexture(), getModeTexture());
    }

    @Nonnull
    protected ITexture getModeTexture() {
        return switch (getMode()) {
            case ITEM_INPUT -> TextureFactory.of(OVERLAY_PIPE_IN_TEXTURE, TextureFactory.of(ITEM_IN_SIGN));
            case ITEM_OUTPUT -> TextureFactory.of(OVERLAY_PIPE_IN_TEXTURE, TextureFactory.of(ITEM_OUT_SIGN));
            case FLUID_INPUT -> TextureFactory.of(OVERLAY_PIPE_IN_TEXTURE, TextureFactory.of(FLUID_IN_SIGN));
            case FLUID_OUTPUT -> TextureFactory.of(OVERLAY_PIPE_IN_TEXTURE,TextureFactory.of(FLUID_OUT_SIGN));
            case ENERGY_INPUT -> TextureFactory.of(OVERLAY_ENERGY_IN_MULTI);
            case ENERGY_OUTPUT -> TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI);
            default -> TextureFactory.of(VOID);
        };
    }

    protected String getModeName(@Nonnull final PartMode mode) {
        return switch (mode) {
            case ITEM_INPUT -> "Item Input";
            case ITEM_OUTPUT -> "Item Output";
            case FLUID_INPUT -> "Fluid Input";
            case FLUID_OUTPUT -> "Fluid Output";
            case ENERGY_INPUT -> "Energy Input";
            case ENERGY_OUTPUT -> "Energy Output";
            default -> "Unknown";
        };
    }

    @Nonnull
    protected PartMode getNextAllowedMode(final List<PartMode> allowedModes) {
        if (this.allowedModes == NOTHING.getValue()) return NOTHING;

        for (PartMode newMode : allowedModes) {
            if (newMode.getValue() > getMode().getValue()) return newMode;
        }
        // Nothing valid found
        return NOTHING;
    }

    @Override
    @Nonnull
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.part";
    }

    @Override
    public boolean shouldTick(final long tickTimer) {
        return modeSelected(ITEM_OUTPUT, FLUID_OUTPUT);
    }

    /**
     * TODO: Make sure the energy/item/fluid hatch is facing that way! or has that mode enabled on that side Check
     * SIDE_UNKNOWN for or coverbehavior
     */

    // #region Fluid - Depending on the part type - proxy it to the multiblock controller, if we have one
    @Override
    @Nullable
    public FluidInventoryLogic getFluidLogic(@Nonnull final ForgeDirection side, @Nonnull final InventoryType type) {
        if (side != getFacing() && side != ForgeDirection.UNKNOWN) return null;

        if (!modeSelected(FLUID_INPUT, FLUID_OUTPUT)) return null;

        final IMultiBlockController controller = getTarget(false);
        if (controller == null) return null;
        return controller
            .getFluidLogic(modeSelected(FLUID_INPUT) ? InventoryType.Input : InventoryType.Output, lockedInventory);
    }

    // #endregion Fluid

    // #region Energy - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    @Nonnull
    public PowerLogic getPowerLogic(@Nonnull final ForgeDirection side) {
        if (side != getFacing() && side != ForgeDirection.UNKNOWN) {
            return new NullPowerLogic();
        }

        if (!modeSelected(ENERGY_INPUT, ENERGY_OUTPUT)) {
            return new NullPowerLogic();
        }

        final IMultiBlockController controller = getTarget(true);
        if (controller == null) {
            return new NullPowerLogic();
        }
        return controller.getPowerLogic();
    }

    // #endregion Energy

    // #region Item - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    @Nullable
    public ItemInventoryLogic getItemLogic(@Nonnull final ForgeDirection side, @Nonnull final InventoryType unused) {
        if (side != getFacing() && side != ForgeDirection.UNKNOWN) return null;

        if (!modeSelected(ITEM_INPUT, ITEM_OUTPUT)) return null;

        final IMultiBlockController controller = getTarget(false);
        if (controller == null) return null;

        return controller
            .getItemLogic(modeSelected(ITEM_INPUT) ? InventoryType.Input : InventoryType.Output, lockedInventory);
    }

    @Override
    @Nullable
    public InventoryType getItemInventoryType() {
        if (!modeSelected(ITEM_INPUT, ITEM_OUTPUT)) return InventoryType.Both;
        return modeSelected(ITEM_INPUT) ? InventoryType.Input : InventoryType.Output;
    }

    // #endregion Item

    // === Modular UI ===
    public String getLocalName() {
        if (modeSelected(ITEM_INPUT)) return "Input Inventory";
        if (modeSelected(ITEM_OUTPUT)) return "Output Inventory";
        if (modeSelected(FLUID_INPUT)) return "Fluid Input Hatch";
        if (modeSelected(FLUID_OUTPUT)) return "Fluid Output Hatch";

        return "Unknown";
    }

    protected boolean isWrongFluid(final Fluid fluid) {
        if (fluid == null) {
            return true;
        }
        final Fluid lockedFluid = getLockedFluid();
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

    protected boolean canOpenControllerGui() {
        return true;
    }

    public void addToolTips(final List<String> list, final ItemStack stack, final boolean f3_h) {
        list.add("A MultiTileEntity Casing");
    }

    public String getInventoryName() {
        final IMultiBlockController controller = getTarget(false);
        if (controller == null) return "";
        if (modeSelected(ITEM_INPUT, ITEM_OUTPUT)) {
            final InventoryType type = modeSelected(ITEM_INPUT) ? InventoryType.Input : InventoryType.Output;
            final ItemInventoryLogic itemLogic = controller.getItemLogic(type, lockedInventory);
            return itemLogic.getDisplayName();
        }
        if (modeSelected(FLUID_INPUT, FLUID_OUTPUT)) {
            final InventoryType type = modeSelected(FLUID_INPUT) ? InventoryType.Input : InventoryType.Output;
            final FluidInventoryLogic fluidLogic = controller.getFluidLogic(type, lockedInventory);
            return fluidLogic.getDisplayName();
        }
        return "";
    }

    @Override
    @Nonnull
    public ForgeDirection getPowerOutputSide() {
        if (!modeSelected(ENERGY_OUTPUT)) return ForgeDirection.UNKNOWN;
        return getFacing();
    }

    @Nonnull
    protected GUIProvider<?> createGUIProvider() {
        return new PartGUIProvider<>(this);
    }

    @Override
    @Nonnull
    public GUIProvider<?> getGUI(@Nonnull final UIBuildContext uiContext) {
        final IMultiBlockController controller = getTarget(false);
        if (controller == null) return guiProvider;
        if (!modeSelected(NOTHING, ENERGY_INPUT, ENERGY_OUTPUT)) return guiProvider;
        if (!canOpenControllerGui()) return guiProvider;
        if (uiContext.getPlayer()
            .isSneaking()) return guiProvider;
        final GUIProvider<?> controllerGUI = controller.getGUI(uiContext);
        return controllerGUI;
    }

    @Override
    public ItemStack getAsItem() {
        return MultiTileEntityRegistry.getRegistry(getRegistryId())
            .getItem(getMetaId());
    }

    @Override
    public String getMachineName() {
        return StatCollector.translateToLocal(getAsItem().getUnlocalizedName());
    }

    @Override
    protected boolean onRightClick(EntityPlayer player, ForgeDirection side, ForgeDirection wrenchSide) {
        if (!shouldOpen()) return false;
        UIInfos.openClientUI(player, this::createWindow);
        return true;
    }

    @Override
    protected boolean onRightClickWithMallet(EntityPlayer player, ForgeDirection side, ForgeDirection wrenchSide) {
        if (allowedModes == NOTHING.getValue()) return true;
        setMode(getNextAllowedMode(PartMode.getPartModesFromBitmask(allowedModes)));
        if (player.isSneaking()) {
            setFacing(wrenchSide);
        }
        if (player instanceof EntityPlayerMP mpPlayer) {
            GTNHLib.proxy.sendMessageAboveHotbar(
                mpPlayer,
                new ChatComponentText(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("gt.multiblockpart.setmode.text")
                        + " "
                        + EnumChatFormatting.GOLD
                        + getMode().getTranslated()),
                100,
                true,
                true);
        }
        issueClientUpdate();
        return true;
    }

    @Override
    public void getGraphicPacketData(GT_Packet_MultiTileEntity packet) {
        super.getGraphicPacketData(packet);
        packet.addData(
            new CasingData(
                getMode() != null ? getMode().ordinal() : NOTHING.ordinal(),
                getAllowedModes(),
                targetPosition != null ? targetPosition.posX : 0,
                targetPosition != null ? targetPosition.posY : 0,
                targetPosition != null ? targetPosition.posZ : 0));
    }
}
