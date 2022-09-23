/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.V;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.client.gui.BW_GUIContainer_Windmill;
import com.github.bartimaeusnek.bartworks.common.items.BW_Stonage_Rotors;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_RotorBlock;
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_Windmill;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GT_TileEntity_Windmill extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_Windmill>
        implements ISurvivalConstructable {

    private static final IIcon[] iIcons = new IIcon[2];
    private static final IIconContainer[] iIconContainers = new IIconContainer[2];
    private static final ITexture[] iTextures = new ITexture[3];

    private static final XSTR localRandomInstance = new XSTR();
    private BW_RotorBlock rotorBlock;
    private int mDoor = 0;
    private int mHardenedClay = 0;
    private int mMulti = 16;

    public GT_TileEntity_Windmill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private GT_TileEntity_Windmill(String aName) {
        super(aName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_Windmill> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_Windmill>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {"       ", "       ", "       ", "   p   ", "       ", "       ", "       "},
                        {"       ", "       ", "  ppp  ", "  p p  ", "  ppp  ", "       ", "       "},
                        {"       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       "},
                        {" ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp "},
                        {" ppspp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp "},
                        {" ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp "},
                        {"       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       "},
                        {"       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       "},
                        {"       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       "},
                        {"       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       "},
                        {"       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       "},
                        {" bb~bb ", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", " bbbbb "},
                    }))
                    .addElement('p', ofBlockAnyMeta(Blocks.planks))
                    .addElement(
                            'c',
                            ofChain(
                                    onElementPass(t -> t.mHardenedClay++, ofBlock(Blocks.hardened_clay, 0)),
                                    ofTileAdder(
                                            GT_TileEntity_Windmill::addDispenserToOutputSet, Blocks.hardened_clay, 0),
                                    onElementPass(t -> t.mDoor++, ofBlock(Blocks.wooden_door, 0))))
                    .addElement('b', ofBlock(Blocks.brick_block, 0))
                    .addElement(
                            's', ofTileAdder(GT_TileEntity_Windmill::setRotorBlock, StructureLibAPI.getBlockHint(), 0))
                    .build();

    @Override
    public IStructureDefinition<GT_TileEntity_Windmill> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Windmill")
                .addInfo("Controller block for the Windmill")
                .addInfo("A primitive Grinder powered by Kinetic energy")
                .addInfo("Speed and output will be affected by wind speed, recipe and rotor")
                .addInfo("Please use the Primitive Rotor")
                .addInfo("Macerates 16 items at a time")
                .addInfo("The structure is too complex!")
                .addInfo("Follow the StructureLib hologram projector to build the main structure.")
                .addSeparator()
                .beginStructureBlock(7, 12, 7, false)
                .addController("Front bottom center")
                .addCasingInfo("Hardened Clay block", 40)
                .addOtherStructurePart("Dispenser", "Any Hardened Clay block")
                .addOtherStructurePart("0-1 Wooden door", "Any Hardened Clay block")
                .addStructureHint("Primitive Kinetic Shaftbox", 1)
                .toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    private final Set<TileEntityDispenser> tileEntityDispensers = new HashSet<>();

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mMaxProgresstime > 0) this.mProgresstime += this.rotorBlock.getGrindPower();
        if (!rotorBlock.rotorSlot.isEmpty()) this.setRotorDamage(rotorBlock, this.rotorBlock.getGrindPower());
        return this.rotorBlock.getGrindPower() > 0;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    private float[] multiplierRecipe(ItemStack itemStack) {
        // will return max and min value of the multiplier, the average of these is used to calculate the multiplier.
        if (itemStack.getItem().equals(Items.wheat)) return new float[] {1.13f, 1.5f};
        else if (itemStack.getItem().equals(Items.bone)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.glowstone)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.pumpkin)) return new float[] {0.8f, 1f};
        else if (Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.gravel)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.cobblestone)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.stone)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.sandstone)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.clay)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.hardened_clay)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.stained_hardened_clay)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.wool)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.netherrack)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.log)
                || Block.getBlockFromItem(itemStack.getItem()).equals(Blocks.log2)) return new float[] {1f, 1.5f};
        else if (GT_OreDictUnificator.getAssociation(itemStack) == null
                || GT_OreDictUnificator.getAssociation(itemStack).mPrefix == null
                || GT_OreDictUnificator.getAssociation(itemStack).mMaterial == null
                || GT_OreDictUnificator.getAssociation(itemStack).mMaterial.mMaterial == null
                || GT_OreDictUnificator.getAssociation(itemStack)
                                .mMaterial
                                .mMaterial
                                .getDust(1)
                        == null) return new float[] {1f, 1f};
        else if (OrePrefixes.ore.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreNetherrack.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreEndstone.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreBlackgranite.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreRedgranite.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreMarble.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.oreBasalt.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix))
            return new float[] {0.5f, 1f};
        else if (OrePrefixes.stone.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneBricks.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneChiseled.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneCobble.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneCracked.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneMossy.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneMossyBricks.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.stoneSmooth.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix)
                || OrePrefixes.cobblestone.equals(GT_OreDictUnificator.getAssociation(itemStack).mPrefix))
            return new float[] {1f, 1.5f};
        return new float[] {1f, 1f};
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) return false;

        if (this.mOutputItems == null) this.mOutputItems = new ItemStack[2];

        GT_Recipe.GT_Recipe_Map tMap = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
        GT_Recipe tRecipe = tMap.findRecipe(this.getBaseMetaTileEntity(), false, false, V[1], null, itemStack);
        if (tRecipe == null) {
            return false;
        }

        if (tRecipe.getOutput(0) != null) {
            // Decrease input stack by appropriate amount (Not always 1)
            for (int i = 0; i < this.mMulti; i++) {
                if (!tRecipe.isRecipeInputEqual(true, null, itemStack)) {
                    this.mMulti = i + 1;
                    break;
                }
            }
            this.updateSlots();
            this.mOutputItems[0] = tRecipe.getOutput(0);
            float[] mRecipe = multiplierRecipe(itemStack);
            float multiper = Math.min(
                    mRecipe[1],
                    Math.max(
                            mRecipe[0],
                            2f
                                    * ((float) Math.sqrt((float) 1 / (this.rotorBlock.getWindStrength() + 1)))
                                    * OutputMultiplier(rotorBlock)
                                    * (mRecipe[0] + mRecipe[1])));
            int amount = Math.round(multiper * (this.mOutputItems[0].stackSize * this.mMulti));

            // Split ItemStack --by gtpp
            List<ItemStack> splitStacks = new ArrayList<>();
            while (amount > this.mOutputItems[0].getMaxStackSize()) {
                ItemStack tmp = this.mOutputItems[0];
                tmp.stackSize = this.mOutputItems[0].getMaxStackSize();
                amount -= this.mOutputItems[0].getMaxStackSize();
                splitStacks.add(tmp);
            }
            ItemStack tmp = this.mOutputItems[0];
            tmp.stackSize = amount;
            splitStacks.add(tmp);
            mOutputItems = splitStacks.toArray(new ItemStack[splitStacks.size()]);
        }
        this.mMaxProgresstime = (tRecipe.mDuration * 2 * 100 * this.mMulti) / (int) getSpeed(rotorBlock);
        this.mMulti = 16;
        return true;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new BW_GUIContainer_Windmill(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new BW_Container_Windmill(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void stopMachine() {
        getBaseMetaTileEntity().disableWorking();
    }

    public boolean addDispenserToOutputSet(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileEntityDispenser) {
            this.tileEntityDispensers.add((TileEntityDispenser) aTileEntity);
            return true;
        }
        return false;
    }

    public boolean setRotorBlock(TileEntity aTileEntity) {
        if (aTileEntity instanceof BW_RotorBlock) {
            this.rotorBlock = (BW_RotorBlock) aTileEntity;
            return true;
        }
        return false;
    }

    @SuppressWarnings("ALL")
    @Override
    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;

        for (TileEntityDispenser tHatch : this.tileEntityDispensers) {
            for (int i = tHatch.getSizeInventory() - 1; i >= 0; i--) {
                if (tHatch.getStackInSlot(i) == null
                        || GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)
                                && aStack.stackSize + tHatch.getStackInSlot(i).stackSize <= 64) {
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

        this.tileEntityDispensers.clear();
        this.mDoor = 0;
        this.mHardenedClay = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 11, 0)) return false;

        if (this.tileEntityDispensers.isEmpty() || this.mDoor > 2 || this.mHardenedClay < 40) return false;

        this.mWrench = true;
        this.mScrewdriver = true;
        this.mSoftHammer = true;
        this.mHardHammer = true;
        this.mSolderingTool = true;
        this.mCrowbar = true;

        return true;
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
    public String[] getInfoData() {
        return new String[] {
            "Progress:",
            this.mProgresstime + " Grindings of " + this.mMaxProgresstime + " needed Grindings",
            "GrindPower:",
            this.rotorBlock.getGrindPower() + "KU/t"
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        GT_TileEntity_Windmill.iIcons[0] = Blocks.brick_block.getIcon(0, 0);
        GT_TileEntity_Windmill.iIconContainers[0] = new IIconContainer() {
            @Override
            public IIcon getIcon() {
                return GT_TileEntity_Windmill.iIcons[0];
            }

            @Override
            public IIcon getOverlayIcon() {
                return null;
            }

            @Override
            public ResourceLocation getTextureFile() {
                return new ResourceLocation("brick");
            }
        };

        GT_TileEntity_Windmill.iIcons[1] = aBlockIconRegister.registerIcon(MainMod.MOD_ID + ":windmill_top");
        GT_TileEntity_Windmill.iIconContainers[1] = new IIconContainer() {
            @Override
            public IIcon getIcon() {
                return GT_TileEntity_Windmill.iIcons[1];
            }

            @Override
            public IIcon getOverlayIcon() {
                return null;
            }

            @Override
            public ResourceLocation getTextureFile() {
                return new ResourceLocation(MainMod.MOD_ID + ":windmill_top");
            }
        };
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {

        ITexture[] ret = new ITexture[6];

        if (this.isClientSide()) {

            if (aFacing == aSide || aSide == 0) {
                GT_TileEntity_Windmill.iTextures[0] = TextureFactory.of(GT_TileEntity_Windmill.iIconContainers[0]);
                Arrays.fill(ret, GT_TileEntity_Windmill.iTextures[0]);
            } else if (aSide == 1) {
                GT_TileEntity_Windmill.iTextures[1] = TextureFactory.of(GT_TileEntity_Windmill.iIconContainers[1]);
                Arrays.fill(ret, GT_TileEntity_Windmill.iTextures[1]);
            } else {
                GT_TileEntity_Windmill.iTextures[2] = TextureFactory.of(Textures.BlockIcons.COVER_WOOD_PLATE);
                Arrays.fill(ret, GT_TileEntity_Windmill.iTextures[2]);
            }
        }
        return ret;
    }

    public boolean isClientSide() {
        if (this.getBaseMetaTileEntity().getWorld() != null)
            return this.getBaseMetaTileEntity().getWorld().isRemote
                    ? FMLCommonHandler.instance().getSide() == Side.CLIENT
                    : FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 11, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(
                STRUCTURE_PIECE_MAIN, stackSize, 3, 11, 0, elementBudget, source, actor, false, true);
    }

    public float OutputMultiplier(BW_RotorBlock rotorBlock) {
        try {
            return ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get().getItem()).getmRotor();
        } catch (Exception e) {
            return 1f;
        }
    }

    public int getSpeed(BW_RotorBlock rotorBlock) {
        try {
            return ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get().getItem()).getSpeed();
        } catch (Exception e) {
            return 1;
        }
    }

    public void setRotorDamage(BW_RotorBlock rotorBlock, int damage) {
        try {
            ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get().getItem())
                    .damageItemStack(rotorBlock.rotorSlot.get(), damage);
        } catch (Exception e) {
            rotorBlock.rotorSlot.damage(damage, false);
        }
    }
}
