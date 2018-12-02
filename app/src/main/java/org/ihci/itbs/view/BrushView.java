package org.ihci.itbs.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.BrushContract;
import org.ihci.itbs.contract.UserContract;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
import org.ihci.itbs.model.pojo.Toothbrush;
import org.ihci.itbs.model.pojo.User;
import org.ihci.itbs.presenter.BrushPresenter;
import org.ihci.itbs.presenter.UserPresenter;
import org.ihci.itbs.view.adapter.ToothbrushAdapter;
import org.ihci.itbs.widget.AwardDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class BrushView extends Activity implements BrushContract.View, UserContract.View {

    private BrushContract.Presenter brushPresenter;
    private UserContract.Presenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);
        initView();
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showBrushList() {

    }

    public void onClickBackToOverview(View view) {
        this.finish();
    }

    public void onClickBackToBrushView(View view) {
        this.setContentView(R.layout.activity_brush);
        initView();
    }

    private void initView() {
        brushPresenter = new BrushPresenter(this);
        userPresenter = new UserPresenter(this);

        initToManagementView();
        initBrushAnime();
        initToothbrushId();
        initBrushButton();
    }

    private void initToManagementView() {
        findViewById(R.id.textViewToothbrushManagement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_brush_manage);
                initManagementView();
            }
        });
    }

    private void initBrushAnime() {
        final GifView brushGifView = findViewById(R.id.gifViewBrush);
        brushGifView.setGifResource(R.drawable.brush_anim);
        brushGifView.pause();
        brushGifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.buttonBrushControl).performClick();
            }
        });
    }

    private void initToothbrushId() {
        TextView textViewCurrentToothbrushId = findViewById(R.id.textViewCurrentToothbrushId);

        Toothbrush currentToothbrush = brushPresenter.getCurrentToothbrush();
        if (currentToothbrush == null) {
            User user = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
            if (user == null) {
                textViewCurrentToothbrushId.setText("请先登录");
                findViewById(R.id.buttonBrushControl).setVisibility(View.GONE);
                return;
            }
            List<Toothbrush> toothbrushes = user.getToothbrushArrayList();
            if (toothbrushes == null || toothbrushes.size() == 0) {
                textViewCurrentToothbrushId.setText("请先进入牙刷管理界面连接牙刷");
                findViewById(R.id.buttonBrushControl).setVisibility(View.GONE);
                return;
            }

            brushPresenter.connectBrush(toothbrushes.get(0).getToothbrushId());

            currentToothbrush = brushPresenter.getCurrentToothbrush();
        }

        findViewById(R.id.buttonBrushControl).setVisibility(View.VISIBLE);
        textViewCurrentToothbrushId.setText("当前牙刷编号：" + currentToothbrush.getToothbrushId());
    }

    private void initBrushButton() {
        final Button button = findViewById(R.id.buttonBrushControl);
        final GifView brushGifView = findViewById(R.id.gifViewBrush);
        if (GlobalSettingModel.getInstance().getCurrentUserName() == null
                || "".equals(GlobalSettingModel.getInstance().getCurrentUserName())
                || userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName()) == null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushPresenter.startBrush();
                    brushGifView.play();
                    button.setText("结束刷牙");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            brushPresenter.stopBrush();
                            button.setText("开始刷牙");

                            brushGifView.pause();

                            showBrushGain();
                            initBrushButton();
                        }
                    });
                }
            });
        }
    }

    private void showBrushGain() {
        Award award = brushPresenter.getAwardGainThisBrush();
        Currency currency = brushPresenter.getCurrencyGainThisBrush();
        int star = currency.getSeniorCurrency() * 3 + currency.getJuniorCurrency();
        List<Award> awardList = new ArrayList<>();
        awardList.add(award);

        AwardDialog awardDialog = new AwardDialog(this, star, awardList);
        awardDialog.create();
        awardDialog.show();
    }

    private void initManagementView() {
        initRecycler();
        initConnectToothbrush();
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewToothbrushList);

        User currentUser = userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName());
        if (currentUser == null) {
            return;
        }
        List<Toothbrush> toothbrushList = currentUser.getToothbrushArrayList();
        if (toothbrushList == null) {
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ToothbrushAdapter toothbrushAdapter = new ToothbrushAdapter(toothbrushList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(toothbrushAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initRecyclerViewItem();
            }
        }, 20);
    }

    private void initRecyclerViewItem() {
        Toothbrush currentToothbrush = brushPresenter.getCurrentToothbrush();
        if (currentToothbrush == null) {
            return;
        }
        int currentToothbrushId = currentToothbrush.getToothbrushId();

        final RecyclerView recyclerView = findViewById(R.id.recyclerViewToothbrushList);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            TextView textViewToothbrushId = layout.findViewById(R.id.textViewToothbrushId);
            ImageView imageViewMarkCurrentToothbrush = layout.findViewById(R.id.imageViewMarkCurrentToothbrush);
            Button buttonDeleteToothbrush = layout.findViewById(R.id.buttonDeleteToothbrush);

            int toothbrushId = 0;
            try {
                toothbrushId = Integer.parseInt(textViewToothbrushId.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (toothbrushId == currentToothbrushId) {
                imageViewMarkCurrentToothbrush.setVisibility(View.VISIBLE);
            } else {
                imageViewMarkCurrentToothbrush.setVisibility(View.GONE);
            }

            final int finalToothbrushId = toothbrushId;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushPresenter.connectBrush(finalToothbrushId);
                    onClickBackToBrushView(null);
                }
            });

            buttonDeleteToothbrush.setVisibility(View.GONE);
            final int finalI = i;
            buttonDeleteToothbrush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushPresenter.removeToothbrush(finalToothbrushId);
                    recyclerView.removeViewAt(finalI);
                }
            });
        }
    }

    public void onClickEditToothbrush(View view) {
        TextView textView = findViewById(R.id.textViewEditToothbrush);
        String text = textView.getText().toString();
        if ("编辑".equals(text)) {
            textView.setText("完成");
        } else {
            textView.setText("编辑");
            findViewById(R.id.imageViewBluetooth).setVisibility(View.VISIBLE);
            initManagementView();
            return;
        }

        Toothbrush currentToothbrush = brushPresenter.getCurrentToothbrush();
        if (currentToothbrush == null) {
            return;
        }
        int currentToothbrushId = currentToothbrush.getToothbrushId();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewToothbrushList);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); ++i) {
            ConstraintLayout layout = (ConstraintLayout) recyclerView.getChildAt(i);
            TextView textViewToothbrushId = layout.findViewById(R.id.textViewToothbrushId);
            ImageView imageViewMarkCurrentToothbrush = layout.findViewById(R.id.imageViewMarkCurrentToothbrush);
            Button buttonDeleteToothbrush = layout.findViewById(R.id.buttonDeleteToothbrush);

            int toothbrushId = 0;
            try {
                toothbrushId = Integer.parseInt(textViewToothbrushId.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (currentToothbrushId == toothbrushId) {
                imageViewMarkCurrentToothbrush.setVisibility(View.VISIBLE);
                buttonDeleteToothbrush.setVisibility(View.GONE);
            } else {
                imageViewMarkCurrentToothbrush.setVisibility(View.GONE);
                buttonDeleteToothbrush.setVisibility(View.VISIBLE);
            }
        }

        findViewById(R.id.imageViewBluetooth).setVisibility(View.GONE);
    }

    private void initConnectToothbrush() {
        ImageView imageViewBluetooth = findViewById(R.id.imageViewBluetooth);
        if (GlobalSettingModel.getInstance().getCurrentUserName() == null
                || "".equals(GlobalSettingModel.getInstance().getCurrentUserName())
                || userPresenter.getUser(GlobalSettingModel.getInstance().getCurrentUserName()) == null) {
            imageViewBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            imageViewBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushPresenter.connectBrush(0);
                    initManagementView();
                }
            });
        }
    }

}
