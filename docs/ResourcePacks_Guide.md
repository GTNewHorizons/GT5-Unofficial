This is a guide for resource packs to set up advanced configurations for GUI.

## Override text color with mcmeta files

You might want to change color of text if your texture has similar color to text. You can place mcmeta files at the following locations:
- `gregtech/textures/gui/background/singleblock_default.mcmeta` (most of the machines)
- `gregtech/textures/gui/background/bronze.mcmeta` (steam bronze machines)
- `gregtech/textures/gui/background/steel.mcmeta` (steam steel machines)
- `gregtech/textures/gui/background/primitive.mcmeta` (steam primitive machines)
- `gregtech/textures/gui/background/fusion_computer.mcmeta` (fusion reactor controller)
- `modularui/textures/gui/background/vanilla_background.mcmeta` (NEI)

(and there might be more in the future, but currently these are exhaustive.)

Here is an example of the file:
```json
{
  "colors": {
    "guiTint": {
      "enableGUITint": true,
      "Black":      "202020",
      "Red":        "800000",
      "Green":      "005B00",
      "Brown":      "553C00",
      "Blue":       "002456",
      "Purple":     "551839",
      "Cyan":       "007780",
      "Light Gray": "AAAAAA",
      "Gray":       "808080",
      "Pink":       "800056",
      "Lime":       "559155",
      "Yellow":     "AAA455",
      "Light Blue": "55A4AA",
      "Magenta":    "BF4095",
      "Orange":     "AA4F00",
      "White":      "FAFAFF",
      "Machine Metal": "0047AB"
    },
    "textColor": {
      "title": "FF7700",
      "title_white": "66FAFA",
      "text_white": "807BAA",
      "text_gray": "AAE055",
      "text_red": "FF2222",
      "nei": "556D8E",
      "nei_overlay_yellow": "0xFDD835"
    }
  }
}
```

## Override progress bar texture

With the transition to ModularUI, many of the textures can be reused in many places now. However, some resource packs still want to add progress bar textures for singleblock machines, unique to each type of them.
You can simply add textures named by the following rules:

- Basically place at `gregtech/textures/gui/progressbar/${unlocalized name of recipemap}`. Unlocalized name can be found on either of:
  - Hold shift while hovering over NEI tab. "HandlerID" indicates unlocalized name.
![](/docs/img/recipemap-unlocalized-name.png)
  - Read code. Usually they're passed as 2nd argument for `GT_Recipe_Map` constructor. Recipemaps are defined at `gregtech.api.util.GT_Recipe`.
- For steam machines, append `_bronze`, `_steel`, or `_primitive`.
- Exceptions: Miner: `miner`, Electric Furnace: `E_Furnace`, Electric Oven: `E_Oven`

Examples:
- `gregtech/textures/gui/progressbar/gt.recipe.laserengraver.png`
- `gregtech/textures/gui/progressbar/gt.recipe.alloysmelter_bronze.png`
- `gregtech/textures/gui/progressbar/E_Furnace.png`
