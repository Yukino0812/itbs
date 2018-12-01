package org.ihci.itbs.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.ihci.itbs.R;
import org.ihci.itbs.contract.RecommendContract;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.presenter.RecommendPresenter;
import org.ihci.itbs.util.StyleSelector;
import org.ihci.itbs.view.adapter.RecommendAdapter;

import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendView extends Activity implements RecommendContract.View {

    private RecommendContract.Presenter recommendPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        initView();
    }

    public void onClickBack(View view) {
        this.finish();
    }

    @Override
    public void runOnViewThread(Runnable action) {
        runOnUiThread(action);
    }

    @Override
    public void showUpdating() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutRecommend);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showStopUpdate() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutRecommend);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshRecommendItem() {
        initRecyclerView();
    }

    private void initView() {
        recommendPresenter = new RecommendPresenter(this);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutRecommend);
        swipeRefreshLayout.setColorSchemeColors(StyleSelector.getColorPrimary());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recommendPresenter.updateLatestRecommendItem();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<RecommendItem> items = recommendPresenter.listRecommendItem();
        if (items == null) {
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecommendList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecommendAdapter recommendAdapter = new RecommendAdapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recommendAdapter);
    }

}
