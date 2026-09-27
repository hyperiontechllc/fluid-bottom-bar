package dev.hyperiontech.fluid_bottom_bar.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

@Composable
internal actual fun SystemBarsAppearance(isDarkTheme: Boolean) {
    LaunchedEffect(key1 = isDarkTheme) {
        val style =
            if (isDarkTheme) {
                UIUserInterfaceStyle.UIUserInterfaceStyleDark
            } else {
                UIUserInterfaceStyle.UIUserInterfaceStyleLight
            }
        UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
            .forEach { window -> window.overrideUserInterfaceStyle = style }
    }
}
