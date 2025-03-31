package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyLava;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
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
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamMegaAlloySmelter extends MTESteamMultiBase<MTESteamMegaAlloySmelter>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "nei";

    private int casingTier = -1;
    private int tierPipeCasing = -1;

    private int casingAmount = 0;

    public MTESteamMegaAlloySmelter(String aName) {
        super(aName);
    }

    public MTESteamMegaAlloySmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamMegaAlloySmelter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Alloy Smelter";
    }

    protected void updateHatchTexture() {
        for (MTEHatch h : mSteamInputs) h.updateTexture(getIndex());
        for (MTEHatch h : mSteamOutputs) h.updateTexture(getIndex());
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(getIndex());
        for (MTEHatch h : mOutputHatches) h.updateTexture(getIndex());
        for (MTEHatch h : mInputHatches) h.updateTexture(getIndex());
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && ((aValue & 0x80) == 0 || aValue == -1)) {
            casingTier = aValue;
        }
    }

    @Override
    protected ITexture getFrontOverlay() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_STEAM_ALLOY_SMELTER_MULTI)
            .extFacing()
            .build();
    }

    @Override
    protected ITexture getFrontOverlayActive() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.OVERLAY_STEAM_ALLOY_SMELTER_MULTI)
            .extFacing()
            .build();
    }

    private int getIndex() {
        if (casingTier <= 1) return ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10);
        return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getIndex()),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getIndex()) };
    }

    private int getCasingTextureID() {
        if (tierPipeCasing == 2 || casingTier == 2)
            return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
        return ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10);
    }

    @Override
    public IStructureDefinition<MTESteamMegaAlloySmelter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private static final String[][] structure = new String[][] {
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "A     A", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAA~AAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D B B D", "D B B D", "D B B D", "D B B D", "DCBCBCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D B B D", "D B B D", "D B B D", "D B B D", "DCBCBCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "ABBABBA", "D     D", "D     D", "D     D", "D     D", "DCCCCCD", "ABBABBA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "A     A", "A     A", "A     A", "A     A", "A     A", "AAAAAAA" } };

    private static final IStructureDefinition<MTESteamMegaAlloySmelter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESteamMegaAlloySmelter>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(structure)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("C", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        .addElement(
            'A',
            ofChain(
                buildSteamInput(MTESteamMegaAlloySmelter.class).casingIndex(10)
                    .dot(1)
                    .allowOnly(ForgeDirection.NORTH)
                    .build(),
                buildHatchAdder(MTESteamMegaAlloySmelter.class)
                    .atLeast(
                        SteamHatchElement.InputBus_Steam,
                        SteamHatchElement.OutputBus_Steam,
                        OutputHatch,
                        InputHatch)
                    .casingIndex(10)
                    .dot(1)
                    .allowOnly(ForgeDirection.NORTH)
                    .buildAndChain(
                        onElementPass(
                            MTESteamMegaAlloySmelter::onCasingAdded,
                            ofBlocksTiered(
                                MTESteamMegaAlloySmelter::getTierMachineCasing,
                                ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                                -1,
                                MTESteamMegaAlloySmelter::setCasingTier,
                                MTESteamMegaAlloySmelter::getCasingTier)))))
        .addElement(
            'B',
            ofBlocksTiered(
                MTESteamMegaAlloySmelter::getTierPipeCasing,
                ImmutableList.of(Pair.of(sBlockCasings2, 12), Pair.of(sBlockCasings2, 13)),
                -1,
                (t, m) -> t.tierPipeCasing = m,
                t -> t.tierPipeCasing))
        .addElement('C', ofAnyLava(true))
        .addElement('D', chainAllGlasses())

        .build();

    public static Integer getTierPipeCasing(Block block, int meta) {
        if (block == sBlockCasings2 && 12 == meta) return 1;
        if (block == sBlockCasings2 && 13 == meta) return 2;
        return null;
    }

    public static int getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            return 2;
        }
        return 0;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this
            .survivialBuildPiece(STRUCTURE_PIECE_MAIN_SURVIVAL, stackSize, 3, 3, 1, realBudget, env, false, true);
    }

    private int tierMachine = -1;

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.alloySmelterRecipes);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tierMachine = -1;
        tierPipeCasing = -1;
        casingTier = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 1)) return false;
        if (tierPipeCasing == 1 && casingTier == 1) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierPipeCasing == 2 && casingTier == 2) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }
        return casingAmount >= 100;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.alloySmelterRecipes;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        casingTier = aValue;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {

        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (availableVoltage < recipe.mEUt) {
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                } else return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.25 * tierMachine)
                    .setDurationModifier(1F / tierMachine);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 256;
    }

    @Override
    public int getTierRecipes() {
        return 1;
    }

    @Override
    protected SteamTypes getSteamType() {
        return SteamTypes.SC_STEAM;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Uses " + EnumChatFormatting.RED + "Supercritical Steam")
            .addInfo("100% faster than using single block steam machines of the same pressure")
            .addInfo("Only consumes steam at 62.5% of the L/s normally required")
            .addInfo("Processes up to 256 items at once")
            .addInfo("Minimum casing: 100")
            .addInfo(HIGH_PRESSURE_TOOLTIP_NOTICE)
            .addInfo(AQUA + "" + BOLD + "Alloy")
            .addInfo(AQUA + "" + ITALIC + "[ noun al-oi, uh-loi; verb uh-loi ]")
            .addInfo(AQUA + "" + BOLD + "noun:")
            .addInfo(
                AQUA + ""
                    + ITALIC
                    + "a substance composed of two or more metals, or of a metal or metals with a nonmetal, intimately mixed, as by fusion or electrodeposition.")
            .addInfo(AQUA + "" + BOLD + "verb:")
            .addInfo(AQUA + "" + ITALIC + "to mix (metals or metal with nonmetal) so as to form an alloy.")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Machine Tier: " + EnumChatFormatting.YELLOW + casingTier);
        info.add("Parallel: " + EnumChatFormatting.YELLOW + getTrueParallel());
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
                + getSteamTierTextForWaila(tag)
                + EnumChatFormatting.RESET);
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
        tag.setInteger("tierMachine", tierMachine);
        tag.setInteger("parallel", getTrueParallel());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("tierMachine", casingTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        casingTier = aNBT.getInteger("tierMachine");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_AUTOCLAVE_LOOP;
    }

}
