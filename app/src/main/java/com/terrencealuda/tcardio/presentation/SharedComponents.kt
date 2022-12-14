package com.terrencealuda.tcardio.presentation

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.terrencealuda.tcardio.R
import com.terrencealuda.tcardio.presentation.theme.wearColorPalette

val tCardioFamily = FontFamily(
    Font(R.font.redhatdisplay_light, FontWeight.Light),
    Font(R.font.redhatdisplay_regular, FontWeight.Normal),
    Font(R.font.redhatdisplay_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.redhatdisplay_medium, FontWeight.Medium),
    Font(R.font.redhatdisplay_bold, FontWeight.Bold)
)



@Composable
fun CardioChip(
    mod: Modifier,
    iconModifier: Modifier = Modifier,
    displayText: String,
    icon: ImageVector,
    detPos: Int = 0
) {
    val ourContext = LocalContext.current
    Chip(
        modifier = mod.border(BorderStroke(1.5.dp, MaterialTheme.colors.primary), CircleShape),
        onClick = {
            if(detPos == 1) {
                val statScreen = Intent(ourContext, MainActivity::class.java)
                ourContext.startActivity(statScreen)
            }else if(detPos == 2){
                val predScreen = Intent(ourContext, PredictionScreen::class.java)
                ourContext.startActivity(predScreen)
            }else{

            }

        },
        colors = ChipDefaults.primaryChipColors(backgroundColor = MaterialTheme.colors.background),

        label = {

            Text(
                fontFamily = tCardioFamily,
                text = displayText,
                maxLines = 2,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {

            Icon(
                imageVector = icon,
                contentDescription = "triggers meditation action",
                modifier = iconModifier
            )
        },
    )
}

@Composable
fun CardioRow(
    textMods: Modifier,
    countLabel1: String,
    healthDataLabel1: String,
    countLabel2: String,
    healthDataLabel2: String
) {
    Row(verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.SpaceAround ,
        modifier = Modifier.padding(top = 10.dp).fillMaxWidth()) {
        Column( horizontalAlignment = Alignment.Start ) {
            Text(
                fontFamily = tCardioFamily,
                //modifier = textMods.padding(3.dp),
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.primary,
                text = countLabel1
            )
            Text(
                fontFamily = tCardioFamily,
                //modifier = textMods,
                textAlign = TextAlign.Start ,
                color = Color(0xFFe62000),
                text = healthDataLabel1
            )
        }

        Column( horizontalAlignment = Alignment.End){
            Text(
                fontFamily = tCardioFamily,
                //modifier = textMods.padding(3.dp),
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.primary,
                text = countLabel2
            )
            Text(
                fontFamily = tCardioFamily,
                //modifier = textMods,
                textAlign = TextAlign.Start ,
                color = Color(0xFFe62000),
                text = healthDataLabel2
            )
        }

    }

}

@Composable
fun CardioColumn(
    iconModifier: Modifier,
    textMods: Modifier,
    countLabel: String,
    healthDataLabel: String,
    itemIcon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Icon(
            imageVector = itemIcon,
            contentDescription = "triggers meditation action",
            modifier = iconModifier
        )
        Text(
            fontFamily = tCardioFamily,
            modifier = textMods.padding(3.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF00cc7a),
            text = countLabel
        )
        Text(
            fontFamily = tCardioFamily,
            modifier = textMods,
            textAlign = TextAlign.Center,
            color = Color(0xFF00cc7a),
            text = healthDataLabel
        )
    }
}

@Composable
fun CardioColumnNoIcon(
    textMods: Modifier,
    countLabel: String,
    healthDataLabel: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(
            fontFamily = tCardioFamily,
            modifier = textMods.padding(3.dp),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = countLabel
        )
        Text(
            fontFamily = tCardioFamily,
            modifier = textMods,
            textAlign = TextAlign.Center,
            color = Color(0xFF999999),
            text = healthDataLabel
        )
    }
}

@Composable
fun ScreenTitle(mod: Modifier, titleText: String) {
    Text(
        fontFamily = tCardioFamily,
        modifier = mod,
        textAlign = TextAlign.Center,
        color = Color(0xFFe62000),
        text = titleText
    )
}

@Composable
fun ScreenBigTitle(mod: Modifier, titleText:String) {
    Text(
        fontFamily = tCardioFamily,
        modifier = mod,
        textAlign = TextAlign.Center,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFe62000),
        text = titleText
    )
}

// Chip Preview
/*@Preview(
    group = "Chip",
    widthDp = WEAR_PREVIEW_ROW_WIDTH_DP,
    heightDp = WEAR_PREVIEW_ROW_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
/*@Composable
fun ChipExamplePreview() {
    TCardioTheme {
        CardioChip(
            mod = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(align = Alignment.Center),
            "Hi",
            Icons.Rounded.OnlinePrediction
        )
    }
}
 */