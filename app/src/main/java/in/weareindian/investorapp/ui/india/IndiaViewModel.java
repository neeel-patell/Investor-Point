package in.weareindian.investorapp.ui.india;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IndiaViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public IndiaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is India fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}