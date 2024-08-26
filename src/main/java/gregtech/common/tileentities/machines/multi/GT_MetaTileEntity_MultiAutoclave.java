package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorVolence;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_AUTOCLAVE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

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

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings10;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_MultiAutoclave extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_MultiAutoclave> implements ISurvivalConstructable {

    public GT_MetaTileEntity_MultiAutoclave(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiAutoclave(String aName) {
        super(aName);
    }

    private HeatingCoilLevel heatLevel;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    protected int itemPipeTier = 0;
    protected int fluidPipeTier = 0;

    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTech_API.sBlockCasings11) return -1;
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
        if (block != GregTech_API.sBlockCasings2) return -1;
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

    private static final IStructureDefinition<GT_MetaTileEntity_MultiAutoclave> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiAutoclave>builder()
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
            buildHatchAdder(GT_MetaTileEntity_MultiAutoclave.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Muffler, Energy)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(3))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiAutoclave::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 3))))
        .addElement('B', Glasses.chainAllGlasses()) // Steel Casings
        .addElement('C', ofFrame(Materials.Polytetrafluoroethylene)) // PTFE Frame
        .addElement(
            'D',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiAutoclave::getFluidTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings2, 12),
                    Pair.of(GregTech_API.sBlockCasings2, 13),
                    Pair.of(GregTech_API.sBlockCasings2, 14),
                    Pair.of(GregTech_API.sBlockCasings2, 15)),
                -2,
                GT_MetaTileEntity_MultiAutoclave::setFluidPipeTier,
                GT_MetaTileEntity_MultiAutoclave::getFluidPipeTier))
        .addElement(
            'E',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiAutoclave::getItemPipeTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings11, 0),
                    Pair.of(GregTech_API.sBlockCasings11, 1),
                    Pair.of(GregTech_API.sBlockCasings11, 2),
                    Pair.of(GregTech_API.sBlockCasings11, 3),
                    Pair.of(GregTech_API.sBlockCasings11, 4),
                    Pair.of(GregTech_API.sBlockCasings11, 5),
                    Pair.of(GregTech_API.sBlockCasings11, 6),
                    Pair.of(GregTech_API.sBlockCasings11, 7)),
                -2,
                GT_MetaTileEntity_MultiAutoclave::setItemPipeTier,
                GT_MetaTileEntity_MultiAutoclave::getItemPipeTier))
        .addElement(
            'F',
            withChannel(
                "coil",
                ofCoil(GT_MetaTileEntity_MultiAutoclave::setCoilLevel, GT_MetaTileEntity_MultiAutoclave::getCoilLevel)))
        .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiAutoclave> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 3)),
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
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 3)),
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
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 3)) };
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
        return new GT_MetaTileEntity_MultiAutoclave(this.mName);
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
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }
}
