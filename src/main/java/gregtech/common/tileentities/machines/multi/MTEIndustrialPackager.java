package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.chainItemPipeCasings;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialPackager extends MTEExtendedPowerMultiBlockBase<MTEIndustrialPackager>
    implements ISurvivalConstructable {

    private static IStructureDefinition<MTEIndustrialPackager> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int MACHINEMODE_PACKAGER = 0;

    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 1;
    private static final int OFFSET_Z = 0;

    private static final int PARALLEL_PER_TIER = 16;
    private static final float SPEED_INCREASE_TIER = 1f;
    private static final float EU_EFFICIENCY = 0.75f;

    public MTEIndustrialPackager(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialPackager(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialPackager(this.mName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialPackager> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialPackager>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    new String[][]{{
                        "     ",
                        "D   ~",
                        "DDCDD"
                    },{
                        "DDDDD",
                        "DAAAD",
                        "DBBBD"
                    },{
                        "     ",
                        "D   D",
                        "DDCDD"
                    }})
                //spotless:on
                .addElement('A', chainAllGlasses())
                .addElement(
                    'B',
                    chainItemPipeCasings(
                        -1,
                        MTEIndustrialPackager::setItemPipeTier,
                        MTEIndustrialPackager::getItemPipeTier))
                .addElement('C', ofFrame(Materials.Iron))
                .addElement(
                    'D',
                    buildHatchAdder(MTEIndustrialPackager.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.SupplyDepotCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(MTEIndustrialPackager::onCasingAdded, Casings.SupplyDepotCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.SupplyDepotCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAAmazonPackagerActive)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAmazonPackagerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.SupplyDepotCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAAmazonPackager)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAmazonPackagerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.SupplyDepotCasing.getCasingTexture() };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Packager, Unpackager, AWD")
            .addInfo("This Multiblock is used for EXTREME packaging requirements")
            .addInfo("Can be configured to work as an Unpackager in controller")
            .addVoltageParallelInfo(PARALLEL_PER_TIER)
            .addDynamicSpeedBonusInfo(SPEED_INCREASE_TIER, TooltipTier.ITEM_PIPE_CASING)
            .addStaticEuEffInfo(EU_EFFICIENCY)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 3, 3, true)
            .addController("Front center")
            .addCasingInfoMin("Supply Depot Casings", 4, false)
            .addCasingInfoExactly("Item Pipe", 3, true)
            .addCasingInfoExactly("Iron Frame Box", 2, false)
            .addCasingInfoExactly("Any Tiered Glass", 3, true)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.ITEM_PIPE_CASING)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Oasis_Cactus")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setEuModifier(EU_EFFICIENCY);
                setSpeedBonus(1F / (SPEED_INCREASE_TIER * (itemPipeTier + 1)));
                return super.process();
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (PARALLEL_PER_TIER * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    private int itemPipeTier = -1;

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        itemPipeTier = -1;
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 4;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mPackageMode")) {
            machineMode = aNBT.getInteger("mPackageMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiPackager;
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.GTPP_MULTI_PACKAGER.mode." + machineMode);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_PACKAGER) ? RecipeMaps.packagerRecipes : RecipeMaps.unpackagerRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.packagerRecipes, RecipeMaps.unpackagerRecipes);
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
