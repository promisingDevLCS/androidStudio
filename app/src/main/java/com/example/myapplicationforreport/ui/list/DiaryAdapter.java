package com.example.myapplicationforreport.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationforreport.R;
import com.example.myapplicationforreport.data.local.MoodConverter;
import com.example.myapplicationforreport.domain.model.Diary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/*
RecyclerView Adapter
- ListAdapter + DiffUtil을 사용해 전체 갱신 대신 변경된 항목만 애니메이션과 함께 업데이트
- 내용 미리 보기는 60자로 잘라서 표시
 */
public class DiaryAdapter extends ListAdapter<Diary, DiaryAdapter.DiaryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Diary diary);
    }
    public interface onItemLongClickListener {
        void onItemLongClick(Diary diary);
    }

    private final OnItemClickListener clickListener;
    private final onItemLongClickListener longClickListener;

    public DiaryAdapter(OnItemClickListener clickListener, onItemLongClickListener longClickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    // DiffUtil
    private static final DiffUtil.ItemCallback<Diary> DIFF_CALLBACK = new DiffUtil.ItemCallback<Diary>() {
        @Override
        public boolean areItemsTheSame(@NonNull Diary oldItem, @NonNull Diary newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Diary oldItem, @NonNull Diary newItem) {
            return Objects.equals(oldItem.getTitle(), newItem.getTitle())
                    && Objects.equals(oldItem.getContent(), newItem.getContent())
                    && Objects.equals(oldItem.getMood(), newItem.getMood())
                    && oldItem.getUpdatedAt() == newItem.getUpdatedAt();
        }
    };

    // ViewHolder 생성 및 바인딩
    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_diary, parent, false);

        return new DiaryViewHolder(view);
    }

    // ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        Diary diary = getItem(position);
        holder.bind(diary, clickListener, longClickListener);
    }

    // ViewHolder
    public static class DiaryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvPreview;
        private final TextView tvDate;
        private final TextView tvMood;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPreview = itemView.findViewById(R.id.tvPreview);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMood = itemView.findViewById(R.id.tvMood);
        }

        void bind(Diary diary, OnItemClickListener clickListener, onItemLongClickListener longClickListener) {
            tvTitle.setText(diary.getTitle());
            tvPreview.setText(truncate(diary.getContent(), 60));
            tvDate.setText(formatDate(diary.getCreatedAt()));
            tvMood.setText(MoodConverter.fromMood(diary.getMood()));

            itemView.setOnClickListener(v -> clickListener.onItemClick(diary));
            itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(diary);
                return true;
            });
        }

        // 내용 미리보기 60자 이상이면 잘라서 표시
        private String truncate(String text, int maxLength) {
            if (text == null) return "";
            return text.length() > maxLength
                    ? text.substring(0, maxLength) + "..."
                    : text;
        }

        private String formatDate(long timestamp) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
            return simpleDateFormat.format(new Date(timestamp));
        }
    }

}
