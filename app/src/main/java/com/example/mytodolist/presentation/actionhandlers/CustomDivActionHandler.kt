import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.example.mytodolist.MainActivity
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class CustomDivActionHandler(private val context: Context) : DivActionHandler() {

    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)
        val host = url.host
        val scheme = url.scheme
        return if (scheme == SCHEME && host == "custom_action" && handleCustomAction()) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleCustomAction(): Boolean {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)

        if (context is Activity) {
            (context as Activity).finish()
        }

        return true
    }
    companion object {
        const val SCHEME = "div-action"
    }
}
