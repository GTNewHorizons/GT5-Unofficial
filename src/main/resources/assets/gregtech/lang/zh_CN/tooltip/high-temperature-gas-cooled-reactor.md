每次运行最多消耗 {red:{var:fuel_percent}}% 的燃料，其中 {red:10}% 为固定值，{red:90}% 取决于公式 y=1-(1-x)^3（x 为填充率）
每项维护故障使冷却效率降低 {red:20}%
消耗 {red:{var:power}} EU/t，缺少氦气时最高增至 {red:{var:power_penalty}} 倍
氦气线性提升热交换器效率，储量充足时最高可达 {red:100}%
反应堆每次运行损失 {red:{var:helium_lost}}% 氦气，启动至少需要 {red:{var:min_helium}}% 氦气
单次运行时长取决于反应堆填充率（{red:{var:min_time}} 秒至 {red:{var:max_time}} 秒）
提供冷却液/水/两者可分别缩短配方时间 {red:{var:coolant_speedup}}%/{red:{var:water_speedup}}%/{red:{var:total_speedup}}%（每秒占总时间比例）
获得最大加速所需的流体量随 TRISO 燃料球数量增长：每球每刻 {red:{var:coolant_per_ball}} 冷却液和 {red:{var:water_per_ball}} 蒸馏水
