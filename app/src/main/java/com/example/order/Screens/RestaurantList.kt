package com.example.order.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.blue2
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.orange2
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.example.order.viewmodels.HomeViewModel
import com.example.order.viewmodels.MenuViewModel
import com.example.order.viewmodels.RestaurantViewModel
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantList(
    userId: String?,
    categoryName: String?,
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    restaurantViewModel: RestaurantViewModel = viewModel(),
    menuViewModel: MenuViewModel = viewModel(),
    onBackClick: () -> Unit,
) {
    val restaurants by restaurantViewModel.getRestaurantsByCategory(categoryName ?: "").collectAsState()

    Scaffold(
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
                        .padding(horizontal = 15.dp)
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
                                text = categoryName!!,
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
                                androidx.compose.material.Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="" )
                            }
                        },
                    )
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(top = 70.dp)) {
            itemsIndexed(restaurants) { index, restaurant ->
                Surface(
                    color = blue2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    shadowElevation = 10.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp).clickable {
                        navController.navigate("restaurantDetail/${restaurant.restaurantName}")
                    }) {
                        Row {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(width = 130.dp, height = 130.dp)
                            ) {
                                restaurant?.restaurantImage?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)) {
                                Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(top = 3.dp),
                                    color = blue3
                                ) {
                                    restaurant?.restaurantType?.let {
                                        Text(
                                            text = it,
                                            fontSize = 14.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))

                                restaurant?.restaurantName?.let {
                                    Text(
                                        text = it,
                                        fontSize = 22.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(170.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "",
                                        tint = orange2
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    restaurant?.restaurantRate?.let {
                                        Text(
                                            text = it.toDouble().toString(),
                                            color = orange2,
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_location),
                                        contentDescription = null,
                                        tint = orange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    restaurant?.restaurantAdd?.let {
                                        Text(
                                            text = it,
                                            fontSize = 17.sp,
                                            color = secondaryFontColor
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        restaurant?.restaurantName?.let { restaurantName ->
                            val menus by menuViewModel.getMenusForRestaurant(restaurantName).collectAsState(initial = emptyList())
                            val randomMenuList = remember(menus) {
                                menus.shuffled().take(5)
                            }
                            LazyRow() {
                                itemsIndexed(randomMenuList) { _, menuItem ->
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .size(90.dp, 90.dp)
                                                .border(
                                                    width = 2.dp,
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                        ) {
                                            menuItem.itemImage?.let {
                                                Image(
                                                    painter = rememberAsyncImagePainter(it),
                                                    contentScale = ContentScale.Crop,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        menuItem.itemPrice?.let {
                                            val formattedPrice = NumberFormat.getCurrencyInstance(
                                                Locale("vi", "VN")
                                            ).format(it)
                                            Text(
                                                text = formattedPrice,
                                                fontSize = 19.sp,
                                                color = primaryFontColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        menuItem.itemName?.let {
                                            TruncatedText(
                                                text = it,
                                                maxLines = 1, // Số dòng tối đa
                                                maxWidth = 100, // Chiều rộng tối đa
                                                fontSize = 17 // Kích thước font chữ
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
