import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodolist.Classes.TodoApi
import com.example.mytodolist.Classes.TodoItem
import com.example.mytodolist.Classes.TodoItemsRepository
import com.example.mytodolist.R
import com.example.mytodolist.ui.screen.todolist.TodoListViewModel
import com.example.mytodolist.ui.screen.todolist.TodoListViewModelFactory
import com.example.mytodolist.ui.theme.LocalColors
import com.example.mytodolist.ui.theme.MyTypography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoListElement(
    todoItemsRepository: TodoItemsRepository,
    snackbarHostState: SnackbarHostState,
    viewModel: TodoListViewModel = viewModel(factory = TodoListViewModelFactory(todoItemsRepository)),
    navigateToAddTask: (String?) -> Unit
) {
    val todoItemsState by viewModel.filteredTodoItems.collectAsState()
    val showCompleted by viewModel.showCompleted.collectAsState()
    val completedTasksCount by viewModel.completedTasksCount.collectAsState()

    val selectedItem by remember { mutableStateOf<TodoItem?>(null) }
    val colors = LocalColors.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopBar(
                scrollBehavior = scrollBehavior,
                showCompleted = showCompleted,
                completedTasksCount = completedTasksCount,
                onChangeCompletedTasksVisibilityClick = viewModel::toggleShowCompleted,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddTask("0") },
                shape = CircleShape,
                containerColor = colors.backElevated
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        },
        containerColor = colors.backPrimary,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colors.backPrimary)
                    .padding(horizontal = 16.dp)
            ) {
                if (todoItemsState.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(colors.backSecondary)
                        )
                    }
                }
                items(todoItemsState) { item ->
                    TodoItemElement(
                        todoItem = item,
                        isSelected = item == selectedItem,
                        onClick = { navigateToAddTask(item.id) },
                        onCheckedChange = { isChecked -> viewModel.updateTodoItemCompletion(item.id, isChecked) }
                    )
                }
                if (todoItemsState.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(colors.backSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    showCompleted: Boolean,
    completedTasksCount: Int,
    onChangeCompletedTasksVisibilityClick: () -> Unit,
) {
    val colors = LocalColors.current
    TopAppBar(
        title = {
            Column {
                Text(text = stringResource(id = R.string.greeting))
                Text(
                    text = stringResource(id = R.string.completed) + " - $completedTasksCount",
                    style = MyTypography.body()
                )
            }
        },
        actions = {
            IconButton(onClick = onChangeCompletedTasksVisibilityClick) {
                if (showCompleted) {
                    Icon(
                        painter = painterResource(id = R.drawable.visibility),
                        contentDescription = "" //TODO
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.visibility_off),
                        contentDescription = ""//TODO
                    )
                }
            }
        },
        colors = topAppBarColors(
            containerColor = colors.backPrimary,
            titleContentColor = colors.labelPrimary,
            actionIconContentColor = colors.colorBlue,
        ),
        scrollBehavior = scrollBehavior
    )
}

