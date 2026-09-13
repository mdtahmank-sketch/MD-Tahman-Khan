package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.SystemMetrics
import com.example.ui.components.SystemHudDashboard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme {
        SystemHudDashboard(
          metrics = SystemMetrics(
            ramUsedBytes = 3600000000L,
            ramTotalBytes = 8000000000L,
            ramUsagePercent = 45,
            cpuLoadPercent = 38,
            cpuFrequencyGhz = 2.4f,
            gpuLoadPercent = 42,
            batteryPercent = 85,
            batteryTempCelsius = 34.8f,
            networkPingMs = 24
          ),
          onCleanMemoryClick = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
