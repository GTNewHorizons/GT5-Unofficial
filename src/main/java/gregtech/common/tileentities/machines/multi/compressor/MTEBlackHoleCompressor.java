package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.render.RenderingTileEntityBlackhole;
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
                .atLeast(Energy.or(ExoticEnergy), InputBus, OutputBus, InputHatch, SpecialHatchElement.UtilityHatch)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(12))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEBlackHoleCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 12))))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 11))
        .addElement('D', ofFrame(Materials.NaquadahAlloy))
        .addElement(
            'E',
            buildHatchAdder(MTEBlackHoleCompressor.class).atLeast(InputHatch)
                .adder(MTEBlackHoleCompressor::addSpacetimeInput)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(11))
                .hint(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings10, 11)))
        .build();

    private int catalyzingCounter = 0;
    private float blackHoleStability = 100;
    private final ArrayList<MTEHatchInput> spacetimeHatches = new ArrayList<>();
    private final ArrayList<MTEBlackHoleUtility> utilityHatches = new ArrayList<>();

    /**
     * 1: Off 2: On, stable 3: On, unstable 4: On, superstable
     */
    private byte blackHoleStatus = 1;

    @SideOnly(Side.CLIENT)
    private SoundLoopAnyBlock blackholeSoundLoop;

    private final FluidStack blackholeCatalyzingCost = (Materials.SpaceTime).getMolten(1);
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

    public boolean addSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEBlackHoleUtility sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return this.utilityHatches.add(sensor);
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEBlackHoleCompressor> {

        UtilityHatch(MTEBlackHoleCompressor::addSensorHatchToMachineList, MTEBlackHoleUtility.class) {

            @Override
            public long count(MTEBlackHoleCompressor bhc) {
                return bhc.utilityHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEBlackHoleCompressor> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEBlackHoleCompressor> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEBlackHoleCompressor> adder() {
            return adder;
        }
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBlackHoleCompressor(this.mName);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        shouldRender = !shouldRender;
        if (!shouldRender) {
            GTUtility.sendChatToPlayer(aPlayer, "Rendering off");
            rendererTileEntity = null;
            destroyRenderBlock();
        } else {
            if (blackHoleStatus != 1) createRenderBlock();
            GTUtility.sendChatToPlayer(aPlayer, "Rendering on");
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
                case 2, 4 -> {
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
        tt.addMachineType("Compressor, Advanced Neutronium Compressor, BHC")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Uses the immense power of the event horizon to compress things")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE
                    + "No longer requires heat management to perform superdense compression")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Can create advanced singularities!")
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
            .addInfo("Once the black hole becomes unstable, it will void recipes and eventually close itself!")
            .addSeparator()
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
            .addInfo(EnumChatFormatting.WHITE + "Use circuit 20 for Compressor and 21 for Neutronium Compressor")
            .addBulkMachineInfo(8, 5f, 0.7f)
            .addInfo(
                EnumChatFormatting.RED + "2x/4x"
                    + EnumChatFormatting.GRAY
                    + " parallels when stability is BELOW "
                    + EnumChatFormatting.RED
                    + "50/20")
            .addTecTechHatchInfo()
            .addInfo(
                EnumChatFormatting.RED
                    + "Recipe tier is limited to hatch tier + 1. Will not perform overclocks above the hatch tier")
            .addInfo(EnumChatFormatting.RED + "Limited to one energy hatch if using a Multi-Amp or Laser hatch")
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
            .toolTipFinisher(Ollie, "BucketBrigade");
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
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 17, 27, 10, realBudget, env, false, true);
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
        // Allow only 1 energy hatch if laser/multiamp
        if (!mExoticEnergyHatches.isEmpty()) {
            if (!mEnergyHatches.isEmpty()) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
        }
        return mCasingAmount >= 950;
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
        tag.setByte("blackHoleStatus", blackHoleStatus);
        tag.setFloat("blackHoleStability", blackHoleStability);
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
        if (tag.getByte("blackHoleStatus") != 1) {
            if (tag.getFloat("blackHoleStability") > 0) {
                currentTip.add(
                    EnumChatFormatting.DARK_PURPLE
                        + StatCollector.translateToLocal("GT5U.waila.black_hole_compressor.active"));
                currentTip.add(
                    EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocalFormatted(
                        "GT5U.waila.black_hole_compressor.stability",
                        "" + EnumChatFormatting.BOLD + Math.round(tag.getFloat("blackHoleStability"))));
            } else {
                currentTip.add(
                    EnumChatFormatting.RED
                        + StatCollector.translateToLocal("GT5U.waila.black_hole_compressor.unstable"));
            }
        } else currentTip.add(
            EnumChatFormatting.DARK_PURPLE
                + StatCollector.translateToLocal("GT5U.waila.black_hole_compressor.offline"));
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
        return -1;
    }

    private void searchAndDecrementCatalysts() {
        // Loop through all items and look for the Activation and Deactivation Catalysts
        // Deactivation resets stability to 100 and catalyzing cost to 1

        if (this.maxProgresstime() != 0) return;

        // spotless:off
        ItemStack[] catalysts = new ItemStack[] {
            ItemList.Black_Hole_Opener.get(1),
            ItemList.Black_Hole_Closer.get(1),
            ItemList.Black_Hole_Stabilizer.get(1)
        };
        //spotless:on

        for (MTEHatchInputBus bus : filterValidMTEs(mInputBusses)) {

            ItemStack removed = bus.removeResource(catalysts, 1);
            if (removed != null) {
                if (bus instanceof IRecipeProcessingAwareHatch aware) {
                    setResultIfFailure(aware.endRecipeProcessing(this));
                    aware.startRecipeProcessing();
                }
                if (ItemList.Black_Hole_Opener.isStackEqual(removed) && blackHoleStatus == 1) {
                    blackHoleStatus = 2;
                    createRenderBlock();
                } else if (ItemList.Black_Hole_Closer.isStackEqual(removed) && blackHoleStatus != 1) {
                    blackHoleStatus = 1;
                    blackHoleStability = 100;
                    catalyzingCostModifier = 1;
                    catalyzingCounter = 0;
                    if (rendererTileEntity != null) rendererTileEntity.startScaleChange(false);
                    collapseTimer = 40;
                    for (MTEBlackHoleUtility hatch : utilityHatches) {
                        hatch.updateRedstoneOutput(false);
                    }
                } else if (ItemList.Black_Hole_Stabilizer.isStackEqual(removed) && blackHoleStatus == 1) {
                    blackHoleStatus = 4;
                    createRenderBlock();
                }
                return;
            }
        }
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        searchAndDecrementCatalysts();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            private int lastMode = -1;

            @NotNull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                int mode = getModeFromCircuit(inputItems);

                if (mode == -1) {
                    lastMode = -1;
                    return Stream.empty();
                }
                if (!(mode == lastMode)) {
                    lastRecipe = null;
                    lastMode = mode;
                }
                switch (mode) {
                    case MACHINEMODE_COMPRESSOR -> {
                        return super.findRecipeMatches(RecipeMaps.compressorRecipes);
                    }
                    case MACHINEMODE_BLACKHOLE -> {
                        return super.findRecipeMatches(RecipeMaps.neutroniumCompressorRecipes);
                    }
                    default -> {
                        return super.findRecipeMatches(null);
                    }
                }
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks(GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt));
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (blackHoleStatus == 1) return CheckRecipeResultRegistry.NO_BLACK_HOLE;
                return super.validateRecipe(recipe);
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifier(0.7F)
            .setSpeedBonus(0.2F);
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // Void contents of active recipe without crashing machine if it becomes unstable
        if (blackHoleStatus == 3) {
            mOutputItems = null;
            mOutputFluids = null;
        }

        return super.onRunningTick(aStack);
    }

    // Asynchronous timer to destroy render block after collapse animation is done playing.
    // This might not sync perfectly to the renderer but this is very low stakes
    private int collapseTimer = -1;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (collapseTimer != -1) {
            if (collapseTimer == 0) {
                destroyRenderBlock();
            }
            collapseTimer--;
        }

        // Skip all the drain logic for clientside, just play sounds
        if (!aBaseMetaTileEntity.isServerSide()) {
            playBlackHoleSounds();
            return;
        }

        // Run stability checks once per second if a black hole is open
        if (blackHoleStatus == 1 || aTick % 20 != 0) return;

        // Update all the utility hatches
        for (MTEBlackHoleUtility hatch : utilityHatches) {
            hatch.updateRedstoneOutput(true);
        }

        // Black hole is superstable, just do rendering, no need for decay or drain logic
        if (blackHoleStatus == 4) {
            if (rendererTileEntity != null) {
                rendererTileEntity.toggleLaser(true);
                rendererTileEntity.setLaserColor(0, 100, 120);
            }
            return;
        }

        // Base 1 loss
        float stabilityDecrease = 1F;

        boolean didDrain = false;

        // Only do loss reductions if the black hole is stable - unstable black hole can't be frozen
        if (blackHoleStability >= 0) {

            // Search all hatches for catalyst fluid
            // If found enough, drain it and reduce stability loss to 0
            // Every 30 drains, double the cost
            FluidStack totalCost = new FluidStack(blackholeCatalyzingCost, catalyzingCostModifier);

            for (MTEHatchInput hatch : spacetimeHatches) {
                if (drain(hatch, totalCost, false)) {
                    drain(hatch, totalCost, true);
                    catalyzingCounter += 1;
                    stabilityDecrease = 0;
                    if (catalyzingCounter >= 30) {
                        // Hidden cap at 1B per tick so we don't integer overflow
                        if (catalyzingCostModifier <= 1000000000) catalyzingCostModifier *= 2;
                        catalyzingCounter = 0;
                    }
                    didDrain = true;
                    break;
                }
            }
        } else blackHoleStatus = 3;

        if (shouldRender) {
            if (rendererTileEntity != null || createRenderBlock()) {
                rendererTileEntity.toggleLaser(didDrain);
                rendererTileEntity.setStability(Math.max(0, blackHoleStability / 100F));
            }
        }

        blackHoleStability -= stabilityDecrease;

        // Close black hole and reset if it has been unstable for 15 minutes or more
        if (blackHoleStability <= -900) {
            blackHoleStatus = 1;
            blackHoleStability = 100;
            catalyzingCostModifier = 1;
            rendererTileEntity = null;
            destroyRenderBlock();

            // Update all the utility hatches
            for (MTEBlackHoleUtility hatch : utilityHatches) {
                hatch.updateRedstoneOutput(false);
            }
        }

    }

    @Override
    public int getMaxParallelRecipes() {
        int parallels = (8 * GTUtility.getTierExtended(this.getMaxInputEu()));
        if (blackHoleStatus == 4) parallels *= 4;
        else if (blackHoleStability < 50) {
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
    public boolean supportsVoidProtection() {
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
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d != ForgeDirection.UP && d != ForgeDirection.DOWN;
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
    private RenderingTileEntityBlackhole rendererTileEntity = null;

    // Returns true if render was actually created
    private boolean createRenderBlock() {
        if (!shouldRender || !mMachine) return false;
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        ForgeDirection opposite = getDirection().getOpposite();
        int x = 7 * opposite.offsetX;
        int z = 7 * opposite.offsetZ;
        int y = 11;

        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, Blocks.air);
        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, GregTechAPI.sBlackholeRender);
        rendererTileEntity = (RenderingTileEntityBlackhole) base.getWorld()
            .getTileEntity(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z);

        rendererTileEntity.startScaleChange(true);
        rendererTileEntity.setStability(blackHoleStability / 100F);
        return true;
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
