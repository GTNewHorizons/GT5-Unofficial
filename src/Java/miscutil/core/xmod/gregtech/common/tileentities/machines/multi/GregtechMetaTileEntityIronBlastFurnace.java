package miscutil.core.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import miscutil.core.block.ModBlocks;
import miscutil.core.xmod.gregtech.api.gui.CONTAINER_IronBlastFurnace;
import miscutil.core.xmod.gregtech.api.gui.GUI_IronBlastFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntityIronBlastFurnace
        extends MetaTileEntity {
    private static final ITexture[] FACING_SIDE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL)};
    private static final ITexture[] FACING_FRONT = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT)};
    private static final ITexture[] FACING_ACTIVE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT_ACTIVE)};
    public int mMaxProgresstime = 0;
    public int mUpdate = 30;
    public int mProgresstime = 0;
    public boolean mMachine = false;
    public ItemStack mOutputItem1;
    public ItemStack mOutputItem2;

    public GregtechMetaTileEntityIronBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 4);
    }

    public GregtechMetaTileEntityIronBlastFurnace(String aName) {
        super(aName, 4);
    }

    @Override
	public String[] getDescription() {
        return new String[]{"Slowly, Skip the Bronze age, Get some Steel!", "Multiblock: 3x3x5 hollow with opening on top", "40 Iron Plated Bricks required"};
    }

    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
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
	public boolean isInputFacing(byte aSide) {
        return false;
    }

    @Override
	public boolean isOutputFacing(byte aSide) {
        return false;
    }

    @Override
	public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
	public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    @Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
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
	public int increaseProgress(int aProgress) {
        this.mProgresstime += aProgress;
        return this.mMaxProgresstime - this.mProgresstime;
    }

    @Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
        return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
    }

    @Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityIronBlastFurnace(this.mName);
    }

    @Override
	public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mProgresstime", this.mProgresstime);
        aNBT.setInteger("mMaxProgresstime", this.mMaxProgresstime);
        if (this.mOutputItem1 != null) {
            NBTTagCompound tNBT = new NBTTagCompound();
            this.mOutputItem1.writeToNBT(tNBT);
            aNBT.setTag("mOutputItem1", tNBT);
        }
        if (this.mOutputItem2 != null) {
            NBTTagCompound tNBT = new NBTTagCompound();
            this.mOutputItem2.writeToNBT(tNBT);
            aNBT.setTag("mOutputItem2", tNBT);
        }
    }

    @Override
	public void loadNBTData(NBTTagCompound aNBT) {
        this.mUpdate = 30;
        this.mProgresstime = aNBT.getInteger("mProgresstime");
        this.mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        this.mOutputItem1 = GT_Utility.loadItem(aNBT, "mOutputItem1");
        this.mOutputItem2 = GT_Utility.loadItem(aNBT, "mOutputItem2");
    }

    @Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new CONTAINER_IronBlastFurnace(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GUI_IronBlastFurnace(aPlayerInventory, aBaseMetaTileEntity);
    }

    private boolean checkMachine() {
        int xDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 4; j++) { //This is height
                for (int k = -1; k < 2; k++) {
                    if ((xDir + i != 0) || (j != 0) || (zDir + k != 0)) {
                        if ((i != 0) || (j == -1) || (k != 0)) {
                            if ((getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k) != ModBlocks.blockCasingsMisc) || (getBaseMetaTileEntity().getMetaIDOffset(xDir + i, j, zDir + k) != 10)) {
                                return false;
                            }
                        } else if ((!GT_Utility.arrayContains(getBaseMetaTileEntity().getBlockOffset(xDir + i, j, zDir + k), new Object[]{Blocks.lava, Blocks.flowing_lava, null})) && (!getBaseMetaTileEntity().getAirOffset(xDir + i, j, zDir + k))) {
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
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if ((aBaseMetaTileEntity.isClientSide()) &&
                (aBaseMetaTileEntity.isActive())) {
            aBaseMetaTileEntity.getWorld().spawnParticle("cloud", aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1) + Math.random(), aBaseMetaTileEntity.getOffsetY(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1) + Math.random(), 0.0D, 0.3D, 0.0D);
        }
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate-- == 0) {
                this.mMachine = checkMachine();
            }
            if (this.mMachine) {
                if (this.mMaxProgresstime > 0) {
                    if (++this.mProgresstime >= this.mMaxProgresstime) {
                        addOutputProducts();
                        this.mOutputItem1 = null;
                        this.mOutputItem2 = null;
                        this.mProgresstime = 0;
                        this.mMaxProgresstime = 0;
                        try {
                          //  GT_Mod.instance.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "steel");
                        } catch (Exception e) {
                        }
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    checkRecipe();
                }
            }
            aBaseMetaTileEntity.setActive((this.mMaxProgresstime > 0) && (this.mMachine));
            if (aBaseMetaTileEntity.isActive()) {
                if (aBaseMetaTileEntity.getAir(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
                    aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1), Blocks.lava, 1, 2);
                    this.mUpdate = 1;
                }
                if (aBaseMetaTileEntity.getAir(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1))) {
                    aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1), Blocks.lava, 1, 2);
                    this.mUpdate = 1;
                }
            } else {
                if (aBaseMetaTileEntity.getBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1), Blocks.air, 0, 2);
                    this.mUpdate = 1;
                }
                if (aBaseMetaTileEntity.getBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1)) == Blocks.lava) {
                    aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getOffsetX(aBaseMetaTileEntity.getBackFacing(), 1), aBaseMetaTileEntity.getYCoord() + 1, aBaseMetaTileEntity.getOffsetZ(aBaseMetaTileEntity.getBackFacing(), 1), Blocks.air, 0, 2);
                    this.mUpdate = 1;
                }
            }
        }
    }

    private void addOutputProducts() {
        if (this.mOutputItem1 != null) {
            if (this.mInventory[2] == null) {
                this.mInventory[2] = GT_Utility.copy(new Object[]{this.mOutputItem1});
            } else if (GT_Utility.areStacksEqual(this.mInventory[2], this.mOutputItem1)) {
                this.mInventory[2].stackSize = Math.min(this.mOutputItem1.getMaxStackSize(), this.mOutputItem1.stackSize + this.mInventory[2].stackSize);
            }
        }
        if (this.mOutputItem2 != null) {
            if (this.mInventory[3] == null) {
                this.mInventory[3] = GT_Utility.copy(new Object[]{this.mOutputItem2});
            } else if (GT_Utility.areStacksEqual(this.mInventory[3], this.mOutputItem2)) {
                this.mInventory[3].stackSize = Math.min(this.mOutputItem2.getMaxStackSize(), this.mOutputItem2.stackSize + this.mInventory[3].stackSize);
            }
        }
    }

    private boolean spaceForOutput(ItemStack aStack1, ItemStack aStack2) {
        if (((this.mInventory[2] == null) || (aStack1 == null) || ((this.mInventory[2].stackSize + aStack1.stackSize <= this.mInventory[2].getMaxStackSize()) && (GT_Utility.areStacksEqual(this.mInventory[2], aStack1)))) && (
                (this.mInventory[3] == null) || (aStack2 == null) || ((this.mInventory[3].stackSize + aStack2.stackSize <= this.mInventory[3].getMaxStackSize()) && (GT_Utility.areStacksEqual(this.mInventory[3], aStack2))))) {
            return true;
        }
        return false;
    }

    private boolean checkRecipe() {
        if (!this.mMachine) {
            return false;
        }
        if ((this.mInventory[0] != null) && (this.mInventory[1] != null) && (this.mInventory[0].stackSize >= 1)) {
            if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "dustIron")) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "ingotIron"))) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 4) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 4*3);
                    this.mMaxProgresstime = 36000;
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke")) && (this.mInventory[1].stackSize >= 2) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 2*3);
                    this.mMaxProgresstime = 4800*5;
                    return true;
                }
                if ((this.mInventory[0].stackSize >= 9) && ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal")) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal"))) && (this.mInventory[1].stackSize >= 4) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 9);
                    getBaseMetaTileEntity().decrStackSize(1, 4*3);
                    this.mMaxProgresstime = 64800*5;
                    return true;
                }
            } else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "dustSteel")) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 2) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 2*3);
                    this.mMaxProgresstime = 3600*5;
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke")) && (this.mInventory[1].stackSize >= 1) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 2L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 1*3);
                    this.mMaxProgresstime = 2400*5;
                    return true;
                }
                if ((this.mInventory[0].stackSize >= 9) && ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal")) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal"))) && (this.mInventory[1].stackSize >= 2) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 2L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 9);
                    getBaseMetaTileEntity().decrStackSize(1, 2*3);
                    this.mMaxProgresstime = 32400*5;
                    return true;
                }
            } else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[0], "blockIron")) {
                if ((this.mInventory[1].getItem() == Items.coal) && (this.mInventory[1].stackSize >= 36) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 64);
                    this.mMaxProgresstime = 64800*9;
                    return true;
                }
                if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "fuelCoke")) && (this.mInventory[1].stackSize >= 18) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 18*3);
                    this.mMaxProgresstime = 43200*5;
                    return true;
                }
                if (((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCoal")) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[1], "blockCharcoal"))) && (this.mInventory[1].stackSize >= 4) && (spaceForOutput(this.mOutputItem1 = GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 9L), this.mOutputItem2 = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 4L)))) {
                    getBaseMetaTileEntity().decrStackSize(0, 1);
                    getBaseMetaTileEntity().decrStackSize(1, 4*3);
                    this.mMaxProgresstime = 64800*5;
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
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aIndex > 1;
    }

    @Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (aIndex < 2) {
        }
        return !GT_Utility.areStacksEqual(aStack, this.mInventory[0]);
    }

    @Override
	public byte getTileEntityBaseType() {
        return 0;
    }
}
