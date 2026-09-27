package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.geometry.Offset

internal object MeshTokens {
    const val BASE_RADIUS_FRACTION = 0.85f
    const val BASE_MIDDLE_STOP = 0.5f
    const val BASE_CORE_ALPHA = 0.55f
    const val BASE_MIDDLE_ALPHA = 0.40f
    const val BASE_EDGE_ALPHA = 0.30f

    const val CORE_RADIUS_SCALE = 3.2f
    const val CORE_ALPHA = 0.5f

    val SweepOffset = Offset(x = 1.5f, y = 0.7f)
    const val SWEEP_RADIUS_SCALE = 2.2f
    const val SWEEP_ALPHA = 0.45f

    const val ACCENT_RADIUS_FRACTION = 0.32f
    const val ACCENT_ALPHA = 0.30f

    const val END_TINT_RADIUS_FRACTION = 0.22f
    const val END_TINT_ALPHA = 0.25f

    const val FLOW_REST_INTENSITY = 0.35f
    const val ACCENT_DRIFT_FRACTION = 0.2f
    const val SWEEP_DRIFT_SCALE = 1.4f
    const val END_TINT_DRIFT_FRACTION = 0.1f
    const val VERTICAL_DRIFT_SCALE = 0.5f
    val AccentDriftX = DriftWave(frequency = 0.5f, offset = 0f)
    val AccentDriftY = DriftWave(frequency = 0.7f, offset = 1.3f)
    val StartTintDriftX = DriftWave(frequency = 0.9f, offset = 3f)
    val StartTintDriftY = DriftWave(frequency = 1.1f, offset = 0.6f)
    val EndTintDriftX = DriftWave(frequency = 0.8f, offset = 4.2f)
    val EndTintDriftY = DriftWave(frequency = 1.2f, offset = 2.4f)
    val SweepDriftX = DriftWave(frequency = 0.8f, offset = 2.1f)
    val SweepDriftY = DriftWave(frequency = 0.6f, offset = 0.4f)
}
