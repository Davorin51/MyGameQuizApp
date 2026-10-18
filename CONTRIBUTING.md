# Contributing

Use JDK 17 and the checked-in Gradle wrapper. Install Android SDK platform 35 and build tools
34.0.0 through Android Studio, accepting their licenses yourself. Keep local paths in ignored
`local.properties`; never commit signing keys, credentials or player data.

For a change, start a branch, add a regression test for the relevant behavior, and run:

```bash
./gradlew :quiz-core:test :app:testDebugUnitTest :app:lintDebug :app:assembleDebug spotlessCheck
```

Use `./gradlew spotlessApply` to apply the pinned Kotlin formatter, and inspect the diff.
Core tests need no emulator. Device tests require an attached/emulated Android device and use
`./gradlew :app:connectedDebugAndroidTest`. Do not report them as passing unless actually run.

Keep XML screens and application ID unless a reviewed migration explicitly requires otherwise.
Domain logic belongs in `quiz-core`; Android drawables/navigation belong in `app`. Introduce
libraries only for concrete implemented needs. Avoid accounts, trackers and advertising.

Document provenance for any contributed code/artwork. Project-wide licensing is pending an
ownership audit; do not assume this repository grants redistribution rights to old assets or
code. Discuss licensing before contributing externally sourced material.
