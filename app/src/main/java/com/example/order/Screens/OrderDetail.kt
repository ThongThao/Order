
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.order.model.Order
import com.example.order.model.OrderItem
import com.example.order.ui.theme.lightGray
import com.example.order.ui.theme.orange
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderDetail(
    orderId: String,
    orderViewModel: OrderViewModel = viewModel(),
    navController: NavHostController // Inject NavHostController for navigation
) {
    // Fetch order details from ViewModel using orderId
    var orderDetail by remember(orderId) {
        mutableStateOf<Order?>(null)
    }
    val ship=20000

    LaunchedEffect(orderId) {
        orderViewModel.getOrderDetail(orderId).collect { order ->
            orderDetail = order
        }
    }
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
                text = orderDetail?.restaurant ?: "",
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
                        text = orderDetail?.custumerName ?: "",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    //so dien thoai
                    Text(
                        text = orderDetail?.custumerPhone ?: "",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    //so duong
                    Text(
                        text = orderDetail?.custumerAdd ?: "",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InforHeader(R.drawable.ic_shopping_bag, "Tóm tắt đơn hàng")
        }
        LazyColumn(modifier = Modifier.height(280.dp)) {
            item {
                orderDetail?.let { order ->
                    order.items?.let { items ->
                        items.forEach { item ->
                            ItemRow(item)
                        }
                    }
                } ?: run {
                    Text("Loading order detail...")
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
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Phí giao hàng:")
            val formattedPrice = NumberFormat.getCurrencyInstance(
                Locale("vi", "VN")
            ).format(ship)
            Text(text = formattedPrice, fontSize = 20.sp)

        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Tổng cộng:")
            val tong= ship +  (orderDetail?.total ?: 0)
            val formattedPrice = NumberFormat.getCurrencyInstance(
                Locale("vi", "VN")
            ).format(tong)
            Text(text = formattedPrice, fontSize = 20.sp)
        }
        if (orderDetail?.status == "Shipping") {
            Button(
                onClick = {
                    updateOrderStatus(orderDetail?.id ?: "", OrderStatus.Shipped)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 50.dp, top = 15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = orange),
            ) {
                Text(
                    text = "Đã nhận hàng",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }

}
fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    val db = FirebaseFirestore.getInstance()
    val orderRef = db.collection("orders").document(orderId)

    orderRef.update("status", newStatus.name)
        .addOnSuccessListener {
            Log.d("UpdateStatus", "Order status updated successfully.")
        }
        .addOnFailureListener { e ->
            Log.w("UpdateStatus", "Error updating order status", e)
        }
}

@Composable
fun ItemRow(item: OrderItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(width = 80.dp, height = 80.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                item.name?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                }
                Text(text = "x${item.quantity}", color = Color.Black)
                item.note?.let {
                    if (it.isNotEmpty()) {
                        Text(text = "Ghi chú: $it", color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            item.price?.let {
                val formattedPrice = NumberFormat.getCurrencyInstance(
                    Locale("vi", "VN")
                ).format(it)
                Text(text = formattedPrice, color = orange, fontSize = 20.sp)
            }
        }

    }
}