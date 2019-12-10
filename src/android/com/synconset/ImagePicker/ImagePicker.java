/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.synconset;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageManager;
import android.os.Build;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.content.pm.PackageInfo;

public class ImagePicker extends CordovaPlugin {
	public static String TAG = "ImagePicker";

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 
		 Context context = this.cordova.getActivity().getApplicationContext();
		
		 if( action.equals("isInstalled") ){
			 this.params = args.getJSONObject(0);
			 JSONObject json = new JSONObject();

			 if( this.params.has("package") ){
				if( isInstalled(this.params.getString("package")) ){
					json.put("installed", true);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} else {
					json.put("installed", false);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				}
				return true;
			 } else {
				json.put("errorMsg", "input is invalid - package");
				this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, json));
				return false;
			 }
		 }

		
		 if( action.equals("hasOpenableAppInDevice") ){
			 this.params = args.getJSONObject(0);
			 JSONObject json = new JSONObject();

			 if( this.params.has("url") && this.params.has("mimetype")){
				if( hasOpenableAppInDevice(this.params.getString("url")) ){
					json.put("hasApp", true);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} else {
					json.put("hasApp", false);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				}
				return true;
			 } else if( this.params.has("url") ){
				if( hasOpenableAppInDevice(this.params.getString("url")) ){
					json.put("hasApp", true);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				} else {
					json.put("hasApp", false);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				}
				return true;
			 } else { // input is error
				json.put("errorMsg", "input is invalid - need send url and mimetype");
				this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, json));
				return false;
			 }
		 }

		 if( action.equals("getOpenableAppListInDevice") ){
			 this.params = args.getJSONObject(0);

			 if( this.params.has("url") && this.params.has("mimetype")){
				ArrayList<String> appList = getOpenableAppListInDevice(this.params.getString("url"));
				JSONArray res = new JSONArray(appList);
				this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, res));
				return true;
			 }
			 
			 if(this.params.has("url")){
				ArrayList<String> appList = getOpenableAppListInDevice(this.params.getString("url"));
				JSONArray res = new JSONArray(appList);
				this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, res));
				return true;
			 } else {
				 JSONObject json = new JSONObject();
				 this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, json));
				 return false;
			 }
		 }




		 // 권한 확인
		 if( action.equals("getPermission") ){
			 // android 6.0 이상이고
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int read = context
					.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
				int write = context
					.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

				// 권한이 없는 경우
				if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
					//this.callbackContext.success("false");

					JSONObject json = new JSONObject();
					json.put("permission", false);
					this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
				}
			 }

			 // 6.0이상이지만 권한을 얻었거나, 하위 버전이라 이미 권한이 있는 ( apk설치시 미리 권한 얻음 ) 경우
			 JSONObject json = new JSONObject();
			 json.put("permission", true);
			 this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
			 return true;
		 }

		 
		 // 권한주기 창 뜨게하기
		 if( action.equals("grantPermission") ){
			 // android 6.0 이상이고
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int read = context
					.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
				int write = context
					.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

				// 권한이 없는 경우
				if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
					Intent intent = new Intent(cordova.getActivity(), GettingPermissionsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			 }

			 // 6.0이상이지만 권한을 얻었거나, 하위 버전이라 이미 권한이 있는 ( apk설치시 미리 권한 얻음 ) 경우
			 JSONObject json = new JSONObject();
			 this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
			 return true;
		 }

		 // 권한 확인이 아닌 바로 이미지 픽커를 켰을 때 ( getPictures )
		 if (action.equals("getPictures")) {
			 this.params = args.getJSONObject(0);
		 }

		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int read = context
				.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = context
				.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(cordova.getActivity(), GettingPermissionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
				if (action.equals("getPictures")) {
					Intent intent = new Intent(cordova.getActivity(), MultiImageChooserActivity.class);
					int max = 20;
					int desiredWidth = 0;
					int desiredHeight = 0;
					int quality = 100;
					if (this.params.has("maximumImagesCount")) {
						max = this.params.getInt("maximumImagesCount");
					}
					if (this.params.has("width")) {
						desiredWidth = this.params.getInt("width");
					}
					if (this.params.has("height")) {
						desiredHeight = this.params.getInt("height");
					}
					if (this.params.has("quality")) {
						quality = this.params.getInt("quality");
					}
					intent.putExtra("MAX_IMAGES", max);
					intent.putExtra("WIDTH", desiredWidth);
					intent.putExtra("HEIGHT", desiredHeight);
					intent.putExtra("QUALITY", quality);
					if (this.cordova != null) {
						this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
					}
				}
			}
        } else {
			if (action.equals("getPictures")) {
				Intent intent = new Intent(cordova.getActivity(), MultiImageChooserActivity.class);
				int max = 20;
				int desiredWidth = 0;
				int desiredHeight = 0;
				int quality = 100;
				if (this.params.has("maximumImagesCount")) {
					max = this.params.getInt("maximumImagesCount");
				}
				if (this.params.has("width")) {
					desiredWidth = this.params.getInt("width");
				}
				if (this.params.has("height")) {
					desiredHeight = this.params.getInt("height");
				}
				if (this.params.has("quality")) {
					quality = this.params.getInt("quality");
				}
				intent.putExtra("MAX_IMAGES", max);
				intent.putExtra("WIDTH", desiredWidth);
				intent.putExtra("HEIGHT", desiredHeight);
				intent.putExtra("QUALITY", quality);
				if (this.cordova != null) {
					this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
				}
			}
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
			JSONArray res = new JSONArray(fileNames);
			this.callbackContext.success(res);
		} else if (resultCode == Activity.RESULT_CANCELED && data != null) {
			String error = data.getStringExtra("ERRORMESSAGE");
			this.callbackContext.error(error);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			JSONArray res = new JSONArray();
			this.callbackContext.success(res);
		} else {
			this.callbackContext.error("No images selected");
		}
	}


	private boolean hasOpenableAppInDevice(String url) {
		Context context = this.cordova.getActivity().getApplicationContext();

		Intent intentCheck = new Intent(Intent.ACTION_VIEW);

		String mimeType = getMimeType(url);

		if (mimeType.length() > 0) {
			intentCheck.setDataAndType(Uri.parse(url), mimeType);
		}
		else {
			intentCheck.setData(Uri.parse(url));
		}

		intentCheck.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> appsList = packageManager.queryIntentActivities(intentCheck, 0);

		if (appsList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private ArrayList<String> getOpenableAppListInDevice(String url){
		Context context = this.cordova.getActivity().getApplicationContext();

		Intent intentCheck = new Intent(Intent.ACTION_VIEW);

		String mimeType = getMimeType(url);

		if (mimeType.length() > 0) {
			intentCheck.setDataAndType(Uri.parse(url), mimeType);
		} else {
			intentCheck.setData(Uri.parse(url));
		}
		intentCheck.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> appsList = packageManager.queryIntentActivities(intentCheck, 0);


		ArrayList<String> apps = new ArrayList<String>();
		for(ResolveInfo resol : appsList){
			apps.add(resol.activityInfo.packageName);
		}

		return apps;
	}

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}

		if(type == null){
			type = "*/*";
		}
		return type;
	}


	private boolean isInstalled(String packageName) {
		Context context = this.cordova.getActivity().getApplicationContext();
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
		for (PackageInfo pack : packs) {
			if(packageName.equals(pack.packageName)){
				return true;
			}
		}
		return false;
	}
}
