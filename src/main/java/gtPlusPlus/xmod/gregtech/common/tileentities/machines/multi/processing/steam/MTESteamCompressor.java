package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.GregTechAPI.sBlockCasings2;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
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
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings2;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamCompressor extends MTESteamMultiBase<MTESteamCompressor> implements ISurvivalConstructable {

    public MTESteamCompressor(String aName) {
        super(aName);
    }

    public MTESteamCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTESteamCompressor(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Compressor";
    }

    private static final String STRUCTUR_PIECE_MAIN = "main";

    private IStructureDefinition<MTESteamCompressor> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shape = new String[][] {
        { "CCC", "CCC", "CCC", "CCC" },
        { "C~C", "C-C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC", "CCC" } };
    //spotless:on

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 1;
    private static final int DEPTH_OFF_SET = 0;

    private int mCountCasing = 0;

    private int tierMachine = 1;

    private int tierMachineCasing = -1;

    public int getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            mCountCasing++;
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            mCountCasing++;
            return 2;
        }
        return 0;
    }

    protected void updateHatchTexture() {
        for (MTEHatch h : mSteamInputs) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mSteamOutputs) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(getCasingTextureID());
    }

    private int getCasingTextureID() {
        if (tierMachineCasing == 2) return ((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0);
        return ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10);
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
    protected GTRenderedTexture getFrontOverlay() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR);
    }

    @Override
    protected GTRenderedTexture getFrontOverlayActive() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_COMPRESSOR_ACTIVE);
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

    @Override
    public IStructureDefinition<MTESteamCompressor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTESteamCompressor>builder()
                .addShape(STRUCTUR_PIECE_MAIN, transpose(shape))
                .addElement(
                    'C',
                    ofChain(
                        buildSteamInput(MTESteamCompressor.class).casingIndex(10)
                            .dot(1)
                            .build(),
                        buildHatchAdder(MTESteamCompressor.class)
                            .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                            .casingIndex(10)
                            .dot(1)
                            .buildAndChain(),
                        ofBlocksTiered(
                            this::getTierMachineCasing,
                            ImmutableList.of(Pair.of(sBlockCasings1, 10), Pair.of(sBlockCasings2, 0)),
                            -1,
                            (t, m) -> t.tierMachineCasing = m,
                            t -> t.tierMachineCasing)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTUR_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTUR_PIECE_MAIN,
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
        mCountCasing = 0;
        tierMachineCasing = -1;
        if (!checkPiece(STRUCTUR_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (tierMachineCasing < 0) return false;
        if (tierMachineCasing == 1 && mCountCasing > 25 && checkHatches()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierMachineCasing == 2 && mCountCasing > 25 && checkHatches()) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }

        return false;
    }

    private boolean checkHatches() {
        return !mSteamInputFluids.isEmpty() && !mSteamInputs.isEmpty()
            && !mSteamOutputs.isEmpty()
            && mOutputHatches.isEmpty()
            && mInputHatches.isEmpty();
    }

    @Override
    public int getMaxParallelRecipes() {
        return tierMachine == 1 ? 8 : 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
    }

    // note that a basic steam machine has .setEUtDiscount(2F).setSpeedBoost(2F). So these are bonuses.
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

            @Override
            @Nonnull
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F)
                    .setSpeedBoost(1.5F);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getTierRecipes() {
        return tierMachine == 1 ? 1 : 2;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Compressor")
            .addInfo("33.3% faster than using a single block Steam Compressor.")
            .addInfo("Uses only 66.6% of the steam/s compared to a single block Steam Compressor.")
            .addInfo("Bronze tier runs recipes up to LV tier")
            .addInfo("Steel tier runs recipes up to MV tier")
            .addInfo("Processes 8x parallel Bronze tier and 16x parallel Steel tier")
            .addSeparator()
            .beginStructureBlock(3, 3, 4, false)
            .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 1)
            .addStructureInfo(EnumChatFormatting.GOLD + "25-30x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 2)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "25-30x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo("")
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
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
                + tag.getInteger("parallel")
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tierMachine", tierMachine);
        tag.setInteger("parallel", getMaxParallelRecipes());
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

    @SideOnly(Side.CLIENT)
    @Override
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_COMPRESSOR_OP.resourceLocation;
    }

}
