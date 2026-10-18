# Repository audit and first milestone

Audited original commit `5c937ad` (the repository's initial commit), before modifying source.
The modernization continues that history and retains package `com.example.mygame`, the three
existing screens and their XML/view-based navigation.

## Existing application

- Three `AppCompatActivity` classes: name entry → quiz → result. No fragments or navigation graph.
- One fixed list of seven four-option, single-answer questions, all repeating the same Brazil flag.
- `Question` mixes answer data with Android drawable IDs. Constants mixes intent keys and content.
- Mutable position, selection and score are owned by `QuizQuestionsActivity` with repeated
  `findViewById`, null assertions and no rotation/process recreation restoration.
- Android Gradle plugin 7.0.3, Kotlin 1.6.0, Gradle 7.0.2, compile/target SDK 31, min SDK 21.
- View Binding enabled but unused. A Material DayNight theme exists, but activities override
  it with light themes and layouts hardcode colors; effective dark-mode support is incomplete.
- Dependencies include duplicate Material declarations, JCenter, and dynamic `junit:4.+`.
- Only generated example tests: `2 + 2` and package-name instrumentation assertion.
- No README, roadmap, changelog, contribution guide, license or CI workflow.

## Prioritized findings

| Priority | Finding | First milestone action |
| --- | --- | --- |
| P0 | Unanswered submission advances the quiz | Domain transition requires an answer |
| P0 | Options remain clickable after scoring, allowing repeated points | Submitted question is locked; score recorded once |
| P1 | Recreated Activity loses quiz state | Validated snapshot restores answers, selection and position |
| P1 | Repeated questions/IDs and progress maximum 10 for seven questions | Seven distinct original questions; validated IDs/content; actual total |
| P1 | Score/navigation code tightly coupled to Android | New pure Kotlin/JVM module used by existing Activity |
| P1 | Outdated build setup and no automatic checks | JDK 17 build baseline, pinned tool versions, Android lint, ktlint and CI |
| P1 | Raster background has visible Pngtree watermark; asset provenance absent | Remove four inherited raster assets; author simple XML background/flag/trophy |
| P2 | Deprecated `systemUiVisibility` fullscreen flag | AndroidX window-insets controller |
| P2 | Repeated view lookup and null assertions | Existing View Binding used in all three activities |
| P2 | Feedback relies heavily on color | Text feedback and accessibility live region |
| P2 | Personal local IDE paths committed | Remove shared IDE XML configuration; ignore `.idea` |
| Future | Timers, question import, persistence, explanations, themes | Tracked separately in the roadmap |

## Security, privacy and ownership

A scan of tracked source/build/configuration files found no apparent API keys, passwords,
backend tokens or signing keys. This is a scoped review, not a guarantee about every historical
byte. Committed IDE metadata contained a developer-specific local user path; it is removed
from the current tree while original Git history remains intact.

The manifest requests no Internet or other permissions. No accounts, advertising or analytics
SDKs were added. A player display name is used only for the local quiz/result flow and Activity
saved state; no network transmission or history database is implemented. Existing Android
`allowBackup=true` is retained and needs review before introducing durable personal progress.

Repository ownership on GitHub does not prove ownership of all original code and assets.
The old raster artwork had no attribution/license record, and the background was visibly
watermarked. The replacement XML geometry and the new question text are authored for this
change. Existing launcher templates and inherited source still require an ownership/provenance
check before applying a project-wide open-source license. **No MIT license is asserted.**

## Architecture decision

Keep XML and Activity navigation for now. Introduce only a Kotlin/JVM domain boundary, not
Compose, Room, Hilt or a full app rewrite. ViewModel/StateFlow is the next lifecycle step once
timed sessions and asynchronous pack loading justify it. A manual constructor is sufficient
for this small dependency graph. JSON import and Room are independent future milestones.

Compile SDK 35 is a bounded toolchain upgrade. Target SDK remains 31 deliberately in this
first change: target-level behavior changes and edge-to-edge/modern platform migration require
device validation and belong in the next platform milestone. This is not a Play Store-ready
release or a claim to use the latest Android stack.

Version references: [AGP 8.7 compatibility and patch fixes](https://developer.android.com/build/releases/agp-8-7-0-release-notes),
[Kotlin Gradle compatibility](https://kotlinlang.org/docs/gradle-configure-project.html),
[Android edge-to-edge behavior](https://developer.android.com/develop/ui/views/layout/edge-to-edge).
