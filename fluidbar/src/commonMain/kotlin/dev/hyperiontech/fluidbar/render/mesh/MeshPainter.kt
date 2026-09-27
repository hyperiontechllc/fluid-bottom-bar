package dev.hyperiontech.fluidbar.render.mesh

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.hyperiontech.fluidbar.render.drawUnitGlow
import dev.hyperiontech.fluidbar.render.drawUnitRect

internal fun DrawScope.drawMesh(
    glows: PaletteGlows,
    mesh: MeshFrame,
) {
    drawBaseGradient(brush = glows.base, mesh = mesh)
    drawAccent(brush = glows.accent, mesh = mesh)
    drawEndTint(
        brush = glows.startTint,
        anchorX = mesh.bounds.left,
        driftX = MeshTokens.StartTintDriftX,
        driftY = MeshTokens.StartTintDriftY,
        mesh = mesh,
    )
    drawEndTint(
        brush = glows.endTint,
        anchorX = mesh.bounds.right,
        driftX = MeshTokens.EndTintDriftX,
        driftY = MeshTokens.EndTintDriftY,
        mesh = mesh,
    )
    drawSweep(brush = glows.sweep, mesh = mesh)
    drawUnitGlow(
        brush = glows.core,
        center = mesh.focus,
        radius = mesh.blobRadius * MeshTokens.CORE_RADIUS_SCALE,
    )
}

private val MeshFrame.verticalReach: Float
    get() = blobRadius * MeshTokens.VERTICAL_DRIFT_SCALE

private fun DrawScope.drawBaseGradient(
    brush: Brush,
    mesh: MeshFrame,
) {
    val radius = mesh.bounds.width * MeshTokens.BASE_RADIUS_FRACTION
    drawUnitRect(
        brush = brush,
        bounds = mesh.bounds,
        origin = mesh.focus,
        scaleX = radius,
        scaleY = radius,
    )
}

private fun DrawScope.drawAccent(
    brush: Brush,
    mesh: MeshFrame,
) {
    val bounds = mesh.bounds
    val mirroredFocusX = bounds.left + bounds.right - mesh.focus.x
    val horizontalReach = bounds.width * MeshTokens.ACCENT_DRIFT_FRACTION
    val driftX = mesh.flow.drift(wave = MeshTokens.AccentDriftX) * horizontalReach
    val driftY = mesh.flow.drift(wave = MeshTokens.AccentDriftY) * mesh.verticalReach
    drawUnitGlow(
        brush = brush,
        center = Offset(x = mirroredFocusX + driftX, y = mesh.focus.y + driftY),
        radius = bounds.width * MeshTokens.ACCENT_RADIUS_FRACTION,
    )
}

private fun DrawScope.drawEndTint(
    brush: Brush,
    anchorX: Float,
    driftX: DriftWave,
    driftY: DriftWave,
    mesh: MeshFrame,
) {
    val horizontalReach = mesh.bounds.width * MeshTokens.END_TINT_DRIFT_FRACTION
    val center =
        Offset(
            x = anchorX + mesh.flow.drift(wave = driftX) * horizontalReach,
            y = mesh.focus.y + mesh.flow.drift(wave = driftY) * mesh.verticalReach,
        )
    drawUnitGlow(
        brush = brush,
        center = center,
        radius = mesh.bounds.width * MeshTokens.END_TINT_RADIUS_FRACTION,
    )
}

private fun DrawScope.drawSweep(
    brush: Brush,
    mesh: MeshFrame,
) {
    val blobRadius = mesh.blobRadius
    val restingCenter = mesh.focus + MeshTokens.SweepOffset * blobRadius
    val horizontalReach = blobRadius * MeshTokens.SWEEP_DRIFT_SCALE
    val drift =
        Offset(
            x = mesh.flow.drift(wave = MeshTokens.SweepDriftX) * horizontalReach,
            y = mesh.flow.drift(wave = MeshTokens.SweepDriftY) * mesh.verticalReach,
        )
    drawUnitGlow(
        brush = brush,
        center = restingCenter + drift,
        radius = blobRadius * MeshTokens.SWEEP_RADIUS_SCALE,
    )
}
