HIP Unit heats up while running
When it reaches maximum heat, it becomes {color_danger:{bold:overheated!}}
This is only resolved by letting the machine fully cool down
When {color_danger:overheated}, recipes are slowed down drastically
{hr}
Some recipes {bold:require} HIP
If the machine is {color_danger:overheated} when one of these recipes starts, it will be voided!
{color_note:Cannot run recipes that require a black hole}
Read the current heat using {color_name:Heat Sensor Hatches}
{hr}
More advanced coils allow better heat control - the unit will take longer to overheat
Unit {color_hot:heats} by {color_hot:({var:heat}% x {var:coil_factor} ^ (Coil Tier - 1))} every second while running
Unit {color_cold:cools} by {color_cold:{var:base_cooling}%} base every second while not running
Cooling rate increases by an additional {color_cold:{var:cooling_increase}%} for each second since running
{hr}
{color_speed:{var:speed}%} speed of singleblock machines of the same voltage ({color_danger:{var:overheated_speed}% when overheated})
Uses {color_eu_usage:{var:eu}%} of the {EU/t} normally required ({color_danger:{var:overheated_eu}% when overheated})
{color_parallel:{var:parallels}} parallels per Voltage Tier ({color_danger:{var:overheated_parallels} when overheated})
