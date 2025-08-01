package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static gregtech.api.GregTechAPI.sBlockCasings4;
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
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.SimpleCuboidMultiblockBase;

public class MTEAdvImplosionCompressor extends SimpleCuboidMultiblockBase {

    public MTEAdvImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
    }

    public MTEAdvImplosionCompressor(String aName) {
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

        setCasingTextureIndex(48);
        setCasingBlock(sBlockCasings4, 0);

        setValidHatches(InputBus, OutputBus, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvImplosionCompressor(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Implosion Compressor";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Factory Grade Advanced Implosion Compressor")
            .addInfo("Speed: +100% | EU Usage: 100% | Parallel: ((Tier/2)+1)")
            .addInfo("Constructed exactly the same as a normal Implosion Compressor")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedImplosionActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedImplosionActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedImplosion;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedImplosionGlow;
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
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.RANDOM_EXPLODE;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAdvImplosion;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (GTUtility.getTier(this.getMaxInputVoltage()) / 2 + 1);
    }

}
