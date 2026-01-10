## Machines comparison using ZPM any circuits

This study compares the **quantity produced, time, and energy consumed** for ZPM any circuits crafting using one or more CAL, and 3 ACAL with different settings.

The target is **ZPM any circuits**. For the CAL, we will ignore the Forge hammer recipe because with UMV it is 1 tick for many items; its presence or absence is irrelevant.

The study is done on ZPM circuits because, for now, it's the only recipe that both machines are able to complete in **one step**. We use **debug inputs and infinite wireless energy**, so we ignore limits on resources and energy.

Here is a description of 5 machines:

* **Machine 1** is the CAL, with energy set via one debug energy hatch set to 1 Amp UMV.
* **Machine 2** is the CAL, with energy set via one debug energy hatch set to 1 Amp UXV.
* **Machine 3** is the ACAL with default settings: MaxParallel = 64, Expected Duration = 70 sec.
* **Machine 4** is the ACAL, with MaxParallel = 512, Expected Duration = 70 sec.
* **Machine 5** is the ACAL, with MaxParallel = 512, Expected Duration = 15 sec.

*Note: The last two columns show how many CALs of the given tier are required to match the throughput.*

| Machine       | ZPM Produced / Time (s) | Energy Used            | Energy per Second  | Energy per Item | **Scale UMV CAL** | **Scale UXV CAL** |
| ------------- | ----------------------- | ---------------------- | ------------------ | --------------- | ----------------- | ----------------- |
| **Machine 1** | 5 / sec                 | 2.5165824 × 10⁹ / s    | 2.5165824 × 10⁹    | 5.0331648 × 10⁸ | **X**             | **X**             |
| **Machine 2** | 22 / sec                | 1.073741824 × 10¹⁰ / s | 1.073741824 × 10¹⁰ | ≈ 4.88 × 10⁸    | **X**             | **X**             |
| **Machine 3** | 42 / sec                | 6.74 × 10⁸             | ≈ 9.63 × 10⁶       | ≈ 2.29 × 10⁵    | **9**             | **2**             |
| **Machine 4** | 336 / sec               | 5.39 × 10⁹             | ≈ 7.70 × 10⁷       | ≈ 2.29 × 10⁵    | **68**            | **16**            |
| **Machine 5** | 1570 / sec              | 2.52 × 10¹⁰            | ≈ 1.68 × 10⁹       | ≈ 1.07 × 10⁶    | **314**           | **72**            |

The inputs of the ACAL are **MaxParallel** and **Expected Duration**. I used the **OverclockCalculator algorithm** with:

* The same settings as the TPM for parallel (DAH)
* A ×20 factor on expected duration to match what is displayed in the ACAL terminal

The ACAL can instantly deliver items (if Expected Duration = 1, the minimum). See below:

| Machine       | Settings    | ZPM Produced / Time (s) | Energy Used | Energy per Second | Energy per Item | **Scale UMV CAL** | **Scale UXV CAL** |
| ------------- | ----------- | ----------------------- | ----------- | ----------------- | --------------- | ----------------- | ----------------- |
| **Machine 6** | P=2048, D=1 | 94,208 items / sec      | 1.51 × 10¹² | 1.51 × 10¹²       | ≈ 1.60 × 10⁷    | 18,842            | 1,801             |
| **Machine 7** | P=1, D=1    | 46 items / sec          | 7.37 × 10⁸  | 7.37 × 10⁸        | ≈ 1.60 × 10⁷    | 10                | 3                 |

## PRO / CON Comparison

### CAL

**Pros:**

* Better for low volume and instant delivery
* Small
* Can craft multi-step circuits
* Can swap recipe for the same circuit
* Perfect overclock

**Cons:**

* Scaling requires many units
* Setup is complex (inputs, buses/hatches, subnet, MM, TE, maintenance hatch, energy)
* You have to change the energy hatch to upscale/downscale

### ACAL

**Pros:**

* Better for high volume and batch crafting
* Scaling depends only on energy input
* Less space required for better results
* One ACAL can pattern different circuits
* Faster, cheaper, one-step circuit crafting
* Makes any circuit efficiently

**Cons:**

* Multiple machines needed to craft different circuits simultaneously
* Bad for low-volume or instant delivery
* Cannot craft multi-step circuits (only "any"; cannot be upgraded)
* Not perfect overclock
