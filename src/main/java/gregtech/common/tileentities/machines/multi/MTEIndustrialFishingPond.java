package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.Collection;
import java.util.List;

import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.TooFewCasings;
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
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StructureErrorId;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialFishingPond extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFishingPond>
    implements ISurvivalConstructable {

    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;

    public static final int FISH_MODE = 14;
    public static final int JUNK_MODE = 15;
    public static final int TREASURE_MODE = 16;

    private static String[][] shape = { { "           ", "    CCC    ", "    C~C    ", "    CCC    " },
        { "           ", "  CC A CC  ", "  CCDBDCC  ", "  CCCCCCC  " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "    CCC    ", "C   CCC   C", "CDDDCCCDDDC", "CCCCCCCCCCC" },
        { "    CCC    ", "CAAACCCAAAC", "CBBBCCCBBBC", "CCCCCCCCCCC" },
        { "    CCC    ", "C   CCC   C", "CDDDCCCDDDC", "CCCCCCCCCCC" },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", "  CC A CC  ", "  CCDBDCC  ", "  CCCCCCC  " },
        { "           ", "    CCC    ", "    CCC    ", "    CCC    " } };
    private static IStructureDefinition<MTEIndustrialFishingPond> STRUCTURE_DEFINITION;
    private int casingAmount;

    public MTEIndustrialFishingPond(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialFishingPond(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFishingPond(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fish Trap")
            .addInfo("Can process (Tier + 1) * 2 recipes")
            .addInfo("Put a numbered circuit into the input bus")
            .addInfo("Circuit " + FISH_MODE + " for Fish")
            .addInfo("Circuit " + JUNK_MODE + " for Junk")
            .addInfo("Circuit " + TREASURE_MODE + " for Treasure")
            .addInfo("Needs to be filled with water")
            .addInfo("Will automatically fill water from input hatch")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(11, 4, 11, false)
            .addController("Front center")
            .addCasingInfoMin("Aquatic Casings", 160, false)
            .addCasingInfoExactly("Stainless Steel Frame Box", 12, false)
            .addCasingInfoExactly("Stainless Steel Sheetmetal", 12, false)
            .addInputBus("Any Aquatic Casing", 1)
            .addOutputBus("Any Aquatic Casing", 1)
            .addInputHatch("Any Aquatic Casing", 1)
            .addEnergyHatch("Any Aquatic Casing", 1)
            .addMaintenanceHatch("Any Aquatic Casing", 1)
            .addMufflerHatch("Any Aquatic Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFishingPond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialFishingPond>builder()
                .addShape(mName, shape)
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialFishingPond.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                        .casingIndex(Casings.AquaticCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.AquaticCasing.asElement())))
                .addElement('A', ofFrame(Materials.StainlessSteel))
                .addElement('B', ofSheetMetal(Materials.StainlessSteel))
                .addElement('D', ofChain(isAir(), ofAnyWater(false)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(mName, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors) {
        super.validateStructure(errors);

        if (casingAmount < 160) {
            errors.add(new TooFewCasings(casingAmount, 160));
        }
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureErrorId> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        if (errors.contains(StructureErrorId.TOO_FEW_CASINGS)) {
            lines.add(
                StatCollector
                    .translateToLocalFormatted("GT5U.gui.missing_casings", 160, context.getInteger("casings")));
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.AquaticCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.AquaticCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)
                .extFacing()
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.AquaticCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.fishPondRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!checkForWater()) return SimpleCheckRecipeResult.ofFailure("no_water");
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * (GTUtility.getTier(this.getMaxInputVoltage()) + 1));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialFishingPond;
    }

    private boolean checkForWater() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        World world = aBaseMetaTileEntity.getWorld();
        int controllerX = aBaseMetaTileEntity.getXCoord();
        int controllerY = aBaseMetaTileEntity.getYCoord();
        int controllerZ = aBaseMetaTileEntity.getZCoord();

        boolean allFilled = true;

        for (int sliceZ = 0; sliceZ < shape.length; sliceZ++) {
            String[] layers = shape[sliceZ];
            for (int layerY = 0; layerY < layers.length; layerY++) {
                String row = layers[layerY];
                for (int charX = 0; charX < row.length(); charX++) {
                    if (row.charAt(charX) != 'D') continue;

                    int[] abc = new int[] { charX - OFFSET_X, layerY - OFFSET_Y, sliceZ - OFFSET_Z };
                    int[] xyz = new int[] { 0, 0, 0 };
                    getExtendedFacing().getWorldOffset(abc, xyz);
                    int wx = controllerX + xyz[0];
                    int wy = controllerY + xyz[1];
                    int wz = controllerZ + xyz[2];

                    Block block = world.getBlock(wx, wy, wz);
                    int meta = world.getBlockMetadata(wx, wy, wz);
                    if (block == Blocks.water && meta == 0) continue;

                    boolean isReplaceable = block == Blocks.air || block == Blocks.flowing_water
                        || ((block == Blocks.water) && meta > 0);
                    if (isReplaceable) {
                        boolean consumed = false;
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(Materials.Water.getFluid(1)) && stored.amount >= 1000) {
                                    stored.amount -= 1000;
                                    world.setBlock(wx, wy, wz, Blocks.water, 0, 3);
                                    consumed = true;
                                    break;
                                }
                            }
                        }
                        if (!consumed) allFilled = false;
                    } else {
                        allFilled = false;
                    }

                }
            }
        }

        return allFilled;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxParallelRecipes", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("maxParallelRecipes"));
    }
}
