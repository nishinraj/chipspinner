package raj.nishin.chipspinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_wolf_chip.view.*
import kotlinx.android.synthetic.main.wolf_chip_spinner.view.*

/**
 * Created by WOLF
 * at 11:28 on Wednesday 27 March 2019
 */


class WolfChipSpinner : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    var colorTheme: String = ""
        set(value) {
            field = value
            spinnerAdapter.colorTheme = value.toCharArray()
        }
    var selectedPosition: Int = 0
        set(value) {
            field = value
            spinnerAdapter.selectedPosition = value
        }
    var onItemSelected: ((position: Int) -> Unit)? = null
    var title = ""
        set(value) {
            field = value
            tvTitle.text = value
        }

    var titleVisible = false
        set(value) {
            field = value
            tvTitle.visibility = if (value) View.VISIBLE else View.GONE
            setListPadding()
        }


    var dividerVisibility = both
        set(value) {
            field = value
            when (value) {
                WolfChipSpinner.none -> {
                    dividerTop.visibility = View.GONE
                    dividerBottom.visibility = View.GONE
                }
                WolfChipSpinner.top -> {
                    dividerTop.visibility = View.VISIBLE
                    dividerBottom.visibility = View.GONE
                }
                WolfChipSpinner.bottom -> {
                    dividerTop.visibility = View.GONE
                    dividerBottom.visibility = View.VISIBLE
                }
                else -> {
                    dividerTop.visibility = View.VISIBLE
                    dividerBottom.visibility = View.VISIBLE
                }
            }
        }
    private var previousPositon = 0
    var dataSet = ArrayList<String>()
        set(value) {
            field = value
            spinnerAdapter.reset(value)
        }
    val spinnerAdapter = ChipSpinnerAdapter()
    fun undoSelection() {
        selectedPosition = previousPositon
    }

    private fun init(ctx: Context, attrs: AttributeSet? = null) {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.wolf_chip_spinner,
            this
        )
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.WolfChipSpinner)
            title = ta.getString(R.styleable.WolfChipSpinner_wcsTitle) ?: ""
            titleVisible =
                ta.getBoolean(R.styleable.WolfChipSpinner_wcsShowTitle, title.isNotEmpty()) && title.isNotEmpty()
            with(ta.getResourceId(R.styleable.WolfChipSpinner_wcsTextColor, -1)) {
                if (-1 != this) {
                    tvTitle.setTextColor(ContextCompat.getColor(ctx, this))
                }
            }
            dividerVisibility = ta.getInt(R.styleable.WolfChipSpinner_wcsDividers, WolfChipSpinner.none)
            spinnerAdapter.colorTheme =
                (ta.getString(R.styleable.WolfChipSpinner_wcsColorTheme) ?: "").toLowerCase().toCharArray()
            ta.recycle()
        }
        setListPadding()
        rvItems.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        rvItems.adapter = spinnerAdapter
        rvItems.isNestedScrollingEnabled = false
    }

    private fun setListPadding() {
        if (titleVisible) {
            rvItems.setPadding(8, 0, 16, 0)
        } else {
            rvItems.setPadding(16, 0, 16, 0)
        }
    }

    inner class ChipSpinnerAdapter(
        val dataSet: ArrayList<String> = ArrayList()

    ) :
        RecyclerView.Adapter<ChipSpinnerAdapter.VH>() {
        private val themeLetters = listOf('g', 'r', 'b', 'y', 'v')
        private var useCustomColorTheme = false
        private var userTheme = "".toCharArray()
        var colorTheme: CharArray = CharArray(0)
            set(value) {
                field = value
                setCustomColorTheme()
            }
        var selectedPosition = 0
            set(value) {
                previousPositon = field
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_wolf_chip, parent, false))

        override fun getItemCount() = dataSet.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            with(holder.itemView) {
                rootView.setOnClickListener {
                    onItemSelected?.invoke(position)
                    this@WolfChipSpinner.selectedPosition = position
                }
                tvChip.setOnClickListener {
                    onItemSelected?.invoke(position)
                    this@WolfChipSpinner.selectedPosition = position
                }
                if (useCustomColorTheme) {
                    tvChip.setBackgroundResource(getBackground(userTheme[position]))
                }
                tvChip.text = dataSet[position]
                isSelected = position == selectedPosition
            }
        }

        inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

        private fun getBackground(letter: Char): Int {
            return when (letter) {
                'g' -> {
                    R.drawable.chip_background_green
                }
                'r' -> {
                    R.drawable.chip_background_red
                }
                'b' -> {
                    R.drawable.chip_background_blue
                }
                'y' -> {
                    R.drawable.chip_background_yellow
                }
                'v' -> {
                    R.drawable.chip_background_violet
                }
                else -> {
                    R.drawable.chip_background_default
                }
            }
        }

        fun reset(value: ArrayList<String>) {
            dataSet.removeAll(dataSet)
            dataSet.addAll(value)
            setCustomColorTheme()
            notifyDataSetChanged()
        }

        private fun setCustomColorTheme() {
            if (colorTheme.isNotEmpty() && colorTheme.size == dataSet.size) {
                useCustomColorTheme = true
                colorTheme.forEach {
                    if (it !in themeLetters) {
                        useCustomColorTheme = false
                    }
                }
                if (useCustomColorTheme) {
                    userTheme = colorTheme
                }
            }
        }
    }

    companion object {
        const val none = 0
        const val top = 1
        const val bottom = 2
        const val both = 3
    }
}

infix fun WolfChipSpinner.onItemSelected(callBack: ((position: Int) -> Unit)) {
    onItemSelected = callBack
}

infix fun WolfChipSpinner.dividers(divider: Int) {
    dividerVisibility = divider
}
