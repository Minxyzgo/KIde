package cn.minxyzgo.kide.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.minxyzgo.kide.R

fun IntRange.toLineString(): String {
    val st = this.toList().toString()
        .replace("[", "")
        .replace("]", "")
        .replace(",", " \n")
    return " $st"
}
//
fun <T : AppCompatEditText> T.onTextChanged(run: T.() -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = run()
    })
}

class EditorFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    /*
    override fun onStart() {
        super.onStart()
        (lineView as LineView).isEnabled = false
        lineView.setOnClickListener(null)
        editTextTextMultiLine.let {
            it as SyntaxEditor
            it.onTextChanged {
                with(lineView as LineView) {
                    var editor = this@onTextChanged
                    editorLineCount = editor.lineCount
                    editorLineHeight = editor.lineHeight
                    invalidate()
                }
            }

            with(it.paint) {
                flags = Paint.UNDERLINE_TEXT_FLAG
                isAntiAlias = true
            }
        }
    }

    class LineView : android.support.v7.widget.AppCompatTextView {
        val lineTextPoint: Paint
        var editorLineCount = 0
        var editorLineHeight = 0

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.buttonStyle)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        init {
            with(Paint().also { lineTextPoint = it } ) {
                this.color = Color.GRAY;
                this.textSize = 40f
            }
        }

        override fun onDraw(canvas: Canvas) {
            val count = editorLineCount - 1
            var textLineY = 0f

            for (i in 0..count) {
                textLineY = ( (i + 1f) * editorLineHeight )  + ( editorLineHeight / 1.25f)
                canvas.drawText("${i + 1}", textScaleX, textLineY , lineTextPoint)
                canvas.save()
            }

            super.onDraw(canvas)
        }
    }

    class SyntaxEditor : AppCompatEditText {

        val linePoint: Paint

        val lineRect: Rect


        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.buttonStyle)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        init {
            with(Paint().also { linePoint = it } ) {
                color = Color.parseColor("#FFF1EDE4")
                isAntiAlias = true
                isDither = true
                strokeWidth = 3f
            }

            lineRect =  Rect()
        }

        override fun onDraw(canvas: Canvas) {
            val count = lineCount - 1
            var textLineY = 0f


            for (i in 0..count) {
                val baseline = getLineBounds(i, lineRect)
                canvas.drawLine(
                    lineRect.left.toFloat(),
                    (baseline + 1).toFloat(),
                    lineRect.right.toFloat(),
                    (baseline + 1).toFloat(),
                    linePoint
                )
            }

            super.onDraw(canvas)
        }
    }
    */
}