package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import appeng.api.storage.data.IAEFluidStack;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.IntFraction;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.client.GTSoundLoop;
import gregtech.client.volumetric.CircularSound;
import gregtech.client.volumetric.LinearSound;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECStorage extends MTEBECMultiblockBase<MTEBECStorage> implements BECInventory {

    private final Object2LongOpenHashMap<Fluid> mStoredCondensate = new Object2LongOpenHashMap<>();

    public MTEBECStorage(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
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
            .withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ElectromagneticWaveguide);
        structure.addCasing('E', AdvancedFusionCoilII);
        structure.addCasing('1', FineStructureConstantManipulator)
            .withHatches(2, 4, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
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
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(
            TextWidget.dynamicString(this::generateStoredCondensateText)
                .setTextAlignment(Alignment.CenterLeft));
    }

    protected String generateStoredCondensateText() {
        StringBuffer ret = new StringBuffer();

        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);

        for (var e : mStoredCondensate.object2LongEntrySet()) {
            ret.append(EnumChatFormatting.AQUA)
                .append(
                    e.getKey().getLocalizedName(new FluidStack(e.getKey(), 1)))
                .append(EnumChatFormatting.WHITE)
                .append(" x ")
                .append(EnumChatFormatting.GOLD);
            numberFormat.format(e.getLongValue(), ret);
            ret.append(" L")
                .append(EnumChatFormatting.WHITE);
            ret.append('\n');
        }

        return ret.toString();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECStorage> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Storage")
            .addInfo("Stores fancy atoms");

        tt.beginStructureBlock();
        tt.addController("Outer wall of middle pillar, second layer from the bottom");
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.becConnectorHatch.get(1));
        tt.addAllCasingInfo(
            Arrays.asList(
                SuperconductivePlasmaEnergyConduit,
                ElectromagneticallyIsolatedCasing,
                FineStructureConstantManipulator,
                ElectromagneticWaveguide,
                AdvancedFusionCoilII),
            null);

        tt.toolTipFinisher(EnumChatFormatting.WHITE, 0, GTValues.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return FineStructureConstantManipulator.getCasingTexture();
    }

    protected Parameters.Group.ParameterIn fieldStrength;
    protected Parameters.Group.ParameterOut optimalCapacity, amountStored;

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch0 = parametrization.getGroup(0);
        fieldStrength = hatch0.makeInParameter(
            0,
            TierEU.LV,
            (t, iParameter) -> "Field Strength (EU/t)",
            (t, iParameter) -> iParameter.get() < 0 ? LedStatus.STATUS_TOO_LOW : LedStatus.STATUS_OK);
        optimalCapacity = hatch0
            .makeOutParameter(0, 0, (t, iParameter) -> "Optimal Capacity (L)", (t, iParameter) -> LedStatus.STATUS_OK);
        amountStored = hatch0
            .makeOutParameter(1, 0, (t, iParameter) -> "Total Stored Condensate (L)", (t, iParameter) -> {
                double ratio = amountStored.get() / optimalCapacity.get();

                if (ratio < 0.5) return LedStatus.STATUS_OK;
                if (ratio < 1) return LedStatus.STATUS_HIGH;
                return LedStatus.STATUS_TOO_HIGH;
            });
    }

    private long getOptimalCapacity(long fieldStrength) {
        return fieldStrength;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setTag("condensate", writeCondensateToTag(mStoredCondensate));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        mStoredCondensate.clear();
        mStoredCondensate.putAll(readCondensateFromTag(aNBT.getCompoundTag("condensate")));
    }

    @Override
    public void onPreTick(IGregTechTileEntity base, long aTick) {
        super.onPreTick(base, aTick);

        if (!base.isAllowedToWork() && !base.isActive() && aTick % 20 == 0) {
            long fieldStrength = (long) this.fieldStrength.get();
            long optimalCapacity = getOptimalCapacity(fieldStrength);
            this.optimalCapacity.set(optimalCapacity);
        }
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        long fieldStrength = (long) this.fieldStrength.get();
        long optimalCapacity = getOptimalCapacity(fieldStrength);
        this.optimalCapacity.set(optimalCapacity);

        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        useLongPower = true;
        lEUt = -fieldStrength;

        double stored = mStoredCondensate.values()
            .longStream()
            .mapToDouble(l -> l)
            .sum();

        amountStored.set(stored);

        if (stored > optimalCapacity) {
            IntFraction decay = new IntFraction(9, 10);

            mStoredCondensate.replaceAll((mat, amount) -> decay.apply(amount));

            mStoredCondensate.values()
                .rem(0);
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        mStoredCondensate.clear();
    }

    @Override
    public List<Pair<Class<?>, Object>> getComponents() {
        return Arrays.asList(Pair.of(BECInventory.class, this));
    }

    @Override
    public @NotNull Object2LongMap<Fluid> getContents() {
        return mStoredCondensate;
    }

    @Override
    public void addCondensate(IAEFluidStack stack) {
        mStoredCondensate.addTo(stack.getFluid(), stack.getStackSize());
        stack.setStackSize(0);

        double stored = 0;

        for (long l : mStoredCondensate.values()) {
            stored += l;
        }

        amountStored.set(stored);
    }

    @Override
    public boolean removeCondensate(IAEFluidStack stack) {
        long stored = mStoredCondensate.getLong(stack.getFluid());

        if (stored <= 0) {
            return false;
        }

        long toConsume = Math.min(stored, stack.getStackSize());

        stack.decStackSize(toConsume);
        stored -= toConsume;

        if (stored == 0) {
            mStoredCondensate.removeLong(stack.getFluid());
        } else {
            mStoredCondensate.put(stack.getFluid(), stored);
        }

        double storedTotal = 0;

        for (long l : mStoredCondensate.values()) {
            storedTotal += l;
        }

        amountStored.set(storedTotal);

        return stack.getStackSize() == 0;
    }

    private GTSoundLoop pillar, torus, torusFar;

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
                torus.setPosition(new CircularSound(this, 0, 8, 7, 0, 1, 0, 17, -90));

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
                torus.setPosition(new LinearSound(this, 0, 2, 7, 0, 14, 7, 64));

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
}
