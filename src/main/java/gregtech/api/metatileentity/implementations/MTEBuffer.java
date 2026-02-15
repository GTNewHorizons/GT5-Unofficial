package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_DOWN;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_DOWN_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_LEFT;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_LEFT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_RIGHT;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_RIGHT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_UP;
import static gregtech.api.enums.Textures.BlockIcons.ARROW_UP_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.api.util.locser.LocSerNumber;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEBuffer extends MTETieredMachineBlock implements IAddUIWidgets {

    private static final int OUTPUT_INDEX = 0;
    private static final int ARROW_RIGHT_INDEX = 1;
    private static final int ARROW_DOWN_INDEX = 2;
    private static final int ARROW_LEFT_INDEX = 3;
    private static final int ARROW_UP_INDEX = 4;
    private static final int FRONT_INDEX = 5;

    private static final int NBT_INTEGER = 3;

    private static final String EMIT_ENERGY_TOOLTIP = "GT5U.machines.emit_energy.tooltip";
    private static final String EMIT_REDSTONE_IF_FULL_TOOLTIP = "GT5U.machines.emit_redstone_if_full.tooltip";
    private static final String INVERT_REDSTONE_TOOLTIP = "GT5U.machines.invert_redstone.tooltip";
    private static final String STOCKING_MODE_TOOLTIP = "GT5U.machines.buffer_stocking_mode.tooltip";
    private static final String SORTING_MODE_TOOLTIP = "GT5U.machines.sorting_mode.tooltip";
    private static final String TARGET_STACK_SIZE_TOOLTIP = "GT5U.machines.target_stack_size.tooltip";
    private static final int BUTTON_SIZE = 18;

    public int mMaxStackSize = 64;
    public static int MAX = 8;
    public boolean bOutput = false, bRedstoneIfFull = false, bInvert = false, bStockingMode = false,
        bSortStacks = false;
    public int mSuccess = 0, mTargetStackSize = 0;
    private int uiButtonCount = 0;

    public MTEBuffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEBuffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[ForgeDirection.VALID_DIRECTIONS.length][17][];
        ITexture tIcon = getOverlayIcon();
        ITexture tOut = TextureFactory.of(OVERLAY_PIPE_OUT);
        ITexture tUp = TextureFactory.of(
            TextureFactory.of(ARROW_UP),
            TextureFactory.builder()
                .addIcon(ARROW_UP_GLOW)
                .glow()
                .build());
        ITexture tDown = TextureFactory.of(
            TextureFactory.of(ARROW_DOWN),
            TextureFactory.builder()
                .addIcon(ARROW_DOWN_GLOW)
                .glow()
                .build());
        ITexture tLeft = TextureFactory.of(
            TextureFactory.of(ARROW_LEFT),
            TextureFactory.builder()
                .addIcon(ARROW_LEFT_GLOW)
                .glow()
                .build());
        ITexture tRight = TextureFactory.of(
            TextureFactory.of(ARROW_RIGHT),
            TextureFactory.builder()
                .addIcon(ARROW_RIGHT_GLOW)
                .glow()
                .build());
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[OUTPUT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tOut };
            rTextures[ARROW_RIGHT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tRight, tIcon };
            rTextures[ARROW_DOWN_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tDown, tIcon };
            rTextures[ARROW_LEFT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tLeft, tIcon };
            rTextures[ARROW_UP_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tUp, tIcon };
            rTextures[FRONT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], tIcon };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        colorIndex = colorIndex + 1;
        if (sideDirection == facingDirection) return mTextures[FRONT_INDEX][colorIndex];
        if (sideDirection.getOpposite() == facingDirection) return mTextures[OUTPUT_INDEX][colorIndex];
        switch (facingDirection) {
            case DOWN -> {
                return mTextures[ARROW_UP_INDEX][colorIndex]; // ARROW_UP
            }
            case UP -> {
                return mTextures[ARROW_DOWN_INDEX][colorIndex]; // ARROW_DOWN
            }
            case NORTH -> {
                switch (sideDirection) {
                    case DOWN, UP -> {
                        return mTextures[ARROW_DOWN_INDEX][colorIndex]; // ARROW_DOWN
                    }
                    case WEST -> {
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    }
                    case EAST -> {
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    }
                    default -> {}
                }
            }
            case SOUTH -> {
                switch (sideDirection) {
                    case DOWN, UP -> {
                        return mTextures[ARROW_UP_INDEX][colorIndex]; // ARROW_UP
                    }
                    case WEST -> {
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    }
                    case EAST -> {
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    }
                    default -> {}
                }
            }
            case WEST -> {
                switch (sideDirection) {
                    case UP, SOUTH -> {
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    }
                    case DOWN, NORTH -> {
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    }
                    default -> {}
                }
            }
            case EAST -> {
                switch (sideDirection) {
                    case UP, SOUTH -> {
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    }
                    case DOWN, NORTH -> {
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    }
                    default -> {}
                }
            }
            default -> {}
        }
        return mTextures[FRONT_INDEX][colorIndex];
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < mInventory.length - 1;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return !isOutputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return getBaseMetaTileEntity().getBackFacing() == side;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512L;
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 50L;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        // Return full value if we're an item and don't exist in the world for tooltip purposes
        return getBaseMetaTileEntity().getWorld() == null || bOutput ? V[mTier] : 0L;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return 2;
    }

    public abstract ITexture getOverlayIcon();

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("bInvert", bInvert);
        aNBT.setBoolean("bOutput", bOutput);
        aNBT.setBoolean("bRedstoneIfFull", bRedstoneIfFull);
        aNBT.setBoolean("bStockingMode", bStockingMode);
        aNBT.setInteger("mTargetStackSize", mTargetStackSize);
        aNBT.setBoolean("bSortStacks", bSortStacks);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mTargetStackSize", mTargetStackSize);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        bInvert = aNBT.getBoolean("bInvert");
        bOutput = aNBT.getBoolean("bOutput");
        bRedstoneIfFull = aNBT.getBoolean("bRedstoneIfFull");
        bSortStacks = aNBT.getBoolean("bSortStacks");
        bStockingMode = aNBT.getBoolean("bStockingMode");
        mTargetStackSize = aNBT.getInteger("mTargetStackSize");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (mTargetStackSize > 0) aNBT.setInteger("mTargetStackSize", mTargetStackSize);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        if (!stack.hasTagCompound()) {
            return;
        }
        addAdditionalTooltipInformation(stack.getTagCompound(), tooltip);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        addAdditionalTooltipInformation(accessor.getNBTData(), currentTip);
    }

    public void addAdditionalTooltipInformation(NBTTagCompound nbt, List<String> tooltip) {
        if (nbt.hasKey("mTargetStackSize", NBT_INTEGER)) {
            addTargetStackTooltip(nbt.getInteger("mTargetStackSize"), tooltip);
        }
    }

    public void addTargetStackTooltip(int targetStackSize, List<String> tooltip) {
        if (targetStackSize > 0) {
            tooltip.add(GTUtility.translate(TARGET_STACK_SIZE_TOOLTIP) + String.format(": %s", targetStackSize));
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        ForgeDirection wrenchingSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);
        onScrewdriverRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    public void onScrewdriverRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (wrenchingSide == getBaseMetaTileEntity().getBackFacing()) {

            mTargetStackSize = (byte) ((mTargetStackSize + (aPlayer.isSneaking() ? -1 : 1)) % 65);
            if (mTargetStackSize < 0) {
                mTargetStackSize = mMaxStackSize;
            }
            if (mTargetStackSize == 0) {
                GTUtility.sendChatTrans(aPlayer, "GT5U.chat.buffer.not_regulate");
            } else {
                GTUtility.sendChatLocSer(aPlayer, "GT5U.chat.buffer.regulate", new LocSerNumber(mTargetStackSize));
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        wrenchingSide = wrenchingSide.getOpposite();
        if (getBaseMetaTileEntity().isValidFacing(wrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(wrenchingSide);
            return true;
        }
        return super.onWrenchRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    protected void handleRedstoneOutput(IGregTechTileEntity aBaseMetaTileEntity) {
        int redstoneOutput = getRedstoneOutput();
        Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
            .forEach(side -> aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) redstoneOutput));
    }

    protected int getRedstoneOutput() {
        return (!bRedstoneIfFull || (bInvert ^ hasEmptySlots())) ? 0 : 15;
    }

    private boolean hasEmptySlots() {
        return IntStream.range(0, mInventory.length)
            .anyMatch(i -> isValidSlot(i) && mInventory[i] == null);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide()
            && (aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()
                || aTimer % 200 == 0
                || mSuccess > 0)) {
            mSuccess--;
            moveItems(aBaseMetaTileEntity, aTimer);
            handleRedstoneOutput(aBaseMetaTileEntity);
        }
    }

    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        moveItems(aBaseMetaTileEntity, aTimer, 1);
    }

    protected void moveItems(IGregTechTileEntity igte, long ignoredTimer, int stacks) {
        GTItemTransfer transfer = new GTItemTransfer();

        transfer.source(igte, ForgeDirection.UNKNOWN);
        transfer.sink(igte.getTileEntityAtSide(igte.getBackFacing()), igte.getFrontFacing());

        transfer.setStacksToTransfer(stacks);

        if (mTargetStackSize > 0) {
            if (bStockingMode) {
                ItemSink sink = transfer.getSink();

                if (sink == null) return;

                OptionalInt stored = sink.getStoredItemsInSink(null);

                if (!stored.isPresent()) return;

                int toTransfer = mTargetStackSize - stored.getAsInt();

                transfer.setMaxTotalTransferred(toTransfer);
            } else {
                transfer.setFilter(stack -> stack.getStackSize() >= mTargetStackSize);
                transfer.setMaxItemsPerTransfer(mTargetStackSize);
            }
        }

        if (transfer.transfer() > 0 || igte.hasInventoryBeenModified()) {
            mSuccess = 50;

            GTUtility.cleanInventory(this);
            if (bSortStacks) {
                GTUtility.compactInventory(this);
            }
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side != aBaseMetaTileEntity.getBackFacing();
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        if (entityPlayer.isSneaking()) {
            // I was so proud of all this but I literally just copied code from OutputBus
            bSortStacks = !bSortStacks;
            GTUtility.sendChatTrans(
                entityPlayer,
                bSortStacks ? "GT5U.chat.buffer.sort_mode.enable" : "GT5U.chat.buffer.sort_mode.disable");
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ, aTool);
    }

    protected void addEmitEnergyButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bOutput,
                val -> bOutput = val,
                GTUITextures.OVERLAY_BUTTON_EMIT_ENERGY,
                this::getEmitEnergyButtonTooltip));
    }

    private GTTooltipDataCache.TooltipData getEmitEnergyButtonTooltip() {
        return mTooltipCache.getData(
            EMIT_ENERGY_TOOLTIP,
            EnumChatFormatting.GREEN + formatNumber(V[mTier])
                + " ("
                + GTUtility.getColoredTierNameFromTier(mTier)
                + EnumChatFormatting.GREEN
                + ")"
                + EnumChatFormatting.GRAY,
            maxAmperesOut());
    }

    protected void addEmitRedstoneIfFullButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bRedstoneIfFull,
                val -> bRedstoneIfFull = val,
                GTUITextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                this::getEmitRedstoneIfFullButtonTooltip).setUpdateTooltipEveryTick(true));
    }

    private GTTooltipDataCache.TooltipData getEmitRedstoneIfFullButtonTooltip() {
        return mTooltipCache.getUncachedTooltipData(
            EMIT_REDSTONE_IF_FULL_TOOLTIP,
            StatCollector.translateToLocal(hasEmptySlots() ? "gui.yes" : "gui.no"),
            getRedstoneOutput());
    }

    protected void addInvertRedstoneButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bInvert,
                val -> bInvert = val,
                GTUITextures.OVERLAY_BUTTON_INVERT_REDSTONE,
                () -> mTooltipCache.getData(INVERT_REDSTONE_TOOLTIP)));
    }

    protected void addStockingModeButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bStockingMode,
                val -> bStockingMode = val,
                GTUITextures.OVERLAY_BUTTON_STOCKING_MODE,
                () -> mTooltipCache.getData(STOCKING_MODE_TOOLTIP)));
    }

    protected void addSortStacksButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bSortStacks,
                val -> bSortStacks = val,
                GTUITextures.OVERLAY_BUTTON_SORTING_MODE,
                () -> mTooltipCache.getData(SORTING_MODE_TOOLTIP)));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addCloseListener(() -> uiButtonCount = 0);
        addEmitEnergyButton(builder);
    }

    protected Widget createToggleButton(Supplier<Boolean> getter, Consumer<Boolean> setter, UITexture picture,
        Supplier<GTTooltipDataCache.TooltipData> tooltipDataSupplier) {
        return new CycleButtonWidget().setToggle(getter, setter)
            .setStaticTexture(picture)
            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(7 + (uiButtonCount++ * BUTTON_SIZE), 62)
            .setSize(BUTTON_SIZE, BUTTON_SIZE)
            .setGTTooltip(tooltipDataSupplier);
    }

    protected void addInventorySlots(ModularWindow.Builder builder) {
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 9)
                .endAtSlot(26)
                .build()
                .setPos(7, 4));
    }
}
