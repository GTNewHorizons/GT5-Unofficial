package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IBiodomeCompatible;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEBiodomeGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gtPlusPlus.core.block.ModBlocks;
import tectech.thing.casing.TTCasingsContainer;

public class MTEBiodome extends MTEExtendedPowerMultiBlockBase<MTEBiodome> implements ISurvivalConstructable {

    public static final int WIDTH_OFFSET = 19;
    public static final int HEIGHT_OFFSET = 0;
    public static final int DEPTH_OFFSET = 19;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEBiodome> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBiodome>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(
                new String[][]{
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  AAA                  ","                 A   A                 ","                A     A                ","               A  AAA  A               ","               A  A~A  A               ","               A  AAA  A               ","                A     A                ","                 A   A                 ","                  AAA                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  ABA                  ","              AA  ABA  AA              ","             ABAA ABA AABA             ","             AABAAABAAABAA             ","              AABADDDABAA              ","               AADDDDDAA               ","            AAAADDAAADDAAAA            ","            BBBBDDAAADDBBBB            ","            AAAADDAAADDAAAA            ","               AADDDDDAA               ","              AABADDDABAA              ","             AABAAABAAABAA             ","             ABAA ABA AABA             ","              AA  ABA  AA              ","                  ABA                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  A A                  ","                  A A                  ","             AA FFFBFFF AA             ","            ABFFFFFBFFFFFBA            ","            AFBFFFFBFFFFBFA            ","             FFBFxxBxxFBFF             ","            FFFFBxxBxxBFFFF            ","            FFFxxBBBBBxxFFF            ","          AAFFFxxBBBBBxxFFFAA          ","            BBBBBBBBBBBBBBB            ","          AAFFFxxBBBBBxxFFFAA          ","            FFFxxBBBBBxxFFF            ","            FFFFBxxBxxBFFFF            ","             FFBFxxBxxFBFF             ","            AFBFFFFBFFFFBFA            ","            ABFFFFFBFFFFFBA            ","             AA FFFBFFF AA             ","                  A A                  ","                  A A                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  A A                  ","                  A A                  ","                FFFBFFF                ","            AAFFFFFBFFFFFAA            ","           ABFFFxxxxxxxFFFBA           ","           AFFxxxxxxxxxxxFFA           ","           FFxxxxxxxxxxxxxFF           ","           FFxxxxxxxxxxxxxFF           ","          FFxxxxxxxxxxxxxxxFF          ","          FFxxxxxxxxxxxxxxxFF          ","        AAFFxxxxxxxxxxxxxxxFFAA        ","          BBxxxxxxxxxxxxxxxBB          ","        AAFFxxxxxxxxxxxxxxxFFAA        ","          FFxxxxxxxxxxxxxxxFF          ","          FFxxxxxxxxxxxxxxxFF          ","           FFxxxxxxxxxxxxxFF           ","           FFxxxxxxxxxxxxxFF           ","           AFFxxxxxxxxxxxFFA           ","           ABFFFxxxxxxxFFFBA           ","            AAFFFFFBFFFFFAA            ","                FFFBFFF                ","                  A A                  ","                  A A                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  A A                  ","                FFFBFFF                ","              FFFFFBFFFFF              ","            AFFFxxxxxxxFFFA            ","           BFFxxxxxxxxxxxFFB           ","          AFFxxxxxxxxxxxxxFFA          ","          FFxxxxxxxxxxxxxxxFF          ","         FFxxxxxxxxxxxxxxxxxFF         ","         FFxxxxxxxxxxxxxxxxxFF         ","        FFxxxxxxxxxxxxxxxxxxxFF        ","        FFxxxxxxxxxxxxxxxxxxxFF        ","       AFFxxxxxxxxxxxxxxxxxxxFFA       ","        BBxxxxxxxxxxxxxxxxxxxBB        ","       AFFxxxxxxxxxxxxxxxxxxxFFA       ","        FFxxxxxxxxxxxxxxxxxxxFF        ","        FFxxxxxxxxxxxxxxxxxxxFF        ","         FFxxxxxxxxxxxxxxxxxFF         ","         FFxxxxxxxxxxxxxxxxxFF         ","          FFxxxxxxxxxxxxxxxFF          ","          AFFxxxxxxxxxxxxxFFA          ","           BFFxxxxxxxxxxxFFB           ","            AFFFxxxxxxxFFFA            ","              FFFFFBFFFFF              ","                FFFBFFF                ","                  A A                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                  A A                  ","                 FA AF                 ","               FFFFBFFFF               ","             FFFxxxxxxxFFF             ","           AFFxxxxxxxxxxxFFA           ","          BAAxxxxxxxxxxxxxAAB          ","         AABxxxxxxxxxxxxxxxBAA         ","         FAxxxxxxxxxxxxxxxxxAF         ","        FFxxxxxxxxxxxxxxxxxxxFF        ","        FxxxxxxxxxxxxxxxxxxxxxF        ","       FFxxxxxxxxxxxxxxxxxxxxxFF       ","       FxxxxxxxxxxxxxxxxxxxxxxxF       ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","     AAFxxxxxxxxxxxxxxxxxxxxxxxFAA     ","       BxxxxxxxxxxxxxxxxxxxxxxxB       ","     AAFxxxxxxxxxxxxxxxxxxxxxxxFAA     ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","       FxxxxxxxxxxxxxxxxxxxxxxxF       ","       FFxxxxxxxxxxxxxxxxxxxxxFF       ","        FxxxxxxxxxxxxxxxxxxxxxF        ","        FFxxxxxxxxxxxxxxxxxxxFF        ","         FAxxxxxxxxxxxxxxxxxAF         ","         AABxxxxxxxxxxxxxxxBAA         ","          BAAxxxxxxxxxxxxxAAB          ","           AFFxxxxxxxxxxxFFA           ","             FFFxxxxxxxFFF             ","               FFFFBFFFF               ","                 FA AF                 ","                  A A                  ","                                       ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","                  A A                  ","                FFA AFF                ","              FFFxxBxxFFF              ","            FFFxxxxxxxxxFFF            ","          AFFxxxxxxxxxxxxxFFA          ","         BAAxxxxxxxxxxxxxxxAAB         ","        AABxxxxxxxxxxxxxxxxxBAA        ","        FAxxxxxxxxxxxxxxxxxxxAF        ","       FFxxxxxxxxxxxxxxxxxxxxxFF       ","       FxxxxxxxxxxxxxxxxxxxxxxxF       ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    AAxxxxxxxxxxxxxxxxxxxxxxxxxxxAA    ","      BxxxxxxxxxxxxxxxxxxxxxxxxxB      ","    AAxxxxxxxxxxxxxxxxxxxxxxxxxxxAA    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","       FxxxxxxxxxxxxxxxxxxxxxxxF       ","       FFxxxxxxxxxxxxxxxxxxxxxFF       ","        FAxxxxxxxxxxxxxxxxxxxAF        ","        AABxxxxxxxxxxxxxxxxxBAA        ","         BAAxxxxxxxxxxxxxxxAAB         ","          AFFxxxxxxxxxxxxxFFA          ","            FFFxxxxxxxxxFFF            ","              FFFxxBxxFFF              ","                FFA AFF                ","                  A A                  ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                                       ","               FFFA AFFF               ","             FFFxxxBxxxFFF             ","           FFFxxxxxxxxxxxFFF           ","         AFFxxxxxxxxxxxxxxxFFA         ","        BAAxxxxxxxxxxxxxxxxxAAB        ","       AABxxxxxxxxxxxxxxxxxxxBAA       ","       FAxxxxxxxxxxxxxxxxxxxxxAF       ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FFxxxxxxxxxxxxxxxxxxxxxxxxxxxFF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FFxxxxxxxxxxxxxxxxxxxxxxxxxxxFF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","      FFxxxxxxxxxxxxxxxxxxxxxxxFF      ","       FAxxxxxxxxxxxxxxxxxxxxxAF       ","       AABxxxxxxxxxxxxxxxxxxxBAA       ","        BAAxxxxxxxxxxxxxxxxxAAB        ","         AFFxxxxxxxxxxxxxxxFFA         ","           FFFxxxxxxxxxxxFFF           ","             FFFxxxBxxxFFF             ","               FFFA AFFF               ","                                       ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                                       ","                  A A                  ","             FFFFFFBFFFFFF             ","           FFxxxxxxxxxxxxxFF           ","          FFxxxxxxxxxxxxxxxFF          ","         AxxxxxxxxxxxxxxxxxxxA         ","        BxxxxxxxxxxxxxxxxxxxxxB        ","       AxxxxxxxxxxxxxxxxxxxxxxxA       ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA   ","    BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB    ","   AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","      FxxxxxxxxxxxxxxxxxxxxxxxxxF      ","       AxxxxxxxxxxxxxxxxxxxxxxxA       ","        BxxxxxxxxxxxxxxxxxxxxxB        ","         AxxxxxxxxxxxxxxxxxxxA         ","          FFxxxxxxxxxxxxxxxFF          ","           FFxxxxxxxxxxxxxFF           ","             FFFFFFBFFFFFF             ","                  A A                  ","                                       ","                                       ","                                       "},
                    {"                                       ","                                       ","                  A A                  ","               FFFFBFFFF               ","           FFFFxxxxxxxxxFFFF           ","         FFxxxxxxxxxxxxxxxxxFF         ","        AFFxxxxxxxxxxxxxxxxxFFA        ","       BAxxxxxxxxxxxxxxxxxxxxxAB       ","      AAxxxxxxxxxxxxxxxxxxxxxxxAA      ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA  ","   BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB   ","  AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","     FFxxxxxxxxxxxxxxxxxxxxxxxxxFF     ","      AAxxxxxxxxxxxxxxxxxxxxxxxAA      ","       BAxxxxxxxxxxxxxxxxxxxxxAB       ","        AFFxxxxxxxxxxxxxxxxxFFA        ","         FFxxxxxxxxxxxxxxxxxFF         ","           FFFFxxxxxxxxxFFFF           ","               FFFFBFFFF               ","                  A A                  ","                                       ","                                       "},
                    {"                                       ","                                       ","                  A A                  ","             FFFFFFBFFFFFF             ","          FFFxxxxxxxxxxxxxFFF          ","         FxxxxxxxxxxxxxxxxxxxF         ","        AxxxxxxxxxxxxxxxxxxxxxA        ","       BxxxxxxxxxxxxxxxxxxxxxxxB       ","      AxxxxxxxxxxxxxxxxxxxxxxxxxA      ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    Fxxxxxxxxxxxxxxxxxxxxxxxxxxxx F    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA  ","   BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB   ","  AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","      AxxxxxxxxxxxxxxxxxxxxxxxxxA      ","       BxxxxxxxxxxxxxxxxxxxxxxxB       ","        AxxxxxxxxxxxxxxxxxxxxxA        ","         FxxxxxxxxxxxxxxxxxxxF         ","          FFFxxxxxxxxxxxxxFFF          ","             FFFFFFBFFFFFF             ","                  A A                  ","                                       ","                                       "},
                    {"                                       ","                  A A                  ","               FFFFBFFFF               ","            FFFxxxxxxxxxFFF            ","         FFFxxxxxxxxxxxxxxxFFF         ","       AFxxxxxxxxxxxxxxxxxxxxxFA       ","      BAxxxxxxxxxxxxxxxxxxxxxxxAB      ","     AAxxxxxxxxxxxxxxxxxxxxxxxxxAA     ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    Fxxxxxxxxxxxxxxxxxxxxxxxxxxxx F    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA ","  BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB  "," AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","     AAxxxxxxxxxxxxxxxxxxxxxxxxxAA     ","      BAxxxxxxxxxxxxxxxxxxxxxxxAB      ","       AFxxxxxxxxxxxxxxxxxxxxxFA       ","         FFFxxxxxxxxxxxxxxxFFF         ","            FFFxxxxxxxxxFFF            ","               FFFFBFFFF               ","                  A A                  ","                                       "},
                    {"                                       ","                  A A                  ","             FFFFFFBFFFFFF             ","           FFxxxxxxxxxxxxxFF           ","         FFxxxxxxxxxxxxxxxxxFF         ","       AFxxxxxxxxxxxxxxxxxxxxxFA       ","      BxxxxxxxxxxxxxxxxxxxxxxxxxB      ","     AxxxxxxxxxxxxxxxxxxxxxxxxxxxA     ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    Fxxxxxxxxxxxxxxxxxxxxxxxxxxxx F    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA ","  BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB  "," AFxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxFA ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","     AxxxxxxxxxxxxxxxxxxxxxxxxxxxA     ","      BxxxxxxxxxxxxxxxxxxxxxxxxxB      ","       AFxxxxxxxxxxxxxxxxxxxxxFA       ","         FFxxxxxxxxxxxxxxxxxFF         ","           FFxxxxxxxxxxxxxFF           ","             FFFFFFBFFFFFF             ","                  A A                  ","                                       "},
                    {"                  A A                  ","                FFxBxFF                ","            FFFFxxxxxxxFFFF            ","          FFxxxxxxxxxxxxxxxFF          ","      A FFxxxxxxxxxxxxxxxxxxxFF A      ","     BAFxxxxxxxxxxxxxxxxxxxxxxxFAB     ","    AAxxxxxxxxxxxxxxxxxxxxxxxxxxxAA    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    AAxxxxxxxxxxxxxxxxxxxxxxxxxxxAA    ","     BAFxxxxxxxxxxxxxxxxxxxxxxxFAB     ","      A FFxxxxxxxxxxxxxxxxxxxFF A      ","          FFxxxxxxxxxxxxxxxFF          ","            FFFFxxxxxxxFFFF            ","                FFxBxFF                ","                  A A                  "},
                    {"                  A A                  ","               FFFxBxFFF               ","            FFFxxxxxxxxxFFF            ","          FFxxxxxxxxxxxxxxxFF          ","      A FFxxxxxxxxxxxxxxxxxxxFF A      ","     BFFxxxxxxxxxxxxxxxxxxxxxxxFFB     ","    AFxxxxxxxxxxxxxxxxxxxxxxxxxxxFA    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","     FxxxxxxxxxxxxxxxxxxxxxxxxxxxF     ","    AFxxxxxxxxxxxxxxxxxxxxxxxxxxxFA    ","     BFFxxxxxxxxxxxxxxxxxxxxxxxFFB     ","      A FFxxxxxxxxxxxxxxxxxxxFF A      ","          FFxxxxxxxxxxxxxxxFF          ","            FFFxxxxxxxxxFFF            ","               FFFxBxFFF               ","                  A A                  "},
                    {"                 AA AA                 ","              FFFxxBxxFFF              ","           FFFxxxxxxxxxxxFFF           ","         FFxxxxxxxxxxxxxxxxxFF         ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","         FFxxxxxxxxxxxxxxxxxFF         ","           FFFxxxxxxxxxxxFFF           ","              FFFxxBxxFFF              ","                 AA AA                 "},
                    {"                 AA AA                 ","             FFFFxxBxxFFFF             ","           FFxxxxxxxxxxxxxFF           ","         FFxxxxxxxxxxxxxxxxxFF         ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","         FFxxxxxxxxxxxxxxxxxFF         ","           FFxxxxxxxxxxxxxFF           ","             FFFFxxBxxFFFF             ","                 AA AA                 "},
                    {"                AAA AAA                ","             FFFxxxBxxxFFF             ","           FFxxxxxxxxxxxxxFF           ","         FFxxxxxxxxxxxxxxxxxFF         ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," BxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxB ","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA","AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA"," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF "," FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","  FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF  ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","   FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF   ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    FxxxxxxxxxxxxxxxxxxxxxxxxxxxxxF    ","    AxxxxxxxxxxxxxxxxxxxxxxxxxxxxxA    ","     BxxxxxxxxxxxxxxxxxxxxxxxxxxxB     ","      AFFxxxxxxxxxxxxxxxxxxxxxFFA      ","         FFxxxxxxxxxxxxxxxxxFF         ","           FFxxxxxxxxxxxxxFF           ","             FFFxxxBxxxFFF             ","                AAA AAA                "},
                    {"             AAAAAAAAAAAAA             ","           AABBBBBBBBBBBBBAA           ","         AACCxxxxxxxxxxxxxCCAA         ","        ACCxxxxxxxxxxxxxxxxxCCA        ","      AACxxxxxxxxxxxxxxxxxxxxxCAA      ","     AACxxxxxxxxxxxxxxxxxxxxxxxCAA     ","    AACxxxxxxxxxxxxxxxxxxxxxxxxxCAA    ","    ACxxxxxxxxxxxxxxxxxxxxxxxxxxxCA    ","   ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA   ","  ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA  ","  ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA  "," ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA "," ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA ","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA","ABxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxBA"," ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA "," ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA ","  ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA  ","  ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA  ","   ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCA   ","    ACxxxxxxxxxxxxxxxxxxxxxxxxxxxCA    ","    AACxxxxxxxxxxxxxxxxxxxxxxxxxCAA    ","     AACxxxxxxxxxxxxxxxxxxxxxxxCAA     ","      AACxxxxxxxxxxxxxxxxxxxxxCAA      ","        ACCxxxxxxxxxxxxxxxxxCCA        ","         AACCxxxxxxxxxxxxxCCAA         ","           AABBBBBBBBBBBBBAA           ","             AAAAAAAAAAAAA             "},
                    {"              AFFFFFFFFFA              ","           EEEEDDDDDDDDDEEEE           ","         EExxxxxxxxxxxxxxxxxEE         ","        ExxxxxxxxxxxxxxxxxxxxxE        ","      EExxxxxxxxxxxxxxxxxxxxxxxEE      ","     EExxxxxxxxxxxxxxxxxxxxxxxxxEE     ","    EExxxxxxxxxxxxxxxxxxxxxxxxxxxEE    ","    ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxE    ","   ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE   ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE ","AExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxEA","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","AExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxEA"," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","   ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE   ","    ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxE    ","    EExxxxxxxxxxxxxxxxxxxxxxxxxxxEE    ","     EExxxxxxxxxxxxxxxxxxxxxxxxxEE     ","      EExxxxxxxxxxxxxxxxxxxxxxxEE      ","        ExxxxxxxxxxxxxxxxxxxxxE        ","         EExxxxxxxxxxxxxxxxxEE         ","           EEEEDDDDDDDDDEEEE           ","              AFFFFFFFFFA              "},
                    {"              AFFFFFFFFFA              ","           EEEEDDDDDDDDDEEEE           ","         EExxxxxxxxxxxxxxxxxEE         ","        ExxxxxxxxxxxxxxxxxxxxxE        ","      EExxxxxxxxxxxxxxxxxxxxxxxEE      ","     EExxxxxxxxxxxxxxxxxxxxxxxxxEE     ","    EExxxxxxxxxxxxxxxxxxxxxxxxxxxEE    ","    ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxE    ","   ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE   ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE ","AExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxEA","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","FDxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxDF","AExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxEA"," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE "," ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","  ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE  ","   ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxE   ","    ExxxxxxxxxxxxxxxxxxxxxxxxxxxxxE    ","    EExxxxxxxxxxxxxxxxxxxxxxxxxxxEE    ","     EExxxxxxxxxxxxxxxxxxxxxxxxxEE     ","      EExxxxxxxxxxxxxxxxxxxxxxxEE      ","        ExxxxxxxxxxxxxxxxxxxxxE        ","         EExxxxxxxxxxxxxxxxxEE         ","           EEEEDDDDDDDDDEEEE           ","              AFFFFFFFFFA              "},
                    {"             AAAAAAAAAAAAA             ","           AACCBBBBBCCCCCCAA           ","         AACCCCCCCCCCCCCCCCCAA         ","        ACCCCCCCCCCCCCCCCCCCCCA        ","      AACCCCCCCCCCCCCCCCCCCCCCCAA      ","     AACCCCCCCCCCCCCCCCCCCCCCCCCAA     ","    AACCCCCCCCCCCCCCCCCCCCCCCCCCCAA    ","    ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCA    ","   ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA   ","  ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA  ","  ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA  "," ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA "," ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA ","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCBA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCBA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCBA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCBA","ABCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ABCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ABCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ABCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ABCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA","ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA"," ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA "," ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA ","  ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA  ","  ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA  ","   ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCA   ","    ACCCCCCCCCCCCCCCCCCCCCCCCCCCCCA    ","    AACCCCCCCCCCCCCCCCCCCCCCCCCCCAA    ","     AACCCCCCCCCCCCCCCCCCCCCCCCCAA     ","      AACCCCCCCCCCCCCCCCCCCCCCCAA      ","        ACCCCCCCCCCCCCCCCCCCCCA        ","         AACCCCCCCCCCCCCCCCCAA         ","           AACCCCCCBBBBBCCAA           ","             AAAAAAAAAAAAA             "}
                }))
        //spotless:on
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings10, 3))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 6))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings12, 8))
        .addElement('D', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 7)))
        .addElement('E', lazy(() -> ofBlock(ModBlocks.blockCasings2Misc, 12)))
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 7))
        .addElement(
            'x',
            ofChain(onElementPass(t -> {}, ofBlock(Blocks.air, 0)), ofTileAdder(MTEBiodome::addMTE, Blocks.air, 0)))
        .build();

    private final Set<IBiodomeCompatible> connectedTEs = new HashSet<>();

    private GTRecipe calibrationRecipe;

    public void setCalibrationRecipe(GTRecipe recipe) {
        calibrationRecipe = recipe;
    }

    public boolean addMTE(TileEntity te) {
        if (te instanceof BaseMetaTileEntity bmte) {
            if (bmte.getMetaTileEntity() instanceof IBiodomeCompatible bc) setTileDim(bc);
        } else if (te instanceof IBiodomeCompatible bc) setTileDim(bc);
        return true;
    }

    private void setTileDim(IBiodomeCompatible te) {
        connectedTEs.add(te);
        te.updateBiodome(this);
    }

    private void clearTileDims() {
        for (IBiodomeCompatible te : connectedTEs) {
            te.updateBiodome(null);
        }
    }

    @Override
    public void onBlockDestroyed() {
        clearTileDims();
    }

    public String getDimensionOverride() {
        return "Nether";
    }

    public MTEBiodome(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBiodome(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBiodome> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBiodome(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Environmental Emulator")
            .addInfo("Replicates the conditions of a dimension within the dome")
            .addInfo("Anything inside will operate as if it was built in the active dimension")
            .addInfo("Each dimension must be calibrated by providing resources from that dimension")
            .addInfo("If the Biodome shuts down, calibration will be lost")
            .addSeparator()
            .addInfo("The Biodome's floor can accept dynamos and energy hatches to pass power into the dome")
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            WIDTH_OFFSET,
            HEIGHT_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.brewingRecipes;
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEBiodomeGui(this);
    }
}
