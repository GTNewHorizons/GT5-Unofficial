Insert an electrode in the {color_name:Electrode Hatch}
Speed, parallel, OC, and power usage depend on the electrode
Some electrodes have special effects, check their tooltip
Below {red:{var:surge_threshold}%} durability: {red:{var:surge_chance}%} chance for an {color_danger:Arc Surge}
Ignites the arc before each recipe, ignition power scales with electrode penalty and parallels
Powers down the arc after work ends
{hr}
{color_mode:Blast Mode}:
Processes {color_name:EBF} recipes at {color_amount:{var:blast_power_multiplier}x} power cost
{gray:{hr}}
{color_mode:Ore Mode}:
Processes metallic ores into molten metal
Startup time: {color_amount:{var:ore_startup_seconds}} seconds
Consumes ores and raw ores from {color_name:Input Buses}
If all parallels are used up, startup ends immediately
If no ores enter for {color_amount:{var:ore_idle_ticks}} ticks, startup ends immediately
Outputs {color_key:Molten Metals}
{color_note:Only furnace-smeltable ores, no blasting}
{hr}
Right-click the controller with a {color_name:Screwdriver} to change mode
