package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
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
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEEndothermicFridge extends MTEExtendedPowerMultiBlockBase<MTEEndothermicFridge>
    implements ISurvivalConstructable {

    // todo: some structure tweaks, mechanic, old t2 integration, tooltip
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 11;
    private static final int VERTICAL_OFFSET = 12;
    private static final int DEPTH_OFFSET = 3;
    private static final int TEXTURE_ID = Casings.FridgeCasing.getTextureId();
    private static final IStructureDefinition<MTEEndothermicFridge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEndothermicFridge>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "          HHH          "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "       HHHCCCHHH       "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "     HHCCCCCCCCCHH     "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "          CCC          ",
                "          C~C          ",
                "          CCC          ",
                "          FJF          ",
                "    HCCCCCHJHCCCCCH    "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "           E           ",
                "           E           ",
                "          CEC          ",
                "          CEC          ",
                "       FFJFJFJFF       ",
                "   HCCCHHHHCHHHHCCCH   "
            },{
                "                       ",
                "                       ",
                "                       ",
                "          DDD          ",
                "        DEFFFED        ",
                "       DDE   EDD       ",
                "       DFE   EFD       ",
                "       DDE   EDD       ",
                "        DEFFFED        ",
                "          DDD          ",
                "           E           ",
                "                       ",
                "                       ",
                "       E       E       ",
                "      FEFFJJJFFEF      ",
                "  HCCCHHHHCCCHHHHCCCH  "
            },{
                "                       ",
                "          CCC          ",
                "       CCECCCECC       ",
                "      CCCECCCECCC      ",
                "      CCCEDDDECCC      ",
                "     CCCFDAAADFCCC     ",
                "    CCCCDDAAADDCCCC    ",
                "     CCCFDAAADFCCC     ",
                "     GCCCEDDDECCCG     ",
                "     GCCCECCCECCCG     ",
                "     G CCECECECC G     ",
                "     G    CCC    G     ",
                "     G           G     ",
                "     G    JJJJ   G     ",
                "     GJFJJJJJJJFJG     ",
                "  HCCHJHCHJCJHCHJHCCH  "
            },{
                "                       ",
                "       CCECCCECC       ",
                "     CCCCGGGGGCCCC     ",
                "     CC         CC     ",
                "    CC           CC    ",
                "   CCC           CCC   ",
                "   CCDDD       DDDCC   ",
                "   CCC           CCC   ",
                "    CC           CC    ",
                "    GCC         CCG    ",
                "    GCCCCGGGGGCCCCG    ",
                "    GIGCCECCCECCGIG    ",
                "    GI           IG    ",
                "    GI  G     G  IG    ",
                "    GIGJJJJJJJJJGIG    ",
                " HCCHHHJJCJCJCJJHHHCCH "
            },{
                "         ECCCE         ",
                "      CCCECCCECCC      ",
                "    CCCGG     GGCCC    ",
                "    C             C    ",
                "   CC             CC   ",
                "  FC               CF  ",
                "  FCD             DCF  ",
                "  FC               CF  ",
                "   CC             CC   ",
                "    C             C    ",
                "    CCCGG     GGCCC    ",
                "     GCCCECCCECCCG     ",
                "         ECCCE         ",
                "       G       G       ",
                "    FGJJJJJJJJJJJGF    ",
                " HCCHHCJJJCCCJJJCHHCCH "
            },{
                "        CEFFFEC        ",
                "      CCCFDDDFCCCC     ",
                "    CCG         GCC    ",
                "   CC             CC   ",
                "  FC               CF  ",
                "  EC               CE  ",
                "  EDI             IDE  ",
                "  EC               CE  ",
                "  FC               CF  ",
                "   CC             CC   ",
                "    CCG         GCC    ",
                "     CCCCFDDDFCCCC     ",
                "        CEFFFEC        ",
                "      J      G  J      ",
                "    JFJJJJJJJJJJJFJ    ",
                " HCCHHHCJCJCJCJCHHHCCH "
            },{
                "       CCECCCECC       ",
                "     CCCFDDDDDFCCC     ",
                "   CCCG         GCCC   ",
                "  FC               CF  ",
                "  EC               CE  ",
                "   E               E   ",
                "  GFI             IFG  ",
                "   E               E   ",
                "  EC               CE  ",
                "  FC               CF  ",
                "   CCCG         GCCC   ",
                "     CCCFDEEEDFCCC     ",
                "       CCEIIIECC       ",
                "      J  G   G  J      ",
                "   FFJJJJJJJJJJJJJFF   ",
                "HCCHHCJJCJCCCJCJJCHHCCH"
            },{
                "       CCECCCECC       ",
                "    CCCCDDDDDDDCCCC    ",
                "   CCDDD       DDDCC   ",
                "  FCD             DCF  ",
                "  EDI             IDE  ",
                "  GFI             IFG  ",
                "  BBB             BBBB ",
                "  GFI             IFG  ",
                "  EDI             IDE  ",
                "  FCD             DCF  ",
                "   CCDDD   B   DDDCC   ",
                "    CCCCDDEBEDDCCCC    ",
                "       CCEIBIECC       ",
                "      J  G B G  J      ",
                "   JJJJJJJJBJJJJJJJJ   ",
                "HCCJCCCCCCCCCCCCCCCJCCH"
            },{
                "       CCECCCECC       ",
                "     CCCFDDDDDFCCC     ",
                "   CCCG         GCCC   ",
                "  FC               CF  ",
                "  EC               CE  ",
                "   E               E   ",
                "  GFI             IFG  ",
                "   E               E   ",
                "  EC               CE  ",
                "  FC               CF  ",
                "   CCCG         GCCC   ",
                "     CCCFDEEEDFCCC     ",
                "       CCEIIIECC       ",
                "      J  G   G  J      ",
                "   FFJJJJJJBJJJJJJFF   ",
                "HCCHHCJJCJCCCJCJJCHHCCH"
            },{
                "        CEFFFEC        ",
                "     CCCCFDDDFCCCC     ",
                "    CCG         GCC    ",
                "   CC             CC   ",
                "  FC               CF  ",
                "  EC               CE  ",
                "  EDI             IDE  ",
                "  EC               CE  ",
                "  FC               CF  ",
                "   CC             CC   ",
                "    CCG         GCC    ",
                "     CCCCFDDDFCCCC     ",
                "        CEFFFEC        ",
                "      J  G   G  J      ",
                "    JFJJJJJBJJJJJFJ    ",
                " HCCHHHCJCJCJCJCHHHCCH "
            },{
                "         ECCCE         ",
                "      CCCECCCECCC      ",
                "    CCCGG     GGCCC    ",
                "    C             C    ",
                "   CC             CC   ",
                "  FC               CF  ",
                "  FCD             DCF  ",
                "  FC               CF  ",
                "   CC             CC   ",
                "    C             C    ",
                "    CCCGG     GGCCC    ",
                "     GCCCECCCECCCG     ",
                "         ECCCE         ",
                "       G       G       ",
                "    FGJJJJJBJJJJJGF    ",
                " HCCHHCJJJCCCJJJCHHCCH "
            },{
                "                       ",
                "       CCECCCECC       ",
                "     CCCCGGGGGCCCC     ",
                "     CC         CC     ",
                "    CC           CC    ",
                "   CCC           CCC   ",
                "   CCDDDB     BDDDCC   ",
                "   CCC           CCC   ",
                "    CC           CC    ",
                "    GCC         CCG    ",
                "    GCCCCGGGGGCCCCG    ",
                "    GIGCCECCCECCGIG    ",
                "    GI           IG    ",
                "    GI  G     G  IG    ",
                "    GIGJJJJBJJJJGIG    ",
                " HCCHHHJJCJCJCJJHHHCCH "
            },{
                "                       ",
                "          CCC          ",
                "       CCECCCECC       ",
                "      CCCECCCECCC      ",
                "      CCCEDDDECCC      ",
                "     CCCFDAAADFCCC     ",
                "    CCCCBDAAADBCCCC    ",
                "     CCCFDAAADFCCC     ",
                "     GCCCEDDDECCCG     ",
                "     GCCCECCCECCCG     ",
                "     G CCECCCECC G     ",
                "     G    CCC    G     ",
                "     G           G     ",
                "     G   JJJJJ   G     ",
                "     GJFJJJBJJJFJG     ",
                "  HCCHJHCHJCJHCHJHCCH  "
            },{
                "                       ",
                "                       ",
                "                       ",
                "          DDD          ",
                "        DEFFFED        ",
                "       DDE   EDD       ",
                "       DBE   EBD       ",
                "       DDE   EDD       ",
                "        DEFFFED        ",
                "          DDD          ",
                "                       ",
                "                       ",
                "                       ",
                "       E       E       ",
                "      FEEFJBJFEEF      ",
                "  HCCCHHHHCCCHHHHCCCH  "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "        B     B        ",
                "                       ",
                "        E     E        ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "       FEJFBFJEF       ",
                "   HCCCHHHHCHHHHCCCH   "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        B     B        ",
                "        BBBBBBB        ",
                "    HCCCCCHJHCCCCCH    "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "          FFF          ",
                "     HHCCCCCCCCCHH     "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "       HHHCCCHHH       "
            },{
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "                       ",
                "          HHH          "}})
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', Casings.CoolantDuct.asElement())
        .addElement(
            'C',
            buildHatchAdder(MTEEndothermicFridge.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(Casings.FridgeCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(MTEEndothermicFridge::onCasingAdded, Casings.FridgeCasing.asElement())))
        .addElement('D', Casings.FrostProofMachineCasing.asElement())
        .addElement('E', Casings.TungstensteelPipeCasing.asElement())
        .addElement('F', Casings.RobustTungstenSteelMachineCasing.asElement())
        .addElement('G', ofFrame(Materials.CallistoIce))
        .addElement('H', Casings.TungstenSteelReinforcedBlock.asElement())
        .addElement('I', ofSheetMetal(Materials.Ledox))
        .addElement(
            'J',
            ofBlocksTiered(
                MTEEndothermicFridge::getTierFromBlock,
                ImmutableList.of(Pair.of(GregTechAPI.sBlockCasings2, 1), Pair.of(GregTechAPI.sBlockCasings8, 14)),
                -1,
                MTEEndothermicFridge::setMachineTier,
                MTEEndothermicFridge::getMachineTier))
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

    @Nullable
    private static Integer getTierFromBlock(Block block, Integer metaID) {
        if (block == GregTechAPI.sBlockCasings2 && metaID == 1) { // frost proof
            return 1;
        }
        if (block == GregTechAPI.sBlockCasings8 && metaID == 14) {
            return 2; // infinity cooled casing
        }
        return null;
    }

    private void setMachineTier(int tier) {
        this.machineTier = tier;
    }

    private int getMachineTier() {
        return this.machineTier;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET) && casingAmount >= 14;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
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
