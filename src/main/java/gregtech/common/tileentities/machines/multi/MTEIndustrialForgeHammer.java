package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialForgeHammer extends MTEExtendedPowerMultiBlockBase<MTEIndustrialForgeHammer>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_Z = 1;

    private Byte solenoidLevel = null;
    private int casingAmount;
    private static IStructureDefinition<MTEIndustrialForgeHammer> STRUCTURE_DEFINITION = null;

    public MTEIndustrialForgeHammer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialForgeHammer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialForgeHammer(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Forge Hammer")
            .addInfo(TooltipHelper.parallelText("Voltage Tier * Solenoid Tier * 6") + " Parallels")
            .addStaticSpeedInfo(2f)
            .addStaticEuEffInfo(1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 9, false)
            .addController("Front center, 2nd layer")
            .addCasing("10-29", "Forge Casing", false)
            .addCasing("20", "Rebolted Black Steel Casing", false)
            .addCasing("3", "Solenoid Superconductor Coil", true)
            .addCasing("2", "Refined Graphite Block", false)
            .addEnergyHatch("1+", "Any base forge casing", 1)
            .addMaintenanceHatch("1", "Any base forge casing", 1)
            .addMufflerHatch("1", "Any base forge casing", 1)
            .addInputBus("1+", "Any base forge casing", 1)
            .addInputHatch("0+", "Any base forge casing", 1)
            .addOutputBus("1+", "Any base forge casing", 1)
            .addOutputHatch("0+", "Any base forge casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.SOLENOID)
            .addStructureAuthors(EnumChatFormatting.GOLD + "PCGMatt")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialForgeHammer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialForgeHammer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", " CCC " },
                        { "     ", "  E  ", " EEE ", "     ", "     ", "     ", "     ", " C~C ", "CCCCC" },
                        { "  D  ", " DDD ", " DAD ", "DDADD", "D A D", "D B D", "D   D", "DCBCD", "DCCCD" },
                        { "     ", "  E  ", " EEE ", "     ", "     ", "     ", "     ", " CCC ", "CCCCC" },
                        { "     ", "     ", "     ", "     ", "     ", "     ", "     ", "     ", " CCC " } })
                .addElement(
                    'A',
                    GTStructureChannels.SOLENOID.use(
                        ofSolenoidCoil(
                            MTEIndustrialForgeHammer::setSolenoidLevel,
                            MTEIndustrialForgeHammer::getSolenoidLevel)))
                .addElement('B', Casings.RefinedGraphiteBlock.asElement())
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialForgeHammer.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(Casings.ForgeCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.ForgeCasing.asElement())))
                .addElement('D', ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, 32100))
                .addElement('E', Casings.ForgeCasing.asElement())
                .build();
        }
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
        casingAmount = 0;
        solenoidLevel = null;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 10);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_FORGE_HAMMER;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            TexturesGtBlock.oMCAIndustrialForgeHammer,
            TexturesGtBlock.oMCAIndustrialForgeHammerGlow,
            TexturesGtBlock.oMCAIndustrialForgeHammerActive,
            TexturesGtBlock.oMCAIndustrialForgeHammerActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.ForgeCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hammerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1 / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 * (solenoidLevel == null ? 1 : solenoidLevel) * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialForgeHammer;
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
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
}
