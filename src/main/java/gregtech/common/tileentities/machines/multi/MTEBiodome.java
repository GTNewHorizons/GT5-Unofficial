package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTRecipeConstants.BIODOME_DIMENSION_STRING;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IBiodomeCompatible;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.blocks.BlockCasings12;
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
                    {"                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                  AAA                  ","                 A   A                 ","                A     A                ","               A  HHH  A               ","               A  H~H  A               ","               A  HHH  A               ","                A     A                ","                 A   A                 ","                  AAA                  ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       ","                                       "},
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
        .addElement(
            'C',
            buildHatchAdder(MTEBiodome.class).atLeast(Dynamo)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(8))
                .hint(2)
                .buildAndChain(onElementPass(x -> {}, ofBlock(GregTechAPI.sBlockCasings12, 8))))
        .addElement('D', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 7)))
        .addElement('E', lazy(() -> ofBlock(ModBlocks.blockCasings2Misc, 12)))
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 7))
        .addElement(
            'H',
            ofChain(
                buildHatchAdder(MTEBiodome.class).atLeast(Energy, InputHatch, InputBus)
                    .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3))
                    .hint(1)
                    .buildAndChain(onElementPass(x -> {}, ofBlock(GregTechAPI.sBlockCasings10, 3)))))
        .addElement(
            'x',
            ofChain(onElementPass(t -> {}, ofBlock(Blocks.air, 0)), ofTileAdder(MTEBiodome::addMTE, Blocks.air, 0)))
        .build();

    public static final int MAX_CALIBRATION = 10000;
    private static final int DECAY_DURATION_TICKS = 12 * 60 * 60 * 20; // 12 hours
    public static final int WARMUP_DURATION = 1200;
    private static final int INACTIVE_GRACE_TICKS = 40;

    public enum BiodomeState {

        IDLE,
        CALIBRATING,
        ACTIVE,
        SHUTTING_DOWN,
        WARMUP;

        public static BiodomeState fromOrdinal(int ordinal) {
            BiodomeState[] values = values();
            return ordinal >= 0 && ordinal < values.length ? values[ordinal] : IDLE;
        }
    }

    private final Set<IBiodomeCompatible> connectedTEs = new HashSet<>();

    private BiodomeState state = BiodomeState.IDLE;
    private int calibrationPercent = 0;
    private String dimension;
    private ItemStack[] inputItems;
    private FluidStack[] inputFluids;

    private int warmupTicks = 0;
    private int inactiveGraceTicks = -1;
    private int decayAccumulator = 0;

    public void setCalibrationRecipe(GTRecipe recipe) {
        state = BiodomeState.CALIBRATING;
        calibrationPercent = 0;
        warmupTicks = 0;
        decayAccumulator = 0;
        clearTileDims();
        inputItems = Arrays.stream(recipe.mInputs)
            .map(s -> s != null ? s.copy() : null)
            .toArray(ItemStack[]::new);
        inputFluids = Arrays.stream(recipe.mFluidInputs)
            .map(f -> f != null ? f.copy() : null)
            .toArray(FluidStack[]::new);
        String meta = recipe.getMetadata(BIODOME_DIMENSION_STRING);
        if (meta != null) dimension = meta;
    }

    public boolean addMTE(TileEntity te) {
        if (te instanceof BaseMetaTileEntity bmte) {
            if (bmte.getMetaTileEntity() instanceof IBiodomeCompatible bc) setTileDim(bc);
        } else if (te instanceof IBiodomeCompatible bc) setTileDim(bc);
        return true;
    }

    private void setTileDim(IBiodomeCompatible te) {
        connectedTEs.add(te);
        if (state == BiodomeState.ACTIVE) {
            te.updateBiodome(getDimensionName());
        }
    }

    private void clearTileDims() {
        notifyConnectedTEs(null);
    }

    private void notifyConnectedTEs(@Nullable String dimensionName) {
        for (IBiodomeCompatible te : connectedTEs) {
            te.updateBiodome(dimensionName);
        }
    }

    private void activateWithCalibration() {
        calibrationPercent = MAX_CALIBRATION;
        inputItems = null;
        inputFluids = null;
        state = BiodomeState.ACTIVE;
        String name = getDimensionName();
        if (!name.isEmpty()) {
            notifyConnectedTEs(name);
        }
    }

    private void decayCalibration() {
        decayAccumulator += MAX_CALIBRATION;
        int decayPoints = decayAccumulator / DECAY_DURATION_TICKS;
        if (decayPoints > 0) {
            calibrationPercent = Math.max(0, calibrationPercent - decayPoints);
            decayAccumulator -= decayPoints * DECAY_DURATION_TICKS;
            if (calibrationPercent <= 0) {
                state = BiodomeState.IDLE;
                calibrationPercent = 0;
                inputItems = null;
                inputFluids = null;
                dimension = null;
                decayAccumulator = 0;
            }
        }
    }

    @Override
    public void onBlockDestroyed() {
        clearTileDims();
        state = BiodomeState.IDLE;
        inactiveGraceTicks = -1;
    }

    public String getDimensionName() {
        return calibrationPercent >= MAX_CALIBRATION && dimension != null ? dimension : "";
    }

    public String getDimension() {
        return dimension != null ? dimension : "";
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
            .addInfo("Select a dimension via the calibration panel, then supply the required items and fluids")
            .addInfo("Requires UV-tier power to maintain the dimensional simulation")
            .addInfo("Floor casings can be replaced with Dynamo Hatches for power output")
            .addSeparator()
            .addTecTechHatchInfo()
            .beginStructureBlock(39, 21, 39, false)
            .addController("Front Center")
            .addInputBus("Controller Casing", 1)
            .addInputHatch("Controller Casing", 1)
            .addEnergyHatch("Controller Casing", 1)
            .addDynamoHatch("Floor Casing", 2)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean hasRunningText() {
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        connectedTEs.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET, errors)) return;
        checkHasEnergyHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.biodomeFakeCalibrationRecipes;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEBiodomeGui(this);
    }

    @Override
    public void onPostTick(IGregTechTileEntity bmte, long aTick) {
        if (bmte.isClientSide() || aTick % 20 != 0 || calibrated) return;

        for (MTEHatchInputBus bus : validMTEList(mInputBusses)) {
            for (int i = 0; i < inputItems.length; i++) {
                if (inputItems[i] == null) continue;
                if (removed != null) inputItems[i].stackSize -= removed.stackSize;
                if (inputItems[i].stackSize == 0) inputItems[i] = null;
            }
        }

        super.onPostTick(bmte, aTick);
                if (mMaxProgresstime > 0) {
                    warmupTicks++;
                    if (warmupTicks >= WARMUP_DURATION) {
                        warmupTicks = 0;
                        activateWithCalibration();
                    }
                } else {
                    decayCalibration();
                }
                break;
            case IDLE:
                break;
        }
    }

    public boolean isCalibrated() {
        return calibrationPercent >= MAX_CALIBRATION;
    }

    public int getCalibrationPercent() {
        return calibrationPercent;
    }

    public BiodomeState getState() {
        return state;
    }

    public int getWarmupTicks() {
        return warmupTicks;
    }

    public ItemStack[] getInputItems() {
        return inputItems;
    }

    public FluidStack[] getInputFluids() {
        return inputFluids;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        switch (state) {
            case WARMUP:
                if (getMaxInputVoltage() < TierEU.UV) {
                    return CheckRecipeResultRegistry.insufficientPower(TierEU.UV);
                }
                lEUt = -TierEU.UV;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                return SimpleCheckRecipeResult.ofSuccess("biodome_warmup");
            case CALIBRATING:
                if (getMaxInputVoltage() < TierEU.UV) {
                    return CheckRecipeResultRegistry.insufficientPower(TierEU.UV);
                }
                lEUt = -TierEU.UV;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                return SimpleCheckRecipeResult.ofSuccess("calibrating");
            case ACTIVE:
                if (getMaxInputVoltage() < TierEU.UV) {
                    return CheckRecipeResultRegistry.insufficientPower(TierEU.UV);
                }
                lEUt = -TierEU.UV;
                mMaxProgresstime = 200;
                mEfficiencyIncrease = 10000;
                return SimpleCheckRecipeResult.ofSuccess("biodome_running");
            default:
                return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("biodomeState", state.ordinal());
        aNBT.setInteger("calibrationPercent", calibrationPercent);
        aNBT.setInteger("warmupTicks", warmupTicks);
        if (dimension != null) {
            aNBT.setString("dimension", dimension);
        }
        if (inputItems != null) {
            NBTTagList itemList = new NBTTagList();
            for (ItemStack item : inputItems) {
                if (item != null) {
                    itemList.appendTag(item.writeToNBT(new NBTTagCompound()));
                } else {
                    itemList.appendTag(new NBTTagCompound());
                }
            }
            aNBT.setTag("inputItems", itemList);
        }
        if (inputFluids != null) {
            NBTTagList fluidList = new NBTTagList();
            for (FluidStack fluid : inputFluids) {
                if (fluid != null) {
                    fluidList.appendTag(fluid.writeToNBT(new NBTTagCompound()));
                } else {
                    fluidList.appendTag(new NBTTagCompound());
                }
            }
            aNBT.setTag("inputFluids", fluidList);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        state = BiodomeState.fromOrdinal(aNBT.getInteger("biodomeState"));
        calibrationPercent = aNBT.getInteger("calibrationPercent");
        warmupTicks = aNBT.getInteger("warmupTicks");
        if (state == BiodomeState.SHUTTING_DOWN) {
            state = BiodomeState.WARMUP;
            warmupTicks = 0;
        }
        if (aNBT.hasKey("dimension")) {
            dimension = aNBT.getString("dimension");
        }
        if (aNBT.hasKey("inputItems")) {
            NBTTagList itemList = aNBT.getTagList("inputItems", 10);
            inputItems = new ItemStack[itemList.tagCount()];
            for (int i = 0; i < itemList.tagCount(); i++) {
                NBTTagCompound tag = itemList.getCompoundTagAt(i);
                if (tag.hasNoTags()) {
                    inputItems[i] = null;
                } else {
                    inputItems[i] = ItemStack.loadItemStackFromNBT(tag);
                }
            }
        }
        if (aNBT.hasKey("inputFluids")) {
            NBTTagList fluidList = aNBT.getTagList("inputFluids", 10);
            inputFluids = new FluidStack[fluidList.tagCount()];
            for (int i = 0; i < fluidList.tagCount(); i++) {
                NBTTagCompound tag = fluidList.getCompoundTagAt(i);
                if (tag.hasNoTags()) {
                    inputFluids[i] = null;
                } else {
                    inputFluids[i] = FluidStack.loadFluidStackFromNBT(tag);
                }
            }
        }
    }
}
