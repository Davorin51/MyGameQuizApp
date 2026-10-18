# Verification record

## Baseline attempt

The original `bash gradlew :app:testDebugUnitTest --no-daemon` was executed before changes.
It failed while downloading Gradle 7.0.2 with `java.net.SocketException: Network is unreachable`.
No baseline compilation or tests completed. This environment has a Java 17 runtime and no Android SDK,
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
the configured-build results are recorded below.
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

## Configured Android build — verified in GitHub Actions

[Successful run 39132119463](https://github.com/Davorin51/MyGameQuizApp/actions/runs/39132119463)
checked source commit `bbd0571c6027870a054d1cea5a3736b62b30eb6d` with JDK 17, Gradle 8.9,
AGP 8.7.3, Kotlin 2.1.21, compile SDK 35 and build tools 34.0.0.

| Executed task | Result |
| --- | --- |
| `:quiz-core:test` | Passed; 15 core test methods, including seeded-session regressions |
| `:app:testDebugUnitTest` | Passed; actual bundled-question adapter regression |
| `spotlessCheck` | Passed with pinned ktlint |
| `:app:lintDebug` | Passed the configured lint gate; not a claim of zero warnings |
| `:app:assembleDebug` | Passed; actual debug APK generated |
| `:quiz-core:jacocoTestReport` | Generated coverage reports; no unmeasured percentage claimed |

The workflow uploaded nonempty `verification-reports` and `debug-apk` artifacts. They are
available through the run page while GitHub retains them. The build was executed in CI,
not on the SDK-less authoring host. No release signing/store packaging was performed.

Two CI-discovered integration issues were corrected before the successful run: the duplicate
root `clean` task (already supplied by build plugins) and the nullable AndroidX window-insets
controller. The original failed runs remain in GitHub history.

The expired target-SDK issue is explicitly configured as a **warning**, because target SDK
31 behavior is preserved pending device-tested migration. Other lint errors remain fatal.
This exception is visible in `app/build.gradle`; passing lint does not mean store eligibility,
completed dark-mode support or completed accessibility/device testing.

No instrumentation/UI test, real device run or running-app screenshot has been claimed.
The original package-name instrumentation sample remains, and meaningful UI coverage is
tracked as future work. XML parsing of all 20 main XML files also passed locally (syntax only).
