import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodolist.data.domain.TodoItem
import com.example.mytodolist.R
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.Theme
import com.example.mytodolist.presentation.viewmodels.TodoListViewModel
import com.example.mytodolist.presentation.viewmodels.TodoListViewModelFactory
import com.example.mytodolist.presentation.theme.LocalColors
import com.example.mytodolist.presentation.theme.MyTypography
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.mytodolist.presentation.theme.MyToDoListTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.ui.input.pointer.pointerInput
import com.example.mytodolist.data.domain.ThemePreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListElement(
    todoItemsRepository: ITodoItemsRepository,
    themePreferences: ThemePreferences,
    snackbarHostState: SnackbarHostState,
    viewModel: TodoListViewModel = viewModel(factory = TodoListViewModelFactory(todoItemsRepository, themePreferences)),
    navigateToAddTask: (String?) -> Unit
) {
    val todoItemsState by viewModel.filteredTodoItems.collectAsState()
    val showCompleted by viewModel.showCompleted.collectAsState()
    val completedTasksCount by viewModel.completedTasksCount.collectAsState()

    val selectedItem by remember { mutableStateOf<TodoItem?>(null) }
    val colors = LocalColors.current
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = {
            ThemeSelectionBottomSheet(
                onThemeSelected = { newTheme ->
                    viewModel.updateTheme(newTheme)
                },
                onDismiss = { scope.launch { scaffoldState.bottomSheetState.hide() } }
            )
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            scope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                    )
                }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        scrollBehavior = scrollBehavior,
                        showCompleted = showCompleted,
                        completedTasksCount = completedTasksCount,
                        onChangeCompletedTasksVisibilityClick = viewModel::toggleShowCompleted,
                        onSettingsClick = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                        onInfoClick = { viewModel.onInfoClick(context) }
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
    }
}

@Composable
fun ThemeSelectionBottomSheet(
    onDismiss: () -> Unit,
    onThemeSelected: (Theme) -> Unit
) {
    val themes = listOf("Light", "Dark", "System Default")

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        themes.forEach { theme ->
            Text(
                text = theme,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val selectedTheme = when (theme) {
                            "Light" -> Theme.LIGHT
                            "Dark" -> Theme.DARK
                            else -> Theme.SYSTEM
                        }
                        onThemeSelected(selectedTheme)
                        onDismiss()
                    }
                    .padding(16.dp)
            )
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
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit
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
            IconButton(onClick = onInfoClick) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "") //Todo
            }
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "") //Todo
            }
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
