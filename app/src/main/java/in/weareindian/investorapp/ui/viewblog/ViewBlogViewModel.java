package in.weareindian.investorapp.ui.viewblog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewBlogViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ViewBlogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is View Blog fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}