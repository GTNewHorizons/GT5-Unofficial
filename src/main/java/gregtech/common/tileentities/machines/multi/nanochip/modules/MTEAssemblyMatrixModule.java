package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_MATRIX_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

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
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEAssemblyMatrixModule extends MTENanochipAssemblyModuleBase<MTEAssemblyMatrixModule> {

    protected static final int ASSEMBLY_OFFSET_X = 3;
    protected static final int ASSEMBLY_OFFSET_Y = 4;
    protected static final int ASSEMBLY_OFFSET_Z = 0;

    public int structureOffsetX() {
        return ASSEMBLY_OFFSET_X;
    }

    public int structureOffsetY() {
        return ASSEMBLY_OFFSET_Y;
    }

    public int structureOffsetZ() {
        return ASSEMBLY_OFFSET_Z;
    }

    protected static final String[][] ASSEMBLY_STRING = new String[][] { { "       ", "  BFB  ", " DDFDD ", "  BFB  " },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { " FFDFF ", "F  G  F", "F  G  F", "FE G EF" }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { "       ", "  BFB  ", " DDFDD ", "  BFB  " } };
    private int machineTier = -1;
    public static final IStructureDefinition<MTEAssemblyMatrixModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEAssemblyMatrixModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ASSEMBLY_STRING)
        // CoAL casing
        .addElement(
            'B',
            ofBlocksTiered(
                (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta + 1 : null,
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
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_MATRIX)
                    .extFacing()
                    .build(),
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.machineTier = -1;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        for (ItemStack stack : outputItems) {
            CircuitComponent circuitComponent = CircuitComponent.tryGetFromFakeStack(stack);
            if (circuitComponent != null && baseMulti != null) {
                baseMulti.addToHistory(circuitComponent.circuitType, stack.stackSize);
            }
        }
        return super.addItemOutputs(outputItems);
    }

    @Override
    public int getOCFactorReduction() {
        return 2;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(getModuleType().getMachineModeText())
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addInfo(
                translateToLocalFormatted("GT5U.tooltip.nac.module.assembly_matrix.action", TOOLTIP_CCs, TOOLTIP_CCs))
            .addSeparator()
            .addInfo(
                translateToLocalFormatted(
                    "GT5U.tooltip.nac.module.assembly_matrix.body.1",
                    TooltipTier.COMPONENT_ASSEMBLY_LINE_CASING.getValue()))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.assembly_matrix.body.2"))
            .addInfo(translateToLocal("GT5U.tooltip.nac.module.assembly_matrix.body.3"))
            .addSeparator()
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.assembly_matrix.flavor.1")))
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.assembly_matrix.flavor.2")))
            .addInfo(tooltipFlavorText(translateToLocal("GT5U.tooltip.nac.module.assembly_matrix.flavor.3")))
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAssemblyMatrixModule(this.mName);
    }

    @Override
    public int getRecipeTier(GTRecipe recipe) {
        return recipe.getMetadataOrDefault(NanochipAssemblyMatrixTierKey.INSTANCE, 1);
    }

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        int recipeTier = recipe.getMetadataOrDefault(NanochipAssemblyMatrixTierKey.INSTANCE, 1);
        int machineTier = getCasingTier();
        if (machineTier >= recipeTier) return CheckRecipeResultRegistry.SUCCESSFUL;
        return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier);
    }

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", machineTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            translateToLocal("GT5U.machines.tier") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("tier")
                + EnumChatFormatting.RESET);
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = translateToLocal("scanner.info.CASS.tier")
            + (machineTier >= 0 ? GTValues.VN[machineTier] : translateToLocal("scanner.info.CASS.tier.none"));
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

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipAssemblyMatrixRecipes;
    }
}
