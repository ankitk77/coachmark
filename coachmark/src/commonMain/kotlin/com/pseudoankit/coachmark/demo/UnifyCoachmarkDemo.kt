package com.pseudoankit.coachmark.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pseudoankit.coachmark.LocalCoachMarkScope
import com.pseudoankit.coachmark.UnifyCoachmark
import com.pseudoankit.coachmark.model.HighlightedViewConfig
import com.pseudoankit.coachmark.model.OverlayClickEvent
import com.pseudoankit.coachmark.model.ToolTipPlacement
import com.pseudoankit.coachmark.overlay.DimOverlayEffect
import com.pseudoankit.coachmark.scope.enableCoachMark
import com.pseudoankit.coachmark.shape.Arrow
import com.pseudoankit.coachmark.shape.Balloon
import com.pseudoankit.coachmark.util.CoachMarkKey

public enum class Keys { Text1, Text2, TextCenter, TextStart, TextBottom, TextTop }


@Composable
public fun UnifyCoachmarkDemo() {
    UnifyCoachmark(
        tooltip = { Tooltip(it) },
        overlayEffect = DimOverlayEffect(Color.Black.copy(alpha = .5f)),
        onOverlayClicked = { OverlayClickEvent.GoNext }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlotTextsAndUseLocalCoachMarkScope()
            Button(
                onClick = { show(Keys.Text1) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Highlight 1")
            }
            Button(
                onClick = { show(*Keys.values()) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Highlight All")
            }
            Button(
                onClick = { show(Keys.TextBottom, Keys.TextTop) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Highlight Some")
            }
        }
    }
}

@Composable
private fun ColumnScope.PlotTextsAndUseLocalCoachMarkScope() {

    CoachMarkTargetText(
        text = "Will show tooltip 1",
        alignment = Alignment.Start,
        key = Keys.Text1,
        placement = ToolTipPlacement.End,
        tooltip = {
            Balloon(arrow = Arrow.Start()) {
                Text(text = "Highlighting Text1 at enableCoachmark method", color = Color.White)
            }
        }
    )

    CoachMarkTargetText(
        text = "Will show tooltip 2",
        alignment = Alignment.Start,
        key = Keys.Text2,
        placement = ToolTipPlacement.End
    )

    CoachMarkTargetText(
        text = "Will show tooltip centrally",
        alignment = Alignment.CenterHorizontally,
        key = Keys.TextCenter,
        placement = ToolTipPlacement.ScreenCenter,
        tooltip = {
            Balloon(arrow = Arrow.Top()) {
                Text(text = "tooltip on screen center at enableCoachmark method", color = Color.White)
            }
        }
    )

    CoachMarkTargetText(
        text = "Will show tooltip to left",
        alignment = Alignment.End,
        key = Keys.TextStart,
        placement = ToolTipPlacement.Start,
        tooltip = {
            Balloon(arrow = Arrow.End()) {
                Text(text = "tooltip to left at enableCoachmark method", color = Color.White)
            }
        }
    )

    CoachMarkTargetText(
        text = "Will show tooltip below",
        alignment = Alignment.CenterHorizontally,
        key = Keys.TextBottom,
        placement = ToolTipPlacement.Bottom
    )

    CoachMarkTargetText(
        text = "Will show tooltip above",
        alignment = Alignment.CenterHorizontally,
        key = Keys.TextTop,
        placement = ToolTipPlacement.Top
    )
}

@Composable
private fun ColumnScope.CoachMarkTargetText(
    text: String,
    alignment: Alignment.Horizontal,
    key: Keys,
    placement: ToolTipPlacement,
    tooltip: @Composable (() -> Unit)? = null
) {
    val coachMarkScope = LocalCoachMarkScope.current

    Text(
        text = text,
        modifier = Modifier
            .align(alignment)
            .enableCoachMark(
                key = key,
                toolTipPlacement = placement,
                highlightedViewConfig = HighlightedViewConfig(
                    shape = HighlightedViewConfig.Shape.Rect(12.dp),
                    padding = PaddingValues(8.dp)
                ),
                coachMarkScope = coachMarkScope,
                tooltip = tooltip
            )
            .padding(16.dp),
        color = Color.Black
    )
}

@Composable
private fun Tooltip(key: CoachMarkKey) {
    when (key) {
        Keys.Text1 -> {
            Balloon(arrow = Arrow.Start()) {
                Text(text = "Highlighting Text1 root level", color = Color.White)
            }
        }

        Keys.Text2 -> {
            Balloon(arrow = Arrow.Start()) {
                Text(text = "Highlighting Text2  root level", color = Color.White)
            }
        }

        Keys.TextCenter -> {
            Balloon(arrow = Arrow.Top()) {
                Text(text = "A tooltip on screen center root level", color = Color.White)
            }
        }

        Keys.TextStart -> {
            Balloon(arrow = Arrow.End()) {
                Text(text = "A tooltip to the left  root level", color = Color.White)
            }
        }

        Keys.TextBottom -> {
            Balloon(arrow = Arrow.Top()) {
                Text(text = "A tooltip below  root level", color = Color.White)
            }
        }

        Keys.TextTop -> {
            Balloon(arrow = Arrow.Bottom()) {
                Text(text = "A tooltip above  root level", color = Color.White)
            }
        }
    }
}