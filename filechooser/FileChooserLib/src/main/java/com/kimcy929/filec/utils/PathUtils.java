package com.kimcy929.filec.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kimcy929 on 13/03/2018.
 */


@SuppressWarnings("WeakerAccess")
public class PathUtils {

    private static final String PATH_TREE = "tree";
    private static final String PRIMARY_TYPE = "primary";
    private static final String RAW_TYPE = "raw";
    private static final String[] projectName = {OpenableColumns.DISPLAY_NAME};

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePathFromDocumentProviderUri(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                //Timber.d("docId -> %s", docId);
                final String[] split = docId.split(":");
                final String type = split[0];

                if (PRIMARY_TYPE.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    // TODO handle non-primary volumes
                    StringBuilder path = new StringBuilder();
                    String pathSegment[] = docId.split(":");
                    return path.append(getRemovableStorageRootPath(context, pathSegment[0])).append(File.separator).append(pathSegment[1]).toString();
                }
            } else if (isDownloadsDocument(uri)) {  // DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);

                //Timber.d("download id -> %s", id);

                if (id.contains("raw:")) {
                    return id.substring(id.indexOf(File.separator));
                } else {
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }

            } else if (isMediaDocument(uri)) {  // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     * @return file path of Uri
     */
    public static String getDirectoryPathFromUri(Context context, Uri uri) {

        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }

        if (isTreeUri(uri)) {

            String treeId = getTreeDocumentId(uri);

            if (treeId != null) {

                String[] paths = treeId.split(":");
                String type = paths[0];
                String subPath = paths.length == 2 ? paths[1] : "";

                if (RAW_TYPE.equalsIgnoreCase(type)) {
                    return treeId.substring(treeId.indexOf(File.separator));
                } else if (PRIMARY_TYPE.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + subPath;
                } else {
                    StringBuilder path = new StringBuilder();
                    String[] pathSegment = treeId.split(":");
                    if (pathSegment.length == 1) {
                        path.append(getRemovableStorageRootPath(context, paths[0]));
                    } else {
                        String rootPath = getRemovableStorageRootPath(context, paths[0]);
                        path.append(rootPath).append(File.separator).append(pathSegment[1]);
                    }

                    return path.toString();
                }
            }
        }
        return null;
    }

    private static String getRemovableStorageRootPath(Context context, String storageId) {
        StringBuilder rootPath = new StringBuilder();
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
        for (File fileDir : externalFilesDirs) {
            if (fileDir.getPath().contains(storageId)) {
                String[] pathSegment = fileDir.getPath().split(File.separator);
                for (String segment : pathSegment) {
                    if (segment.equals(storageId)) {
                        rootPath.append(storageId);
                        break;
                    }
                    rootPath.append(segment).append(File.separator);
                }
                //rootPath.append(fileDir.getPath().split("/Android")[0]); // faster
                break;
            }
        }
        return rootPath.toString();
    }

    public static List<File> getListRemovableStorage(Context context) {

        List<File> paths = new ArrayList<>();

        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
        for (File fileDir : externalFilesDirs) {
            if (fileDir != null) {
                if (isExternalStorageRemovable(fileDir)) {
                    String path = fileDir.getPath();
                    if (path.contains("/Android")) {
                        paths.add(new File(path.substring(0, path.indexOf("/Android"))));
                    }
                }
            }
        }

        return paths;
    }

    private static boolean isExternalStorageRemovable(File fileDir) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                return Environment.isExternalStorageRemovable(fileDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hasRemoveStorage(fileDir);
    }

    private static boolean hasRemoveStorage(File fileDir) {
        String path = fileDir.getPath();
        String[] segments = path.split(File.separator);
        return segments.length > 2 && segments[2].matches("^\\w{4}-\\w{4}$");
    }

    public static String getFileName(Uri uri) {
        List<String> segments = uri.getPathSegments();
        return (segments.isEmpty()) ? "" : segments.get(segments.size() - 1);
    }

    public static String getDisplayName(Context context, Uri uri) {
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, projectName, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameColumn = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                String name = cursor.getString(nameColumn);

                if (TextUtils.isEmpty(name.trim())) {
                    return getFileName(uri);
                } else {
                    return name;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return getFileName(uri);
    }

    /**
     * https://github.com/rcketscientist/DocumentActivity/blob/master/library/src/main/java/com/anthonymandra/framework/DocumentUtil.java#L56
     *
     * Extract the via {@link DocumentsContract.Document#COLUMN_DOCUMENT_ID} from the given URI.
     * From {@link DocumentsContract} but return null instead of throw
     */

    public static String getTreeDocumentId(Uri uri) {
        final List<String> paths = uri.getPathSegments();
        if (paths.size() >= 2 && PATH_TREE.equals(paths.get(0))) {
            return paths.get(1);
        }
        return null;
    }

    public static boolean isTreeUri(Uri uri) {
        final List<String> paths = uri.getPathSegments();
        return (paths.size() == 2 && PATH_TREE.equals(paths.get(0)));
    }
}
