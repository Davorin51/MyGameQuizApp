# Development roadmap

Estimates reflect this small three-screen app, its missing data layer and limited tests.
They include implementation and meaningful verification, assume one Android engineer, and
exclude store-review delays. They are planning ranges, not hours already spent.

| Milestone | Concrete remaining work and acceptance criteria | Estimate |
| --- | --- | --- |
| 1. Lifecycle architecture | ViewModel/StateFlow, stable session UI state, pack-version-aware restoration; recreation tests | 4–6 h |
| 2. Question packs | Versioned JSON schema, bounded file import, detailed validation, normalized duplicate policy, sample packs and selection | 5–8 h |
| 3. Engine options | Categories, difficulty, seeded selection, explanations, explicit multi-answer exact-match scoring policy with tests | 4–7 h |
| 4. Timed quizzes | Monotonic-clock timer, pause/background policy, expiry/submit race handling and fake-clock tests | 4–6 h |
| 5. Local persistence | Room schema/migrations, repository, transactions, interrupted-write handling and repository tests | 5–8 h |
| 6. Progress | History, personal bests, category totals, accurately labeled percentages, delete/reset data flow | 3–5 h |
| 7. Material XML UX | Coherent light/dark themes, loading/errors, tablets/landscape, text scaling and consistent navigation | 5–8 h |
| 8. Accessibility and platform | TalkBack feedback, contrast/touch/focus checks, current target SDK/edge-to-edge, API 21 + modern-device regression | 4–7 h |
| 9. Local achievements | Transparent thresholds based on persisted real completions, replay/idempotency tests | 2–3 h |
| 10. Release verification | Emulator UI suite, real screenshots, dependency locks/verification, signing guide, asset/source ownership and license resolution | 4–6 h |

**Remaining estimate: 40–64 engineering hours.** Re-estimate after data import and lifecycle
work. Do not treat this range as a commitment or increase it to satisfy a portfolio metric.

## First implemented slice

Domain extraction, single-answer scoring/validation, existing-screen integration, unique
sample questions, instance-state snapshot support and initial build/quality automation.
See `docs/verification.md` for what was actually executed. No future row above is marked done.
