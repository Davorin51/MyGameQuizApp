# Changelog

## Unreleased — quiz foundation

- Extract Android-independent question validation and single-answer session/scoring.
- Prevent unanswered skips, repeated scoring and edits after submission.
- Restore validated session snapshots after Activity recreation.
- Use existing View Binding, actual question totals, textual feedback and cleaner navigation.
- Replace repeated sample data with seven distinct original geography questions.
- Replace inherited raster artwork, including a watermarked background, with original XML graphics.
- Upgrade build baseline to Gradle 8.9, AGP 8.7.3, Kotlin 2.1.21 and JDK 17; compile SDK 35.
- Keep target SDK 31 and min SDK 21 pending platform/device migration.
- Add JVM tests, sample-pack regression, Kotlin formatting, Android lint and CI.
- Remove developer-specific IDE configuration and document remaining work/licensing limits.

Original initial commit is retained. No release APK or emulator verification is implied here.
