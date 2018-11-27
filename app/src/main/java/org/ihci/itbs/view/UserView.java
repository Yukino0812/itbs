package org.ihci.itbs.view;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.presenter.UserPresenter;
import org.ihci.itbs.util.StyleSelector;
import org.ihci.itbs.view.adapter.UserAdapter;

import java.util.List;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

/**
 * @author Yukino Yukinoshita
 */

public class UserView extends Activity implements UserContract.View {

    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    private void initView() {
        userPresenter = new UserPresenter(this);
        initTopBar();
        initRecyclerView();
        initBottomBar();
    }

    private void initTopBar() {
        ConstraintLayout constraintLayoutTopBar = findViewById(R.id.constraintLayoutTopBar);
        constraintLayoutTopBar.setBackgroundColor(StyleSelector.getColorLight());
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUser);
        List<User> userList = userPresenter.listLocalUser();
        if (userList == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        UserAdapter userAdapter = new UserAdapter(userList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerViewItem();
            }
        }, 20);
    }

    private void initRecyclerViewItem() {
        final RecyclerView recyclerView = findViewById(R.id.recyclerViewUser);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            Button deleteButton = layout.findViewById(R.id.buttonDeleteLocalUser);
            ImageView imageViewMark = layout.findViewById(R.id.imageViewMarkCurrentUser);
            final TextView textViewUserName = layout.findViewById(R.id.textViewUserName);
            if (textViewUserName.getText().toString().equals(GlobalSettingModel.getInstance().getCurrentUserName())) {
                imageViewMark.setVisibility(View.VISIBLE);
            } else {
                imageViewMark.setVisibility(View.GONE);
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userPresenter.getUser(textViewUserName.getText().toString());
                    if (user == null
                            || user.getUserName() == null
                            || "".equals(user.getUserName())
                            || user.getUserPassword() == null) {
                        userPresenter.removeLocalUser(textViewUserName.getText().toString());
                        return;
                    }
                    if (userPresenter.login(user.getUserName(), user.getUserPassword())) {
                        backToOverviewView();
                    }
                }
            });
            final int finalI = i;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userPresenter.removeLocalUser(textViewUserName.getText().toString());
                    recyclerView.removeViewAt(finalI);
                }
            });
        }
    }

    private void initBottomBar() {
        TextView textViewAddNewUser = findViewById(R.id.textViewAddNewUser);
        GradientDrawable drawable = (GradientDrawable) textViewAddNewUser.getBackground();

        textViewAddNewUser.setTextColor(StyleSelector.getTextColor());
        drawable.setColor(StyleSelector.getColorPrimary());

        textViewAddNewUser.setBackground(drawable);
    }

    public void onClickBack(View view) {
        backToOverviewView();
    }

    public void onClickEdit(View view) {
        TextView textView = findViewById(R.id.textViewEditUser);
        String text = textView.getText().toString();
        if ("编辑".equals(text)) {
            textView.setText("完成");
        } else {
            textView.setText("编辑");
            initView();
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUser);
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            TextView textViewUserName = layout.findViewById(R.id.textViewUserName);
            if (!textViewUserName.getText().toString().equals(GlobalSettingModel.getInstance().getCurrentUserName())) {
                layout.findViewById(R.id.buttonDeleteLocalUser).setVisibility(View.VISIBLE);
            }
            layout.findViewById(R.id.imageViewMarkCurrentUser).setVisibility(View.GONE);
        }
    }

    public void onClickAddNewUser(View view) {
        setContentView(R.layout.activity_new_user);
        initNewUserView();
    }

    private void initNewUserView() {
        initRegister();
        initShowPassword();
        initButton();
    }

    private void initRegister() {
        final TextView textViewDescription = findViewById(R.id.textViewNewUserViewDescription);
        final TextView textViewNewUserRegister = findViewById(R.id.textViewNewUserRegister);
        final EditText editTextRepeatPassword = findViewById(R.id.editTextCheckPassword);
        final TextView textViewConfirm = findViewById(R.id.textViewConfirm);

        textViewNewUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("登录".equals(textViewDescription.getText().toString())) {
                    textViewDescription.setText("注册");
                    textViewNewUserRegister.setText("登录");
                    textViewConfirm.setText("注册");
                    editTextRepeatPassword.setVisibility(View.VISIBLE);
                } else {
                    textViewDescription.setText("登录");
                    textViewNewUserRegister.setText("新用户注册");
                    textViewConfirm.setText("登录");
                    editTextRepeatPassword.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initShowPassword() {
        final CheckBox checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);

        checkBoxShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextPassword = findViewById(R.id.editTextPassword);
                EditText editTextRepeatPassword = findViewById(R.id.editTextCheckPassword);
                if (checkBoxShowPassword.isChecked()) {
                    editTextPassword.setInputType(TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextRepeatPassword.setInputType(TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editTextPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                    editTextRepeatPassword.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }

    private void initButton() {
        final TextView textViewDescription = findViewById(R.id.textViewNewUserViewDescription);
        TextView textViewConfirm = findViewById(R.id.textViewConfirm);

        textViewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("登录".equals(textViewDescription.getText().toString())) {
                    EditText editTextUserName = findViewById(R.id.editTextUserName);
                    EditText editTextPassword = findViewById(R.id.editTextPassword);
                    if (userPresenter.login(editTextUserName.getText().toString(), editTextPassword.getText().toString())) {
                        onClickBackUserView(null);
                    } else {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    EditText editTextUserName = findViewById(R.id.editTextUserName);
                    EditText editTextPassword = findViewById(R.id.editTextPassword);
                    EditText editTextRepeatPassword = findViewById(R.id.editTextCheckPassword);
                    if (!editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "两次输入的密码不同", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userPresenter.register(editTextUserName.getText().toString(), editTextPassword.getText().toString())) {
                        onClickBackUserView(null);
                    } else {
                        Toast.makeText(getApplicationContext(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onClickBackUserView(View view) {
        onCreate(null);
    }

    private void backToOverviewView() {
        this.finish();
    }

}