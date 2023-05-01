package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IronBlastFurnace extends MetaTileEntity implements IAddUIWidgets {

    private static final ITexture[] FACING_SIDE = { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top) };
    private static final ITexture[] FACING_FRONT = {
            new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_Off) };
    private static final ITexture[] FACING_ACTIVE = {
            new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Redstone_On) };
    public int mMaxProgresstime = 0;
    public int mUpdate = 30;
    public int mProgresstime = 0;
    public boolean mMachine = false;
    public ItemStack mOutputItem1;
    public ItemStack mOutputItem2;

    public GregtechMetaTileEntity_IronBlastFurnace(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional, 4);
    }

    public GregtechMetaTileEntity_IronBlastFurnace(final String aName) {
        super(aName, 4);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Iron is a much better furnace material!", "Can be Automated",
                "Multiblock: 3x3x5 hollow with opening on top",
                "Same shape as Bronze/Bricked blast furnace, except one ring of 8 taller.",
                "40 Iron Plated Bricks required", };
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

    @Override
    public boolean isSteampowered() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isPneumatic() {
        return false;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public boolean isInputFacing(final ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return facing.offsetY == 0;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public int getProgresstime() {
        return this.mProgresstime;
    }

    @Override
    public int maxProgresstime() {
        return this.mMaxProgresstime;
    }

    @Override
    public int increaseProgress(final int aProgress) {
        this.mProgresstime += aProgress;
        return this.mMaxProgresstime - this.mProgresstime;
    }

    @Override
    public boolean allowCoverOnSide(final ForgeDirection side, final GT_ItemStack aCoverID) {
        return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover())
                && (super.allowCoverOnSide(side, aCoverID));
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IronBlastFurnace(this.mName);
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setInteger("mProgresstime", this.mProgresstime);
        aNBT.setInteger("mMaxProgresstime", this.mMaxProgresstime);
        if (this.mOutputItem1 != null) {
            final NBTTagCompound tNBT = new NBTTagCompound();
            this.mOutputItem1.writeToNBT(tNBT);
            aNBT.setTag("mOutputItem1", tNBT);
        }
        if (this.mOutputItem2 != null) {
            final NBTTagCompound tNBT = new NBTTagCompound();
            this.mOutputItem2.writeToNBT(tNBT);
            aNBT.setTag("mOutputItem2", tNBT);
        }
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.mUpdate = 30;
        this.mProgresstime = aNBT.getInteger("mProgresstime");
        this.mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        this.mOutputItem1 = GT_Utility.loadItem(aNBT, "mOutputItem1");
        this.mOutputItem2 = GT_Utility.loadItem(aNBT, "mOutputItem2");
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    private boolean checkMachine() {
        final int xDir = this.getBaseMetaTileEntity().getBackFacing().offsetX;
        final int zDir = this.getBaseMetaTileEntity().getBackFacing().offsetZ;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 4; j++) { // This is height
                for (int k = -1; k < 2; k++) {
                    if (((xDir + i) != 0) || (j != 0) || ((zDir + k) != 0)) {
                        if ((i != 0) || (j == -1) || (k != 0)) {
                            if ((this.getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k)
                                    != ModBlocks.blockCasingsMisc)
                                    || (this.getBaseMetaTileEntity().getMetaIDOffset(xDir + i, j, zDir + k) != 10)) {
                                return false;
                            }
                        } else if ((!GT_Utility.arrayContains(
                                this.getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k),
                                new Object[] { Blocks.lava, Blocks.flowing_lava, null }))
                                && (!this.getBaseMetaTileEntity().getAirOffset(xDir + i, j, zDir + k))) {
                                    return false;
                                }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onMachineBlockUpdate() {
        this.mUpdate = 30;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
            aBaseMetaTileEntity.getWorld().spawnParticle(
                    "cloud",
                    aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1) + Math.random(),
                    aBaseMetaTileEntity.getOffsetY(aBaseMetaTileEntity.getBackFacing(), 1),
                    aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1) + Math.random(),
                    0.0D,
                    0.3D,
                    0.0D);
        }
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate-- == 0) {
                this.mMachine = this.checkMachine();
            }
            if (this.mMachine) {
                if (this.mMaxProgresstime > 0) {
                    if (++this.mProgresstime >= this.mMaxProgresstime) {
                        this.addOutputProducts();
                        this.mOutputItem1 = null;
                        this.mOutputItem2 = null;
                        this.mProgresstime = 0;
                        this.mMaxProgresstime = 0;
                        try {
                            GT_Mod.instance.achievements.issueAchievement(
                                    aBaseMetaTileEntity.getWorld()
                                            .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                    "steel");
                        } catch (final Exception e) {}
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    this.checkRecipe();
                }
            }
            aBaseMetaTileEntity.setActive((this.mMaxProgresstime > 0) && (this.mMachine));
            if (aBaseMetaTileEntity.isActive()) {
                if (aBaseMetaTileEntity.getAir(
                        aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
                    aBaseMetaTileEntity.getWorld().setBlock(
                            aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
                            Blocks.lava,
                            1,
                            2);
                    this.mUpdate = 1;
                }
                if (aBaseMetaTileEntity.getAir(
                        aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                        aBaseMetaTileEntity.getYCoord() + 1,
                        aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
                    aBaseMetaTileEntity.getWorld().setBlock(
                            aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                            aBaseMetaTileEntity.getYCoord() + 1,
                            aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
                            Blocks.lava,
                            1,
                            2);
                    this.mUpdate = 1;
                }
            } else {
                if (aBaseMetaTileEntity.getBlock(
                        aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld().setBlock(
                            aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
                            Blocks.air,
                            0,
                            2);
                    this.mUpdate = 1;
                }
                if (aBaseMetaTileEntity.getBlock(
                        aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                        aBaseMetaTileEntity.getYCoord() + 1,
                        aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld().setBlock(
                            aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1),
                            aBaseMetaTileEntity.getYCoord() + 1,
                            aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1),
                            Blocks.air,
                            0,
                            2);
                    this.mUpdate = 1;
                }
            }
        }
    }

    private void addOutputProducts() {
        if (this.mOutputItem1 != null) {
            if (this.mInventory[2] == null) {
                this.mInventory[2] = GT_Utility.copy(new Object[] { this.mOutputItem1 });
            } else if (GT_Utility.areStacksEqual(this.mInventory[2], this.mOutputItem1)) {
                this.mInventory[2].stackSize = Math.min(
                        this.mOutputItem1.getMaxStackSize(),
                        this.mOutputItem1.stackSize + this.mInventory[2].stackSize);
            }
        }
        if (this.mOutputItem2 != null) {
            if (this.mInventory[3] == null) {
                this.mInventory[3] = GT_Utility.copy(new Object[] { this.mOutputItem2 });
            } else if (GT_Utility.areStacksEqual(this.mInventory[3], this.mOutputItem2)) {
                this.mInventory[3].stackSize = Math.min(
                        this.mOutputItem2.getMaxStackSize(),
                        this.mOutputItem2.stackSize + this.mInventory[3].stackSize);
            }
        }
    }

    private boolean spaceForOutput(final ItemStack aStack1, final ItemStack aStack2) {
        if (((this.mInventory[2] == null) || (aStack1 == null)
                || (((this.mInventory[2].stackSize + aStack1.stackSize) <= this.mInventory[2].getMaxStackSize())
                        && (GT_Utility.areStacksEqual(this.mInventory[2], aStack1))))
                && ((this.mInventory[3] == null) || (aStack2 == null)
                        || (((this.mInventory[3].stackSize + aStack2.stackSize) <= this.mInventory[3].getMaxStackSize())
                                && (GT_Utility.areStacksEqual(this.mInventory[3], aStack2))))) {
            return true;
        }
        return false;
    }

    private int getProperTime(int time) {
        return time / 3;
    }

    private boolean checkRecipe() {

        if (!this.mMachine) {
            return false;
        }
        if ((this.mInventory[0] != null) && (this.mInventory[1] != null) && (this.mInventory[0].stackSize >= 1)) {
            if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "dustIron"))
                    || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "ingotIron"))) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 4)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 4 * 3);
                    this.mMaxProgresstime = getProperTime(36000);
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke"))
                        && (this.mInventory[1].stackSize >= 2)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dustTiny, Materials.Ash, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 2 * 3);
                    this.mMaxProgresstime = getProperTime(4800);
                    return true;
                }
                if ((this.mInventory[0].stackSize >= 9)
                        && ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal"))
                                || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal")))
                        && (this.mInventory[1].stackSize >= 4)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 9);
                    this.getBaseMetaTileEntity().decrStackSize(1, 4 * 3);
                    this.mMaxProgresstime = getProperTime(64800);
                    return true;
                }
            } else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "dustSteel")) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 2)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dustTiny, Materials.DarkAsh, 2L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 2 * 3);
                    this.mMaxProgresstime = getProperTime(3600);
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke"))
                        && (this.mInventory[1].stackSize >= 1)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dustTiny, Materials.Ash, 2L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 1 * 3);
                    this.mMaxProgresstime = getProperTime(2400);
                    return true;
                }
                if ((this.mInventory[0].stackSize >= 9)
                        && ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal"))
                                || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal")))
                        && (this.mInventory[1].stackSize >= 2)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dust, Materials.DarkAsh, 2L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 9);
                    this.getBaseMetaTileEntity().decrStackSize(1, 2 * 3);
                    this.mMaxProgresstime = getProperTime(32400);
                    return true;
                }
            } else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "blockIron")) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 36)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 64);
                    this.mMaxProgresstime = getProperTime(64800);
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke"))
                        && (this.mInventory[1].stackSize >= 18)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L),
                                this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 18 * 3);
                    this.mMaxProgresstime = getProperTime(43200);
                    return true;
                }
                if (((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal"))
                        || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal")))
                        && (this.mInventory[1].stackSize >= 4)
                        && (this.spaceForOutput(
                                this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L),
                                this.mOutputItem2 = GT_OreDictUnificator
                                        .get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    this.getBaseMetaTileEntity().decrStackSize(0, 1);
                    this.getBaseMetaTileEntity().decrStackSize(1, 4 * 3);
                    this.mMaxProgresstime = getProperTime(64800);
                    return true;
                }
            }
        }
        this.mOutputItem1 = null;
        this.mOutputItem2 = null;
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return aIndex > 1;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        if (aIndex < 2) {}
        return !GT_Utility.areStacksEqual(aStack, this.mInventory[0]);
    }

    @Override
    public byte getTileEntityBaseType() {
        return 0;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new SlotWidget(inventoryHandler, 0)
                        .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_INGOT)
                        .setPos(33, 15))
                .widget(
                        new SlotWidget(inventoryHandler, 1)
                                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_FURNACE)
                                .setPos(33, 33))
                .widget(
                        new SlotWidget(inventoryHandler, 2).setAccess(true, false)
                                .setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_INGOT)
                                .setPos(85, 24))
                .widget(
                        new SlotWidget(inventoryHandler, 3).setAccess(true, false)
                                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_DUST)
                                .setPos(103, 24))
                .widget(
                        new ProgressBar().setTexture(GTPP_UITextures.PROGRESSBAR_ARROW_2, 20)
                                .setProgress(() -> (float) mProgresstime / mMaxProgresstime).setPos(58, 24)
                                .setSize(20, 18));
    }
}
