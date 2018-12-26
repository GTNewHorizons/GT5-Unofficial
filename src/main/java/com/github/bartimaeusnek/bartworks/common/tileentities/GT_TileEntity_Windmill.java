package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.BW_GUIContainer_Windmill;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

import static gregtech.api.enums.GT_Values.V;

public class GT_TileEntity_Windmill extends GT_MetaTileEntity_MultiBlockBase {

    private static IIcon[] iIcons = new IIcon[2];
    private static IIconContainer[] iIconContainers = new IIconContainer[2];
    private static ITexture[] iTextures = new ITexture[2];

    private final ArrayList<TileEntityDispenser> tedList = new ArrayList<TileEntityDispenser>();
    private BW_RotorBlock rotorBlock;
    private byte hasDoor;

    public GT_TileEntity_Windmill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_Windmill(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    public boolean onRunningTick(ItemStack aStack) {
        if (this.mMaxProgresstime > 0)
            this.mProgresstime += this.rotorBlock.getGrindPower();
        return this.rotorBlock.getGrindPower() > 0;
    }

    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    public boolean recipe_fallback(ItemStack aStack){
        //sight... fallback to the macerator recipes
        GT_Recipe.GT_Recipe_Map tMap = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
        if (tMap == null)
            return false;
        GT_Recipe tRecipe = tMap.findRecipe(getBaseMetaTileEntity(), false, false, V[1], null, aStack);
        if (tRecipe == null)
            return false;
        if (tRecipe.getOutput(0) != null) {
            aStack.stackSize--;
            mOutputItems[0] = tRecipe.getOutput(0);
            if (new XSTR().nextInt(2) == 0){
                if (tRecipe.getOutput(1) != null)
                    mOutputItems[1] = tRecipe.getOutput(1);
                else
                    mOutputItems[1] = tRecipe.getOutput(0);
            }
        }
        this.mMaxProgresstime = (tRecipe.mDuration * 2 *100);
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {

        if (itemStack == null || itemStack.getItem() == null)
            return false;

        if (this.mOutputItems == null)
            this.mOutputItems = new ItemStack[2];

        //Override Recipes that doesnt quite work well with OreUnificator
        //Items
        if (itemStack.getItem().equals(Items.wheat)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 15 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L));
            this.mOutputItems[1] = (GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wheat, 1L));
            return true;
        } else if (itemStack.getItem().equals(Items.bone)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 15 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.dye, 4, 15);
            else
                this.mOutputItems[0] = new ItemStack(Items.dye, 3, 15);
            return true;
        }
        //Blocks
        else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.gravel)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 30 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.flint, 2);
            else
                this.mOutputItems[0] = new ItemStack(Items.flint);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.cobblestone) || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.stone)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 2L);
            else
                this.mOutputItems[0] = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.sandstone)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 45 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Blocks.sand, 3);
            else
                this.mOutputItems[0] = new ItemStack(Blocks.sand, 2);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.clay) || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.hardened_clay) || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.stained_hardened_clay)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            this.mOutputItems[0] = Materials.Clay.getDust(1);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.redstone_block)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            this.mOutputItems[0] = Materials.Redstone.getDust(9);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.glass)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L));
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.wool)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.string, 3);
            else
                this.mOutputItems[0] = new ItemStack(Items.string, 2);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.glowstone)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.glowstone_dust, 4);
            else
                this.mOutputItems[0] = new ItemStack(Items.glowstone_dust, 3);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.netherrack)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 2L));
            else
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1L));
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.log)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L));
            else
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.log2)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L));
            else
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.pumpkin)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 15 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.pumpkin_seeds, 2);
            else
                this.mOutputItems[0] = new ItemStack(Items.pumpkin_seeds, 1);
            return true;
        } else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.melon_block)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 15 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = new ItemStack(Items.melon_seeds, 2);
            else
                this.mOutputItems[0] = new ItemStack(Items.melon_seeds, 1);
            return true;
        }

        //null checks for GT shit
        if (GT_OreDictUnificator.getAssociation(itemStack) == null ||
                GT_OreDictUnificator.getAssociation(itemStack).mPrefix == null ||
                GT_OreDictUnificator.getAssociation(itemStack).mMaterial == null ||
                GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial == null ||
                GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial.getDust(1) == null
        )
            return recipe_fallback(itemStack); //fallback for all non-unificated Items

        //Ore Unificator shit for balance
        if (OrePrefixes.ingot.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) || OrePrefixes.gem.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 45 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        } else if (OrePrefixes.ore.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.crushed, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        } else if (OrePrefixes.crushed.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 30 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dustImpure, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        } else if (OrePrefixes.crushedPurified.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 30 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dustPure, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        } else if (OrePrefixes.crushedCentrifuged.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 30 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        } else if (OrePrefixes.block.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, (GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial.mSubTags.contains(SubTag.METAL) || GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial.mSubTags.contains(SubTag.CRYSTAL)) ? 9L : 1L));
            return true;
        } else if (
                        OrePrefixes.stone.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneBricks.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneChiseled.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneCobble.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneCracked.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneMossy.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneMossyBricks.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.stoneSmooth.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix) ||
                        OrePrefixes.cobblestone.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
        ) {
            itemStack.stackSize -= 1;
            this.mMaxProgresstime = 60 * 20 * 100;
            if (new XSTR().nextInt(2) == 0)
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 2L));
            else
                this.mOutputItems[0] = (GT_OreDictUnificator.get(OrePrefixes.dust, GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial, 1L));
            return true;
        }
        return recipe_fallback(itemStack); //2nd fallback
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new BW_GUIContainer_Windmill(aPlayerInventory, aBaseMetaTileEntity,this.getLocalName());
    }

    public boolean addDispenserToOutputSet(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileEntityDispenser) {
            this.tedList.add((TileEntityDispenser) aTileEntity);
            return true;
        }
        return false;
    }

    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack))
            return false;

        for (TileEntityDispenser tHatch : this.tedList) {
            for (int i = tHatch.getSizeInventory() - 1; i >= 0; i--) {
                if (tHatch.getStackInSlot(i) == null || GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack) && aStack.stackSize + tHatch.getStackInSlot(i).stackSize <= 64) {
                    if (GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)) {
                        ItemStack merge = tHatch.getStackInSlot(i).copy();
                        merge.stackSize = aStack.stackSize + tHatch.getStackInSlot(i).stackSize;
                        tHatch.setInventorySlotContents(i, merge);
                    } else {
                        tHatch.setInventorySlotContents(i, aStack.copy());
                    }

                    if (GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)) {
                        aStack = null;
                        return true;
                    } else {
                        tHatch.setInventorySlotContents(i, null);
                        aStack = null;
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {

        /*
         * offset x1 = 3x3
         * offset x2 = 5x5
         * offset x3 = 7x7
         * etc.
         */

        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;

        //floor
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (!((Math.abs(x) == 3 && Math.abs(z) == 3) || (xDir + x == 0 && zDir + z == 0)))
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + x, 0, zDir + z) != Blocks.brick_block) {
                        return false;
                    }
            }
        }

        //h_clay shaft
        for (int y = 1; y <= 4; y++)
            for (int x = -2; x <= 2; x++)
                for (int z = -2; z <= 2; z++) {
                    if (!((Math.abs(x) == 2 && Math.abs(z) == 2) || (Math.abs(x) < 2 && Math.abs(z) != 2)))
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z) != Blocks.hardened_clay && !this.addDispenserToOutputSet(aBaseMetaTileEntity.getTileEntityOffset(xDir + x, y, zDir + z))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z) == Blocks.wooden_door && this.hasDoor < 3) {
                                this.hasDoor++;
                                continue;
                            }
                            return false;
                        }
                }

        //plank layer 1
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (!(Math.abs(x) < 2 && Math.abs(z) != 2)) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + x, 5, zDir + z) != Blocks.planks)
                        return false;
                }
            }

        //plank layer 2-4
        for (int x = -3; x <= 3; x++)
            for (int y = 6; y <= 8; y++)
                for (int z = -3; z <= 3; z++)
                    if (!(((Math.abs(x) == 3 && Math.abs(z) == 3) || (Math.abs(x) < 3 && (Math.abs(z) != 2 || Math.abs(z) != 1))) || (xDir + x == 0 && zDir + z == 0 && y == 7)))
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z) != Blocks.planks)
                            return false;

        //plank layer 5
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                if (!(Math.abs(x) < 2 && (Math.abs(z) != 2))) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + x, 9, zDir + z) != Blocks.planks)
                        return false;
                }
        //plank layer 6
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                if (!(Math.abs(x) < 1 && (Math.abs(z) != 1))) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + x, 10, zDir + z) != Blocks.planks)
                        return false;
                }
        //plank layer 7
        if (aBaseMetaTileEntity.getBlockOffset(xDir, 11, zDir) != Blocks.planks)
            return false;

        //Rotor Block
        TileEntity te = this.getBaseMetaTileEntity().getWorld().getTileEntity(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord() + 7, this.getBaseMetaTileEntity().getZCoord());

        if (te instanceof BW_RotorBlock)
            this.rotorBlock = (BW_RotorBlock) te;
        else
            return false;

        //check for outputs
        if (this.tedList.isEmpty())
            return false;

        this.mWrench = true;
        this.mScrewdriver = true;
        this.mSoftHammer = true;
        this.mHardHammer = true;
        this.mSolderingTool = true;
        this.mCrowbar = true;

        //reset door cause bugs >_>
        this.hasDoor = 0;

        return true;
    }


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mEfficiency < 0)
                this.mEfficiency = 0;
            if (--this.mUpdate == 0 || --this.mStartUpCheck == 0) {
                this.hasDoor = 0;
                this.tedList.clear();
                this.mMachine = this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
            }
            if (this.mStartUpCheck < 0) {
                if (this.mMachine) {
                    if (this.mMaxProgresstime > 0) {
                        if (this.onRunningTick(this.mInventory[1])) {
                            if (this.mMaxProgresstime > 0 && this.mProgresstime >= this.mMaxProgresstime) {
                                if (this.mOutputItems != null)
                                    for (ItemStack tStack : this.mOutputItems)
                                        if (tStack != null) {
                                            this.addOutput(tStack);
                                        }
                                this.mEfficiency = 10000;
                                this.mOutputItems = new ItemStack[2];
                                this.mProgresstime = 0;
                                this.mMaxProgresstime = 0;
                                this.mEfficiencyIncrease = 0;
                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    if (this.checkRecipe(this.mInventory[1]))
                                        this.updateSlots();
                                }
                            }
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (this.checkRecipe(this.mInventory[1]))
                                    this.updateSlots();
                            }
                        }
                    }
                } else {
                    this.mMachine = this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
                    return;
                }
            } else {
                this.stopMachine();
            }
        }
        aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (this.mWrench ? 0 : 1) | (this.mScrewdriver ? 0 : 2) | (this.mSoftHammer ? 0 : 4) | (this.mHardHammer ? 0 : 8) | (this.mSolderingTool ? 0 : 16) | (this.mCrowbar ? 0 : 32) | (this.mMachine ? 0 : 64));
        aBaseMetaTileEntity.setActive(this.mMaxProgresstime > 0);
    }

    @Override
    public int getCurrentEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public void updateSlots() {
        if (this.mInventory[1] != null && this.mInventory[1].stackSize <= 0) {
            this.mInventory[1] = null;
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }


    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_Windmill(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A primitive Grinder powered by Kinetic energy.",
                "WxHxL: 7x12x7",
                "Layer 1: 7x7 Bricks, corners are air, controller at front centered.",
                "Layer 2-4: 5x5 Hardened Clay, corners are air, can contain one door,",
                "hollow, must contain at least one Dispenser",
                "Layer 5: 5x5 Wood Planks. Corners are filled, hollow.",
                "Layer 6: 7x7 Wood Planks. Corners are air, hollow.",
                "Layer 8: 7x7 Wood Planks. Corners are air, hollow,",
                "front centered must be a Primitive Kinetic Shaftbox",
                "Layer 9: 7x7 Wood Planks. Corners are air, hollow.",
                "Layer 10: 5x5 Wood Planks. Corners are filled, hollow.",
                "Layer 11: 3x3 Wood Planks. Corners are filled, hollow.",
                "Layer 12: 1x1 Wood Plank.",
                "Needs a Wind Mill Rotor in the Shaftbox to operate",
                "Input items in Controller",
                "Output items will appear in the dispensers",
                "Added by bartimaeusnek via "+ ChatColorHelper.DARKGREEN+"BartWorks"
        };
    }

    @Override
    public String[] getInfoData() {
        return new String[]{"Progress:", this.mProgresstime + " Grindings of " + this.mMaxProgresstime + " needed Grindings", "GrindPower:", Integer.toString(this.rotorBlock.getGrindPower()) + "KU/t"};
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
            iIcons[0]=aBlockIconRegister.registerIcon("brick");
            iIconContainers[0]=new IIconContainer() {
                @Override
                public IIcon getIcon() {
                    return iIcons[0];
                }

                @Override
                public IIcon getOverlayIcon() {
                    return iIcons[0];
                }

                @Override
                public ResourceLocation getTextureFile() {
                    return new ResourceLocation("brick");
                }
            };

        iIcons[1]=aBlockIconRegister.registerIcon(MainMod.modID+":windmill_top");
        iIconContainers[1]=new IIconContainer() {
            @Override
            public IIcon getIcon() {
                return iIcons[1];
            }

            @Override
            public IIcon getOverlayIcon() {
                return iIcons[1];
            }

            @Override
            public ResourceLocation getTextureFile() {
                return new ResourceLocation(MainMod.modID+":windmill_top");
            }
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {

        ITexture[] ret = new ITexture[0];

        if (isClientSide()) {
            iTextures[0] = new GT_RenderedTexture(iIconContainers[0], Dyes.getModulation(0, Dyes.MACHINE_METAL.mRGBa));

            ret = new ITexture[6];
                for (int i = 0; i < 6; i++) {
                    ret[i] = iTextures[0];
            }
            if (aSide == 1){
                iTextures[1] = new GT_RenderedTexture(iIconContainers[1], Dyes.getModulation(0, Dyes.MACHINE_METAL.mRGBa));

                ret = new ITexture[6];
                for (int i = 0; i < 6; i++) {
                    ret[i] = iTextures[1];
                }
            }

        }


        return ret;
    }

    public boolean isClientSide() {
        if (this.getBaseMetaTileEntity().getWorld() != null)
            return this.getBaseMetaTileEntity().getWorld().isRemote ? FMLCommonHandler.instance().getSide() == Side.CLIENT : FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }
}
