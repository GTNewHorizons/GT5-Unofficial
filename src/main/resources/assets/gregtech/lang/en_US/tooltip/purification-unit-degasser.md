{aqua:{bold:Water Tier: }}{color_tier:{var:tier}}
Must be linked to a Purification Plant using a {color_name:{item:gregtech:gt.metaitem.01:32708}} to work
{gray:{hr}}
At the start of the operation, the {color_name:{var:control_hatch}} will output a redstone signal
To succeed the recipe, you will need to successfully decode the instructions in the signal
To decode the signal, interpret the signal strength as a 4-bit number from 0-15
Denote the lowest bit as bit 1, and the highest as bit 4
{gray:{hr}}
{white:{bold:Bit 1: }}{color_misc:{bold:Ozone Sparging by Inert Gas}}
If this bit is on, you must insert an {color_name:inert gas} into the machine
To determine which gas to insert, interpret bits {color_misc:2-3} as a 2-bit number
0: {color_amount:{var:gas0_amount} {var:unit}} {color_name:{var:gas0}} / 1: {color_amount:{var:gas1_amount} {var:unit}} {color_name:{var:gas1}} / 2: {color_amount:{var:gas2_amount} {var:unit}} {color_name:{var:gas2}} / 3: {color_amount:{var:gas3_amount} {var:unit}} {color_name:{var:gas3}}
{gray:{hr}}
{white:{bold:Bit 2: }}{color_misc:{bold:Superconductive Deionization}}
If this bit is on, you must insert {color_amount:{var:supercon_amount} {var:unit}} of {color_name:Molten Superconductor Base}
Using higher tier superconductor provides bonus output
Output multiplier: {dark_green:UV}: {white:1x} / {dark_red:UHV}: {white:1.25x} / {dark_purple:UEV}: {white:1.5x} / {dark_blue:{bold:UIV}}: {white:1.75x} / {red:{bold:UMV}}: {white:2x}
{gray:{hr}}
{white:{bold:Bit 3: }}{color_misc:{bold:Gravitationally-Generated Differential Vacuum Extraction}}
If this bit is on, you must insert {color_amount:{var:catalyst_amount} {var:unit}} of {color_name:{var:catalyst}}
{gray:{hr}}
{white:{bold:Bit 4: }}{color_misc:{bold:Seldonian Settlement Process}}
If this bit is on, {color_note:DISREGARD} all other bits and do not insert anything
{gray:{hr}}
{white:{bold:No bits: }}{color_misc:{bold:Machine Overload}}
In rare cases, the machine may overload and output no control signal at all
To prevent machine damage, insert {color_amount:{var:coolant_amount} {var:unit}} of {color_name:{var:coolant}}
{gray:{hr}}
The recipe can only succeed if the entire signal is decoded correctly
Inserting any fluid not requested by the signal will always void the recipe
{gray:{hr}}
{aqua:{italic:The penultimate stage of water purification, step seven, is an irregular series of complex}}
{aqua:{italic:processes designed to remove any residual materials left by the decontaminants from the previous}}
{aqua:{italic:steps such as any energetic ions, acids, clarifiers, or gasses. Depending on what the Degasser}}
{aqua:{italic:detects in the water, it will request various materials to complete the processes listed above.}}
