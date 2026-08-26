{light_purple:Измерения поставляются отдельно!}
Преобразует протоматерию в антиматерию
Используйте отвёртку, чтобы отключить рендеринг
Пассивно расходует {var:base_consumption} + ({dark_aqua:Антиматерия} * 1000)^(1.5 - {green:{bold:{underline:М}}}) EU/t
Потребление снижается на 0.5% каждый тик, когда он пуст
Активно использует ({dark_aqua:Антиматерия} * {var:active_base_mult})^(1.5 - {dark_purple:{bold:{underline:Г}}}) EU за цикл для производства антиматерии
{gray:{hr}}
Циклы каждую секунду
Каждый цикл записывается наименьшее количество антиматерии в 16 шлюзах антиматерии
Все шлюзы с количеством антиматерии превышающем минимальное {red:будут терять половину разницы!}
{red:Уничтожает 10% антиматерии, }если у него во время цикла заканчивается энергия/протоматерия
Базовое производство на шлюз составляет (1/16) * ({dark_aqua:Антиматерия}^(0.5 + {gold:{bold:{underline:С}}})) антиматерии каждый цикл
Каждый шлюз умножает базовую выработку на случайное число, взятое из
нормального распределения со средним значением 0.2 + {aqua:{bold:{underline:А}}} и расхождением в 1
Суммарное изменение может быть отрицательное!
{gray:{hr}}
Может быть снабжен стабилизирующими жидкостями для улучшения генерации антиматерии
Каждая стабилизация может одновременно использовать только одну из жидкостей
{green:{bold:{underline:М}}}{green:агнитная стабилизация} (Использует {dark_aqua:Антиматерию}^0.5 Л жидкости за цикл)
1. {fluid:molten.tengampurified} = {green:0.1}
2. {fluid:temporalfluid} = {green:0.2}
3. {fluid:molten.magmatter} = {green:0.3}
{dark_purple:{bold:{underline:Г}}}{dark_purple:равитационная стабилизация} (Использует {dark_aqua:Антиматерию}^0.5 Л жидкости за цикл)
1. {fluid:molten.spacetime} = {dark_purple:0.05}
2. {fluid:spatialfluid} = {dark_purple:0.10}
3. {fluid:molten.eternity} = {dark_purple:0.15}
{gold:{bold:{underline:С}}}{gold:табилизация сдерживания} (Использует {dark_aqua:Антиматерию}^(2/7) Л жидкости за цикл)
1. {fluid:molten.shirabon} = {gold:0.05}
2. {fluid:molten.magnetohydrodynamicallyconstrainedstarmatter} = {gold:0.10}
{aqua:{bold:{underline:А}}}{aqua:ктивационная стабилизация} (Использует {dark_aqua:Антиматерию}^(1/3) Л жидкости за цикл)
1. {fluid:naquadah based liquid fuel mkv (depleted)} = {aqua:0.05}
2. {fluid:naquadah based liquid fuel mkvi (depleted)} = {aqua:0.10}
