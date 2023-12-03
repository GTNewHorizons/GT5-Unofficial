package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings4;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_Adv_Implosion
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Adv_Implosion> {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_Adv_Implosion> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_Adv_Implosion(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Adv_Implosion(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_Implosion(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Implosion Compressor";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Factory Grade Advanced Implosion Compressor")
                .addInfo("Speed: +100% | EU Usage: 100% | Parallel: ((Tier/2)+1)")
                .addInfo("Constructed exactly the same as a normal Implosion Compressor")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front center").addCasingInfoMin("Robust TungstenSteel Casing", 10, false)
                .addInputBus("Any casing", 1).addOutputBus("Any casing", 1).addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1).addMufflerHatch("Any casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_Adv_Implosion> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Adv_Implosion>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    ofHatchAdder(GregtechMetaTileEntity_Adv_Implosion::addAdvImplosionList, 48, 1),
                                    onElementPass(x -> ++x.mCasing, ofBlock(sBlockCasings4, 0))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public final boolean addAdvImplosionList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return 48;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.implosionRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F).setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.RANDOM_EXPLODE;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiAdvImplosion;
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
    public int getMaxParallelRecipes() {
        return (GT_Utility.getTier(this.getMaxInputVoltage()) / 2 + 1);
    }

}
