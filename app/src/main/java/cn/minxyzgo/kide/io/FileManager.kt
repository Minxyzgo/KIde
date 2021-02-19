package cn.minxyzgo.kide.io

import android.app.Activity
import cn.minxyzgo.kide.Settings
import com.github.ahmadaghazadeh.editor.widget.CodeEditor
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File


fun readFile(path: String, act: Activity) {
    val str = File(path).readText(Settings.charset)
    val edit = act.editor as CodeEditor
    edit.setText(str, 1)
}




