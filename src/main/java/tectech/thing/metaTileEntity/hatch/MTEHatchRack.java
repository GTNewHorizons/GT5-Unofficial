package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.util.GTModHandler.getModItem;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTRenderedTexture;
import tectech.TecTech;
import tectech.loader.TecTechConfig;
import tectech.thing.gui.TecTechUITextures;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by Tec on 03.04.2017.
 */
public class MTEHatchRack extends MTEHatch implements IAddGregtechLogo, IAddUIWidgets {

    private static Textures.BlockIcons.CustomIcon EM_R;
    private static Textures.BlockIcons.CustomIcon EM_R_ACTIVE;
    public int heat = 0;
    private float overClock = 1, overVolt = 1;
    private static final Map<String, RackComponent> componentBinds = new HashMap<>();

    private String clientLocale = "en_US";

    public MTEHatchRack(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.rack.desc.0"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.rack.desc.1") });
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchRack(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eHeat", heat);
        aNBT.setFloat("eOverClock", overClock);
        aNBT.setFloat("eOverVolt", overVolt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat = aNBT.getInteger("eHeat");
        overClock = aNBT.getFloat("eOverClock");
        overVolt = aNBT.getFloat("eOverVolt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_R_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK_ACTIVE");
        EM_R = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(EM_R_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(EM_R) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchRack(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (aBaseMetaTileEntity.isActive() || heat > 2000) {
            return false;
        }
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (aBaseMetaTileEntity.isActive() || heat > 2000) {
            return false;
        }
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public int getSizeInventory() { // HACK TO NOT DROP CONTENTS!!!
        return heat > 2000 || getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        // if(aBaseMetaTileEntity.isActive())
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        // else if(heat>0)
        // aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        // else
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    private int getComputationPower(float overclock, float overvolt, boolean tickingComponents) {
        float computation = 0, heat = 0;
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] == null || mInventory[i].stackSize != 1) {
                continue;
            }
            RackComponent comp = componentBinds.get(TTUtility.getUniqueIdentifier(mInventory[i]));
            if (comp == null) {
                continue;
            }
            if (tickingComponents) {
                if (this.heat > comp.maxHeat) {
                    mInventory[i] = null;
                    continue;
                } else if (comp.subZero || this.heat >= 0) {
                    heat += (1f + comp.coolConstant * this.heat / 100000f)
                        * (comp.heatConstant > 0 ? comp.heatConstant * overclock * overvolt * overvolt : -10f);

                    if (overvolt > TecTech.RANDOM.nextFloat()) {
                        computation += comp.computation * (1 + overclock * overclock)
                            / (1 + (overclock - overvolt) * (overclock - overvolt));
                    }
                }
            } else {
                computation += comp.computation * (1 + overclock * overclock)
                    / (1 + (overclock - overvolt) * (overclock - overvolt)); // For getInfoData()
            }
        }
        if (tickingComponents) {
            this.heat += Math.ceil(heat);
        }
        return (int) Math.floor(computation);
    }

    public int tickComponents(float oc, float ov) {
        overClock = oc;
        overVolt = ov;
        return getComputationPower(overClock, overVolt, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == CommonValues.MULTI_CHECK_AT) {
                if (heat > 0) {
                    float heatC = 0;
                    for (int i = 0; i < mInventory.length; i++) {
                        if (mInventory[i] == null || mInventory[i].stackSize != 1) {
                            continue;
                        }
                        RackComponent comp = componentBinds.get(TTUtility.getUniqueIdentifier(mInventory[i]));
                        if (comp == null) {
                            continue;
                        }
                        if (heat - 20 > comp.maxHeat) {
                            mInventory[i] = null;
                        } else if (comp.heatConstant < 0) {
                            heatC += comp.heatConstant * (heat / 10000f);
                        }
                    }
                    heat += Math.max(-heat, Math.ceil(heatC));
                    heat -= Math.max(heat / 1000, 20);
                }
            }
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            translateToLocalFormatted("tt.keyphrase.Base_computation", clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + getComputationPower(overClock, overVolt, false),
            translateToLocalFormatted("tt.keyphrase.Heat_Accumulated", clientLocale) + ": "
                + EnumChatFormatting.RED
                + heat
                + EnumChatFormatting.RESET };
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO)
                .setSize(18, 18)
                .setPos(151, 63));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK)
                .setPos(46, 17)
                .setSize(84, 60));

        Pos2d[] positions = new Pos2d[] { new Pos2d(68, 27), new Pos2d(90, 27), new Pos2d(68, 49), new Pos2d(90, 49), };
        for (int i = 0; i < positions.length; i++) {
            builder.widget(new SlotWidget(new BaseSlot(inventoryHandler, i) {

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isEnabled() {
                    return !getBaseMetaTileEntity().isActive() && heat <= 0;
                }
            }).setBackground(getGUITextureSet().getItemSlot(), TecTechUITextures.OVERLAY_SLOT_RACK)
                .setPos(positions[i]));

            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BUTTON_STANDARD_LIGHT_16x16)
                    .setPos(152, 24)
                    .setSize(16, 16))
                .widget(
                    new DrawableWidget()
                        .setDrawable(
                            () -> getBaseMetaTileEntity().isActive() ? TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON
                                : TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED)
                        .setPos(152, 24)
                        .setSize(16, 16))
                .widget(
                    new FakeSyncWidget.BooleanSyncer(
                        () -> getBaseMetaTileEntity().isActive(),
                        val -> getBaseMetaTileEntity().setActive(val)));
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BUTTON_STANDARD_LIGHT_16x16)
                    .setPos(152, 41)
                    .setSize(16, 16))
                .widget(
                    new DrawableWidget()
                        .setDrawable(
                            () -> heat > 0 ? TecTechUITextures.OVERLAY_BUTTON_HEAT_ON
                                : TecTechUITextures.OVERLAY_BUTTON_HEAT_OFF)
                        .setPos(152, 41)
                        .setSize(16, 16))
                .widget(new FakeSyncWidget.IntegerSyncer(() -> heat, val -> heat = val));
        }
    }

    public static void run() { // 20k heat cap max!
        new RackComponent(ItemList.Circuit_Crystalprocessor.get(1), 60, 56, -1f, 2000, true); // IV
        new RackComponent(ItemList.Circuit_Crystalcomputer.get(1), 80, 54, -1f, 2000, true); // LuV
        new RackComponent(ItemList.Circuit_Ultimatecrystalcomputer.get(1), 100, 52, -1f, 2000, true); // ZPM
        new RackComponent(ItemList.Circuit_Crystalmainframe.get(1), 120, 50, -1f, 2000, true); // UV

        new RackComponent(ItemList.Circuit_Neuroprocessor.get(1), 160, 46, -1f, 4000, true); // LuV
        new RackComponent(ItemList.Circuit_Wetwarecomputer.get(1), 180, 44, -1f, 4000, true); // ZPM
        new RackComponent(ItemList.Circuit_Wetwaresupercomputer.get(1), 200, 42, -1f, 4000, true); // UV
        new RackComponent(ItemList.Circuit_Wetwaremainframe.get(1), 220, 40, -1f, 4000, true); // UHV

        new RackComponent("IC2:ic2.reactorVent", 0, -1, 40f, 2000, false); // Heat Vent
        new RackComponent("IC2:ic2.reactorVentCore", 0, -1, 80f, 4000, false); // Reactor Heat Vent
        new RackComponent("IC2:ic2.reactorVentGold", 0, -1, 120f, 6000, false); // Overclocked Heat Vent
        new RackComponent("IC2:ic2.reactorVentDiamond", 0, -1, 160f, 8000, false); // Advanced Heat Vent

        if (NewHorizonsCoreMod.isModLoaded()) {
            // GTNH-GT5u circuits (these components causes crashes when used with the original GT5u)
            new RackComponent(ItemList.Circuit_Bioprocessor.get(1), 200, 36, -1f, 6000, true); // ZPM
            new RackComponent(ItemList.Circuit_Biowarecomputer.get(1), 220, 34, -1f, 6000, true); // UV
            new RackComponent(ItemList.Circuit_Biowaresupercomputer.get(1), 240, 32, -1f, 6000, true); // UHV
            new RackComponent(ItemList.Circuit_Biomainframe.get(1), 260, 30, -1f, 6000, true); // UEV

            new RackComponent(ItemList.Circuit_OpticalProcessor.get(1), 200, 26, -1f, 8000, true); // UV
            new RackComponent(ItemList.Circuit_OpticalAssembly.get(1), 220, 24, -1f, 8000, true); // UHV
            new RackComponent(ItemList.Circuit_OpticalComputer.get(1), 240, 22, -1f, 8000, true); // UEV
            new RackComponent(ItemList.Circuit_OpticalMainframe.get(1), 260, 20, -1f, 8000, true); // UIV

            new RackComponent("dreamcraft:item.PikoCircuit", 260, 12, -1f, 9500, true); // UMV
            new RackComponent("dreamcraft:item.QuantumCircuit", 320, 10, -1f, 10000, true); // UXV
        }

        if (OpenComputers.isModLoaded()) {
            new RackComponent("OpenComputers:item.oc.CPU2", 80, 46, -1f, 2000, true); // CPU T3
            new RackComponent("OpenComputers:item.oc.GraphicsCard2", 100, 44, -1f, 2000, true); // GPU T3
            new RackComponent("OpenComputers:item.oc.APU1", 120, 42, -1f, 2000, true); // APU T3
            new RackComponent("OpenComputers:item.oc.APU2", 240, 40, -1f, 2000, true); // APU Creative
        }

        if (GraviSuite.isModLoaded()) {
            new RackComponent(getModItem(GraviSuite.ID, "itemSimpleItem", 1, 2), 0, -1, 200f, 10000, false); // CC
        }
    }

    public static class RackComponent implements Comparable<RackComponent> {

        private final String unlocalizedName;
        private final float heatConstant, coolConstant, computation, maxHeat;
        private final boolean subZero;

        RackComponent(ItemStack is, float computation, float heatConstant, float coolConstant, float maxHeat,
            boolean subZero) {
            this(TTUtility.getUniqueIdentifier(is), computation, heatConstant, coolConstant, maxHeat, subZero);
        }

        RackComponent(String is, float computation, float heatConstant, float coolConstant, float maxHeat,
            boolean subZero) {
            unlocalizedName = is;
            this.computation = computation;
            this.heatConstant = heatConstant;
            this.coolConstant = coolConstant;
            this.maxHeat = maxHeat;
            this.subZero = subZero;
            componentBinds.put(unlocalizedName, this);
            if (TecTechConfig.DEBUG_MODE) {
                TecTech.LOGGER.info("Component registered: " + unlocalizedName);
            }
        }

        @Override
        public int compareTo(RackComponent o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RackComponent) {
                return compareTo((RackComponent) obj) == 0;
            }
            return false;
        }
    }
}
