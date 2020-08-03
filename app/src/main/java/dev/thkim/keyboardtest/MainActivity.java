package dev.thkim.keyboardtest;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements KeyboardHeightObserver {

    View popView;
    PopupWindow popupWindow;

    Button btnShow;
    Button btnHide;

    int keyboardHeight;
    int viewHeight = -1;

    private KeyboardHeightProvider keyboardHeightProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboardHeightProvider = new KeyboardHeightProvider(this);

        View view = findViewById(R.id.activity_main);
        view.post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });

        btnShow = findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOnKeyboard();
            }
        });

        btnHide = findViewById(R.id.btn_hide);
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideOnKeyboard();
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        keyboardHeightProvider.close();
    }

    public void showOnKeyboard() {
        popView = getLayoutInflater().inflate(R.layout.view_keyboard, null);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) popView.findViewById(R.id.layout_emoticons).getLayoutParams();
        params.height = keyboardHeight;

        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }

    public void hideOnKeyboard() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onKeyboardHeightChanged(int height) {
        this.keyboardHeight = height;
        Log.i("KeyboardTest", "onKeyboardHeightChanged this.keyboardHeight = " + this.keyboardHeight);
    }
}