
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.ui.theme.orange

@Composable
fun CartDetail(
    userId: String?,
    restaurantName: String?,
    cartViewModel: CartViewModel = viewModel(),
    navController: NavHostController
) {
    // Lấy giỏ hàng từ ViewModel
    userId?.let {
        cartViewModel.getCart(userId)
    }
    val cart by cartViewModel.carts.collectAsState()

    // Lọc giỏ hàng theo tên nhà hàng
    val restaurantCart = cart[restaurantName]

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
                text = "$restaurantName",
                fontSize = 24.sp,
                fontWeight = MaterialTheme.typography.h6.fontWeight,
            )

        }

        // Hiển thị chi tiết các món ăn trong giỏ hàng của nhà hàng
        if (restaurantCart != null && restaurantCart.items != null && restaurantCart.items.isNotEmpty()) {
            LazyColumn {
                items(restaurantCart.items) { cartItem ->
                    cartItem?.let {
                        CartItemRow(cartItem)
                    }
                }
            }
        } else {
            Text(text = "Giỏ hàng trống", color = Color.Gray)
        }
    }
}

@Composable
fun CartItemRow(cartItem: CartItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(cartItem.image),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                cartItem.name?.let {
                    Text(text = it, fontWeight = FontWeight.Bold)
                }
                cartItem.description?.let {
                    Text(text = it, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${cartItem.price} VND", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Số lượng: ${cartItem.quantity}", color = Color.Black)
        cartItem.note?.let {
            if (it.isNotEmpty()) {
                Text(text = "Ghi chú: $it", color = Color.Gray)
            }
        }
    }
}
