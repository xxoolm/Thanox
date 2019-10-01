package github.tornaco.android.thanos.app.donate;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;

import java.util.Objects;

public class DonateActivity extends ThemeActivity implements SwitchBar.OnSwitchChangeListener {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, DonateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_donate_activity_donate);

        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.pay_ali).setOnClickListener(v -> AliPayUtil.startPay(DonateActivity.this));

        findViewById(R.id.pay_wechat).setOnClickListener(v -> {
            ImageView imageView = new ImageView(DonateActivity.this);
            imageView.setImageResource(R.drawable.wechat_pay);
            new AlertDialog.Builder(DonateActivity.this)
                    .setView(imageView)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        // Noop.
                    })
                    .show();
        });

        SwitchBar switchBar = findViewById(R.id.switchbar);
        switchBar.setEnabled(true);
        switchBar.show();
        switchBar.setOnRes(R.string.module_donate_donated);
        switchBar.setOffRes(R.string.module_donate_donated);
        switchBar.setTextViewLabel(DonateSettings.isDonated(getApplicationContext()));
        switchBar.setChecked(DonateSettings.isDonated(getApplicationContext()));
        switchBar.addOnSwitchChangeListener(this);
    }

    @Override
    public void onSwitchChanged(SwitchCompat switchView, boolean isChecked) {
        DonateSettings.setDonated(getApplicationContext(), isChecked);
    }
}
