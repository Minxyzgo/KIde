package com.github.ahmadaghazadeh.sample;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.github.ahmadaghazadeh.editor.widget.CodeEditor;
import com.github.ahmadaghazadeh.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CodeModel codeModel = new CodeModel("<html>111111111111111111", "html");
        ActivityMainBinding mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewDataBinding.setVariable(BR.viewModel, codeModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.editor.setOnTextChange(str -> {
            Toast.makeText(this, mViewDataBinding.editor.getText(),Toast.LENGTH_LONG).show();
        });


    }


}
