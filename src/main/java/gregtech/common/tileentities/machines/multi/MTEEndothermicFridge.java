package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;

public class MTEEndothermicFridge extends MTEExtendedPowerMultiBlockBase<MTEEndothermicFridge>
    implements ISurvivalConstructable {

    // todo: some structure tweaks, mechanic, old t2 integration, tooltip
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 2;
    private static final int VERTICAL_OFFSET = 10;
    private static final int DEPTH_OFFSET = 6;
    private static final int TEXTURE_ID = Casings.FridgeCasing.getTextureId();
    private static final IStructureDefinition<MTEEndothermicFridge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEndothermicFridge>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
         transpose(new String[][]{
             {"                   ","                   ","                   ","                   ","           B       ","        CBBBBBC    ","       BCBBBBBCB   ","      BBCBBBBBCBB  ","       BCBBBBBCB   ","        CBBBBBC    ","           B       ","                   ","                   ","                   ","                   "},
             {"                   ","                   ","                   ","         BBBBB     ","       BCBBBBBCB   ","      BBCBBBBBCBB  ","      B         B  ","     BB         BB ","      B         B  ","      BBCBBBBBCBB  ","       BCBBBBBCB   ","         BBBBB     ","                   ","                   ","                   "},
             {"                   ","                   ","           B       ","       BCBBBBBCB   ","      BBCBBBBBCBB  ","     B           B ","     B           BD","     B           BD","     B           BD","     B           B ","      BBCBBBBBCBB  ","       BCBBBBBCB   ","           B       ","                   ","                   "},
             {"                   ","                   ","        CBBBBBC    ","      BBCBBBBBCBB  ","     B           B ","     B           BD","     B           B ","     B           B ","     B           B ","     B           BD","     B           B ","      BBCBBBBBCBB  ","        CBBBBBC    ","                   ","                   "},
             {"                   ","                   ","       BCBAAABCB   ","      B         B  ","     B           BD","     B           B ","    BB           BH","  CCBC           B ","    BB           BH","     B           B ","     B           BD","      B         B  ","       BCBAAABCB   ","                   ","                   "},
             {"                   ","                   ","      BBCBAAABCBB  ","     BB         BB ","     B           BD","     B           B ","  CCBC           B "," CCCCC           CH","  CCBC           B ","     B           B ","     B           BD","     BB         BB ","      BBCBAAABCBB  ","                   ","                   "},
             {"                   ","        E     E    ","       BCBAAABCB   ","      B         B  ","     B           BD","     B           B ","  C BB           BH"," CCCBC           B ","  C BB           BH","     B           B ","     B           BD","      B         B  ","       BCBAAABCB   ","        E     E    ","                   "},
             {"                   ","        E     E    ","       ECBBBBBCE   ","      BBCBBBBBCBB  ","     B           B ","     B           BD","  C  B           B "," CCC B           B ","  C  B           B ","     B           BD","     B           B ","      BBCBBBBBCBB  ","       ECBBBBBCE   ","        E     E    ","                   "},
             {"                   ","        E     E    ","       EEE B EEE   ","       BCBBBBBCB   ","      BBCBBBBBCBB  ","     B           B ","  C  B           BD"," CCC B           BD","  C  B           BD","     B           B ","      BBCBBBBBCBB  ","       BCBBBBBCB   ","       EEE B EEE   ","        E     E    ","                   "},
             {"                   ","        E     E    ","       EEE   EEE   ","        EBBBBBE    ","       BCBBBBBCB   ","      BBCBBBBBCBB  "," HHH  B         B  "," HCH BB         BB "," HHH  B         B  ","      BBCBBBBBCBB  ","       BCBBBBBCB   ","        EBBBBBE    ","       EEE   EEE   ","        E     E    ","                   "},
             {"                   ","        E     E    ","       EEE   EEE   ","        E     E    ","        E  B  E    ","        CBBBBBC    "," B~B   BCBBBBBCB   "," BCB  BBCBBBBBCBB  "," BBB   BCBBBBBCB   ","        CBBBBBC    ","        E  B  E    ","        E     E    ","       EEE   EEE   ","        E     E    ","                   "},
             {"                   ","        E     E    ","       EEE   EEE   ","        E     E    ","        E     E    ","        EE   EE    "," GGG    E E E E    "," GGG    E  E  E    "," GGG    E E E E    ","        EE   EE    ","        E     E    ","        E     E    ","       EEE   EEE   ","        E     E    ","                   "},
             {"    FFFFFFFFFFF    ","  FFFFFFFFFFFFFFF  "," FFFFFFFFFFFFFFFFF ","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF","FFFFFFFFFFFFFFFFFFF"," FFFFFFFFFFFFFFFFF ","  FFFFFFFFFFFFFFF  ","    FFFFFFFFFFF    "}
         }))
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', Casings.FridgeCasing.asElement())
        .addElement('C', Casings.TungstensteelPipeCasing.asElement())
        .addElement('D', Casings.RobustTungstenSteelMachineCasing.asElement())
        .addElement('E', ofFrame(Materials.Osmiridium))
        .addElement('F', Casings.TungstenSteelReinforcedBlock.asElement())
        .addElement('G', Casings.NaquadahReinforcedBlock.asElement())
        .addElement('H', Casings.OsmiumItemPipeCasing.asElement())
        .addElement(
            'B',
            buildHatchAdder(MTEEndothermicFridge.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(15))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEEndothermicFridge::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 15))))
        .addElement('A', chainAllGlasses())
        .addElement('C', ofFrame(Materials.Steel))
        .build();

    public MTEEndothermicFridge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEndothermicFridge(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEEndothermicFridge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEndothermicFridge(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Brewery, BBB")
            .addBulkMachineInfo(4, 1.5F, 1F)
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    private int casingAmount;
    private int machineTier;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET) && casingAmount >= 14;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
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

}
