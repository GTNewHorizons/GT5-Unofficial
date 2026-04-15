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
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;

public class MTEPyrolyseOven extends MTEExtendedPowerMultiBlockBase<MTEPyrolyseOven> implements ISurvivalConstructable {

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
        .addElement('E', ofFrame(Materials.Steel))
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
            .beginStructureBlock(7, 6, 5, false)
            .addController("Front center")
            .addCasingInfoMin("Pyrolyse Oven Casing", 60, false)
            .addCasingInfoExactly("Heating Coils", 12, true)
            .addCasingInfoExactly("Steel Frame Box", 2, false)
            .addCasingInfoExactly("Steel Pipe Casing", 8, false)
            .addCasingInfoExactly("Steel Firebox Casing", 4, false)
            .addEnergyHatch("Any bottom layer casing", 1)
            .addMaintenanceHatch("Any bottom layer casing", 1)
            .addMufflerHatch("Any top layer casing", 2)
            .addInputBus("Any top layer casing", 2)
            .addInputHatch("Any top layer casing", 2)
            .addOutputBus("Any bottom layer casing", 1)
            .addOutputHatch("Any bottom layer casing", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Ya9yu")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.PyrolyseOvenCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.PyrolyseOvenCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PYROLYSE_OVEN_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.PyrolyseOvenCasing.getCasingTexture() };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        coilHeat = HeatingCoilLevel.None;
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 60
            && !mMufflerHatches.isEmpty();
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
