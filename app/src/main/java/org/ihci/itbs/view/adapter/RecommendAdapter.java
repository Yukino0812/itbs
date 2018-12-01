package org.ihci.itbs.view.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.util.DateSelector;
import org.ihci.itbs.view.ItbsApplication;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private List<RecommendItem> recommendItemList;

    public RecommendAdapter(List<RecommendItem> items) {
        super();
        recommendItemList = items;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.ViewHolder holder, int position) {
        holder.bind(recommendItemList.get(position));
    }

    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recommend_view, parent, false);
        return new RecommendAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return recommendItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewTitle;
        TextView textViewSource;
        TextView textViewUpdateDate;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            textViewTitle = view.findViewById(R.id.textViewRecommendItemTitle);
            textViewSource = view.findViewById(R.id.textViewRecommendItemSource);
            textViewUpdateDate = view.findViewById(R.id.textViewRecommendItemUpdateTime);
        }

        void bind(RecommendItem item) {
            final String link = item.getLink();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                    ItbsApplication.getContext().startActivity(intent);
                }
            });

            textViewTitle.setText(item.getTitle());
            textViewSource.setText(item.getSource());
            textViewUpdateDate.setText(DateSelector.dateToStringWithoutTime(item.getUpdateDate()));
        }

    }

}
