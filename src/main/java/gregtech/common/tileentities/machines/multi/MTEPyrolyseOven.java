package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;

public class MTEPyrolyseOven extends MTEExtendedPowerMultiBlockBase<MTEPyrolyseOven>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private HeatingCoilLevel coilHeat;
    private int casingAmount;

    private static final IStructureDefinition<MTEPyrolyseOven> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPyrolyseOven>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "       ", "       ", " F   F ", "FBF FBF", "DBD~DBD", "GGGGGGG" },
                { "       ", "       ", " C   C ", "F FFF F", "D D D D", "GCGGGCG" },
                { "       ", "       ", " C   C ", "F FFF F", "D     D", "GCGGGCG" },
                { "       ", "       ", " C   C ", "F FFF F", "D D D D", "GCGGGCG" },
                { " E   E ", " A   A ", " A   A ", "FAF FAF", "DADDDAD", "GGGGGGG" } })
        .addElement('A', Casings.SteelPipeCasing.asElement())
        .addElement('B', Casings.SteelFireboxCasing.asElement())
        .addElement(
            'C',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEPyrolyseOven::setCoilLevel, MTEPyrolyseOven::getCoilLevel))))
        .addElement('D', onElementPass(MTEPyrolyseOven::onCasingAdded, Casings.PyrolyseOvenCasing.asElement()))
        .addElement('E', ofFrame(Materials2Materials.Steel))
        .addElement(
            'F',
            buildHatchAdder(MTEPyrolyseOven.class).atLeast(InputBus, InputHatch, Muffler)
                .casingIndex(Casings.PyrolyseOvenCasing.textureId)
                .hint(2)
                .buildAndChain(onElementPass(MTEPyrolyseOven::onCasingAdded, Casings.PyrolyseOvenCasing.asElement())))
        .addElement(
            'G',
            buildHatchAdder(MTEPyrolyseOven.class).atLeast(OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(Casings.PyrolyseOvenCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(MTEPyrolyseOven::onCasingAdded, Casings.PyrolyseOvenCasing.asElement())))
        .build();

    public MTEPyrolyseOven(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPyrolyseOven(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Coke Oven")
            .addInfo("Industrial Charcoal producer")
            .addDynamicSpeedInfo(0.5f, TooltipTier.COIL)
            .addInfo("EU/t is not affected by Coil tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 7, 6, true)
            .addController("Front center, 2nd layer")
            .addCasing("60-68", "Pyrolyse Oven Casing", false)
            .addCasing("12", "Heating Coil", true)
            .addCasing("8", "Steel Pipe Casing", false)
            .addCasing("4", "Steel Firebox Casing", false)
            .addCasing("2", "Steel Frame Box", false)
            .addEnergyHatch("1+", "Any bottom casing", 1)
            .addMaintenanceHatch("1", "Any bottom casing", 1)
            .addMufflerHatch("1", "Any top casing", 2)
            .addInputBus("0+", "Any top casing", 2)
            .addInputHatch("0+", "Any top casing", 2)
            .addOutputBus("0+", "Any bottom casing", 1)
            .addOutputHatch("0+", "Any bottom casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Ya9yu")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_PYROLYSE_OVEN,
            OVERLAY_FRONT_PYROLYSE_OVEN_GLOW,
            OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE,
            OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.PyrolyseOvenCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.pyrolyseRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonusSupplier(this::getSpeedBonus);
    }

    @Override
    public IStructureDefinition<MTEPyrolyseOven> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilHeat;
    }

    private void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        coilHeat = aCoilLevel;
    }

    public double getSpeedBonus() {
        return 2f / (1 + coilHeat.getTier());
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        coilHeat = HeatingCoilLevel.None;
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 60);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionPyrolyseOvenPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPyrolyseOven(this.mName);
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }
}
