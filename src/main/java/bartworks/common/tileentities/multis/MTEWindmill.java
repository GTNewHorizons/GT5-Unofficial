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

package bartworks.common.tileentities.multis;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;

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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import bartworks.API.modularUI.BWUITextures;
import bartworks.MainMod;
import bartworks.common.items.ItemStonageRotors;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.tileentities.classic.TileEntityRotorBlock;
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
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class MTEWindmill extends MTEEnhancedMultiBlockBase<MTEWindmill>
    implements ISurvivalConstructable, IGetTitleColor {

    private static final IIcon[] iIcons = new IIcon[2];
    private static final IIconContainer[] iIconContainers = new IIconContainer[2];
    private static final ITexture[] iTextures = new ITexture[3];

    private TileEntityRotorBlock rotorBlock;
    private int mDoor = 0;
    private int mHardenedClay = 0;
    private int mMulti = 16;

    public MTEWindmill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTEWindmill(String aName) {
        super(aName);
    }

    private static final IStructureElement<MTEWindmill> DISPENSER_OR_CLAY = new IStructureElement<>() {

        @Override
        public boolean check(MTEWindmill t, World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);

            if (block == Blocks.dispenser) {
                return t.addDispenserToOutputSet(world.getTileEntity(x, y, z));
            }

            if (block == Blocks.hardened_clay && world.getBlockMetadata(x, y, z) == 0) {
                t.mHardenedClay++;
                return true;
            }

            return false;
        }

        @Override
        public boolean spawnHint(MTEWindmill t, World world, int x, int y, int z, ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, Blocks.hardened_clay, 0);
            return true;
        }

        @Override
        public boolean placeBlock(MTEWindmill t, World world, int x, int y, int z, ItemStack trigger) {
            return world.setBlock(x, y, z, Blocks.hardened_clay, 0, 3);
        }

        @Override
        public BlocksToPlace getBlocksToPlace(MTEWindmill t, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            return BlocksToPlace
                .create(new ItemStack(Blocks.dispenser, 1, 0), new ItemStack(Blocks.hardened_clay, 1, 0));
        }
    };

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEWindmill> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEWindmill>builder()
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
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  cdc  ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
                    { " bb~bb ", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", " bbbbb " }, }))
        .addElement('p', ofBlockAnyMeta(Blocks.planks))
        .addElement(
            'c',
            ofChain(
                onElementPass(t -> t.mHardenedClay++, ofBlock(Blocks.hardened_clay, 0)),
                ofTileAdder(MTEWindmill::addDispenserToOutputSet, Blocks.hardened_clay, 0),
                onElementPass(t -> t.mDoor++, new IStructureElementNoPlacement<MTEWindmill>() {

                    private final IStructureElement<MTEWindmill> delegate = ofBlock(Blocks.wooden_door, 0);

                    @Override
                    public boolean check(MTEWindmill gt_tileEntity_windmill, World world, int x, int y, int z) {
                        return this.delegate.check(gt_tileEntity_windmill, world, x, y, z);
                    }

                    @Override
                    public boolean spawnHint(MTEWindmill gt_tileEntity_windmill, World world, int x, int y, int z,
                        ItemStack trigger) {
                        return this.delegate.spawnHint(gt_tileEntity_windmill, world, x, y, z, trigger);
                    }
                })))
        .addElement('d', DISPENSER_OR_CLAY)
        .addElement('b', ofBlock(Blocks.brick_block, 0))
        .addElement('s', new IStructureElement<>() {

            @Override
            public boolean check(MTEWindmill t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return t.setRotorBlock(tileEntity);
            }

            @Override
            public boolean couldBeValid(MTEWindmill mteWindmill, World world, int x, int y, int z, ItemStack trigger) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof TileEntityRotorBlock;
            }

            @Override
            public boolean spawnHint(MTEWindmill t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), 0);
                return true;
            }

            @Override
            public boolean placeBlock(MTEWindmill gt_tileEntity_windmill, World world, int x, int y, int z,
                ItemStack trigger) {
                return false;
            }

            @Override
            public BlocksToPlace getBlocksToPlace(MTEWindmill gt_tileEntity_windmill, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                return BlocksToPlace.create(new ItemStack(ItemRegistry.ROTORBLOCK));
            }
        })
        .build();

    @Override
    public IStructureDefinition<MTEWindmill> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Windmill")
            .addInfo("A primitive Grinder powered by Kinetic energy")
            .addInfo("Speed and output will be affected by wind speed, recipe and rotor")
            .addInfo("Please use the Primitive Rotor")
            .addInfo("Macerates 16 items at a time")
            .beginStructureBlock(7, 12, 7, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Hardened Clay block", 40, false)
            .addOtherStructurePart("Dispenser", "Any Hardened Clay block")
            .addOtherStructurePart("0-1 Wooden door", "Any Hardened Clay block")
            .addStructureHint("tile.BWRotorBlock.0.name", 1)
            .toolTipFinisher();
        return tt;
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
        final ItemData association = GTOreDictUnificator.getAssociation(itemStack);
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
    public @NotNull CheckRecipeResult checkProcessing() {
        ItemStack itemStack = getControllerSlot();
        if (itemStack == null || itemStack.getItem() == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (this.mOutputItems == null) this.mOutputItems = new ItemStack[2];

        GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
            .caching(false)
            .items(itemStack)
            .voltage(V[1])
            .find();
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
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
            this.mOutputItems = splitStacks.toArray(new ItemStack[0]);
        }
        this.mMaxProgresstime = tRecipe.mDuration * 2 * 100 * this.mMulti / this.getSpeed(this.rotorBlock);
        this.mMulti = 16;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
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
        if (aTileEntity instanceof TileEntityRotorBlock) {
            this.rotorBlock = (TileEntityRotorBlock) aTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public boolean addItemOutputs(ItemStack[] stacks) {
        for (ItemStack stack : stacks) {
            if (GTUtility.isStackInvalid(stack)) continue;

            for (TileEntityDispenser tHatch : this.tileEntityDispensers) {
                for (int i = tHatch.getSizeInventory() - 1; i >= 0; i--) {
                    if (tHatch.getStackInSlot(i) == null || GTUtility.areStacksEqual(tHatch.getStackInSlot(i), stack)
                        && stack.stackSize + tHatch.getStackInSlot(i).stackSize <= 64) {
                        if (GTUtility.areStacksEqual(tHatch.getStackInSlot(i), stack)) {
                            ItemStack merge = tHatch.getStackInSlot(i)
                                .copy();
                            merge.stackSize = stack.stackSize + tHatch.getStackInSlot(i).stackSize;
                            tHatch.setInventorySlotContents(i, merge);
                        } else {
                            tHatch.setInventorySlotContents(i, stack.copy());
                        }

                        if (GTUtility.areStacksEqual(tHatch.getStackInSlot(i), stack)) {
                            return true;
                        }
                        tHatch.setInventorySlotContents(i, null);
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEWindmill(this.mName);
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(StatCollector
            .translateToLocalFormatted("BW.infoData.wind_mill.progress", this.mProgresstime, this.mMaxProgresstime));

        info.add(StatCollector
            .translateToLocalFormatted("BW.infoData.wind_mill.grind_power", this.rotorBlock.getGrindPower()));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        MTEWindmill.iIcons[0] = Blocks.brick_block.getIcon(0, 0);
        MTEWindmill.iIconContainers[0] = new IIconContainer() {

            @Override
            public IIcon getIcon() {
                return MTEWindmill.iIcons[0];
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

        MTEWindmill.iIcons[1] = aBlockIconRegister.registerIcon(MainMod.MOD_ID + ":windmill_top");
        MTEWindmill.iIconContainers[1] = new IIconContainer() {

            @Override
            public IIcon getIcon() {
                return MTEWindmill.iIcons[1];
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
                MTEWindmill.iTextures[0] = TextureFactory.of(MTEWindmill.iIconContainers[0]);
                Arrays.fill(ret, MTEWindmill.iTextures[0]);
            } else if (side == ForgeDirection.UP) {
                MTEWindmill.iTextures[1] = TextureFactory.of(MTEWindmill.iIconContainers[1]);
                Arrays.fill(ret, MTEWindmill.iTextures[1]);
            } else {
                MTEWindmill.iTextures[2] = TextureFactory.of(Textures.BlockIcons.COVER_WOOD_PLATE);
                Arrays.fill(ret, MTEWindmill.iTextures[2]);
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
        return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 11, 0, elementBudget, env, false, true);
    }

    public float OutputMultiplier(TileEntityRotorBlock rotorBlock) {
        ItemStack stack = rotorBlock.rotorSlot.get();
        if (stack == null || !(stack.getItem() instanceof ItemStonageRotors rotor)) {
            return 1;
        }
        return rotor.getmRotor();
    }

    public int getSpeed(TileEntityRotorBlock rotorBlock) {
        ItemStack stack = rotorBlock.rotorSlot.get();
        if (stack == null || !(stack.getItem() instanceof ItemStonageRotors rotor)) {
            return 1;
        }
        return rotor.getSpeed();
    }

    public void setRotorDamage(TileEntityRotorBlock rotorBlock, int damage) {
        ItemStack stack = rotorBlock.rotorSlot.get();
        if (stack == null || !(stack.getItem() instanceof ItemStonageRotors rotor)) {
            rotorBlock.rotorSlot.damage(damage, false);
            return;
        }
        rotor.damageItemStack(stack, damage);
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(BWUITextures.BACKGROUND_BROWN)
            .setItemSlot(BWUITextures.SLOT_BROWN)
            .setTitleTab(
                BWUITextures.TAB_TITLE_BROWN,
                BWUITextures.TAB_TITLE_DARK_BROWN,
                BWUITextures.TAB_TITLE_ANGULAR_BROWN);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(BWUITextures.PICTURE_BW_LOGO_47X21)
                .setSize(47, 21)
                .setPos(123, 59));
    }

    @Override
    public int getTitleColor() {
        return this.COLOR_TITLE_WHITE.get();
    }

    @Override
    protected boolean useMui2() {
        return false;
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
                    if (MTEWindmill.this.mMaxProgresstime > 0) {
                        if (System.currentTimeMillis() / DIVIDER % 40 == 30)
                            this.setDrawable(BWUITextures.PICTURE_WINDMILL_ROTATING[3]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 20)
                            this.setDrawable(BWUITextures.PICTURE_WINDMILL_ROTATING[2]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 10)
                            this.setDrawable(BWUITextures.PICTURE_WINDMILL_ROTATING[1]);
                        else if (System.currentTimeMillis() / DIVIDER % 40 == 0)
                            this.setDrawable(BWUITextures.PICTURE_WINDMILL_ROTATING[0]);
                    } else {
                        this.setDrawable(BWUITextures.PICTURE_WINDMILL_EMPTY);
                    }
                }
            }.setDrawable(BWUITextures.PICTURE_WINDMILL_EMPTY)
                .setPos(85, 27)
                .setSize(32, 32))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> this.mMaxProgresstime, val -> this.mMaxProgresstime = val))
            .widget(
                new ItemDrawable(
                    () -> this.mMachine && !this.getBaseMetaTileEntity()
                        .isActive()
                            ? MetaGeneratedTool01.INSTANCE
                                .getToolWithStats(IDMetaTool01.SOFTMALLET.ID, 1, null, null, null)
                            : null).asWidget()
                                .setPos(66, 66))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> this.getBaseMetaTileEntity()
                        .isActive(),
                    val -> this.getBaseMetaTileEntity()
                        .setActive(val)))
            .widget(
                new TextWidget(GTUtility.trans("138", "Incomplete Structure.")).setTextAlignment(Alignment.CenterLeft)
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
