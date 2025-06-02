package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.CyclotronCoil;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.shutdown.ShutDownReason;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.thing.metaTileEntity.hatch.MTEHatchBEC;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECDiode extends MTEBECMultiblockBase<MTEBECDiode> {

    private MTEHatchBEC inputHatch;
    private boolean wasWorking;

    public MTEBECDiode(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBECDiode(MTEBECDiode prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECDiode(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_DIODE;
    }

    @Override
    public IStructureDefinition<MTEBECDiode> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing).withHatches(1, 8, Arrays.asList(Energy, ExoticEnergy));
        structure.addCasing('C', CyclotronCoil);
        structure.addCasing('D', AdvancedFusionCoilII);
        structure.addCasing('1', ElectromagneticallyIsolatedCasing).withHatches(2, 1, Arrays.asList(INPUT_HATCH));
        structure.addCasing('2', ElectromagneticallyIsolatedCasing).withHatches(3, 1, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    protected ITexture getCasingTexture() {
        return ElectromagneticallyIsolatedCasing.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_MAXWELL_GATE_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    protected List<? extends BECFactoryElement> getRoutedDiscoverySeeds() {
        if (inputHatch == null) return Collections.emptyList();
        if (mMaxProgresstime == 0) return Collections.emptyList();

        return Arrays.asList(inputHatch);
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        if (wasWorking) {
            wasWorking = false;
            network.routeTracker.onElementAdded(this);
            network.invalidateRoutes();
        }
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        useLongPower = true;
        lEUt = -TierEU.RECIPE_UIV;
        mMaxProgresstime = 1;
        mEfficiency = 10000;

        if (!wasWorking) {
            wasWorking = true;
            network.routeTracker.onElementAdded(this);
            network.invalidateRoutes();
        }

        return SimpleCheckRecipeResult.ofSuccess("routing");
    }

    private static final IHatchElement<MTEBECMultiblockBase<?>> INPUT_HATCH = new GTStructureUtility.ProxyHatchElement<>(BECHatches.Hatch) {
        @Override
        public IGTHatchAdder<? super MTEBECMultiblockBase<?>> adder() {
            var adder = super.adder();

            return (multi, hatch, textureId) -> {
                boolean success = adder.apply(multi, hatch, textureId);

                if (success) {
                    ((MTEBECDiode) multi).inputHatch = (MTEHatchBEC) hatch.getMetaTileEntity();
                }

                return success;
            };
        }
    };
}
