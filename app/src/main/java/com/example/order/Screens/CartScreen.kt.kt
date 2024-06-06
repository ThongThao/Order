
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.R
import com.example.order.navigation.Screen
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.secondaryFontColor
import java.text.NumberFormat
import java.util.Locale


@Composable
fun CartScreen(userId: String?, cartViewModel: CartViewModel = viewModel(), navController: NavHostController,) {
    userId?.let {
        cartViewModel.getCart(userId)
    }
    val cart by cartViewModel.carts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()){

                IconButton(
                    onClick = {  navController.popBackStack() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        tint = orange,
                        modifier = Modifier.size(24.dp)
                    )

                }
            Spacer(modifier = Modifier.width(58.dp))
                Text(
                    text = "Giỏ hàng của bạn",
                    fontSize = 24.sp,
                    fontWeight = MaterialTheme.typography.h6.fontWeight,
                )

        }

        cart?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                it.forEach { (restaurantName, restaurantCart) ->
                    item {
                        Row {

                            Column(modifier = Modifier.clickable {
                                navController.navigate(Screen.CartDetail.route)
                            }) {
                                Text(
                                    text = restaurantName,
                                    fontSize = 24.sp,
                                    fontWeight = MaterialTheme.typography.h6.fontWeight,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = restaurantCart.items.let { "${it?.size}" } + " món",
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        color = secondaryFontColor
                                    )
                                    restaurantCart.total.let {
                                        val formattedPrice =
                                            NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                                                .format(it)
                                        Text(
                                            text = formattedPrice,
                                            fontSize = 20.sp,
                                            fontWeight = MaterialTheme.typography.h6.fontWeight,
                                            color = Color.Black
                                        )
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
//                    items(restaurantCart.items ?: emptyList()) { cartItem ->
//                        cartItem?.let { item ->
//                            CartItemView(item)
//                        }
//                    }
                }
            }
        }
            ?: run {
            Text(
                text = "Giỏ hàng của bạn đang trống.",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CartItemView(cartItem: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = cartItem.name ?: "",
                fontSize = 18.sp,
                fontWeight = MaterialTheme.typography.subtitle1.fontWeight
            )
            Text(
                text = cartItem.description ?: "",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Số lượng: ${cartItem.quantity}",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Ghi chú: ${cartItem.note}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            cartItem.price?.let {
                Text(
                    text = "${it * (cartItem.quantity ?: 0)} VND",
                    fontSize = 16.sp,
                    fontWeight = MaterialTheme.typography.subtitle1.fontWeight,
                    color = Color.Black
                )
            }
        }
    }
}
