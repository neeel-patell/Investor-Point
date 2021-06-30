package in.weareindian.investorapp.ui.prime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrimeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PrimeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Business fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}