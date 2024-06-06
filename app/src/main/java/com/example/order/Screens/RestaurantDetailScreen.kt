package com.example.order.Screens

import Cart
import CartItem
import CartViewModel
import RestaurantCart
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.FilledButton
import com.example.order.model.Menu
import com.example.order.ui.theme.blue2
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.gray
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.primaryFontColor
import com.example.order.viewmodels.FavoriteViewModel
import com.example.order.viewmodels.MenuViewModel
import com.example.order.viewmodels.RestaurantViewModel
import com.example.order.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RestaurantDetailScreen(
    restaurantName: String?,
    onBackClick: () -> Unit,
    menuViewModel: MenuViewModel = viewModel(),
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(),
    userId: String?,
    cartViewModel: CartViewModel = viewModel() // Inject CartViewModel
) {
    val restaurantViewModel: RestaurantViewModel = viewModel()
    val favoriteViewModel: FavoriteViewModel = viewModel()
    val restaurant by restaurantViewModel.getRestaurantById(restaurantName)
        .collectAsState(initial = null)
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val selectedMenuItem = remember { mutableStateOf<Menu?>(null) }
    val quantity = remember { mutableStateOf(1) }
    var note by remember { mutableStateOf("") }
    val user by userViewModel.getUser(userId).observeAsState()

    user?.let {
        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = {
                selectedMenuItem.value?.let { menuItem ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                bottomSheetState.hide()
                                selectedMenuItem.value = null
                            }
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Red,
                                modifier = Modifier.size(30.dp)
                            )

                        }
                        menuItem.itemImage?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.padding(top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column() {
                                menuItem.itemName?.let {
                                    Text(
                                        text = it,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                menuItem.itemDescription?.let {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {

                                menuItem.itemPrice?.let {
                                    val formattedPrice = NumberFormat.getCurrencyInstance(
                                        Locale("vi", "VN")
                                    ).format(it)
                                    Text(
                                        text = formattedPrice,
                                        fontSize = 24.sp,
                                        color = primaryFontColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .padding(top = 2.dp)
                                .background(color = gray)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Ghi chú cho quán",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = blue2,
                                unfocusedBorderColor = blue3
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    if (quantity.value > 1) {
                                        quantity.value -= 1
                                    }
                                },
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_remove),
                                    contentDescription = "Decrease quantity",
                                    tint = orange,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = quantity.value.toString(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            IconButton(
                                onClick = {
                                    quantity.value += 1
                                },
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase quantity",
                                    tint = orange,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(35.dp))
                        FilledButton(
                            text = "Thêm vào giỏ hàng",
                            modifier = Modifier.padding(horizontal = 34.dp),
                            onClick = {
                                if (selectedMenuItem.value != null) {
                                    val cartItem = selectedMenuItem.value!!.itemPrice?.let { price ->
                                        CartItem(
                                            id = selectedMenuItem.value!!.itemId ?: "",
                                            name = selectedMenuItem.value!!.itemName ?: "",
                                            description = selectedMenuItem.value!!.itemDescription,
                                            image = selectedMenuItem.value!!.itemImage,
                                            price = price,
                                            quantity = quantity.value,
                                            note = note
                                        )
                                    }

                                    val restaurantCart = RestaurantCart(
                                        restaurantName = restaurantName ?: "",
                                        items = listOf(cartItem),
                                        total = 0
                                    )

                                    // Update the local cart state
                                    cartViewModel.addToCart(restaurantCart)

                                    // Create a Cart object for the user
                                    val cart = Cart(
                                        idCart = null, // Assuming this will be generated by Firestore
                                        idCustomer = user?.id,
                                        restaurantCarts = cartViewModel.carts.value
                                    )

                                    // Save the cart to Firestore
                                    cartViewModel.saveCartToFirestore(
                                        userId = user?.id ?: "",
                                        cart = cart,
                                        onSuccess = {
                                            coroutineScope.launch {
                                                bottomSheetState.hide()
                                                selectedMenuItem.value = null
                                            }
                                        },
                                        onError = { errorMessage ->
                                            coroutineScope.launch {
                                                Log.e("TAG", "onErr: $errorMessage" )
                                            }
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) {
                restaurant?.let {  restaurant->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        restaurant.restaurantImage?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(270.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = Modifier.matchParentSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Black.copy(alpha = 0.5f)) // Độ trong suốt của màu đen
                                )
                            }
                        }

                    }
                    Row (modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape)
                                .background(color = Color.White)
                        ) {

                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint= orange
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape)
                                .background(color = Color.White),
                        ) {

                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Back",
                                    tint= orange
                                )
                            }
                        }

                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 300.dp),
                        color = Color.White,
                    ) {
                        Column() {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Thực đơn",
                                    fontSize = 26.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        vertical = 4.dp,
                                        horizontal = 12.dp
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .padding(top = 2.dp)
                                        .background(Color.LightGray)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            restaurant?.restaurantName?.let { restaurantName ->
                                val menus by menuViewModel.getMenusForRestaurant(restaurantName)
                                    .collectAsState(initial = emptyList())
                                LazyColumn(
                                ) {
                                    itemsIndexed(menus) { _, menuItem ->
                                        Row(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable {
                                                    selectedMenuItem.value = menuItem
                                                    coroutineScope.launch { bottomSheetState.show() }
                                                },
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier
                                                    .size(120.dp, 120.dp)
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
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                menuItem.itemName?.let {
                                                    androidx.compose.material.Text(
                                                        text = it,
                                                        fontSize = 24.sp,
                                                        color = primaryFontColor,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                menuItem.itemDescription?.let {
                                                    androidx.compose.material.Text(
                                                        text = it,
                                                        fontSize = 18.sp,
                                                        color = Color.Gray,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(18.dp))
                                                menuItem.itemPrice?.let {
                                                    val formattedPrice =
                                                        NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                                                            .format(it)
                                                    Text(
                                                        text = formattedPrice,
                                                        fontSize = 24.sp,
                                                        color = primaryFontColor,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.CenterEnd
                                            ) {
                                                IconButton(
                                                    onClick = { },
                                                    modifier = Modifier.size(50.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AddCircle,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(50.dp),
                                                        tint = blue3
                                                    )
                                                }
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .padding(top = 2.dp)
                                                .background(Color.LightGray)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(270.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column() {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                                ) {
                                    restaurant.restaurantName?.let {
                                        Text(
                                            text = it,
                                            fontSize = 30.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(start = 8.dp
                                            ),
                                            color = Color.White
                                        )
                                    }
                                    user!!.id?.let { userId ->
                                        restaurant.restaurantId?.let { restaurantId ->
                                            FavoriteButton(
                                                userId = userId,
                                                restaurantId = restaurantId,
                                                favoriteViewModel = favoriteViewModel
                                            )
                                        }
                                    }
                                }
                                Row (verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {  }){
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription =" ",
                                        modifier = Modifier.padding(
                                            start = 8.dp
                                        ),
                                        tint = Color.Yellow
                                    )

                                    Text(
                                        text = "${restaurant.restaurantRate}" + " | Đánh giá",
                                        modifier = Modifier.padding(bottom = 3.dp),
                                        fontSize = 20.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }




                    }

                }

            }
        }
    }
@Composable
fun FavoriteButton(
    userId: String,
    restaurantId: String,
    favoriteViewModel: FavoriteViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isFavorite = remember { mutableStateOf(false) }

    LaunchedEffect(userId, restaurantId) {
        isFavorite.value = favoriteViewModel.isFavorite(userId, restaurantId)
    }

    val icon: ImageVector = if (isFavorite.value) {
        Icons.Default.Favorite
    } else {
        Icons.Default.FavoriteBorder
    }

    IconButton(
        onClick = {
            coroutineScope.launch {
                if (isFavorite.value) {
                    favoriteViewModel.removeFavorite(userId, restaurantId)
                } else {
                    favoriteViewModel.addFavorite(userId, restaurantId)
                }
                isFavorite.value = !isFavorite.value
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Toggle Favorite",
            modifier = Modifier.size(30.dp),
            tint = Color.Red,
        )
    }
}
