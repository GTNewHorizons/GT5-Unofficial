package gregtech.common.tileentities.machines.multi.compressor;

import static bartworks.util.BWTooltipReference.TT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.GTValues.Ollie;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE_UNSTABLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_BLACKHOLE_UNSTABLE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.tileentities.render.TileEntityBlackhole;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.multi.base.SoundLoopAnyBlock;

public class MTEBlackHoleCompressor extends MTEExtendedPowerMultiBlockBase<MTEBlackHoleCompressor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEBlackHoleCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBlackHoleCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","            CCC     CCC            ","         CCCCC       CCCCC         ","         CCCC         CCCC         ","                                   ","                                   ","                                   ","         CCCC         CCCC         ","         CCCCC       CCCCC         ","            CCC     CCC            ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","             CCC   CCC             ","            CCCC   CCCC            ","           CCCCC   CCCCC           ","         CCCCCC     CCCCCC         ","       CCCCCCC       CCCCCCC       ","       CCCCCC         CCCCCC       ","                                   ","                                   ","                                   ","       CCCCCC         CCCCCC       ","       CCCCCCC       CCCCCCC       ","         CCCCCC     CCCCCC         ","           CCCCC   CCCCC           ","            CCCC   CCCC            ","             CCC   CCC             ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","      CCCCCCCB       BCCCCCCC      ","      CCCCCCB   BBB   BCCCCCC      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      CCCCCCB   BBB   BCCCCCC      ","      CCCCCCCB       BCCCCCCC      ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCBCCCCCBCCCCCCB       ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","       BCCCCCCBCCCCCBCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","    CCCCCCCCCB       BCCCCCCCCC    ","    CCCCCCCCB         BCCCCCCCC    ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","    CCCCCCCCB         BCCCCCCCC    ","    CCCCCCCCCB       BCCCCCCCCC    ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","          BB           BB          ","         BBBB  CCCCC  BBBB         ","         BBBBCC     CCBBBB         ","          BBC         CBB          ","           C           C           ","   CCC     C           C     CCC   ","   CCC    C             C    CCC   ","          C             C          ","          C             C          ","          C             C          ","   CCC    C             C    CCC   ","   CCC     C           C     CCC   ","           C           C           ","          BBC         CBB          ","         BBBBCC     CCBBBB         ","         BBBB  CCCCC  BBBB         ","          BB           BB          ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","           BBB CCCCC BBB           ","           BBCC     CCBB           ","           BC         CB           ","   CC       C         C       CC   ","   CC      C           C      CC   ","    D      C           C      D    ","    D      C           C      D    ","    D      C           C      D    ","   CC      C           C      CC   ","   CC       C         C       CC   ","           BC         CB           ","           BBCC     CCBB           ","           BBB CCCCC BBB           ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBBCCCCCBBB            ","            BBC     CBB            ","  CCC       BC       CB       CCC  ","  CCC       C         C       CCC  ","            C         C            ","            C         C            ","            C         C            ","  CCC       C         C       CCC  ","  CCC       BC       CB       CCC  ","            BBC     CBB            ","            BBBCCCCCBBB            ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","             BBCCCCCBB             ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","   D         C       C         D   ","   D         C       C         D   ","   D         C       C         D   ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","             BBCCCCCBB             ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              BCCCCCB              ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","             C       C             ","             C       C             ","             C       C             ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","              BCCCCCB              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CCC          BBCCCBB          CCC "," CCC          BC   CB          CCC ","  D           C     C           D  ","  D           C     C           D  ","  D           C     C           D  "," CCC          BC   CB          CCC "," CCC          BBCCCBB          CCC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   "},
                {"                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC            BCCCB            CC "," CC           BC   CB           CC ","              C     C              ","              C     C              ","              C     C              "," CC           BC   CB           CC "," CC            BCCCB            CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   "},
                {"                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC             BBB             CC "," CC            BCCCB            CC "," D            BCCCCCB            D "," D            BCCCCCB            D "," D            BCCCCCB            D "," CC            BCCCB            CC "," CC             BBB             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   "},
                {"                                   ","              CC   CC              ","              CCBBBCC              ","                CCC                ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC                             CC "," CC             BBB             CC ","  BC           B   B           CB  ","  BC           B   B           CB  ","  BC           B   B           CB  "," CC             BBB             CC "," CC                             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                CCC                ","              CCBBBCC              ","              CC   CC              ","                                   "},
                {"              CCDDDCC              ","              CC   CC              ","              CBBBBBC              ","               C   C               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCC                             CCC","CCBC                           CBCC","D B                             B D","D B                             B D","D B                             B D","CCBC                           CBCC","CCC                             CCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               C   C               ","              CBBBBBC              ","              CC   CC              ","              CCDDDCC              "},
                {"              CC   CC              ","              CCCCCCC              ","              BBBBBBB              ","              C ABA C              ","                ABA                ","                 A                 ","                 A                 ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBAA                         AABC "," CBBBAA                     AABBBC "," CBAA                         AABC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                 A                 ","                 A                 ","                ABA                ","              C ABA C              ","              BBBBBBB              ","              CCCCCCC              ","              CC   CC              "},
                {"              CC   CC              ","              CCCECCC              ","              BBBBBBB              ","              C BBB C              ","                BBB                ","                ABA                ","                ABA                ","                 B                 ","                 B                 ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBBBAA                     AABBBC "," EBBBBBBB                 BBBBBBBE "," CBBBAA                     AABBBC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                 B                 ","                 B                 ","                ABA                ","                ABA                ","                BBB                ","              C BBB C              ","              BBBBBBB              ","              CCCECCC              ","              CC   CC              "},
                {"              CC   CC              ","              CCCCCCC              ","              BBBBBBB              ","              C ABA C              ","                ABA                ","                 A                 ","                 A                 ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBAA                         AABC "," CBBBAA                     AABBBC "," CBAA                         AABC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                 A                 ","                 A                 ","                ABA                ","              C ABA C              ","              BBBBBBB              ","              CCCCCCC              ","              CC   CC              "},
                {"              CCDDDCC              ","              CC   CC              ","              CBBBBBC              ","               C   C               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCC                             CCC","CCBC                           CBCC","D B                             B D","D B                             B D","D B                             B D","CCBC                           CBCC","CCC                             CCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               C   C               ","              CBBBBBC              ","              CC   CC              ","              CCDDDCC              "},
                {"                                   ","              CC   CC              ","              CCBBBCC              ","                CCC                ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC                             CC "," CC             BBB             CC ","  BC           B   B           CB  ","  BC           B   B           CB  ","  BC           B   B           CB  "," CC             BBB             CC "," CC                             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                CCC                ","              CCBBBCC              ","              CC   CC              ","                                   "},
                {"                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC             BBB             CC "," CC            BCCCB            CC "," D            BCCCCCB            D "," D            BCCCCCB            D "," D            BCCCCCB            D "," CC            BCCCB            CC "," CC             BBB             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   "},
                {"                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC            BCCCB            CC "," CC           BC   CB           CC ","              C     C              ","              C     C              ","              C     C              "," CC           BC   CB           CC "," CC            BCCCB            CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   "},
                {"                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CCC          BBCCCBB          CCC "," CCC          BC   CB          CCC ","  D           C     C           D  ","  D           C     C           D  ","  D           C     C           D  "," CCC          BC   CB          CCC "," CCC          BBCCCBB          CCC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              BCCCCCB              ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","             C       C             ","             C       C             ","             C       C             ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","              BCCCCCB              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","             BBCCCCCBB             ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","   D         C       C         D   ","   D         C       C         D   ","   D         C       C         D   ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","             BBCCCCCBB             ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBBCCCCCBBB            ","            BBC     CBB            ","  CCC       BC       CB       CCC  ","  CCC       C         C       CCC  ","            C         C            ","            C         C            ","            C         C            ","  CCC       C         C       CCC  ","  CCC       BC       CB       CCC  ","            BBC     CBB            ","            BBBCCCCCBBB            ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","           BBB CCCCC BBB           ","           BBCC     CCBB           ","           BC         CB           ","   CC       C         C       CC   ","   CC      C           C      CC   ","    D      C           C      D    ","    D      C           C      D    ","    D      C           C      D    ","   CC      C           C      CC   ","   CC       C         C       CC   ","           BC         CB           ","           BBCC     CCBB           ","           BBB CCCCC BBB           ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","          BB           BB          ","         BBBB  CC~CC  BBBB         ","         BBBBCC     CCBBBB         ","          BBC         CBB          ","           C           C           ","   CCC     C           C     CCC   ","   CCC    C             C    CCC   ","          C             C          ","          C             C          ","          C             C          ","   CCC    C             C    CCC   ","   CCC     C           C     CCC   ","           C           C           ","          BBC         CBB          ","         BBBBCC     CCBBBB         ","         BBBB  CCCCC  BBBB         ","          BB           BB          ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","    CCCCCCCCCB       BCCCCCCCCC    ","    CCCCCCCCB         BCCCCCCCC    ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","    CCCCCCCCB         BCCCCCCCC    ","    CCCCCCCCCB       BCCCCCCCCC    ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCBCCCCCBCCCCCCB       ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","       BCCCCCCBCCCCCBCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","      CCCCCCCB       BCCCCCCC      ","      CCCCCCB   BBB   BCCCCCC      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      CCCCCCB   BBB   BCCCCCC      ","      CCCCCCCB       BCCCCCCC      ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","             CCC   CCC             ","            CCCC   CCCC            ","           CCCCC   CCCCC           ","         CCCCCC     CCCCCC         ","       CCCCCCC       CCCCCCC       ","       CCCCCC         CCCCCC       ","                                   ","                                   ","                                   ","       CCCCCC         CCCCCC       ","       CCCCCCC       CCCCCCC       ","         CCCCCC     CCCCCC         ","           CCCCC   CCCCC           ","            CCCC   CCCC            ","             CCC   CCC             ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","            CCC     CCC            ","         CCCCC       CCCCC         ","         CCCC         CCCC         ","                                   ","                                   ","                                   ","         CCCC         CCCC         ","         CCCCC       CCCCC         ","            CCC     CCC            ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "}
            }))
            //spotless:on
        .addElement('A', ofBlock(GregTechAPI.sBlockGlass1, 4))
        .addElement(
            'B',
            buildHatchAdder(MTEBlackHoleCompressor.class)
                .atLeast(Energy.or(ExoticEnergy), InputBus, OutputBus, InputHatch)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(12))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEBlackHoleCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 12))))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 11))
        .addElement('D', ofFrame(Materials.NaquadahAlloy))
        .addElement(
            'E',
            buildHatchAdder(MTEBlackHoleCompressor.class).atLeast(InputHatch)
                .adder(MTEBlackHoleCompressor::addSpacetimeInput)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(11))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings10, 11)))
        .build();

    private int catalyzingCounter = 0;
    private float blackHoleStability = 100;
    private final ArrayList<MTEHatchInput> spacetimeHatches = new ArrayList<>();

    /**
     * 1: Off
     * 2: On, stable
     * 3: On, unstable
     */
    private byte blackHoleStatus = 1;

    @SideOnly(Side.CLIENT)
    private SoundLoopAnyBlock blackholeSoundLoop;

    private final FluidStack blackholeCatalyzingCost = (MaterialsUEVplus.SpaceTime).getMolten(1);
    private int catalyzingCostModifier = 1;

    public MTEBlackHoleCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBlackHoleCompressor(String aName) {
        super(aName);
    }

    private boolean addSpacetimeInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            if (aTileEntity.getMetaTileEntity() instanceof MTEHatchInput hatch) {
                hatch.updateTexture(aBaseCasingIndex);
                spacetimeHatches.add(hatch);
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public IStructureDefinition<MTEBlackHoleCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBlackHoleCompressor(this.mName);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        shouldRender = !shouldRender;
        if (!shouldRender) {
            PlayerUtils.messagePlayer(aPlayer, "Rendering off");
            rendererTileEntity = null;
            destroyRenderBlock();
        } else {
            if (blackHoleStatus != 1) createRenderBlock();
            PlayerUtils.messagePlayer(aPlayer, "Rendering on");
        }

    }

    @Override
    public void onValueUpdate(byte aValue) {
        byte oBlackHoleStatus = blackHoleStatus;
        blackHoleStatus = aValue;
        if (oBlackHoleStatus != blackHoleStatus) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @SideOnly(Side.CLIENT)
    public void playBlackHoleSounds() {
        if (blackHoleStatus > 1) {
            if (blackholeSoundLoop == null) {
                ForgeDirection oppositeDirection = getDirection().getOpposite();
                int offsetX = 7 * oppositeDirection.offsetX;
                int offsetY = 11;
                int offsetZ = 7 * oppositeDirection.offsetZ;

                World world = Minecraft.getMinecraft().thePlayer.worldObj;
                IGregTechTileEntity base = getBaseMetaTileEntity();

                int x = base.getXCoord() + offsetX;
                int y = base.getYCoord() + offsetY;
                int z = base.getZCoord() + offsetZ;

                Block blockAtSoundLocation = world.getBlock(x, y, z);
                if (blockAtSoundLocation == Blocks.air) return;

                int[] offset = { offsetX, offsetY, offsetZ };
                blackholeSoundLoop = new SoundLoopAnyBlock(
                    SoundResource.GT_MACHINES_BLACK_HOLE_COMPRESSOR.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    false,
                    offset,
                    Blocks.air);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(blackholeSoundLoop);
            }
        } else {
            if (blackholeSoundLoop != null) {
                blackholeSoundLoop = null;
            }
        }
    }

    @Override
    public byte getUpdateData() {
        return blackHoleStatus;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            IIconContainer MAIN_OVERLAY;
            IIconContainer GLOW_OVERLAY;
            switch (blackHoleStatus) {
                default -> {
                    MAIN_OVERLAY = OVERLAY_MULTI_BLACKHOLE;
                    GLOW_OVERLAY = OVERLAY_MULTI_BLACKHOLE_GLOW;
                }
                case 2 -> {
                    MAIN_OVERLAY = OVERLAY_MULTI_BLACKHOLE_ACTIVE;
                    GLOW_OVERLAY = OVERLAY_MULTI_BLACKHOLE_ACTIVE_GLOW;
                }
                case 3 -> {
                    MAIN_OVERLAY = OVERLAY_MULTI_BLACKHOLE_UNSTABLE;
                    GLOW_OVERLAY = OVERLAY_MULTI_BLACKHOLE_UNSTABLE_GLOW;
                }
            }

            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 11)),
                TextureFactory.builder()
                    .addIcon(MAIN_OVERLAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(GLOW_OVERLAY)
                    .extFacing()
                    .glow()
                    .build() };

        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 11)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Compressor/Advanced Neutronium Compressor")
            .addInfo("Controller Block for the Pseudostable Black Hole Containment Field")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Uses the immense power of the event horizon to compress things")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE
                    + "No longer requires heat management to perform superdense compression")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Can create advanced singularities!")
            .addInfo(EnumChatFormatting.BLUE + "Use circuit 20 for Compressor and 21 for Neutronium Compressor")
            .addSeparator()
            .addInfo(
                "Insert a " + EnumChatFormatting.WHITE
                    + "Black Hole Seed"
                    + EnumChatFormatting.GRAY
                    + " to open a black hole")
            .addInfo(
                "The black hole will begin its life at " + EnumChatFormatting.RED
                    + "100"
                    + EnumChatFormatting.GRAY
                    + " stability and slowly decay")
            .addInfo(
                "Stability decays by " + EnumChatFormatting.RED
                    + "1/s"
                    + EnumChatFormatting.GRAY
                    + " until it reaches 0")
            .addInfo("At 0 stability, the black hole is " + EnumChatFormatting.DARK_RED + "UNSTABLE")
            .addInfo("Once the black hole becomes unstable, it will void all inputs for recipes which require it")
            .addSeparator()
            .addInfo("Running recipes in the machine will slow the decay rate by " + EnumChatFormatting.RED + "25%")
            .addInfo(
                "The decay can be " + EnumChatFormatting.BOLD
                    + "halted"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " by inserting 1 L/s of spacetime into specific hatches")
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "30"
                    + EnumChatFormatting.GRAY
                    + " total seconds saved by spacetime insertion will "
                    + EnumChatFormatting.RED
                    + "double"
                    + EnumChatFormatting.GRAY
                    + " the cost per second!")
            .addInfo(
                "Insert a " + EnumChatFormatting.WHITE
                    + "Black Hole Collapser"
                    + EnumChatFormatting.GRAY
                    + " to close the black hole")
            .addInfo("To restore stability and reset spacetime costs, close the black hole and open a new one")
            .addSeparator()
            .addInfo(
                "Recipes not utilizing the black hole have their lengths " + EnumChatFormatting.RED
                    + "doubled"
                    + EnumChatFormatting.GRAY
                    + " if it becomes unstable")
            .addInfo("400% faster than singleblock machines of the same voltage")
            .addInfo("Only uses 70% of the EU/t normally required")
            .addInfo("Gains 8 parallels per voltage tier")
            .addInfo(
                EnumChatFormatting.RED + "2x/4x"
                    + EnumChatFormatting.GRAY
                    + " parallels when stability is BELOW "
                    + EnumChatFormatting.RED
                    + "50/20")
            .addInfo("Accepts " + TT + " energy hatches")
            .addInfo(
                EnumChatFormatting.RED
                    + "Recipe tier is limited to hatch tier + 1. Will not perform overclocks above the hatch tier.")
            .addInfo(AuthorFourIsTheNumber + EnumChatFormatting.RESET + " & " + Ollie)
            .addInfo("Rendering by: " + EnumChatFormatting.WHITE + "BucketBrigade")
            .addSeparator()
            .beginStructureBlock(35, 33, 35, false)
            .addCasingInfoMin("Background Radiation Absorbent Casing", 950, false)
            .addCasingInfoExactly("Extreme Density Space-Bending Casing", 3667, false)
            .addCasingInfoExactly("Hawking Radiation Realignment Focus", 64, false)
            .addCasingInfoExactly("Naquadah Alloy Frame Box", 144, false)
            .addInputHatch("Spacetime Insertion, Behind Laser", 2)
            .addInputBus("Any Radiation Absorbent Casing", 1)
            .addOutputBus("Any Radiation Absorbent Casing", 1)
            .addInputHatch("Any Radiation Absorbent Casing", 1)
            .addEnergyHatch("Any Radiation Absorbent Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 17, 27, 10);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 17, 27, 10, realBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
        mExoticEnergyHatches.clear();
        spacetimeHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 17, 27, 10)) return false;
        if (mCasingAmount < 950) return false;

        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("catalyzingCounter")) catalyzingCounter = aNBT.getInteger("catalyzingCounter");
        if (aNBT.hasKey("catalyzingCostModifier")) catalyzingCostModifier = aNBT.getInteger("catalyzingCostModifier");
        if (aNBT.hasKey("blackHoleStatus")) blackHoleStatus = aNBT.getByte("blackHoleStatus");
        if (aNBT.hasKey("blackHoleStability")) blackHoleStability = aNBT.getFloat("blackHoleStability");
        if (aNBT.hasKey("shouldRender")) shouldRender = aNBT.getBoolean("shouldRender");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("catalyzingCounter", catalyzingCounter);
        aNBT.setInteger("catalyzingCostModifier", catalyzingCostModifier);
        aNBT.setByte("blackHoleStatus", blackHoleStatus);
        aNBT.setFloat("blackHoleStability", blackHoleStability);
        aNBT.setBoolean("shouldRender", shouldRender);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("parallels", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("parallels"));
    }

    private int getModeFromCircuit(ItemStack[] t) {
        for (ItemStack j : t) {
            if (j.getItem() == GTUtility.getIntegratedCircuit(0)
                .getItem()) {
                if (j.getItemDamage() == 20) {
                    return MACHINEMODE_COMPRESSOR;
                } else if (j.getItemDamage() <= 21) {
                    return MACHINEMODE_BLACKHOLE;
                }
            }
        }
        return 0;
    }

    private void searchAndDecrementCatalysts() {
        // Loop through all items and look for the Activation and Deactivation Catalysts
        // Deactivation resets stability to 100 and catalyzing cost to 1

        // Has to do this "start/endRecipeProcessing" nonsense, or it doesn't work with stocking bus.
        for (MTEHatchInputBus bus : mInputBusses) {
            ItemStack[] inv = bus.getRealInventory();
            if (inv != null) {
                for (int i = 0; i < inv.length; i++) {
                    ItemStack inputItem = inv[i];
                    if (inputItem != null) {
                        if (inputItem.getItem() instanceof MetaGeneratedItem01) {
                            if (inputItem.getItemDamage() == 32418 && (blackHoleStatus == 1)) {
                                startRecipeProcessing();
                                bus.decrStackSize(i, 1);
                                endRecipeProcessing();
                                blackHoleStatus = 2;
                                createRenderBlock();
                                return;
                            } else if (inputItem.getItemDamage() == 32419 && !(blackHoleStatus == 1)) {
                                startRecipeProcessing();
                                bus.decrStackSize(i, 1);
                                endRecipeProcessing();
                                inputItem.stackSize -= 1;
                                blackHoleStatus = 1;
                                blackHoleStability = 100;
                                catalyzingCostModifier = 1;
                                rendererTileEntity = null;
                                destroyRenderBlock();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                searchAndDecrementCatalysts();

                RecipeMap<?> realMap = (getModeFromCircuit(inputItems) == MACHINEMODE_COMPRESSOR)
                    ? RecipeMaps.compressorRecipes
                    : RecipeMaps.neutroniumCompressorRecipes;
                return super.findRecipeMatches(realMap);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                // Limit ocs up to hatch tier
                int ocs = GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt);
                if (ocs < 0) ocs = 0;
                return new OverclockCalculator().setRecipeEUt(recipe.mEUt)
                    .setAmperage(availableAmperage)
                    .setEUt(availableVoltage)
                    .setDuration(recipe.mDuration)
                    .setSpeedBoost(speedBoost)
                    .setEUtDiscount(euModifier)
                    .setAmperageOC(amperageOC)
                    .setDurationDecreasePerOC(overClockTimeReduction)
                    .setEUtIncreasePerOC(overClockPowerIncrease)
                    .setParallel(getMaxParallelRecipes())
                    .limitOverclockCount(ocs);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                // Default speed bonus
                setSpeedBonus(0.2F);
                if (blackHoleStatus == 1) return CheckRecipeResultRegistry.NO_BLACK_HOLE;

                // If the recipe doesn't require black hole, but it is unstable, incur a 0.5x speed penalty
                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) == 0 && (blackHoleStatus == 3)) {
                    setSpeedBonus(5F);
                }

                // Cap recipes to energy hatch + 1
                if (GTUtility.getTier(getAverageInputVoltage()) < GTUtility.getTier(recipe.mEUt) - 1)
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                return super.validateRecipe(recipe);
            }

            @Nonnull
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                // If recipe needs a black hole and one is active but unstable, continuously void items
                if ((blackHoleStatus == 3) && recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0) {
                    return CheckRecipeResultRegistry.UNSTABLE_BLACK_HOLE;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes)
            .setEuModifier(0.7F);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // Void contents of active recipe without crashing machine if it becomes unstable
        if (blackHoleStatus != 2) {
            mOutputItems = null;
            mOutputFluids = null;
        }

        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) {
            playBlackHoleSounds();
        }
        if (aTick % 20 == 0) {
            if (blackHoleStatus == 2) {
                if (blackHoleStability >= 0) {
                    float stabilityDecrease = 1F;
                    // If the machine is running, reduce stability loss by 25%
                    if (this.maxProgresstime() != 0) {
                        stabilityDecrease = 0.75F;
                    }

                    // Search all hatches for catalyst fluid
                    // If found enough, drain it and reduce stability loss to 0
                    // Every 30 drains, double the cost
                    FluidStack totalCost = new FluidStack(blackholeCatalyzingCost, catalyzingCostModifier);

                    boolean didDrain = false;
                    for (MTEHatchInput hatch : spacetimeHatches) {
                        if (drain(hatch, totalCost, false)) {
                            drain(hatch, totalCost, true);
                            catalyzingCounter += 1;
                            stabilityDecrease = 0;
                            if (catalyzingCounter >= 30) {
                                catalyzingCostModifier *= 2;
                                catalyzingCounter = 0;
                            }
                            didDrain = true;
                            break;
                        }
                    }
                    if (shouldRender) {
                        if (rendererTileEntity == null) createRenderBlock();
                        rendererTileEntity.toggleLaser(didDrain);
                        rendererTileEntity.setStability(blackHoleStability / 100F);
                    }
                    if (blackHoleStability >= 0) blackHoleStability -= stabilityDecrease;
                    else blackHoleStability = 0;
                } else blackHoleStatus = 3;
            }
        }
    }

    public int getMaxParallelRecipes() {
        int parallels = (8 * GTUtility.getTier(this.getMaxInputVoltage()));
        if (blackHoleStability < 60) {
            parallels *= 2;
            if (blackHoleStability < 20) parallels *= 2;
        }
        return parallels;
    }

    private static final int MACHINEMODE_COMPRESSOR = 0;
    private static final int MACHINEMODE_BLACKHOLE = 1;

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.compressorRecipes, RecipeMaps.neutroniumCompressorRecipes);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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
    public void onBlockDestroyed() {
        destroyRenderBlock();
        super.onBlockDestroyed();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    private boolean shouldRender = true;
    private TileEntityBlackhole rendererTileEntity = null;

    private void createRenderBlock() {
        if (!shouldRender) return;
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        ForgeDirection opposite = getDirection().getOpposite();
        int x = 7 * opposite.offsetX;
        int z = 7 * opposite.offsetZ;
        int y = 11;

        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, Blocks.air);
        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, GregTechAPI.sBlackholeRender);
        rendererTileEntity = (TileEntityBlackhole) base.getWorld()
            .getTileEntity(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z);

        rendererTileEntity.setStability(blackHoleStability / 100F);
    }

    private void destroyRenderBlock() {
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        ForgeDirection opposite = getDirection().getOpposite();
        int x = 7 * opposite.offsetX;
        int z = 7 * opposite.offsetZ;
        int y = 11;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, Blocks.air);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
