package basisproject.lym.org.basisproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import lym.base.BaseDialog;
import lym.base.MaterialDesignDialog;
import lym.config.CommonDialogConfig;
import lym.listener.OnButtonListener;

/**
 * @author ym.li
 * @since 2019年2月24日18:16:10
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDesignDialog designDialog = new MaterialDesignDialog(MainActivity.this);
                designDialog.setContentTv("(｡･∀･)ﾉﾞ嗨，我是遵循MaterialDesign样式风格的dialog，我使用起来很方便哟，喜欢你能喜欢(≧∇≦)ﾉ");
                designDialog.setButtonClickListener(new OnButtonListener() {
                    @Override
                    public void onClick(int clickType, BaseDialog dialogInterface) {
                        if (clickType == CommonDialogConfig.CANCEL_CLICK) {
                            Toast.makeText(MainActivity.this, "我是Dialog的取消按钮事件", Toast.LENGTH_LONG).show();
                        } else if (clickType == CommonDialogConfig.SURE_CLICK) {
                            Toast.makeText(MainActivity.this, "我是Dialog的确认按钮事件", Toast.LENGTH_LONG).show();
                        }
                        dialogInterface.dismiss();
                    }
                });
                designDialog.show();
            }
        });
    }
}
