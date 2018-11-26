package org.ihci.itbs.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.ihci.itbs.R;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.util.StyleSelector;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> users) {
        super();
        userList = users;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView userName;
        Button deleteButton;

        ViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.imageViewUserAvatar);
            userName = view.findViewById(R.id.textViewUserName);
            deleteButton = view.findViewById(R.id.buttonDeleteLocalUser);
        }

        void bind(User user) {
            if (user != null && user.getAvatar() != null) {
                avatar.setImageBitmap(user.getAvatar());
            } else {
                avatar.setImageDrawable(StyleSelector.getDefaultAvatar());
            }

            if (user != null && !"".equals(user.getUserName())) {
                userName.setText(user.getUserName());
            } else {
                userName.setText("User Name");
            }

            deleteButton.setVisibility(View.GONE);
        }

    }

}
