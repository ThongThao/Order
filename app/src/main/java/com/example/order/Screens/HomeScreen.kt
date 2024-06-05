package com.example.order.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.SearchField
import com.example.order.model.Category
import com.example.order.model.Menu
import com.example.order.model.Restaurant
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.orange1
import com.example.order.ui.theme.orange2
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.example.order.viewmodels.HomeViewModel
import com.example.order.viewmodels.MenuViewModel
import com.example.order.viewmodels.RestaurantViewModel
import com.example.order.viewmodels.UserViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.Collator
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    restaurantViewModel: RestaurantViewModel = viewModel(),
    menuViewModel: MenuViewModel = viewModel(),
    navToRes: () -> Unit,
    navToCart: () -> Unit,
    navToChangeAddress: () -> Unit,
    navController: NavHostController,
    userId: String?
               ) {
    val user by userViewModel.getUser(userId).observeAsState()
    val categoryList by homeViewModel.categories.collectAsState()
    val restaurantList by restaurantViewModel.restaurants.collectAsState()
    val menuList by menuViewModel.menus.collectAsState()
    var showChangeAddressScreen by remember { mutableStateOf(false) }
    val onChangeAddressClick: () -> Unit = {
        showChangeAddressScreen = true
    }
    user?.let { // Hiển thị giao diện
        if (showChangeAddressScreen) {
            navToChangeAddress()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                //--> Top Section
                item {
                    TopBar(it.fullName.toString(), viewCart = navToCart)
                    Location(onChangeAddressClick)
                    SearchField()
                    Spacer(modifier = Modifier.size(18.dp))
                    Categorys(categoryList)
                    Spacer(modifier = Modifier.size(25.dp))
                }
                item {
                    SectionHeader(sectionName = "Nổi bật", viewAll = navToRes)
                    Spacer(modifier = Modifier.size(8.dp))
                }
                //--> PopularRestaurants Section
                items(restaurantList) { item ->
                    PopularRestaurantItem(item, navController)
                }

                //--> MostPopular Section
                item {
                    SectionHeader(sectionName = "Phổ biến") {}
                    Spacer(modifier = Modifier.size(8.dp))
                    MostPopular(restaurantList)
                }
                item {
                    SectionHeader(sectionName = "Đề xuất") {}
                    Spacer(modifier = Modifier.size(8.dp))
                }
                items(menuList) { item ->
                    MenuItem(item, navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Location(onChangeAddressClick: () -> Unit) {
    var location by remember { mutableStateOf("") }

    getLocation(LocalContext.current) { specificLocation ->
        location = specificLocation
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            tint = orange,
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .height(34.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = location,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Black
            )
        }

        IconButton(onClick = { onChangeAddressClick()}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@SuppressLint("MissingPermission")
fun getLocation(context: Context, onLocationReceived: (String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 10000 // 10 seconds
        fastestInterval = 5000 // 5 seconds
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses?.firstOrNull()

                val specificLocation = address?.let {
                    "${it.featureName ?: ""}, ${it.thoroughfare ?: ""}, ${it.subLocality ?: ""}, ${it.locality ?: ""}, ${it.subAdminArea ?: ""}, ${it.adminArea ?: ""}, ${it.countryName ?: ""}"
                } ?: "Unknown Location"

                onLocationReceived(specificLocation)
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
}

@Composable
fun TopBar(userName: String, viewCart: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            text = "Xin chào ",
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 20.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        )
            Text(
                text = "$userName!",
                style = TextStyle(
                    color = orange,
                    fontSize = 20.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        IconButton(onClick = viewCart ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = null,
                tint = primaryFontColor
            )
        }
    }
}

@Composable
fun Categorys(categoryList: List<Category>) {
    val collator = Collator.getInstance(Locale("vi", "VN"))
    val sortedCategoryList = categoryList.sortedWith(compareBy(collator) { it.categoryName })

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(sortedCategoryList) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                .clickable { /* Xử lý khi nút được nhấn */ }) {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    painter = rememberAsyncImagePainter(item.categoryImage),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = item.categoryName,
                    style = TextStyle(
                        color = primaryFontColor,
                        fontSize = 14.sp,
                        fontFamily = metropolisFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }


        }
        item { Spacer(modifier = Modifier.size(12.dp)) }
    }
}

@Composable
fun SectionHeader(sectionName: String, viewAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sectionName,
            style = TextStyle(
                color = primaryFontColor,
                fontSize = 20.sp,
                fontFamily = metropolisFontFamily,
                fontWeight = FontWeight.Medium
            )
        )
        TextButton(onClick = viewAll) {
            Text(
                "Xem thêm",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = orange,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun PopularRestaurantItem(item: Restaurant,navController: NavHostController) {
    val itemRate = item.restaurantRate?.toDouble()
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 25.dp, start = 15.dp)
        .clickable { navController.navigate("restaurantDetail/${item.restaurantName}")}) {
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
                Text(
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
            Text(
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
                Text(
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
            Row (
               verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Địa chỉ: ",
                    style = TextStyle(
                        fontSize = 17.sp,
                    )
                )
            Text(
                text = "${item.restaurantAdd?: 0} ",
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

@Composable
fun MostPopular(restaurantList: List<Restaurant>) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        item { Spacer(modifier = Modifier.size(12.dp)) }
        items(restaurantList) { item ->
            MostPopularItem(item)
        }
        item { Spacer(modifier = Modifier.size(12.dp)) }
    }
}
@Composable
fun MostPopularItem(item: Restaurant, modifier: Modifier = Modifier) {
    val itemRate = item.restaurantRate?.toDouble()
    Column(modifier = modifier
        .padding(horizontal = 8.dp)
        .clickable { }) {
        val painter = rememberAsyncImagePainter(
            model = item.restaurantImage,
            placeholder = painterResource(R.drawable.ic_logo), // Placeholder image resource
        )
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        item.restaurantName?.let {
            Text(
                modifier = Modifier
                    .padding(top = 6.dp),
                text = it,
                style = TextStyle(
                    color = primaryFontColor,
                    fontSize = 17.sp,
                    fontFamily = metropolisFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${item.restaurantType} ",
                style = TextStyle(
                    color = secondaryFontColor,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                )
            )

            Icon(
                painterResource(id = R.drawable.ic_star),
                contentDescription = "Đánh giá",
                tint = orange1
            )
            Text(
                text = " $itemRate ",
                style = TextStyle(
                    color = orange,
                    fontSize = 13.sp,
                    fontFamily = metropolisFontFamily
                ),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

    }
}
@Composable
fun MenuItem(item: Menu,navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp, start = 15.dp)
            .clickable {navController.navigate("restaurantDetail/${item.itemRestaurant}") }) {
        item.itemImage?.let {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp)),
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
            item.itemName?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        color = primaryFontColor,
                        fontSize = 22.sp,
                        fontFamily = metropolisFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription =null ,
                    tint= orange2)
                Spacer(modifier = Modifier.width(4.dp))
                item.itemRestaurant?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = metropolisFontFamily,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(orange)
                )
                Spacer(modifier = Modifier.width(6.dp))
                item.itemType?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = metropolisFontFamily,
                            textAlign = TextAlign.Center
                        )
                    )
                }


            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                item.itemPrice?.let {
                    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(it)
                    Text(
                        text = formattedPrice,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily =  metropolisFontFamily, // Thay thế FontFamily.Default bằng metropolisFontFamily nếu bạn có định nghĩa nó
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

