package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.PerformanceMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Game Booster - DarLink XArena", appName)
  }

  @Test
  fun `verify darlink performance modes loaded correctly`() {
    val modes = PerformanceMode.entries
    assertTrue(modes.isNotEmpty())
    assertTrue(modes.any { it == PerformanceMode.BEAST })
    assertTrue(modes.any { it == PerformanceMode.BALANCED })
    assertTrue(modes.any { it == PerformanceMode.BATTERY_SAVER })
  }
}
