package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.CondensateGuidanceCoil;
import static gregtech.api.casing.Casings.CondensateTransformativeCoil;
import static gregtech.api.casing.Casings.ConflictInducementCasing;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.PeaceEnforcementCasing;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.api.storage.data.IAEFluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.OCMethod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.IntFraction;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.client.GTSoundLoop;
import gregtech.client.volumetric.CircularSound;
import gregtech.client.volumetric.LinearSound;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import it.unimi.dsi.fastutil.Pair;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.thing.CustomItemList;
import tectech.thing.gui.bec.MTEBECStorageGui;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchCondensateDetector;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.LongParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECStorage extends MTEBECMultiblockBase<MTEBECStorage> implements BECInventory, IParametrized {

    private final CondensateList storedCondensate = new CondensateList();
    private final HashSet<MTEHatchCondensateDetector> condensateDetectors = new HashSet<>();

    private boolean contentsChanged = false;

    public static final String FIELD_STRENGTH_PARAMETER = "fieldStrength";

    private LongParameter fieldStrengthParameter;
    private GTSoundLoop pillar, torus, torusFar;

    public MTEBECStorage(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEBECStorage(MTEBECStorage prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECStorage(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_CONTAINMENT_FIELD;
    }

    @Override
    public IStructureDefinition<MTEBECStorage> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, DetectorHatchElement.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ConflictInducementCasing);
        structure.addCasing('E', PeaceEnforcementCasing);
        structure.addCasing('F', CondensateTransformativeCoil);
        structure.addCasing('G', CondensateGuidanceCoil);
        structure.addCasing('H', ElectromagneticWaveguide);
        structure.addCasing('1', FineStructureConstantManipulator)
            .withHatches(2, 4, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        condensateDetectors.clear();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_STORAGE_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEBECStorageGui(this);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECStorage> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Storage, Entangled Condensate Storage")
            .addMarkdown(new ResourceLocation("gregtech", "bec-storage"))
            .addSupportAny();

        tt.beginStructureBlock(45, 17, 45, true)
            .addController(StatCollector.translateToLocal("GT5U.tooltip.bec-storage.controller-pos"))
            .addCasing("1045", SuperconductivePlasmaEnergyConduit.getLocalizedName(), false)
            .addCasing("1236", ElectromagneticWaveguide.getLocalizedName(), false)
            .addCasing("896", ConflictInducementCasing.getLocalizedName(), false)
            .addCasing("568", PeaceEnforcementCasing.getLocalizedName(), false)
            .addCasing("508", CondensateGuidanceCoil.getLocalizedName(), false)
            .addCasing("439-442", FineStructureConstantManipulator.getLocalizedName(), false)
            .addCasing("0-343", ElectromagneticallyIsolatedCasing.getLocalizedName(), false)
            .addCasing("292", CondensateTransformativeCoil.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("GT5U.tooltip.bec-storage.hatch-pos"), 1)
            .addMiscHatch(
                "1-4",
                "Bose-Einstein Condensate Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-storage.bec-hatch-pos"),
                2)
            .addMiscHatch(
                "0+",
                "Bose-Einstein Condensate Detector Hatch",
                StatCollector.translateToLocal("GT5U.tooltip.bec-storage.hatch-pos"),
                1)
            .toolTipFinisher(GTAuthors.AuthorPineapple);
        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return FineStructureConstantManipulator.getCasingTexture();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setTag("condensate", storedCondensate.saveToNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        storedCondensate.loadFromNBT(aNBT.getCompoundTag("condensate"));
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        useLongPower = true;
        lEUt = -fieldStrengthParameter.getValue();

        if (getAmountStored() > fieldStrengthParameter.getValue()) {
            IntFraction decay = new IntFraction(9, 10);

            storedCondensate.replaceAll((mat, amount) -> decay.apply(amount));

            storedCondensate.object2LongEntrySet()
                .removeIf(e -> e.getLongValue() <= 0);
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public double getAmountStored() {
        double sum = 0;

        for (long l : storedCondensate.values()) {
            sum += l;
        }

        return sum;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        contentsChanged = true;
        storedCondensate.clear();
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Arrays.asList(Pair.of(BECInventory.class, this));
    }

    @Override
    public @NotNull CondensateList getContents() {
        return storedCondensate;
    }

    @Override
    public double getCondensateCapacity() {
        return fieldStrengthParameter.getValue();
    }

    @Override
    public void addCondensate(IAEFluidStack stack) {
        if (mMaxProgresstime <= 0) {
            // Should be cleared by stopMachine, but just to be sure let's do it again here
            contentsChanged = true;
            storedCondensate.clear();
            return;
        }

        storedCondensate.addTo(stack.getFluid(), stack.getStackSize());
        stack.setStackSize(0);

        contentsChanged = true;
    }

    @Override
    public boolean removeCondensate(IAEFluidStack stack) {
        if (mMaxProgresstime <= 0) {
            // Should be cleared by stopMachine, but just to be sure let's do it again here
            contentsChanged = true;
            storedCondensate.clear();
            return false;
        }

        long stored = storedCondensate.getLong(stack.getFluid());

        if (stored <= 0) {
            return false;
        }

        long toConsume = Math.min(stored, stack.getStackSize());

        stack.decStackSize(toConsume);
        stored -= toConsume;

        if (stored == 0) {
            storedCondensate.removeLong(stack.getFluid());
        } else {
            storedCondensate.put(stack.getFluid(), stored);
        }

        contentsChanged = true;

        return stack.getStackSize() == 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (contentsChanged) {
            contentsChanged = false;

            for (MTEHatchCondensateDetector hatch : condensateDetectors) {
                hatch.updateAmount(this);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void doActivitySound(SoundResource activitySound) {
        if (getBaseMetaTileEntity().isActive()) {
            if (torus == null) {
                torus = new GTSoundLoop(
                    SoundResource.GT_MACHINES_BEC_GENERATOR.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                torus.setVolume(2);
                torus.setPosition(new CircularSound(this, 0, 8, 7, 0, 1, 0, 17, 90));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(torus);
            }

            if (torusFar == null) {
                torusFar = new GTSoundLoop(
                    SoundResource.GT_MACHINES_BEC_GENERATOR.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                torusFar.setVolume(2);
                torusFar.setPosition(new CircularSound(this, 0, 8, 7, 0, 1, 0, 17, -90));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(torusFar);
            }

            if (pillar == null) {
                pillar = new GTSoundLoop(
                    SoundResource.GT_MACHINES_ARC_FURNACE_LOOP.resourceLocation,
                    getBaseMetaTileEntity(),
                    false,
                    true,
                    GTSoundLoop.VOLUME_RAMP * 4);
                pillar.setVolume(2);
                pillar.setPosition(new LinearSound(this, 0, 2, 7, 0, 14, 7, 64));

                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(pillar);
            }
        } else {
            if (pillar != null) pillar.stop();
            if (torus != null) torus.stop();
            if (torusFar != null) torusFar.stop();

            pillar = null;
            torus = null;
            torusFar = null;
        }
    }

    @OCMethod
    public void setFieldStrength(long strength) {
        fieldStrengthParameter.setValue(Math.max(1, strength));
    }

    @OCMethod
    public long getFieldStrength() {
        return fieldStrengthParameter.getValue();
    }

    @OCMethod
    public List<Pair<String, Long>> getStoredCondensate() {
        return storedCondensate.object2LongEntrySet()
            .stream()
            .map(e -> Pair.of(FluidRegistry.getFluidName(e.getKey()), e.getLongValue()))
            .collect(Collectors.toList());
    }

    private static class DetectorHatchElement implements IHatchElement<MTEBECStorage> {

        public static final DetectorHatchElement INSTANCE = new DetectorHatchElement();

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchCondensateDetector.class);
        }

        @Override
        public IGTHatchAdder<? super MTEBECStorage> adder() {
            return (self, igte, texture) -> {
                IMetaTileEntity imte = igte.getMetaTileEntity();

                if (imte instanceof MTEHatchCondensateDetector hatch) {
                    hatch.updateTexture(texture);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());
                    self.condensateDetectors.add(hatch);
                    return true;
                } else {
                    return false;
                }
            };
        }

        @Override
        public String name() {
            return "DetectorHatchElement";
        }

        @Override
        public String getDisplayName() {
            return CustomItemList.Hatch_BEC_CondensateDetector.getDisplayName();
        }

        @Override
        public long count(MTEBECStorage storage) {
            return storage.condensateDetectors.size();
        }
    }

    @Override
    public void initParameters() {
        fieldStrengthParameter = new LongParameter(
            1L,
            "GT5U.gui.text.bec-field-strength",
            FIELD_STRENGTH_PARAMETER,
            () -> 1L,
            () -> Long.MAX_VALUE);
    }

    @Override
    public void loadLegacyParameters(NBTTagCompound nbt) {}

    @Override
    public List<Parameter<?, ?>> getParameters() {
        return List.of(fieldStrengthParameter);
    }
}
