package com.example.order.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.app.R
import com.example.order.model.Rate
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.blackcart
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.orange1
import com.example.order.ui.theme.secondaryFontColor
import com.example.order.viewmodel.RatingViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RateRestaurantScreen(
    restaurantName: String,
    ratingViewModel: RatingViewModel = viewModel(),
    navController: NavController
) {
    val ratings by ratingViewModel.ratings.collectAsState()

    LaunchedEffect(restaurantName) {
        ratingViewModel.getRatingsForRestaurant(restaurantName)
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,

        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(color = Color.White)
                ) {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            actionIconContentColor = Color.Black
                        ),
                        title = {
                            androidx.compose.material3.Text(
                                text = "Đánh giá",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            Button(
                                onClick = { navController.popBackStack()},
                                contentPadding = PaddingValues(),
                                shape = Shapes.small,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                //                    elevation = 5.dp,
                                modifier = Modifier
                                    .width(38.dp)
                                    .height(38.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="" )
                            }
                        },
                    )
                }
            }
        },
        content = {

        if (ratings.isEmpty()) {
            Text("No ratings yet.")
        } else {
            LazyColumn {
                items(ratings) { rating ->
                    RatingItem(rating)
                }
            }
        }
    }
    )
}

@Composable
fun RatingItem(rating: Rate) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null,
                modifier = Modifier.height(20.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            rating!!.timestamp?.let { date ->
                val formattedDate = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale("vi", "VN")
                ).format(date)
                androidx.compose.material3.Text(
                    text = formattedDate,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = blackcart
                )
            }


        }
        Row (verticalAlignment = Alignment.CenterVertically){
            Icon(
                imageVector = Icons.Default.AccountCircle, 
                contentDescription = "", tint = blue3, modifier = Modifier.size(35.dp)
            )
            Text(
                text = "${rating.customerName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold)
        }
        Row (verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.star_24dp),
                contentDescription ="",
                Modifier.size(24.dp),
                tint = orange1)
            Text(
                text = "${rating.rating}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                style = MaterialTheme.typography.subtitle1
            )
        }
        rating.comment?.let {
            Text(
                text = "$it",
                style = MaterialTheme.typography.body2,
                fontSize = 18.sp,
                color =   secondaryFontColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
