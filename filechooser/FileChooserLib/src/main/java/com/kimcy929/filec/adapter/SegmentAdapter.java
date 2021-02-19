package com.kimcy929.filec.adapter;

import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimcy929.filec.R;

import java.io.File;
import java.util.List;

public class SegmentAdapter extends RecyclerView.Adapter<SegmentAdapter.ViewHolder> {
    private List<String> segmentList;

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.segment_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindHolder(segmentList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return segmentList != null ? segmentList.size() : 0;
    }

    @SuppressWarnings("unchecked")
    public void addPathSegments(List<String> newData) {
        segmentList = newData;
        notifyDataSetChanged();
        onItemClickListener.scrollToLastSegment();
    }

    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSegment;
        AppCompatImageView imageArrowIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            txtSegment = itemView.findViewById(R.id.txtSegment);
            imageArrowIcon = itemView.findViewById(R.id.imageArrowIcon);

            txtSegment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    // rebuild path
                    StringBuilder pathBuilder = new StringBuilder();
                    if (adapterPosition == 0) {
                        pathBuilder.append(File.separator);
                    } else {
                        for (int i = 1; i <= adapterPosition; i++) {
                            pathBuilder.append(File.separator).append(segmentList.get(i));
                        }
                    }
                    onItemClickListener.pathSegmentClick(pathBuilder.toString());
                }
            });
        }

        public void bindHolder(String segment, int position) {

            if (!TextUtils.equals(segment, File.separator)) {
                txtSegment.setText(segment);
            } else {
                txtSegment.setText(R.string.root_folder_name);
            }

            if (position < getItemCount() - 1) {
                imageArrowIcon.setVisibility(View.VISIBLE);
                int color = ContextCompat.getColor(txtSegment.getContext(), android.R.color.secondary_text_light_nodisable);
                txtSegment.setTextColor(color);
                imageArrowIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            } else {
                imageArrowIcon.setVisibility(View.GONE);
                int color = ContextCompat.getColor(txtSegment.getContext(), android.R.color.primary_text_light);
                txtSegment.setTextColor(color);
            }

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void pathSegmentClick(String path);
        void scrollToLastSegment();
    }
}
