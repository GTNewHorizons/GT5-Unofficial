package gtPlusPlus.xmod.gregtech.common.tileentities.automation;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_ElectricInventoryManager extends GT_MetaTileEntity_TieredMachineBlock
        implements IAddGregtechLogo, IAddUIWidgets {

    public int[] mSlotRange = new int[4];
    public boolean mWorkedLastTick = false;
    protected String mLocalName;

    public GT_MetaTileEntity_ElectricInventoryManager(final int aID, final int aTier, final String aDescription) {
        super(
                aID,
                "basicmachine.automation.inventorymanager.0" + aTier,
                "Electric Inventory Manager (" + GT_Values.VN[aTier] + ")",
                aTier,
                16,
                aDescription);
        mLocalName = "Auto Workbench (" + GT_Values.VN[aTier] + ")";
    }

    public GT_MetaTileEntity_ElectricInventoryManager(final String aName, final int aTier, final String[] aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, 16, aDescription, aTextures);
    }

    @Override
    public boolean isTransformerUpgradable() {
        return true;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
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
    public long maxEUInput() {
        return GT_Values.V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return GT_Values.V[mTier];
    }

    @Override
    public long getMinimumStoredEU() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public long maxEUStore() {
        return GT_Values.V[this.mTier] * (this.mTier * GT_Values.V[this.mTier]);
    }

    @Override
    public long maxAmperesIn() {
        return 4;
    }

    @Override
    public long maxAmperesOut() {
        return 4;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 3;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return !isOutputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        for (int i = 0; i < mSlotRange.length; i++) {
            if (side == getRangeDirection(i) && getRangeEnergy(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 16;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ElectricInventoryManager(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mSlotRange0", mSlotRange[0]);
        aNBT.setInteger("mSlotRange1", mSlotRange[1]);
        aNBT.setInteger("mSlotRange2", mSlotRange[2]);
        aNBT.setInteger("mSlotRange3", mSlotRange[3]);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mSlotRange[0] = aNBT.getInteger("mSlotRange0");
        mSlotRange[1] = aNBT.getInteger("mSlotRange1");
        mSlotRange[2] = aNBT.getInteger("mSlotRange2");
        mSlotRange[3] = aNBT.getInteger("mSlotRange3");
    }

    public void iterateRangeDirection(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~7) | (((mSlotRange[aIndex] & 7) + 1) % 6);
    }

    public void switchRangeEnergy(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~8) | ((mSlotRange[aIndex] & 8) > 0 ? 0 : 8);
    }

    public void iterateSlot1Direction(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~112) | (((((mSlotRange[aIndex] & 112) >> 4) + 1) % 6) << 4);
    }

    public void iterateSlot2Direction(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~896) | (((((mSlotRange[aIndex] & 896) >> 7) + 1) % 6) << 7);
    }

    public void iterateSlot3Direction(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~7168) | (((((mSlotRange[aIndex] & 7168) >> 10) + 1) % 6) << 10);
    }

    public void switchSlot1InOut(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~8192) | ((mSlotRange[aIndex] & 8192) > 0 ? 0 : 8192);
    }

    public void switchSlot2InOut(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~16384) | ((mSlotRange[aIndex] & 16384) > 0 ? 0 : 16384);
    }

    public void switchSlot3InOut(int aIndex) {
        mSlotRange[aIndex] = (mSlotRange[aIndex] & ~32768) | ((mSlotRange[aIndex] & 32768) > 0 ? 0 : 32768);
    }

    public ForgeDirection getRangeDirection(int aIndex) {
        return ForgeDirection.getOrientation(mSlotRange[aIndex] & 7);
    }

    public ForgeDirection getSlot1Direction(int aIndex) {
        return ForgeDirection.getOrientation((mSlotRange[aIndex] & 112) >> 4);
    }

    public ForgeDirection getSlot2Direction(int aIndex) {
        return ForgeDirection.getOrientation((mSlotRange[aIndex] & 896) >> 7);
    }

    public ForgeDirection getSlot3Direction(int aIndex) {
        return ForgeDirection.getOrientation((mSlotRange[aIndex] & 7168) >> 10);
    }

    public boolean getRangeEnergy(int aIndex) {
        return (mSlotRange[aIndex] & 8) > 0;
    }

    public boolean getSlot1InOut(int aIndex) {
        return (mSlotRange[aIndex] & 8192) > 0;
    }

    public boolean getSlot2InOut(int aIndex) {
        return (mSlotRange[aIndex] & 16384) > 0;
    }

    public boolean getSlot3InOut(int aIndex) {
        return (mSlotRange[aIndex] & 32768) > 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().isServerSide()
                && getBaseMetaTileEntity().getUniversalEnergyStored() >= 5000
                && (getBaseMetaTileEntity().hasWorkJustBeenEnabled() || getBaseMetaTileEntity().getTimer() % 100 == 0
                        || mWorkedLastTick
                        || getBaseMetaTileEntity().hasInventoryBeenModified())) {
            mWorkedLastTick = false;

            IInventory[] tTileEntities = new IInventory[] { getBaseMetaTileEntity().getIInventoryAtSide(DOWN),
                    getBaseMetaTileEntity().getIInventoryAtSide(UP), getBaseMetaTileEntity().getIInventoryAtSide(NORTH),
                    getBaseMetaTileEntity().getIInventoryAtSide(SOUTH),
                    getBaseMetaTileEntity().getIInventoryAtSide(WEST),
                    getBaseMetaTileEntity().getIInventoryAtSide(EAST), null, null };

            int tCost = 0;

            for (int i = 0; i < 4; i++) {
                final int ordinalRangeDirection = getRangeDirection(i).ordinal();
                if (tTileEntities[ordinalRangeDirection] != null) {
                    ArrayList<ItemStack> tList = new ArrayList<ItemStack>();
                    ItemStack tStack;
                    tList.add(null);

                    tStack = mInventory[3 + i * 3 + 0];
                    if (tStack == null) {
                        if (getSlot1InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot1Direction(i),
                                getSlot1Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot1Direction(i),
                                getSlot1Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    } else {
                        tList.set(0, tStack);
                        if (getSlot1InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot1Direction(i),
                                getSlot1Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot1Direction(i),
                                getSlot1Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    }

                    tStack = mInventory[3 + i * 3 + 1];
                    if (tStack == null) {
                        if (getSlot2InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot2Direction(i),
                                getSlot2Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot2Direction(i),
                                getSlot2Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    } else {
                        tList.set(0, tStack);
                        if (getSlot2InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot2Direction(i),
                                getSlot2Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot2Direction(i),
                                getSlot2Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    }

                    tStack = mInventory[3 + i * 3 + 2];
                    if (tStack == null) {
                        if (getSlot3InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot3Direction(i),
                                getSlot3Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot3Direction(i),
                                getSlot3Direction(i),
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    } else {
                        tList.set(0, tStack);
                        if (getSlot3InOut(i)) tCost += 5 * GT_Utility.moveOneItemStack(
                                getBaseMetaTileEntity(),
                                tTileEntities[ordinalRangeDirection],
                                getSlot3Direction(i),
                                getSlot3Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                        else tCost += 5 * GT_Utility.moveOneItemStack(
                                tTileEntities[ordinalRangeDirection],
                                getBaseMetaTileEntity(),
                                getSlot3Direction(i),
                                getSlot3Direction(i),
                                tList,
                                false,
                                (byte) tStack.stackSize,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                    }
                }
            }

            if (tCost > 0) {
                mWorkedLastTick = true;
                getBaseMetaTileEntity().decreaseStoredEnergyUnits(tCost, true);
            }
        }
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils
                .addAll(this.mDescriptionArray, "It's simpler than you think. I promise.", CORE.GT_Tooltip.get());
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[16][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getBottom(i);
            rTextures[1][i + 1] = this.getTop(i);
            rTextures[2][i + 1] = this.getNegativeZ(i);
            rTextures[3][i + 1] = this.getPositiveZ(i);
            rTextures[4][i + 1] = this.getNegativeX(i);
            rTextures[5][i + 1] = this.getPositiveX(i);
            rTextures[6][i + 1] = this.getBottomRedstone(i);
            rTextures[7][i + 1] = this.getTopRedstone(i);
            rTextures[8][i + 1] = this.getNegativeZRedstone(i);
            rTextures[9][i + 1] = this.getPositiveZRedstone(i);
            rTextures[10][i + 1] = this.getNegativeXRedstone(i);
            rTextures[11][i + 1] = this.getPositiveXRedstone(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[!aRedstone ? side.ordinal() : side.ordinal() + 6][aColorIndex < 0 ? 0 : aColorIndex];
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Green) };
    }

    public ITexture[] getNegativeZ(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Blue) };
    }

    public ITexture[] getPositiveZ(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Cyan) };
    }

    public ITexture[] getNegativeX(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Purple) };
    }

    public ITexture[] getPositiveX(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Yellow) };
    }

    public ITexture[] getBottomRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red_Redstone) };
    }

    public ITexture[] getTopRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Green_Redstone) };
    }

    public ITexture[] getNegativeZRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Blue_Redstone) };
    }

    public ITexture[] getPositiveZRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Cyan_Redstone) };
    }

    public ITexture[] getNegativeXRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Purple_Redstone) };
    }

    public ITexture[] getPositiveXRedstone(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Yellow_Redstone) };
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo()).setSize(17, 17).setPos(154, 59));
    }

    // Internal copy of values stored in this tile. Client will use these to render stuff.
    private final int[] mTargetDirections = new int[12];
    private final int[] mRangeDirections = new int[4];
    private final boolean[] mTargetInOut = new boolean[12];
    private final boolean[] mTargetEnergy = new boolean[4];

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        for (int i = 0; i < 3; i++) {
            builder.widget(
                    new SlotWidget(inventoryHandler, i)
                            .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_CHEST)
                            .setPos(154, 4 + i * 18));
        }

        int[] slotXPositions = new int[] { 4, 60, 79, 135 };
        for (int i = 0; i < 12; i++) {
            final int index = i;
            builder.widget(new SlotWidget(new BaseSlot(inventoryHandler, i + 3, true)) {

                @Override
                protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                    super.phantomClick(clickData, cursorStack);
                    if (clickData.mouseButton != 0 && cursorStack != null && getMcSlot().getHasStack()) {
                        getMcSlot().getStack().setItemDamage(OreDictionary.WILDCARD_VALUE);
                    }
                }
            }.setControlsAmount(true).disableShiftInsert().setBackground(() -> {
                if (index % 3 == 0) {
                    return new IDrawable[] { GTPP_UITextures.SLOT_INVENTORY_MANAGER[mRangeDirections[index / 3]],
                            GTPP_UITextures.OVERLAY_SLOT_INVENTORY_MANAGER_COLOR[mRangeDirections[index / 3]] };
                } else if (index % 3 == 1) {
                    return new IDrawable[] { GTPP_UITextures.SLOT_INVENTORY_MANAGER[mRangeDirections[index / 3]],
                            GTPP_UITextures.OVERLAY_SLOT_INVENTORY_MANAGER_ARROW[mRangeDirections[index / 3]] };
                } else {
                    return new IDrawable[] { GTPP_UITextures.SLOT_INVENTORY_MANAGER[mRangeDirections[index / 3]] };
                }
            }).setPos(slotXPositions[i / 3], 4 + (i % 3) * 18));
        }
        for (int i = 0; i < 4; i++) {
            final int index = i;
            builder.widget(
                    new ButtonWidget().setOnClick((clickData, widget) -> switchRangeEnergy(index)).setBackground(
                            () -> new IDrawable[] {
                                    mTargetEnergy[index] ? ModularUITextures.ITEM_SLOT : GT_UITextures.BUTTON_STANDARD,
                                    GT_UITextures.OVERLAY_BUTTON_EMIT_ENERGY })
                            .setPos(slotXPositions[i], 59).setSize(18, 18));
        }

        int[] buttonXPositions = new int[] { 23, 41, 98, 116 };
        for (int i = 0; i < 12; i++) {
            final int index = i;
            builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (index % 3 == 0) {
                    if (clickData.mouseButton != 0) {
                        switchSlot1InOut(index / 3);
                    } else {
                        iterateSlot1Direction(index / 3);
                    }
                } else if (index % 3 == 1) {
                    if (clickData.mouseButton != 0) {
                        switchSlot2InOut(index / 3);
                    } else {
                        iterateSlot2Direction(index / 3);
                    }
                } else {
                    if (clickData.mouseButton != 0) {
                        switchSlot3InOut(index / 3);
                    } else {
                        iterateSlot3Direction(index / 3);
                    }
                }
            }).setBackground(
                    () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                            GTPP_UITextures.OVERLAY_BUTTON_DIRECTION[mTargetDirections[index]],
                            mTargetInOut[index] ? GTPP_UITextures.OVERLAY_BUTTON_TIP_RED
                                    : GTPP_UITextures.OVERLAY_BUTTON_TIP_GREEN })
                    .setPos(buttonXPositions[i / 3], 4 + (i % 3) * 18).setSize(18, 18));
        }
        for (int i = 0; i < 4; i++) {
            final int index = i;
            builder.widget(
                    new ButtonWidget().setOnClick((clickData, widget) -> iterateRangeDirection(index))
                            .setBackground(
                                    () -> new IDrawable[] { GT_UITextures.BUTTON_STANDARD,
                                            GTPP_UITextures.OVERLAY_BUTTON_DIRECTION_GRAY[mRangeDirections[index]] })
                            .setPos(buttonXPositions[i], 59).setSize(18, 18));
        }

        for (int i = 0; i < mTargetDirections.length; i++) {
            final int index = i;
            builder.widget(new FakeSyncWidget.IntegerSyncer(() -> {
                if (index % 3 == 0) {
                    return getSlot1Direction(index / 3).ordinal();
                } else if (index % 3 == 1) {
                    return getSlot2Direction(index / 3).ordinal();
                } else {
                    return getSlot3Direction(index / 3).ordinal();
                }
            }, val -> mTargetDirections[index] = val));
        }
        for (int i = 0; i < mRangeDirections.length; i++) {
            final int index = i;
            builder.widget(
                    new FakeSyncWidget.IntegerSyncer(
                            () -> getRangeDirection(index).ordinal(),
                            val -> mRangeDirections[index] = val));
        }
        for (int i = 0; i < mTargetInOut.length; i++) {
            final int index = i;
            builder.widget(new FakeSyncWidget.BooleanSyncer(() -> {
                if (index % 3 == 0) {
                    return getSlot1InOut(index / 3);
                } else if (index % 3 == 1) {
                    return getSlot2InOut(index / 3);
                } else {
                    return getSlot3InOut(index / 3);
                }
            }, val -> mTargetInOut[index] = val));
        }
        for (int i = 0; i < mTargetEnergy.length; i++) {
            final int index = i;
            builder.widget(
                    new FakeSyncWidget.BooleanSyncer(() -> getRangeEnergy(index), val -> mTargetEnergy[index] = val));
        }
    }
}
