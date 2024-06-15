package com.example.order.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.model.Category
import com.example.order.ui.theme.blue2
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.gray
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.orange2
import com.example.order.ui.theme.placeholderColor
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
fun RestaurantScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavHostController,
                     restaurantViewModel: RestaurantViewModel = viewModel(),
                     menuViewModel: MenuViewModel = viewModel(),
                     onBackClick: () -> Unit
) {
    val restaurantList by restaurantViewModel.restaurants.collectAsState()
    val categories by homeViewModel.categories.collectAsState()

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Add a special "All" category
    val allCategory = Category(categoryName = "Tất cả")
    val updatedCategories = listOf(allCategory) + categories

    val filteredRestaurants = if (selectedCategory != null && selectedCategory?.categoryName != "Tất cả") {
        restaurantList.filter { restaurant ->
            selectedCategory?.let { category ->
                restaurant.restaurantType?.contains(category.categoryName) ?: false
            } ?: true
        }
    } else {
        restaurantList
    }
    Scaffold(
        topBar = {
            Column {
                Row(modifier = Modifier.padding(top = 10.dp)) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = orange,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    var value by remember { mutableStateOf("") }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    TextField(
                        modifier = Modifier
                            .width(320.dp)
                            .height(50.dp)
                            .padding(horizontal = 10.dp),
                        value = value,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = gray,
                            cursorColor = orange,
                            disabledLabelColor = gray,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        onValueChange = {
                            value = it
                        },
                        placeholder = {
                            Text(
                                text = "Tìm kiếm",
                                style = TextStyle(
                                    color = placeholderColor,
                                    fontSize = 14.sp,
                                    fontFamily = metropolisFontFamily
                                )
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = primaryFontColor,
                            fontSize = 15.sp,
                            fontFamily = metropolisFontFamily
                        ),
                    )
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = orange,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(45.dp)
                        )
                    DropdownMenu(
                        categories = updatedCategories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = category
                            // Apply filter based on selected category
                        }
                    )

                }
            }
        }
    ) {
            LazyColumn(modifier = Modifier.padding(top = 120.dp)) {
                itemsIndexed(filteredRestaurants) { index, restaurant ->
                    Surface(
                        color = blue2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        shadowElevation = 10.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp).clickable { navController.navigate("restaurantDetail/${restaurant.restaurantName}")  }) {
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        restaurant?.restaurantName?.let {
                                            Text(
                                                text = it,
                                                fontSize = 22.sp,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold
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
@Composable
fun TruncatedText(text: String, maxLines: Int, maxWidth: Int, fontSize: Int) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.width(maxWidth.dp)
    )
}
@Composable
fun DropdownMenu(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row( verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.border(width = 1.dp, color = Color.LightGray,shape = RoundedCornerShape(30.dp))) {

        Text(
            text = selectedCategory?.categoryName ?: "Select category",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(start = 10.dp)


        )
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
       }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                ) {
                    Text(text = category.categoryName)
                }
            }
        }

    }
}


