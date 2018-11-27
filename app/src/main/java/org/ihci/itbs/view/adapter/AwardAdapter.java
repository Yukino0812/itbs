package org.ihci.itbs.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.view.ItbsApplication;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder> {

    private List<Award> awardList;

    public AwardAdapter(List<Award> awards) {
        super();
        awardList = awards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_award_view, parent, false);
        return new AwardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(awardList.get(position));
    }

    @Override
    public int getItemCount() {
        return awardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView awardPicture;
        TextView awardName;
        TextView textViewSeniorCurrency;
        TextView textViewJuniorCurrency;
        Button buyButton;

        ViewHolder(View view) {
            super(view);
            awardPicture = view.findViewById(R.id.imageViewAwardPicture);
            awardName = view.findViewById(R.id.textViewAwardName);
            textViewSeniorCurrency = view.findViewById(R.id.textViewSeniorCurrency);
            textViewJuniorCurrency = view.findViewById(R.id.textViewJuniorCurrency);
            buyButton = view.findViewById(R.id.buttonBuyAward);
        }

        void bind(Award award) {
            if (award.getAwardName() != null && !"".equals(award.getAwardName())) {
                awardName.setText(getStringAwardName(award.getAwardName()));
                awardName.setHint(award.getAwardName());
                awardPicture.setImageDrawable(getDrawableAwardPicture(award.getAwardName()));

                Currency currency = award.getAwardValue();
                if (currency == null) {
                    textViewSeniorCurrency.setText(String.valueOf(0));
                    textViewJuniorCurrency.setText(String.valueOf(0));
                } else {
                    textViewSeniorCurrency.setText(String.valueOf(currency.getSeniorCurrency()));
                    textViewJuniorCurrency.setText(String.valueOf(currency.getJuniorCurrency()));
                }
            } else {
                awardName.setText("Unknown Award");
                awardName.setHint("Unknown Award");
                textViewSeniorCurrency.setText(String.valueOf(0));
                textViewJuniorCurrency.setText(String.valueOf(0));
                buyButton.setVisibility(View.GONE);
            }
        }

        private Drawable getDrawableAwardPicture(String awardName) {
            try {
                R.drawable instance = new R.drawable();
                Field field = instance.getClass().getField("award_" + awardName);
                return ItbsApplication.getContext().getResources().getDrawable(field.getInt(instance));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return ItbsApplication.getContext().getDrawable(R.drawable.award_default);
            }
        }

        private String getStringAwardName(String awardName){
            try {
                R.string instance = new R.string();
                Field field = instance.getClass().getField(awardName);
                return ItbsApplication.getContext().getResources().getString(field.getInt(instance));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return awardName;
            }
        }

    }

}
