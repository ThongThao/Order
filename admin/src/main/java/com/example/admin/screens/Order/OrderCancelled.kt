package com.example.admin.screens.Order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.model.Order
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blackcart
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.delete
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class OrderCancelled : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    var pendingList by remember { mutableStateOf(listOf<Order>()) }

                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbPending: CollectionReference = db.collection("orders")

                    dbPending.whereEqualTo("status", "Canceled").get().addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty) {
                            val list = queryDocumentSnapshot.documents.mapNotNull { d ->
                                d.toObject<Order>()?.apply { id = d.id }
                            }
                            pendingList = list
                        } else {
                            Toast.makeText(
                                this@OrderCancelled,
                                "No data found in Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@OrderCancelled,
                            "Fail to get the data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    CancelledScreen(context, pendingList)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelledScreen(context: Context, pendingList: List<Order>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = blue,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = {
                Text(
                    text = "Order Cancelled",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    context.startActivity(Intent(context, OrderManager::class.java))
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            },
        )
        LazyColumn(
            Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
        ) {
            items(pendingList) { order ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = null,
                                modifier = Modifier.height(20.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            order.date?.let { date ->
                                val formattedDate = SimpleDateFormat(
                                    "dd/MM/yyyy HH:mm",
                                    Locale("vi", "VN")
                                ).format(date)
                                Text(
                                    text = formattedDate,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontSize = 15.sp,
                                    color = blackcart
                                )
                            }
                        }

                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFECE0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFDDC7),
                                shape = RoundedCornerShape(12.dp),
                            )
                            .padding(bottom = 2.dp),
                        shadowElevation = 6.dp,
                        onClick = {
                            val i = Intent(context, OrderDetail::class.java)
                            i.putExtra("OrderID", order.id)
                            context.startActivity(i)
                        }
                    ) {
                        Row {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                                    .weight(2f),
                            ) {
                                androidx.compose.material3.Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 12.dp),
                                    color = delete
                                ) {

                                    order.status?.let {
                                        Text(
                                            text = it,
                                            fontSize = 15.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 8.dp
                                            ),
                                            color = Color.White
                                        )
                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 13.dp)
                                ) {
                                    Text(
                                        text = "Total:",
                                        fontSize = 17.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    order.total?.let {
                                        val formattedPrice = NumberFormat.getCurrencyInstance(
                                            Locale("vi", "VN")
                                        ).format(it)
                                        Text(
                                            text = formattedPrice,
                                            fontSize = 18.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 5.dp)
                                ) {
                                    Text(
                                        text = "Customer:",
                                        fontSize = 17.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${order.custumerName}",
                                        fontSize = 18.sp,
                                        color = blackcart,
                                        fontWeight = FontWeight.Medium
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
