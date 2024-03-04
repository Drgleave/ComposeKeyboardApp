package com.example.composekeyboardapp



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composekeyboardapp.ui.theme.ComposeKeyboardAppTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {


    /** Activity Creation **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeKeyboardAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    GenerateUI()
                }
            }
        }
    }

}

/** Initial Interface **/
@Composable
fun GenerateUI() {

    /** text input param **/
    val inputText = remember { mutableStateOf("") }

    Box(
        Modifier.fillMaxHeight().fillMaxWidth().background(Color.Black.copy(alpha = 0.4f))) {

        Column {

            /** Text View **/
            Box(modifier = Modifier.weight(1F)) {
                Text(text = inputText.value,
                    Modifier
                        .padding(Dp(16F))
                        .verticalScroll(rememberScrollState()),
                    color = Color.White,
                    fontSize = 42.sp,
                    letterSpacing = 10.sp,
                    lineHeight = 35.sp,
                    overflow= TextOverflow.Ellipsis,
                    softWrap = true)
            }

            /** Keyboard function **/
            Box {
                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    KeyboardUI(inputText)
                }
            }

        }


    }

}


/** Generate Keyboard Key items **/
@Composable
fun KeyboardUI(inputTxt: MutableState<String>) {
    val keysMatrix = arrayOf(
        arrayOf("1", "2", "3"),
        arrayOf("4", "5", "6"),
        arrayOf("7", "8", "9"),
        arrayOf("DELETE", "0", "CLEAR")
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        keysMatrix.forEach { row ->
            FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 56.dp) {
                Row(Modifier) {
                    row.forEach { key ->
                        KeyboardKey(inputTxt, keyboardKey = key, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

}

/** Keyboard Key UI **/
@Composable
fun FixedHeightBox(modifier: Modifier, height: Dp, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val h = height.roundToPx()
        layout(constraints.maxWidth, h) {
            placeables.forEach { placeable ->
                placeable.place(x = 0, y = min(0, h - placeable.height))
            }
        }
    }
}

/** Keyboard Key Function **/
@Composable
fun KeyboardKey(inputTxt: MutableState<String>, keyboardKey: String, modifier: Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
        Text(keyboardKey, Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .border(1.dp, Color.Black.copy(alpha = 0.3f))
                .clickable(interactionSource = interactionSource, indication = null) {
                    when (keyboardKey) {
                        "DELETE" -> {
                            val resultString: String = if (inputTxt.value.length > 1) {
                                inputTxt.value.substring(0, inputTxt.value.length - 1)
                            } else {
                                ""
                            }
                            inputTxt.value = resultString
                        }

                        "CLEAR" -> {
                            inputTxt.value = ""
                        }

                        else -> {
                            inputTxt.value += keyboardKey
                        }
                    }
                }
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            color = Color.White
        )

        if (pressed.value) {
            Text(
                keyboardKey,
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black.copy(alpha = 0.6f))
                    .background(Color.White)
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    )

            )
        }
    }
}