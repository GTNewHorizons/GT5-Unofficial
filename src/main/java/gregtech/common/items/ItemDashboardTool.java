package gregtech.common.items;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.items.GTGenericItem;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;
import gregtech.client.hud.elements.CheckboxElement;
import gregtech.client.hud.elements.DroplistElement;
import gregtech.client.hud.elements.GraphElement;
import gregtech.client.hud.elements.RectElement;
import gregtech.client.hud.elements.TextElement;
import gregtech.client.hud.elements.TextInputElement;

public class ItemDashboardTool extends GTGenericItem {

    public static boolean dashboardEnabled = false;

    public ItemDashboardTool(String unlocalized, String english, String tooltip) {
        super(unlocalized, english, tooltip);
        setMaxStackSize(1);

        HUDManager hud = HUDManager.getInstance();
        hud.register();
        hud.setEditGuiSupplier(() -> new HUDGui.GuiHUDEdit(hud));
    }

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        list.add("Author: Vortex");
        list.add("Right-click to toggle dashboard HUD");
        list.add("Press RSHIFT to toggle edit mode");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            dashboardEnabled = !dashboardEnabled;
            HUDManager hud = HUDManager.getInstance();
            hud.setEnabled(dashboardEnabled);

            if (dashboardEnabled && hud.getWidgets()
                .isEmpty()) {
                createWidgets();
            }
        }

        return stack;
    }

    private void createWidgets() {
        HUDManager hud = HUDManager.getInstance();

        // 1. GraphElement: график высоты игрока
        Supplier<Double> heightSupplier = () -> {
            if (Minecraft.getMinecraft().thePlayer != null) {
                return (double) Minecraft.getMinecraft().thePlayer.posY;
            }
            return 0.0;
        };
        GraphElement heightGraphElement = new GraphElement(heightSupplier).size(120, 50);

        CompositeWidget heightGraph = new CompositeWidget(10, 10).isConfigurable();
        heightGraph.addElement(
            new RectElement().size(140, 70)
                .offset(-10, -10));
        heightGraph.addElement(heightGraphElement);
        hud.addWidget(heightGraph);

        // 2. TextElement: текущая высота игрока
        CompositeWidget heightTextWidget = new CompositeWidget(10, 90).isConfigurable();
        heightTextWidget.addElement(new TextElement(() -> {
            if (Minecraft.getMinecraft().thePlayer != null) {
                return "Player Y: " + (int) Minecraft.getMinecraft().thePlayer.posY;
            }
            return "Player Y: ?";
        }));
        hud.addWidget(heightTextWidget);

        // 3. TextInputElement: можно ввести число для теста
        CompositeWidget testInputWidget = new CompositeWidget(10, 130).isConfigurable();
        testInputWidget.addElement(new TextInputElement(text -> {
            try {
                int val = Integer.parseInt(text);
                System.out.println("Input value: " + val);
            } catch (NumberFormatException ignored) {}
        }).size(80, 18));
        hud.addWidget(testInputWidget);

        // 4. DroplistElement: пример выбора опций
        CompositeWidget droplistWidget = new CompositeWidget(10, 160).isConfigurable();
        droplistWidget.addElement(
            new DroplistElement(
                () -> Arrays.asList("Option 1", "Option 2", "Option 3"),
                0,
                selected -> System.out.println("Selected: " + selected)).size(100, 18));
        hud.addWidget(droplistWidget);

        // 5. CheckboxElement: пример включения/отключения
        CompositeWidget checkboxWidget = new CompositeWidget(10, 190).isConfigurable();
        checkboxWidget.addElement(
            new CheckboxElement("Enable feature", false, checked -> System.out.println("Checkbox: " + checked)));
        hud.addWidget(checkboxWidget);
    }

}
