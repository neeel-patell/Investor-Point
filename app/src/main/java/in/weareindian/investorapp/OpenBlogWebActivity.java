package in.weareindian.investorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import in.weareindian.investorapp.R;

public class OpenBlogWebActivity extends AppCompatActivity {

    String blodID;
    String htmlCode;
    HtmlTextView htmlTextView;
    Button btnViewBlogShare;
    String ramdomCharater, blogShareUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_blog_web);

        Init();
        SetHtmlView();

        btnViewBlogShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateRandomCharacter(1);

                blogShareUrl = "https://myquiknews.com/quiknews/user/blog/read.php?details=blog_"+blodID+ramdomCharater;

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = blogShareUrl;
                String shareSub = "Blog";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });
    }

    private void SetHtmlView() {
        htmlTextView.setHtml(htmlCode, new HtmlHttpImageGetter(htmlTextView));
    }

    private void Init() {
        blodID = getIntent().getStringExtra("id");
        htmlCode = getIntent().getStringExtra("htmlCode");
        btnViewBlogShare = findViewById(R.id.btnViewBlogShare);
        htmlTextView = (HtmlTextView) findViewById(R.id.html_text_Blog);
    }

    private void generateRandomCharacter(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        ramdomCharater = sb.toString();

    }
}