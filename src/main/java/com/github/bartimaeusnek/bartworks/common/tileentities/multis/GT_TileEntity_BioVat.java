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
import static com.github.bartimaeusnek.bartworks.util.BW_Util.ofGlassTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.net.RendererPacket;
import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import com.github.bartimaeusnek.bartworks.util.ResultWrongSievert;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_TileEntity_BioVat extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_BioVat> {

    public static final HashMap<Coords, Integer> staticColorMap = new HashMap<>();

    private static final byte TIMERDIVIDER = 20;

    private final HashSet<EntityPlayerMP> playerMPHashSet = new HashSet<>();
    private final ArrayList<GT_MetaTileEntity_RadioHatch> mRadHatches = new ArrayList<>();
    private int height = 1;
    private Fluid mFluid = FluidRegistry.LAVA;
    private BioCulture mCulture;
    private ItemStack mStack;
    private boolean needsVisualUpdate = true;
    private byte mGlassTier;
    private int mSievert;
    private int mNeededSievert;
    private int mCasing = 0;
    private int mExpectedMultiplier = 0;
    private int mTimes = 0;
    private boolean isVisibleFluid = false;

    public GT_TileEntity_BioVat(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_BioVat(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TileEntity_BioVat> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_TileEntity_BioVat>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                            new String[][] { { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                                    { "ggggg", "gaaag", "gaaag", "gaaag", "ggggg" },
                                    { "ggggg", "gaaag", "gaaag", "gaaag", "ggggg" },
                                    { "cc~cc", "ccccc", "ccccc", "ccccc", "ccccc" }, }))
            .addElement(
                    'c',
                    ofChain(
                            ofHatchAdder(GT_TileEntity_BioVat::addMaintenanceToMachineList, CASING_INDEX, 1),
                            ofHatchAdder(GT_TileEntity_BioVat::addOutputToMachineList, CASING_INDEX, 1),
                            ofHatchAdder(GT_TileEntity_BioVat::addInputToMachineList, CASING_INDEX, 1),
                            ofHatchAdder(GT_TileEntity_BioVat::addRadiationInputToMachineList, CASING_INDEX, 1),
                            ofHatchAdder(GT_TileEntity_BioVat::addEnergyInputToMachineList, CASING_INDEX, 1),
                            onElementPass(e -> e.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1))))
            .addElement('a', ofChain(isAir(), ofBlockAnyMeta(FluidLoader.bioFluidBlock)))
            .addElement(
                    'g',
                    ofGlassTiered((byte) 1, (byte) 127, (byte) 0, (te, v) -> te.mGlassTier = v, te -> te.mGlassTier, 1))
            .build();

    @Override
    public IStructureDefinition<GT_TileEntity_BioVat> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Bacterial Vat").addInfo("Controller block for the Bacterial Vat")
                .addInfo("For maximum efficiency boost keep the Output Hatch always half filled!").addSeparator()
                .beginStructureBlock(5, 4, 5, false).addController("Front bottom center")
                .addCasingInfoMin("Clean Stainless Steel Casings", 19, false)
                .addOtherStructurePart("Glass", "Hollow two middle layers", 2)
                .addStructureInfo("The glass can be any glass, i.e. Tinkers Construct Clear Glass")
                .addStructureInfo("Some Recipes need more advanced Glass Types").addMaintenanceHatch("Any casing", 1)
                .addOtherStructurePart("Radio Hatch", "Any casing", 1).addInputBus("Any casing", 1)
                .addOutputBus("Any casing", 1).addInputHatch("Any casing", 1).addOutputHatch("Any casing", 1)
                .addEnergyHatch("Any casing", 1).toolTipFinisher(MULTIBLOCK_ADDED_BY_BARTWORKS);
        return tt;
    }

    public static int[] specialValueUnpack(int aSpecialValue) {
        int[] ret = new int[4];
        ret[0] = aSpecialValue & 0xF; // = glass tier
        ret[1] = aSpecialValue >>> 4 & 0b11; // = special value
        ret[2] = aSpecialValue >>> 6 & 0b1; // boolean exact svt | 1 = true | 0 = false
        ret[3] = aSpecialValue >>> 7 & Integer.MAX_VALUE; // = sievert
        return ret;
    }

    private int getInputCapacity() {
        return this.mInputHatches.stream().mapToInt(GT_MetaTileEntity_Hatch_Input::getCapacity).sum();
    }

    private int getOutputCapacity() {
        return this.mOutputHatches.stream().mapToInt(GT_MetaTileEntity_Hatch_Output::getCapacity).sum();
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
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
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
        double y = this.getOutputCapacity() / 2D, z = ConfigHandler.bioVatMaxParallelBonus;

        int ret = MathUtils.ceilInt((-1D / y * Math.pow(x - y, 2D) + y) / y * z);
        return MathUtils.clamp(1, ret, ConfigHandler.bioVatMaxParallelBonus);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (!BW_Util.areStacksEqualOrNull(
                        (ItemStack) recipe.mSpecialItems,
                        GT_TileEntity_BioVat.this.getControllerSlot()))
                    return CheckRecipeResultRegistry.NO_RECIPE;
                int[] conditions = GT_TileEntity_BioVat.specialValueUnpack(recipe.mSpecialValue);
                GT_TileEntity_BioVat.this.mNeededSievert = conditions[3];

                if (GT_TileEntity_BioVat.this.mGlassTier < conditions[0]) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(conditions[0]);
                }

                if (conditions[2] == 0) {
                    if (GT_TileEntity_BioVat.this.mSievert < GT_TileEntity_BioVat.this.mNeededSievert) {
                        return ResultWrongSievert.insufficientSievert(GT_TileEntity_BioVat.this.mNeededSievert);
                    }
                } else if (GT_TileEntity_BioVat.this.mSievert != conditions[3]) {
                    return ResultWrongSievert.wrongSievert(conditions[3]);
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (!result.wasSuccessful()) {
                    return result;
                }
                // We already made sure the recipe runs. Now the vat looks for as many "parallels" as it can do
                GT_TileEntity_BioVat.this.mExpectedMultiplier = GT_TileEntity_BioVat.this
                        .getExpectedMultiplier(this.lastRecipe.getFluidOutput(0), true);
                GT_TileEntity_BioVat.this.mTimes = 1;
                for (int i = 1; i < GT_TileEntity_BioVat.this.mExpectedMultiplier; i++) {
                    if (GT_TileEntity_BioVat.this.depleteInput(this.lastRecipe.mFluidInputs[0])) {
                        GT_TileEntity_BioVat.this.mTimes++;
                    }
                }
                this.outputFluids[0].amount *= GT_TileEntity_BioVat.this.mTimes;
                return result;
            }
        };
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setSpecialSlotItem(this.getControllerSlot());
    }

    public FluidStack getStoredFluidOutputs() {
        // Only one output Hatch
        assert this.mOutputHatches.size() == 1;
        return this.mOutputHatches.get(0).getFluid();
    }

    private boolean addRadiationInputToMachineList(IGregTechTileEntity aTileEntity, int CasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null || !(aMetaTileEntity instanceof GT_MetaTileEntity_RadioHatch)) {
            return false;
        } else {
            ((GT_MetaTileEntity_RadioHatch) aMetaTileEntity).updateTexture(CasingIndex);
            return this.mRadHatches.add((GT_MetaTileEntity_RadioHatch) aMetaTileEntity);
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        this.mRadHatches.clear();
        this.mGlassTier = 0;
        this.mCasing = 0;

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 2, 3, 0)) return false;

        return this.mCasing >= 19 && this.mRadHatches.size() <= 1
                && this.mOutputHatches.size() == 1
                && this.mMaintenanceHatches.size() == 1
                && this.mInputHatches.size() > 0
                && this.mEnergyHatches.size() > 0;
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

    private void sendAllRequiredRendererPackets() {
        int height = this.reCalculateHeight();
        if (this.mFluid != null && height > 1 && this.reCalculateFluidAmmount() > 0) {
            for (int x = -1; x < 2; x++) for (int y = 1; y < height; y++)
                for (int z = -1; z < 2; z++) this.sendPackagesOrRenewRenderer(x, y, z, this.mCulture);
        }
    }

    private void sendPackagesOrRenewRenderer(int x, int y, int z, BioCulture lCulture) {
        int xDir = this.getXDir();
        int zDir = this.getZDir();

        GT_TileEntity_BioVat.staticColorMap.remove(
                new Coords(
                        xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                        y + this.getBaseMetaTileEntity().getYCoord(),
                        zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                        this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
        GT_TileEntity_BioVat.staticColorMap.put(
                new Coords(
                        xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                        y + this.getBaseMetaTileEntity().getYCoord(),
                        zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                        this.getBaseMetaTileEntity().getWorld().provider.dimensionId),
                lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB());

        if (SideReference.Side.Server) {
            MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                    this.getBaseMetaTileEntity().getWorld(),
                    new RendererPacket(
                            new Coords(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                    this.getBaseMetaTileEntity().getWorld().provider.dimensionId),
                            lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                            true),
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getZCoord());
            MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                    this.getBaseMetaTileEntity().getWorld(),
                    new RendererPacket(
                            new Coords(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                    this.getBaseMetaTileEntity().getWorld().provider.dimensionId),
                            lCulture == null ? BioCulture.NULLCULTURE.getColorRGB() : lCulture.getColorRGB(),
                            false),
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getZCoord());
        }
        this.needsVisualUpdate = true;
    }

    private void check_Chunk() {
        World aWorld = this.getBaseMetaTileEntity().getWorld();
        if (!aWorld.isRemote) {

            for (Object tObject : aWorld.playerEntities) {
                if (!(tObject instanceof EntityPlayerMP tPlayer)) {
                    break;
                }
                Chunk tChunk = aWorld.getChunkFromBlockCoords(
                        this.getBaseMetaTileEntity().getXCoord(),
                        this.getBaseMetaTileEntity().getZCoord());
                if (tPlayer.getServerForPlayer().getPlayerManager()
                        .isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
                    if (!this.playerMPHashSet.contains(tPlayer)) {
                        this.playerMPHashSet.add(tPlayer);
                        this.sendAllRequiredRendererPackets();
                    }
                } else {
                    this.playerMPHashSet.remove(tPlayer);
                }
            }
        }
    }

    private void placeFluid() {
        this.isVisibleFluid = true;
        int xDir = this.getXDir();
        int zDir = this.getZDir();
        this.height = this.reCalculateHeight();
        if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0) for (int x = -1; x < 2; x++) {
            for (int y = 0; y < this.height; y++) {
                for (int z = -1; z < 2; z++) {
                    if (this.getBaseMetaTileEntity().getWorld()
                            .getBlock(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord())
                            .equals(Blocks.air))
                        this.getBaseMetaTileEntity().getWorld().setBlock(
                                xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                y + this.getBaseMetaTileEntity().getYCoord(),
                                zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                FluidLoader.bioFluidBlock);
                }
            }
        }
    }

    private void removeFluid(int xDir, int zDir) {
        this.isVisibleFluid = false;

        for (int x = -1; x < 2; x++) {
            for (int y = 1; y < 3; y++) {
                for (int z = -1; z < 2; z++) {
                    if (this.getBaseMetaTileEntity().getWorld()
                            .getBlock(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord())
                            .equals(FluidLoader.bioFluidBlock))
                        this.getBaseMetaTileEntity().getWorld().setBlockToAir(
                                xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                y + this.getBaseMetaTileEntity().getYCoord(),
                                zDir + z + this.getBaseMetaTileEntity().getZCoord());
                    GT_TileEntity_BioVat.staticColorMap.remove(
                            new Coords(
                                    xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                    y + this.getBaseMetaTileEntity().getYCoord(),
                                    zDir + z + this.getBaseMetaTileEntity().getZCoord()),
                            this.getBaseMetaTileEntity().getWorld().provider.dimensionId);
                }
            }
        }
    }

    private int reCalculateFluidAmmount() {
        return this.getStoredFluids().stream().mapToInt(fluidStack -> fluidStack.amount).sum();
    }

    private int reCalculateHeight() {
        return this.reCalculateFluidAmmount() > this.getCapacity() / 4 - 1
                ? this.reCalculateFluidAmmount() >= this.getCapacity() / 2 ? 3 : 2
                : 1;
    }

    public void doAllVisualThings() {
        if (this.getBaseMetaTileEntity().isServerSide()) {
            if (this.mMachine) {
                ItemStack aStack = this.mInventory[1];
                BioCulture lCulture = null;
                int xDir = this.getXDir();
                int zDir = this.getZDir();

                if (this.getBaseMetaTileEntity().getTimer() % 200 == 0) {
                    this.check_Chunk();
                }

                if (this.needsVisualUpdate
                        && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 0) {
                    for (int x = -1; x < 2; x++) for (int y = 1; y < 3; y++)
                        for (int z = -1; z < 2; z++) this.getBaseMetaTileEntity().getWorld().setBlockToAir(
                                xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                y + this.getBaseMetaTileEntity().getYCoord(),
                                zDir + z + this.getBaseMetaTileEntity().getZCoord());
                }

                this.height = this.reCalculateHeight();
                if (this.mFluid != null && this.height > 1 && this.reCalculateFluidAmmount() > 0) {
                    if (!BW_Util.areStacksEqualOrNull(aStack, this.mStack) || this.needsVisualUpdate
                            && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 1) {
                        for (int x = -1; x < 2; x++) {
                            for (int y = 1; y < this.height; y++) {
                                for (int z = -1; z < 2; z++) {
                                    if (aStack == null
                                            || aStack.getItem() instanceof LabParts && aStack.getItemDamage() == 0) {
                                        if (this.mCulture == null || aStack == null
                                                || aStack.getTagCompound() == null
                                                || this.mCulture.getID() != aStack.getTagCompound().getInteger("ID")) {
                                            lCulture = aStack == null || aStack.getTagCompound() == null ? null
                                                    : BioCulture
                                                            .getBioCulture(aStack.getTagCompound().getString("Name"));
                                            this.sendPackagesOrRenewRenderer(x, y, z, lCulture);
                                        }
                                    }
                                }
                            }
                        }
                        this.mStack = aStack;
                        this.mCulture = lCulture;
                    }
                    if (this.needsVisualUpdate
                            && this.getBaseMetaTileEntity().getTimer() % GT_TileEntity_BioVat.TIMERDIVIDER == 1) {
                        if (this.getBaseMetaTileEntity().isClientSide()) new Throwable().printStackTrace();
                        this.placeFluid();
                        this.needsVisualUpdate = false;
                    }
                }
            } else {
                this.onRemoval();
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (this.height != this.reCalculateHeight()) this.needsVisualUpdate = true;
        this.doAllVisualThings();
        if (this.getBaseMetaTileEntity().isServerSide() && this.mRadHatches.size() == 1) {
            this.mSievert = this.mRadHatches.get(0).getSievert();
            if (this.getBaseMetaTileEntity().isActive() && this.mNeededSievert > this.mSievert)
                this.mOutputFluids = null;
        }
        if (aBaseMetaTileEntity.isServerSide() && this.mMaxProgresstime <= 0) {
            this.mTimes = 0;
            this.mMaxProgresstime = 0;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mFluidHeight", this.height);
        if (this.mCulture != null && !this.mCulture.getName().isEmpty())
            aNBT.setString("mCulture", this.mCulture.getName());
        else if ((this.mCulture == null || this.mCulture.getName().isEmpty())
                && !aNBT.getString("mCulture").isEmpty()) {
                    aNBT.removeTag("mCulture");
                }
        if (this.mFluid != null) aNBT.setString("mFluid", this.mFluid.getName());
        aNBT.setInteger("mSievert", this.mSievert);
        aNBT.setInteger("mNeededSievert", this.mNeededSievert);
        aNBT.setBoolean("isVisibleFluid", this.isVisibleFluid);
        super.saveNBTData(aNBT);
    }

    @Override
    public void onRemoval() {
        if (this.isVisibleFluid) {
            int xDir = this.getXDir();
            int zDir = this.getZDir();
            this.removeFluid(xDir, zDir);
            this.sendRenderPackets(xDir, zDir);
        } else if (this.getBaseMetaTileEntity().getWorld().getWorldTime() % 20 == 7) {
            this.sendRenderPackets();
        }

        super.onRemoval();
    }

    private int getXDir() {
        return this.getBaseMetaTileEntity().getBackFacing().offsetX * 2;
    }

    private int getZDir() {
        return this.getBaseMetaTileEntity().getBackFacing().offsetZ * 2;
    }

    private void sendRenderPackets() {
        int xDir = this.getXDir();
        int zDir = this.getZDir();
        this.sendRenderPackets(xDir, zDir);
    }

    private void sendRenderPackets(int xDir, int zDir) {
        if (SideReference.Side.Server) {
            for (int x = -1; x < 2; x++) {
                for (int y = 1; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        MainMod.BW_Network_instance.sendPacketToAllPlayersInRange(
                                this.getBaseMetaTileEntity().getWorld(),
                                new RendererPacket(
                                        new Coords(
                                                xDir + x + this.getBaseMetaTileEntity().getXCoord(),
                                                y + this.getBaseMetaTileEntity().getYCoord(),
                                                zDir + z + this.getBaseMetaTileEntity().getZCoord(),
                                                this.getBaseMetaTileEntity().getWorld().provider.dimensionId),
                                        this.mCulture == null ? BioCulture.NULLCULTURE.getColorRGB()
                                                : this.mCulture.getColorRGB(),
                                        true),
                                this.getBaseMetaTileEntity().getXCoord(),
                                this.getBaseMetaTileEntity().getZCoord());
                    }
                }
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.height = aNBT.getInteger("mFluidHeight");
        this.mCulture = BioCulture.getBioCulture(aNBT.getString("mCulture"));
        if (!aNBT.getString("mFluid").isEmpty()) this.mFluid = FluidRegistry.getFluid(aNBT.getString("mFluid"));
        this.mSievert = aNBT.getInteger("mSievert");
        this.mNeededSievert = aNBT.getInteger("mNeededSievert");
        super.loadNBTData(aNBT);
        this.isVisibleFluid = aNBT.getBoolean("isVisibleFluid");
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_BioVat(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW).extFacing().glow()
                            .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW).extFacing().glow()
                            .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 3, 0);
    }

    @Override
    public String[] getInfoData() {
        final String[] baseInfoData = super.getInfoData();
        final String[] infoData = new String[baseInfoData.length + 2];
        System.arraycopy(baseInfoData, 0, infoData, 0, baseInfoData.length);
        // See https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/11923
        // here we must check the machine is well-formed as otherwise getExpectedMultiplier might error out!
        infoData[infoData.length - 2] = StatCollector.translateToLocal("BW.infoData.BioVat.expectedProduction") + ": "
                + EnumChatFormatting.GREEN
                + (this.mMachine
                        ? (this.mMaxProgresstime <= 0 ? this.getExpectedMultiplier(null, false)
                                : this.mExpectedMultiplier) * 100
                        : -1)
                + EnumChatFormatting.RESET
                + " %";
        infoData[infoData.length - 1] = StatCollector.translateToLocal("BW.infoData.BioVat.production") + ": "
                + EnumChatFormatting.GREEN
                + (this.mMaxProgresstime <= 0 ? 0 : this.mTimes) * 100
                + EnumChatFormatting.RESET
                + " %";
        return infoData;
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
            float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

}
