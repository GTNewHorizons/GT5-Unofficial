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
import static gregtech.api.GregTechAPI.sBlockCasingsWindmill;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_MACERATOR;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.items.ItemStonageRotors;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.tileentities.classic.TileEntityRotorBlock;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.multiblock.MTEWindmillGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.nei.RecipeDisplayInfo;

public class MTEWindmill extends MTEEnhancedMultiBlockBase<MTEWindmill>
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    /*
     * TODO: improve overclock describer
     * TODO: textures
     * TODO: add block names to lang
     * TODO: WAILA update
     * TODO: clean up class
     */

    private static final int MAX_PARALLELS = 16;

    private TileEntityRotorBlock rotorBlock;
    private int mDoor = 0;
    private int mHardenedClay = 0;
    private int mShaftBlocks = 0;

    private enum windLevel {
        NON_EXISTENT,
        PRETTY_LOW,
        COMMON,
        RATHER_STRONG,
        VERY_STRONG,
        TOO_STRONG
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_LEGACY = "legacy";

    private static final int VERTICAL_OFFSET = 9;
    private static final int HORIZONTAL_OFFSET = 3;
    private static final int DEPTH_OFFSET = 0;
    private static final int MILLSTONE_META = 2;
    private static final int RECIPE_DURATION_MULTI = 4;

    private final OverclockDescriber overclockDescriber;

    private final Set<TileEntityDispenser> tileEntityDispensers = new HashSet<>();

    public MTEWindmill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.overclockDescriber = createOverclockDescriber();
    }

    private MTEWindmill(String aName) {
        super(aName);
        this.overclockDescriber = createOverclockDescriber();
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

    private static final IStructureDefinition<MTEWindmill> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEWindmill>builder()
        // spotless:off
        .addShape(STRUCTURE_PIECE_LEGACY, transpose(new String[][] {
            { "       ", "       ", "       ", "   p   ", "       ", "       ", "       " },
            { "       ", "       ", "  ppp  ", "  p p  ", "  ppp  ", "       ", "       " },
            { "       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
            { " ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
            { " pprpp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
            { " ppppp ", "p     p", "p     p", "p     p", "p     p", "p     p", " ppppp " },
            { "       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
            { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
            { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  cdc  ", "       " },
            { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
            { "       ", "  ccc  ", " c   c ", " c   c ", " c   c ", "  ccc  ", "       " },
            { " bb~bb ", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", "bbbbbbb", " bbbbb " }}))
        .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
            { "       ", "       ", "   p   ", "  ppp  ", "   p   ", "       ", "       " },
            { "       ", "       ", "  ppp  ", "  p p  ", "  ppp  ", "       ", "       " },
            { "       ", "  ppp  ", " p   p ", " p   p ", " p   p ", "  ppp  ", "       " },
            { "       ", " pprpp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
            { "       ", " ppppp ", " p   p ", " p   p ", " p   p ", " ppppp ", "       " },
            { "       ", "  sss  ", " s   s ", " s   s ", " s   s ", "  sss  ", "       " },
            { "       ", "  sss  ", " s   s ", " s   s ", " s   s ", "  sss  ", "       " },
            { "       ", "  sss  ", " s   s ", " s   s ", " s   s ", "  sss  ", "       " },
            { "       ", "  sss  ", " s   s ", " s h s ", " s   s ", "  sss  ", "       " },
            { "  a~a  ", " aaaaa ", "aaaaaaa", "aaaaaaa", "aaaaaaa", " aaaaa ", "  aaa  " }}))
        // spotless:on
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
        .addElement('r', new IStructureElement<>() {

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
        .addElement('a', ofBlock(sBlockCasingsWindmill, 0))
        .addElement('h', ofBlock(sBlockCasingsWindmill, MILLSTONE_META))
        .addElement(
            's',
            ofChain(
                onElementPass(t -> t.mShaftBlocks++, ofBlock(sBlockCasingsWindmill, 1)),
                ofTileAdder(MTEWindmill::addDispenserToOutputSet, sBlockCasingsWindmill, 1),
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
        .build();

    @Override
    public IStructureDefinition<MTEWindmill> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    // TODO: remake/stylize tooltip
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Macerator")
            .addInfo("A primitive Grinder powered by Kinetic energy")
            .addInfo("Speed and output will be affected by wind speed, recipe and rotor")
            .addInfo("Rotor can be put in the Primitive Kinetic Shaftbox")
            .addInfo("Macerates up to 16 items at a time")
            .addInfo("Amount of parallels based on wind speed")
            .addInfo("Processing time is the same regardless of parallels")
            .addInfo("Wind speed can be determined using the Simple Wind Meter")
            .addInfo("========================================================")
            .addInfo("2 parallels: Low")
            .addInfo("4 parallels: Common")
            .addInfo("8 parallels: Rather strong")
            .addInfo("16 parallels: Very Strong")
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

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!this.rotorBlock.rotorSlot.isEmpty()) this.setRotorDamage(this.rotorBlock, this.rotorBlock.getGrindPower());
        return this.rotorBlock.getGrindPower() > 0;
    }

    @Override
    protected @NotNull CheckRecipeResult doCheckRecipe() {
        if (this.rotorBlock.rotorSlot.isEmpty()) return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
        processingLogic.setInputItems(getControllerSlot());
        return switch (getWindLevel(this.rotorBlock)) {
            case NON_EXISTENT -> CheckRecipeResultRegistry.WIND_LOW;
            case TOO_STRONG -> CheckRecipeResultRegistry.WIND_HIGH;
            default -> processingLogic.process();
        };
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setDurationModifier(RECIPE_DURATION_MULTI);
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getParallels);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[1]);
        logic.setAvailableAmperage(MAX_PARALLELS);
        logic.setAmperageOC(false);
        logic.setMaxTierSkips(0);
    }

    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    @Override
    public @Nullable OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    private static OverclockDescriber createOverclockDescriber() {
        return new OverclockDescriber((byte) 1) {

            @Override
            public @NotNull String getTierString() {
                return StatCollector.translateToLocal("GT5U.nei.display.windmill");
            }

            @Override
            public @NotNull OverclockCalculator createCalculator(@NotNull OverclockCalculator template,
                @NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setDurationModifier(RECIPE_DURATION_MULTI);
            }

            @Override
            public void drawEnergyInfo(@NotNull RecipeDisplayInfo recipeInfo) {
                return;
            }
        };
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
                if (stack.stackSize == 0) break;

                for (int i = 0; i < tHatch.getSizeInventory(); i++) {
                    if (stack.stackSize == 0) break;

                    if (tHatch.getStackInSlot(i) == null) {
                        tHatch.setInventorySlotContents(i, stack.copy());
                        stack.stackSize = 0;
                        break;
                    }
                    if (GTUtility.areStacksEqual(tHatch.getStackInSlot(i), stack)
                        && (tHatch.getStackInSlot(i).stackSize < stack.getMaxStackSize())) {
                        int tmp = tHatch.getStackInSlot(i).stackSize + stack.stackSize;
                        stack.stackSize = Math.max(tmp - stack.getMaxStackSize(), 0);
                        tHatch.getStackInSlot(i).stackSize = Math.min(tmp, stack.getMaxStackSize());
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        this.tileEntityDispensers.clear();
        this.mDoor = 0;
        this.mHardenedClay = 0;

        if (this.checkPiece(STRUCTURE_PIECE_LEGACY, 3, 11, 0, null)) {
            checkCasingMin(errors, this.mHardenedClay, 40);
            if (this.tileEntityDispensers.isEmpty()) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_dispenser"));
            }
            if (this.mDoor > 2) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.too_many_doors"));
            }
            return;
        }

        this.tileEntityDispensers.clear();
        this.mDoor = 0;
        this.mHardenedClay = 0;

        if (this.checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) {
            checkCasingMin(errors, this.mShaftBlocks, 40);
            if (this.tileEntityDispensers.isEmpty()) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_dispenser"));
            }
            if (this.mDoor > 2) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.too_many_doors"));
            }
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

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {

        if (facing == side) {
            return new ITexture[] { TextureFactory.of(Textures.BlockIcons.WINDMILL_BASE_CASING),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_STEAM_MACERATOR) // placeholder
                    .extFacing()
                    .build() };
        } else {
            // texture is a placeholder for now
            return new ITexture[] { TextureFactory.of(Textures.BlockIcons.WINDMILL_BASE_CASING) };
        }
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    private int getParallels() {
        windLevel wind = getWindLevel(this.rotorBlock);
        if (invalidWindLevel()) return 0;
        return (int) Math.pow(2, wind.ordinal());
    }

    private float currentWind(TileEntityRotorBlock rotorBlock) {
        if (rotorBlock == null) return 0;
        return rotorBlock.getWindStrength();
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

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.PRIMITIVE;
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

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean canBeMuffled() {
        return false;
    }

}
