package gregtech.common.tileentities.machines.multi;

import static gregtech.api.casing.Casings.AlchemicalCasing;
import static gregtech.api.casing.Casings.AlchemicalConstructTiered;
import static gregtech.api.casing.Casings.BorosilicateGlassAny;
import static gregtech.api.casing.Casings.WardedGlass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.casing.ICasingGroup;
import gregtech.api.enums.Mods;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEEntropicProcessor extends MTEExtendedPowerMultiBlockBase<MTEEntropicProcessor>
    implements ISurvivalConstructable, IStructureProvider<MTEEntropicProcessor> {

    protected final StructureWrapper<MTEEntropicProcessor> structure;
    protected final StructureWrapperInstanceInfo<MTEEntropicProcessor> structureInstanceInfo;

    public MTEEntropicProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;

        structure.loadStructure();
    }

    protected MTEEntropicProcessor(MTEEntropicProcessor prototype) {
        super(prototype.mName);

        structure = prototype.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEEntropicProcessor(this);
    }

    @Override
    public String[][] getDefinition() {
        // spotless:off
        return new String[][]{{
            "  BBB  ",
            "   B   ",
            "   ~   ",
            "   B   ",
            "  BBB  "
        },{
            " BBBBB ",
            " BA AB ",
            " BA AB ",
            " BA AB ",
            " BBBBB "
        },{
            "BBBBBBB",
            " A   A ",
            " A   A ",
            " A   A ",
            "BBBBBBB"
        },{
            "BBBBBBB",
            "B  C  B",
            "B  C  B",
            "B  C  B",
            "BBBBBBB"
        },{
            "BBBBBBB",
            " A   A ",
            " A   A ",
            " A   A ",
            "BBBBBBB"
        },{
            " BBBBB ",
            " BA AB ",
            " BA AB ",
            " BA AB ",
            " BBBBB "
        },{
            "  BBB  ",
            "   B   ",
            "   B   ",
            "   B   ",
            "  BBB  "
        }};
        // spotless:on
    }

    @Override
    public IStructureDefinition<MTEEntropicProcessor> compile(String[][] def) {
        if (Mods.Thaumcraft.isModLoaded()) {
            structure.addCasing('A', WardedGlass);
        } else {
            structure.addCasing('A', BorosilicateGlassAny);
        }

        structure.addCasing('B', AlchemicalCasing)
            .withHatches(1, 32, Arrays.asList(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy))
            .withChannel(GTStructureChannels.ALCHEMICAL_CASING);

        if (Mods.Thaumcraft.isModLoaded()) {
            structure.addCasing('C', AlchemicalConstructTiered)
                .withChannel(GTStructureChannels.ALCHEMICAL_CONSTRUCT);
        } else {
            structure.addCasing('C', AlchemicalCasing);
        }

        return structure.buildStructure(def);
    }

    @Override
    public IStructureInstance<MTEEntropicProcessor> getStructureInstance() {
        return structureInstanceInfo;
    }

    @Override
    public IStructureDefinition<MTEEntropicProcessor> getStructureDefinition() {
        return structure.structureDefinition;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        structureInstanceInfo.clearHatches();
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        structure.construct(this, trigger, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        return structure.survivalConstruct(this, trigger, elementBudget, env);
    }

    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity base) {
        boolean successful = super.checkStructure(aForceReset, base);

        base.issueTileUpdate();
        structureInstanceInfo.onPostCheck(this);

        return successful;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity base, ItemStack itemStack) {
        return structure.checkStructure(this);
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        structureInstanceInfo.validate(errors, context);
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        structureInstanceInfo.localizeStructureErrors(errors, context, lines);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEEntropicProcessor> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("machtype.entropic_processor")
            .addInfo("gt.entropic_processor.tips.1")
            .addSeparator()
            .addInfo("gt.entropic_processor.tips.2", TooltipHelper.parallelText(8), TooltipHelper.parallelText(32))
            .addSeparator()
            .addInfo("gt.entropic_processor.tips.3")
            .addSeparator()
            .addInfo("gt.entropic_processor.tips.4");

        tt.beginStructureBlock(true)
            .addAllCasingInfo();

        tt.addSubChannelUsage(GTStructureChannels.ALCHEMICAL_CASING);
        tt.addSubChannelUsage(GTStructureChannels.ALCHEMICAL_CONSTRUCT);

        tt.toolTipFinisher();

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10_000;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.entropicProcessing;
    }

    private int getCasingTier() {
        if (structureInstanceInfo == null) return 0;

        return GTUtility.clamp(structureInstanceInfo.getCasingTier(ICasingGroup.ofCasing(AlchemicalCasing), 0), 0, 2);
    }

    private int getConstructTier() {
        if (structureInstanceInfo == null) return 0;

        return GTUtility
            .clamp(structureInstanceInfo.getCasingTier(ICasingGroup.ofCasing(AlchemicalConstructTiered), 0), 0, 1);
    }

    @Override
    public int getMaxParallelRecipes() {
        return getConstructTier() == 1 ? 32 : 8;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setMachineHeat((getCasingTier() + 1) * 1800)
                    .setRecipeHeat(0)
                    .setHeatOC(true)
                    .setHeatDiscount(false);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        // Save the casing tier so that the controller background doesn't change when the world is reloaded
        // This doesn't do anything else
        aNBT.setInteger("casingTier", getCasingTier());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        structureInstanceInfo.setCasingTier(ICasingGroup.ofCasing(AlchemicalCasing), aNBT.getInteger("casingTier"));
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("casingTier", getCasingTier());

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        structureInstanceInfo.setCasingTier(ICasingGroup.ofCasing(AlchemicalCasing), data.getInteger("casingTier"));

        getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity igte, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstoneLevel) {
        List<ITexture> textures = new ArrayList<>();

        switch (getCasingTier()) {
            case 0 -> textures.add(
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.MACHINE_CASING_THAUMIUM)
                    .build());
            case 1 -> textures.add(
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.MACHINE_CASING_VOID)
                    .build());
            case 2 -> textures.add(
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.MACHINE_CASING_ICHORIUM)
                    .build());
        }

        if (side == facing) {
            if (active) {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .build());
            } else {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                        .extFacing()
                        .build());
            }
        }

        return textures.toArray(new ITexture[0]);
    }
}
