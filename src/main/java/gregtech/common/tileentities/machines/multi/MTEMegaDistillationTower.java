package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;

public class MTEMegaDistillationTower extends MTEExtendedPowerMultiBlockBase<MTEMegaDistillationTower> {// implements
                                                                                                        // ISurvivalConstructable
                                                                                                        // {

    // todo: survival construct, distillery mode, a little gimmick, possible stat buffs, tooltip, recipes and migration
    private static final int MACHINEMODE_TOWER = 0;
    private static final int MACHINEMODE_DISTILLERY = 1;

    protected final List<List<MTEHatchOutput>> outputHatchesPerLayer = new ArrayList<>();

    protected int casingAmount;
    protected int height;
    protected boolean isTopLayerFound;

    protected static final String STRUCTURE_PIECE_BASE = "base";
    private static final int HORIZONTAL_OFFSET = 6;
    private static final int VERTICAL_OFFSET = 9;
    private static final int DEPTH_OFFSET = 0;
    private static final int LAYER_OFFSET_BASE = 9;
    private static final int LAYER_OFFSET_INCREMENT = 6;
    private static final int FINAL_LAYER_OFFSET = 12;

    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_TOP = "top";
    private static final IStructureDefinition<MTEMegaDistillationTower> STRUCTURE_DEFINITION;

    static {
        IHatchElement<MTEMegaDistillationTower> bottomLayeredOutputHatch = OutputHatch
            .withCount(MTEMegaDistillationTower::getCurrentLayerBottomOutputHatchCount)
            .withAdder(MTEMegaDistillationTower::addLayerBottomOutputHatch);
        IHatchElement<MTEMegaDistillationTower> topLayeredOutputHatch = OutputHatch
            .withCount(MTEMegaDistillationTower::getCurrentLayerBottomOutputHatchCount)
            .withAdder(MTEMegaDistillationTower::addLayerTopOutputHatch);
        IHatchElement<MTEMegaDistillationTower> finalLayeredOutputHatch = OutputHatch
            .withCount(MTEMegaDistillationTower::getFinalLayerOutputHatchCount)
            .withAdder(MTEMegaDistillationTower::addFinalLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<MTEMegaDistillationTower>builder()
            // spotless:off
            .addShape(STRUCTURE_PIECE_BASE,transpose(new String[][]{
                {"        FFF    ","     EAEFDF    ","    E   EFFFFF ","   E  H  EFFBFF","   A HCH AFBDBF","   E  H  EFFBFF","    E   E  FFF ","     EAE       ","               "},
                {"               ","     GGG D     ","    G   G      ","   G  H  G  B  ","   G HCH G BDB ","   G  H  G  B  ","    G   G      ","     GGG       ","               "},
                {"               ","     EAE D     "," F  E   E    F "," F E  H  E  BF "," FFA HCH A BDB "," F E  H  E  BF "," F  E   E    F ","     EAE       ","               "},
                {"               ","     EAE DDD   "," F  E   E    F ","H  E  H  E  B B","HHHHHHCH A BDBB","H  E  H  E  B B"," F  E   E    F ","     EAE       ","               "},
                {"               ","     GGG   D   "," F  G   G    F ","HHHHHHH  G  BBB","1CCCCCCH G BDD2","HHHHHHH  G  BBB"," F  G   G    F ","     GGG       ","               "},
                {"               ","     EAE   D   "," F  E   E    F ","H  E  H  E  B B","HHHHHHCH A BDBB","H  E  H  E  B B"," F  E   E    F ","     EAE       ","               "},
                {"     AAA   A   ","    AEEEA ADA  "," F AE   EAEA F "," FAE  H  EAEBE "," FAE HCH EABDB "," FAE  H  EAEBE "," F AE   EA   F ","    AEEEA      ","     AAA       "},
                {"  EEGEEEGEEEE  "," EEEE   EEEEEE ","EEEE     EEEEEE","EEE   H   EEBEE","AAE  HCH  EBDBA","EEE   H   EEBEE","EEEE     EEEEEE"," EEEE   EEEEEE ","  EEEEAEEEEEE  "},
                {" EEEGAAAGEGGGE "," E           E ","E             E","G     H     B G","G    HCH   BDBG","G     H     B G","E             E"," E           E "," EEEEEAEEEGGGE "},
                {" AAAGA~AGAGGGA "," A           A ","A             A","G     H     B G","G    HCH   BDBG","G     H     B G","A             A"," A           A "," AAAAAAAAAGGGA "},
                {" AEEGAAAGEGGGA "," E           E ","E             E","G     H     B G","G    HCH   BDBG","G     H     B G","E             E"," E           E "," AEEEEEEEEGGGA "},
                {" AEEGEEEGEEEEA "," EEEEEEEEEEEEE ","EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE","AEEEEEEEEEEEEEA","EEEEEEEEEEEEEEE","EEEEEEEEEEEEEEE"," EEEEEEEEEEEEE "," AEEEEEEEEEEEA "}
            }))
            .addShape(STRUCTURE_PIECE_LAYER,transpose(new String[][]{
                {"        NNN    ","     MIMNLN    ","    M   MNNNNN ","   M  P  MNNJNN","   I PKP INPLJN","   M  P  MNNJNN","    M   M  NNN ","     MIM       ","               "},
                {"               ","     OOO L     ","    O   O      ","   O  P  O  P  ","   O PKP O PKPP","   O  P  O  P  ","    O   O      ","     OOO       ","               "},
                {"               ","     MIM L     ","    M   M      ","   M  P  M  PPP","   I PKP I PKK4","   M  P  M  PPP","    M   M      ","     MIM       ","               "},
                {"               ","     MIM L     ","    M   M      ","   M  P  M  P  ","   I PKP I PKPP","   M  P  M  P  ","    M   M      ","     MIM       ","               "},
                {"               ","     OOO L     ","    O   O      ","   O  P  O  PPP","   O PKP O PKK3","   O  P  O  PPP","    O   O      ","     OOO       ","               "},
                {"               ","     MIM L     ","    M   M      ","   M  P  M  P  ","   I PKP I PKPP","   M  P  M  P  ","    M   M      ","     MIM       ","               "}
            }) )
            .addShape(
                STRUCTURE_PIECE_TOP,transpose(new String[][]{
                {"               ","               ","               ","               ","      WWWWWWW  ","               ","               ","               ","               "},
                {"               ","               ","               ","      WWWWWWW  ","     WRRRRRRRW ","      WWWWWWW  ","               ","               ","               "},
                {"               ","               ","               ","      W     W  ","     WRWWWWWRW ","      W     W  ","               ","               ","               "},
                {"               ","               ","               ","      W     W  ","     WRW   WRW ","      W     W  ","               ","               ","               "},
                {"               ","               ","      Q        ","     QWQ    W  ","    QWRWQ  WRW ","     QWQ    W  ","      Q        ","               ","               "},
                {"               ","      Q        ","     TTT       ","    TTWTT   W  ","   QTWRWTQ WRW ","    TTWTT   W  ","     TTT       ","      Q        ","               "},
                {"               ","     TQT       ","    TTTTT  UUU ","   TT W TTUUWUU","   QTWRWTQUWRWU","   TT W TTUUWUU","    TTTTT  UUU ","     TQT       ","               "},
                {"               ","     VVV       ","    V   V      ","   V  W  V  W  ","   V WRW V WRW ","   V  W  V  W  ","    V   V      ","     VVV       ","               "},
                {"               ","     TQT       ","    T   T      ","   T  W  T  W  ","   Q WRW Q WRW ","   T  W  T  W  ","    T   T      ","     TQT       ","               "},
                {"               ","     TQT       ","    T   T      ","   T  W  T  W  ","   Q WRW Q WRWW","   T  W  T  W  ","    T   T      ","     TQT       ","               "},
                {"      SSSS     ","     VSV S     ","    V   V      ","   V  W  V  WWW","   V WRW V WRR5","   V  W  V  WWW","    V   V      ","     VVV       ","               "},
                {"               ","     TQT S     ","    T   T      ","   T  W  T  W  ","   Q WRW Q WRWW","   T  W  T  W  ","    T   T      ","     TQT       ","               "}
            }))
            // spotless:on
            // base structure blocks / elements
            .addElement('A', Casings.NaquadahReinforcedDistillationCasing.asElement())
            .addElement('B', Casings.SolidSteelMachineCasing.asElement())
            .addElement('C', Casings.BronzePipeCasing.asElement())
            .addElement('D', Casings.SteelPipeCasing.asElement())
            .addElement(
                'E',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(Maintenance, Energy, InputBus)
                    .casingIndex(Casings.CleanStainlessSteelMachineCasing.textureId)
                    .hint(1)
                    .buildAndChain(
                        onElementPass(
                            MTEMegaDistillationTower::onCasingAdded,
                            Casings.CleanStainlessSteelMachineCasing.asElement())))
            .addElement('F', ofSheetMetal(Materials.StainlessSteel))
            .addElement('G', ofSheetMetal(Materials.Naquadah))
            .addElement('H', Casings.StrongBronzeMachineCasing.asElement())
            .addElement(
                '1',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(InputHatch)
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(1)
                    .buildAndChain(Casings.NaquadahReinforcedDistillationCasing.asElement()))
            .addElement(
                '2',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(OutputBus)
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(2)
                    .buildAndChain(Casings.NaquadahReinforcedDistillationCasing.asElement()))
            // second layer blocks / elements
            .addElement('I', Casings.NaquadahReinforcedDistillationCasing.asElement())
            .addElement('J', Casings.SolidSteelMachineCasing.asElement())
            .addElement('K', Casings.BronzePipeCasing.asElement())
            .addElement('L', Casings.SteelPipeCasing.asElement())
            .addElement('M', Casings.CleanStainlessSteelMachineCasing.asElement())
            .addElement('N', ofSheetMetal(Materials.StainlessSteel))
            .addElement('O', ofSheetMetal(Materials.Naquadah))
            .addElement('P', Casings.StrongBronzeMachineCasing.asElement())
            .addElement(
                '3',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(bottomLayeredOutputHatch)
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(3)
                    .buildAndChain(Casings.NaquadahReinforcedDistillationCasing.asElement()))
            .addElement(
                '4',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(topLayeredOutputHatch)
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(4)
                    .buildAndChain(Casings.NaquadahReinforcedDistillationCasing.asElement()))
            // top layer blocks / elements
            .addElement('Q', Casings.NaquadahReinforcedDistillationCasing.asElement())
            .addElement('R', Casings.BronzePipeCasing.asElement())
            .addElement('S', Casings.SteelPipeCasing.asElement())
            .addElement('T', Casings.CleanStainlessSteelMachineCasing.asElement())
            .addElement('U', ofSheetMetal(Materials.StainlessSteel))
            .addElement('V', ofSheetMetal(Materials.Naquadah))
            .addElement('W', Casings.StrongBronzeMachineCasing.asElement())
            .addElement(
                '5',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(finalLayeredOutputHatch)
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(5)
                    .buildAndChain(Casings.NaquadahReinforcedDistillationCasing.asElement()))
            .build();
    }

    public MTEMegaDistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaDistillationTower(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaDistillationTower(this.mName);
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity bastMTE, ItemStack stack, List<StructureError> errors) {
        this.outputHatchesPerLayer.forEach(List::clear);
        this.height = 1;
        this.isTopLayerFound = false;

        if (!checkPiece(STRUCTURE_PIECE_BASE, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) return;

        List<Integer> missingLayers = new ArrayList<>();

        while (this.height <= 5) {
            if (!checkPiece(
                STRUCTURE_PIECE_LAYER,
                HORIZONTAL_OFFSET,
                LAYER_OFFSET_BASE + (LAYER_OFFSET_INCREMENT * height),
                DEPTH_OFFSET,
                errors)) {
                // if this failed, check for top layer instead. if top layer fails, return. otherwise break out of the
                // loop.
                if (!checkPiece(
                    STRUCTURE_PIECE_TOP,
                    HORIZONTAL_OFFSET,
                    FINAL_LAYER_OFFSET + LAYER_OFFSET_BASE + (LAYER_OFFSET_INCREMENT * (height)),
                    DEPTH_OFFSET,
                    errors)) return;
                int topHatchIndex = this.height * 2;
                if (this.outputHatchesPerLayer.size() < topHatchIndex + 1
                    || this.outputHatchesPerLayer.get(topHatchIndex)
                        .isEmpty()) {
                    missingLayers.add(height + 1);
                }
                break;
            }

            // there are 5 total middle layers that output hatches may be on, exlcuding the top layer
            int outputHatchLayers = this.height * 2; // this is the amount of output hatches expected
            // for a height of 1, output hatches would be at index 0 and index 1. ie expected -1, expected -2.
            int bottomLayerHatch = outputHatchLayers - 2;
            int topLayerHatch = outputHatchLayers - 1;

            if (this.outputHatchesPerLayer.size() < outputHatchLayers
                || this.outputHatchesPerLayer.get(bottomLayerHatch)
                    .isEmpty()
                || this.outputHatchesPerLayer.get(topLayerHatch)
                    .isEmpty()) {
                missingLayers.add(height);
            }
            height++;
        }

        if (!missingLayers.isEmpty()) {
            errors.add(StructureErrors.missingOutputHatchDT(missingLayers));
        }

        checkCasingMin(errors, casingAmount, 20);
        checkOneMaintenanceHatch(errors);
        checkHasAnyEnergy(errors);
        checkHasInputHatch(errors);

    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
        int totalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 1, 5);
        for (int currentLayer = 1; currentLayer <= totalHeight; currentLayer++) {
            buildPiece(
                STRUCTURE_PIECE_LAYER,
                stackSize,
                hintsOnly,
                HORIZONTAL_OFFSET,
                LAYER_OFFSET_BASE + LAYER_OFFSET_INCREMENT * currentLayer,
                DEPTH_OFFSET);
        }
        buildPiece(
            STRUCTURE_PIECE_TOP,
            stackSize,
            hintsOnly,
            HORIZONTAL_OFFSET,
            FINAL_LAYER_OFFSET + LAYER_OFFSET_BASE + LAYER_OFFSET_INCREMENT * totalHeight,
            DEPTH_OFFSET);

    }

    // each middle layer is composed of a bottom and a top hatch. for height h, the bottom hatch layer is at index 2h-2
    // the top hatch layer is at index 2h-1. the final hatch layer is at index 2h.

    protected int getCurrentLayerBottomOutputHatchCount() {
        int currentLayer = (height * 2) - 2;
        return outputHatchesPerLayer.size() < currentLayer || height <= 0 ? 0
            : outputHatchesPerLayer.get(currentLayer)
                .size();
    }

    protected int getCurrentLayerTopOutputHatchCount() {
        int currentLayer = (height * 2) - 1;
        return outputHatchesPerLayer.size() < currentLayer || height <= 0 ? 0
            : outputHatchesPerLayer.get(currentLayer)
                .size();
    }

    protected int getFinalLayerOutputHatchCount() {
        int currentLayer = height * 2 + 1; // in a max dt (height 5), this is index 10. so height*2
        return outputHatchesPerLayer.size() < currentLayer + 1 || height <= 0 ? 0
            : outputHatchesPerLayer.get(currentLayer)
                .size();
    }

    protected boolean addLayerBottomOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput tHatch)) return false;
        int hatchLayer = (height * 2) - 2;
        while (outputHatchesPerLayer.size() <= hatchLayer) outputHatchesPerLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return outputHatchesPerLayer.get(hatchLayer)
            .add(tHatch);
    }

    protected boolean addLayerTopOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput tHatch)) return false;
        int hatchLayer = (height * 2) - 1;
        while (outputHatchesPerLayer.size() <= hatchLayer) outputHatchesPerLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return outputHatchesPerLayer.get(hatchLayer)
            .add(tHatch);
    }

    protected boolean addFinalLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()
            || !(aTileEntity.getMetaTileEntity() instanceof MTEHatchOutput tHatch)) return false;
        int hatchLayer = height * 2;
        while (outputHatchesPerLayer.size() <= hatchLayer) outputHatchesPerLayer.add(new ArrayList<>());
        tHatch.updateTexture(aBaseCasingIndex);
        return outputHatchesPerLayer.get(hatchLayer)
            .add(tHatch);
    }

    @Override
    public IStructureDefinition<MTEMegaDistillationTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public boolean canDumpFluidToME() {

        // All fluids can be dumped to ME only if each layer contains a ME Output Hatch.
        for (List<MTEHatchOutput> tLayerOutputHatches : this.outputHatchesPerLayer) {

            boolean foundMEHatch = false;

            for (IFluidStore tHatch : tLayerOutputHatches) {
                if (tHatch instanceof MTEHatchOutputME tMEHatch) {
                    if (tMEHatch.canAcceptFluid()) {
                        foundMEHatch = true;
                        break;
                    }
                }
            }

            // Exit if we didn't find a valid hatch on this layer.
            if (!foundMEHatch) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] outputFluids) {
        for (int i = 0; i < outputFluids.length && i < this.outputHatchesPerLayer.size(); i++) {
            FluidStack tStack = outputFluids[i].copy();
            if (!dumpFluid(this.outputHatchesPerLayer.get(i), tStack, true))
                dumpFluid(this.outputHatchesPerLayer.get(i), tStack, false);
        }
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return this.getFluidOutputSlotsByLayer(toOutput, this.outputHatchesPerLayer);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.distillationTowerRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Centrifuge")
            .addBulkMachineInfo(1, 1, 1)
            .addInfo("Disable animations with a screwdriver")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 5, true)
            .addController("Front center")
            .addCasingInfoMin("Centrifuge Casing", 6, false)
            .addCasingInfoExactly("Large Sieve Grate", 18, false)
            .addCasingInfoExactly("Eglin Steel Frame Box", 24, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Ducked")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId()) };
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

}
