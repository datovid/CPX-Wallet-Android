package chinapex.com.wallet.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.model.ApexWalletDbHelper;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.SharedPreferencesUtils;
import chinapex.com.wallet.view.wallet.CreateWalletActivity;
import chinapex.com.wallet.view.wallet.ImportWalletActivity;

public class NewVisitorActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = NewVisitorActivity.class.getSimpleName();
    private Button mCreate_wallet;
    private Button mImport_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstEnter();

        // 设置透明导航键
        setNavigationBarColorTransparent();
        setContentView(R.layout.activity_new_visitor);

        initView();

    }

    private void isFirstEnter() {
        boolean isFirstEnter = (boolean) SharedPreferencesUtils.getParam(ApexWalletApplication.getInstance(), Constant
                .IS_FIRST_ENTER, true);
        if (isFirstEnter) {
            CpLog.i(TAG, "this is first enter!");
            SharedPreferencesUtils.putParam(ApexWalletApplication.getInstance(), Constant.IS_FIRST_ENTER, false);
        } else {
            startActivity(MainActivity.class, true);
        }
    }

    private void initView() {
        mCreate_wallet = (Button) findViewById(R.id.bt_new_visitor_create_wallet);
        mImport_wallet = (Button) findViewById(R.id.bt_new_visitor_import_wallet);

        mCreate_wallet.setOnClickListener(this);
        mImport_wallet.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_new_visitor_create_wallet:
                startActivity(CreateWalletActivity.class, true);
                break;
            case R.id.bt_new_visitor_import_wallet:
                startActivity(ImportWalletActivity.class, true);
                break;
        }
    }

    private void setNavigationBarColorTransparent() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

}
