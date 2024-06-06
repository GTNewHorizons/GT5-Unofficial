package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings2;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GregtechMetaTileEntity_SteamWasher extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamWasher>
    implements ISurvivalConstructable {

    public GregtechMetaTileEntity_SteamWasher(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamWasher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_SteamWasher(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Washer";
    }

    private static final String STRUCTUR_PIECE_MAIN = "main";

    private static IStructureDefinition<GregtechMetaTileEntity_SteamWasher> STRUCTURE_DEFINITION = null;

    private final String[][] shape = new String[][] {
        { "         ", "         ", " CCCCCC  ", "         ", "         " },
        { "         ", "         ", " C    C  ", "         ", "         " },
        { "     AAA ", "    A   A", " C  A C A", "    A   A", "     AAA " },
        { "    ADDDA", "AAA D   D", "AAA D C D", "AAA D   D", "    ADDDA" },
        { "    ADDDA", "A~A DEEED", "AAA DECED", "AAA DEEED", "    ADDDA" },
        { "    AAAAA", "AAA ABBBA", "AAA ABABA", "AAA ABBBA", "    AAAAA" } };

    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 4;
    private static final int depthOffSet = 1;

    private int tierGearBoxCasing = -1;
    private int tierPipeCasing = -1;
    private int tierMachineCasing = -1;
    private int tierMachine = 1;

    private String tGlasses = "Any Glass";

    private String tMachineCasing = "Solid Bronze or Steel Machine Casing";

    public static int getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) return 1;
        if (block == sBlockCasings2 && 0 == meta) return 2;
        return -1;
    }

    private String tGearBoxCasing = "Bronze or Steel Gear Box Casing";

    public static int getTierGearBoxCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 2 == meta) return 1;
        if (block == sBlockCasings2 && 3 == meta) return 2;
        return -1;
    }

    private String tPipeCasing = "Bronze or Steel Pipe Casing";

    public static int getTierPipeCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 12 == meta) return 1;
        if (block == sBlockCasings2 && 13 == meta) return 2;
        return -1;
    }

    protected void updateHatchTexture() {
        for (GT_MetaTileEntity_Hatch h : mSteamInputs) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mSteamOutputs) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mSteamInputFluids) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mInputHatches) h.updateTexture(getCasingTextureID());
    }

    protected static String getNickname() {
        return "EvgenWarGold";
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", tierMachine);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tierMachine = aNBT.getInteger("tierMachine");
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Machine Tier: " + EnumChatFormatting.YELLOW + tierMachine);
        info.add("Parallel: " + EnumChatFormatting.YELLOW + getMaxParallelRecipes());
        return info.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();

        currenttip.add(
            StatCollector.translateToLocal("GTPP.machines.tier") + ": "
                + EnumChatFormatting.YELLOW
                + tag.getInteger("tierMachine")
                + EnumChatFormatting.RESET);
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.curparallelism") + ": "
                + EnumChatFormatting.BLUE
                + tag.getInteger("paralell")
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tierMachine", tierMachine);
        tag.setInteger("paralell", getMaxParallelRecipes());
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierGearBoxCasing = -1;
        tierPipeCasing = -1;
        tierMachineCasing = -1;
        if (!checkPiece(STRUCTUR_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (tierGearBoxCasing < 0 && tierPipeCasing < 0 && tierMachineCasing < 0) return false;
        if (tierGearBoxCasing == 1 && tierPipeCasing == 1 && tierMachineCasing == 1) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierGearBoxCasing == 2 && tierPipeCasing == 2 && tierMachineCasing == 2) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }
        return false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        tierMachineCasing = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) tierMachineCasing;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamWasher> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamWasher>builder()

                .addShape(STRUCTUR_PIECE_MAIN, transpose(shape))
                .addElement(
                    'B',
                    withChannel(
                        "tier",
                        ofBlocksTiered(
                            GregtechMetaTileEntity_SteamWasher::getTierGearBoxCasing,
                            ImmutableList.of(Pair.of(sBlockCasings2, 2), Pair.of(sBlockCasings2, 3)),
                            -1,
                            (t, m) -> t.tierGearBoxCasing = m,
                            t -> t.tierGearBoxCasing)))
                .addElement(
                    'C',
                    withChannel(
                        "tier",
                        ofBlocksTiered(
                            GregtechMetaTileEntity_SteamWasher::getTierPipeCasing,
                            ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                            -1,
                            (t, m) -> t.tierPipeCasing = m,
                            t -> t.tierPipeCasing)))
                .addElement('D', ofChain(ofBlock(Blocks.glass, 0), Glasses.chainAllGlasses()))
                .addElement(
                    'E',
                    ofChain(
                        isAir(),
                        ofBlockAnyMeta(Blocks.water),
                        ofBlockAnyMeta(Blocks.flowing_water),
                        ofBlockAnyMeta(BlocksItems.getFluidBlock(InternalName.fluidDistilledWater))))
                .addElement(
                    'A',
                    withChannel(
                        "tier",
                        ofChain(
                            ofBlocksTiered(
                                GregtechMetaTileEntity_SteamWasher::getTierMachineCasing,
                                ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                                -1,
                                (t, m) -> t.tierMachineCasing = m,
                                t -> t.tierMachineCasing),
                            buildSteamInput(GregtechMetaTileEntity_SteamWasher.class).casingIndex(10)
                                .dot(1)
                                .build(),
                            buildHatchAdder(GregtechMetaTileEntity_SteamWasher.class)
                                .atLeast(
                                    SteamHatchElement.InputBus_Steam,
                                    SteamHatchElement.OutputBus_Steam,
                                    InputHatch)
                                .casingIndex(10)
                                .dot(1)
                                .buildAndChain())))
                .build();

        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTUR_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
            STRUCTUR_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER_ACTIVE);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()) };
    }

    private int getCasingTextureID() {
        if (tierGearBoxCasing == 2 || tierPipeCasing == 2 || tierMachineCasing == 2)
            return ((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0);
        return ((GT_Block_Casings1) GregTech_API.sBlockCasings1).getTextureIndex(10);
    }

    @Override
    public int getMaxParallelRecipes() {
        return tierMachine == 1 ? 8 : 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.oreWasherRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F * tierMachine)
                    .setSpeedBoost(1.5F);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Washer")
            .addInfo("Runs recipes up to LV tier")
            .addInfo("Washing up to Tier 1 - 8 and Tier 2 - 16 things at a time")
            .addInfo("Multi consumes x2 amount of steam on Tier 2")
            .addSeparator()
            .beginStructureBlock(5, 5, 9, false)
            .addCasingInfoMin(tMachineCasing, 55, false)
            .addCasingInfo(tPipeCasing, 12)
            .addCasingInfo(tGearBoxCasing, 8)
            .addCasingInfo(tGlasses, 24)
            .addOtherStructurePart(TT_steaminputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamoutputbus, "Any casing", 1)
            .addOtherStructurePart(TT_steamhatch, "Any casing", 1)
            .addInputHatch("Any casing", 1)
            .toolTipFinisher(getNickname());
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }
}
