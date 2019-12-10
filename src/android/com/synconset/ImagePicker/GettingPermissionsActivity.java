package com.synconset;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class GettingPermissionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int read = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
			int write = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(
					new String[]{
						android.Manifest.permission.READ_EXTERNAL_STORAGE,
						android.Manifest.permission.WRITE_EXTERNAL_STORAGE
					},
					0
				);
			} else {
				GettingPermissionsActivity.this.finish();
			}
		} else {
			GettingPermissionsActivity.this.finish();
		}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                GettingPermissionsActivity.this,
                "권한이 승인되었습니다. 메뉴를 다시 클릭해 이미지를 선택하세요.",
                Toast.LENGTH_SHORT
            ).show();
            GettingPermissionsActivity.this.finish();
		} else {
			Toast.makeText(
                GettingPermissionsActivity.this,
                "스토리지 권한이 거부되었습니다.",
                Toast.LENGTH_SHORT
            ).show();
            GettingPermissionsActivity.this.finish();
        }
    }
}
