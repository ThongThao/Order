
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.Screens.HomeScreens
import com.example.order.Screens.getLocation
import com.example.order.model.Order
import com.example.order.model.OrderItem
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.delete
import com.example.order.ui.theme.grayFont
import com.example.order.ui.theme.lightGray
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.orange1
import com.example.order.ui.theme.red
import com.example.order.viewmodels.UserViewModel
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@Composable
fun CartDetail(
    userId: String?,
    restaurantName: String?,
    cartViewModel: CartViewModel = viewModel(),
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.getUser(userId).observeAsState()
    // Lấy giỏ hàng từ ViewModel
    userId?.let {
        cartViewModel.getCart(userId)
    }
    val cart by cartViewModel.carts.collectAsState()
    var location by remember { mutableStateOf("") }

    getLocation(LocalContext.current) { specificLocation ->
        location = specificLocation
    }
    // Lọc giỏ hàng theo tên nhà hàng
    val restaurantCart = cart[restaurantName]
    user?.let { user ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = orange,
                        modifier = Modifier.size(24.dp)
                    )

                }
                Spacer(modifier = Modifier.width(100.dp))
                Text(
                    text =  restaurantName ?: "",
                    fontSize = 24.sp,
                    fontWeight = MaterialTheme.typography.h6.fontWeight,
                )

            }
            InforHeader(R.drawable.ic_location, "Thông tin giao hàng")
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = lightGray,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .height(150.dp)
                    .padding(10.dp),
                shadowElevation = 6.dp,
                onClick = {
                    //den trang sua/ them dia chi
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(2f)
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = user.fullName?: "",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        //so dien thoai
                        Text(
                            text = user.phoneNumber?: "",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        //so duong
                        Text(
                            text = location,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {

                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "",
                            tint = red
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InforHeader(R.drawable.ic_shopping_bag, "Tóm tắt đơn hàng")
                TextButton(
                    onClick = { navController.navigate("restaurantDetail/${restaurantCart?.restaurantName}")  }
                ) {
                    Text(
                        text = "Thêm món",
                        color = orange,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            // Hiển thị chi tiết các món ăn trong giỏ hàng của nhà hàng
            if (restaurantCart != null && restaurantCart.items != null && restaurantCart.items.isNotEmpty()) {
                LazyColumn(modifier = Modifier.height(320.dp)) {
                    items(restaurantCart.items) { cartItem ->
                        cartItem?.let {
                            CartItemRow(userId,cartItem,cartViewModel,restaurantName)
                        }
                    }
                }
            } else {
                Text(text = "Giỏ hàng trống", color = Color.Gray)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(top = 2.dp)
                    .background(Color.LightGray)
            )
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Tổng cộng:")
                val tong=restaurantCart?.total ?: 0
                val formattedPrice = NumberFormat.getCurrencyInstance(
                    Locale("vi", "VN")
                ).format(tong)
                Text(text = formattedPrice, fontSize = 20.sp)
            }
            Button(
                onClick = {
                    val items = restaurantCart?.items?.map { cartItem ->
                        OrderItem(
                            id = cartItem?.id,
                            name = cartItem?.name,
                            description = cartItem?.description,
                            image = cartItem?.image,
                            price = cartItem?.price,
                            quantity = cartItem?.quantity,
                            note = cartItem?.note
                        )
                    } ?: emptyList()

                    val order = Order(
                        custumerid = userId,
                        custumerName = user.fullName,

                        custumerPhone = user.phoneNumber,
                        custumerAdd = location,
                        date = Date(),
                        total = restaurantCart?.total,
                        restaurant = restaurantName,
                        status = OrderStatus.Processing.name,
                        items = items
                    )

                    cartViewModel.placeOrder(order, onSuccess = {
                        userId?.let {
                            cartViewModel.removeRestaurantCart(it, restaurantName ?: "", onSuccess = {
                                navController.navigate(HomeScreens.LIST.name)
                            }, onFailure = { exception ->
                                // Handle failure
                            })
                        }
                    }, onFailure = { exception ->
                        // Handle failure
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 50.dp, top = 15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = orange),
            ) {
                Text(
                    text = "Đặt đơn",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }

    }
}
    @Composable
    fun InforHeader(@DrawableRes iconResouce: Int, text: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            androidx.compose.material.Icon(
                painter = painterResource(id = iconResouce),
                contentDescription = null,
                modifier = Modifier.height(20.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
    }

    @Composable
    fun CartItemRow(userId: String?,cartItem: CartItem,cartViewModel: CartViewModel,restaurantName: String?) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    color = lightGray,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(bottom = 2.dp),
            shadowElevation = 7.dp,
            onClick = {
                //den trang chi tiet
            }
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material.Surface(
                    shape = Shapes.small,
                    modifier = Modifier.size(
                        width = 90.dp,
                        height = 90.dp
                    )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(cartItem?.image),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                        .padding(horizontal = 10.dp, vertical = 0.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${cartItem.name}",
                        fontSize = 22.sp,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${cartItem.price}",
                        fontSize = 16.sp,
                        color = grayFont,
                        fontWeight = FontWeight.Medium
                    ) //don gia

                    Spacer(modifier = Modifier.height(6.dp))
                    cartItem.price?.let { price ->
                        cartItem.quantity?.let { quantity ->
                            val totalPrice = price * quantity
                            val formattedPrice = NumberFormat.getCurrencyInstance(
                                Locale("vi", "VN")
                            ).format(totalPrice)
                            Text(
                                text = formattedPrice, // hiển thị tổng tiền của mặt hàng với 2 chữ số thập phân
                                fontSize = 17.sp,
                                color = Color.Black,
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var value by remember { mutableStateOf(cartItem.quantity) }

                    Button(
                        onClick = { value = value!! + 1
                            cartViewModel.updateCartItemQuantity(userId!!,restaurantName!!,
                                cartItem.id!!, value!!
                            )

                        },
                        contentPadding = PaddingValues(),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                0xFFFFF1D8
                            )
                        ),
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_plus),
                            null,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(23.dp)
                            .height(23.dp)
                            .clip(CircleShape)
                            .background(
                                color = orange1,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$value",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { if (value!! > 1) {
                            value = value!! - 1
                            cartViewModel.updateCartItemQuantity(userId!!,restaurantName!!,
                                cartItem.id!!, value!!
                            )
                        }
                        },
                        contentPadding = PaddingValues(),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                0xFFFFF1D8
                            )
                        ),
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_minus),
                            null,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }

                }
            }
            Row( ) {
                androidx.compose.material3.Surface(
                    onClick = {
cartViewModel.removeItemFromCart(userId!!, restaurantName!!, cartItem.id!!)
                    },
                    shape = CircleShape,
                    color = delete
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .size(20.dp)

                    )
                }
            }
        }
    }