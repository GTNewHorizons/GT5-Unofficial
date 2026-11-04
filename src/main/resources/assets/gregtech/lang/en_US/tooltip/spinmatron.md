3 Modes: {light_purple:Light} | {blue:Standard} | {dark_green:Heavy}
{hr}
{color_parallel:{var:parallels} Parallels} per {color_name:Total Turbine} Tier
{color_speed:{var:speed}% Speed}
{color_efficiency:{var:eu_eff}% {EU} Usage}
{color_name:Non-Huge Turbines} have reduced effectiveness...
Gains {var:slots_per_tier} {color_name:Turbine Slots} per {color_name:Structure} Tier
Requires {aqua:(Recipe Tier * {var:fluid_per_tier} {L}/s)} of {color_key:Kerosene} to run
Supply {color_key:Biocatalyzed Propulsion Fluid} instead for a {color_parallel:{var:tier2_mult}x} Parallel increase
{color_note:Overclocks are limited to Hatch Tier + 1}
{hr}
{light_purple:Light Mode}:
- {color_speed:+{var:light_speed}% Speed Bonus}
- Maximum Recipe Tier is {aqua:(Voltage Tier - {var:light_offset})}
{hr}
{blue:Standard Mode}: Regular processing
{hr}
{dark_green:Heavy Mode}: Requires {color_key:Biocatalyzed Propulsion Fluid}
- Divides {color_parallel:Parallels} by {color_parallel:{var:heavy_div}}
- Multiplies {red:EU Cost} by {red:{var:heavy_eu}}
- {light_purple:Uncapped Overclocks}
- Some recipes {dark_red:require} {dark_green:Heavy Mode}
