package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;

@IMetaTileEntity.SkipGenerateDescription
public class MTEAutoCrafter extends MTEExtendedPowerMultiBlockBase<MTEAutoCrafter>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingAmount;
    // spotless:off
    private static final String[][] structure = {
        { "CCC", "CCC", "CCC" },
        { "C~C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC" }, };
    // spotless:on
    private static IStructureDefinition<MTEAutoCrafter> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public MTEAutoCrafter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAutoCrafter(String mName) {
        super(mName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEAutoCrafter(this.mName);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAutoCrafter;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.auto_assembler"))
            .addMarkdown(new ResourceLocation("gregtech", "auto-crafter"))
            .addBulkMachineInfo(2, 3f, 1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("10-20", Casings.BulkProductionFrame.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputHatch("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER,
            Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_GLOW,
            Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE,
            Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.BulkProductionFrame.getCasingTexture();
    }

    @Override
    public IStructureDefinition<MTEAutoCrafter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAutoCrafter>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    buildHatchAdder(MTEAutoCrafter.class)
                        .atLeast(InputBus, OutputBus, InputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.BulkProductionFrame.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.BulkProductionFrame.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, itemStack, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity baseMetaTileEntity, ItemStack itemStack, List<StructureError> errors) {
        casingAmount = 0;
        checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors);
        checkCasingMin(errors, casingAmount, 10);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * (Math.max(1, GTUtility.getTier(getMaxInputVoltage())));
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
