package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.ElementalDataOrbHatch;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.ItemStack;
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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEElementalDuplicator extends MTEExtendedPowerMultiBlockBase<MTEElementalDuplicator>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingAmount;
    private static final String[][] structure = {
        { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", "CCCC~CCCC", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ",
            "   CCC   " },
        { "   CAC   ", "  ABFBA  ", " ABFGFBA ", "CBFGDGFBC", "AFGDDDGFA", "CBFGDGFBC", " ABFGFBA ", "  ABFBA  ",
            "   CAC   " },
        { "   CEC   ", "  E   E  ", " E     E ", "C   D   C", "E  DDD  E", "C   D   C", " E     E ", "  E   E  ",
            "   CEC   " },
        { "   CEC   ", "  E   E  ", " E     E ", "C   D   C", "E  DDD  E", "C   D   C", " E     E ", "  E   E  ",
            "   CEC   " },
        { "   CAC   ", "  ABFBA  ", " ABFGFBA ", "CBFGDGFBC", "AFGDDDGFA", "CBFGDGFBC", " ABFGFBA ", "  ABFBA  ",
            "   CAC   " },
        { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ",
            "   CCC   " }, };
    private static IStructureDefinition<MTEElementalDuplicator> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public MTEElementalDuplicator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEElementalDuplicator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEElementalDuplicator(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        tt.addMachineType("Replicator")
            .addInfo("Produces raw elements from UU-Matter")
            .addBulkMachineInfo(8, 2f, 1f)
            .addInfo("Maximum 1x Data Orb Repository")
            .addInfo("The programmed circuit selects which Data Orb to use (1-16)")
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 6, 9, true)
            .addController("Top center, 6th layer")
            .addCasing("120-139", "Elemental Confinement Shell", false)
            .addCasing("24", "Particle Containment Casing", false)
            .addCasing("24", "Matter Fabricator Casing", false)
            .addCasing("24", "Matter Generation Coil", false)
            .addCasing("24", "Resonance Chamber III", false)
            .addCasing("20", "High Voltage Current Capacitor", false)
            .addCasing("16", "Modulator III", false)
            .addMiscHatch(
                "1",
                StatCollector.translateToLocal("GTPP.tooltip.structure.data_orb_repository"),
                "Any confinement shell",
                1)
            .addEnergyHatch("1+", "Any confinement shell", 1)
            .addMaintenanceHatch("1", "Any confinement shell", 1)
            .addMufflerHatch("1", "Any confinement shell", 1)
            .addInputHatch("1+", "Any confinement shell", 1)
            .addOutputAny("1+", "Any confinement shell", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEElementalDuplicator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEElementalDuplicator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, structure)
                .addElement('A', Casings.MatterFabricatorCasing.asElement())
                .addElement('B', Casings.MatterGenerationCoil.asElement())
                .addElement('D', Casings.HighVoltageCurrentCapacitor.asElement())
                .addElement('E', Casings.ParticleContainmentCasing.asElement())
                .addElement('F', Casings.ResonanceChamber3.asElement())
                .addElement('G', Casings.Modulator3.asElement())
                .addElement(
                    'C',
                    lazy(
                        t -> ofChain(
                            buildHatchAdder(MTEElementalDuplicator.class)
                                .atLeast(
                                    InputHatch,
                                    OutputBus,
                                    OutputHatch,
                                    Maintenance,
                                    Muffler,
                                    Energy,
                                    ElementalDataOrbHatch)
                                .casingIndex(Casings.ElementalConfinementShell.textureId)
                                .hint(1)
                                .build(),
                            onElementPass(x -> ++x.casingAmount, Casings.ElementalConfinementShell.asElement()))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, itemStack, 4, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 4, 4, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 120);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputHatch(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            TexturesGtBlock.oMCAElementalDuplicator,
            TexturesGtBlock.oMCAElementalDuplicatorGlow,
            TexturesGtBlock.oMCAElementalDuplicatorActive,
            TexturesGtBlock.oMCAElementalDuplicatorActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.ElementalConfinementShell.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.replicatorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F)
            .enablePerfectOverclock()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        for (MTEHatchElementalDataOrbHolder hatch : validMTEList(getElementalDataOrbHatches())) {
            ItemStack orb = hatch.getOrbByCircuit();
            logic.setSpecialSlotItem(orb);
            break;
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondElementalDuplicator;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                clearHatches();
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getStoredInputsForColor(Optional<Byte> color) {
        ArrayList<ItemStack> tItems = super.getStoredInputsForColor(Optional.empty());
        for (MTEHatchElementalDataOrbHolder tHatch : validMTEList(getElementalDataOrbHatches())) {
            byte busColor = tHatch.getBaseMetaTileEntity()
                .getColorization();
            if (color.isPresent() && busColor != -1 && busColor != color.get()) continue;
            tItems.add(tHatch.getOrbByCircuit());
        }
        tItems.removeAll(Collections.singleton(null));
        return tItems;
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
