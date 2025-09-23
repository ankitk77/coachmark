package com.pseudoankit.coachmark.overlay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.model.TooltipConfig
import com.pseudoankit.coachmark.util.CoachMarkDefaults

/** Containing composable must use these values for layoutId on current and previous tooltip. */
public object TooltipId {
    public const val current: Int = 1
    public const val previous: Int = 2
}

/** Proposed minimum useful tooltip width; this can be adjusted if needed */
private const val TOOLTIP_MAX_WIDTH_OVERRIDE_PX = 30

/**
 * Extracted from DimOverlayEffect, in case another overlay effect needs it in the future.
 *
 * @param configCurrent of coach mark highlight; null suits case where there's no "current" tooltip in content
 * @param configPrevious of coach mark highlight; null suits case where there's no "previous" tooltip in content
 * @param paddingForTooltip min distance between tooltip and left/right side of screen/overlay
 */
@Composable
public fun OverlayLayout(
    configCurrent: TooltipConfig?,
    configPrevious: TooltipConfig?,
    modifier: Modifier = Modifier,
    paddingForTooltip: Dp = CoachMarkDefaults.ToolTip.paddingForTooltip,
    content: @Composable @UiComposable () -> Unit,
) {
    Layout(content, modifier) { measurables, constraints ->

        // child count < 2 occurs on first and last coach mark
        require(measurables.size <= 2) { "OverlayLayout cannot have more than two children" }

        val gapTooltipScreenPx = paddingForTooltip.roundToPx()

        // measure children
        val placeableCurrent = measure(
            tooltipConfig = configCurrent,
            layoutId = TooltipId.current,
            measurables = measurables,
            constraintsParent = constraints,
            gapTooltipScreenPx = gapTooltipScreenPx
        )
        val placeablePrevious = measure(
            tooltipConfig = configPrevious,
            layoutId = TooltipId.previous,
            measurables = measurables,
            constraintsParent = constraints,
            gapTooltipScreenPx = gapTooltipScreenPx
        )

        // place children
        layout(constraints.maxWidth, constraints.maxHeight) {
            place(placeableCurrent, configCurrent, constraints.maxWidth, constraints.maxHeight)
            place(placeablePrevious, configPrevious, constraints.maxWidth, constraints.maxHeight)
        }
    }
}


/**
 * @param layoutId use consts from TooltipId
 * @return null if tooltipConfig is null
 */
private fun measure(
    tooltipConfig: TooltipConfig?,
    layoutId: Int,
    measurables: List<Measurable>,
    constraintsParent: Constraints,
    gapTooltipScreenPx: Int,
): Placeable? {
    if (tooltipConfig == null) return null

    // constrain max width to prevent tooltip running off screen
    var maxWidth = when (tooltipConfig.toolTipPlacement) {
        ToolTipPlacement.Start -> {
            tooltipConfig.layout.startX.toInt() - gapTooltipScreenPx // left edge of highlight, minus overlay padding
        }

        ToolTipPlacement.End -> {
            constraintsParent.maxWidth - gapTooltipScreenPx - tooltipConfig.layout.endX.toInt()
        }

        // Top and Bottom: allow full screen width, minus edge padding
        else -> constraintsParent.maxWidth - (gapTooltipScreenPx shl 1)
    }

    /*
        We can't currently constraint max height to prevent tooltip running off screen, because text
        already uses as much width as it can by default, so there's no horizontal space to trade.
    */

    /*
        This is mainly intended to avoid a crash when calculated max width is less than zero, such
        as when tooltip is positioned to "End" while highlight is very close to right side of screen.
        This fail-soft approach, where we display the tooltip running off the screen, allows the
        developer to see the problem at runtime without having to check the log.
     */
    if (maxWidth < TOOLTIP_MAX_WIDTH_OVERRIDE_PX) {
        maxWidth = Constraints.Infinity
    }

    val constraintsChild = Constraints(
        minWidth = 0,
        minHeight = 0,
        maxWidth = maxWidth,
        maxHeight = constraintsParent.maxHeight,
    )

    return measurables.find { it.layoutId == layoutId }?.measure(constraintsChild)
}

/**
 * Centralizes null checks and switching on toolTipPlacement value.
 * @param placeable no-op if null
 * @param config no-op if null
 * @param containerWidth the width of the container overlay
 * @param containerHeight the height of the container overlay
 */
private fun Placeable.PlacementScope.place(
    placeable: Placeable?, config: TooltipConfig?,
    containerWidth: Int,
    containerHeight: Int
) {
    if (placeable != null && config != null) {
        val layout = config.layout
        var x = 0
        var y = 0

        // result positive when highlight is larger, negative when tooltip is larger
        fun calculateCenteringOffset(independentHeight: Int, dependentHeight: Int): Int =
            (independentHeight - dependentHeight) shr 1

        fun centerVertically() =
            (layout.startY + calculateCenteringOffset(layout.height, placeable.height)).toInt()

        fun centerHorizontally() =
            (layout.startX + calculateCenteringOffset(layout.width, placeable.width)).toInt()

        fun centerVerticallyToScreen() = (containerHeight - placeable.height) / 2

        fun centerHorizontallyToScreen() = (containerWidth - placeable.width) / 2

        when (config.toolTipPlacement) {
            ToolTipPlacement.Start -> {
                x = layout.startX.toInt() - placeable.width
                y = centerVertically()
            }

            ToolTipPlacement.End -> {
                x = layout.endX.toInt()
                y = centerVertically()
            }

            ToolTipPlacement.Top -> {
                x = centerHorizontally()
                y = layout.startY.toInt() - placeable.height
            }

            ToolTipPlacement.Bottom -> {
                x = centerHorizontally()
                y = layout.endY.toInt()
            }

            ToolTipPlacement.ScreenCenter -> {
                x = centerHorizontallyToScreen()
                y = centerVerticallyToScreen()
            }
        }

        placeable.placeRelative(x, y)
    }
}
