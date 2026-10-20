{aqua:{bold:Water Tier: }}{color_tier:{var:tier}}
Must be linked to a Purification Plant using a {color_name:{item:gregtech:gt.metaitem.01:32708}} to work
{hr}
At the start of the operation, the {color_name:{var:control_hatch}} will output a redstone signal
To succeed the recipe, you will need to successfully decode the instructions in the signal
To decode the signal, interpret the signal strength as a 4-bit number from 0-15
Denote the lowest bit as bit 1, and the highest as bit 4
{hr}
{white:{bold:Bit 1: }}{color_misc:{bold:Ozone Sparging by Inert Gas}}
If this bit is on, you must insert an {color_name:inert gas} into the machine
To determine which gas to insert, interpret bits {color_misc:2-3} as a 2-bit number
0: {white:{var:gas0_amount} {L} {var:gas0}} / 1: {white:{var:gas1_amount} {L} {var:gas1}} / 2: {white:{var:gas2_amount} {L} {var:gas2}} / 3: {white:{var:gas3_amount} {L} {var:gas3}}
{hr}
{white:{bold:Bit 2: }}{color_misc:{bold:Superconductive Deionization}}
If this bit is on, you must insert {white:{var:supercon_amount} {L}} of {white:Molten Superconductor Base}
Using higher tier superconductor provides bonus output
Output multiplier: {var:tier0}: {white:{var:mult0}x} / {var:tier1}: {white:{var:mult1}x} / {var:tier2}: {white:{var:mult2}x} / {var:tier3}: {white:{var:mult3}x} / {var:tier4}: {white:{var:mult4}x}
{hr}
{white:{bold:Bit 3: }}{color_misc:{bold:Gravitationally-Generated Differential Vacuum Extraction}}
If this bit is on, you must insert {white:{var:catalyst_amount} {L}} of {white:{var:catalyst}}
{hr}
{white:{bold:Bit 4: }}{color_misc:{bold:Seldonian Settlement Process}}
If this bit is on, {color_note:DISREGARD} all other bits and do not insert anything
{hr}
{white:{bold:No bits: }}{color_misc:{bold:Machine Overload}}
In rare cases, the machine may overload and output no control signal at all
To prevent machine damage, insert {color_cold:{var:coolant_amount} {L}} of {color_cold:{var:coolant}}
{hr}
The recipe can only succeed if the entire signal is decoded correctly
Inserting any fluid not requested by the signal will always void the recipe
{hr}
{aqua:{italic:The penultimate stage of water purification, step seven, is an irregular series of complex}}
{aqua:{italic:processes designed to remove any residual materials left by the decontaminants from the previous}}
{aqua:{italic:steps such as any energetic ions, acids, clarifiers, or gasses. Depending on what the Degasser}}
{aqua:{italic:detects in the water, it will request various materials to complete the processes listed above.}}
