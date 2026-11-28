{light_purple:Dimensions not included!}
Converts Protomatter into Antimatter
Use a {white:Screwdriver} to disable rendering
{color_eu_usage:Passive consumption =} {white:{var:base_consumption} + ({dark_aqua:Antimatter} * {var:passive_base_mult})^({var:passive_exponent} - {green:{bold:{underline:M}}})} EU/t
The consumption decays by {color_amount:{var:decay_percent}%} every tick when empty
{color_eu_usage:Actively uses =} {white:({dark_aqua:Antimatter} * {var:active_base_mult})^({var:active_exponent} - {dark_purple:{bold:{underline:G}}})} EU/cycle to produce antimatter
{hr}
Cycles every {color_amount:{var:cycle_seconds}} second
Every cycle, the lowest antimatter amount across {color_amount:{var:hatch_count}} hatches is recorded
{color_amount:Base Production per Hatch =} {white:({var:production_divisor}) * ({dark_aqua:Antimatter}^({var:production_base_exponent} + {color_parallel:{bold:{underline:C}}}))} antimatter each cycle
Each hatch multiplies its base production by a random number drawn from a normal distribution
with a mean of {white:{var:distribution_base_mean} + {color_amperage:{bold:{underline:A}}}} and a variance of {color_amount:{var:distribution_variance}}
{color_note:The total gain of antimatter can be negative!}
Hatches exceeding this amount {color_danger:lose {var:excess_loss_percent}% of the difference!}
{color_danger:Voids {var:failure_void_percent}% of antimatter} if it runs out of energy/protomatter during a cycle
{hr}
Stabilization fluids improve antimatter generation, with only one fluid usable per type at a time
{green:{bold:{underline:M}}}agnetic Stabilization ({white:{dark_aqua:Antimatter}^{var:magnetic_fluid_exponent}{L}/cycle})
1. {fluid:molten.tengampurified} = {green:{var:magnetic_tengam_bonus}}
2. {fluid:temporalfluid} = {green:{var:magnetic_temporal_bonus}}
3. {fluid:molten.magmatter} = {green:{var:magnetic_magmatter_bonus}}
{dark_purple:{bold:{underline:G}}}ravity Stabilization ({white:{dark_aqua:Antimatter}^{var:gravity_fluid_exponent}{L}/cycle})
1. {fluid:molten.spacetime} = {dark_purple:{var:gravity_spacetime_bonus}}
2. {fluid:spatialfluid} = {dark_purple:{var:gravity_spatial_bonus}}
3. {fluid:molten.eternity} = {dark_purple:{var:gravity_eternity_bonus}}
{gold:{bold:{underline:C}}}ontainment Stabilization ({white:{dark_aqua:Antimatter}^{var:containment_fluid_exponent}{L}/cycle})
1. {fluid:molten.shirabon} = {gold:{var:containment_shirabon_bonus}}
2. {fluid:molten.magnetohydrodynamicallyconstrainedstarmatter} = {gold:{var:containment_starmatter_bonus}}
{aqua:{bold:{underline:A}}}ctivation Stabilization ({white:{dark_aqua:Antimatter}^{var:activation_fluid_exponent}{L}/cycle})
1. {fluid:naquadah based liquid fuel mkv (depleted)} = {aqua:{var:activation_mkv_bonus}}
2. {fluid:naquadah based liquid fuel mkvi (depleted)} = {aqua:{var:activation_mkvi_bonus}}
