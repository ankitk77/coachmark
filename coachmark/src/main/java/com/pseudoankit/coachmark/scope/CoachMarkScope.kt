package com.pseudoankit.coachmark.scope

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.pseudoankit.coachmark.model.HighlightedViewConfig
import com.pseudoankit.coachmark.model.OverlayClickEvent
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.model.TooltipConfig
import com.pseudoankit.coachmark.util.CoachMarkDefaults
import com.pseudoankit.coachmark.util.CoachMarkKey

/**
 * contract containing necessary methods to interact with coachmark
 */
public interface CoachMarkScope {

    /**
     * denotes the current visible tooltip
     */
    public val currentVisibleTooltip: TooltipConfig?

    /**
     * denotes the previous visible tooltip
     */
    public val lastVisibleTooltip: TooltipConfig?

    /**
     * modifier extension to enable coachmark on a view
     * @param key unique key to be applied for a view
     * @param toolTipPlacement decides the placement of tooltip w.r.t the actual view
     * @param tooltipAnimationSpec animation spec to be applied when showing/hiding tooltip
     * @param highlightedViewConfig config to be applied to highlight the actual view when showing tooltip
     */
    public fun Modifier.enableCoachMark(
        key: CoachMarkKey,
        toolTipPlacement: ToolTipPlacement,
        tooltipAnimationSpec: AnimationSpec<Float> = CoachMarkDefaults.ToolTip.animationSpec,
        highlightedViewConfig: HighlightedViewConfig = HighlightedViewConfig(),
        minOffsetFromScreen: Dp = CoachMarkDefaults.ToolTip.minOffsetFromScreen
    ): Modifier

    /**
     * method to hide the current visible tooltip and also removes the list of keys passed by calling [show]
     */
    public fun hide()

    /**
     * method to show one or more tooltips
     * @param keys keys to be shown one after another
     * if passed multiple keys then it will be displayed sequentially on the basis of [OverlayClickEvent]
     */
    public fun show(vararg keys: CoachMarkKey)
}