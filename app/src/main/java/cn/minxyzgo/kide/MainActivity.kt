package cn.minxyzgo.kide

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.minxyzgo.kide.io.readFile
import com.github.ahmadaghazadeh.editor.widget.CodeEditor
import com.kimcy929.filec.FileChooserActivity
import com.kide.app.TermuxActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        const val REQUEST_DIRECTORY = 1
        const val REQUEST_FILE = 2
        const val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!checkStoragePermissions()) ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            with(File("/data/data/com.minxyzgo.kide/files/usr-staging/bin")) {
                if(!isDirectory) mkdirs()
            }
            startActivity(Intent(this, TermuxActivity::class.java))
            /*setContentView(R.layout.terminal_layout)
            var v: TerminalView = findViewById(R.id.terminalView)
            v.attachSession(TerminalSession(
                "123",
                "123",
                arrayOf("123"),
                arrayOf("131"),
                object : TerminalSession.SessionChangedCallback {
                    override fun onColorsChanged(session: TerminalSession?) {

                    }

                    override fun onBell(session: TerminalSession?) {

                    }

                    override fun onClipboardText(session: TerminalSession?, text: String?) {

                    }

                    override fun onTitleChanged(changedSession: TerminalSession?) {

                    }

                    override fun onSessionFinished(finishedSession: TerminalSession?) {

                    }

                    override fun onTextChanged(changedSession: TerminalSession?) {

                    }

                }).apply {
                updateSize(90, 10)
                emulator.paste("help")
            })
            v.setTextSize(15)

            v.setOnKeyListener(
                object : TerminalViewClient{
                    override fun readControlKey(): Boolean {
                        return true
                    }

                    override fun copyModeChanged(copyMode: Boolean) {
                    }

                    override fun shouldBackButtonBeMappedToEscape(): Boolean {
                        return true
                    }

                    override fun onSingleTapUp(e: MotionEvent?) {

                    }

                    override fun onKeyDown(
                        keyCode: Int,
                        e: KeyEvent?,
                        session: TerminalSession?
                    ): Boolean {
                        return true
                    }

                    override fun onScale(scale: Float): Float {
                        return 99f
                    }

                    override fun readAltKey(): Boolean {
                        return true
                    }

                    override fun onCodePoint(
                        codePoint: Int,
                        ctrlDown: Boolean,
                        session: TerminalSession?
                    ): Boolean {
                        return true
                    }

                    override fun onKeyUp(keyCode: Int, e: KeyEvent?): Boolean {
                        return true
                    }

                    override fun onLongPress(event: MotionEvent?): Boolean {
                        return true
                    }
                }
            )*/
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.openFile -> chooseFile()
            R.id.saveFile -> createFile()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun chooseDirectory() {
        //if (checkStoragePermissions()) {
            val directoryIntent = Intent(
                this,
                FileChooserActivity::class.java
            )
            directoryIntent.putExtra(
                FileChooserActivity.INIT_DIRECTORY_EXTRA,
                Environment.getExternalStorageDirectory().path
            )
            directoryIntent.putExtra(
                FileChooserActivity.GET_ONLY_DIRECTORY_PATH_FILE_EXTRA,
                false
            ) //true if you take create only a path
            startActivityForResult(
                directoryIntent,
                REQUEST_DIRECTORY
            )
        //}
    }


    private fun chooseFile() {
        if (checkStoragePermissions()) {
            val fileIntent =
                Intent(this, FileChooserActivity::class.java)
            fileIntent.putExtra("CHOOSE_FILE_EXTRA", true)
            startActivityForResult(
                fileIntent,
                REQUEST_FILE
            )
        }
    }

    private fun createFile() {
        if (checkStoragePermissions()) {
            val fileIntent =
                Intent(this, FileChooserActivity::class.java)
            fileIntent.putExtra(FileChooserActivity.SAVE_FILE_EXTRA, true)
            fileIntent.putExtra(FileChooserActivity.SAVE_FILE_MSG_EXTRA,
                (editor as CodeEditor).text.toString())
            fileIntent.putExtra(FileChooserActivity.SAVE_FILE_CHARSET_EXTRA, Settings.charset.name())
            startActivityForResult(
                fileIntent,
                REQUEST_FILE
            )
        }
    }

    private fun checkStoragePermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 2
            )
            return false
        }
        return true
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == FileChooserActivity.RESULT_CODE_DIRECTORY_SELECTED) {
                val newPath =
                    data?.getStringExtra(FileChooserActivity.RESULT_DIRECTORY_EXTRA)
                //txtPath!!.text = newPath
            }
        } else if (requestCode == REQUEST_FILE) {
            if (resultCode == FileChooserActivity.RESULT_CODE_FILE_SELECTED) {
                val newPath =
                    data?.getStringExtra(FileChooserActivity.RESULT_FILE_EXTRA)
                newPath?.run { readFile(this, this@MainActivity) }
            }
        }
    }
}