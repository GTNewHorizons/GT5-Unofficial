Machine Type: {color_machine_type:BEC I/O Node Controller Hatch}
{color_mode:Pause Control}
Temporarily pauses a {color_key:{item:gregtech:gt.blockmachines:15757}} when the hatch receives a redstone signal.
Nodes can be paused immediately, or upon entering the next step.
Paused nodes do not consume condensate and do not fail due to contaminants.
{color_note:Nodes can be paused before starting a recipe, which will cause them to idle immediately when a recipe starts.}
{hr}
{color_mode:Parallel Control}
Controls the max parallel for {color_key:{item:gregtech:gt.blockmachines:15757}} when processing recipes.
The provided {color_name:Programmable Circuit} provides the {light_purple:Base} value for the {color_parallel:Parallel} calculation.
The value from physical circuit will be preferred over the ghost circuit's value, if both are supplied.
The {red:Multiplier} value starts at {color_amount:1} and increases according to the strength of the received {color_name:Redstone Signal}.
{color_parallel:Parallel}{color_formula: = Base × Multiplier}
This {color_parallel:Parallel} value overrides the Parallel setting in the {color_key:{item:gregtech:gt.blockmachines:15757}}.
Multiple {color_key:{item:gregtech:gt.blockmachines:15759}} can be used and their {color_parallel:Parallel} values will be summed up together.
