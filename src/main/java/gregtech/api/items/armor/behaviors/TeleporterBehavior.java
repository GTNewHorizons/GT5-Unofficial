package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.TELEPORTER_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.ItemStackGuiData;
import com.cleanroommc.modularui.factory.ItemStackGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;

public class TeleporterBehavior implements IArmorBehavior, IGuiHolder<ItemStackGuiData> {

    public static TeleporterBehavior INSTANCE = new TeleporterBehavior();
    private final ItemStackGuiFactory GUIFactory = new ItemStackGuiFactory("gtarmor:teleportgui", this);

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.TELEPORTER_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.TELEPORTER_KEY;
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (player instanceof FakePlayer) return;
        if (!tag.getBoolean(ArmorHelper.TELEPORTER_KEY)) return;

        if (player.isSneaking()) savePlayerPositionToNBT(tag, player);
        else {
            ItemStackGuiData guiData = new ItemStackGuiData(player, stack);
            GuiManager.open(GUIFactory, guiData, (EntityPlayerMP) player);
        }
    }

    private void savePlayerPositionToNBT(NBTTagCompound tag, EntityPlayer player) {
        NBTTagList positions = tag.getTagList(ArmorHelper.TELEPORTER_POSITIONS, Constants.NBT.TAG_STRING);
        if (positions == null) positions = new NBTTagList();

        positions.appendTag(
            new NBTTagString(serializePosition("Unnamed", player.dimension, player.posX, player.posY, player.posZ)));
        tag.setTag(ArmorHelper.TELEPORTER_POSITIONS, positions);
    }

    private String serializePosition(String name, int dim, double x, double y, double z) {
        return name + "," + dim + "," + x + "," + y + "," + z;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(TELEPORTER_KEYBIND);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.teleporter");
    }

    @Override
    public ModularPanel buildUI(ItemStackGuiData data, PanelSyncManager syncManager) {
        ModularPanel panel = new ModularPanel("gtarmor:teleporter");
        ModularPanel renamePopup = new ModularPanel("gtarmor:teleporterrename");
        TextFieldWidget renameTextbox = new TextFieldWidget();

        IPanelHandler popupPanel = syncManager.panel("popup_panel", (manager, syncHandler) -> renamePopup, true);

        EntityPlayer player = data.getPlayer();
        NBTTagCompound tag = data.getTagCompound();
        NBTTagList positions = tag.getTagList(ArmorHelper.TELEPORTER_POSITIONS, Constants.NBT.TAG_STRING);

        Column buttonColumn = new Column();
        buttonColumn.top(5);

        for (int i = 0; i < positions.tagCount(); i++) {
            int finalI = i;

            int dimension = 0;
            double posX = 0, posY = 0, posZ = 0;

            if (positions.tagList.get(finalI) instanceof NBTTagString positionString) {
                List<String> posList = Arrays.asList(
                    positionString.func_150285_a_()
                        .split(","));

                try {
                    dimension = Integer.parseInt(posList.get(1));
                    posX = Double.parseDouble(posList.get(2));
                    posY = Double.parseDouble(posList.get(3));
                    posZ = Double.parseDouble(posList.get(4));
                } catch (NumberFormatException e) {
                    GTLog.out.println("ERROR: Positional NBT tag was improperly formatted!");
                    continue;
                }

                Row row = new Row();
                row.height(18)
                    .left(5);

                int fDimension = dimension;
                double fPosX = posX;
                double fPosY = posY;
                double fPosZ = posZ;

                row.child(new ButtonWidget<>().syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (player.dimension != fDimension) {
                        GTUtility.moveEntityToDimensionAtCoords(player, fDimension, fPosX, fPosY, fPosZ);
                    } else {
                        player.setPositionAndUpdate(fPosX, fPosY, fPosZ);
                    }
                }))
                    .size(72, 18)
                    .child(
                        IKey.str(posList.get(0))
                            .asWidget()
                            .center()));

                row.child(
                    new ButtonWidget<>()
                        .syncHandler(
                            new InteractionSyncHandler().setOnMousePressed(
                                mouseData -> positions.func_150304_a(
                                    finalI,
                                    new NBTTagString(serializePosition("Named", fDimension, fPosX, fPosY, fPosZ)))))
                        .size(18, 18)
                        .child(
                            IKey.str("E")
                                .asWidget()
                                .center()));

                row.child(new ButtonWidget<>().syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    positions.tagList.remove(finalI);
                    // TODO: should trigger rebuild
                    if (syncManager.isClient()) WidgetTree.resize(panel);
                }))
                    .size(18, 18)
                    .child(
                        IKey.str("X")
                            .asWidget()
                            .center()));

                buttonColumn.child(row);
            }
        }

        panel.child(buttonColumn);

        return panel;
    }
}
