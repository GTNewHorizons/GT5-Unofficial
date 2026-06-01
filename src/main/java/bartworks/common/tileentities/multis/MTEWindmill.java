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

import bartworks.MainMod;
import bartworks.common.items.ItemStonageRotors;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.tileentities.classic.TileEntityRotorBlock;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.multiblock.MTEWindmillGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTEWindmill extends MTEEnhancedMultiBlockBase<MTEWindmill> implements ISurvivalConstructable {

    private static final IIcon[] iIcons = new IIcon[2];
    private static final IIconContainer[] iIconContainers = new IIconContainer[2];
    private static final ITexture[] iTextures = new ITexture[3];

    private static final int MAX_PARALLELS = 16;

    public TileEntityRotorBlock rotorBlock;
    private int mDoor = 0;
    private int mHardenedClay = 0;

    private enum windLevel {
        NON_EXISTENT,
        PRETTY_LOW,
        COMMON,
        RATHER_STRONG,
        VERY_STRONG,
        TOO_STRONG
    }

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
            .addInfo("Macerates up to 16 items at a time")
            .addInfo("Amount of parallels based on wind speed")
            .addInfo("Processing time is the same regardless of parallels")
            .addInfo("Wind speed can be determined using the Simple Wind Meter")
            .addInfo("========================================================")
            .addInfo("2 parallels: Low")
            .addInfo("2 parallels: Common")
            .addInfo("2 parallels: Rather strong")
            .addInfo("2 parallels: Very Strong")
            .addInfo("========================================================")
            .beginStructureBlock(7, 12, 7, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Hardened Clay Block", 40, false)
            .addOtherStructurePart("Dispenser", "Any Hardened Clay Block")
            .addOtherStructurePart("0-1 Wooden door", "Any Hardened Clay Block")
            .addStructureHint("tile.BWRotorBlock.0.name", 1)
            .toolTipFinisher();
        return tt;
    }

    private final Set<TileEntityDispenser> tileEntityDispensers = new HashSet<>();

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mMaxProgresstime > 0) this.mProgresstime++;
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

    private float multiplierRecipe(ItemStack itemStack) {
        // will return max and min value of the multiplier, the average of these is used to calculate the multiplier.
        final Item item = itemStack.getItem();
        if (item == Items.wheat || item == Items.reeds) {
            return 1.5f;
        }
        if (item == Items.clay_ball) {
            return 1.25f;
        }
        final Block block = Block.getBlockFromItem(item);
        if (block == Blocks.gravel || block == Blocks.cobblestone
            || block == Blocks.stone
            || block == Blocks.sandstone
            || block == Blocks.wool
            || block == Blocks.netherrack
            || block == Blocks.log
            || block == Blocks.log2) {
            return 1.5f;
        }
        if (block == Blocks.clay || block == Blocks.hardened_clay || block == Blocks.stained_hardened_clay) {
            return 1.25f;
        }
        final ItemData association = GTOreDictUnificator.getAssociation(itemStack);
        final OrePrefixes prefix = association == null ? null : association.mPrefix;
        if (OrePrefixes.stone == prefix || OrePrefixes.stoneBricks == prefix
            || OrePrefixes.stoneChiseled == prefix
            || OrePrefixes.stoneCobble == prefix
            || OrePrefixes.stoneCracked == prefix
            || OrePrefixes.stoneMossy == prefix
            || OrePrefixes.stoneMossyBricks == prefix
            || OrePrefixes.stoneSmooth == prefix
            || OrePrefixes.cobblestone == prefix) {
            return 1.5f;
        }
        return 1f;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ItemStack itemStack = getControllerSlot();
        if (itemStack == null || itemStack.getItem() == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (invalidWindLevel()) return CheckRecipeResultRegistry.NO_RECIPE;

        if (this.mOutputItems == null) this.mOutputItems = new ItemStack[2];

        GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
            .caching(false)
            .items(itemStack)
            .voltage(V[1])
            .find();
        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int parallels = getParallels(this.rotorBlock);
        if (tRecipe.getOutput(0) != null) {
            // Decrease input stack by appropriate amount (Not always 1)
            for (int i = 0; i < parallels; i++) {
                if (!tRecipe.isRecipeInputEqual(true, null, itemStack)) {
                    parallels = i;
                    break;
                }
            }
            this.updateSlots();
            this.mOutputItems[0] = tRecipe.getOutput(0);
            float multiplier = getMultiplier(this.rotorBlock, itemStack);
            int amount = (int) Math.floor(multiplier * (this.mOutputItems[0].stackSize * parallels));

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
        this.mMaxProgresstime = tRecipe.mDuration * 2 * MAX_PARALLELS;
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

    private float currentWind(TileEntityRotorBlock rotorBlock) {
        if (rotorBlock == null) return 0;
        return rotorBlock.getWindStrength();
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        this.tileEntityDispensers.clear();
        this.mDoor = 0;
        this.mHardenedClay = 0;
        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 3, 11, 0, errors)) return;
        if (this.tileEntityDispensers.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_dispenser"));
        }
        if (this.mDoor > 2) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.too_many_doors"));
        }
        checkCasingMin(errors, this.mHardenedClay, 40);
        if (getWindLevel(this.rotorBlock) == windLevel.NON_EXISTENT) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.wind_low"));
        } else if (getWindLevel(this.rotorBlock) == windLevel.TOO_STRONG) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.wind_high"));
        }
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
    public String[] getInfoData() {
        return new String[] {
            StatCollector
                .translateToLocalFormatted("BW.infoData.wind_mill.progress", this.mProgresstime, this.mMaxProgresstime),
            StatCollector
                .translateToLocalFormatted("BW.infoData.wind_mill.grind_power", this.rotorBlock.getGrindPower()) };
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

    private int getParallels(TileEntityRotorBlock rotorBlock) {
        windLevel wind = getWindLevel(rotorBlock);
        if (invalidWindLevel()) return 0;
        return (int) Math.pow(2, wind.ordinal());
    }

    private float getMultiplier(TileEntityRotorBlock rotorBlock, ItemStack itemStack) {
        if (invalidWindLevel()) return 0;
        return multiplierRecipe(itemStack) * rotorMultiplier(rotorBlock);
    }

    private float rotorMultiplier(TileEntityRotorBlock rotorBlock) {
        ItemStack stack = rotorBlock.rotorSlot.get();
        if (stack == null || !(stack.getItem() instanceof ItemStonageRotors rotor)) {
            return 1;
        }
        return rotor.getmRotor();
    }

    private windLevel getWindLevel(TileEntityRotorBlock rotorBlock) {
        float windSpeed = currentWind(rotorBlock);
        return windSpeed < 1f ? windLevel.NON_EXISTENT
            : windSpeed < 10f ? windLevel.PRETTY_LOW
                : windSpeed < 20f ? windLevel.COMMON
                    : windSpeed < 30f ? windLevel.RATHER_STRONG
                        : windSpeed < 50f ? windLevel.VERY_STRONG : windLevel.TOO_STRONG;
    }

    private boolean invalidWindLevel() {
        return getWindLevel(this.rotorBlock) == windLevel.NON_EXISTENT
            || getWindLevel(this.rotorBlock) == windLevel.TOO_STRONG;
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
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEWindmillGui(this);
    }

    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.PRIMITIVE;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}
