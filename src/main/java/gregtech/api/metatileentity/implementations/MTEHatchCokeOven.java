package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTECokeOven;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchCokeOven extends MTEHatch {

    private enum Mode {

        Input((byte) 0, "Input"),
        OutputItem((byte) 1, "Output Items"),
        OutputFluid((byte) 2, "Output Fluids");

        private final byte index;
        private final String name;

        Mode(byte index, String name) {
            this.index = index;
            this.name = name;
        }

        private Mode next() {
            return switch (this) {
                case Input -> OutputItem;
                case OutputItem -> OutputFluid;
                case OutputFluid -> Input;
            };
        }

        private static Mode fromIndex(byte index) {
            return switch (index) {
                case 0 -> Input;
                case 1 -> OutputItem;
                case 2 -> OutputFluid;
                default -> throw new IllegalArgumentException("Invalid Mode index: " + index);
            };
        }

        private void saveNBTData(NBTTagCompound NBT) {
            NBT.setByte("inputMode", index);
        }

        private static Mode loadNBTData(NBTTagCompound NBT) {
            final byte index = NBT.getByte("inputMode");
            return fromIndex(index);
        }

        @Override
        public @NotNull String toString() {
            return switch (this) {
                case Input -> StatCollector.translateToLocal("GT5U.machines.coke_oven_hatch.mode.input");
                case OutputItem -> StatCollector.translateToLocal("GT5U.machines.coke_oven_hatch.mode.output");
                case OutputFluid -> StatCollector.translateToLocal("GT5U.machines.coke_oven_hatch.mode.output_fluid");
            };
        }

        public @NotNull String getLocalizedName() {
            return switch (this) {
                case Input -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.input");
                case OutputItem -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.output_item");
                case OutputFluid -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.output_fluid");
            };
        }
    }

    /** Fluid transfer rate in liters per second. */
    private static final int FLUID_TRANSFER_RATE = 1_000;

    private Mode mode = Mode.Input;
    private int controllerIndex = 0;
    private MTECokeOven activeController;
    private final ArrayList<MTECokeOven> controllers = new ArrayList<>();
    private boolean destroyed = false;

    public MTEHatchCokeOven(int ID, String name, String nameRegional) {
        super(
            ID,
            name,
            nameRegional,
            0,
            0,
            new String[] { "Hatch for automating the Coke Oven",
                EnumChatFormatting.STRIKETHROUGH + "------------------------------",
                EnumChatFormatting.GREEN + "Modes" + EnumChatFormatting.GRAY + ": Input, Output, Output (Fluids)",
                "Cycle mode with a Screwdriver", });
    }

    public MTEHatchCokeOven(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 0, description, textures);
    }

    @Override
    public MTEHatchCokeOven newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEHatchCokeOven(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.PickaxeLevel0.toTileEntityBaseType();
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        super.saveNBTData(NBT);
        mode.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        super.loadNBTData(NBT);
        mode = Mode.loadNBTData(NBT);
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        final NBTTagCompound data = new NBTTagCompound();
        mode.saveNBTData(data);
        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        mode = Mode.loadNBTData(data);
    }

    @Override
    public void onBlockDestroyed() {
        destroyed = true;
        super.onBlockDestroyed();
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(StatCollector.translateToLocalFormatted("GT5U.waila.mode", mode.getLocalizedName()));
    }

    /**
     * Executes the active controller’s action once per second and cycles to the next.
     * Transfers items or fluids forward depending on mode.
     */
    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isClientSide()) return;
        if (tick % 20 != 0) return;
        if (nextActiveController()) return;

        final ForgeDirection sideFront = baseMetaTileEntity.getFrontFacing();
        final ForgeDirection sideBack = baseMetaTileEntity.getBackFacing();

        switch (mode) {
            case Input -> {}
            case OutputItem -> {
                GTItemTransfer transfer = new GTItemTransfer();

                transfer.outOfMachine(this, sideFront);

                transfer.transfer();
            }
            case OutputFluid -> {
                final IFluidHandler target = baseMetaTileEntity.getITankContainerAtSide(sideFront);
                if (target == null) return;
                GTUtility.moveFluid(activeController, target, ForgeDirection.UNKNOWN, FLUID_TRANSFER_RATE, null);
            }
        }
    }

    /** Adds a controller the hatch is a part of. */
    public void addController(MTECokeOven controller) {
        if (controllers.contains(controller)) return;
        controllers.add(controller);
    }

    /** Sets the next active controller. */
    private boolean nextActiveController() {
        if (controllerIndex >= controllers.size()) {
            controllerIndex = 0;
            return true;
        }

        activeController = controllers.get(controllerIndex);

        if (activeController == null) {
            controllers.remove(controllerIndex);
            return true;
        }

        controllerIndex = (controllerIndex + 1) % controllers.size();
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public int getCapacity() {
        return mode == Mode.OutputFluid ? MTECokeOven.FLUID_CAPACITY : 0;
    }

    @Override
    public int getSizeInventory() {
        if (destroyed) return 0;
        return activeController != null ? 2 : 0;
    }

    @Override
    public @Nullable ItemStack getStackInSlot(int index) {
        if (activeController == null) return null;
        return activeController.getStackInSlot(index);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        return switch (mode) {
            case Input -> new int[] { MTECokeOven.INPUT_SLOT };
            case OutputItem -> new int[] { MTECokeOven.OUTPUT_SLOT };
            default -> null;
        };
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (activeController == null) return;
        activeController.setInventorySlotContents(index, stack);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, int ordinalSide) {
        if (mode != Mode.Input) return false;
        if (activeController == null) return false;
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity == null) return false;
        final ForgeDirection facing = baseMetaTileEntity.getFrontFacing();
        if (facing.ordinal() != ordinalSide) return false;
        return activeController.canInsertItem(index, itemStack, ForgeDirection.UNKNOWN.ordinal());
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, int ordinalSide) {
        if (activeController == null) return false;
        return activeController.canExtractItem(index, itemStack, ordinalSide);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer player, float x, float y, float z,
        ItemStack tool) {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity == null) return;
        if (baseMetaTileEntity.isClientSide()) return;
        mode = mode.next();
        baseMetaTileEntity.issueTileUpdate();
        GTUtility.sendChatToPlayer(player, mode.toString());
    }

    private static final ITexture TEXTURE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0));
    private static final ITexture[] TEXTURE_OUT = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_OUT) };
    private static final ITexture[] TEXTURE_OUT_INDICATOR = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_OUT),
        TextureFactory.of(ITEM_OUT_SIGN) };
    private static final ITexture[] TEXTURE_IN = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_IN) };
    private static final ITexture[] TEXTURE_IN_INDICATOR = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_IN),
        TextureFactory.of(ITEM_IN_SIGN) };
    private static final ITexture[] TEXTURE_OUT_FLUID = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_OUT) };
    private static final ITexture[] TEXTURE_OUT_FLUID_INDICATOR = { TEXTURE_CASING, TextureFactory.of(OVERLAY_PIPE_OUT),
        TextureFactory.of(FLUID_OUT_SIGN) };

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side != facing) return new ITexture[] { TEXTURE_CASING };

        return switch (mode) {
            case Input -> GTMod.proxy.mRenderIndicatorsOnHatch ? TEXTURE_IN_INDICATOR : TEXTURE_IN;
            case OutputItem -> GTMod.proxy.mRenderIndicatorsOnHatch ? TEXTURE_OUT_INDICATOR : TEXTURE_OUT;
            case OutputFluid -> GTMod.proxy.mRenderIndicatorsOnHatch ? TEXTURE_OUT_FLUID_INDICATOR : TEXTURE_OUT_FLUID;
        };
    }

    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return null;
    }

    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return null;
    }
}
