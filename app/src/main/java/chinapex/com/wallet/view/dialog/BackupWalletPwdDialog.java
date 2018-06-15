package chinapex.com.wallet.view.dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IFromKeystoreToWalletCallback;
import chinapex.com.wallet.executor.runnable.FromKeystoreToWallet;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;
import chinapex.com.wallet.view.wallet.BackupWalletActivity;
import neomobile.Wallet;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class BackupWalletPwdDialog extends DialogFragment implements View.OnClickListener,
        IFromKeystoreToWalletCallback {

    private static final String TAG = BackupWalletPwdDialog.class.getSimpleName();
    private WalletBean mCurrentWalletBean;
    private Button mBt_dialog_pwd_backup_cancel;
    private Button mBt_dialog_pwd_backup_confirm;
    private EditText mEt_dialog_pwd_backup;


    public static BackupWalletPwdDialog newInstance() {
        return new BackupWalletPwdDialog();
    }

    public void setCurrentWalletBean(WalletBean currentWalletBean) {
        mCurrentWalletBean = currentWalletBean;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        // 去掉边框
        Window window = getDialog().getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new ColorDrawable(0));
        }

        // 点击空白区域不可取消
        setCancelable(false);

        return inflater.inflate(R.layout.dialog_backup_wallet_pwd, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(DensityUtil.dip2px(getActivity(), 257), DensityUtil
                .dip2px(getActivity(), 159));
    }

    private void initData() {

    }

    private void initView(View view) {
        mBt_dialog_pwd_backup_cancel = view.findViewById(R.id.bt_dialog_pwd_backup_cancel);
        mBt_dialog_pwd_backup_confirm = view.findViewById(R.id.bt_dialog_pwd_backup_confirm);
        mEt_dialog_pwd_backup = view.findViewById(R.id.et_dialog_pwd_backup);

        mBt_dialog_pwd_backup_cancel.setOnClickListener(this);
        mBt_dialog_pwd_backup_confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dialog_pwd_backup_cancel:
                dismiss();
                break;
            case R.id.bt_dialog_pwd_backup_confirm:
                String pwd = mEt_dialog_pwd_backup.getText().toString().trim();
                TaskController.getInstance().submit(new FromKeystoreToWallet(mCurrentWalletBean
                        .getKeyStore(), pwd, this));
                break;
        }
    }

    @Override
    public void fromKeystoreWallet(Wallet wallet) {
        if (null == wallet) {
            CpLog.e(TAG, "pwd is not match keystore");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "密码输入有误！", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        String mnemonicEnUs = null;
        try {
            mnemonicEnUs = wallet.mnemonic("en_US");
            CpLog.i(TAG, "mnemonicEnUs:" + mnemonicEnUs);
        } catch (Exception e) {
            CpLog.e(TAG, "mnemonicEnUs exception:" + e.getMessage());
        }

        if (TextUtils.isEmpty(mnemonicEnUs)) {
            CpLog.e(TAG, "mnemonicEnUs is null！");
            return;
        }

        Intent intent = new Intent(ApexWalletApplication.getInstance(), BackupWalletActivity.class);
        intent.putExtra(Constant.BACKUP_MNEMONIC, mnemonicEnUs);
        intent.putExtra(Constant.WALLET_BEAN, mCurrentWalletBean);
        startActivity(intent);
        dismiss();
    }

}
