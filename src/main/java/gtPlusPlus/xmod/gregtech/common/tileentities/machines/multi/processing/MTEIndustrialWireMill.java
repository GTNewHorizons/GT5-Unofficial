package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.ForgeDirection;

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

public class MTEIndustrialWireMill extends SimpleCuboidMultiblockBase {

    public MTEIndustrialWireMill(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
    }

    public MTEIndustrialWireMill(final String aName) {
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

        setCasingTextureIndex(70);
        setCasingBlock(ModBlocks.blockCasingsMisc, 6);

        setValidHatches(InputBus, OutputBus, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialWireMill(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Wiremill, IWF";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("200% faster than using single block machines of the same voltage")
            .addInfo("Only uses 75% of the EU/t normally required")
            .addInfo("Processes four items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_RECYCLER_OP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialWireMillActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialWireMillActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialWireMill;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialWireMillGlow;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                inputSeparation ? "interaction.separateBusses.enabled" : "interaction.separateBusses.disabled"));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialWireMill;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
