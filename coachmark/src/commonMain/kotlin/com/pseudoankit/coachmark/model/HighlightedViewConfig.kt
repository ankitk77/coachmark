package com.pseudoankit.coachmark.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pseudoankit.coachmark.scope.CoachMarkScope
import com.pseudoankit.coachmark.util.CoachMarkDefaults
import com.pseudoankit.coachmark.util.buildPath
import com.pseudoankit.coachmark.util.toPx

/**
 * the config is used to highlight the view for which tooltip is currently visible
 * @param shape shape of the view
 * @param padding padding of the view
 *
 * @see CoachMarkScope.enableCoachMark to enable coachmark in a view
 */
@Stable
public data class HighlightedViewConfig(
    val shape: Shape = Shape.Rect(12.dp),
    val padding: PaddingValues = CoachMarkDefaults.HighlightedView.padding
) {

    public sealed interface Shape {

        /**
         * @return path to be applied around the actual view when tooltip is visible
         */
        public fun pathToHighlight(density: Density, size: Size): Path

        /**
         * Denotes rectangle shape
         * @param cornerRadius corner radius of rectangle
         */
        public data class Rect(val cornerRadius: Dp) : Shape {
            override fun pathToHighlight(density: Density, size: Size): Path = buildPath {
                addRoundRect(
                    roundRect = RoundRect(
                        rect = Rect(
                            offset = Offset.Zero,
                            size = size
                        ),
                        cornerRadius = CornerRadius(cornerRadius.toPx(density))
                    )
                )
            }
        }

        /**
         * Shape that doesn't highlight the view in any way.
         */
        public object None : Shape {
            override fun pathToHighlight(
                density: Density,
                size: Size
            ): Path = Path()
        }
    }
}
