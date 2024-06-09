/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_BY_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.V;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.bartimaeusnek.bartworks.API.modularUI.BW_UITextures;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.items.BW_Stonage_Rotors;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_RotorBlock;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

public class GT_TileEntity_Windmill extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_Windmill>
    implements ISurvivalConstructable, IGetTitleColor {

    private static final IIcon[] iIcons = new IIcon[2];
    private static final IIconContainer[] iIconContainers = new IIconContainer[2];
    private static final ITexture[] iTextures = new ITexture[3];

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
    private static final IStructureDefinition<GT_TileEntity_Windmill> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_TileEntity_Windmill>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "       ", "       ", "       ", "   p   ", "       ", "       ", "       " },
                    { "       ", "       ", "  ppp  ", "  p p  ", "  ppp  ", "       ", "       " },
                    { "       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
                    { " ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
                    { " ppspp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
                    { " ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
                    { "       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { " bb~bb ", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", " bbbbb " }, }))
        .addElement('p', ofBlockAnyMeta(Blocks.planks))
        .addElement(
            'c',
            ofChain(
                onElementPass(t -> t.mHardenedClay++, ofBlock(Blocks.hardened_clay, 0)),
                ofTileAdder(GT_TileEntity_Windmill::addDispenserToOutputSet, Blocks.hardened_clay, 0),
                onElementPass(t -> t.mDoor++, new IStructureElementNoPlacement<GT_TileEntity_Windmill>() {

                    private final IStructureElement<GT_TileEntity_Windmill> delegate = ofBlock(Blocks.wooden_door, 0);

                    @Override
                    public boolean check(GT_TileEntity_Windmill gt_tileEntity_windmill, World world, int x, int y,
                        int z) {
                        return this.delegate.check(gt_tileEntity_windmill, world, x, y, z);
                    }

                    @Override
                    public boolean spawnHint(GT_TileEntity_Windmill gt_tileEntity_windmill, World world, int x, int y,
                        int z, ItemStack trigger) {
                        return this.delegate.spawnHint(gt_tileEntity_windmill, world, x, y, z, trigger);
                    }
                })))
        .addElement('b', ofBlock(Blocks.brick_block, 0))
        .addElement('s', new IStructureElement<GT_TileEntity_Windmill>() {

            @Override
            public boolean check(GT_TileEntity_Windmill t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return t.setRotorBlock(tileEntity);
            }

            @Override
            public boolean spawnHint(GT_TileEntity_Windmill t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), 0);
                return true;
            }

            @Override
            public boolean placeBlock(GT_TileEntity_Windmill gt_tileEntity_windmill, World world, int x, int y, int z,
                ItemStack trigger) {
                return false;
            }

            @Override
            public BlocksToPlace getBlocksToPlace(GT_TileEntity_Windmill gt_tileEntity_windmill, World world, int x,
                int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
                return BlocksToPlace.create(new ItemStack(ItemRegistry.ROTORBLOCK));
            }
        })
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
            .addCasingInfoMin("Hardened Clay block", 40, false)
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
        if (!this.rotorBlock.rotorSlot.isEmpty()) this.setRotorDamage(this.rotorBlock, this.rotorBlock.getGrindPower());
        return this.rotorBlock.getGrindPower() > 0;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    private float[] multiplierRecipe(ItemStack itemStack) {
        // will return max and min value of the multiplier, the average of these is used to calculate the multiplier.
        final Item item = itemStack.getItem();
        if (item == Items.wheat) {
            return new float[] { 1.13f, 1.5f };
        }
        final Block block = Block.getBlockFromItem(item);
        if (item == Items.bone || block == Blocks.glowstone || block == Blocks.pumpkin) {
            return new float[] { 0.8f, 1f };
        }
        if (block == Blocks.gravel || block == Blocks.cobblestone
            || block == Blocks.stone
            || block == Blocks.sandstone
            || block == Blocks.clay
            || block == Blocks.hardened_clay
            || block == Blocks.stained_hardened_clay
            || block == Blocks.wool
            || block == Blocks.netherrack
            || block == Blocks.log
            || block == Blocks.log2) {
            return new float[] { 1f, 1.5f };
        }
        final ItemData association = GT_OreDictUnificator.getAssociation(itemStack);
        final OrePrefixes prefix = association == null ? null : association.mPrefix;
        if (prefix == null || association.mMaterial == null
            || association.mMaterial.mMaterial == null
            || association.mMaterial.mMaterial.getDust(1) == null) {
            return new float[] { 1f, 1f };
        }
        if (OrePrefixes.ore == prefix || OrePrefixes.oreNetherrack == prefix
            || OrePrefixes.oreEndstone == prefix
            || OrePrefixes.oreBlackgranite == prefix
            || OrePrefixes.oreRedgranite == prefix
            || OrePrefixes.oreMarble == prefix
            || OrePrefixes.oreBasalt == prefix) {
            return new float[] { 0.5f, 1f };
        }
        if (OrePrefixes.stone == prefix || OrePrefixes.stoneBricks == prefix
            || OrePrefixes.stoneChiseled == prefix
            || OrePrefixes.stoneCobble == prefix
            || OrePrefixes.stoneCracked == prefix
            || OrePrefixes.stoneMossy == prefix
            || OrePrefixes.stoneMossyBricks == prefix
            || OrePrefixes.stoneSmooth == prefix
            || OrePrefixes.cobblestone == prefix) {
            return new float[] { 1f, 1.5f };
        }
        return new float[] { 1f, 1f };
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) return false;

        if (this.mOutputItems == null) this.mOutputItems = new ItemStack[2];

        GT_Recipe tRecipe = RecipeMaps.maceratorRecipes
            .findRecipe(this.getBaseMetaTileEntity(), false, false, V[1], null, itemStack);
        if (tRecipe == null) {
            return false;
        }

        if (tRecipe.getOutput(0) != null) {
            // Decrease input stack by appropriate amount (Not always 1)
            for (int i = 0; i < this.mMulti; i++) {
                if (!tRecipe.isRecipeInputEqual(true, null, itemStack)) {
                    this.mMulti = i;
                    break;
                }
            }
            this.updateSlots();
            this.mOutputItems[0] = tRecipe.getOutput(0);
            float[] mRecipe = this.multiplierRecipe(itemStack);
            float multiper = Math.min(
                mRecipe[1],
                Math.max(
                    mRecipe[0],
                    2f * (float) Math.sqrt((float) 1 / (this.rotorBlock.getWindStrength() + 1))
                        * this.OutputMultiplier(this.rotorBlock)
                        * (mRecipe[0] + mRecipe[1])));
            int amount = (int) Math.floor(multiper * (this.mOutputItems[0].stackSize * this.mMulti));

            // Split ItemStack --by gtpp
            List<ItemStack> splitStacks = new ArrayList<>();
            while (amount > this.mOutputItems[0].getMaxStackSize()) {
                ItemStack tmp = this.mOutputItems[0].copy();
                tmp.stackSize = this.mOutputItems[0].getMaxStackSize();
                amount -= this.mOutputItems[0].getMaxStackSize();
                splitStacks.add(tmp);
            }
            ItemStack tmp = this.mOutputItems[0].copy();
            tmp.stackSize = amount;
            splitStacks.add(tmp);
            this.mOutputItems = splitStacks.toArray(new ItemStack[splitStacks.size()]);
        }
        this.mMaxProgresstime = tRecipe.mDuration * 2 * 100 * this.mMulti / this.getSpeed(this.rotorBlock);
        this.mMulti = 16;
        return true;
    }

    @Override
    public void stopMachine() {
        this.getBaseMetaTileEntity()
            .disableWorking();
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

    @Override
    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;

        for (TileEntityDispenser tHatch : this.tileEntityDispensers) {
            for (int i = tHatch.getSizeInventory() - 1; i >= 0; i--) {
                if (tHatch.getStackInSlot(i) == null || GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)
                    && aStack.stackSize + tHatch.getStackInSlot(i).stackSize <= 64) {
                    if (GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)) {
                        ItemStack merge = tHatch.getStackInSlot(i)
                            .copy();
                        merge.stackSize = aStack.stackSize + tHatch.getStackInSlot(i).stackSize;
                        tHatch.setInventorySlotContents(i, merge);
                    } else {
                        tHatch.setInventorySlotContents(i, aStack.copy());
                    }

                    if (GT_Utility.areStacksEqual(tHatch.getStackInSlot(i), aStack)) {
                        aStack = null;
                        return true;
                    }
                    tHatch.setInventorySlotContents(i, null);
                    aStack = null;
                    return false;
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

        return this.checkPiece(STRUCTURE_PIECE_MAIN, 3, 11, 0) && !this.tileEntityDispensers.isEmpty()
            && this.mDoor <= 2
            && this.mHardenedClay >= 40;
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
        return new String[] { "Progress:",
            this.mProgresstime + " Grindings of " + this.mMaxProgresstime + " needed Grindings", "GrindPower:",
            this.rotorBlock.getGrindPower() + "KU/t" };
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {

        ITexture[] ret = new ITexture[6];

        if (this.isClientSide()) {

            if (facing == side || side == ForgeDirection.DOWN) {
                GT_TileEntity_Windmill.iTextures[0] = TextureFactory.of(GT_TileEntity_Windmill.iIconContainers[0]);
                Arrays.fill(ret, GT_TileEntity_Windmill.iTextures[0]);
            } else if (side == ForgeDirection.UP) {
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
        if (this.getBaseMetaTileEntity()
            .getWorld() != null)
            return this.getBaseMetaTileEntity()
                .getWorld().isRemote
                    ? FMLCommonHandler.instance()
                        .getSide() == Side.CLIENT
                    : FMLCommonHandler.instance()
                        .getEffectiveSide() == Side.CLIENT;
        return FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 11, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 11, 0, elementBudget, env, false, true);
    }

    public float OutputMultiplier(BW_RotorBlock rotorBlock) {
        try {
            return ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get()
                .getItem()).getmRotor();
        } catch (Exception e) {
            return 1f;
        }
    }

    public int getSpeed(BW_RotorBlock rotorBlock) {
        try {
            return ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get()
                .getItem()).getSpeed();
        } catch (Exception e) {
            return 1;
        }
    }

    public void setRotorDamage(BW_RotorBlock rotorBlock, int damage) {
        try {
            ((BW_Stonage_Rotors) rotorBlock.rotorSlot.get()
                .getItem()).damageItemStack(rotorBlock.rotorSlot.get(), damage);
        } catch (Exception e) {
            rotorBlock.rotorSlot.damage(damage, false);
        }
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(BW_UITextures.BACKGROUND_BROWN)
            .setItemSlot(BW_UITextures.SLOT_BROWN)
            .setTitleTab(
                BW_UITextures.TAB_TITLE_BROWN,
                BW_UITextures.TAB_TITLE_DARK_BROWN,
                BW_UITextures.TAB_TITLE_ANGULAR_BROWN);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(BW_UITextures.PICTURE_BW_LOGO_47X21)
                .setSize(47, 21)
                .setPos(123, 59));
    }

    @Override
    public int getTitleColor() {
        return this.COLOR_TITLE_WHITE.get();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(this.inventoryHandler, 1).setBackground(
                this.getGUITextureSet()
                    .getItemSlot())
                .setPos(59, 35))
            .widget(new DrawableWidget() {

                private static final int DIVIDER = 125;

                @Override
                public void onScreenUpdate() {
                    super.onScreenUpdate();
                    if (GT_TileEntity_Windmill.this.mMaxProgresstime > 0) {
                        if (System.currentTimeMillis() / DIVIDER % 40 == 30)
                            this.setDrawable(BW_UITextures.PICTURE_WINDMILL_ROTATING[3]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 20)
                            this.setDrawable(BW_UITextures.PICTURE_WINDMILL_ROTATING[2]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 10)
                            this.setDrawable(BW_UITextures.PICTURE_WINDMILL_ROTATING[1]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 0)
                            this.setDrawable(BW_UITextures.PICTURE_WINDMILL_ROTATING[0]);
                    } else {
                        this.setDrawable(BW_UITextures.PICTURE_WINDMILL_EMPTY);
                    }
                }
            }.setDrawable(BW_UITextures.PICTURE_WINDMILL_EMPTY)
                .setPos(85, 27)
                .setSize(32, 32))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> this.mMaxProgresstime, val -> this.mMaxProgresstime = val))
            .widget(
                new ItemDrawable(
                    () -> this.mMachine && !this.getBaseMetaTileEntity()
                        .isActive()
                            ? GT_MetaGenerated_Tool_01.INSTANCE
                                .getToolWithStats(GT_MetaGenerated_Tool_01.SOFTMALLET, 1, null, null, null)
                            : null).asWidget()
                                .setPos(66, 66))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> this.getBaseMetaTileEntity()
                        .isActive(),
                    val -> this.getBaseMetaTileEntity()
                        .setActive(val)))
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure."))
                    .setDefaultColor(this.COLOR_TEXT_WHITE.get())
                    .setMaxWidth(150)
                    .setEnabled(widget -> !this.mMachine)
                    .setPos(92, 22))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> this.mMachine, val -> this.mMachine = val));
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
