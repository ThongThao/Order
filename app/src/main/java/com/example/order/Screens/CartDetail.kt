package com.example.order.Screens

import CartItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CartDetail(restaurantName: String?) {

}

@Composable
fun CartItemRow(cartItem: CartItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(cartItem.image),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.width(16.dp))
            cartItem.name?.let { Text(text = it) }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$${cartItem.price}", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
