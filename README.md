# Compose Multiplatform Coachmark/Onboarding Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ankitk77/coachmark.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.ankitk77/coachmark)
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>

A lightweight Compose multiplatform library dedicated to creating seamless onboarding experiences.

| android | iOS |
|-|-|
| <img alt="android" src="https://github.com/pseudoankit/coachmark/assets/54987308/a5167820-9603-42d0-bd13-e1eeccb3cff5" width="240" height = "500"/> | <img alt="ios" src="https://github.com/pseudoankit/coachmark/assets/54987308/39215e1b-0bb6-49c1-a1c8-32ca94e2c3f0" width="250" height = "500"/> |

| Feature                           | Description                                                                                 |
|----------------------------------|---------------------------------------------------------------------------------------------|
| Custom Coachmarks                | Easily create and customize coachmarks to guide users through your app.                     |
| Flexible and Extensible          | Customize coachmarks to match your app's design and extend functionality as needed.         |
| Cross-Platform Compatibility    | Compatible with Android and iOS, ideal for multiplatform projects.                         |
| Jetpack Compose Integration     | Seamlessly integrate coachmarks with Jetpack Compose UI components.                         |
| Dynamic Tooltip Views            | Display tooltip views for each key when coachmarks are active, enhancing user guidance.     |
| Comprehensive Documentation      | Access detailed documentation and support for easy implementation.                          |


## Installation

<details>
<summary>Android Project</summary>
  
<br>In your module's gradle
    
```kotlin
dependencies {
    implementation("io.github.ankitk77:coachmark:<latest_version🔝>")
}
```
</details>

<details>
<summary>Compose Multiplatform Project</summary>
  
<br>In your shared module gradle
    
```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.ankitk77:coachmark:<latest_version🔝>")
            }
        }
    }
}
```
</details>

## Usage

[Complete Demo Code](https://github.com/pseudoankit/coachmark/blob/master/coachmark/src/commonMain/kotlin/com/pseudoankit/coachmark/demo/UnifyCoachmarkDemo.kt)

Define key for all composables that needs to be higlighted.
```kotlin
enum class Keys { Text1, Text2 }
```

At the root level, ensure that your code is wrapped with UnifyCoachmark.
```kotlin
UnifyCoachmark(
    tooltip = { /*@Deprecated since v2.0.4,  pass tooltip while calling `enableCoachMark` method*/ },
    overlayEffect = DimOverlayEffect(Color.Black.copy(alpha = .5f)),
    onOverlayClicked = { OverlayClickEvent.GoNext }
) { this : CoachmarkScope
    Content()     // Source code below ⏬
}
```

Call the enableCoachMark method in all the composables that need to be highlighted. 
<br>This method can be invoked within the CoachmarkScope. 
<br>If you are not in the CoachmarkScope, you can access it by calling LocalCoachMarkScope.current.
```kotlin
@Composable
private fun Content() {
    Text(
        text = "Will show tooltip 1",
        modifier = Modifier
            .enableCoachMark(
                key = Keys.Text1,    // unique key that we declared above
                toolTipPlacement = ToolTipPlacement.Top,
                highlightedViewConfig = HighlightedViewConfig(
                    shape = HighlightedViewConfig.Shape.Rect(12.dp),
                    padding = PaddingValues(8.dp)
                ),
                tooltip = {
                    Balloon(arrow = Arrow.Start()) {
                        Text(
                            text = "Highlighting Text1",
                        )
                    }
                },
                coachMarkScope = LocalCoachMarkScope.current  // not needed if you are already in CoachmarkScope
            )
    )
}
```

Overlay Logic referred from <a href = "https://github.com/svenjacobs/reveal">reveal</a> library

# License
```xml
Copyright 2024 ankitk77 (Ankit)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
