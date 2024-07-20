package com.example.mytodolist.presentation.screens.about

import CustomDivActionHandler
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodolist.R
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import org.json.JSONObject
import java.io.InputStreamReader

class DivKitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_div_kit)

        val divContainer = findViewById<FrameLayout>(R.id.div_container)
        val inputStream = resources.openRawResource(R.raw.about_screen)
        val jsonString = InputStreamReader(inputStream).use { it.readText() }
        val divData = JSONObject(jsonString).asDiv2DataWithTemplates()


        val div2View = Div2View(Div2Context(
            baseContext = this,
            configuration = createDivConfiguration(),
            lifecycleOwner = this
        ))

        divContainer.addView(div2View)
        div2View.setData(divData, DivDataTag("asd"))
    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(PicassoDivImageLoader(this))
            .actionHandler(CustomDivActionHandler(this)).build()
    }

    private fun JSONObject.asDiv2DataWithTemplates(): DivData {
        val templates = getJSONObject("templates")
        val card = getJSONObject("card")
        val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
        environment.parseTemplates(templates)
        return DivData(environment, card)
    }
}