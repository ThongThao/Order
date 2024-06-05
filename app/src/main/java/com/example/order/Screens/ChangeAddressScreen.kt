package com.example.order.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.order.ui.theme.orange

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangeAddressScreen(
    onBackClick: () -> Unit
) {
    Scaffold {
        Row(
            modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = orange,
                    modifier = Modifier.size(45.dp)
                )
            }
            Text(
                text = "Đổi địa chỉ",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp
            )
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Back",
                    tint = orange,
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}
