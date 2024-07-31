package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTech_API.sBlockCasings1;
import static gregtech.api.GregTech_API.sBlockCasings2;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

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
import gregtech.api.GregTech_API;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings2;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GregtechMetaTileEntity_SteamMacerator
    extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamMacerator> implements ISurvivalConstructable {

    public GregtechMetaTileEntity_SteamMacerator(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamMacerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new GregtechMetaTileEntity_SteamMacerator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Macerator";
    }

    private static final String STRUCTUR_PIECE_MAIN = "main";

    private IStructureDefinition<GregtechMetaTileEntity_SteamMacerator> STRUCTURE_DEFINITION = null;

    private final String[][] shape = new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
        { "CCC", "CCC", "CCC" } };
    // spotless:on

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 1;
    private static final int DEPTH_OFF_SET = 0;

    private int mCounCasing = 0;

    private int tierMachine = 0;

    private int tierMachineCasing = -1;

    public int getTierMachineCasing(Block block, int meta) {
        if (block == sBlockCasings1 && 10 == meta) {
            mCounCasing++;
            return 1;
        }
        if (block == sBlockCasings2 && 0 == meta) {
            mCounCasing++;
            return 2;
        }
        return 0;
    }

    protected void updateHatchTexture() {
        for (GT_MetaTileEntity_Hatch h : mSteamInputs) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mSteamOutputs) h.updateTexture(getCasingTextureID());
        for (GT_MetaTileEntity_Hatch h : mSteamInputFluids) h.updateTexture(getCasingTextureID());
    }

    private int getCasingTextureID() {
        if (tierMachineCasing == 2) return ((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0);
        return ((GT_Block_Casings1) GregTech_API.sBlockCasings1).getTextureIndex(10);
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
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_STEAM_MACERATOR_ACTIVE);
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
    public IStructureDefinition<GregtechMetaTileEntity_SteamMacerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamMacerator>builder()
                .addShape(STRUCTUR_PIECE_MAIN, transpose(shape))
                .addElement(
                    'C',
                    ofChain(
                        buildSteamInput(GregtechMetaTileEntity_SteamMacerator.class).casingIndex(10)
                            .dot(1)
                            .build(),
                        buildHatchAdder(GregtechMetaTileEntity_SteamMacerator.class)
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
        tierMachineCasing = -1;
        mCounCasing = 0;
        if (!checkPiece(STRUCTUR_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (tierMachineCasing < 0) return false;
        if (tierMachineCasing == 1 && mCounCasing > 14
            && !mSteamInputFluids.isEmpty()
            && !mSteamInputs.isEmpty()
            && !mSteamOutputs.isEmpty()) {
            updateHatchTexture();
            tierMachine = 1;
            return true;
        }
        if (tierMachineCasing == 2 && mCounCasing > 14
            && !mSteamInputFluids.isEmpty()
            && !mSteamInputs.isEmpty()
            && !mSteamOutputs.isEmpty()) {
            updateHatchTexture();
            tierMachine = 2;
            return true;
        }

        return false;
    }

    @Override
    public int getMaxParallelRecipes() {
        return tierMachine == 1 ? 8 : 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.maceratorRecipes;
    }

    @Override
    public int getItemOutputLimit() {
        return 1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            // note that a basic steam machine has .setEUtDiscount(2F).setSpeedBoost(2F). So these here are bonuses.
            @Override
            @Nonnull
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe)
                    .setEUtDiscount(1.33F)
                    .setSpeedBoost(1.5F);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Macerator")
            .addInfo("33.3% faster than using a single block Steam Macerator.")
            .addInfo("Uses only 66.6% of the steam/s required compared to a single block Steam Macerator on Tier 1.")
            .addInfo("Macerates up to 8 x Tier things at a time.")
            .addSeparator()
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
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 1)
            .addStructureInfo(EnumChatFormatting.GOLD + "14-22x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 2)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "14-22x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo("")
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
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
        return SoundResource.IC2_MACHINES_MACERATOR_OP.resourceLocation;
    }

}
