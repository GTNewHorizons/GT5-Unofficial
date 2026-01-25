package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.colorString;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCI;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import goodgenerator.loader.Loaders;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.NanochipAssemblyMatrixTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEAssemblyMatrixModule extends MTENanochipAssemblyModuleBase<MTEAssemblyMatrixModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int ASSEMBLY_OFFSET_X = 3;
    protected static final int ASSEMBLY_OFFSET_Y = 4;
    protected static final int ASSEMBLY_OFFSET_Z = 0;
    protected static final String[][] ASSEMBLY_STRING = new String[][] { { "       ", "  BFB  ", " DDFDD ", "  BFB  " },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { " FFDFF ", "F  G  F", "F  G  F", "FE G EF" }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { "       ", "  BFB  ", " DDFDD ", "  BFB  " } };
    private int machineTier = -1;
    public static final IStructureDefinition<MTEAssemblyMatrixModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEAssemblyMatrixModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ASSEMBLY_STRING)
        // Iron bar
        .addElement('A', ofBlock(Blocks.iron_bars, 0))
        // CoAL casing
        .addElement(
            'B',
            ofBlocksTiered(
                (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : null,
                IntStream.range(0, 14)
                    .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                    .collect(Collectors.toList()),
                -1,
                MTEAssemblyMatrixModule::setCasingTier,
                MTEAssemblyMatrixModule::getCasingTier))
        // Nanochip Mesh Interface Casing
        .addElement('C', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('D', Casings.NanochipReinforcementCasing.asElement())
        // Naquadah Alloy Frame Box
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        // Nanochip Glass
        .addElement('F', Casings.NanochipComplexGlass.asElement())
        .addElement('G', ofSheetMetal(Materials.NaquadahAlloy))

        .build();

    private void setCasingTier(int tier) {
        this.machineTier = tier;
    }

    private int getCasingTier() {
        return this.machineTier;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_MATRIX)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_MATRIX_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    /**
     * Create new nanochip assembly module
     *
     * @param aID           ID of this module
     * @param aName         Name of this module
     * @param aNameRegional Localized name of this module
     */
    public MTEAssemblyMatrixModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEAssemblyMatrixModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.AssemblyMatrix;
    }

    @Override
    public IStructureDefinition<MTEAssemblyMatrixModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, ASSEMBLY_OFFSET_X, ASSEMBLY_OFFSET_Y, ASSEMBLY_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ASSEMBLY_OFFSET_X,
            ASSEMBLY_OFFSET_Y,
            ASSEMBLY_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = -1;
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        return checkPiece(STRUCTURE_PIECE_MAIN, ASSEMBLY_OFFSET_X, ASSEMBLY_OFFSET_Y, ASSEMBLY_OFFSET_Z);
    }

    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        for (ItemStack stack : outputItems) {
            CircuitComponent circuitComponent = CircuitComponent.tryGetFromFakeStack(stack);
            if (circuitComponent != null && baseMulti != null) {
                baseMulti.addToHistory(circuitComponent.circuitTier, stack.stackSize);
            }
        }
        return super.addItemOutputs(outputItems);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addSeparator()
            .addInfo("Assembles your Circuit Part " + TOOLTIP_CC + "s into Circuit " + TOOLTIP_CC + "s")
            .addInfo(
                "Outputs are placed in the " + TOOLTIP_VCO
                    + " with the same "
                    + colorString()
                    + " as the input "
                    + TOOLTIP_VCI)
            .addInfo("Maximum allowed recipe tier is determined by")
            .addInfo("the tier of the " + EnumChatFormatting.WHITE + "Component Assembly Line Casing")
            .addInfo("Has " + EnumChatFormatting.WHITE + EnumChatFormatting.UNDERLINE + "unlimited parallel")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "After processing your CCs through an intricate pipeline, they are finally")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "ready to be assembled into a circuit. This circuit can be converted into a real")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "item in the NAC control center, or processed further into a higher tier circuit")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAssemblyMatrixModule(this.mName);
    }

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        int recipeTier = recipe.getMetadataOrDefault(NanochipAssemblyMatrixTierKey.INSTANCE, 1) -1;
        int machineTier = getCasingTier();
        if (machineTier + 1 >= recipeTier) return CheckRecipeResultRegistry.SUCCESSFUL;
        return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", machineTier + 1);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.tier") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("tier")
                + EnumChatFormatting.RESET);
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = StatCollector.translateToLocal("scanner.info.CASS.tier")
            + (machineTier >= 0 ? GTValues.VN[machineTier + 1]
                : StatCollector.translateToLocal("scanner.info.CASS.tier.none"));
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("machineTier", machineTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineTier = aNBT.getInteger("machineTier");
    }

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipAssemblyMatrixRecipes;
    }
}
