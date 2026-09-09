加速粒子束流，以使其通过相对论性拉莫尔效应产生光子束流
因此电中性§5粒子§7和§2聚焦§7低于25的束流不受影响
{lang:gtnhlanth.tt.beaminfo}
{gray:{hr}}
运行需要消耗§f 32,000 {var:fluidUnit}/s §7的§b冷却剂§7，并返回§c热冷却剂
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{gray:{hr}}
提升功率可提升§6输出束流通量
使用温度更低的§b冷却液§7能降低§2束流聚焦§7的损耗
升级§d天线机械方块§7的等级以提升机器性能
以下公式中的所有§9能量§7单位必须为千电子伏特（keV）
{gray:{hr}}
§9输出束流能量§f = §eIR §f* §e功率系数
其中 §eIR §f = (§9输入束流能量§f)^(1.13 * (§d天线等级§f)^(4/9)) / 40,000,000
且 §e功率系数§f = 1 - 0.15^(§3总输入EU/t§f / (30,384 * (§d天线等级§f)^2.5))
{gray:{hr}}
若 §2输入束流聚焦§f > §e机器聚焦§7，则 §2输出束流聚焦§f = (§2输入束流聚焦§f + §e机器聚焦§f) / 2.5
若 §2输入束流聚焦§f ≤ §e机器聚焦度§f，则 §2输出束流聚焦§f = §2输入束流聚焦§f * §e机器聚焦度§f / 100
其中 §e机器聚焦§f = max(10, min(90, 1.5^(12 - §b冷却剂温度§f/40)))
{gray:{hr}}
§6输出束流通量§f = floor( 2.5^(§d天线等级§f) * sqrt(§3总输入EU/t§f) * §6输入束流通量§f / 15,000)
