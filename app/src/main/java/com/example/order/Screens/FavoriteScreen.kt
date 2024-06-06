package com.example.order.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.model.Restaurant
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.orange1
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.example.order.viewmodels.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    userId: String,
    favoriteViewModel: FavoriteViewModel = viewModel(),
    navController: NavHostController,
) {
    val favoriteRestaurants by favoriteViewModel.favoriteRestaurants.collectAsState()

    LaunchedEffect(userId) {
        favoriteViewModel.fetchFavoriteRestaurants(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = orange,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = {
                    Text(
                        text = "Yêu thích",
                        fontSize = 25.sp,
                        fontWeight = MaterialTheme.typography.h6.fontWeight,
                        modifier = Modifier.padding(start = 125.dp)
                    )
                },

            )
        }
    )  { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            items(favoriteRestaurants) { restaurant ->
                RestaurantItem(item = restaurant,navController)
            }
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant,navController: NavHostController) {
    val itemRate = item.restaurantRate?.toDouble()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)
        .clickable { navController.navigate("restaurantDetail/${item.restaurantName}") }) {
        item.restaurantImage?.let {
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(bottom = 16.dp),
        ) {
            item.restaurantName?.let {
                androidx.compose.material.Text(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 10.dp),
                    text = it,
                    style = TextStyle(
                        color = primaryFontColor,
                        fontSize = 24.sp,
                        fontFamily = metropolisFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            androidx.compose.material.Text(
                text = "${item.restaurantType} ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 17.sp,
                    fontFamily = metropolisFontFamily
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(
                    painterResource(id = R.drawable.ic_star),
                    contentDescription = "Đánh giá",
                    tint = orange1,
                    modifier = Modifier.size(18.dp)
                )
                androidx.compose.material.Text(
                    text = " $itemRate ",
                    style = TextStyle(
                        color = orange,
                        fontSize = 18.sp,
                        fontFamily = metropolisFontFamily
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                androidx.compose.material.Text(
                    text = "Địa chỉ: ",
                    style = TextStyle(
                        fontSize = 17.sp,
                    )
                )
                androidx.compose.material.Text(
                    text = "${item.restaurantAdd ?: 0} ",
                    style = TextStyle(
                        color = primaryFontColor,
                        fontSize = 15.sp,
                        fontFamily = metropolisFontFamily
                    )
                )
            }


        }
    }
}

