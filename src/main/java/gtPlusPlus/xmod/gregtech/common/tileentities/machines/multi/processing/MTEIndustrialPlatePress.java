package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GTUITextures;
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
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.gui.MTEIndustrialPlatePressGui;

public class MTEIndustrialPlatePress extends SimpleCuboidMultiblockBase {

    private static final int MACHINEMODE_BENDER = 0;
    private static final int MACHINEMODE_FORMER = 1;

    private static IStructureDefinition<MTEIndustrialPlatePress> STRUCTURE_DEFINITION = null;

    public MTEIndustrialPlatePress(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
    }

    public MTEIndustrialPlatePress(final String aName) {
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

        setCasingTextureIndex(50);
        setCasingBlock(ModBlocks.blockCasingsMisc, 4);

        setValidHatches(InputBus, OutputBus, InputHatch, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialPlatePress(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Bending Machine, Forming Press, IMP";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("500% faster than using single block machines of the same voltage")
            .addInfo("Processes four items per voltage tier")
            .addInfo("Circuit for recipe goes in the Input Bus")
            .addInfo("Each Input Bus can have a different Circuit/Shape!")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_COMPRESSOR_OP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialPlatePressActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialPlatePressActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDIndustrialPlatePress;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDIndustrialPlatePressGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return 50;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_FORMER) ? RecipeMaps.formingPressRecipes : RecipeMaps.benderRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.formingPressRecipes, RecipeMaps.benderRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 6F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        if (machineMode == MACHINEMODE_FORMER)
            return PollutionConfig.pollutionPerSecondMultiIndustrialPlatePress_ModeForming;
        return PollutionConfig.pollutionPerSecondMultiIndustrialPlatePress_ModeBending;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        // Migrates old NBT tag to the new one
        if (aNBT.hasKey("mFormingMode")) {
            machineMode = aNBT.getBoolean("mFormingMode") ? MACHINEMODE_FORMER : MACHINEMODE_BENDER;
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_BENDING);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_FORMING);
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.GTPP_MULTI_INDUSTRIAL_PLATE_PRESS.mode." + machineMode);
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    protected @NotNull MTEIndustrialPlatePressGui getGui() {
        return new MTEIndustrialPlatePressGui(this);
    }
}
