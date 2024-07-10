
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mytodolist.data.Importance
import com.example.mytodolist.data.TodoItem
import com.example.mytodolist.R
import com.example.mytodolist.presentation.theme.LocalColors
import com.example.mytodolist.presentation.theme.MyTypography
import java.text.DateFormat
import java.util.Date

@Composable
fun TodoItemElement(
    todoItem: TodoItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val colors = LocalColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                if (isSelected) colors.backSecondary
                else colors.backSecondary
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .background(
                    if (isSelected) colors.backSecondary
                    else colors.backSecondary
                )
        )
        {
            Checkbox(
                checked = todoItem.done,
                onCheckedChange = { isChecked -> onCheckedChange(isChecked) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (todoItem.importance != Importance.basic.toString()) {
                val painter = when(todoItem.importance) {
                    Importance.important.toString() -> painterResource(id = R.drawable.high_priority)
                    Importance.low.toString() -> painterResource(id = R.drawable.low_priority)
                    else -> null
                }
                painter?.let {
                    Image(
                        painter = it,
                        contentDescription = "", // TODO повставлять описания
                        modifier = Modifier
                            .width(10.dp)
                            .height(16.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            TodoItemText(
                text = todoItem.text,
                done = todoItem.done,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.info_outline),
                contentDescription = "", // TODO повставлять описания
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        todoItem.deadline?.let { deadline ->
            val formattedDeadline = DateFormat.getDateInstance().format(Date(deadline.toLong()))
            Text(
                text = formattedDeadline,
                color = colors.colorBlue,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun TodoItemText(
    text: String?,
    done: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text?: stringResource(id = R.string.wait),
        style = if (done) {
            MyTypography.body().copy(
                textDecoration = TextDecoration.LineThrough
            )
        } else {
            MyTypography.body()
        },
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}