# CuteSteps 💗

A cute, girly Android step counter app. Pink everywhere, pixel font everywhere.

## Features

- **Today's steps** — big pixel-font number on a rounded card, with a progress bar
  toward a 10,000-step daily goal.
- **Last 7 days** — a rounded pink bar chart with weekday labels; today's bar is
  highlighted in a deeper pink.
- **Cute UI** — Needy Girl Overdose-style Y2K look: pink polka-dot desktop,
  white "windows" with pink title bars, window buttons, chunky pink borders,
  heart/star stickers, and the Press Start 2P pixel font. Content respects
  `safeDrawing` insets, so nothing hides behind the camera cutout/status bar.
- **Pixel font** — "Press Start 2P" from Google Fonts, loaded as a downloadable
  font resource (`res/font/press_start_2p.xml`, prefetched via `preloaded_fonts`)
  and applied to all text in Compose.

## How it works

- Reads the hardware `TYPE_STEP_COUNTER` sensor (cumulative steps since boot)
  through `SensorManager` in `data/StepRepository.kt`.
- The repository stores a per-day baseline (`base_yyyy-MM-dd`) and end-of-day
  (`end_yyyy-MM-dd`) cumulative value in `SharedPreferences`, so it can compute
  steps for today and each of the previous 6 days. Old entries are pruned.
- While the app is open, the sensor listener updates the UI live. A
  `PeriodicWorkRequest` (`data/StepSamplerWorker.kt`, every 15 minutes) takes a
  one-shot sensor reading so history keeps filling even when the app is closed.
- On Android 10+ the app requests the `ACTIVITY_RECOGNITION` runtime permission
  before reading the sensor.

Note: `TYPE_STEP_COUNTER` counts since device boot, so "today" is measured from
the first reading of the day (or since reboot). For production-grade history,
swap the repository for a Health Connect `STEPS` reader.

## Build

Requires JDK 17 and Android Studio Hedgehog (2023.1.1) or newer.

```bash
# from the project root, generate the Gradle wrapper jar if missing
gradle wrapper

# then build
./gradlew assembleDebug
```

Or simply open the `CuteSteps` folder in Android Studio and press Run.
The first launch on a device without Google Play services will fall back to the
system font; on normal devices the pixel font is downloaded automatically.

## Project layout

```
app/src/main/java/com/example/cutsteps/
├── MainActivity.kt            # entry point, schedules background sampling
├── CuteStepsApp.kt            # permission flow + root composable
├── data/
│   ├── StepRepository.kt      # sensor reading + 7-day history in SharedPreferences
│   └── StepSamplerWorker.kt   # WorkManager one-shot sampler (every 15 min)
└── ui/
    ├── HomeScreen.kt          # today card + 7-day chart
    ├── StepViewModel.kt       # state + permission handling
    └── theme/Theme.kt         # pink palette + Press Start 2P pixel font
```

## Customize the look

- Colors: `ui/theme/Theme.kt` (`Pink100`…`Pink700`, `Berry`).
- Font: change `GoogleFont(name = "Press Start 2P")` to any Google Font, e.g.
  `"Silkscreen"` or `"VT323"` for other pixel styles.
- Daily goal: `DAILY_GOAL` in `ui/HomeScreen.kt`.
- Launcher icon: replace `res/drawable/ic_launcher.xml` (currently a pink heart)
  with your own art; for adaptive icons add `res/mipmap-anydpi-v26/ic_launcher.xml`.
