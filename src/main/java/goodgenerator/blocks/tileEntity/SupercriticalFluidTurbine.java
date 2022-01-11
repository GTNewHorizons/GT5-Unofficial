package goodgenerator.blocks.tileEntity;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_LargeTurbineBase;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class SupercriticalFluidTurbine extends GT_MetaTileEntity_LargeTurbineBase {

    private static final IIconContainer turbineOn = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_05");
    private static final IIconContainer turbineOff = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_15");
    private static final IIconContainer turbineEmpty = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_25");

    public SupercriticalFluidTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public SupercriticalFluidTurbine(String aName) {
        super(aName);
    }

    @Override
    public int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        return 0;
    }

    @Override
    public Block getCasingBlock() {
        return Loaders.supercriticalFluidTurbineCasing;
    }

    @Override
    public int getCasingMeta() {
        return 0;
    }

    @Override
    public int getCasingTextureIndex() {
        return 1538;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Supercritical Fluid Turbine")
                .addInfo("Controller block for Supercritical Fluid Turbine")
                .addInfo("Needs a Turbine, place inside controller")
                .addInfo("The Supercritical Fluid's temperature gives extra efficiency.")
                .addSeparator()
                .beginStructureBlock(3, 3, 4, true)
                .addController("Front center")
                .addCasingInfo("SC Turbine Casing", 24)
                .addDynamoHatch("Back center", 1)
                .addMaintenanceHatch("Side centered", 2)
                .addMufflerHatch("Side centered", 2)
                .addInputHatch("Supercritical Fluid, Side centered", 2)
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new SupercriticalFluidTurbine(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(1538),
                aFacing == aSide ?
                        (aActive ? TextureFactory.of(turbineOn) : hasTurbine() ? TextureFactory.of(turbineOff) : TextureFactory.of(turbineEmpty))
                        : Textures.BlockIcons.getCasingTextureForId(1538)
        };
    }
}
