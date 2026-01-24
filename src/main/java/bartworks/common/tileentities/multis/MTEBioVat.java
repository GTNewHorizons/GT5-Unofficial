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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import bartworks.API.SideReference;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.configs.Configuration;
import bartworks.common.items.ItemLabParts;
import bartworks.common.loaders.FluidLoader;
import bartworks.common.net.PacketBioVatRenderer;
import bartworks.common.tileentities.tiered.MTERadioHatch;
import bartworks.util.BWUtil;
import bartworks.util.BioCulture;
import bartworks.util.Coords;
import bartworks.util.MathUtils;
import bartworks.util.ResultWrongSievert;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.recipe.Sievert;
import gregtech.common.misc.GTStructureChannels;

public class MTEBioVat extends MTEEnhancedMultiBlockBase<MTEBioVat> implements ISurvivalConstructable {

    public static final HashMap<Coords, Integer> staticColorMap = new HashMap<>();

    private static final byte TIMERDIVIDER = 20;

    private final HashSet<EntityPlayerMP> playerMPHashSet = new HashSet<>();
    private final ArrayList<MTERadioHatch> mRadHatches = new ArrayList<>();
    private int height = 1;
    private Fluid mFluid = FluidRegistry.LAVA;
    private BioCulture mCulture;
    private ItemStack mStack;
    private boolean needsVisualUpdate = true;
    private int glassTier = -1;
    private int mSievert;
    private int mNeededSievert;
    private int mCasing = 0;
    private int mExpectedMultiplier = 0;
    private int mTimes = 0;
    private boolean isVisibleFluid = false;
    private final Sievert defaultSievertData = new Sievert(0, false);

    public MTEBioVat(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBioVat(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEBioVat> STRUCTURE_DEFINITION = StructureDefinition.<MTEBioVat>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                    { "ggggg", "gaaag", "gaaag", "gaaag", "ggggg" }, { "ggggg", "gaaag", "gaaag", "gaaag", "ggggg" },
                    { "cc~cc", "ccccc", "ccccc", "ccccc", "ccccc" }, }))
        .addElement(
            'c',
            ofChain(
                buildHatchAdder(MTEBioVat.class)
                    .atLeast(
                        Maintenance,
                        InputBus,
                        OutputBus,
                        InputHatch,
                        OutputHatch,
                        Energy,
                        RadioHatchElement.RadioHatch)
                    .hint(1)
                    .casingIndex(CASING_INDEX)
                    .build(),
                onElementPass(e -> e.mCasing++, ofBlock(GregTechAPI.sBlockCasings4, 1))))
        .addElement('a', ofChain(isAir(), ofBlockAnyMeta(FluidLoader.bioFluidBlock)))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .build();

    @Override
    public IStructureDefinition<MTEBioVat> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Bacterial Vat, Bac Vat")
            .addInfo(EnumChatFormatting.AQUA + "Advanced Bio Processing")
            .addSeparator()
            .addInfo(
                "Some recipes require " + EnumChatFormatting.GREEN
                    + "R"
                    + EnumChatFormatting.DARK_GREEN
                    + "A"
                    + EnumChatFormatting.GREEN
                    + "D"
                    + EnumChatFormatting.DARK_GREEN
                    + "I"
                    + EnumChatFormatting.GREEN
                    + "A"
                    + EnumChatFormatting.DARK_GREEN
                    + "T"
                    + EnumChatFormatting.GREEN
                    + "I"
                    + EnumChatFormatting.DARK_GREEN
                    + "O"
                    + EnumChatFormatting.GREEN
                    + "N"
                    + EnumChatFormatting.GRAY
                    + " supplied with a "
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.GREEN
                    + "Radio Hatch")
            .addInfo("Radiation can be either a minimum requirement or an exact value")
            .addInfo("Efficiency depends on Output Hatch fluid level")
            .addInfo("Efficiency peaks at " + EnumChatFormatting.LIGHT_PURPLE + "50%")
            .beginStructureBlock(5, 4, 5, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Clean Stainless Steel Casings", 19, false)
            .addOtherStructurePart(
                StatCollector.translateToLocal("tooltip.bw.structure.glass"),
                "Hollow two middle layers",
                2)
            .addCasingInfoExactly("Any Tiered Glass", 32, true)
            .addStructureInfo("Some Recipes need more advanced Glass Types")
            .addMaintenanceHatch("Any casing", 1)
            .addOtherStructurePart(StatCollector.translateToLocal("tooltip.bw.structure.radio_hatch"), "Any casing", 1)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addInputHatch("Any casing", 1)
            .addOutputHatch("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    private int getInputCapacity() {
        return this.mInputHatches.stream()
            .mapToInt(MTEHatchInput::getCapacity)
            .sum();
    }

    private int getOutputCapacity() {
        return this.mOutputHatches.stream()
            .mapToInt(MTEHatchOutput::getCapacity)
            .sum();
    }

    @Override
    public int getCapacity() {
        int ret = 0;
        ret += this.getInputCapacity();
        // ret += getOutputCapacity();
        return ret;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return super.fill(resource, doFill);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.bacterialVatRecipes;
    }

    /**
     * Calculates the expected output multiplier based on the output hatch
     *
     * @param recipeFluidOutput the recipe fluid output
     * @param needEqual         if the recipeFluidOutput should be equal to the fluid in the output hatch
     * @return the expected output multiplier
     */
    private int getExpectedMultiplier(@Nullable FluidStack recipeFluidOutput, boolean needEqual) {
        FluidStack storedFluidOutputs = this.getStoredFluidOutputs();
        if (storedFluidOutputs == null) return 1;
        if (!needEqual || storedFluidOutputs.isFluidEqual(recipeFluidOutput)) {
            return this.calcMod(storedFluidOutputs.amount) + 1;
        }
        return 1;
    }

    private int calcMod(double x) {
        double y = this.getOutputCapacity() / 2D, z = Configuration.Multiblocks.bioVatMaxParallelBonus;

        int ret = MathUtils.ceilInt((-1D / y * (x - y) * (x - y) + y) / y * z);
        return MathUtils.clamp(1, ret, Configuration.Multiblocks.bioVatMaxParallelBonus);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                Sievert data = recipe.getMetadataOrDefault(GTRecipeConstants.SIEVERT, defaultSievertData);
                int sievert = data.sievert;
                boolean isExact = data.isExact;
                int glass = recipe.getMetadataOrDefault(GLASS, 0);
                if (!BWUtil.areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, MTEBioVat.this.getControllerSlot()))
                    return CheckRecipeResultRegistry.NO_RECIPE;
                MTEBioVat.this.mNeededSievert = sievert;

                if (MTEBioVat.this.glassTier < glass) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(glass);
                }

                if (!isExact) {
                    if (MTEBioVat.this.mSievert < MTEBioVat.this.mNeededSievert) {
                        return ResultWrongSievert.insufficientSievert(MTEBioVat.this.mNeededSievert);
                    }
                } else if (MTEBioVat.this.mSievert != sievert) {
                    return ResultWrongSievert.wrongSievert(sievert);
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                return super.createParallelHelper(recipeWithMultiplier(recipe, inputFluids));
            }
        };
    }

    protected GTRecipe recipeWithMultiplier(GTRecipe recipe, FluidStack[] fluidInputs) {
        GTRecipe tRecipe = recipe.copy();
        int multiplier = getExpectedMultiplier(recipe.getFluidOutput(0), true);
        mExpectedMultiplier = multiplier;
        // Calculate max multiplier limited by input fluids
        long fluidAmount = 0;
        for (FluidStack fluid : fluidInputs) {
            if (recipe.mFluidInputs[0].isFluidEqual(fluid)) {
                fluidAmount += fluid.amount;
            }
        }
        multiplier = (int) Math.min(multiplier, fluidAmount / recipe.mFluidInputs[0].amount);
        // In case multiplier is 0
        multiplier = Math.max(multiplier, 1);
        mTimes = multiplier;
        tRecipe.mFluidInputs[0].amount *= multiplier;
        tRecipe.mFluidOutputs[0].amount *= multiplier;
        return tRecipe;
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpecialSlotItem(this.getControllerSlot());
    }

    public FluidStack getStoredFluidOutputs() {
        // Only one output Hatch, enforced in checkMachine.
        return this.mOutputHatches.get(0)
            .getFluid();
    }

    private boolean addRadiationInputToMachineList(IGregTechTileEntity aTileEntity, int CasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTERadioHatch radioHatch)) {
            return false;
        } else {
            radioHatch.updateTexture(CasingIndex);
            return this.mRadHatches.add(radioHatch);
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        this.mRadHatches.clear();
        this.glassTier = -1;
        this.mCasing = 0;

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 2, 3, 0)) return false;

        return this.mCasing >= 19 && this.mRadHatches.size() <= 1
            && !this.mEnergyHatches.isEmpty()
            && this.mMaintenanceHatches.size() == 1
            && this.mOutputHatches.size() == 1;
    }

    private void sendAllRequiredRendererPackets(int offsetX_L, int offsetY_L, int offsetZ_L, int offsetX_U,
        int offsetY_U, int offsetZ_U) {
        int height = this.reCalculateHeight();
        if (this.mFluid != null && height > 1 && this.reCalculateFluidAmmount() > 0) {
            for (int x = offsetX_L; x <= offsetX_U; x++) {
                for (int y = offsetY_L; y <= offsetY_U; y++) {
                    for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                        this.sendPackagesOrRenewRenderer(x, y, z, this.mCulture);
                    }
                }
            }
        }
    }

    private void sendPackagesOrRenewRenderer(int x, int y, int z, BioCulture lCulture) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        int xDir = this.getXDir();
        int zDir = this.getZDir();

        MTEBioVat.staticColorMap.remove(
            new Coords(
                xDir + x + aBaseMetaTileEntity.getXCoord(),
                y + aBaseMetaTileEntity.getYCoord(),
                zDir + z + aBaseMetaTileEntity.getZCoord(),
                aBaseMetaTileEntity.getWorld().provider.dimensionId));
        MTEBioVat.staticColorMap.put(
            new Coords(
                xDir + x + aBaseMetaTileEntity.getXCoord(),
                y + aBaseMetaTileEntity.getYCoord(),
                zDir + z + aBaseMetaTileEntity.getZCoord(),
                aBaseMetaTileEntity.getWorld().provider.dimensionId),
            lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB());

        if (SideReference.Side.Server) {
            GTValues.NW.sendPacketToAllPlayersInRange(
                aBaseMetaTileEntity.getWorld(),
                new PacketBioVatRenderer(
                    new Coords(
                        xDir + x + aBaseMetaTileEntity.getXCoord(),
                        y + aBaseMetaTileEntity.getYCoord(),
                        zDir + z + aBaseMetaTileEntity.getZCoord(),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId),
                    lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                    true),
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getZCoord());
            GTValues.NW.sendPacketToAllPlayersInRange(
                aBaseMetaTileEntity.getWorld(),
                new PacketBioVatRenderer(
                    new Coords(
                        xDir + x + aBaseMetaTileEntity.getXCoord(),
                        y + aBaseMetaTileEntity.getYCoord(),
                        zDir + z + aBaseMetaTileEntity.getZCoord(),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId),
                    lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                    false),
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getZCoord());
        }
        this.needsVisualUpdate = true;
    }

    private void check_Chunk(int offsetX_L, int offsetY_L, int offsetZ_L, int offsetX_U, int offsetY_U, int offsetZ_U) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        World aWorld = aBaseMetaTileEntity.getWorld();
        if (!aWorld.isRemote) {
            for (Object tObject : aWorld.playerEntities) {
                if (!(tObject instanceof EntityPlayerMP tPlayer)) {
                    break;
                }
                Chunk tChunk = aWorld
                    .getChunkFromBlockCoords(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getZCoord());
                if (tPlayer.getServerForPlayer()
                    .getPlayerManager()
                    .isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    if (!this.playerMPHashSet.contains(tPlayer)) {
                        this.playerMPHashSet.add(tPlayer);
                        this.sendAllRequiredRendererPackets(
                            offsetX_L,
                            offsetY_L,
                            offsetZ_L,
                            offsetX_U,
                            offsetY_U,
                            offsetZ_U);
                    }
                } else {
                    this.playerMPHashSet.remove(tPlayer);
                }
            }
        }
    }

    private void placeFluid(int xDir, int zDir, int offsetX_L, int offsetY_L, int offsetZ_L, int offsetX_U,
        int offsetY_U, int offsetZ_U) {

        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        this.isVisibleFluid = true;
        this.height = this.reCalculateHeight();
        if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0) {
            for (int x = offsetX_L; x <= offsetX_U; x++) {
                for (int y = offsetY_L; y <= offsetY_U; y++) {
                    for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                        if (aBaseMetaTileEntity.getWorld()
                            .getBlock(
                                xDir + x + aBaseMetaTileEntity.getXCoord(),
                                y + aBaseMetaTileEntity.getYCoord(),
                                zDir + z + aBaseMetaTileEntity.getZCoord())
                            .equals(Blocks.air))
                            aBaseMetaTileEntity.getWorld()
                                .setBlock(
                                    xDir + x + aBaseMetaTileEntity.getXCoord(),
                                    y + aBaseMetaTileEntity.getYCoord(),
                                    zDir + z + aBaseMetaTileEntity.getZCoord(),
                                    FluidLoader.bioFluidBlock);
                    }
                }
            }
        }
    }

    private void removeFluid(int xDir, int zDir, int offsetX_L, int offsetY_L, int offsetZ_L, int offsetX_U,
        int offsetY_U, int offsetZ_U) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        this.isVisibleFluid = false;

        for (int x = offsetX_L; x <= offsetX_U; x++) {
            for (int y = offsetY_L; y <= offsetY_U; y++) {
                for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                    if (aBaseMetaTileEntity.getWorld()
                        .getBlock(
                            xDir + x + aBaseMetaTileEntity.getXCoord(),
                            y + aBaseMetaTileEntity.getYCoord(),
                            zDir + z + aBaseMetaTileEntity.getZCoord())
                        .equals(FluidLoader.bioFluidBlock))
                        aBaseMetaTileEntity.getWorld()
                            .setBlockToAir(
                                xDir + x + aBaseMetaTileEntity.getXCoord(),
                                y + aBaseMetaTileEntity.getYCoord(),
                                zDir + z + aBaseMetaTileEntity.getZCoord());
                    MTEBioVat.staticColorMap.remove(
                        new Coords(
                            xDir + x + aBaseMetaTileEntity.getXCoord(),
                            y + aBaseMetaTileEntity.getYCoord(),
                            zDir + z + aBaseMetaTileEntity.getZCoord()),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId);
                }
            }
        }
    }

    private int reCalculateFluidAmmount() {
        return this.getStoredFluids()
            .stream()
            .mapToInt(fluidStack -> fluidStack.amount)
            .sum();
    }

    private int reCalculateHeight() {
        return this.reCalculateFluidAmmount() > this.getCapacity() / 4 - 1
            ? this.reCalculateFluidAmmount() >= this.getCapacity() / 2 ? 3 : 2
            : 1;
    }

    public void doAllVisualThings() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // Set the bounds of the render section
        ExtendedFacing f = getExtendedFacing();
        Vec3Impl nearCornerOffset = f.getWorldOffset(new Vec3Impl(1, -1, -1));
        Vec3Impl farCornerOffset = f.getWorldOffset(new Vec3Impl(-1, -2, 1));
        int offsetX_L = Math.min(nearCornerOffset.get0(), farCornerOffset.get0());
        int offsetY_L = Math.min(nearCornerOffset.get1(), farCornerOffset.get1());
        int offsetZ_L = Math.min(nearCornerOffset.get2(), farCornerOffset.get2());
        int offsetX_U = Math.max(nearCornerOffset.get0(), farCornerOffset.get0());
        int offsetY_U = Math.max(nearCornerOffset.get1(), farCornerOffset.get1());
        int offsetZ_U = Math.max(nearCornerOffset.get2(), farCornerOffset.get2());

        int xDir = this.getXDir();
        int zDir = this.getZDir();

        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mMachine) {
                ItemStack aStack = this.mInventory[1];
                BioCulture lCulture = null;

                if (aBaseMetaTileEntity.getTimer() % 200 == 0) {
                    this.check_Chunk(offsetX_L, offsetY_L, offsetZ_L, offsetX_U, offsetY_U, offsetZ_U);
                }

                if (this.needsVisualUpdate && aBaseMetaTileEntity.getTimer() % MTEBioVat.TIMERDIVIDER == 0) {
                    for (int x = offsetX_L; x <= offsetX_U; x++) {
                        for (int y = offsetY_L; y <= offsetY_U; y++) {
                            for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                                aBaseMetaTileEntity.getWorld()
                                    .setBlockToAir(
                                        xDir + x + aBaseMetaTileEntity.getXCoord(),
                                        y + aBaseMetaTileEntity.getYCoord(),
                                        zDir + z + aBaseMetaTileEntity.getZCoord());
                            }
                        }
                    }
                }

                this.height = this.reCalculateHeight();
                if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0) {
                    if (!BWUtil.areStacksEqualOrNull(aStack, this.mStack)
                        || this.needsVisualUpdate && aBaseMetaTileEntity.getTimer() % MTEBioVat.TIMERDIVIDER == 1) {
                        for (int x = offsetX_L; x <= offsetX_U; x++) {
                            for (int y = offsetY_L; y <= offsetY_U; y++) {
                                for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                                    if (aStack == null
                                        || aStack.getItem() instanceof ItemLabParts && aStack.getItemDamage() == 0) {
                                        if (this.mCulture == null || aStack == null
                                            || aStack.getTagCompound() == null
                                            || this.mCulture.getID() != aStack.getTagCompound()
                                                .getInteger("ID")) {
                                            lCulture = aStack == null || aStack.getTagCompound() == null ? null
                                                : BioCulture.getBioCulture(
                                                    aStack.getTagCompound()
                                                        .getString("Name"));
                                            this.sendPackagesOrRenewRenderer(x, y, z, lCulture);
                                        }
                                    }
                                }
                            }
                        }
                        this.mStack = aStack;
                        this.mCulture = lCulture;
                    }
                    if (this.needsVisualUpdate && aBaseMetaTileEntity.getTimer() % MTEBioVat.TIMERDIVIDER == 1) {
                        if (aBaseMetaTileEntity.isClientSide()) new Throwable().printStackTrace();
                        this.placeFluid(xDir, zDir, offsetX_L, offsetY_L, offsetZ_L, offsetX_U, offsetY_U, offsetZ_U);
                        this.needsVisualUpdate = false;
                    }
                }
            } else {
                this.onRemoval();
            }
        }
    }

    @Override
    public void onRemoval() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        ExtendedFacing f = getExtendedFacing();
        Vec3Impl nearCornerOffset = f.getWorldOffset(new Vec3Impl(1, -1, -1));
        Vec3Impl farCornerOffset = f.getWorldOffset(new Vec3Impl(-1, -2, 1));
        int offsetX_L = Math.min(nearCornerOffset.get0(), farCornerOffset.get0());
        int offsetY_L = Math.min(nearCornerOffset.get1(), farCornerOffset.get1());
        int offsetZ_L = Math.min(nearCornerOffset.get2(), farCornerOffset.get2());
        int offsetX_U = Math.max(nearCornerOffset.get0(), farCornerOffset.get0());
        int offsetY_U = Math.max(nearCornerOffset.get1(), farCornerOffset.get1());
        int offsetZ_U = Math.max(nearCornerOffset.get2(), farCornerOffset.get2());

        int xDir = this.getXDir();
        int zDir = this.getZDir();

        if (this.isVisibleFluid) {
            removeFluid(xDir, zDir, offsetX_L, offsetY_L, offsetZ_L, offsetX_U, offsetY_U, offsetZ_U);
            sendRenderPackets(xDir, zDir, offsetX_L, offsetY_L, offsetZ_L, offsetX_U, offsetY_U, offsetZ_U);
        } else if (aBaseMetaTileEntity.getWorld()
            .getWorldTime() % 20 == 7) {
                sendRenderPackets(xDir, zDir, offsetX_L, offsetY_L, offsetZ_L, offsetX_U, offsetY_U, offsetZ_U);
            }

        super.onRemoval();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.height != this.reCalculateHeight()) this.needsVisualUpdate = true;
        this.doAllVisualThings();
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mRadHatches.size() == 1) {
                this.mSievert = this.mRadHatches.get(0)
                    .getSievert();
            } else {
                this.mSievert = 0;
            }
            if (aBaseMetaTileEntity.isActive() && this.mNeededSievert > this.mSievert) this.mOutputFluids = null;
            if (this.mMaxProgresstime <= 0) {
                this.mTimes = 0;
                this.mMaxProgresstime = 0;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mFluidHeight", this.height);
        if (this.mCulture != null && !this.mCulture.getName()
            .isEmpty()) aNBT.setString("mCulture", this.mCulture.getName());
        else if ((this.mCulture == null || this.mCulture.getName()
            .isEmpty()) && !aNBT.getString("mCulture")
                .isEmpty()) {
                    aNBT.removeTag("mCulture");
                }
        if (this.mFluid != null) aNBT.setString("mFluid", this.mFluid.getName());
        aNBT.setInteger("mSievert", this.mSievert);
        aNBT.setInteger("mNeededSievert", this.mNeededSievert);
        aNBT.setBoolean("isVisibleFluid", this.isVisibleFluid);
        super.saveNBTData(aNBT);
    }

    private int getXDir() {
        return this.getBaseMetaTileEntity()
            .getBackFacing().offsetX * 2;
    }

    private int getZDir() {
        return this.getBaseMetaTileEntity()
            .getBackFacing().offsetZ * 2;
    }

    private void sendRenderPackets(int xDir, int zDir, int offsetX_L, int offsetY_L, int offsetZ_L, int offsetX_U,
        int offsetY_U, int offsetZ_U) {
        if (SideReference.Side.Server) {
            IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
            for (int x = offsetX_L; x <= offsetX_U; x++) {
                for (int y = offsetY_L; y <= offsetY_U; y++) {
                    for (int z = offsetZ_L; z <= offsetZ_U; z++) {
                        GTValues.NW.sendPacketToAllPlayersInRange(
                            aBaseMetaTileEntity.getWorld(),
                            new PacketBioVatRenderer(
                                new Coords(
                                    xDir + x + aBaseMetaTileEntity.getXCoord(),
                                    y + aBaseMetaTileEntity.getYCoord(),
                                    zDir + z + aBaseMetaTileEntity.getZCoord(),
                                    aBaseMetaTileEntity.getWorld().provider.dimensionId),
                                this.mCulture == null ? BioCulture.NULLCULTURE.getColorRGB()
                                    : this.mCulture.getColorRGB(),
                                true),
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getZCoord());
                    }
                }
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.height = aNBT.getInteger("mFluidHeight");
        this.mCulture = BioCulture.getBioCulture(aNBT.getString("mCulture"));
        if (!aNBT.getString("mFluid")
            .isEmpty()) this.mFluid = FluidRegistry.getFluid(aNBT.getString("mFluid"));
        this.mSievert = aNBT.getInteger("mSievert");
        this.mNeededSievert = aNBT.getInteger("mNeededSievert");
        super.loadNBTData(aNBT);
        this.isVisibleFluid = aNBT.getBoolean("isVisibleFluid");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEBioVat(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(StatCollector.translateToLocal("BW.infoData.BioVat.expectedProduction") + ": "
            + EnumChatFormatting.GREEN
            + (this.mMachine
            ? (this.mMaxProgresstime <= 0 ? this.getExpectedMultiplier(null, false) : this.mExpectedMultiplier)
            * 100
            : -1)
            + EnumChatFormatting.RESET
            + " %");

        info.add(StatCollector.translateToLocal("BW.infoData.BioVat.production") + ": "
            + EnumChatFormatting.GREEN
            + (this.mMaxProgresstime <= 0 ? 0 : this.mTimes) * 100
            + EnumChatFormatting.RESET
            + " %");
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    private enum RadioHatchElement implements IHatchElement<MTEBioVat> {

        RadioHatch(MTEBioVat::addRadiationInputToMachineList, MTERadioHatch.class) {

            @Override
            public long count(MTEBioVat mteBioVat) {
                return mteBioVat.mRadHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEBioVat> adder;

        @SafeVarargs
        RadioHatchElement(IGTHatchAdder<MTEBioVat> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEBioVat> adder() {
            return adder;
        }
    }
}
