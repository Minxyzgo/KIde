package com.kimcy929.filec.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimcy929.filec.R;

import java.io.File;
import java.util.List;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {

    private List<File> folderList;

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindHolder(folderList.get(position));
    }

    @Override
    public int getItemCount() {
        return folderList != null ? folderList.size() : 0;
    }

    public void addFolders(List<File> data, String path) {
        folderList = data;
        notifyDataSetChanged();
        onItemClickListener.scrollToLastPosition(path);
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPath;
        AppCompatImageView imageFileIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            txtPath = itemView.findViewById(R.id.txtPath);
            imageFileIcon = itemView.findViewById(R.id.imageFileIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    onItemClickListener.directoryItemClick(folderList.get(adapterPosition), adapterPosition);
                }
            });
        }

        public void bindHolder(File file) {
            if (file.isDirectory()) {
                imageFileIcon.setImageResource(R.drawable.ic_folder_black_24dp);
            } else {
                imageFileIcon.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
            }
            txtPath.setText(file.getName());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void directoryItemClick(File file, int position);
        void scrollToLastPosition(String path);
    }
}
