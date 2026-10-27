{aqua:{bold:Water Tier: }}{color_tier:{var:tier}}
Must be linked to a Purification Plant using a {color_name:{item:gregtech:gt.metaitem.01:32708}} to work
{hr}
Insert {color_key:Quark Releasing Catalysts} into the input bus while running
Every recipe cycle, a different combination of {color_amount:2} different {color_key:Quark Releasing Catalysts}
will correctly identify the lone quark and succeed the recipe
The order of catalysts in the combination does not matter, so {blue:Up}/{light_purple:Down} can be identified by inserting {light_purple:Down}/{blue:Up}
{hr}
Every {color_time:{var:interval}} ticks, consumes ALL catalysts in the input bus
The base cost of inserting a catalyst is {color_name:{var:catalyst_cost} {L}} of {color_name:{var:catalyst_fluid}}
For every duplicate occurrence of an inserted catalyst in the sequence, this cost is doubled
{hr}
Keeps track of the entire sequence of catalysts inserted this recipe
If the correct catalyst combination is in the sequence of inserted catalysts, immediately outputs {color_name:{var:output} {L}} of {color_name:{var:output_fluid}}
At the end of a successful recipe, outputs additional {color_name:{var:extra_output} {L}} of {color_name:{var:output_fluid}} per parallel
At the end of the recipe, returns all incorrectly inserted catalysts in the output bus
{hr}
{aqua:{italic:The final stage of water purification goes beyond subatomic particles and identifies the smallest}}
{aqua:{italic:possible imperfections within the baryons themselves. By correctly identifying which pairs of quark}}
{aqua:{italic:flavors are required, the unit will activate the catalysts, stabilizing the errant particles.}}
{aqua:{italic:This ultimately creates both Stabilised Baryonic Matter and, most importantly, absolutely perfectly purified water.}}
