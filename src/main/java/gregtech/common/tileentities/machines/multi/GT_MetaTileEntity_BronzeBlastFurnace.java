package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.SteamVariant;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_BronzeBlastFurnace extends GT_MetaTileEntity_PrimitiveBlastFurnace {

    private static final ITexture[] FACING_SIDE = { TextureFactory.of(MACHINE_BRONZEPLATEDBRICKS) };
    private static final ITexture[] FACING_FRONT = { TextureFactory.of(MACHINE_BRONZEBLASTFURNACE) };
    private static final ITexture[] FACING_ACTIVE = { TextureFactory.of(MACHINE_BRONZEBLASTFURNACE_ACTIVE),
        TextureFactory.builder()
            .addIcon(MACHINE_BRONZEBLASTFURNACE_ACTIVE_GLOW)
            .glow()
            .build() };

    public GT_MetaTileEntity_BronzeBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_BronzeBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Disabled" };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
        boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BronzeBlastFurnace(this.mName);
    }

    @Override
    protected Block getCasingBlock() {
        return GregTech_API.sBlockCasings1;
    }

    @Override
    protected int getCasingMetaID() {
        return 10;
    }

    @Override
    public String getName() {
        return "Bronze Blast Furnace";
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }
}
