package com.example.myshoppinglistapp

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Item(
    var id: Int,
    var name: String,
    var quantity: Int,
    var IsEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun ShoppingListItem(
    item: Item,
    isEditable: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(

        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Cyan),
                shape = RoundedCornerShape(20)
            ),horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        Text(text = item.name, Modifier.padding(16.dp))

        Text(text = item.quantity.toString(), Modifier.padding(16.dp))
        IconButton(onClick =  isEditable ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color(0xFFFD843F),
                modifier = Modifier
                    .size(20.dp)



                )
        }
        IconButton(onClick =  onDeleteClick ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                tint = Color(0xFFFD843F)
            )
        }


    }

}

@Composable
fun ShoppingItemEditor(item: Item, onEditComplete: (String, Int) -> Unit) {
    var editName by remember {
        mutableStateOf(item.name)
    }
    var editQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.IsEditing)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column {
            BasicTextField(
                value = editName,
                onValueChange = {
                    editName = it
                },
            )

            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = editQuantity,
                onValueChange = {
                    editQuantity = it
                },
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editName, editQuantity.toIntOrNull() ?: 1)
        }) { Text("Save") }
    }


}

@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<Item>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf(1) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                showDialog = true

            },
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
                    item->
                if(item.IsEditing)
                {
                    ShoppingItemEditor(item = item, onEditComplete = {
                            editname,editquantity ->
                        sItems = sItems.map {it.copy(IsEditing =false)}
                        val editItem=sItems.find{it.id==item.id}
                        editItem?.let{
                            it.name=editname
                            it.quantity=editquantity
                        }

                    })
                }
                else {
                    ShoppingListItem(item = item, {
                        sItems = sItems.map { it.copy(IsEditing = it.id==item.id) }
                    }) { sItems = sItems - item }
                }

            }

        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween

                ) {
                    Button(onClick = {
                        if (itemName.isNotEmpty()) {
                            val newItem = Item(
                                sItems.size + 1,
                                itemName, itemQuantity,

                                )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = 1

                        }
                    }) {
                        Text("Add Item")
                    }
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }

                }

            },
            title = { Text(text = "Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {
                            itemName = it
                        })
                    OutlinedTextField(
                        value = itemQuantity.toString(),
                        onValueChange = {
                            itemQuantity = it.toInt()
                        },
                        singleLine = true,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )

                }
            }
        )
    }
}




