package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GTValues.AuthorVolence;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.text.DecimalFormat;
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

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMultiAutoclave extends MTEExtendedPowerMultiBlockBase<MTEMultiAutoclave>
    implements ISurvivalConstructable {

    public MTEMultiAutoclave(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiAutoclave(String aName) {
        super(aName);
    }

    private HeatingCoilLevel heatLevel;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    protected int itemPipeTier = 0;
    protected int fluidPipeTier = 0;

    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings11) return -1;
        if (metaID < 0 || metaID > 7) return -1;
        return metaID + 1;
    }

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    private static Integer getFluidTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings2) return -1;
        if (metaID < 12 || metaID > 15) return -1;
        return metaID - 11;
    }

    private void setFluidPipeTier(int tier) {
        fluidPipeTier = tier;
    }

    private int getFluidPipeTier() {
        return fluidPipeTier;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.heatLevel = aCoilLevel;
    }

    public Integer getCoilTier() {
        return (int) this.getCoilLevel()
            .getTier() + 1;
    }

    private static final IStructureDefinition<MTEMultiAutoclave> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiAutoclave>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "  AAA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ",
                        "  AAA  " },
                    { " ABBBA ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ",
                        " ABBBA ", },
                    { "ABBBBBA", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A",
                        "ABBBBBA", },
                    { "ABBBBBA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA",
                        "ABBBBBA", },
                    { "ABBBBBA", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A",
                        "ABBBBBA", },
                    { "AABBBAA", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ",
                        "AABBBAA", },
                    { "A A~A A", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ",
                        "A AAA A" } }))
        .addElement(
            'A',
            buildHatchAdder(MTEMultiAutoclave.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Muffler, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEMultiAutoclave::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 3))))
        .addElement('B', Glasses.chainAllGlasses()) // Steel Casings
        .addElement('C', ofFrame(Materials.Polytetrafluoroethylene)) // PTFE Frame
        .addElement(
            'D',
            ofBlocksTiered(
                MTEMultiAutoclave::getFluidTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTechAPI.sBlockCasings2, 12),
                    Pair.of(GregTechAPI.sBlockCasings2, 13),
                    Pair.of(GregTechAPI.sBlockCasings2, 14),
                    Pair.of(GregTechAPI.sBlockCasings2, 15)),
                -2,
                MTEMultiAutoclave::setFluidPipeTier,
                MTEMultiAutoclave::getFluidPipeTier))
        .addElement(
            'E',
            ofBlocksTiered(
                MTEMultiAutoclave::getItemPipeTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTechAPI.sBlockCasings11, 0),
                    Pair.of(GregTechAPI.sBlockCasings11, 1),
                    Pair.of(GregTechAPI.sBlockCasings11, 2),
                    Pair.of(GregTechAPI.sBlockCasings11, 3),
                    Pair.of(GregTechAPI.sBlockCasings11, 4),
                    Pair.of(GregTechAPI.sBlockCasings11, 5),
                    Pair.of(GregTechAPI.sBlockCasings11, 6),
                    Pair.of(GregTechAPI.sBlockCasings11, 7)),
                -2,
                MTEMultiAutoclave::setItemPipeTier,
                MTEMultiAutoclave::getItemPipeTier))
        .addElement('F', withChannel("coil", ofCoil(MTEMultiAutoclave::setCoilLevel, MTEMultiAutoclave::getCoilLevel)))
        .build();

    @Override
    public IStructureDefinition<MTEMultiAutoclave> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Autoclave")
            .addInfo("Controller Block for the Industrial Autoclave.")
            .addInfo("Gains 12 parallels per item pipe casing tier.")
            .addInfo("Each pipe casing (bronze, steel, titanium, tungstensteel)")
            .addInfo("decreases the EU usageby 1/pipe tier.")
            .addInfo("Heating Coils increase speed by 1/((tier + 1) / 2).")
            .addInfo("Needs a minimum of 128 Pressure Containment Casings.")
            .addInfo(AuthorVolence)
            .addSeparator()
            .beginStructureBlock(7, 5, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Pressure Containment Casings", 128, false)
            .addCasingInfoExactly("Item Pipe Casings", 7, true)
            .addCasingInfoExactly("Pipe Casings", 14, true)
            .addCasingInfoExactly("Heating Coils", 7, true)
            .addCasingInfoExactly("PTFE Frame", 42, false)
            .addInputBus("Any of the Pressure Containment Casings", 1)
            .addOutputBus("Any of the Pressure Containment Casings", 1)
            .addEnergyHatch("Any of the Pressure Containment Casings", 1)
            .addMaintenanceHatch("Any of the Pressure Containment Casings", 1)
            .addMufflerHatch("Any of the Pressure Containment Casings", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fluidPipeTier = -2;
        itemPipeTier = -2;
        mCasingAmount = 0;
        mEnergyHatches.clear();
        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0)) return false;
        return this.mMaintenanceHatches.size() == 1 && fluidPipeTier >= 0
            && mCasingAmount >= 128
            && itemPipeTier >= 0
            && mEnergyHatches.size() >= 1
            && mMufflerHatches.size() == 1;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 3)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 3)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_AUTOCLAVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_AUTOCLAVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 3)) };
        }
        return rTexture;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 0, elementBudget, env, false, true);
        return build;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiAutoclave(this.mName);
    }

    public float euModifier(int fluidPipeTier) {
        return (float) (12 - fluidPipeTier) / 12;
    }

    public float speedBoost(int coilTier) {
        return (float) 1 / (1 + 0.25f * coilTier);
    }

    public int getMaxParallelRecipes() {
        return itemPipeTier * 12;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                euModifier = euModifier(fluidPipeTier);
                speedBoost = speedBoost(getCoilTier());
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("fluidPipeTier", getFluidPipeTier());
        tag.setInteger("itemPipeTier", getItemPipeTier());
        tag.setInteger("coilTier", getCoilTier());
        tag.setFloat("getMaxParallelRecipes", getMaxParallelRecipes());
    }

    private static final DecimalFormat dfTwo = new DecimalFormat("0.00");
    private static final DecimalFormat dfNone = new DecimalFormat("#");

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.fluidPipeTier") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("fluidPipeTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.euModifier") + ": "
                + EnumChatFormatting.WHITE
                + dfTwo.format(Math.max(0, euModifier(tag.getInteger("fluidPipeTier")) * 100))
                + "%");
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.itemPipeTier") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("itemPipeTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + dfNone.format(Math.max(0, tag.getFloat("getMaxParallelRecipes"))));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.coilLevel") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("coilTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + dfNone.format(Math.max(0, 100 / speedBoost(tag.getInteger("coilTier"))))
                + "%");
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.autoclaveRecipes;
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
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }
}
