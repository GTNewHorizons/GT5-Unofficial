package gregtech.api.metatileentity.implementations;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;

public abstract class MTEBuffer extends MTETieredMachineBlock implements IAddUIWidgets {

    private static final int OUTPUT_INDEX = 0;
    private static final int ARROW_RIGHT_INDEX = 1;
    private static final int ARROW_DOWN_INDEX = 2;
    private static final int ARROW_LEFT_INDEX = 3;
    private static final int ARROW_UP_INDEX = 4;
    private static final int FRONT_INDEX = 5;

    private static final String EMIT_ENERGY_TOOLTIP = "GT5U.machines.emit_energy.tooltip";
    private static final String EMIT_REDSTONE_IF_FULL_TOOLTIP = "GT5U.machines.emit_redstone_if_full.tooltip";
    private static final String INVERT_REDSTONE_TOOLTIP = "GT5U.machines.invert_redstone.tooltip";
    private static final String STOCKING_MODE_TOOLTIP = "GT5U.machines.buffer_stocking_mode.tooltip";
    private static final String SORTING_MODE_TOOLTIP = "GT5U.machines.sorting_mode.tooltip";
    private static final int BUTTON_SIZE = 18;

    public int mMaxStackSize = 64;
    public static int MAX = 8;
    public boolean bOutput = false, bRedstoneIfFull = false, bInvert = false, bStockingMode = false,
        bSortStacks = false;
    public int mSuccess = 0, mTargetStackSize = 0;
    private int uiButtonCount = 0;

    public MTEBuffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEBuffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEBuffer(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
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
    public boolean isSimpleMachine() {
        return false;
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

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    public abstract ITexture getOverlayIcon();

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getBackFacing()) {

            mTargetStackSize = (byte) ((mTargetStackSize + (aPlayer.isSneaking() ? -1 : 1)) % 65);
            if (mTargetStackSize < 0) {
                mTargetStackSize = mMaxStackSize;
            }
            if (mTargetStackSize == 0) {
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("098", "Do not regulate Item Stack Size"));
            } else {
                GTUtility.sendChatToPlayer(
                    aPlayer,
                    GTUtility.trans("099", "Regulate Item Stack Size to: ") + mTargetStackSize);
            }
        }
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ) {
        wrenchingSide = wrenchingSide.getOpposite();
        if (getBaseMetaTileEntity().isValidFacing(wrenchingSide)) {
            getBaseMetaTileEntity().setFrontFacing(wrenchingSide);
            return true;
        }
        return false;
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
            updateSlots();
            moveItems(aBaseMetaTileEntity, aTimer);
            handleRedstoneOutput(aBaseMetaTileEntity);
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) 0);
    }

    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        moveItems(aBaseMetaTileEntity, aTimer, 1);
    }

    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long ignoredTimer, int stacks) {
        int tCost;
        if (bStockingMode) tCost = GTUtility.moveMultipleItemStacks(
            aBaseMetaTileEntity,
            aBaseMetaTileEntity.getTileEntityAtSide(aBaseMetaTileEntity.getBackFacing()),
            aBaseMetaTileEntity.getBackFacing(),
            aBaseMetaTileEntity.getFrontFacing(),
            null,
            false,
            mTargetStackSize == 0 ? 64 : (byte) mTargetStackSize,
            mTargetStackSize == 0 ? 1 : (byte) mTargetStackSize,
            (byte) 64,
            (byte) 1,
            stacks);
        else tCost = GTUtility.moveMultipleItemStacks(
            aBaseMetaTileEntity,
            aBaseMetaTileEntity.getTileEntityAtSide(aBaseMetaTileEntity.getBackFacing()),
            aBaseMetaTileEntity.getBackFacing(),
            aBaseMetaTileEntity.getFrontFacing(),
            null,
            false,
            (byte) 64,
            (byte) 1,
            mTargetStackSize == 0 ? 64 : (byte) mTargetStackSize,
            mTargetStackSize == 0 ? 1 : (byte) mTargetStackSize,
            stacks);

        if (tCost > 0 || aBaseMetaTileEntity.hasInventoryBeenModified()) {
            mSuccess = 50;
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

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        if (bSortStacks) fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        HashMap<GTUtility.ItemId, Integer> slots = new HashMap<>(mInventory.length);
        HashMap<GTUtility.ItemId, ItemStack> stacks = new HashMap<>(mInventory.length);
        List<GTUtility.ItemId> order = new ArrayList<>(mInventory.length);
        List<Integer> validSlots = new ArrayList<>(mInventory.length);
        for (int i = 0; i < mInventory.length - 1; i++) {
            if (!isValidSlot(i)) continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if (s == null) continue;
            GTUtility.ItemId sID = GTUtility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            order.add(sID);
            mInventory[i] = null;
        }
        int slotindex = 0;
        for (GTUtility.ItemId sID : order) {
            int toSet = slots.get(sID);
            if (toSet == 0) continue;
            int slot = validSlots.get(slotindex);
            slotindex++;
            mInventory[slot] = stacks.get(sID)
                .copy();
            toSet = Math.min(toSet, mInventory[slot].getMaxStackSize());
            mInventory[slot].stackSize = toSet;
            slots.merge(sID, toSet, (a, b) -> a - b);
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ) {
        if (entityPlayer.isSneaking()) {
            // I was so proud of all this but I literally just copied code from OutputBus
            bSortStacks = !bSortStacks;
            GTUtility.sendChatToPlayer(
                entityPlayer,
                GTUtility.trans("200", "Sort mode: ")
                    + (bSortStacks ? GTUtility.trans("088", "Enabled") : GTUtility.trans("087", "Disabled")));
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, entityPlayer, aX, aY, aZ);
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
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(V[mTier])
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
