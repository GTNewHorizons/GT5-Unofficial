package GoodGenerator.Blocks.TEs;

import GoodGenerator.Main.GoodGenerator;
import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FuelRefineFactory extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    @SideOnly(Side.CLIENT)
    protected String textureNames;
    protected String name;
    private IStructureDefinition<FuelRefineFactory> multiDefinition = null;

    public FuelRefineFactory(String name){super(name);}

    public FuelRefineFactory(int id, String name, String nameRegional){
        super(id,name,nameRegional);
        this.name = name;
        textureNames = GoodGenerator.MOD_ID+":"+name;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {

    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new FuelRefineFactory(this.mName);
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aSide == aFacing){
            if(aActive) return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL),new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)};
            return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL),new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)};
        }
        return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL)};
    }
}
