package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.SimpleCuboidMultiblockBase;

public class MTEIndustrialExtruder extends SimpleCuboidMultiblockBase {

    public MTEIndustrialExtruder(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
    }

    public MTEIndustrialExtruder(final String aName) {
        super(aName);
        initStructure();
    }

    protected void initStructure() {
        setWidth(3);
        setHeight(3);
        setMinLength(3);
        setMaxLength(12);

        setMinCasingsBase(4);
        setMinCasingsPerLayer(2);

        setCasingTextureIndex(97);
        setCasingBlock(ModBlocks.blockCasings3Misc, 1);

        setValidHatches(InputBus, OutputBus, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialExtruder(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Extruder, IEM";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("250% faster than using single block machines of the same voltage")
            .addInfo("Processes four items per voltage tier")
            .addInfo("Extrusion Shape for recipe goes in the Input Bus")
            .addInfo("Each Input Bus can have a different shape!")
            .addInfo("You can use several input buses per multiblock")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialExtruderActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialExtruderActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialExtruder;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialExtruderGlow;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.extruderRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialExtruder;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }
}
