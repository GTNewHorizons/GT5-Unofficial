package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;

public class GT_MetaTileEntity_IndustrialElectromagneticSeparator
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_IndustrialElectromagneticSeparator>
    implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> STRUCTURE_DEFINITION = null;

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(final int aID, final String aName,
        final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_IndustrialElectromagneticSeparator>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.GTPP_INDEX(5))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 5))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialElectromagneticSeparator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Electromagnetic Separator")
            .addInfo("Controller Block for the Industrial Electromagnetic Separator")
            .addInfo("Insert a magnetic core into the magnetic core housing to use")
            .addInfo("Better magnetic cores give further bonuses")
            .addInfo("???% faster than using single block machines of the same voltage")
            .addInfo("Only uses ??% of the EU/t normally required")
            .addInfo("Processes ?? items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Electromagnetic Casings", 6, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("(Magnetic Outputs) Any Casing", 1)
            .addOutputBus("(Amagnetic Outputs) Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6;
    }

    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.electroMagneticSeparatorRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
