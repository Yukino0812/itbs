package org.ihci.itbs.model;

import org.ihci.itbs.contract.RecommendContract;
import org.ihci.itbs.model.pojo.RecommendItem;
import org.ihci.itbs.model.repo.RecommendRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendModel {

    private RecommendContract.Presenter presenter;

    public RecommendModel(RecommendContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public List<RecommendItem> listRecommendItem() {
        List<RecommendItem> recommendItems = RecommendRepo.getInstance().getRecommendItemArrayList();
        if (recommendItems == null) {
            return null;
        }
        return new ArrayList<>(recommendItems);
    }

    public void updateRecommendItem(List<RecommendItem> recommendItems) {
        if (recommendItems == null) {
            return;
        }
        RecommendRepo.getInstance().setRecommendItemArrayList(new ArrayList<>(recommendItems));
    }

}
