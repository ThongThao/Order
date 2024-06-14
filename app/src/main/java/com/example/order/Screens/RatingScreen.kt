package com.example.order.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app.R
import com.example.order.model.Rate
import com.example.order.ui.theme.green
import com.example.order.ui.theme.orange1
import com.example.order.ui.theme.red
import com.example.order.ui.theme.white
import com.example.order.viewmodel.RatingViewModel
import com.example.order.viewmodels.UserViewModel
import java.util.Date
import java.util.UUID

@Composable
fun RatingAndComment(
    userId: String?,
    restaurantName: String?,
    navController: NavController,
    ratingViewModel: RatingViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    var rating by remember { mutableStateOf(0.0) }
    var comment by remember { mutableStateOf("") }
    val user by userViewModel.getUser(userId).observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Đánh giá nhà hàng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Simple star rating (could be replaced with a more complex component)
        Row {
            (1..5).forEach { index ->
                IconButton(onClick = { rating = index.toDouble() }) {
                    Icon(
                        painter = painterResource(
                            if (index <= rating) R.drawable.star_24dp  else R.drawable.star_outline_24dp
                        ),
                        contentDescription = null,
                        tint = if (index <= rating) orange1 else Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Comment input
        TextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Viết bình luận...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Submit and cancel buttons
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                val rate = Rate(
                    id = UUID.randomUUID().toString(),
                    customerId = userId,
                    customerName = user?.fullName,
                    restaurantName = restaurantName,
                    rating = rating,
                    comment = comment,
                    timestamp = Date()
                )
                ratingViewModel.submitRating(rate, restaurantName!!)
                navController.popBackStack()
                Log.d("RatingAndComment", "Rating: ${userId}, Comment: ${restaurantName},rate:${rating},")
            }, colors = ButtonDefaults.buttonColors(backgroundColor = green, contentColor = white)) {
                Text("Gửi")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(backgroundColor = red, contentColor = white))  {
                Text("Hủy")
            }
        }
    }
}
