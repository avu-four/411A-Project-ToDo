package com.example.todoapp

data class ToDoItem(
    val id: Int,
    val text: String,
    val isDone: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp()
        }
    }
}

@Composable
fun ToDoApp() {
    val context = LocalContext.current
    var text by rememberSaveable { mutableStateOf("") }
    var items by rememberSaveable { mutableStateOf(listOf<ToDoItem>()) }

    Scaffold (
        topBar = { TopAppBar(title = { Text("To-Do List") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ToDoImpact(
                text = text,
                onTextChange = { text = it },
                onAdd = {
                    val trimmed = text.trim()
                    if (trimmed.isNotEmpty()) {
                        items = items + ToDoItem(id = items.size + 1, text = trimmed)
                        text = ""
                    } else {
                        Toast.makeTest(context, "Task name cannot be blank", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            ToDoSection(
                title = "Items",
                items = items.filter { !it.isDone },
                onToggle = { item ->
                    items = items.map { currentItem ->
                        if (currentItem.id == currentitem.id) {
                            currentItem.copy(isDone = true)
                        } else {
                            currentItem
                        }
                    }
                },
                onDelete = { item ->
                    items = items.filterNot { currentItem ->
                        currentItem.id == item.id
                    }
                }
            )
        }
    }
}

@Composable
fun ToDoInput(text: String, onTextChange: (String) -> Unit, onAdd: () -> Unit) {
    Row {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Enter the task name") },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onAdd) { Text("Add") }
    }
}

@Composable
fun ToDoSection(
    title: String,
    items: List<ToDoItem>,
    onToggle: (ToDoItem) -> Unit,
    onDelete: (ToDoItem) -> Unit
) {
    if (items.isNotEmpty()) {
        Text(
            title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        items.forEach { item ->
            ToDoRow(item, onToggle, onDelete)
        }
    } else {
        Text(
            "No $title yet",
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ToDoRow(item: ToDoItem, onToggle: (ToDoItem) -> Unit, onDelete: (ToDoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.cyan, RoundedCornerShape(8.dp))
            .padding(8.dp)
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.text, Modifier.weight(1f))
        Checkbox(checked = item.isDone, onCheckedChange = { onToggle(item) })
        IconButton(onClick = { onDelete(item) }) {
            Icon(Icons.Default.Close, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    TodoApp()
}