package raj.nishin.chipspinnerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import raj.nishin.chipspinner.onItemSelected

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        with(ArrayList<String>().apply {
            add("Action")
            add("Animation")
            add("Comedy")
            add("Drama")
            add("Fantasy")
            add("Action")
            add("Animation")
            add("Comedy")
            add("Drama")
            add("Fantasy")
            add("Action")
            add("Animation")
            add("Comedy")
            add("Drama")
            add("Fantasy")

        }) {
            wcsClass.dataSet = this
            wcsClass.onItemSelected {
                tvSelected.text = "Selected:${get(it)}, ${wcsClass.selectedItems}"
                wcsClass.undoSelection()
            }
        }
//        wcsClass.colorTheme = "rgbyv"
        tvSelected.setOnClickListener {
            wcsClass.unselectAll()
        }
    }


}
