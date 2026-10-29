Farms and harvests trees using {EU}
Place a {color_key:Sapling} in the controller and a {color_key:Tool} in an input bus
{hr}
Different tools are required for different outputs:
{color_key:Logs}: {color_name:Saw} {color_speed:({var:tool_multiplier_logs_saw}x)}, {color_name:Buzzsaw} {color_speed:({var:tool_multiplier_logs_buzzsaw}x)}, {color_name:Chainsaw} {color_speed:({var:tool_multiplier_logs_chainsaw}x)}
{color_key:Saplings}: {color_name:Branch Cutter} {color_speed:({var:tool_multiplier_saplings_branch_cutter}x)}, {color_name:Grafter} {color_speed:({var:tool_multiplier_saplings_grafter}x)}
{color_key:Leaves}: {color_name:Shears} {color_speed:({var:tool_multiplier_leaves_shear}x)}, {color_name:Wire Cutter} {color_speed:({var:tool_multiplier_leaves_wire_cutter}x)}, {color_name:Electric Wire Cutter} {color_speed:({var:tool_multiplier_leaves_electric_wire_cutter}x)}
{color_key:Fruit}: {color_name:Knife} {color_speed:({var:tool_multiplier_fruit_knife}x)}
{color_note:Multiple tools can be used at the same time}
{hr}
Work time is fixed at {color_time:{var:ticks_per_operation}}
{color_name:Energy Input Tier} multiplies output further
{color_amount:Output multiplier =} {color_formula:2 * (Tier^2) - (2 * Tier) + 5}
