package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEImplosionCompressor extends MTEExtendedPowerMultiBlockBase<MTEImplosionCompressor>
    implements ISurvivalConstructable {

    private static IStructureDefinition<MTEImplosionCompressor> STRUCTURE_DEFINITION = null;
    private int mCasing;

    public MTEImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEImplosionCompressor(this.mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 16 && mMaintenanceHatches.size() == 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public IStructureDefinition<MTEImplosionCompressor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEImplosionCompressor>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEImplosionCompressor.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, Casings.SolidSteelMachineCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Implosion Compressor")
            .addInfo("Explosions are fun")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoRange("Solid Steel Machine Casing", 16, 24, false)
            .addStructureInfo("Casings can be replaced with Explosion Hazard Signs")
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addMufflerToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.SolidSteelMachineCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.implosionRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    @Override
    protected int getTimeBetweenProcessSounds() {
        return 10;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.RANDOM_EXPLODE;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionImplosionCompressorPerSecond;
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
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }
}
