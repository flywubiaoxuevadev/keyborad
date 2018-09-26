package test.com.keybroad_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.demo.keyboard.keyboard.AlertDialogUtil;
import com.example.demo.keyboard.keyboard.OnKeyInputLinstener;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private Unbinder mUnbinder;
    @BindView(R.id.btn)
    Button btn;
    AlertDialogUtil alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        RxView.clicks(btn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                if (alertDialog == null) {
                    alertDialog = new AlertDialogUtil(MainActivity.this);
                    alertDialog.setOnKeyInputLinstener(new OnKeyInputLinstener() {
                        @Override
                        public void onKeyInput(String content) {
                            //Toast.makeText(MainActivity.this,"-->>"+content,1000).show();
                        }
                    });
                }
                alertDialog.showKeyboard();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
