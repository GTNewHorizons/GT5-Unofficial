package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEOreWashingPlant extends MTEExtendedPowerMultiBlockBase<MTEOreWashingPlant>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingAmount;
    private boolean needsWaterFill = false;
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int MACHINEMODE_OREWASH = 0;
    private static final int MACHINEMODE_SIMPLEWASH = 1;
    private static final String[][] structure = new String[][] { { "     ", " CCC ", " C~C ", " CCC " },
        { "   B ", "CDADC", "CBDDC", "CCCCC" }, { "  B  ", "CDADC", "CDBDC", "CCCCC" },
        { " B   ", "CDADC", "CDDBC", "CCCCC" }, { "C   C", "CBABC", "CDBDC", "CCCCC" },
        { "C  BC", "CDADC", "CBDDC", "CCCCC" }, { "C B C", "CDADC", "CDBDC", "CCCCC" },
        { "CB  C", "CDADC", "CDDBC", "CCCCC" }, { " CCC ", " CCC ", " CCC ", " CCC " } };

    private static final IStructureDefinition<MTEOreWashingPlant> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEOreWashingPlant>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', Casings.SteelGearBoxCasing.asElement())
        .addElement('B', ofFrame(Materials2Materials.Steel))
        .addElement(
            'C',
            buildHatchAdder(MTEOreWashingPlant.class)
                .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                .casingIndex(114) // WashPlantCasing
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.WashPlantCasing.asElement())))
        .addElement('D', ofChain(ofAnyWater(false), isAir()))
        .build();

    public MTEOreWashingPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOreWashingPlant(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEOreWashingPlant(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Ore Washer, Simple Washer, OWP")
            .addBulkMachineInfo(4, 5f, 1f)
            .addInfo("Can be configured with a screwdriver to also be used as Simple Washer")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 5, 4, false)
            .addController("Front center")
            .addCasing("70-85", "Wash Plant Casing", false)
            .addCasing("15", "Steel Frame Box", false)
            .addCasing("7", "Steel Gear Box Casing", false)
            .addEnergyHatch("1+", "Any wash plant casing", 1)
            .addMaintenanceHatch("1", "Any wash plant casing", 1)
            .addMufflerHatch("1", "Any wash plant casing", 1)
            .addInputBus("1+", "Any wash plant casing", 1)
            .addInputHatch("1+", "Any wash plant casing", 1)
            .addOutputBus("1+", "Any wash plant casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "ya9yu")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEOreWashingPlant> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        needsWaterFill = false;
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) {
            needsWaterFill = GTStructureUtility.hasWaterAtStructurePosition(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'D');
            return;
        }
        checkCasingMin(errors, casingAmount, 70);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);

        if (!errors.isEmpty()) return;
        needsWaterFill = true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            TexturesGtBlock.oMCDIndustrialWashPlant,
            TexturesGtBlock.oMCDIndustrialWashPlantGlow,
            TexturesGtBlock.oMCDIndustrialWashPlantActive,
            TexturesGtBlock.oMCDIndustrialWashPlantActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return TextureFactory.of(ModBlocks.blockCasings2Misc, 4);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        switch (machineMode) {
            case MACHINEMODE_OREWASH -> {
                return RecipeMaps.oreWasherRecipes;
            }
            default -> {
                return GTPPRecipeMaps.simpleWasherRecipes;
            }
        }
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.oreWasherRecipes, GTPPRecipeMaps.simpleWasherRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialWashPlant_ModeWasher;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && aTick % 20 == 0) {
            if (GTStructureUtility.fillStructureWithWater(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'D')) {
                needsWaterFill = false;
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mMode")) {
            machineMode = aNBT.getInteger("mMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public String getMachineModeKey() {
        return "GT5U.GTPP_MULTI_WASH_PLANT.mode." + machineMode;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
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
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_OREWASH) return MACHINEMODE_SIMPLEWASH;
        else return MACHINEMODE_OREWASH;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_ORE_WASHER_PLANT_LOOP;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_WASHPLANT,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SIMPLEWASHER);
    }
}
