package com.tencent.shadow.test.dynamic.host;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tencent.shadow.test.lib.constant.Constant;
import com.tencent.shadow.test.lib.custom_view.TestViewConstructorCacheView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TestHostTheme);

        LinearLayout rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);

        TextView infoTextView = new TextView(this);
        infoTextView.setText(R.string.main_activity_info);
        rootView.addView(infoTextView);

        final Spinner partKeySpinner = new Spinner(this);
        ArrayAdapter<String> partKeysAdapter = new ArrayAdapter<>(this, R.layout.part_key_adapter);
        partKeysAdapter.addAll(
                Constant.PART_KEY_PLUGIN_MAIN_APP,
                Constant.PART_KEY_MULTIDEX_V1_0_2,
                Constant.PART_KEY_MULTIDEX_V2_0_1
        );
        partKeySpinner.setAdapter(partKeysAdapter);

        rootView.addView(partKeySpinner);

        Button startPluginButton = new Button(this);
        startPluginButton.setText(R.string.start_plugin);
        startPluginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partKey = (String) partKeySpinner.getSelectedItem();
                Intent intent = new Intent(MainActivity.this, PluginLoadActivity.class);
                intent.putExtra(Constant.KEY_PLUGIN_PART_KEY, partKey);
                switch (partKey) {
                    case Constant.PART_KEY_PLUGIN_MAIN_APP:
                        intent.putExtra(Constant.KEY_ACTIVITY_CLASSNAME, "com.tencent.shadow.test.plugin.general_cases.lib.gallery.splash.SplashActivity");
                        break;
                    case Constant.PART_KEY_MULTIDEX_V1_0_2:
                        intent.putExtra(Constant.KEY_ACTIVITY_CLASSNAME, "com.tencent.shadow.test.plugin.particular_cases.multidex.v1_0_2.PluginMultidexV1_0_2Activity");
                        break;
                    case Constant.PART_KEY_MULTIDEX_V2_0_1:
                        intent.putExtra(Constant.KEY_ACTIVITY_CLASSNAME, "com.tencent.shadow.test.plugin.particular_cases.multidex.v2_0_1.PluginMultidexV2_0_1Activity");
                        break;
                }
                startActivity(intent);
            }
        });
        rootView.addView(startPluginButton);

        rootView.addView(new TestViewConstructorCacheView(this));

        setContentView(rootView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        throw new RuntimeException("必须赋予权限.");
                    }
                }
            }
        }
    }

}
