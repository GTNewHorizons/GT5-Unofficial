package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_HATCH_INPUT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_HATCH_INPUT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_PIPE_PORT_INPUT;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.multi.nanochip.factory.IVacuumStorage;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public class MTEHatchVacuumConveyorInput extends MTEHatchVacuumConveyor {

    public MTEHatchVacuumConveyorInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] {});
    }

    public MTEHatchVacuumConveyorInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchVacuumConveyorInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { translateToLocalFormatted("GT5U.tooltip.nac.hatch.vc.base.1", TOOLTIP_COLORED),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.2"), translateToLocal("GT5U.tooltip.nac.hatch.vc.base.3"),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.input.base.1"),
            EnumChatFormatting.STRIKETHROUGH
                + "------------------------------------------------------------------------",
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.4"),
            translateToLocal("GT5U.tooltip.nac.hatch.vc.base.5") };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(OVERLAY_VACUUM_HATCH_INPUT, Dyes.getModulation(getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(OVERLAY_VACUUM_PIPE_PORT_INPUT) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(OVERLAY_VACUUM_HATCH_INPUT_ACTIVE, Dyes.getModulation(getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(OVERLAY_VACUUM_PIPE_PORT_INPUT) };
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    // Try to consume a stack of fake input items from this hatch. Returns the amount of items consumed.
    public int tryConsume(ItemStack stack) {
        if (contents == null) return 0;
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(stack);
        Map<CircuitComponent, Long> inventory = contents.getComponents();
        // Find this component in the inventory
        Long amount = inventory.get(component);
        if (amount != null) {
            // If found, consume as much as possible
            int toConsume = Math.min((int) Math.min(Integer.MAX_VALUE, amount), stack.stackSize);
            amount -= toConsume;
            if (amount > 0) {
                inventory.put(component, amount);
            } else {
                // Remove component from inventory if it is fully drained
                inventory.remove(component);
            }
            return toConsume;
        }
        return 0;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == VACUUM_MOVE_TICK) {
                IVacuumStorage[] outputs = this.getNetwork()
                    .getComponents(IVacuumStorage.class)
                    .toArray(new IVacuumStorage[0]);
                // only one output per input and they have to be on the same nac (the if check)
                if (outputs.length != 1) {
                    return;
                }
                if (this.mainController != (outputs[0].getMainController())) {
                    return;
                }

                CircuitComponentPacket newPacket = outputs[0].extractPacket();
                this.unifyPacket(newPacket);

            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
}
