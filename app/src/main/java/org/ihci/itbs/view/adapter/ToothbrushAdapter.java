package org.ihci.itbs.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.Toothbrush;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class ToothbrushAdapter extends RecyclerView.Adapter<ToothbrushAdapter.ViewHolder> {

    private List<Toothbrush> toothbrushList;

    public ToothbrushAdapter(List<Toothbrush> toothbrushes) {
        super();
        toothbrushList = toothbrushes;
    }

    @Override
    public void onBindViewHolder(@NonNull ToothbrushAdapter.ViewHolder holder, int position) {
        holder.bind(toothbrushList.get(position));
    }

    @NonNull
    @Override
    public ToothbrushAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_toothbrush_view, parent, false);
        return new ToothbrushAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return toothbrushList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewToothbrushId;

        ViewHolder(View view) {
            super(view);
            textViewToothbrushId = view.findViewById(R.id.textViewToothbrushId);
        }

        void bind(Toothbrush toothbrush) {
            if (toothbrush != null && toothbrush.getToothbrushId() != 0) {
                textViewToothbrushId.setText(String.valueOf(toothbrush.getToothbrushId()));
            } else {
                textViewToothbrushId.setText("未知牙刷");
            }
        }

    }

}
