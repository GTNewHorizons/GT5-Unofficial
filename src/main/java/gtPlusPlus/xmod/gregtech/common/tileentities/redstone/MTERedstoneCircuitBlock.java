package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.CircuitryBehavior;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gtPlusPlus.xmod.gregtech.api.gui.GTPPUITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTERedstoneCircuitBlock extends MTERedstoneBase implements IRedstoneCircuitBlock, IAddUIWidgets {

    public int mGate = 0, mGateData[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
    public boolean bOutput = true;

    public MTERedstoneCircuitBlock(int aID) {
        super(aID, "redstone.circuit", "Redstone Circuit Block", 1, 5, "Computes Redstone");
    }

    public MTERedstoneCircuitBlock(final String aName, String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, 1, 5, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERedstoneCircuitBlock(this.mName, mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean hasSidedRedstoneOutputBehavior() {
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
        return !this.isOutputFacing(side);
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isPneumatic() {
        return false;
    }

    @Override
    public boolean isSteampowered() {
        return false;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == this.getOutputFacing();
    }

    @Override
    public long getMinimumStoredEU() {
        return 512;
    }

    @Override
    public long maxEUInput() {
        return GTValues.V[1];
    }

    @Override
    public long maxEUOutput() {
        return bOutput ? GTValues.V[1] : 0;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public long maxEUStore() {
        return GTValues.V[3] * 1024;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mGate", mGate);
        aNBT.setIntArray("mGateData", mGateData);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mGate = aNBT.getInteger("mGate");
        mGateData = aNBT.getIntArray("mGateData");
        if (mGateData.length != 8) mGateData = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    public void switchGateForward(boolean aShift) {
        try {
            Set<Integer> tKeys = GregTechAPI.sCircuitryBehaviors.keySet();
            ArrayList<Integer> tList = new ArrayList<>();
            tList.addAll(tKeys);
            if (tList.size() <= 0) return;
            Collections.sort(tList);
            if (!GregTechAPI.sCircuitryBehaviors.containsKey(mGate)) mGate = tList.get(0);
            int tIndex = Collections.binarySearch(tList, mGate);
            tIndex += aShift ? 16 : 1;
            while (tIndex >= tList.size()) tIndex -= tList.size();
            mGate = tList.get(tIndex);
            switchGate();
        } catch (Throwable e) {
            GTLog.err.print(e);
        }
    }

    public void switchGateBackward(boolean aShift) {
        try {
            Set<Integer> tKeys = GregTechAPI.sCircuitryBehaviors.keySet();
            ArrayList<Integer> tList = new ArrayList<>();
            tList.addAll(tKeys);
            if (tList.size() <= 0) return;
            Collections.sort(tList);
            if (!GregTechAPI.sCircuitryBehaviors.containsKey(mGate)) mGate = tList.get(0);
            int tIndex = Collections.binarySearch(tList, mGate);
            tIndex -= aShift ? 16 : 1;
            while (tIndex < 0) tIndex += tList.size();
            mGate = tList.get(tIndex);
            switchGate();
        } catch (Throwable e) {
            GTLog.err.print(e);
        }
    }

    @Override
    public void onFacingChange() {
        resetRedstone();
    }

    private void resetRedstone() {
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.DOWN, (byte) 0);
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.UP, (byte) 0);
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.NORTH, (byte) 0);
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.SOUTH, (byte) 0);
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.WEST, (byte) 0);
        getBaseMetaTileEntity().setInternalOutputRedstoneSignal(ForgeDirection.EAST, (byte) 0);
    }

    public void changeGateData(int aIndex, int aValue) {
        mGateData[aIndex] += aValue;
        validateGateData();
    }

    public void stackGateData(int aIndex, ItemStack aStack) {
        mGateData[aIndex] = GTUtility.stackToInt(aStack);
        validateGateData();
    }

    private void switchGate() {
        resetRedstone();
        Arrays.fill(mGateData, 0);
        CircuitryBehavior tBehaviour = GregTechAPI.sCircuitryBehaviors.get(mGate);
        if (tBehaviour != null) try {
            tBehaviour.initParameters(mGateData, this);
        } catch (Throwable e) {
            GTLog.err.print(e);
        }
        validateGateData();
    }

    private void validateGateData() {
        CircuitryBehavior tBehaviour = GregTechAPI.sCircuitryBehaviors.get(mGate);
        if (tBehaviour != null) try {
            tBehaviour.validateParameters(mGateData, this);
        } catch (Throwable e) {
            GTLog.err.print(e);
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getBaseMetaTileEntity().setGenericRedstoneOutput(true);
        validateGateData();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        getBaseMetaTileEntity().setGenericRedstoneOutput(true);
        if (getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().isServerSide()) {
            mInventory[0] = mInventory[1] = mInventory[2] = mInventory[3] = mInventory[4] = null;
            if (getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU()) {
                if (getBaseMetaTileEntity().isActive()) {
                    CircuitryBehavior tBehaviour = GregTechAPI.sCircuitryBehaviors.get(mGate);
                    if (tBehaviour != null) {
                        try {
                            tBehaviour.onTick(mGateData, this);
                            if (tBehaviour.displayItemStack(mGateData, this, 0))
                                mInventory[1] = getCoverByID(mGateData[0]);
                            if (tBehaviour.displayItemStack(mGateData, this, 1))
                                mInventory[2] = getCoverByID(mGateData[1]);
                            if (tBehaviour.displayItemStack(mGateData, this, 2))
                                mInventory[3] = getCoverByID(mGateData[2]);
                            if (tBehaviour.displayItemStack(mGateData, this, 3))
                                mInventory[4] = getCoverByID(mGateData[3]);
                        } catch (Throwable e) {
                            GTLog.err.print(e);
                        }
                    }
                }
                getBaseMetaTileEntity().setErrorDisplayID(0);
            } else {
                getBaseMetaTileEntity().setErrorDisplayID(1);
            }
        }
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Only Calc server-side
        if (!this.getBaseMetaTileEntity()
            .isServerSide()) {
            return;
        }
        // Emit Redstone
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            byte aRedstone = getBaseMetaTileEntity().getOutputRedstoneSignal(side);
            this.getBaseMetaTileEntity()
                .setInternalOutputRedstoneSignal(side, aRedstone);
        }
    }

    @Override
    public final boolean hasRedstoneSignal() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (getBaseMetaTileEntity().getOutputRedstoneSignal(side) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    /** The Item List for Covers */
    public static final Map<Integer, ItemStack> sCoversItems = new HashMap<>();

    private static void initCovers() {
        for (GTItemStack aKey : GregTechAPI.sCovers.keySet()) {
            ItemStack aStack = aKey.toStack()
                .copy();
            if (aStack != null) {
                sCoversItems.put(GTUtility.stackToInt(aStack), aStack);
            }
        }
    }

    public static ItemStack getCoverByID(int aStack) {
        if (sCoversItems.isEmpty()) {
            initCovers();
        }
        return sCoversItems.get(Integer.valueOf(aStack));
    }

    @Override
    public ForgeDirection getOutputFacing() {
        return getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean setRedstone(byte aStrength, ForgeDirection side) {
        if (getOutputRedstone(side) != aStrength) {
            if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(1, false)) {
                getBaseMetaTileEntity().setInternalOutputRedstoneSignal(side, aStrength);
                getBaseMetaTileEntity().setErrorDisplayID(0);
                return true;
            } else {
                getBaseMetaTileEntity().setErrorDisplayID(1);
                return false;
            }
        }
        return false;
    }

    /*
     * @Override public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) { if (aSide ==
     * getOutputFacing()) { if (side == ForgeDirection.DOWN) return aRedstone ? 56 : 54; if (side == ForgeDirection.UP)
     * return aRedstone ? 53 : 52; return aRedstone ? 94 : 93; } if (side == ForgeDirection.DOWN) return aRedstone ? 60
     * : 59; if (side == ForgeDirection.UP) return aRedstone ? 58 : 57; return aRedstone ? 62 : 61; }
     */

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    public byte getOutputRedstone(ForgeDirection side) {
        return getBaseMetaTileEntity().getOutputRedstoneSignal(side);
    }

    @Override
    public byte getInputRedstone(ForgeDirection side) {
        return getBaseMetaTileEntity().getInternalInputRedstoneSignal(side);
    }

    @Override
    public Block getBlockAtSide(ForgeDirection side) {
        return getBaseMetaTileEntity().getBlockAtSide(side);
    }

    @Override
    public byte getMetaIDAtSide(ForgeDirection side) {
        return getBaseMetaTileEntity().getMetaIDAtSide(side);
    }

    @Override
    public TileEntity getTileEntityAtSide(ForgeDirection side) {
        return getBaseMetaTileEntity().getTileEntityAtSide(side);
    }

    @Override
    public int getRandom(int aRange) {
        return getBaseMetaTileEntity().getRandomNumber(aRange);
    }

    @Override
    public CoverBehavior getCover(ForgeDirection side) {
        return (CoverBehavior) getBaseMetaTileEntity().getCoverBehaviorAtSideNew(side);
    }

    @Override
    public int getCoverID(ForgeDirection side) {
        return getBaseMetaTileEntity().getCoverIDAtSide(side);
    }

    @Override
    public int getCoverVariable(ForgeDirection side) {
        return ((ISerializableObject.LegacyCoverData) getBaseMetaTileEntity().getComplexCoverDataAtSide(side)).get();
    }

    @Override
    public ICoverable getOwnTileEntity() {
        return getBaseMetaTileEntity();
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getSides(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getSidesActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }

    private GTRenderedTexture getBase() {
        return new GTRenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Top_Off) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Top_On) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Off),
            new GTRenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red) };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_On),
            new GTRenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red_Redstone) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_Off) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_On) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Off) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { getBase(), new GTRenderedTexture(TexturesGtBlock.Casing_Redstone_Side_On) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTPPUITextures.PICTURE_REDSTONE_CIRCUIT_SCREEN)
                .setPos(43, 5)
                .setSize(108, 72));
        for (int i = 0; i < 4; i++) {
            final int index = i;
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                ItemStack tStack = widget.getContext()
                    .getPlayer().inventory.getItemStack();
                if (tStack == null) {
                    changeGateData(
                        index,
                        clickData.mouseButton == 0 ? clickData.shift ? +128 : +1 : clickData.shift ? -128 : -1);
                } else {
                    tStack = GTUtility.copy(tStack);
                    if (clickData.mouseButton != 0) tStack.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    stackGateData(index, tStack);
                }
            })
                .setBackground(GTUITextures.BUTTON_STANDARD, GTPPUITextures.OVERLAY_BUTTON_PLUS_MINUS)
                .setPos(7, 5 + i * 18)
                .setSize(18, 18))
                .widget(
                    SlotWidget.phantom(inventoryHandler, i + 1)
                        .disableInteraction()
                        .setPos(25, 5 + i * 18));
        }
        builder.widget(
            new CycleButtonWidget().setToggle(() -> bOutput, val -> bOutput = val)
                .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_EMIT_ENERGY)
                .addTooltip("Toggle EU Output")
                .setPos(151, 5)
                .setSize(18, 18))
            .widget(
                new CycleButtonWidget()
                    .setToggle(() -> getBaseMetaTileEntity().isActive(), val -> getBaseMetaTileEntity().setActive(val))
                    .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                    .setStaticTexture(GTPPUITextures.OVERLAY_BUTTON_ACTIVE_STATE)
                    .addTooltip("Toggle Active State")
                    .setPos(151, 23)
                    .setSize(18, 18))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) switchGateForward(clickData.shift);
                else switchGateBackward(clickData.shift);
            })
                .setBackground(GTUITextures.BUTTON_STANDARD, GTPPUITextures.OVERLAY_BUTTON_CHANGE_MODE)
                .addTooltip("Change Redstone Circuit")
                .setPos(151, 41)
                .setSize(18, 18));

        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> mGate, val -> mGate = val));
        for (int i = 0; i < mGateData.length; i++) {
            final int index = i;
            builder.widget(new FakeSyncWidget.IntegerSyncer(() -> mGateData[index], val -> mGateData[index] = val));
        }

        builder.widget(new DrawableWidget().setDrawable(() -> {
            if (getBaseMetaTileEntity().getErrorDisplayID() > 0) {
                if ((getBaseMetaTileEntity().getTimer() / 5) % 2 == 0) {
                    return GTPPUITextures.PICTURE_ELECTRICITY_ERROR;
                } else {
                    return null;
                }
            } else {
                return GTPPUITextures.PICTURE_ELECTRICITY_FINE;
            }
        })
            .setPos(140, 9)
            .setSize(7, 7))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> getBaseMetaTileEntity().getErrorDisplayID(),
                    val -> getBaseMetaTileEntity().setErrorDisplayID(val)));

        builder.widget(TextWidget.dynamicString(() -> {
            CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
            if (tCircuit != null) return tCircuit.getName();
            return "";
        })
            .setSynced(false)
            .setDefaultColor(COLOR_TEXT_WHITE.get())
            .setPos(46, 8))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) return tCircuit.getDescription();
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 19))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) return tCircuit.getDataDescription(mGateData, 0);
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 33))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) return tCircuit.getDataDescription(mGateData, 1);
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 44))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) return tCircuit.getDataDescription(mGateData, 2);
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 55))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) return tCircuit.getDataDescription(mGateData, 3);
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(46, 66))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) {
                    String tString = tCircuit.getDataDisplay(mGateData, 0);
                    return tString == null ? GTUtility.parseNumberToString(mGateData[0]) : tString;
                }
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(99, 33))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) {
                    String tString = tCircuit.getDataDisplay(mGateData, 1);
                    return tString == null ? GTUtility.parseNumberToString(mGateData[1]) : tString;
                }
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(99, 44))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) {
                    String tString = tCircuit.getDataDisplay(mGateData, 2);
                    return tString == null ? GTUtility.parseNumberToString(mGateData[2]) : tString;
                }
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(99, 55))
            .widget(TextWidget.dynamicString(() -> {
                CircuitryBehavior tCircuit = GregTechAPI.sCircuitryBehaviors.get(mGate);
                if (tCircuit != null) {
                    String tString = tCircuit.getDataDisplay(mGateData, 3);
                    return tString == null ? GTUtility.parseNumberToString(mGateData[3]) : tString;
                }
                return "";
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get())
                .setPos(99, 66));
    }
}
