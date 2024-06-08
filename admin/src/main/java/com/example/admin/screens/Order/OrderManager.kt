package com.example.admin.screens.Order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.R
import com.example.admin.screens.Home
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blue2

class OrderManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
OrderScreen(LocalContext.current)
                }
            }
        }
    }
}
@Composable
fun OrderScreen(context:Context){
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { context.startActivity(Intent(context, Home::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(75.dp))
            Text(
                text = "Order Manager", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

        }
        Surface(
            color = blue2,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .padding(10.dp).border(width = 1.dp, color = Color.Black),
            onClick = {
                context.startActivity(Intent(context, OrderPending::class.java))
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Processing",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(10.dp)

                )
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription ="" )
            }
        }
        Surface(
            color = blue2,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .padding(10.dp).border(width = 1.dp, color = Color.Black),
            onClick = {
                context.startActivity(Intent(context, OrderShipping::class.java))
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shipping",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(10.dp)

                )
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription ="" )
            }
        }
        Surface(
            color = blue2,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .padding(10.dp).border(width = 1.dp, color = Color.Black),
            onClick = {
                context.startActivity(Intent(context, OrderCancelled::class.java))
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Canceled",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(10.dp)

                )
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription ="" )
            }
        }
        Surface(
            color = blue2,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .padding(10.dp).border(width = 1.dp, color = Color.Black),
            onClick = {
                context.startActivity(Intent(context, OrderCompleted::class.java))
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Completed",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(10.dp)

                )
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription ="" )
            }
        }
    }
}
