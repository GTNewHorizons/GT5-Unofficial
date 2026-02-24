package gregtech.common.tileentities.machines.multi.foundry;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOFOUNDRY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOFOUNDRY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOFOUNDRY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXOFOUNDRY_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.api.util.GTUtility.getTier;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.I3DGeometryRenderer;
import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.PostProcessingManager;
import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.shaders.BloomShader;
import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.shaders.UniversiumShader;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.IModelCustomExt;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasingsFoundry;
import gregtech.common.gui.modularui.multiblock.MTEExoFoundryGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.casing.TTCasingsContainer;

public class MTEExoFoundry extends MTEExtendedPowerMultiBlockBase<MTEExoFoundry>
    implements ISurvivalConstructable, IMTERenderer, I3DGeometryRenderer {

    private static final List<CoolingFluid> COOLING_FLUIDS = ImmutableList.of(
        new CoolingFluid(Materials.SuperCoolant, 1, 100),
        new CoolingFluid(Materials.SpaceTime, 2, 50),
        new CoolingFluid(Materials.Eternity, 3, 25));

    private final ArrayList<MTEHatchInput> coolantHatches = new ArrayList<>();
    private CoolingFluid currentCoolingFluid = null;
    private static final int horizontalOffset = 7;
    private static final int verticalOffset = 53;
    private static final int depthOffset = 0;

    public boolean terminalSwitch = false;

    private int additionalOverclocks = 0;

    public final FoundryData foundryData = new FoundryData();

    public static final int MIN_CASINGS = 462; // 462 is the total casings (489) - 1 casing for ehatch - 1 casing for
                                               // output
                                               // bus - 25 casings to hold recipes
    // modified values for display and calculations

    // array of ordinals for nbt saving purposes
    private final int[] moduleHorizontalOffsets = { 7, 7, 7, 7 };
    private final int[] moduleVerticalOffsets = { 12, 20, 38, 46 };
    private final int[] moduleDepthOffsets = { 0, 0, 0, 0 };
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTEExoFoundry> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEExoFoundry>builder()
        // spotless:off
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][]{
                    {"     HHHHH     ","   DDHHBHHDD   ","  D  HHBHH  D  "," D  HHHBHHH  D "," D HHHBHBHHH D ","HHHHHHBHBHHHHHH","HHHHBBHHHBBHHHH","HBBBHHHBHHHBBBH","HHHHBBHHHBBHHHH","HHHHHHBHBHHHHHH"," D HHHBHBHHH D "," D  HHHBHHH  D ","  D  HHBHH  D  ","   DDHHBHHDD   ","     HHHHH     "},
                    {"      HBH      ","       D       ","       D       ","       D       ","       D       ","               ","H             H","BDDDD     DDDDB","H             H","               ","       D       ","       D       ","       D       ","       D       ","      HBH      "},
                    {"     HHHHH     ","   DDHBBBHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHEHH  HHH","HBHHHHEEEHHHHBH","HBHHHEEEEEHHHBH","HBHHHHEEEHHHHBH","HHH  HHEHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHBBBHDD   ","     HHHHH     "},
                    {"               ","               ","     H   H     ","     HDDDH     ","               ","  HH  EBE  HH  ","   D E C E D   ","   D BC CB D   ","   D E C E D   ","  HH  EBE  HH  ","               ","     HDDDH     ","     H   H     ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    H     H    ","   H   D   H   ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","   H   D   H   ","    H     H    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    F     F    ","   H  EFE  H   ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","   H  EFE  H   ","    F     F    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      ABA      ","    F  D  F    ","      EBE      ","   A E C E A   ","   BDBC CBDB   ","   A E C E A   ","      EBE      ","    F  D  F    ","      ABA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      ABA      ","    F  D  F    ","      EBE      ","   A E C E A   ","   BDBC CBDB   ","   A E C E A   ","      EBE      ","    F  D  F    ","      ABA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      HBH      ","   A H C H A   ","   A BC CB A   ","   A H C H A   ","      HBH      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      DBD      ","   A D C D A   ","   A BC CB A   ","   A D C D A   ","      DBD      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      DBD      ","   A D C D A   ","   A BC CB A   ","   A D C D A   ","      DBD      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      DBD      ","   A D C D A   ","   A BC CB A   ","   A D C D A   ","      DBD      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      HBH      ","   A H C H A   ","   A BC CB A   ","   A H C H A   ","      HBH      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      ABA      ","    F  D  F    ","      EBE      ","   A E C E A   ","   BDBC CBDB   ","   A E C E A   ","      EBE      ","    F  D  F    ","      ABA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      ABA      ","    F  D  F    ","      EBE      ","   A E C E A   ","   BDBC CBDB   ","   A E C E A   ","      EBE      ","    F  D  F    ","      ABA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EBE      ","   A E C E A   ","   A BC CB A   ","   A E C E A   ","      EBE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    F     F    ","   H  EFE  H   ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","   H  EFE  H   ","    F     F    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    H     H    ","   H   D   H   ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","   H   D   H   ","    H     H    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","     H   H     ","     HDDDH     ","               ","  HH  EBE  HH  ","   D E C E D   ","   D BC CB D   ","   D E C E D   ","  HH  EBE  HH  ","               ","     HDDDH     ","     H   H     ","               ","               "},
                    {"     HHHHH     ","   DDHBBBHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHEHH  HHH","HBHHHHEEEHHHHBH","HBHHHEEEEEHHHBH","HBHHHHHEEHHHHBH","HHH  HHEHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHBBBHDD   ","     HHHHH     "},
                    {"      H~H      ","       D       ","       D       ","       D       ","       D       ","               ","H             H","BDDDD     DDDDB","H             H","               ","       D       ","       D       ","       D       ","       D       ","      HBH      "},
                    {"     HHHHH     ","   DDHHBHHDD   ","  D  HHBHH  D  "," D  HHHBHHH  D "," D HHHBHBHHH D ","HHHHHHBHBHHHHHH","HHHHBBHHHBBHHHH","HBBBHHHBHHHBBBH","HHHHBBHHHBBHHHH","HHHHHHBHBHHHHHH"," D HHHBHBHHH D "," D  HHHBHHH  D ","  D  HHBHH  D  ","   DDHHBHHDD   ","     HHHHH     "}
                }))
        .addElement('A', ofBlock(GregTechAPI.sBlockGlass1, 7)) // Foundry Glass
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings11, 7))
        .addElement('C', activeCoils(ofBlock(GregTechAPI.sBlockCasingsFoundry, 12)))
        .addElement('D', ofFrame(Materials.Netherite))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasingsFoundry, 11))
        .addElement(
            'F',
            GTStructureChannels.MAGNETIC_CHASSIS.use(
                ofBlocksTiered(
                    MTEExoFoundry::getTierFromMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 1),
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 2),
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 3)),
                    -1,
                    MTEExoFoundry::setMachineTier,
                    MTEExoFoundry::getMachineTier)))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings11, 7))
        .addElement(
            'H',
            buildHatchAdder(MTEExoFoundry.class).atLeast(InputHatch, OutputBus, InputBus, Energy.or(ExoticEnergy))
                .hint(1)
                .casingIndex(((BlockCasingsFoundry) GregTechAPI.sBlockCasingsFoundry).getTextureIndex(0))
                .buildAndChain(
                    onElementPass(MTEExoFoundry::onCasingAdded, ofBlock(GregTechAPI.sBlockCasingsFoundry, 0))))
        .addShape(
            FoundryModule.STREAMLINED_CASTERS.structureID,
            transpose(
                new String[][] {
                    {"               ","               ","   K       K   ","  K         K  ","               ","               ","               ","               ","               ","               ","               ","  K         K  ","   K       K   ","               ","               "},
                    {"               ","       L       ","  JK       KJ  ","  K         K  ","               ","               ","               "," L           L ","               ","               ","               ","  K         K  ","  JK       KJ  ","       L       ","               "},
                    {"       K       ","   K  JIJ  K   ","  MI       IM  "," KI         IK ","               ","               "," J           J ","KI           IK"," J           J ","               ","               "," KI         IK ","  MI       IM  ","   K  JIJ  K   ","       K       "},
                    {"       M       ","   L  KNK  L   ","  JN       NJ  "," LN         NL ","               ","               "," K           K ","MN           NM"," K           K ","               ","               "," LN         NL ","  JN       NJ  ","   L  KNK  L   ","       M       "},
                    {"       K       ","   K  JIJ  K   ","  MI       IM  "," KI         IK ","               ","               "," J           J ","KI           IK"," J           J ","               ","               "," KI         IK ","  MI       IM  ","   K  JIJ  K   ","       K       "},
                    {"               ","       L       ","  JK       KJ  ","  K         K  ","               ","               ","               "," L           L ","               ","               ","               ","  K         K  ","  JK       KJ  ","       L       ","               "},
                    {"               ","               ","   K       K   ","  K         K  ","               ","               ","               ","               ","               ","               ","               ","  K         K  ","   K       K   ","               ","               "}
                }
            ))
        .addElement('I', ofFrame(Materials.Tritanium))
        .addElement('J', ofFrame(Materials.SuperconductorUEVBase))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasingsFoundry,10))
        .addElement('L', ofSheetMetal(Materials.SuperconductorUEVBase))
        .addElement('M', lazy(() -> ofBlock(ModBlocks.blockCasings5Misc, 3)) )
        .addElement('N', lazy(() -> ofBlock(ModBlocks.blockSpecialMultiCasings, 13)))
        .addShape(
            FoundryModule.EXTRA_CASTING_BASINS.structureID,
            transpose(
                new String[][] {
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                    {"      eee      ","      beb      ","               ","               ","               ","               ","eb           be","ee           ee","eb           be","               ","               ","               ","               ","      beb      ","      eee      "},
                    {"     ebabe     ","   ccbHHHbcc   ","  fb       bf  "," cb         bc "," c           c ","eb           be","bH           Hb","aH           Ha","bH           Hb","eb           be"," c           c "," cb         bc ","  db       bf  ","   ccbHHHbcc   ","     ebabe     "},
                    {"     ehghe     ","   ddeHHHedd   ","  dd       dd  "," dd         dd "," d           d ","ee           ee","hH           Hh","gH           Hg","hH           Hh","ee           ee"," d           d "," dd         dd ","  dd       dd  ","   ddeHHHedd   ","     ehghe     "},
                    {"     ebabe     ","   ccbHHHbcc   ","  fb       bf  "," cb         bc "," c           c ","eb           be","bH           Hb","aH           Ha","bH           Hb","eb           be"," c           c "," cb         bc ","  db       bf  ","   ccbHHHbcc   ","     ebabe     "},
                    {"      eee      ","      beb      ","               ","               ","               ","               ","eb           be","ee           ee","eb           be","               ","               ","               ","               ","      beb      ","      eee      "},
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
                }
            ))
        .addElement('a', lazy(()->ofSheetMetal(GGMaterial.preciousMetalAlloy)))
        .addElement('b', ofBlock(GregTechAPI.sBlockCasings10, 13))
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings10, 14))
        .addElement('d', ofFrame(Materials.Erbium))
        .addElement('e', ofBlock(GregTechAPI.sBlockCasingsFoundry,8))
        .addElement('f', ofSheetMetal(Materials.Erbium))
        .addElement('g',  lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, GGMaterial.preciousMetalAlloy.getmID())))
        .addElement('h',  lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasings, GGMaterial.preciousMetalAlloy.getmID())))
        .addShape(
            FoundryModule.POWER_EFFICIENT_SUBSYSTEMS.structureID,
            transpose(
                new String[][]{
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                    {"      lll      ","       p       ","               ","               ","               ","               ","l             l","lp           pl","l             l","               ","               ","               ","               ","       p       ","      lll      "},
                    {"     lmnml     ","      ili      ","  po       op  ","  o         o  ","               ","l             l","mi           im","nl           ln","mi           im","l             l","               ","  o         o  ","  po       op  ","      ili      ","     lmnml     "},
                    {"     lnpnl     ","   i plklp i   ","  ji       ij  "," ii         ii ","               ","lp           pl","nl           ln","pk           kp","nl           ln","lp           pl","               "," ii         ii ","  ji       ij  ","   i plklp i   ","     lnpnl     "},
                    {"     lmnml     ","      ili      ","  po       op  ","  o         o  ","               ","l             l","mi           im","nl           ln","mi           im","l             l","               ","  o         o  ","  po       op  ","      ili      ","     lmnml     "},
                    {"      lll      ","       p       ","               ","               ","               ","               ","l             l","lp           pl","l             l","               ","               ","               ","               ","       p       ","      lll      "},
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
                }))
        .addElement('i', ofFrame(Materials.Dysprosium))
        .addElement('j', ofBlock(GregTechAPI.sBlockCasings11, 5))
        .addElement('k', ofFrame(Materials.TengamAttuned))
        .addElement('l', ofBlock(GregTechAPI.sBlockCasingsFoundry,6))
        .addElement('m', ofSheetMetal(Materials.Samarium))
        .addElement('n', ofSheetMetal(Materials.TengamAttuned))
        .addElement('o', ofSheetMetal(Materials.Quantium))
        .addElement('p', lazy(() -> ofBlock(ModBlocks.blockCustomMachineCasings, 3))) // TODO: replace with MEBF casing after rework)
        .addShape(
            FoundryModule.HYPERCOOLER.structureID,
            transpose(
                new String[][] {
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                    {"      rqr      ","    rrusurr    ","               ","               "," r           r "," r           r ","ru           ur","qs           sq","ru           ur"," r           r "," r           r ","               ","               ","    rrusurr    ","      rqr      "},
                    {"     utwtu     ","   tuu v uut   ","               "," t           t "," u           u ","uu           uu","t             t","wv           vw","t             t","uu           uu"," u           u "," t           t ","               ","   tuu v uut   ","     utwtu     "},
                    {"     qwwwq     ","   sssvtvsss   ","  q         q  "," s           s "," s           s ","qs           sq","wv           vw","wt           tw","wv           vw","qs           sq"," s           s "," s           s ","  q         q  ","   sssvtvsss   ","     qwwwq     "},
                    {"     utwtu     ","   tuu v uut   ","               "," t           t "," u           u ","uu           uu","t             t","wv           vw","t             t","uu           uu"," u           u "," t           t ","               ","   tuu v uut   ","     utwtu     "},
                    {"      rqr      ","    rrusurr    ","               ","               "," r           r "," r           r ","ru           ur","qs           sq","ru           ur","rr           r ","             r ","               ","               ","    rrusurr    ","      rqr      "},
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
                }))
        .addElement('q', ofBlock(GregTechAPI.sBlockCasings8,14))
        .addElement('r', ofFrame(Materials.InfinityCatalyst))
        .addElement('s', ofBlock(GregTechAPI.sBlockGlass1,3))
        .addElement('t', ofBlock(GregTechAPI.sBlockCasingsFoundry, 9))
        .addElement('u', ofSheetMetal(Materials.CallistoIce))
        .addElement('v', ofSheetMetal(Materials.SuperconductorUHVBase))
        // TODO: replace with MVF casing after rework
        .addElement('w', buildHatchAdder(MTEExoFoundry.class).hatchClass(MTEHatchInput.class)
                .adder(MTEExoFoundry::addCoolantInputToMachineList)
                .casingIndex(TAE.getIndexFromPage(2, 10))
                .hint(2)
                .buildAndChain(lazy(() -> ofBlock(ModBlocks.blockCasings3Misc, 10))))
        .addShape(
            FoundryModule.HELIOCAST_REINFORCEMENT.structureID,
            transpose(
                new String[][] {
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                    {"       4       ","               ","  2         1  ","               ","               ","               ","               ","3             3","               ","               ","               ","               ","  1         2  ","               ","       4       "},
                    {"     64746     ","   26  0  61   ","  8         8  "," 2           1 "," 6           6 ","6             6","3             3","70           07","3             3","6             6"," 6           6 "," 1           2 ","  8         8  ","   16  0  62   ","     64746     "},
                    {"      757      ","   8  050  8   ","  50       05  "," 80         08 ","               ","               ","70           07","55           55","70           07","               ","               "," 80         08 ","  50       05  ","   8  050  8   ","      757      "},
                    {"     64746     ","   26  0  61   ","  8         8  "," 2           1 "," 6           6 ","6             6","3             3","70           07","3             3","6             6"," 6           6 "," 1           2 ","  8         8  ","   16  0  62   ","     64746     "},
                    {"       4       ","               ","  2         1  ","               ","               ","               ","               ","3             3","               ","               ","               ","               ","  1         2  ","               ","       4       "},
                    {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
                }))
        .addElement('0', ofFrame(Materials.SixPhasedCopper))
        .addElement('1', ofFrame(Materials.Mellion))
        .addElement('2', ofFrame(Materials.Creon))
        .addElement('3', ofFrame(Materials.TranscendentMetal))
        .addElement('4', ofFrame(Materials.SpaceTime))
        .addElement('5', ofBlock(GregTechAPI.sBlockCasingsFoundry,7))
        .addElement('6', lazy(() -> ofBlock(GodforgeCasings, 3)))
        .addElement('7', ofSheetMetal(Materials.SpaceTime))
        .addElement('8',  lazy(() -> ofBlock(BlockGodforgeGlass.INSTANCE, 0)))

        .addShape(
            FoundryModule.EFFICIENT_OC.structureID,
            transpose(
                new String[][] {
                    {"       ^       ","               ","               ","               ","               ","               ","               ","^             ^","               ","               ","               ","               ","               ","               ","       ^       "},
                    {"      ^@^      ","       #       ","  ^^       ^^  ","  ^         ^  ","               ","               ","^             ^","@#           #@","^             ^","               ","               ","  ^         ^  ","  ^^       ^^  ","       #       ","      ^@^      "},
                    {"     ^@%@^     ","      %F%      ","  %@       @%  ","  @F       F@  ","               ","^             ^","@%           %@","%F           F%","@%           %@","^             ^","               ","  @F       F@  ","  %@       @%  ","      %F%      ","     ^@%@^     "},
                    {"    ^@%!%@^    ","   !!#F!F#!!   ","  !!       !!  "," !!         !! ","^!           !^","@#           #@","%F           F%","!!           !!","%F           F%","@#           #@","^!           !^"," !!         !! ","  !!       !!  ","   !!#F!F#!!   ","    ^@%!%@^    "},
                    {"     ^@%@^     ","      %F%      ","  %@       @%  ","  @F       F@  ","               ","^             ^","@%           %@","%F           F%","@%           %@","^             ^","               ","  @F       F@  ","  %@       @%  ","      %F%      ","     ^@%@^     "},
                    {"      ^@^      ","       #       ","  ^^       ^^  ","  ^         ^  ","               ","               ","^             ^","@#           #@","^             ^","               ","               ","  ^         ^  ","  ^^       ^^  ","       #       ","      ^@^      "},
                    {"       ^       ","               ","               ","               ","               ","               ","               ","^             ^","               ","               ","               ","               ","               ","               ","       ^       "}
                }))
        .addElement('!', lazy(() -> ofBlock(Loaders.antimatterContainmentCasing, 0)))
        .addElement('@', lazy(() -> ofBlock(Loaders.gravityStabilizationCasing, 0)))
        .addElement('#', ofFrame(Materials.Naquadria))
        .addElement('%', ofBlock(GregTechAPI.sBlockCasingsFoundry, 5))
        .addElement('^', lazy(() -> ofBlock(Loaders.magneticFluxCasing, 0)))
        .addShape(
            FoundryModule.UNIVERSAL_COLLAPSER.structureID,
            transpose(
                new String[][] {
                    {"               ","   Q       Q   ","  RR       RR  "," QR         RQ ","               ","               ","               ","               ","               ","               ","               "," QR         RQ ","  RR       RR  ","   Q       Q   ","               "},
                    {"      RQR      ","       T       ","  T         T  ","               ","               ","               ","R             R","QT           TQ","R             R","               ","               ","               ","  T         T  ","       T       ","      RQR      "},
                    {"     RRQRR     ","   UWWSTSXXV   ","  WW       XX  "," UW         XV "," W           X ","RW           XR","RS           SR","QT           TQ","RS           SR","RX           WR"," X           W "," VX         WU ","  XX       WW  ","   VXXSTSWWU   ","     RRQRR     "},
                    {"     QQZQQ     ","     TTYTT     ","               ","               ","               ","QT           TQ","QT           TQ","ZY           YZ","QT           TQ","QT           TQ","               ","               ","               ","     TTYTT     ","     QQZQQ     "},
                    {"     RRQRR     ","   VXXSTSWWU   ","  XX       WW  "," VX         WU "," X           W ","RX           WR","RS           SR","QT           TQ","RS           SR","RW           XR"," W           X "," UW         XV ","  WW       XX  ","   UWWSTSXXV   ","     RRQRR     "},
                    {"      RQR      ","       T       ","  T         T  ","               ","               ","               ","R             R","QT           TQ","R             R","               ","               ","               ","  T         T  ","       T       ","      RQR      "},
                    {"               ","   Q       Q   ","  RR       RR  "," QR         RQ ","               ","               ","               ","               ","               ","               ","               "," QR         RQ ","  RR       RR  ","   Q       Q   ","               "}
                }
            ))
        .addElement('Q', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10)))
        .addElement('R', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11)))
        .addElement('S', ofFrame(Materials.Universium))
        .addElement('T', ofFrame(Materials.MHDCSM))
        .addElement('U', ofFrame(Materials.WhiteDwarfMatter))
        .addElement('V', ofFrame(Materials.BlackDwarfMatter))
        .addElement('W', ofBlock(GregTechAPI.sBlockMetal9, 6))
        .addElement('X',ofBlock(GregTechAPI.sBlockMetal9, 7))
        .addElement('Y',ofBlock(GregTechAPI.sBlockMetal9, 13))
        .addElement('Z', activeCoils(ofBlock(GregTechAPI.sBlockCasingsFoundry, 4)))
        .addShape(
            FoundryModule.UNSET.structureID,
            transpose(
                new String[][] {
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " },
                    { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               " } }))
        .build();
    //spotless:on

    public MTEExoFoundry(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExoFoundry(String aName) {
        super(aName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        foundryData.tier = aNBT.getInteger("multiTier");
        foundryData.modules[0] = FoundryModule.values()[aNBT.getInteger("module1OR")];
        foundryData.modules[1] = FoundryModule.values()[aNBT.getInteger("module2OR")];
        foundryData.modules[2] = FoundryModule.values()[aNBT.getInteger("module3OR")];
        foundryData.modules[3] = FoundryModule.values()[aNBT.getInteger("module4OR")];
        shouldRender = aNBT.getBoolean("shouldRender");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("multiTier", foundryData.tier);
        aNBT.setInteger("module1OR", foundryData.modules[0].ordinal());
        aNBT.setInteger("module2OR", foundryData.modules[1].ordinal());
        aNBT.setInteger("module3OR", foundryData.modules[2].ordinal());
        aNBT.setInteger("module4OR", foundryData.modules[3].ordinal());
        aNBT.setBoolean("shouldRender", shouldRender);
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_EXOFOUNDRY;
    }

    @Override
    public IStructureDefinition<MTEExoFoundry> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExoFoundry(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EXOFOUNDRY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EXOFOUNDRY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EXOFOUNDRY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EXOFOUNDRY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier, Foundry")
            .addBulkMachineInfo(foundryData.parallelScaleBase, foundryData.speedModifierBase, foundryData.euEffBase)
            .addInfo(
                "Will " + EnumChatFormatting.BOLD
                    + "not"
                    + EnumChatFormatting.GRAY
                    + " perform "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "overclocks"
                    + EnumChatFormatting.GRAY
                    + " over the hatch tier without modules")
            .addInfo(
                "Will " + EnumChatFormatting.BOLD
                    + "not"
                    + EnumChatFormatting.GRAY
                    + " process "
                    + EnumChatFormatting.DARK_BLUE
                    + EnumChatFormatting.UNDERLINE
                    + "UIV+"
                    + EnumChatFormatting.GRAY
                    + " voltage tier recipes without modules")
            .addInfo(
                "Has " + EnumChatFormatting.GOLD
                    + "3 Tiers"
                    + EnumChatFormatting.GRAY
                    + ", depending on Central Chassis Casing")
            .addInfo(
                "Has " + EnumChatFormatting.GOLD
                    + "2/3/4"
                    + EnumChatFormatting.GRAY
                    + " Module Slots, based on machine tier")
            .addInfo(
                "Each Module Slot has " + EnumChatFormatting.GOLD
                    + "7"
                    + EnumChatFormatting.GRAY
                    + " different options")
            .addInfo(
                EnumChatFormatting.GOLD + "Modules" + EnumChatFormatting.GRAY + " are selected inside the Controller")
            .addInfo(
                EnumChatFormatting.GOLD + "Module"
                    + EnumChatFormatting.GRAY
                    + " stats are shown in NEI and the Controller")
            .addInfo("Toggle Render with Screwdriver")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo(EnumChatFormatting.RED + "Glorious Evolution!")
            .beginStructureBlock(15, 55, 15, true)
            .addController("Front Center")
            .addCasingInfoMinColored(
                "Primary Exo-Foundry Casing",
                EnumChatFormatting.GRAY,
                MIN_CASINGS,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Exo-Foundry Containment Glass",
                EnumChatFormatting.GRAY,
                548,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Inner Foundry Siphon Casing",
                EnumChatFormatting.GRAY,
                281,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Central Magnetic Chassis",
                EnumChatFormatting.GRAY,
                260,
                EnumChatFormatting.GOLD,
                true)
            .addCasingInfoExactlyColored(
                "Netherite Frame Box",
                EnumChatFormatting.GRAY,
                224,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Central Exo-Foundry Regulation Casing",
                EnumChatFormatting.GRAY,
                196,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Black Plutonium Item Pipe Casing",
                EnumChatFormatting.GRAY,
                173,
                EnumChatFormatting.GOLD,
                false)
            .addInputBus("Any Foundry Casing", 1)
            .addOutputBus("Any Foundry Casing", 1)
            .addInputHatch("Any Foundry Casing", 1)
            .addEnergyHatch("Any Foundry Casing", 1)
            .addStructureInfoSeparator()
            .addStructureInfo("Check NEI for Module structure costs");

        tt.toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        for (int i = 0; i < 2 + (foundryData.tier - 1); i++) {
            FoundryModule m = foundryData.modules[i];
            if (m != FoundryModule.UNSET) {
                buildPiece(
                    m.structureID,
                    stackSize,
                    hintsOnly,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i]);
            }
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        int built = 0;
        for (int i = 0; i < 2 + (foundryData.tier - 1); i++) {
            FoundryModule m = foundryData.modules[i];
            if (m != FoundryModule.UNSET) {
                built += survivalBuildPiece(
                    m.structureID,
                    stackSize,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i],
                    realBudget,
                    env,
                    false,
                    true);
            }
        }
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            realBudget,
            env,
            false,
            true);

        return built;
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    private void setMachineTier(int tier) {
        foundryData.tier = tier;
    }

    private int getMachineTier() {
        return foundryData.tier;
    }

    @Nullable
    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasingsFoundry) return null;
        if (metaID < 1 || metaID > 3) return null;
        return metaID;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        foundryData.tier = -1;
        coolantHatches.clear();
        // limit hatch space to about 25 hatches without modules. T.D.S removes 20 for balance, and casters adds 36 by
        // proxy.
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, depthOffset)) {
            getBaseMetaTileEntity().issueTileUpdate(); // update for the tier variable
            return checkModules() && casingAmount >= MIN_CASINGS + (foundryData.tdsPresent ? 20 : 0);
        }
        getBaseMetaTileEntity().issueTileUpdate(); // update for the tier variable
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMTE, long aTick) {
        super.onPostTick(baseMTE, aTick);
        if (baseMTE.isServerSide()) {
            // Updates approx every 30 sec as disconnected structure pieces do not send updates on change
            // if this is not checked, people could theoretically cheese the modules
            // if structure lib ever gets proper support for disconnected substructures, this can be changed.
            if (mUpdate <= -550) mUpdate = 50;

        }
    }

    private boolean checkModules() {
        for (int i = 0; i < 2 + (foundryData.tier - 1); i++) {
            FoundryModule m = foundryData.modules[i];
            if (!checkPiece(
                m.structureID,
                moduleHorizontalOffsets[i],
                moduleVerticalOffsets[i],
                moduleDepthOffsets[i])) {
                return false;
            }
            if (m == FoundryModule.HYPERCOOLER && coolantHatches.size() != 1) return false;
        }
        return true;

    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput inp) {
            inp.updateTexture(aBaseCasingIndex);
            coolantHatches.add(inp);
            return true;
        }
        return false;
    }

    public CoolingFluid findCoolingFluid() {
        for (MTEHatchInput hatch : coolantHatches) {
            Optional<CoolingFluid> fluid = COOLING_FLUIDS.stream()
                .filter(candidate -> drain(hatch, candidate.getStack(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
        }
        return null;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        foundryData.checkSolidifierModules();
        logic.setSpeedBonus(1F / foundryData.speedModifierAdj);
        logic.setMaxParallel(
            (int) (Math.floor(foundryData.parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage())));
        logic.setEuModifier(foundryData.euEffAdj);
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
    }

    @Nonnull
    @Override
    protected CheckRecipeResult checkRecipeForCustomHatches(CheckRecipeResult lastResult) {
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                List<ItemStack> items = hatch.getNonConsumableItems();
                FluidStack fluid = solidifierHatch.getFluid();

                if (items != null && fluid != null) {
                    processingLogic.setInputItems(items);
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        lastResult = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        return lastResult;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                additionalOverclocks = 0;

                if (foundryData.hypercoolerPresent) {
                    currentCoolingFluid = findCoolingFluid();
                    if (currentCoolingFluid == null
                        || (currentCoolingFluid.material == Materials.Eternity && !foundryData.allowEternity)) {
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                    additionalOverclocks = currentCoolingFluid.grantedOC;
                }

                additionalOverclocks += foundryData.extraOverclocks;

                if (GTUtility.getTier(recipe.mEUt) >= VoltageIndex.UIV && !foundryData.UIVRecipesEnabled) {
                    return CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks(additionalOverclocks + (getTier(getAverageInputVoltage()) - getTier(recipe.mEUt)))
                    .setDurationDecreasePerOC(foundryData.ocFactorBase + foundryData.ocFactorAdditive);

            }

        };
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        if (mMaxProgresstime > 0 && aTick % 20 == 0 && foundryData.hypercoolerPresent) {
            if (this.currentCoolingFluid != null) {
                FluidStack fluid = this.currentCoolingFluid.getStack();
                for (MTEHatchInput hatch : coolantHatches) {
                    if (drain(hatch, fluid, false)) {
                        drain(hatch, fluid, true);
                        return;
                    }
                }
                stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
            }
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return ImmutableList.of(RecipeMaps.fluidSolidifierRecipes, RecipeMaps.foundryFakeModuleCostRecipes);
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
    public boolean getDefaultInputSeparationMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    // GUI code
    @Override
    public int getMaxParallelRecipes() {
        checkModules();
        return (int) (Math.floor(foundryData.parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    protected @NotNull MTEExoFoundryGui getGui() {
        return new MTEExoFoundryGui(this);
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.EXOFOUNDRY;
    }

    // getters/setters for mui syncing
    public int getModuleSynced(int index) {
        if (index > FoundryModule.values().length - 1) index = 0;

        return foundryData.modules[index].ordinal();
    }

    public void setModule(int index, int ordinal) {
        foundryData.setModule(index, ordinal);
        // structure check on module set, to prevent cheesing
        getBaseMetaTileEntity().issueTileUpdate(); // tile update to sync to client
        this.setStructureUpdateTime(1);
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        // only allowable upright due to chassis casing pattern.
        return IAlignmentLimits.Builder.allowAll()
            .deny(ForgeDirection.DOWN)
            .deny(ForgeDirection.UP)
            .deny(Rotation.UPSIDE_DOWN)
            .deny(Rotation.CLOCKWISE)
            .deny(Rotation.COUNTER_CLOCKWISE)
            .build();
    }

    // Render code
    private boolean shouldRender = true;
    private boolean renderInitialized;
    private static IModelCustomExt ring;
    private static ShaderProgram ringProgram;
    private int uRingColor;

    private void initializeRender() {
        // spotless:off
        ring = (IModelCustomExt) AdvancedModelLoader.loadModel(
            new ResourceLocation(
                GregTech.resourceDomain,
                "textures/model/foundry_ring.obj"
            )
        );


        try {
            ringProgram = new ShaderProgram(
                GregTech.resourceDomain,
                "shaders/foundry.vert.glsl",
                "shaders/foundry.frag.glsl"
            );
            uRingColor = ringProgram.getUniformLocation("u_Color");
        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error(e.getMessage());
            return;
        }
        renderInitialized = true;
        // spotless:on
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!shouldRender || !getBaseMetaTileEntity().isActive()) {
            return;
        }

        if (!renderInitialized) {
            initializeRender();
            if (!renderInitialized) return;
        }
        ForgeDirection dir = getDirection();
        PostProcessingManager.getInstance()
            .addDelayedRenderer(this, x + 0.5f - dir.offsetX * 7, y + 0.5f, z + 0.5f - dir.offsetZ * 7);
    }

    @Override
    public void render(Object none) {
        renderRings(false);

        BloomShader.getInstance()
            .bindFramebuffer();

        renderRings(true);

        BloomShader.unbind();
        ShaderProgram.clear();
    }

    private void renderRings(boolean bloom) {
        int i = 0;
        GL11.glColor4f(1, 1, 1, 1);
        for (FoundryModule module : foundryData.modules) {
            if (i == foundryData.tier + 1) return;
            if (module == FoundryModule.UNSET) {
                i++;
                continue;
            }
            if (module == FoundryModule.UNIVERSAL_COLLAPSER) {
                renderUniversiumRing(i, bloom);
                i++;
                continue;
            }

            if (bloom) {
                renderStandardRing(i, module.red, module.green, module.blue);
            } else {
                // Render a black color in the non-bloom pass
                // The shader should technically blend the main color + bloom color together, but I probably messed up
                // somewhere, so the color is too oversaturated if I do that, and I can't be bothered with fixing it
                // ¯\_(ツ)_/¯
                renderStandardRing(i, 0, 0, 0);
            }

            i++;
        }
    }

    private void renderStandardRing(int index, float red, float green, float blue) {
        ringProgram.use();
        GL20.glUniform3f(uRingColor, red, green, blue);
        renderRing(index);
    }

    private void renderUniversiumRing(int index, boolean bloom) {
        final UniversiumShader shader = UniversiumShader.getInstance();
        if (bloom) {
            shader
                .setBackgroundColor(
                    FoundryModule.UNIVERSAL_COLLAPSER.red,
                    FoundryModule.UNIVERSAL_COLLAPSER.green,
                    FoundryModule.UNIVERSAL_COLLAPSER.blue)
                .setStarColor(24);
        } else {
            // Color needs to be enabled here to make the stars not blurry
            shader.setBackgroundColor(0, 0, 0)
                .setStarColor(3);
        }
        shader.use();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        renderRing(index);

        UniversiumShader.clear();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    private void renderRing(int index) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 9 + index * 8 + (index > 1 ? 10 : 0), 0);
        GL11.glScalef(0.8f, 1.2f, 0.8f);
        ring.renderAllVAO();
        GL11.glPopMatrix();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x - 10, y + 7, z - 10, x + 10, y + 44, z + 10);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        shouldRender = !shouldRender;
        getBaseMetaTileEntity().issueTileUpdate();
        GTUtility.sendChatTrans(aPlayer, "GT5U.machines.animations." + (shouldRender ? "enabled" : "disabled"));
    }

    /*
     * packet of render information.
     * Sends on world load, on module set, on screwdriver right click, and on structure check
     */
    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("multiTier", foundryData.tier);
        tag.setInteger("module1OR", foundryData.modules[0].ordinal());
        tag.setInteger("module2OR", foundryData.modules[1].ordinal());
        tag.setInteger("module3OR", foundryData.modules[2].ordinal());
        tag.setInteger("module4OR", foundryData.modules[3].ordinal());
        tag.setBoolean("shouldRender", shouldRender);
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        foundryData.tier = data.getInteger("multiTier");
        foundryData.modules[0] = FoundryModule.values()[data.getInteger("module1OR")];
        foundryData.modules[1] = FoundryModule.values()[data.getInteger("module2OR")];
        foundryData.modules[2] = FoundryModule.values()[data.getInteger("module3OR")];
        foundryData.modules[3] = FoundryModule.values()[data.getInteger("module4OR")];
        shouldRender = data.getBoolean("shouldRender");
    }

    // data class
    public static class CoolingFluid {

        public Materials material;
        public int grantedOC;
        public int amount;

        public CoolingFluid(Materials material, int grantedOC, int amount) {
            this.material = material;
            this.grantedOC = grantedOC;
            this.amount = amount;
        }

        public FluidStack getStack() {
            FluidStack stack = material.getFluid(amount);
            if (stack == null) {
                return material.getMolten(amount);
            }
            return stack;
        }
    }
}
