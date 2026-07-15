# When to mock in an IntelliJ plugin

Prefer real lightweight IntelliJ fixtures over mocks for PSI, projects, editors, files, extension lookup, and documentation flows. IntelliJ objects have lifecycle and threading semantics that ad-hoc mocks rarely reproduce.

Mock or fake only at a genuine external seam, such as:

- time, randomness, filesystem, or network access owned by the plugin;
- an external process or service;
- an expensive boundary for which the platform provides no suitable test fixture.

Do not mock:

- PSI elements merely to avoid configuring fixture text;
- the plugin's own internal classes;
- platform extension lookup when registration is behavior under test;
- read/write action or disposal behavior that a fixture can exercise.

When an external dependency must vary, define the smallest plugin-owned interface at that seam and inject an adapter. Keep IntelliJ Platform types at the platform-facing edge instead of duplicating them in a broad abstraction.
