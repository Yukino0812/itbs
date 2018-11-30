package org.ihci.itbs.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Goal;
import org.ihci.itbs.view.ItbsApplication;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<Goal> goalList;

    public GoalAdapter(List<Goal> goals) {
        super();
        goalList = goals;
    }

    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.ViewHolder holder, int position) {
        holder.bind(goalList.get(position));
    }

    @NonNull
    @Override
    public GoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_goal_view, parent, false);
        return new GoalAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView award;
        TextView goalDescription;

        ViewHolder(View view) {
            super(view);
            award = view.findViewById(R.id.imageViewAward);
            goalDescription = view.findViewById(R.id.textViewGoalDescription);
        }

        void bind(Goal goal) {
            goalDescription.setHint(String.valueOf(0));
            if (goal == null) {
                goalDescription.setText("Unknown goal");
                award.setVisibility(View.GONE);
                return;
            }
            if (goal.getContent() != null) {
                goalDescription.setText(goal.getContent());
                goalDescription.setHint(String.valueOf(goal.getGoalId()));
            } else {
                goalDescription.setText("Unknown goal");
            }
            Award goalAward = goal.getAward();
            if (goalAward != null) {
                if (goalAward.getAwardName() != null) {
                    award.setImageDrawable(getDrawableAwardPicture(goalAward.getAwardName()));
                } else {
                    award.setVisibility(View.GONE);
                }
            } else {
                award.setVisibility(View.GONE);
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

    }

}
