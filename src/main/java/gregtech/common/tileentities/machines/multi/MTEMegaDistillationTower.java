package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_GLOW;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DISTILLATION_TOWER;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DISTILLING;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;

public class MTEMegaDistillationTower extends MTEExtendedPowerMultiBlockBase<MTEMegaDistillationTower>
    implements ISurvivalConstructable, ICasingTextureProvider {

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
            .withCount(MTEMegaDistillationTower::getCurrentLayerTopOutputHatchCount)
            .withAdder(MTEMegaDistillationTower::addLayerTopOutputHatch);
        IHatchElement<MTEMegaDistillationTower> finalLayeredOutputHatch = OutputHatch
            .withCount(MTEMegaDistillationTower::getFinalLayerOutputHatchCount)
            .withAdder(MTEMegaDistillationTower::addFinalLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<MTEMegaDistillationTower>builder()
            // spotless:off
            .addShape(STRUCTURE_PIECE_BASE,transpose(new String[][]{
                {"        FFF    ","     EAEFDF    ","    E F EFFFFF ","   E  H  EFFBFF","   AFHCHFAFBDBF","   E  H  EFFBFF","    E F E  FFF ","     EAE       ","               "},
                {"               ","     GGG D     ","    G   G      ","   G  H  G  B  ","   G HCH G BDB ","   G  H  G  B  ","    G   G      ","     GGG       ","               "},
                {"               ","     EAE D     "," F  E   E    F "," F E  H  E  BF "," FFA HCH A BDB "," F E  H  E  BF "," F  E   E    F ","     EAE       ","               "},
                {"               ","     EAE DDD   "," F  E   E    F ","H  E  H  E  B B","HHHHHHCH A BDBB","H  E  H  E  B B"," F  E   E    F ","     EAE       ","               "},
                {"               ","     GGG   D   "," F  G   G    F ","HHHHHHH  G  BBB","3CCCCCCH G BDD2","HHHHHHH  G  BBB"," F  G   G    F ","     GGG       ","               "},
                {"               ","     EAE   D   "," F  E   E    F ","H  E     E  B B","HHHHHHH  A BDBB","H  E     E  B B"," F  E   E    F ","     EAE       ","               "},
                {"     GGG   G   ","    GEAEG GDG  "," F GE   EGEGEF "," FGE     EGEBE "," FAA     AGBDB "," FGE     EGEBE "," F GE   EG   F ","    GEAEG      ","     GGG       "},
                {"  EEG111GE111  "," EEEE   EEEDEE ","EEEE     EEEEEE","1EE       EEBE1","111       1BDB1","1EE       EEBE1","EEEE     EE1EEE"," EEEE   EEE1EE ","  EEG111GE111  "},
                {" GEEG111GEGGGE "," E         D E ","EE           EE","G             G","G           D G","G             G","EE           EE"," E           E "," GEEG111GEGGGE "},
                {" G11G1~1G1GGG1 "," 1  CCCCC  D 1 ","11         D 11","G   CCCCC  D  G","G          DD G","G   CCCCC  D  G","11         D 11"," 1  CCCCC    1 "," G11G111G1GGG1 "},
                {" GEEG111GEGGGE "," E  C   C  D E ","EE           EE","G   C   C     G","G             G","G   C   C     G","EE         D EE"," E  C   C    E "," GEEG111GEGGGE "},
                {" GEEG111GE111E "," EEEEE1EEEE1EE ","EEEEEE1EEEE1EEE","1EEEEE1EEEE1EE1","111111111111111","1EEEEE1EEEE1EE1","EEEEEE1EEEE1EEE"," EEEEE1EEEE1EE "," GEEG111GE111E "}
            }))
            .addShape(STRUCTURE_PIECE_LAYER,transpose(new String[][]{
                {"        FFF    ","     EAEFDF    ","    E F EFFFFF ","   E  H  EFFHFF","   AFHCHFAFHCHF","   E  H  EFFHFF","    E F E  FFF ","     EAE       ","               "},
                {"               ","     GGG D     ","    G   G      ","   G  H  G  H  ","   G HCH G HCHH","   G  H  G  H  ","    G   G      ","     GGG       ","               "},
                {"               ","     EAE D     ","    E   E      ","   E  H  E  HHH","   A HCH A HCC5","   E  H  E  HHH","    E   E      ","     EAE       ","               "},
                {"               ","     EAE D     ","    E   E      ","   E  H  E  H  ","   A HCH A HCHH","   E  H  E  H  ","    E   E      ","     EAE       ","               "},
                {"               ","     GGG D     ","    G   G      ","   G  H  G  HHH","   G HCH G HCC4","   G  H  G  HHH","    G   G      ","     GGG       ","               "},
                {"               ","     EAE D     ","    E   E      ","   E  H  E  H  ","   A HCH A HCHH","   E  H  E  H  ","    E   E      ","     EAE       ","               "}
            }) )
            .addShape(
                STRUCTURE_PIECE_TOP,transpose(new String[][]{
                    {"               ","               ","               ","               ","      HHHHHHH  ","               ","               ","               ","               "},
                    {"               ","               ","               ","      HHHHHHH  ","     HCCCCCCCH ","      HHHHHHH  ","               ","               ","               "},
                    {"               ","               ","               ","      H     H  ","     HCHHHHHCH ","      H     H  ","               ","               ","               "},
                    {"               ","               ","               ","      H     H  ","     HCH   HCH ","      H     H  ","               ","               ","               "},
                    {"               ","               ","      A        ","     AHA    H  ","    AHCHA  HCH ","     AHA    H  ","      A        ","               ","               "},
                    {"               ","      A        ","     EEE       ","    EEHEE   H  ","   AEHCHEA HCH ","    EEHEE   H  ","     EEE       ","      A        ","               "},
                    {"               ","     EAE       ","    EEEEE  FFF ","   EE H EEFFHFF","   AEHCHEAFHCHF","   EE H EEFFHFF","    EEEEE  FFF ","     EAE       ","               "},
                    {"               ","     GGG       ","    G   G      ","   G  H  G  H  ","   G HCH G HCH ","   G  H  G  H  ","    G   G      ","     GGG       ","               "},
                    {"               ","     EAE       ","    E   E      ","   E  H  E  H  ","   A HCH A HCH ","   E  H  E  H  ","    E   E      ","     EAE       ","               "},
                    {"               ","     EAE       ","    E   E      ","   E  H  E  H  ","   A HCH A HCHH","   E  H  E  H  ","    E   E      ","     EAE       ","               "},
                    {"      DDDD     ","     GDG D     ","    G   G      ","   G  H  G  HHH","   G HCH G HCC6","   G  H  G  HHH","    G   G      ","     GGG       ","               "},
                    {"               ","     EAE D     ","    E   E      ","   E  H  E  H  ","   A HCH A HCHH","   E  H  E  H  ","    E   E      ","     EAE       ","               "}
                }))
            // spotless:on
            // base structure blocks / elements
            .addElement(
                'A',
                onElementPass(
                    MTEMegaDistillationTower::onCasingAdded,
                    Casings.NaquadahReinforcedDistillationCasing.asElement()))
            .addElement('B', Casings.SolidSteelMachineCasing.asElement())
            .addElement('C', Casings.BronzePipeCasing.asElement())
            .addElement('D', Casings.SteelPipeCasing.asElement())
            .addElement('E', Casings.CleanStainlessSteelMachineCasing.asElement())
            .addElement('F', ofFrame(Materials.StainlessSteel))
            .addElement('G', ofSheetMetal(Materials.Naquadah))
            .addElement('H', Casings.StrongBronzeMachineCasing.asElement())
            // first slice hatches
            .addElement(
                '1',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(Maintenance, InputBus, Energy.or(ExoticEnergy))
                    .casingIndex(Casings.NaquadahReinforcedDistillationCasing.textureId)
                    .hint(1)
                    .buildAndChain(
                        onElementPass(
                            MTEMegaDistillationTower::onCasingAdded,
                            Casings.NaquadahReinforcedDistillationCasing.asElement())))
            .addElement(
                '2',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(OutputBus)
                    .casingIndex(Casings.SteelPipeCasing.textureId)
                    .hint(2)
                    .buildAndChain(Casings.SteelPipeCasing.asElement()))
            .addElement(
                '3',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(InputHatch)
                    .casingIndex(Casings.BronzePipeCasing.textureId)
                    .hint(3)
                    .buildAndChain(Casings.BronzePipeCasing.asElement()))
            // middle slice hatches
            .addElement(
                '4',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(bottomLayeredOutputHatch)
                    .casingIndex(Casings.BronzePipeCasing.textureId)
                    .hint(4)
                    .buildAndChain(Casings.BronzePipeCasing.asElement()))
            .addElement(
                '5',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(topLayeredOutputHatch)
                    .casingIndex(Casings.BronzePipeCasing.textureId)
                    .hint(5)
                    .buildAndChain(Casings.BronzePipeCasing.asElement()))
            // top slice hatch
            .addElement(
                '6',
                buildHatchAdder(MTEMegaDistillationTower.class).atLeast(finalLayeredOutputHatch)
                    .casingIndex(Casings.BronzePipeCasing.textureId)
                    .hint(6)
                    .buildAndChain(Casings.BronzePipeCasing.asElement()))
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
    public void checkMachine(IGregTechTileEntity baseMTE, ItemStack stack, List<StructureError> errors) {
        this.outputHatchesPerLayer.forEach(List::clear);
        this.casingAmount = 0;
        this.height = 1;
        this.isTopLayerFound = false;
        if (!checkPiece(STRUCTURE_PIECE_BASE, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET, errors)) return;

        List<Integer> missingLayers = new ArrayList<>();

        while (this.height <= 5) {

            if (this.isTopLayerFound) {
                break; // needed to break out of the loop in the case the structure isn't max height.
            }

            if (checkPiece(
                STRUCTURE_PIECE_TOP,
                HORIZONTAL_OFFSET,
                FINAL_LAYER_OFFSET + LAYER_OFFSET_BASE + (LAYER_OFFSET_INCREMENT * (height)),
                DEPTH_OFFSET,
                null)) {
                this.isTopLayerFound = true;
                int topHatchIndex = (this.height) * 2;
                if (this.outputHatchesPerLayer.size() < topHatchIndex + 1
                    || this.outputHatchesPerLayer.get(topHatchIndex)
                        .isEmpty()) {
                    missingLayers.add(height + 1);
                }
            }
            if (!checkPiece(
                STRUCTURE_PIECE_LAYER,
                HORIZONTAL_OFFSET,
                LAYER_OFFSET_BASE + (LAYER_OFFSET_INCREMENT * height),
                DEPTH_OFFSET,
                errors)) return;

            // there are 5 total middle layers that output hatches may be on, excluding the top layer
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

        checkCasingMin(errors, casingAmount, 150);
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

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        this.height = 0; // required for nei preview to not crash on out of index. (hatch adder being out of bounds)
        int built = this.survivalBuildPiece(
            STRUCTURE_PIECE_BASE,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            realBudget,
            env,
            false,
            true);
        if (built >= 0) return built;

        int totalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 1, 5);

        for (int currentLayer = 1; currentLayer <= totalHeight; currentLayer++) {
            built = this.survivalBuildPiece(
                STRUCTURE_PIECE_LAYER,
                stackSize,
                HORIZONTAL_OFFSET,
                LAYER_OFFSET_BASE + LAYER_OFFSET_INCREMENT * currentLayer,
                DEPTH_OFFSET,
                realBudget,
                env,
                false,
                true);
            if (currentLayer == 5) {
                // workaround as for some reason highest middle level was not building the top piece
                built += this.survivalBuildPiece(
                    STRUCTURE_PIECE_TOP,
                    stackSize,
                    HORIZONTAL_OFFSET,
                    FINAL_LAYER_OFFSET + LAYER_OFFSET_BASE + LAYER_OFFSET_INCREMENT * currentLayer,
                    DEPTH_OFFSET,
                    realBudget,
                    env,
                    false,
                    true);
                return built;
            }
            if (built >= 0) return built;
        }
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_TOP,
            stackSize,
            HORIZONTAL_OFFSET,
            FINAL_LAYER_OFFSET + LAYER_OFFSET_BASE + LAYER_OFFSET_INCREMENT * totalHeight,
            DEPTH_OFFSET,
            realBudget,
            env,
            false,
            true);
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
        int currentLayer = height * 2; // in a max dt (height 5), this is index 10. so height*2
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
    protected boolean addFluidOutputs(FluidStack[] outputFluids) {
        boolean succeed = true;
        for (int i = 0; i < outputFluids.length && i < this.outputHatchesPerLayer.size(); i++) {
            FluidStack stack = outputFluids[i].copy();
            addOutputPartial(stack, outputHatchesPerLayer.get(i));
            if (stack.amount > 0) succeed = false;
        }
        return succeed;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        return this.getFluidOutputSlotsByLayer(toOutput, this.outputHatchesPerLayer);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatTrans(aPlayer, "GT5U.MULTI_MACHINE_CHANGE", new ChatComponentTranslation(getMachineModeKey()));
    }

    @Override
    public int nextMachineMode() {
        if (this.machineMode == MACHINEMODE_DISTILLERY) return MACHINEMODE_TOWER;
        return MACHINEMODE_DISTILLERY;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<>(this)
            .withMachineModeIcons(OVERLAY_BUTTON_MACHINEMODE_DISTILLATION_TOWER, OVERLAY_BUTTON_MACHINEMODE_DISTILLING);
    }

    @Override
    public String getMachineModeKey() {
        if (this.machineMode == MACHINEMODE_DISTILLERY) return "GT5U.MDT.mode.1";
        return "GT5U.MDT.mode.0";
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return machineMode == MACHINEMODE_TOWER ? RecipeMaps.distillationTowerRecipes : RecipeMaps.distilleryRecipes;
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.distillationTowerRecipes, RecipeMaps.distilleryRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public int getMaxParallelRecipes() {
        if (this.machineMode == MACHINEMODE_DISTILLERY) {
            // 512 - 1024 parallels min to max height
            return Configuration.Multiblocks.megaMachinesMax * (1 + this.height / 2);
        }
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    private static final float DISTILLERY_SPEED = 1.5f;
    private static final float DISTILLERY_EU_EFFICIENCY = 0.5f;

    private static final float TOWER_SPEED = 1.2f;
    private static final float TOWER_EU_EFFICIENCY = 0.9f;

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(this.getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
        if (this.machineMode == MACHINEMODE_DISTILLERY) {
            // make it compete with dangote somewhat. it will still be less eu efficient. numbers can be tweaked
            logic.setSpeedBonus(DISTILLERY_SPEED);
            logic.setEuModifier(DISTILLERY_EU_EFFICIENCY);
        } else {
            // same here, still worse than dangote but with laser
            logic.setSpeedBonus(TOWER_SPEED);
            logic.setEuModifier(TOWER_EU_EFFICIENCY);
        }
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

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_DISTILLERY_LOOP;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Distillery, DT, MDT")
            .addInfo("Stats dictated by tower mode, change mode in GUI or with a screwdriver")
            .addInfo("Has up to 5 middle slices and 1 top slice, the amount of middle slices is the 'Tower Height'")
            .addInfo("Each middle slice adds 2 output hatches, the top slice adds one output hatch")
            .addSeparator()
            .addInfo("Distillery Mode")
            .addInfo(
                TooltipHelper.parallelText(Configuration.Multiblocks.megaMachinesMax + " * (1 + Tower Height/2)")
                    + " Parallels")
            .addStaticSpeedInfo(DISTILLERY_SPEED)
            .addStaticEuEffInfo(DISTILLERY_EU_EFFICIENCY)
            .addInfo("Fluids output to the first hatch only")
            .addSeparator()
            .addInfo("Distillation Tower Mode")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addStaticSpeedInfo(TOWER_SPEED)
            .addStaticEuEffInfo(TOWER_EU_EFFICIENCY)
            .addInfo("Fluids output to their corresponding layer only")
            .addSeparator()
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + "Big Oil will be pleased with this!")
            .beginVariableStructureBlock(15, 15, 30, 54, 9, 9, true)
            .addController("Front off-center, 3rd Layer")
            .addStructureInfo(EnumChatFormatting.BLUE + "Base Structure. 1 Middle Slice:")
            .addCasingInfoMin("Naquadah Reinforced Distillation Machine Casing", 100, false)
            .addCasingInfoExactly("Naquadah Sheetmetal", 179, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 360, false)
            .addCasingInfoExactly("Stainless Steel Framebox", 99, false)
            .addCasingInfoExactly("Bronze Pipe Casing", 84, false)
            .addCasingInfoExactly("Strong Bronze Machine Casing", 215, false)
            .addCasingInfoExactly("Steel Pipe Casing", 44, false)
            .addCasingInfoExactly("Solid Steel Machine Casing", 41, false)
            .addStructureInfo(EnumChatFormatting.BLUE + "Additional Middle Slices:")
            .addCasingInfoExactly("Naquadah Reinforced Distillation Machine Casing", 16, false)
            .addCasingInfoExactly("Naquadah Sheetmetal", 32, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 48, false)
            .addCasingInfoExactly("Stainless Steel Framebox", 27, false)
            .addCasingInfoExactly("Bronze Pipe Casing", 16, false)
            .addCasingInfoExactly("Strong Bronze Machine Casing", 57, false)
            .addCasingInfoExactly("Steel Pipe Casing", 6, false)
            .addInputBus("Any Naquadah Distillation Casing in the first 5 layers", 1)
            .addEnergyHatch("Any Naquadah Distillation Casing in the first 5 layers", 1)
            .addMaintenanceHatch("Any Naquadah Distillation Casing in the first 5 layers", 1)
            .addOutputBus("Bottom Slice, Steel Pipe Casing, 8th layer, furthest right", 2)
            .addInputHatch("Bottom Slice, Bronze Pipe Casing, 8th layer, furthest left", 3)
            .addOutputHatch("Middle Slices & Top Slice, Bronze Pipe Casing, furthest right", 4, 5, 6)
            .addSubChannelUsage(GTStructureChannels.STRUCTURE_HEIGHT)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Mallady")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_MEGA_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_MEGA_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(Casings.NaquadahReinforcedDistillationCasing.getTextureId());
    }
}
