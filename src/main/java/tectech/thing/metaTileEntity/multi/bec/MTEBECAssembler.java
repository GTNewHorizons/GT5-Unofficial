package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.CyclotronCoil;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import appeng.api.storage.data.IAEFluidStack;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchNanite;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.modularui2.sync.NaniteTierSyncValue;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECAssembler extends MTEBECMultiblockBase<MTEBECAssembler> implements IDataCopyable {

    private final List<MTEBECIONode> ioNodes = new ArrayList<>();

    private final List<MTEHatchNanite> naniteHatches = new ArrayList<>();

    private NaniteTier currentNaniteTier;
    private int availableNanites;

    public MTEBECAssembler(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEBECAssembler(MTEBECAssembler prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBECAssembler(this);
    }

    @Override
    public String[][] getDefinition() {
        return BECStructureDefinitions.BEC_ASSEMBLER;
    }

    @Override
    public IStructureDefinition<MTEBECAssembler> compile(String[][] definition) {
        structure.addCasing('A', SuperconductivePlasmaEnergyConduit);
        structure.addCasing('B', ElectromagneticallyIsolatedCasing)
            .withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, NaniteHatchElement.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ElectromagneticWaveguide);
        structure.addCasing('E', CyclotronCoil);
        structure.addCasing('F', AdvancedFusionCoilII);
        structure.addCasing('G', ElectromagneticallyIsolatedCasing)
            .withHatches(2, 2, Arrays.asList(BECHatches.Hatch));

        return structure.buildStructure(definition);
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();

        naniteHatches.clear();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTEBECAssembler> tt = new StructureWrapperTooltipBuilder<>(structure);

        tt.addMachineType("BEC Assembler, Observation Array")
            .addMarkdown(new ResourceLocation("gregtech", "bec-assembler"));

        tt.beginStructureBlock();
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.Hatch_BEC_Connector.get(1));
        tt.addAllCasingInfo();

        tt.toolTipFinisher(GTAuthors.AuthorPineapple);

        return tt;
    }

    @Override
    protected ITexture getCasingTexture() {
        return SuperconductivePlasmaEnergyConduit.getCasingTexture();
    }

    @Override
    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_ASSEMBLER_ACTIVE)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.condensateAssemblingRecipes;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new Gui();
    }

    @Override
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        mMaxProgresstime = 20;
        mEfficiency = 10_000;
        useLongPower = true;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (GTUtility.isServer()) {
            boolean nanitesChanged = false;

            for (MTEHatchNanite hatch : naniteHatches) {
                if (hatch.hasChanged()) {
                    nanitesChanged = true;
                    hatch.unmarkChanged();
                }
            }

            if (nanitesChanged) {
                currentNaniteTier = null;
                availableNanites = 0;

                for (MTEHatchNanite hatch : naniteHatches) {
                    NaniteTier tier = NaniteTier.fromStack(hatch.getItemStack());

                    if (tier == null) continue;

                    if (currentNaniteTier == null || tier.ordinal() < currentNaniteTier.ordinal()) {
                        currentNaniteTier = tier;
                    }

                    availableNanites += hatch.getItemCount();
                }

                Iterator<MTEBECIONode> iter = ioNodes.iterator();

                while (iter.hasNext()) {
                    MTEBECIONode node = iter.next();

                    if (node == null) {
                        iter.remove();
                        continue;
                    }

                    if (!node.isValid()) {
                        node.setNaniteShare(null, 0);
                        iter.remove();
                        continue;
                    }

                    // Intentionally share the same nanite count between every io node even though it doesn't make
                    // physical sense, so that proper automation is incentivized even more.
                    node.setNaniteShare(currentNaniteTier, availableNanites);
                }

                igte.setActive(!ioNodes.isEmpty());
            }

            lEUt = 0;

            long euInput = getMaxInputEu();

            Iterator<MTEBECIONode> iter = ioNodes.iterator();

            while (iter.hasNext()) {
                MTEBECIONode node = iter.next();

                if (node == null) {
                    iter.remove();
                    continue;
                }

                node.setPowered(false);

                if (!node.isValid()) {
                    node.setNaniteShare(null, 0);
                    iter.remove();
                    continue;
                }

                long request = node.getAssemblerEUt();

                if (euInput >= request) {
                    lEUt -= request;
                    euInput -= request;
                    node.setPowered(true);
                }
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);

        if (reason.wasCritical()) {
            // you really don't want to powerfail :tootroll:
            for (MTEBECIONode node : ioNodes) {
                if (node.isPowered()) {
                    node.stopMachine(reason);
                }
            }
        }
    }

    public void addIONode(MTEBECIONode node) {
        ioNodes.add(node);
        node.setNaniteShare(currentNaniteTier, availableNanites);
    }

    public void removeIONode(MTEBECIONode node) {
        ioNodes.remove(node);
        node.setNaniteShare(null, 0);
    }

    public void drainCondensate(IAEFluidStack stack) {
        if (network == null) return;

        network.drainCondensate(this, stack);
    }

    public int getSlowdowns(Collection<Fluid> validMaterials) {
        return network.getSlowdowns(this, validMaterials);
    }

    @Override
    public void onUnload() {
        super.onUnload();

        if (GTUtility.isServer()) {
            // copy list to prevent CMEs
            List<MTEBECIONode> nodes = new ArrayList<>(ioNodes);
            ioNodes.clear();

            for (MTEBECIONode node : nodes) {
                node.disconnect();
            }
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity igte, EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP)) return;

        ItemStack heldItem = player.getHeldItem();
        if (!ItemList.Tool_DataStick.isStackEqual(heldItem, false, true)) return;

        heldItem.setTagCompound(getCopiedData(player));
        heldItem.setStackDisplayName(
            MessageFormat.format(
                "{0} Link Data Stick ({1}, {2}, {3})",
                getStackForm(1).getDisplayName(),
                igte.getXCoord(),
                igte.getYCoord(),
                igte.getZCoord()));
        player.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        tag.setString("type", getCopiedDataIdentifier(player));
        tag.setInteger("x", igte.getXCoord());
        tag.setInteger("y", igte.getYCoord());
        tag.setInteger("z", igte.getZCoord());

        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        return false;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return "bec-assembler";
    }

    private enum NaniteHatchElement implements IHatchElement<MTEBECAssembler> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Arrays.asList(MTEHatchNanite.class);
        }

        @Override
        public IGTHatchAdder<? super MTEBECAssembler> adder() {
            return NaniteHatchElement::adder;
        }

        private static boolean adder(MTEBECAssembler assembler, IGregTechTileEntity igte, Short texture) {
            if (igte.getMetaTileEntity() instanceof MTEHatchNanite naniteHatch) {
                assembler.naniteHatches.add(naniteHatch);
                naniteHatch.updateTexture(texture);
                naniteHatch.updateCraftingIcon(assembler.getMachineCraftingIcon());

                return true;
            }

            return false;
        }

        @Override
        public String getDisplayName() {
            return ItemList.Hatch_Nanite.getDisplayName();
        }

        @Override
        public long count(MTEBECAssembler assembler) {
            return assembler.naniteHatches.size();
        }
    }

    private class Gui extends TTMultiblockBaseGui<MTEBECAssembler> {

        public Gui() {
            super(MTEBECAssembler.this);
        }

        @Override
        protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
            GenericSyncValue<CondensateList> condensate = GenericSyncValue.builder(CondensateList.class)
                .getter(
                    () -> network == null ? new CondensateList() : network.getStoredCondensate(MTEBECAssembler.this))
                .adapter(new CondensateListAdapter())
                .build();

            syncManager.syncValue("condensate", condensate);
            syncManager
                .syncValue("naniteTier", new NaniteTierSyncValue(() -> currentNaniteTier, t -> currentNaniteTier = t));
            syncManager.syncValue("naniteCount", new IntSyncValue(() -> availableNanites, i -> availableNanites = i));

            TextWidget<?> naniteWidget = IKey
                .dynamic(
                    () -> GRAY + "Providing Nanites:\n  "
                        + AQUA
                        + (currentNaniteTier == null ? "None" : currentNaniteTier.describe())
                        + GRAY
                        + " x "
                        + GOLD
                        + NumberFormatUtil.formatNumber(availableNanites)
                        + GRAY)
                .asWidget()
                .widthRel(1);

            TextWidget<?> contentsWidget = IKey.dynamic(() -> {
                StringBuilder ret = new StringBuilder();

                ret.append(GRAY)
                    .append("Available Condensate:\n");

                if (condensate.getValue()
                    .isEmpty()) {
                    ret.append(GRAY)
                        .append("None");
                }

                for (var e : condensate.getValue()
                    .object2LongEntrySet()) {
                    ret.append("  ")
                        .append(AQUA)
                        .append(CondensateType.getCondensateName(e.getKey()))
                        .append(GRAY)
                        .append(" x ")
                        .append(GOLD)
                        .append(NumberFormatUtil.formatFluid(e.getLongValue()))
                        .append(GRAY)
                        .append('\n');
                }

                return ret.toString();
            })
                .asWidget()
                .widthRel(1);

            return super.createTerminalTextWidget(syncManager, parent).child(naniteWidget)
                .child(contentsWidget);
        }
    }
}
