package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.AwardContract;
import org.ihci.itbs.model.AwardModel;
import org.ihci.itbs.model.GlobalSettingModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Yukino Yukinoshita
 */

public class AwardPresenter implements AwardContract.Presenter {

    private WeakReference<AwardContract.View> viewWeakReference;
    private AwardModel model;

    public AwardPresenter(AwardContract.View view){
        this.viewWeakReference = new WeakReference<>(view);
        model = new AwardModel(this);
    }

    @Override
    public List<Award> listAllAward() {
        List<Award> awards = model.listAward();
        Collections.sort(awards, new Comparator<Award>() {
            @Override
            public int compare(Award o1, Award o2) {
                int typeValue = o1.getAwardType().compareTo(o2.getAwardType());
                if(typeValue!=0){
                    return typeValue;
                }
                int currency1 = o1.getAwardValue().getSeniorCurrency() * 3 + o1.getAwardValue().getJuniorCurrency();
                int currency2 = o2.getAwardValue().getSeniorCurrency() * 3 + o2.getAwardValue().getJuniorCurrency();
                return currency1>currency2?1:currency1==currency2?0:-1;
            }
        });
        return awards;
    }

    @Override
    public Award getAward(String awardName) {
        return model.getAward(awardName);
    }

    @Override
    public void userGetAward(Award award) {
        UserModel userModel = new UserModel(this);
        User user = userModel.getLocalUser(GlobalSettingModel.getInstance().getCurrentUserName());
        ArrayList<Award> awards = user.getAwardArrayList();
        awards.add(award);
        user.setAwardArrayList(awards);
        userModel.updateUser(user.getUserName(), user);
    }

    @Override
    public void notifyUpdate() {
        viewWeakReference.get().showAwardList(listAllAward());
    }
}
