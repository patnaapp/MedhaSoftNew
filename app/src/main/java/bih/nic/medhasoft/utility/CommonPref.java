package bih.nic.medhasoft.utility;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.Get_usersalldetails;
import bih.nic.medhasoft.entity.UserDetails;


public class CommonPref {

	static Context context;

	CommonPref() {

	}

	CommonPref(Context context) {
		CommonPref.context = context;
	}



	public static void setUserDetails(Context context, BlkUserDetails userInfo) {

		String key = "_USER_DETAILS";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();

		editor.putString("Password", userInfo.get_Password());
		//editor.putString("DistrictCode", userInfo.getDistrictCode());
		//editor.putString("DistrictName", userInfo.getDistrictName());
		//editor.putString("DOB", userInfo.getd());

		editor.putString("UserID", userInfo.get_UserID());
		editor.putString("Role", userInfo.get_UserRole());

		editor.putString("UserName", userInfo.get_UserName());

		//editor.putString("Qualification", userInfo.getQualification());
		editor.putString("DistCode", userInfo.get_DistrictCode());
		editor.putString("DistName", userInfo.get_DistrictName());
		editor.putString("Blkcode", userInfo.get_BlockCode());
		editor.putString("BlkName", userInfo.get_BlockName());
//		editor.putString("Address", userInfo.getAddress());
//		editor.putString("Email", userInfo.getEmail());
//		editor.putString("LastVisitedOn", userInfo.getLastVisitedOn());
		//editor.putString("FatherName", userInfo.getFatherName());
		//editor.putString("BlockCode", userInfo.getBlockCode());
		//editor.putString("BlockName", userInfo.getBlockName());



		editor.commit();

	}

	public static BlkUserDetails getUserDetails(Context context) {

		String key = "_USER_DETAILS";
		BlkUserDetails userInfo = new BlkUserDetails();
		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		userInfo.set_UserRole(prefs.getString("Role", ""));
		userInfo.set_Password(prefs.getString("Password", ""));


		userInfo.set_UserID(prefs.getString("UserID", ""));

		userInfo.set_UserName(prefs.getString("UserName", ""));
		//userInfo.setQualification(prefs.getString("Qualification", ""));

		userInfo.set_DistrictCode(prefs.getString("DistCode", ""));
		userInfo.set_DistrictName(prefs.getString("DistName", ""));
		userInfo.set_BlockCode(prefs.getString("Blkcode", ""));
		userInfo.set_BlockName(prefs.getString("BlkName", ""));

		//userInfo.setFatherName(prefs.getString("FatherName", ""));
		//userInfo.setBlockCode(prefs.getString("BlockCode", ""));
		//userInfo.setBlockName(prefs.getString("BlockName", ""));

		return userInfo;
	}



	public static void setCheckUpdate(Context context, long dateTime) {

		String key = "_CheckUpdate";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		Editor editor = prefs.edit();


		dateTime=dateTime+1*3600000;
		editor.putLong("LastVisitedDate", dateTime);

		editor.commit();

	}

	public static int getCheckUpdate(Context context) {

		String key = "_CheckUpdate";

		SharedPreferences prefs = context.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		long a = prefs.getLong("LastVisitedDate", 0);


		if(System.currentTimeMillis()>a)
			return 1;
		else
			return 0;
	}

	public static void setAwcId(Activity activity, String awcid){
		String key = "_Awcid";
		SharedPreferences prefs = activity.getSharedPreferences(key,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("code2", awcid);
		editor.commit();
	}


}
