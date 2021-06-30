package in.weareindian.investorapp.ui.defense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DefenseViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public DefenseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Business fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}