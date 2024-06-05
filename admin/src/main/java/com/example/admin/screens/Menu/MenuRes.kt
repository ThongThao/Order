package com.example.admin.screens.Menu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.admin.R
import com.example.admin.model.Restaurant
import com.example.admin.screens.Home
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blue2
import com.example.admin.ui.theme.blue3
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class MenuRes : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                var restaurantList = mutableStateListOf<Restaurant>()
                var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                val dbRestaurant: CollectionReference = db.collection("Restaurant")

                dbRestaurant.get().addOnSuccessListener { queryDocumentSnapshot ->
                    if (!queryDocumentSnapshot.isEmpty){
                        val list = queryDocumentSnapshot.documents
                        for (d in list){
                            val c: Restaurant? = d.toObject(Restaurant::class.java)
                            c?.restaurantId = d.id
                            Log.e("TAG", "Course id is : " + c!!.restaurantId)
                            restaurantList.add(c)
                        }
                    }else{
                        Toast.makeText(
                            this@MenuRes,
                            "No data found in Database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this@MenuRes,
                        "Fail to get the data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                MenuResUI(LocalContext.current, restaurantList)

        }
    }
}
@SuppressLint("SuspiciousIndentation")
@Composable
fun MenuResUI(context: Context, restaurantList: SnapshotStateList<Restaurant>) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { context.startActivity(Intent(context, Home::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(60.dp))
            Text(
                text = "Menu Manager", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

        }
        LazyColumn {
            itemsIndexed(restaurantList) { index, item ->
                Surface(
                    color = blue2,
                    modifier = Modifier
                        .height(340.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),

                    shadowElevation = 10.dp,
                    onClick = {
                        val i = Intent(context, AllItem::class.java)
                        i.putExtra("restaurantId", item?.restaurantId)
                        i.putExtra("restaurantName", item?.restaurantName)
                        i.putExtra("restaurantImage", item?.restaurantImage)
                        i.putStringArrayListExtra("menu", ArrayList(item?.menu ?: emptyList()))


                        context.startActivity(i)
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(width = 360.dp, height = 200.dp)
                        ) {
                            restaurantList[index]?.restaurantImage?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(2f),
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .padding(top = 3.dp),
                                            color = blue3
                                        ) {
                                            restaurantList[index]?.restaurantType?.let {
                                                Text(
                                                    text = it,
                                                    fontSize = 13.sp,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    modifier = Modifier.padding(
                                                        vertical = 4.dp,
                                                        horizontal = 8.dp
                                                    ),
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        restaurantList[index]?.restaurantName?.let {
                                            Text(
                                                text = it,
                                                fontSize = 18.sp,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row() {
                                            Text(text = "Menu: ")

                                            Text(
                                                text = restaurantList[index]?.menu?.let {
                                                    "${it.size}"
                                                } ?: "0"
                                            )
                                            Text(text = " items")
                                        }


                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
