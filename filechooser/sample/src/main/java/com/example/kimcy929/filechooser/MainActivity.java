package com.example.kimcy929.filechooser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kimcy929.filec.FileChooserActivity;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_DIRECTORY = 1;
    private static final int REQUEST_FILE = 2;

    private TextView txtPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPath = findViewById(R.id.txtPath);

        Button btnChooseDirectory = findViewById(R.id.btnChooseDirectory);
        btnChooseDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDirectory();
            }
        });

        Button btnChooseFile = findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
    }

    private void chooseDirectory() {
        if (checkStoragePermissions()) {
            Intent directoryIntent = new Intent(this, FileChooserActivity.class);
            directoryIntent.putExtra(FileChooserActivity.INIT_DIRECTORY_EXTRA, Environment.getExternalStorageDirectory().getPath());
            directoryIntent.putExtra(FileChooserActivity.GET_ONLY_DIRECTORY_PATH_FILE_EXTRA, false); //true if you take create only a path
            startActivityForResult(directoryIntent, REQUEST_DIRECTORY);
        }
    }

    private void chooseFile() {
        if (checkStoragePermissions()) {
            Intent fileIntent = new Intent(this, FileChooserActivity.class);
            fileIntent.putExtra(FileChooserActivity.CHOOSE_FILE_EXTRA, true);
            startActivityForResult(fileIntent, REQUEST_FILE);
        }
    }

    private boolean checkStoragePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == FileChooserActivity.RESULT_CODE_DIRECTORY_SELECTED) {
                String newPath = data.getStringExtra(FileChooserActivity.RESULT_DIRECTORY_EXTRA);
                txtPath.setText(newPath);
            }
        } else if (requestCode == REQUEST_FILE) {
            if (resultCode == FileChooserActivity.RESULT_CODE_FILE_SELECTED) {
                String newPath = data.getStringExtra(FileChooserActivity.RESULT_FILE_EXTRA);
                txtPath.setText(newPath);
            }
        }
    }
}
