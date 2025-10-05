package com.pseudoankit.coachmark.model

import androidx.compose.runtime.Immutable

/**
 * denotes the placement of tooltip w.r.t  the actual view
 */
@Immutable
public enum class ToolTipPlacement {

    /**
     * denotes that tooltip will be placed at left/start of actual view
     */
    Start,

    /**
     * denotes that tooltip will be placed at right/end of actual view
     */
    End,

    /**
     * denotes that tooltip will be placed at top of actual view
     */
    Top,

    /**
     * denotes that tooltip will be placed at bottom of actual view
     */
    Bottom,

    /**
     * denotes that tooltip will be placed at the center of the screen, regardless of the actual view.
     */
    ScreenCenter

}
