package tectech.thing.metaTileEntity.multi.bec;

import static gregtech.api.casing.Casings.AdvancedFusionCoilII;
import static gregtech.api.casing.Casings.CyclotronCoil;
import static gregtech.api.casing.Casings.ElectromagneticWaveguide;
import static gregtech.api.casing.Casings.ElectromagneticallyIsolatedCasing;
import static gregtech.api.casing.Casings.FineStructureConstantManipulator;
import static gregtech.api.casing.Casings.SuperconductivePlasmaEnergyConduit;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.enums.GTValues;
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
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.boseEinsteinCondensate.CondensateStack;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;
import tectech.thing.metaTileEntity.multi.structures.BECStructureDefinitions;

public class MTEBECAssembler extends MTEBECMultiblockBase<MTEBECAssembler> implements IDataCopyable {

    private final List<MTEBECIONode> ioNodes = new ArrayList<>();

    private final List<MTEHatchNanite> naniteHatches = new ArrayList<>();

    private NaniteTier currentNaniteTier;
    private int availableNanites;

    public MTEBECAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
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
        structure.addCasing('B', ElectromagneticallyIsolatedCasing).withHatches(1, 16, Arrays.asList(Energy, ExoticEnergy, NaniteHatchElement.INSTANCE));
        structure.addCasing('C', FineStructureConstantManipulator);
        structure.addCasing('D', ElectromagneticWaveguide);
        structure.addCasing('E', CyclotronCoil);
        structure.addCasing('F', AdvancedFusionCoilII);
        structure.addCasing('G', ElectromagneticallyIsolatedCasing).withHatches(2, 2, Arrays.asList(BECHatches.Hatch));

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

        tt.addMachineType("BEC Assembler")
            .addInfo("Prints fancy atoms");

        tt.beginStructureBlock();
        tt.addHatchNameOverride(BECHatches.Hatch, CustomItemList.becConnectorHatch.get(1));
        tt.addAllCasingInfo();

        tt.toolTipFinisher(EnumChatFormatting.WHITE, 0, GTValues.AuthorPineapple);

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
    protected @NotNull CheckRecipeResult checkProcessing_EM() {
        boolean active = false;

        for (var node : ioNodes) {
            if (node.mMaxProgresstime > 0) {
                active = true;
                break;
            }
        }

        mMaxProgresstime = 20;
        mEfficiency = 10_000;

        return active ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NONE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity igte, long aTick) {
        super.onPostTick(igte, aTick);

        if (isServerSide()) {
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
                    // physical sense so that proper automation is incentivized even more.
                    node.setNaniteShare(currentNaniteTier, availableNanites);
                }

                igte.setActive(!ioNodes.isEmpty());
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

    public void drainCondensate(CondensateStack stack) {
        if (network == null) return;

        if (stack.amount > 0) {
            network.drainCondensate(this, Collections.singletonList(stack));
        }
    }

    public int getSlowdowns(Collection<Object> validMaterials) {
        return network.getSlowdowns(this, validMaterials);
    }

    @Override
    public void onUnload() {
        super.onUnload();

        if (isServerSide()) {
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

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setInteger("nanite", currentNaniteTier == null ? -1 : currentNaniteTier.ordinal());
        tag.setInteger("count", availableNanites);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        NaniteTier tier = tag.getInteger("nanite") == -1 ? null : NaniteTier.values()[tag.getInteger("nanite")];

        currenttip.add(
            MessageFormat.format(
                "Nanite Tier: {0}",
                tier == null ? "None"
                    : tier.getStack()
                        .getDisplayName()));
        currenttip.add(MessageFormat.format("Available Nanites: {0}", tag.getInteger("count")));
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
}
