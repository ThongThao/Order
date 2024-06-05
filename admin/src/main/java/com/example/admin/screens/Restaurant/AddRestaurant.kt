package com.example.admin.screens.Restaurant

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.R
import com.example.admin.model.Restaurant
import com.example.admin.screens.SelectItemImageSection
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.grayFont
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class AddRestaurant : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp),
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "",
                                tint = blue,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(60.dp))
                        Text(
                            text = "Add Restaurant", style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(0.dp, 50.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    AddRestaurantUI(LocalContext.current)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRestaurantUI(context: Context){

    val restaurantName = remember {
        mutableStateOf("")
    }
    val restaurantAdd = remember {
        mutableStateOf("")
    }
    val restaurantType = remember {
        mutableStateOf("")
    }
    val restaurantRate = remember {
        mutableStateOf("")
    }
    val restaurantImage = remember {
        mutableStateOf("")
    }
    val categories = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    // Fetch categories from Firestore
    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbCategory: CollectionReference = db.collection("Category")

        dbCategory.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.documents.mapNotNull {
                Pair(it.id, it.getString("categoryName") ?: "")
            }
            categories.value = list
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting categories", exception)
        }
    }


    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = restaurantName.value,
            onValueChange = { restaurantName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Name", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = restaurantAdd.value,
            onValueChange = { restaurantAdd.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Address", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Category",
                color = grayFont
            )
            Spacer(modifier = Modifier.size(7.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                modifier = Modifier.height(58.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = restaurantType.value,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        style = TextStyle(color = Color.Black),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.padding(end=15.dp)
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(150.dp)
            ) {
                categories.value.forEach { (categoryId,categoryName) ->
                    DropdownMenuItem(
                        {
                            Text(text = categoryName, color = Color.Black)
                        },
                        onClick = {
                            restaurantType.value = categoryName
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = restaurantRate.value,
            onValueChange = { restaurantRate.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Rate", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(9.dp))
        SelectItemImageSection{
                imageUrl -> restaurantImage.value = imageUrl // Assign the URL to the mutableState variable

        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                addRestaurant(
                    restaurantName.value,
                    restaurantAdd.value,
                    restaurantType.value,
                    restaurantRate.value,
                    restaurantImage.value,
                    context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            ),
//                border = BorderStroke(0.5.dp, Color.Red)
        ) {
            Text(text = "ADD", fontWeight = FontWeight.Bold,fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(30.dp))
        TextButton(onClick = {
            context.startActivity(Intent(context, AllRestaurant::class.java))
        },
        ) {
            Text(
                text = "View all restaurant",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
        }
    }
}

fun addRestaurant(
    restaurantName: String,
    restaurantAdd: String,
    restaurantType: String,
    restaurantRate: String,
    restaurantImage: String,
    context: Context
){

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbRestaurant: CollectionReference = db.collection("Restaurant")
    // Kiểm tra xem restaurantName đã tồn tại trong Firebase chưa
    dbRestaurant.whereEqualTo("restaurantName", restaurantName)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Nếu restaurantName không tồn tại, thêm nhà hàng mới
                val restaurantId = UUID.randomUUID().toString()
                val restaurant = Restaurant(
                    restaurantId,
                    restaurantName,
                    restaurantAdd,
                    restaurantType,
                    restaurantRate.toDouble(),
                    restaurantImage
                )

                // Thêm nhà hàng vào Collection "Restaurant"
                dbRestaurant.add(restaurant)
                    .addOnSuccessListener { restaurantDocument ->
                        // Thêm nhà hàng vào danh sách nhà hàng của category tương ứng
                        addToCategory(restaurantType, restaurantDocument.id)
                        Toast.makeText(
                            context,
                            "Added Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Failed to add restaurant\n$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    context,
                    "Restaurant with name $restaurantName already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Failed to check restaurant existence\n$e",
                Toast.LENGTH_SHORT
            ).show()
        }
}


private fun addToCategory(categoryName: String, restaurantId: String) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCategory: CollectionReference = db.collection("Category")

    // Tìm category có categoryId tương ứng
    dbCategory.whereEqualTo("categoryName", categoryName)
        .get()
        .addOnSuccessListener { querySnapshot ->
            // Lấy document đầu tiên trong kết quả
            val categoryDocument = querySnapshot.documents.firstOrNull()
            if (categoryDocument != null) {
                // Lấy danh sách nhà hàng hiện có trong category
                val restaurants = categoryDocument.get("restaurants") as? MutableList<String>
                // Nếu danh sách không null, thêm restaurantId mới vào danh sách
                if (restaurants != null) {
                    restaurants.add(restaurantId)
                    // Cập nhật lại trường restaurants trong category
                    categoryDocument.reference.update("restaurants", restaurants)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Restaurant added to category successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to add restaurant to category: $e")
                        }
                } else {
                    // Nếu danh sách null, tạo danh sách mới và thêm restaurantId vào danh sách
                    val newRestaurants = mutableListOf<String>()
                    newRestaurants.add(restaurantId)
                    // Cập nhật lại trường restaurants trong category
                    categoryDocument.reference.update("restaurants", newRestaurants)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Restaurant added to category successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to add restaurant to category: $e")
                        }
                }
            }
        }
}





