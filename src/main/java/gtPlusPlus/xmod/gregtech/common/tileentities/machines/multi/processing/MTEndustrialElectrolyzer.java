package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.SimpleCuboidMultiblockBase;

public class MTEndustrialElectrolyzer extends SimpleCuboidMultiblockBase {

    public MTEndustrialElectrolyzer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
    }

    public MTEndustrialElectrolyzer(final String aName) {
        super(aName);
        initStructure();
    }

    protected void initStructure() {
        setWidth(3);
        setHeight(3);
        setMinLength(3);
        setMaxLength(12);

        setMinCasingsBase(0);
        setMinCasingsPerLayer(2);

        setCasingTextureIndex(69);
        setCasingBlock(ModBlocks.blockCasingsMisc, 5);

        setValidHatches(InputBus, OutputBus, InputHatch, OutputHatch, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEndustrialElectrolyzer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Electrolyzer";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("180% faster than using single block machines of the same voltage")
            .addInfo("Only uses 90% of the EU/t normally required")
            .addInfo("Processes two items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialElectrolyzerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialElectrolyzerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialElectrolyzer;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialElectrolyzerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(5);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.electrolyzerNonCellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 2.8F)
            .setEuModifier(0.9F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialElectrolyzer;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * GTUtility.getTier(this.getMaxInputVoltage());
    }

}
