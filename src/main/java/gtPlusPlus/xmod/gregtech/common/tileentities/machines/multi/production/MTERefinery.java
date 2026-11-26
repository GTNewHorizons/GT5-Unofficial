package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;

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
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;

@IMetaTileEntity.SkipGenerateDescription
public class MTERefinery extends MTEExtendedPowerMultiBlockBase<MTERefinery>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingAmount;
    // spotless:off
    private static final String[][] structure = {
        { "   ", " N ", "   " },
        { " N ", "NIN", " N " },
        { " N ", "NIN", " N " },
        { " N ", "NIN", " N " },
        { " Z ", "ZIZ", " Z " },
        { " N ", "NIN", " N " },
        { "XXX", "XXX", "XXX" },
        { "X~X", "XXX", "XXX" }, };
    // spotless:on
    private static IStructureDefinition<MTERefinery> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public MTERefinery(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTERefinery(final String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.fuel_refinery"))
            .addMarkdown(new ResourceLocation("gregtech", "refinery"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 9, 3, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("17", Casings.HastelloyNSealantBlock.getLocalizedName(), false)
            .addCasing("7-11", Casings.HastelloyXStructuralBlock.getLocalizedName(), false)
            .addCasing("5", Casings.IncoloyDSFluidContainmentBlock.getLocalizedName(), false)
            .addCasing("4", Casings.ReactorShieldCasing.getLocalizedName(), false)
            .addEnergyHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_hastelloy_x_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_hastelloy_x_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_hastelloy_x_casing"), 1)
            .addInputHatch("2+", StatCollector.translateToLocal("gt.mbtt.structure.any_hastelloy_x_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_hastelloy_x_casing"), 1)
            .toolTipFinisher();
        // spotless:on
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
            Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER,
            Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW,
            Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE,
            Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.HastelloyXStructuralBlock.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fissionFuelProcessingRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    @Override
    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity imte = aTileEntity.getMetaTileEntity();
        if (imte instanceof MTEHatchMuffler muffler && muffler.mTier >= 5) {
            return addToMachineList(aTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTERefinery> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTERefinery>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'X',
                    ofChain(
                        buildHatchAdder(MTERefinery.class)
                            .atLeast(Energy, Maintenance, OutputHatch, OutputBus, InputHatch)
                            .casingIndex(Casings.HastelloyXStructuralBlock.textureId)
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTERefinery.class).atLeast(Muffler)
                            .adder(MTERefinery::addMufflerToMachineList)
                            .hatchItemFilterAnd(t -> filterByMTETier(6, Integer.MAX_VALUE))
                            .casingIndex(Casings.HastelloyXStructuralBlock.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.HastelloyXStructuralBlock.asElement())))
                .addElement('I', Casings.IncoloyDSFluidContainmentBlock.asElement())
                .addElement('N', Casings.HastelloyNSealantBlock.asElement())
                .addElement('Z', Casings.ReactorShieldCasing.asElement())
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 7, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 7, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 7, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 7);
        checkOneEnergyHatch(errors);
        checkOneMaintenanceHatch(errors);
        checkOneMufflerHatch(errors);
        checkHatchMin(errors, InputHatch, 2);
        checkHatchMax(errors, InputHatch, 4);
        checkHasOutputHatch(errors);
        checkHatchMax(errors, OutputHatch, 2);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiRefinery;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTERefinery(this.mName);
    }
}
