package com.example.admin.screens.Category

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.admin.R
import com.example.admin.model.Category
import com.example.admin.screens.Home
import com.example.admin.ui.theme.Purple80
import com.example.admin.ui.theme.blue2
import com.example.admin.ui.theme.blueColor
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class AllCategory : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blueColor,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "All Category",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    startActivity(Intent(this@AllCategory, Home::class.java))
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            }
                        )
                    }
                ) {
                    var categoryList = mutableStateListOf<Category>()
                    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
                    val dbCategory: CollectionReference = db.collection("Category")

                    dbCategory.get().addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty){
                            val list = queryDocumentSnapshot.documents
                            for (d in list){
                                val c: Category? = d.toObject(Category::class.java)
                                c?.categoryId = d.id
                                Log.e("TAG", "Course id is : " + c!!.categoryId)
                                categoryList.add(c)
                            }
                        }else{
                            Toast.makeText(
                                this@AllCategory,
                                "No data found in Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@AllCategory,
                            "Fail to get the data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    allUI(LocalContext.current, categoryList, navController = rememberNavController())
                }
            }
        }
    }
}
private fun deleteDataFromFirebase(categoryId: String?, context: Context) {

    // getting our instance from Firebase Firestore.
    val db = FirebaseFirestore.getInstance();

    // below line is for getting the collection
    // where we are storing our courses.
    db.collection("Category").document(categoryId.toString()).delete().addOnSuccessListener {
        Log.e("TAG", "Course id is : " +categoryId)

        Toast.makeText(context, "Product Deleted successfully..", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, AllCategory::class.java))
    }.addOnFailureListener {
        // on below line displaying toast message when
        // we are not able to delete the course
        Toast.makeText(context, "Fail to delete item..", Toast.LENGTH_SHORT).show()
    }

}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState","SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun allUI(context: Context, productList: SnapshotStateList<Category>,navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = { context.startActivity(Intent(context, AddCategory::class.java)) }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }

        LazyColumn {
            itemsIndexed(productList) {index, item ->
                Surface(
                    color = blue2,
                    modifier = Modifier
                        .height(125.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),

                    shadowElevation = 10.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(width = 80.dp, height = 80.dp)
                        ) {
                            productList[index]?.categoryImage?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Spacer(modifier = Modifier.height(6.dp))

                            productList[index]?.categoryName?.let {
                                Text(
                                    text = it,
                                    fontSize =  24.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                        }
                        Column( modifier = Modifier.padding(end=10.dp)) {

                            IconButton(
                                onClick = {
                                    val i = Intent(context, UpdateCategory::class.java)
                                    i.putExtra("categoryName", item?.categoryName)
                                    i.putExtra("categoryImage", item?.categoryImage)
                                    i.putExtra("categoryId", item?.categoryId)

                                    context.startActivity(i)
                                          },
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_edit),
                                    contentDescription = "",
                                    tint = Purple80,
                                    modifier = Modifier.size(27.dp)
                                )
                            }
                            IconButton(
                                onClick = { deleteDataFromFirebase(productList[index]?.categoryId, context) },
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "",
                                    tint= Color.Red,
                                    modifier = Modifier.size(27.dp)

                                )
                            }

                        }


                    }
                }
            }
        }
    }
}
