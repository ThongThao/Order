package com.example.admin.screens.Restaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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

class UpdateRestaurant : ComponentActivity() {
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
                            text = "Edit Restaurant", style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 50.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        updateResUI(
                            LocalContext.current,
                            intent.getStringExtra("restaurantName"),
                            intent.getStringExtra("restaurantAdd"),
                            intent.getStringExtra("restaurantType"),
                            intent.getStringExtra("restaurantRate"),
                            intent.getStringExtra("restaurantImage"),
                            intent.getStringExtra("restaurantId"),
                        )

                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateResUI(
    context: Context,
    name: String?,
    add: String?,
    type: String?,
    rate: String?,
    imgurl: String?,
    restaurantId: String?
){
    val restaurantName = remember {
        mutableStateOf(name)
    }
    val restaurantAdd = remember {
        mutableStateOf(add)
    }
    val restaurantType = remember {
        mutableStateOf(type)
    }
    val restaurantRate = remember {
        mutableStateOf(rate)
    }
    val restaurantImage = remember {
        mutableStateOf(imgurl)
    }
    val newImageUrl = remember {
        mutableStateOf<String?>(null)
    }
    val categories = remember { mutableStateOf<List<String>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    // Fetch categories from Firestore
    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbCategory: CollectionReference = db.collection("Category")

        dbCategory.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.documents.mapNotNull { it.getString("categoryName") }
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
            value = restaurantName.value.toString(),
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
            value = restaurantAdd.value.toString(),
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
                        text = restaurantType.value.toString(),
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
                categories.value.forEach { category ->
                    DropdownMenuItem(
                        {
                            Text(text = category, color = Color.Black)
                        },
                        onClick = {
                            restaurantType.value = category
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = restaurantRate.value.toString(),
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
        SelectItemImageSection { imageUrl ->
            // Gán URL mới cho mutable state variable
            newImageUrl.value = imageUrl
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (TextUtils.isEmpty(restaurantName.value.toString())) {
                    Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val imageUrl = newImageUrl.value ?: restaurantImage.value
                    updateResToFirebase(
                        restaurantId,
                        restaurantName.value,
                        restaurantAdd.value,
                        restaurantType.value,
                        restaurantRate.value?.toDouble(),
                        imageUrl,
                        context
                    )
                }
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
            Text(text = "UPDATE", fontWeight = FontWeight.Bold,fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.size(30.dp))
        TextButton(onClick = {
            context.startActivity(Intent(context, AllRestaurant::class.java))
        },
        ) {
            Text(
                text = "Back",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
        }
    }

}

private fun updateResToFirebase(
    restaurantId: String?,
    name: String?,
    add: String?,
    type: String?,
    rate: Double?,
    imgurl: String?,
    context: Context
) {
    val updatedRestaurant = Restaurant(restaurantId, name,add,type,rate,imgurl)

    // getting our instance from Firebase Firestore.
    val db = FirebaseFirestore.getInstance();
    db.collection("Restaurant").document(restaurantId.toString()).set(updatedRestaurant)
        .addOnSuccessListener {
            // on below line displaying toast message and opening
            // new activity to view courses.
            Toast.makeText(context, "Product Updated successfully..", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, AllRestaurant::class.java))
            //  finish()

        }.addOnFailureListener {
            Toast.makeText(context, "Fail to update product : " + it.message, Toast.LENGTH_SHORT)
                .show()
        }
}

