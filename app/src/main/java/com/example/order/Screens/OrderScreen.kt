package com.example.order.Screens

import OrderViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.order.model.Order
import com.example.order.ui.theme.blue2
import com.example.order.ui.theme.blue3
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.yellow1
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    userId:String?,
    orderViewModel: OrderViewModel = viewModel(),
    navController: NavHostController
) {

    val orders by orderViewModel.orders.collectAsState()
    LaunchedEffect(userId) {
        userId?.let { orderViewModel.fetchOrdersForUser(it) }
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
                            text = "Đơn hàng",
                            fontSize = 25.sp,
                            fontWeight = MaterialTheme.typography.h6.fontWeight,
                            modifier = Modifier.padding(start = 125.dp)
                        )
                    },

                    )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp)
            ) {
                orders.let {
                    allOrder(order = orders,  navController)
                }
            }
        }
}
@Composable
fun getStatusText(status: String?): String {
    return when (status) {
        "Processing" -> "Chờ xác nhận"
        "Shipping" -> "Đang giao hàng"
        "Canceled" -> "Đã hủy"
        "Delivered" -> "Đã nhận"
        else -> ""
    }
}
@Composable
fun allOrder(order: List<Order>, navController: NavHostController){
    LazyColumn {
        items(order){
            Surface(
                color = blue2,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    navController.navigate("order_detail/${it.id}")
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Row() {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(top = 3.dp),
                                color = blue3
                            ) {
                                it.date?.let { date ->
                                    val formattedDate = SimpleDateFormat(
                                        "dd/MM/yyyy HH:mm",
                                        Locale.getDefault()
                                    ).format(date)
                                    Text(
                                        text = formattedDate,
                                        fontSize = 12.sp,
                                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 8.dp
                                        ),
                                        color = Color.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(top = 3.dp),
                                color = yellow1
                            ) {
                                it.status?.let {
                                    Text(
                                        text =  getStatusText(it),
                                        fontSize = 12.sp,
                                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(
                                            vertical = 4.dp,
                                            horizontal = 8.dp
                                        ),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${it.restaurant}",
                            modifier = Modifier.padding(top = 5.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        it.items?.size.let {
                            Text(text = "$it món")
                            
                        }
                        
                        it.total?.let {
                            val formattedPrice = NumberFormat.getCurrencyInstance(
                                Locale("vi", "VN")
                            ).format(it)
                            Text(
                                text = "Tổng tiền: $formattedPrice",
                                modifier = Modifier.padding(top = 5.dp),
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}