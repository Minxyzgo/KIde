# FileChooser
Support choose the path of a folder or file easier

Download

Use gradle:
```gradle
allprojects {
    repositories {
        google()
        jcenter()
        //Add jitpack.io
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
  implementation 'com.github.kimcy929:FileChooser:v1.0.1'
}
```
or Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.kimcy929</groupId>
    <artifactId>FileChooser</artifactId>
    <version>v1.0.1</version>
</dependency>
```
How to I use this lib?

It is very simple, you can see detail in the [sample][1]

```java
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
```

![alt text](https://i.imgur.com/De515wb.png)

## License

```
Copyright 2018 Kimcy929

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: https://github.com/kimcy929/FileChooser/blob/master/sample/src/main/java/com/example/kimcy929/filechooser/MainActivity.java
