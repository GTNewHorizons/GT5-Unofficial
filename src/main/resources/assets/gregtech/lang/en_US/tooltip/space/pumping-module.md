Module that adds Space Pumping Operations to the Space Elevator
{light_purple:{bold:{lang-eval:gt.blockmachines.multimachine.project.ig.pump.t{var:moduleTier}.lore}}}
{lang:gt.blockmachines.multimachine.project.ig.desc2}
Set Planet and Gas Type determine pumped fluid
Can be void protected by locking output hatches
Runs at {VNF:voltageTier} with up to {color_parallel:{var:parallels}} parallels
{ifndef:parallel_recipes:Processes {color_amount:1} recipe at once}{ifdef:parallel_recipes:Processes up to {color_amount:{var:parallel_recipes}} different recipes at once}
{lang-eval:gt.blockmachines.multimachine.project.ig.motorT{var:motorTier}}
