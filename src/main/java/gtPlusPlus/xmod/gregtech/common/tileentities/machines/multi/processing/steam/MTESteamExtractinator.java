package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamExtractinator extends MTESteamMultiBase<MTESteamExtractinator> implements ISurvivalConstructable {

    public MTESteamExtractinator(String aName) {
        super(aName);
    }

    public MTESteamExtractinator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTESteamExtractinator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Resource Extractor";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private IStructureDefinition<MTESteamExtractinator> STRUCTURE_DEFINITION = null;

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 8;
    private static final int DEPTH_OFF_SET = 10;

    @Override
    protected ITexture getFrontOverlay() {
        return TextureFactory.of(Textures.BlockIcons.OVERLAY_EXTRACTINATOR);
    }

    @Override
    protected ITexture getFrontOverlayActive() {
        return TextureFactory.of(Textures.BlockIcons.OVERLAY_EXTRACTINATOR_ACTIVE);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10)),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10)) };
    }

    @Override
    public IStructureDefinition<MTESteamExtractinator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamExtractinator>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (transpose(
                        new String[][] {
                            { "               ", "               ", "       AA   AA ", "       A     A ",
                                "               ", "               ", "               ", "               ",
                                "               ", "               ", "               ", "               ",
                                "               ", "       A     A ", "       AA   AA ", "               ",
                                "               " },
                            { "               ", "               ", "       AAAAAAA ", "       A     A ",
                                "       A     A ", "       A     A ", "       A     A ", "       A     A ",
                                "       A     A ", "       A     A ", "EEE    A     A ", "EEE    A     A ",
                                "EEE    A     A ", "       A     A ", "       AAAAAAA ", "               ",
                                "               " },
                            { "          G    ", "               ", "        HHHHH  ", "       H     H ",
                                "       H     H ", "       D     D ", "       H     H ", "       H     H ",
                                "       H     H ", "       H     H ", "KKK    H     H ", "KCCCCCCD     D ",
                                "KKK    H     H ", "       H     H ", "        HHHHH  ", "               ",
                                "          G    " },
                            { "          B    ", "         BBB   ", "        ABBBA  ", "       AIIIIIA ",
                                "      DDIIIIIDD", "      DDIIIIIDD", "      DDIIIIIDD", "       AIIIIIA ",
                                "       AIIIIIA ", "       AIIIIIA ", "HHH   DDIIIIIDD", "HCH   DDIIIIIDD",
                                "HHH   DDIIIIIDD", "       AIIIIIA ", "        ABBBA  ", "         BBB   ",
                                "          B    " },
                            { "          G    ", "               ", "        HHHHH  ", "       H     H ",
                                "      GH     HG", "       D     D ", "      GH     HG", "       H     H ",
                                "       H     H ", "       H     H ", "MMM   GH     HG", "MCCCCCCD     D ",
                                "MMM   GH     HG", "       H     H ", "        HHHHH  ", "               ",
                                "          G    " },
                            { "          G    ", "               ", "       HGGGGGH ", "       H     H ",
                                "      GH     HG", "       H     H ", "      G       G", "               ",
                                "               ", "               ", "HHH   G       G", "HCH    H     H ",
                                "HHH   GH     HG", "       H     H ", "       HGGGGGH ", "               ",
                                "          G    " },
                            { "          G    ", "               ", "        HHHHH  ", "       H     H ",
                                "      GH     HG", "       D     D ", "      GH     HG", "       H     H ",
                                "       H     H ", "       H     H ", "MMM   GH     HG", "MCCCCCCD     D ",
                                "MMM   GH     HG", "       H     H ", "        HHHHH  ", "               ",
                                "          G    " },
                            { "          B    ", "         BBB   ", "        ABBBA  ", "       AIIIIIA ",
                                "      DDIIIIIDD", "      DDIIIIIDD", "      DDIIIIIDD", "       AIIIIIA ",
                                "       AIIIIIA ", "       AIIIIIA ", "HHH   DDIIIIIDD", "HCC   DDIIIIIDD",
                                "HHH   DDIIIIIDD", "       AIIIIIA ", "        ABBBA  ", "         BBB   ",
                                "          B    " },
                            { "          G    ", "               ", "        HHHHH  ", "       H     H ",
                                "       H     H ", "       D     D ", "       H     H ", "       H     H ",
                                "       H     H ", "       H     H ", "L~L    H     H ", "LCCCCCCD     D ",
                                "LLL    H     H ", "       H     H ", "        HHHHH  ", "               ",
                                "          G    " },
                            { "          G    ", "               ", "       JJJJJJJ ", "       JJJJJJJ ",
                                "       JJJJJJJ ", "       JJJJJJJ ", "       JJJJJJJ ", "       JJJJJJJ ",
                                "       JJJJJJJ ", "       JJJJJJJ ", "EEE    JJJJJJJ ", "EEE    JJJJJJJ ",
                                "EEE    JJJJJJJ ", "       JJJJJJJ ", "       JJJJJJJ ", "               ",
                                "          G    " } })))
                .addElement('A', ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 3))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 12))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 13))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings3, 13))
                .addElement('G', ofFrame(Materials.Steel))
                .addElement('H', ofBlock(GregTechAPI.sBlockCasingsSteam, 2))
                .addElement('I', ofBlock(GregTechAPI.sBlockGlass1, 5))
                .addElement('J', ofBlock(GregTechAPI.sBlockCasingsSteam, 3))
                .addElement(
                    'K',
                    ofChain(
                        buildHatchAdder(MTESteamExtractinator.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, InputHatch)
                            .casingIndex(10)
                            .dot(1)
                            .buildAndChain(),
                        ofBlock(GregTechAPI.sBlockCasings1, 10)))
                .addElement(
                    'L',
                    ofChain(
                        buildHatchAdder(MTESteamExtractinator.class)
                            .atLeast(SteamHatchElement.OutputBus_Steam, OutputHatch)
                            .casingIndex(10)
                            .dot(2)
                            .buildAndChain(),
                        ofBlock(GregTechAPI.sBlockCasings1, 10)))
                .addElement(
                    'M',
                    ofChain(
                        buildSteamInput(MTESteamExtractinator.class).casingIndex(10)
                            .dot(3)
                            .build(),
                        ofBlock(GregTechAPI.sBlockCasings1, 10)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (checkHatches()) {
            return true;
        }
        return false;
    }

    private boolean checkHatches() {
        return !mSteamInputFluids.isEmpty() && !mSteamInputs.isEmpty()
            && !mSteamOutputs.isEmpty()
            && !mOutputHatches.isEmpty()
            && !mInputHatches.isEmpty();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.steamExtractinatorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            // note that a basic steam machine has .setEUtDiscount(2F).setSpeedBoost(2F). So these here are bonuses.
            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setDurationModifier((1f));
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getTierRecipes() {
        return 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Uses " + EnumChatFormatting.GOLD + "Superheated Steam")
            .addInfo("Vaporizes impurities in different soil slurries to generate usable materials")
            .addInfo("Processes up to 4 recipes at once")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "From Steam to rocks, the power of the pressure may bring you infinite wealth!.")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected SteamTypes getSteamType() {
        return SteamTypes.SH_STEAM;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Parallel: " + EnumChatFormatting.YELLOW + getMaxParallelRecipes());
        return info.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.curparallelism") + ": "
                + EnumChatFormatting.BLUE
                + tag.getInteger("parallel")
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("parallel", getMaxParallelRecipes());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP;
    }
}
