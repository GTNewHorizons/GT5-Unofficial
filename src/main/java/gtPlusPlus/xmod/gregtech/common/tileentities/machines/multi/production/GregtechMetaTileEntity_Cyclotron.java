package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_Cyclotron extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Cyclotron>
    implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_Cyclotron> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_Cyclotron(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Cyclotron(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Particle Accelerator";
    }

    public int tier() {
        return 5;
    }

    @Override
    public long maxEUStore() {
        return 1800000000L;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Cyclotron(this.mName);
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_Cyclotron> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Cyclotron>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "               ", "      hhh      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " h           h ", " h           h ",
                                " h           h ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      hhh      ", "               ", },
                            { "      hhh      ", "    hhccchh    ", "   hcchhhcch   ", "  hchh   hhch  ",
                                " hch       hch ", " hch       hch ", "hch         hch", "hch         hch",
                                "hch         hch", " hch       hch ", " hch       hch ", "  hchh   hhch  ",
                                "   hcch~hcch   ", "    hhccchh    ", "      hhh      ", },
                            { "               ", "      hhh      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " h           h ", " h           h ",
                                " h           h ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      hhh      ", "               ", } }))
                .addElement(
                    'h',
                    buildHatchAdder(GregtechMetaTileEntity_Cyclotron.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(44)
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                .addElement('c', ofBlock(getCyclotronCoil(), getCyclotronCoilMeta()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 7, 1, 12);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 7, 1, 12, elementBudget, env, false, true);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 7, 1, 12) && mCasing >= 40 && checkHatch();
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public int getCasingMeta() {
        return 10;
    }

    public Block getCyclotronCoil() {
        return ModBlocks.blockCasings2Misc;
    }

    public int getCyclotronCoilMeta() {
        return 9;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Super Magnetic Speed Shooter")
            .addSeparator()
            .addInfo("Particles are accelerated over 186 revolutions to 80% light speed")
            .addInfo("Can produce a continuous beam current of 2.2 mA at 590 MeV")
            .addInfo("Which will be extracted from the Isochronous Cyclotron")
            .addSeparator()
            .addInfo("Consists of the same layout as a Fusion Reactor")
            .addInfo("Any external casing can be a hatch/bus, unlike Fusion")
            .addInfo("Cyclotron Machine Casings around Cyclotron Coil Blocks")
            .addInfo("All Hatches must be IV or better")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .addCasingInfoMin("Cyclotron Machine Casings", 40, false)
            .addCasingInfoMin("Cyclotron Coil", 32, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return getIconOverlay();
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return getIconOverlay();
    }

    @Override
    protected int getCasingTextureId() {
        return 44;
    }

    public IIconContainer getIconOverlay() {
        if (this.getBaseMetaTileEntity()
            .isActive()) {
            return TexturesGtBlock.Overlay_MatterFab_Active_Animated;
        }
        return TexturesGtBlock.Overlay_MatterFab_Animated;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cyclotronRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) {
                    for (ItemStack s : outputItems) {
                        if (s != null) {
                            if (s.getItem() instanceof IonParticles) {
                                long aCharge = IonParticles.getChargeState(s);
                                if (aCharge == 0) {
                                    IonParticles.setChargeState(
                                        s,
                                        MathUtils.getRandomFromArray(
                                            new int[] { -5, -5, -4, -4, -4, -3, -3, -3, -3, -3, -2, -2, -2, -2, -2, -2,
                                                -2, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                                1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4,
                                                5, 5, 5, 6, 6 }));
                                }
                            }
                        }
                    }
                }
                return result;
            }
        };
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mOutputBusses.size() > 0) {
            for (GT_MetaTileEntity_Hatch_OutputBus g : this.mOutputBusses) {
                if (g != null) {
                    for (ItemStack s : g.mInventory) {
                        if (s != null) {
                            if (s.getItem() instanceof IonParticles) {
                                long aCharge = IonParticles.getChargeState(s);
                                if (aCharge == 0) {
                                    IonParticles.setChargeState(
                                        s,
                                        MathUtils.getRandomFromArray(
                                            new int[] { -5, -5, -4, -4, -4, -3, -3, -3, -3, -3, -2, -2, -2, -2, -2, -2,
                                                -2, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                                1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4,
                                                5, 5, 5, 6, 6 }));
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiCyclotron;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getExtraInfoData() {
        String tier = tier() == 5 ? "I" : "II";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration;
            }
        }

        return new String[] { "COMET - Compact Cyclotron MK " + tier, "EU Required: " + powerRequired + "EU/t",
            "Stored EU: " + this.getEUVar() + " / " + maxEUStore() };
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
