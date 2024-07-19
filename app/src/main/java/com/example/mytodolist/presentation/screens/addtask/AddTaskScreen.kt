import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mytodolist.R
import com.example.mytodolist.presentation.theme.LocalColors
import com.example.mytodolist.presentation.theme.MyTypography
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Switch
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SwitchColors
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodolist.AddTaskViewModel
import com.example.mytodolist.AddTaskViewModelFactory
import java.text.DateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mytodolist.App
import com.example.mytodolist.data.domain.ITodoItemsRepository
import com.example.mytodolist.data.domain.Importance

@Composable
fun AddTaskScreen(
    todoItemsRepository: ITodoItemsRepository,
    taskId: String?,
    navController: NavHostController,
    viewModel: AddTaskViewModel = viewModel(factory = AddTaskViewModelFactory(todoItemsRepository, taskId)),
) {
    val colors = LocalColors.current
    val importance by viewModel.importance.collectAsState()
    val deadline by viewModel.deadline.collectAsState()
    val deleteEnabled by viewModel.deleteEnabled.collectAsState()
    val taskDescription by viewModel.taskDescription.collectAsState()

    var dropDownMenuVisible by remember { mutableStateOf(false) }
    var datePickerVisible by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopBar(
                onCloseClick = { navController.popBackStack() },
                onSaveClick = { viewModel.saveTask(); navController.popBackStack() }
            )
        },
        containerColor = colors.backPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(enabled = true, state = rememberScrollState())
        ) {
            CustomTextField(
                value = taskDescription,
                onValueChange = viewModel::onDescriptionChange
            )
            Spacer(Modifier.height(16.dp))
            PrioritySelect(
                dropDownMenuVisible,
                importance,
                viewModel::onPriorityChange,
            ) {
                dropDownMenuVisible = it
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            DeadlinePicker(datePickerVisible, viewModel::onDeadlineChange) { datePickerVisible = false }

            DeadlineView(deadline?.toString(), datePickerVisible, viewModel::onDeadlineChange) {
                datePickerVisible = true
            }

            if (deleteEnabled) {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                TextButton(
                    onClick = { viewModel.deleteTask(); navController.popBackStack() },
                    colors = ButtonDefaults.textButtonColors(colors.colorRed)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "" //TODO
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(text = stringResource(id = R.string.delete))
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun DeadlineView(
    deadline: String?,
    datePickerVisible: Boolean,
    onDeadlineChange: (Long?) -> Unit,
    showDatePicker: () -> Unit = {},
) {
    val colors = LocalColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                style = MyTypography.body(),
            )
            if (!deadline.isNullOrBlank()) {
                val formattedDeadline = DateFormat.getDateInstance().format(Date(deadline.toLong()))
                Text(
                    text = formattedDeadline,
                    style = MyTypography.subhead()
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Switch(
            checked = !deadline.isNullOrBlank() || datePickerVisible,
            onCheckedChange = {
                if (it) showDatePicker()
                else onDeadlineChange(null)
            },
            colors = SwitchColors(
                checkedIconColor = colors.colorBlue,
                uncheckedIconColor = colors.colorBlue,
                disabledCheckedIconColor = colors.colorBlue,
                disabledUncheckedIconColor = colors.colorBlue,

                checkedBorderColor = colors.backPrimary,
                uncheckedBorderColor = colors.backPrimary,
                disabledCheckedBorderColor = colors.backPrimary,
                disabledUncheckedBorderColor = colors.backPrimary,

                checkedThumbColor = colors.colorBlue,
                uncheckedThumbColor = colors.labelPrimary,
                disabledCheckedThumbColor = colors.colorBlue,
                disabledUncheckedThumbColor = colors.labelPrimary,

                checkedTrackColor = colors.colorBlue.copy(alpha = 0.5f),
                uncheckedTrackColor = colors.labelSecondary,
                disabledCheckedTrackColor = colors.colorBlue.copy(alpha = 0.5f),
                disabledUncheckedTrackColor = colors.labelSecondary,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    datePickerVisible: Boolean,
    onDeadlineChange: (deadline: Long?) -> Unit,
    onDismissRequest: () -> Unit = {},
) {
    if (datePickerVisible) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = {
                    onDeadlineChange(datePickerState.selectedDateMillis)
                    onDismissRequest()
                }) {
                    Text(
                        text = stringResource(id = R.string.apply),
                        style = MyTypography.body()
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MyTypography.body()
                    )
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val colors = LocalColors.current
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "" //TODO
                )
            }
        },
        actions = {
            TextButton(onClick = onSaveClick) {
                Text(
                    text = stringResource(id = R.string.save).uppercase(),
                    color = colors.labelPrimary
                )
            }
        },
        colors = topAppBarColors(
            containerColor = colors.backPrimary,
            titleContentColor = colors.labelPrimary,
            navigationIconContentColor = colors.labelPrimary
        )
    )
}



@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    val colors = LocalColors.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Adjust padding as needed
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = colors.backSecondary,
            unfocusedContainerColor = colors.backSecondary,
            focusedLabelColor = colors.labelPrimary,
            unfocusedTextColor = colors.labelSecondary
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun PrioritySelect(
    dropDownMenuVisible: Boolean,
    importance: Importance,
    onPriorityChange: (Importance) -> Unit,
    changeMenuVisibility: (Boolean) -> Unit
) {
    val colors = LocalColors.current
    Box(
        Modifier
            .fillMaxWidth()
            .clickable { changeMenuVisibility(true) }
    ) {
        Column {
            Text(
                text = "Важность",
                style = MyTypography.body(),
            )
            Text(
                text = when (importance) {
                    Importance.low -> stringResource(id = R.string.priority_low)
                    Importance.basic -> stringResource(id = R.string.priority_default)
                    Importance.important -> stringResource(id = R.string.priority_high)
                },
                style = MyTypography.subhead(),
            )
        }
        DropdownMenu(
            expanded = dropDownMenuVisible,
            onDismissRequest = { changeMenuVisibility(false) },
            modifier = Modifier
                .background(color = colors.backSecondary)
        ) {
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(id = R.string.priority_default),
                    style = MyTypography.body()
                )
            }, onClick = { onPriorityChange(Importance.basic); changeMenuVisibility(false) })
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(id = R.string.priority_low),
                    style = MyTypography.body()
                )
            }, onClick = { onPriorityChange(Importance.low); changeMenuVisibility(false) })
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(id = R.string.priority_high),
                    style = MyTypography.body(),
                    color = colors.colorRed
                )
            }, onClick = { onPriorityChange(Importance.important); changeMenuVisibility(false) })
        }
    }
}