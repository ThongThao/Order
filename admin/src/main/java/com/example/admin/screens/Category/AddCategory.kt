package com.example.admin.screens.Category

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.R
import com.example.admin.model.Category
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.grayFont
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID
import coil.compose.rememberImagePainter
import com.example.admin.screens.SelectItemImageSection


class AddCategory : ComponentActivity() {

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
                            text = "Add Category", style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 50.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){

                    addUI(LocalContext.current)

                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addUI(context: Context){
    val categoryName = remember {
        mutableStateOf("")
    }
    val categoryImage = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = categoryName.value,
            onValueChange = { categoryName.value = it },
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
        SelectItemImageSection{
                imageUrl -> categoryImage.value = imageUrl // Assign the URL to the mutableState variable

        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                addCategory(categoryName.value,categoryImage.value, context)
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
            context.startActivity(Intent(context, AllCategory::class.java))
        },
        ) {
            Text(
                text = "View all category",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
        }
    }
}


fun addCategory(
    categoryName: String,
    categoryImage: String,
    context: Context
){

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCategory: CollectionReference = db.collection("Category")

    // Kiểm tra xem categoryName đã tồn tại trong Firebase chưa
    dbCategory.whereEqualTo("categoryName", categoryName)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Nếu categoryName không tồn tại, thêm category mới
                val categoryId = UUID.randomUUID().toString()
                val category = Category(categoryId, categoryName, categoryImage)

                dbCategory.add(category)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Added Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Failed to add category\n$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                // Nếu categoryName đã tồn tại, thông báo lỗi
                Toast.makeText(
                    context,
                    "Category with name $categoryName already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Failed to check category existence\n$e",
                Toast.LENGTH_SHORT
            ).show()
        }
}





