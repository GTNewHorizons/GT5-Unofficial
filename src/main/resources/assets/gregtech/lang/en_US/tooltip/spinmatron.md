3 Modes: {color_mode:Light} | {color_mode:Standard} | {color_mode:Heavy}
{hr}
{color_parallel:{var:parallels} Parallels} per {color_name:Total Turbine} Tier
{color_speed:{var:speed}% Speed}
{color_efficiency:{var:eu_eff}% {EU} Usage}
{color_name:Non-Huge Turbines} have reduced effectiveness...
Gains {color_amount:{var:slots_per_tier}} {color_name:Turbine Slots} per {color_name:Structure} Tier
Requires {color_amount:(Recipe Tier * {var:fluid_per_tier} {L}/s)} of {color_key:Kerosene} to run
Supply {color_key:Biocatalyzed Propulsion Fluid} instead for a {color_parallel:{var:tier2_mult}x} Parallel increase
{color_note:Overclocks are limited to Hatch Tier + 1}
{hr}
{color_mode:Light Mode}:
- {color_speed:+{var:light_speed}% Speed Bonus}
- Maximum Recipe Tier is {aqua:(Voltage Tier - {var:light_offset})}
{hr}
{color_mode:Standard Mode}: Regular processing
{hr}
{color_mode:Heavy Mode}: Requires {color_key:Biocatalyzed Propulsion Fluid}
- Divides {color_parallel:Parallels} by {color_parallel:{var:heavy_div}}
- Multiplies {color_eu_usage:{EU} Cost} by {color_eu_usage:{var:heavy_eu}}
- {color_mechanic:Uncapped Overclocks}
- Some recipes {color_danger:require} {color_mode:Heavy Mode}
