package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gtnhlanth.util.DescTextLocalization.addHintNumber;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gtnhlanth.common.register.LanthItemList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTEEtchingArrayModule extends MTENanochipAssemblyModuleBase<MTEEtchingArrayModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int ETCHING_OFFSET_X = 3;
    protected static final int ETCHING_OFFSET_Y = 4;
    protected static final int ETCHING_OFFSET_Z = 0;
    protected static final String[][] ETCHING_STRUCTURE = new String[][] {
        { "  EEE  ", " EBBBE ", " EBHBE ", " BBBBB " }, { "  AGA  ", " A   A ", " G F G ", "B     B" },
        { "  AGA  ", " A   A ", " G F G ", "B     B" }, { "  AGA  ", " A   A ", " G F G ", "B     B" },
        { "  AGA  ", " A   A ", " G F G ", "B     B" }, { "  AGA  ", " BCCCB ", " BCCCB ", "BBCCCBB" },
        { "  EEE  ", " EAEAE ", " EAEAE ", " BAEAB " } };

    // todo implement crystal calibration
    private MTEHatchDynamoTunnel laserSource = null;
    private int laserAmps = 0;
    private int laserTier = 0;

    public static final IStructureDefinition<MTEEtchingArrayModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEEtchingArrayModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ETCHING_STRUCTURE)
        // Nanochip Mesh Interface Casing
        .addElement('A', Casings.NanochipMeshInterfaceCasing.asElement())
        // Nanochip Reinforcement Casing
        .addElement('B', Casings.NanochipReinforcementCasing.asElement())
        // Shielded Accelerator Casing
        .addElement('C', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
        // Enriched Holmium Frame box
        .addElement('E', ofFrame(Materials.EnrichedHolmium))
        // Non-Photonic Matter Exclusion Glass
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 3))
        // Nanochip Glass
        .addElement('G', Casings.NanochipComplexGlass.asElement())
        // Laser Source hatch
        .addElement(
            'H',
            buildHatchAdder(MTEEtchingArrayModule.class).adder(MTEEtchingArrayModule::addLaserSource)
                .hatchClass(MTEHatchDynamoTunnel.class)
                .casingIndex(Casings.NanochipMeshInterfaceCasing.getTextureId())
                .hint(1)
                .build())
        .build();

    private boolean addLaserSource(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchDynamoTunnel laser) {
                laserSource = laser;
                laserSource.updateTexture(aBaseCasingIndex);
                laserAmps = (int) laserSource.maxAmperesOut();
                laserTier = (int) laserSource.getOutputTier();
                return true;
            }
        }
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public MTEEtchingArrayModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEEtchingArrayModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.EtchingArray;
    }

    @Override
    public IStructureDefinition<MTEEtchingArrayModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ETCHING_OFFSET_X,
            ETCHING_OFFSET_Y,
            ETCHING_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z)) return false;
        return laserSource != null;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(machineInfoText("Etching Array"))
            .addInfo(TOOLTIP_MODULE_DESCRIPTION)
            .addSeparator()
            .addInfo("Uses high-energy lasers to etch your Chip " + TOOLTIP_CCs)
            .addInfo(TOOLTIP_COLOR_MATCH_VCS)
            .addInfo(TOOLTIP_INFINITE_PARALLEL)
            .addSeparator()
            .addInfo(
                "In a fully calibrated Crystal NAC, the laser source hatch provides bonuses based on its attributes")
            .addSeparator()
            .addInfo(tooltipFlavorText("Perfect for etching even the finest details"))
            .addOtherStructurePart("Any Laser Source Hatch", addHintNumber(1))
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCI)
            .addStructureInfo(TOOLTIP_STRUCTURE_BASE_VCO)
            .toolTipFinisher();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("installed", laserSource != null);
        tag.setInteger("laserAmps", laserAmps);
        tag.setInteger("laserTier", laserTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("installed")) {
            currentTip.add(
                EnumChatFormatting.LIGHT_PURPLE + "Installed Laser: "
                    + tag.getInteger("laserAmps")
                    + "A "
                    + GTValues.VN[tag.getInteger("laserTier")]);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEtchingArrayModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Etched " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEtchingArray;
    }
}
