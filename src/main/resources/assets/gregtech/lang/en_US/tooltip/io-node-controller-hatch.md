Machine Type: {color_machine_type:BEC I/O Node Controller Hatch}
{color_mode:Pause Control}
Temporarily pauses a {color_key:{item:gregtech:gt.blockmachines:15757}} when the hatch receives a redstone signal.
Nodes can be paused immediately, or upon entering the next step.
Paused nodes do not consume condensate and do not fail due to contaminants.
Nodes can be paused before starting a recipe, which will cause them to idle immediately when a recipe starts.
{hr}
{color_mode:Parallel Control}
Controls the max parallel for {color_key:{item:gregtech:gt.blockmachines:15757}} when doing recipes
{color_name:Programmable Circuit} provides the {light_purple:Base} value for {yellow:Parallel}
A physical {color_name:Programmable Circuit} will override the {light_purple:Base} value from ghost circuit
The {red:Multiplier} value starts from {color_amount:1} and increase based on the amount of {color_name:Redstone Signal} received
{yellow:Parallel} = {light_purple:Base} × {red:Multiplier}
The {yellow:Parallel} overrides the parallel set in the {color_key:{item:gregtech:gt.blockmachines:15757}}
While {yellow:Parallel} from other {color_key:{item:gregtech:gt.blockmachines:15759}} will adds to the current overridden {yellow:Parallel}
