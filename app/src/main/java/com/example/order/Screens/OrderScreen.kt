package com.example.order.Screens

import OrderViewModel
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.order.model.Order
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.blackcart
import com.example.order.ui.theme.grayFont
import com.example.order.ui.theme.green
import com.example.order.ui.theme.menu
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
    val sortedOrders = orders.sortedByDescending { it.date }

    androidx.compose.material.Scaffold(
        scaffoldState = rememberScaffoldState(),
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                title = {
                    Text(
                        text = "Đơn hàng",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },

            )
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp)
            ) {
                orders.let {
                    allOrder(userId ,order =  sortedOrders, navController)
                }
            }
        }
    )
}
@Composable
fun getStatusText(status: String?): String {
    return when (status) {
        "Processing" -> "Chờ xác nhận"
        "Shipping" -> "Đang giao hàng"
        "Shipped" -> "Đã giao hàng"
        "Canceled" -> "Đã hủy"
        "Delivered" -> "Đã nhận"
        else -> ""
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun allOrder(userId: String?,order: List<Order>, navController: NavHostController){
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState
    ) {
        items(order) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null,
                        modifier = Modifier.height(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    it.date?. let { date ->
                        val formattedDate = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale("vi", "VN")
                        ).format(date)
                        Text(
                            text = formattedDate,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = blackcart
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    androidx.compose.material.Surface(
                        onClick = {
                            navController.navigate("order_detail/${userId!!}/${it?.id!!}")
                            Log.e("order:","${it.id}")
                        },
                        shape = Shapes.medium,
                        color = Color.White,
                        border = BorderStroke(1.dp, menu),
                        elevation = 1.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .weight(1f)
                            ) {
                                androidx.compose.material.Text(
                                    text = "${it.restaurant}",
                                    color = Color.DarkGray,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                androidx.compose.material.Text(
                                    text = "x${it.items?.size} món",
                                    color = Color.DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                it.total.let {
                                    val formattedPrice = NumberFormat.getCurrencyInstance(
                                        Locale("vi", "VN")
                                    ).format(it)
                                    androidx.compose.material.Text(
                                        text = "Tổng tiền: "+ formattedPrice,
                                        color = Color.DarkGray,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                    )
                                }
                                it.status.let {
                                    androidx.compose.material.Text(
                                        text = getStatusText(it),
                                        color = green,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                it.date?. let { date ->
                                    val formattedDate = SimpleDateFormat(
                                        "HH:mm",
                                        Locale.getDefault()
                                    ).format(date)
                                    androidx.compose.material.Text(
                                        text = formattedDate,
                                        color = grayFont,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = grayFont,
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                        .clickable { })
                            }
                        }
                    }
                }
            }
        }
    }

}
