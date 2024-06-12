package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.*;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class CoolantTower extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<CoolantTower> multiDefinition = null;
    private final int CASING_INDEX = 1542;

    public CoolantTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public CoolantTower(String name) {
        super(name);
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<CoolantTower>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", "  BB   BB  ",
                                "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "           ", "    BBB    ", "   B   B   ", "   B   B   ",
                                "   B   B   ", "    BBB    ", "           ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", "  BB   BB  ",
                                "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B     B  ",
                                "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           " },
                            { "           ", "    BBB    ", "   BBBBB   ", "  BB   BB  ", " BB     BB ", " BB     BB ",
                                " BB     BB ", "  BB   BB  ", "   BBBBB   ", "    BBB    ", "           " },
                            { "           ", "    BBB    ", "   B   B   ", "  B     B  ", " B       B ", " B       B ",
                                " B       B ", "  B     B  ", "   B   B   ", "    BBB    ", "           " },
                            { "           ", "   BBBBB   ", "  BB   BB  ", " BB     BB ", " B       B ", " B       B ",
                                " B       B ", " BB     BB ", "  BB   BB  ", "   BBBBB   ", "           " },
                            { "   HH~HH   ", "  HBBBBBH  ", " HB     BH ", "HB       BH", "HB       BH", "HB       BH",
                                "HB       BH", "HB       BH", " HB     BH ", "  HBBBBBH  ", "   HHHHH   " },
                            { "   CCCCC   ", "  C     C  ", " C       C ", "C         C", "C         C", "C         C",
                                "C         C", "C         C", " C       C ", "  C     C  ", "   CCCCC   " }, }))
                .addElement('B', ofBlockAnyMeta(GregTech_API.sBlockConcretes, 8))
                .addElement('C', ofFrame(Materials.TungstenCarbide))
                .addElement(
                    'H',
                    buildHatchAdder(CoolantTower.class).atLeast(GT_HatchElement.InputHatch, GT_HatchElement.OutputHatch)
                        .casingIndex(CASING_INDEX)
                        .dot(1)
                        .buildAndChain(ofBlockAnyMeta(GregTech_API.sBlockConcretes, 8)))
                .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 5, 11, 0);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coolant Tower")
            .addInfo("Controller block for the Coolant Tower.")
            .addInfo("Turn Steam back to Distilled Water.")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .addController("Mid of the second layer.")
            .addInputHatch("Input Hatch", 1)
            .addOutputHatch("Output Hatch", 1)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 5, 11, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("CoolantTower.hint", 3);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new CoolantTower(mName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        this.mMaxProgresstime = 20;
        int steam = 0;

        for (GT_MetaTileEntity_Hatch_Input tHatch : filterValidMTEs(mInputHatches)) {
            steam += maybeDrainHatch(tHatch);
        }
        addOutput(GT_ModHandler.getDistilledWater(steam / 160));
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int maybeDrainHatch(GT_MetaTileEntity_Hatch_Input tHatch) {
        if (tHatch instanceof GT_MetaTileEntity_Hatch_MultiInput) {
            int drained = 0;
            for (FluidStack maybeSteam : ((GT_MetaTileEntity_Hatch_MultiInput) tHatch).getStoredFluid()) {
                drained += maybeDrainSteam(tHatch, maybeSteam);
            }
            return drained;
        }
        return maybeDrainSteam(tHatch, tHatch.getFillableStack());
    }

    private int maybeDrainSteam(GT_MetaTileEntity_Hatch_Input tHatch, FluidStack maybeSteam) {
        if (maybeSteam == null) return 0;
        if (!GT_Utility.areFluidsEqual(maybeSteam, GT_ModHandler.getSteam(1))) return 0;
        FluidStack defoSteam = tHatch.drain(ForgeDirection.UNKNOWN, maybeSteam, true);
        return defoSteam.amount;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[12][6], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[12][6], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[12][6] };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 5, 11, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
