package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;

public class MTESteamGateAssembler extends MTESteamMultiBase<MTESteamGateAssembler> implements ISurvivalConstructable {

    public MTESteamGateAssembler(String aName) {
        super(aName);
    }

    public MTESteamGateAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "An unholy amalgamation of pipes and cogs, capable of using incredible amounts of steam.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Each chamber is carefully calibrated with near-infinite amounts of pressure.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "What would drive someone to create such a device? What purpose could this possibly serve?")
            .beginStructureBlock(3, 3, 3, false)
            .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Basic " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "14-25x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "14-25x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected GTRenderedTexture getFrontOverlay() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_GATE_ASSEMBLER);
    }

    @Override
    protected GTRenderedTexture getFrontOverlayActive() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_GATE_ASSEMBLER);
    }

    @Override
    public int getTierRecipes() {
        return 0;
    }

    @Override
    public String getMachineType() {
        return "Boundless Creation Engine";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 10, 11, 10);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 10, 11, 10, elementBudget, env, false, true);
    }

    // spotless:off
    private static final IStructureDefinition<MTESteamGateAssembler> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESteamGateAssembler>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(new String[][]{
                {"                     "," BBB             AAA "," BBB             AAA "," BBB             AAA ","                     ","                     ","                     ","                     ","         BBB         ","        BBBBB        ","        BBBBB        ","        BBBBB        ","         BBB         ","                     ","                     ","                     ","                     "," AAA             BBB "," AAA             BBB "," AAA             BBB ","                     "},
                {"                     "," HHH             GGG "," H H             G G "," HHH             GGG ","                     ","                     ","                     ","                     ","         HHH         ","        H   H        ","        H   H        ","        H   H        ","         HHH         ","                     ","                     ","                     ","                     "," GGG             HHH "," G G             H H "," GGG             HHH ","                     "},
                {"                     "," BBB             AAA "," BBB             AAA "," BBB             AAA ","                     ","                     ","                     ","                     ","         BBB         ","        B   B        ","        B   B        ","        B   B        ","         BBB         ","                     ","                     ","                     ","                     "," AAA             BBB "," AAA             BBB "," AAA             BBB ","                     "},
                {" BDB             ACA ","BBBBB           AAAAA","DBBBD           CAAAC","BBBBB           AAAAA"," BDB             ACA "," EFE             EFE "," EFE             EFE "," EFE     BBB     EFE "," EFE    BFBEB    EFE "," EFEEEEBFBBBEBEEEEFE "," EFFFFFBBBBBBBFFFFFE "," EFEEEEBEBBBFBEEEEFE "," EFE    BEBFB    EFE "," EFE     BBB     EFE "," EFE     EFE     EFE "," EFE     EFE     EFE "," ACA     EFE     BDB ","AAAAAEEEEEFEEEEEBBBBB","CAAACFFFFFFFFFFFDBBBD","AAAAAEEEEEEEEEEEBBBBB"," ACA             BDB "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        B E B        ","       B     B       ","        E   E        ","       B     B       ","        B E B        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        B E B        ","       B     B       ","        E   E        ","       B     B       ","        B E B        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {" BBB             AAA ","BBBBB           AAAAA","BBBBB           AAAAA","BBBBB           AAAAA"," BBB             AAA ","                     ","                     ","         BBB         ","        BBBBB        ","       BBBBBBB       ","       BBBBBBB       ","       BBBBBBB       ","        BBBBB        ","         BBB         ","                     ","                     "," AAA             BBB ","AAAAA           BBBBB","AAAAA           BBBBB","AAAAA           BBBBB"," AAA             BBB "},
                {" BBB             AAA ","B   B           A   A","B   B           A   A","B   B           A   A"," BBB             AAA ","                     ","                     ","         B B         ","                     ","       B     B       ","          B          ","       B     B       ","                     ","         B B         ","                     ","                     "," AAA             BBB ","A   A           B   B","A   A           B   B","A   A           B   B"," AAA             BBB "},
                {" HHH             GGG ","H   H           G   G","H   H           G   G","H   H           G   G"," HHH             GGG ","                     ","                     ","         B B         ","                     ","       B     B       ","          ~          ","       B     B       ","                     ","         B B         ","                     ","                     "," GGG             HHH ","G   G           H   H","G   G           H   H","G   G           H   H"," GGG             HHH "},
                {" BBB             AAA ","B   B           A   A","B   B           A   A","B   B           A   A"," BBB             AAA ","                     ","                     ","         B B         ","                     ","       B     B       ","          B          ","       B     B       ","                     ","         B B         ","                     ","                     "," AAA             BBB ","A   A           B   B","A   A           B   B","A   A           B   B"," AAA             BBB "},
                {" BBB             AAA ","BBBBB           AAAAA","BBBBB           AAAAA","BBBBB           AAAAA"," BBB             AAA "," FEF             FEF "," FEF             FEF "," FEF     BBB     FEF "," FEF    BBBBB    FEF "," FEFFFFBBBBBBBFFFFEF "," FEEEEEBBBBBBBEEEEEF "," FEFFFFBBBBBBBFFFFEF "," FEF    BBBBB    FEF "," FEF     BBB     FEF "," FEF     FEF     FEF "," FEF     FEF     FEF "," AAA     FEF     BBB ","AAAAAFFFFFEFFFFFBBBBB","AAAAAEEEEEEEEEEEBBBBB","AAAAAFFFFFFFFFFFBBBBB"," AAA             BBB "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        B E B        ","       B     B       ","        E   E        ","       B     B       ","        B E B        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        B E B        ","       B     B       ","        E   E        ","       B     B       ","        B E B        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {"                     ","  F               E  "," FFF             EEE ","  F               E  ","                     ","                     ","                     ","         B B         ","        D E D        ","       B     B       ","        E   E        ","       B     B       ","        D E D        ","         B B         ","                     ","                     ","                     ","  E               F  "," EEE             FFF ","  E               F  ","                     "},
                {" BDB             ACA ","BBBBB           AAAAA","DBBBD           CAAAC","BBBBB           AAAAA"," BDB             ACA "," EFE             EFE "," EFE             EFE "," EFE     BBB     EFE "," EFE    BBBBB    EFE "," EFEEEEBBBBBBBEEEEFE "," EFFFFFBBBBBBBFFFFFE "," EFEEEEBBBBBBBEEEEFE "," EFE    BBBBB    EFE "," EFE     BBB     EFE "," EFE     EFE     EFE "," EFE     EFE     EFE "," ACA     EFE     BDB ","AAAAAEEEEEFEEEEEBBBBB","CAAACFFFFFFFFFFFDBBBD","AAAAAEEEEEEEEEEEBBBBB"," ACA             BDB "}
            })))
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 10))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 0))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 2))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 3))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings2, 12))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings2, 13))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings3, 13))
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings3, 14))
        .build();
    //spotless:on

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.steamGateAssemblerRecipes;
    }

    @Override
    public IStructureDefinition<MTESteamGateAssembler> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 10, 11, 10);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamGateAssembler(this.mName);
    }
}
