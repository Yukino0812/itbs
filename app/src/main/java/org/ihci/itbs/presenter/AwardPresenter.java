package org.ihci.itbs.presenter;

import org.ihci.itbs.contract.AwardContract;
import org.ihci.itbs.model.AwardModel;
import org.ihci.itbs.model.UserModel;
import org.ihci.itbs.model.pojo.Award;
import org.ihci.itbs.model.pojo.Currency;
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

    public AwardPresenter(AwardContract.View view) {
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
                if (typeValue != 0) {
                    return typeValue;
                }
                int currency1 = o1.getAwardValue().getSeniorCurrency() * 3 + o1.getAwardValue().getJuniorCurrency();
                int currency2 = o2.getAwardValue().getSeniorCurrency() * 3 + o2.getAwardValue().getJuniorCurrency();
                return -(currency1 > currency2 ? 1 : currency1 == currency2 ? 0 : -1);
            }
        });
        return awards;
    }

    @Override
    public Award getAward(String awardName) {
        return model.getAward(awardName);
    }

    @Override
    public boolean buyAward(User user, String awardName) {
        if (user == null || awardName == null || "".equals(awardName)) {
            return false;
        }
        UserModel userModel = new UserModel(this);
        User currentUser = userModel.getUser(user.getUserName());
        if (currentUser == null || currentUser.getCurrency() == null) {
            return false;
        }
        int userCurrency = user.getCurrency().getSeniorCurrency() * 3 + user.getCurrency().getJuniorCurrency();

        Award award = model.getAward(awardName);
        if (award == null || award.getAwardValue() == null) {
            return false;
        }
        int awardVal = award.getAwardValue().getSeniorCurrency() * 3 + award.getAwardValue().getJuniorCurrency();

        if (userCurrency < awardVal) {
            return false;
        }

        List<Award> userOldAward = user.getAwardArrayList();
        ArrayList<Award> userAward;
        if (userOldAward == null) {
            userAward = new ArrayList<>();
        } else {
            userAward = new ArrayList<>(userOldAward);
        }
        userAward.add(award);

        userCurrency -= awardVal;
        Currency currency = new Currency();
        currency.setSeniorCurrency(userCurrency / 3);
        currency.setJuniorCurrency(userCurrency % 3);

        currentUser.setCurrency(currency);
        currentUser.setAwardArrayList(userAward);

        userModel.updateUser(currentUser.getUserName(), currentUser);

        return true;
    }

    /**
     * Do not use {@link AwardPresenter#listAllAward()} here before change the
     * code of {@link org.ihci.itbs.model.repo.AwardLocalRepo#realSync(List)}.
     * <p>
     * Because {@link AwardPresenter#listAllAward()} will call
     * {@link AwardModel#listAward()}, and cause a {@code AwardLocalRepo}
     * update, then notify presenter to update, will make a loop.
     */
    @Override
    public void notifyUpdate() {
        viewWeakReference.get().runOnViewThread(new Runnable() {
            @Override
            public void run() {
                // update by user
            }
        });
    }
}
