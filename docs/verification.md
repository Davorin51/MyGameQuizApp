# Verification record

## Baseline attempt

The original `bash gradlew :app:testDebugUnitTest --no-daemon` was executed before changes.
It failed while downloading Gradle 7.0.2 with `java.net.SocketException: Network is unreachable`.
No baseline compilation or tests completed. This environment has JDK 17 and no Android SDK,
Android Studio, emulator, system Gradle or standalone Kotlin compiler.

## First modernization attempts

The official Gradle 8.9 binary was downloaded and its SHA-256 matched the published value:
`d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`.
The Gradle invocation for `:quiz-core:test spotlessApply` reached project configuration but
could not resolve the Android Gradle plugin through this environment's Java network path.
This is a dependency-resolution failure, not a passing test or a source compilation result.

The pinned ktlint 1.5.0 CLI completed formatting and a subsequent check with no violations.
A supplemental direct JVM compile using its embedded Kotlin 2.1.0 compiler, followed by
JUnit 4.13.2, passed **15 core tests** (including the 200 seeded random sessions). This proves
that core source/tests executed locally; it is not a Gradle/Android build or verification of
the configured Kotlin 2.1.21 build. GitHub Actions provides the separate SDK/JDK environment;
its actual configured-build result will be recorded after execution.
No emulator/device UI tests or screenshots have been produced in this environment.

## Intended regression coverage

- No unanswered skip, no repeated score, submitted-answer lock and bounded final score.
- Selection ranges, one-question/full session completion and invalid saved-state rejection.
- Restore before/after submit, between questions and at completion.
- Input-list mutation isolation; 200 seeded sessions compared with an independent score counter.
- Empty packs, IDs, prompt/options, duplicate normalized content and locale-independent checks.
- Actual bundled question adapter validates and completes with seven correct answers.

This list describes test source; only an executed test report establishes a pass.

The first CI run stopped in SDK setup because the setup action defaults to the obsolete
`tools` package, which the SDK repository no longer provides. The workflow now requests
only the specific platform/build-tools packages and platform-tools. No compile/test result
is attributed to that failed setup run.
