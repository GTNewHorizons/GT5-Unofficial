# Energy improvements: AE2 measurement baseline, 2026-09-07

This is the measurement-first batch for consolidated-plan item 9. The optimization portion remains next; no runtime work has yet been removed. GT production code is unchanged.

AE2 work is committed in `Applied-Energistics-2-Unofficial` as `77b88f3de` on `codex/ae2-energy-baseline`, based on `9ad45c99e`. The detailed report is that repository's `docs/energy-work-baseline-2026-09-07.md`.

The harness exercises real `EnergyGridCache`, quartz-fiber forwarding, native `GTPowerSink` and GT P2P dispatch using controlled storage/target callbacks. The fiber fixture fixes backlink-first iteration for reproducible counts. At depth 32 it measures 1,055 provider simulations during commit, 63 provider commit calls and 1,055 entries copied across 63 visited-set copies. Depths 0, 1, 2, 4, 8 and 16 are also recorded; the single-grid control has no provider work. These counts verify the quadratic repeated-suffix pattern, not TPS or CPU savings.

Fully blocked GT offers only simulate, so eliminating commit-time simulation would not accelerate them. P2P separately makes one callback per rejecting output (1–32 tested), stopping after one callback if the first output accepts the whole offer. No output deduplication is introduced.

Controls verify whole paid GT amps, partial capacity, local buffers and `extra`, alternate fiber routes, external-provider simulation/commit and seen-set separation, and capacity mutation between outer simulation and commit. The mutation case demonstrates why an unconditional cached quote is unsafe. The tests do not establish actual modpack event ordering or live downstream GT selection cost.

All 68 pre-existing AE2 tests passed before editing; all 77 tests passed afterward, including nine new measurement/control cases. Formatting and both Checkstyle tasks passed, and the final diff was reviewed. The AE2 change contains only tests, their existing GT dependency made available to the test source set, and documentation. No new benchmarking library, production API, persistent cache or correctness fix was added. No clean task ran, and the eight GT supporting reports remain intact.

Next: implement the accepted-path AE2 optimization as its own patch against this baseline, retaining unknown-provider fallback. First extend native storage event/re-entry and membership-mutation coverage sufficiently to prove any per-call reuse. Preserve whole-amp commit, request/provider order, buffers and seen rules. Actual server CPU/allocation/MSPT measurements and the wider benchmark-world steps remain outstanding. Routing and separate correctness fixes retain their planned sequence. Nothing was pushed.
