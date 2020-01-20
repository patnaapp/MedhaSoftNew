package bih.nic.medhasoft.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import bih.nic.medhasoft.EditBendDetailsActivity;
import bih.nic.medhasoft.Model.studentList;
import bih.nic.medhasoft.PREHomeActivity;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.ViewEditListActivity;
import bih.nic.medhasoft.ViewEditUpdatedListActivity;
import bih.nic.medhasoft.database.DataBaseHelper;
import bih.nic.medhasoft.database.WebServiceHelper;
import bih.nic.medhasoft.entity.MARKATTPERRESULT;
import bih.nic.medhasoft.utility.MarshmallowPermission;
import bih.nic.medhasoft.utility.ShorCutICON;
import bih.nic.medhasoft.utility.Utiilties;


public class VIewEditListAdapter extends BaseAdapter{

	private Context mContext;
	String version="";
	private LayoutInflater mLayoutInflater;
	private ArrayList<studentList> mEntries = new ArrayList<>();
	String _WardCode,_WardName,_FYearCode,_SchemeName,_SchemeCode;
	DataBaseHelper helper;
	ShorCutICON ico;
	String showWhat;
	public VIewEditListAdapter(Context context,String sWhat) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		helper=new DataBaseHelper(mContext);
		ico=new ShorCutICON(mContext);
		showWhat=sWhat;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEntries.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mEntries.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = (RelativeLayout) mLayoutInflater.inflate(R.layout.grid_view_edit_details_item,	null);

		//txt_ACNum, txt_IFSC, txt_AdmDt
		TextView txtfn = (TextView) vi.findViewById(R.id.textView_fn);
		TextView txtmn = (TextView) vi.findViewById(R.id.textView_mn);
		TextView txtdob = (TextView) vi.findViewById(R.id.textView_dob);
		TextView txtname = (TextView) vi.findViewById(R.id.textView_rollno);
		TextView txtadnum = (TextView) vi.findViewById(R.id.textView);

		TextView txt_ACNum = (TextView) vi.findViewById(R.id.txt_ACNum);
		TextView txt_IFSC = (TextView) vi.findViewById(R.id.txt_IFSC);
		TextView txt_AdmDt = (TextView) vi.findViewById(R.id.txt_AdmDt);
		TextView txt_Mob = (TextView) vi.findViewById(R.id.txt_Mob);


		TextView txt_Gen = (TextView) vi.findViewById(R.id.txt_Gen);
		TextView txt_Ward = (TextView) vi.findViewById(R.id.txt_Ward);
		TextView txt_Cat = (TextView) vi.findViewById(R.id.txt_Cat);
		TextView txt_Minority = (TextView) vi.findViewById(R.id.txt_Minority);
		TextView txt_Handi = (TextView) vi.findViewById(R.id.txt_Handi);
		TextView txt_AdNum = (TextView) vi.findViewById(R.id.txt_AdNum);
		TextView txt_AdName = (TextView) vi.findViewById(R.id.txt_AdName);
		TextView txt_reason = (TextView) vi.findViewById(R.id.txt_reason);


		ImageView bedit = (ImageView) vi.findViewById(R.id.btnedit);
		ImageView bupload = (ImageView) vi.findViewById(R.id.btnupload);
		ImageView beditleft = (ImageView) vi.findViewById(R.id.btneditleft);
		ImageView buploadleft = (ImageView) vi.findViewById(R.id.btnuploadleft);

		if(showWhat.equalsIgnoreCase("E"))
		{
			bupload.setVisibility(View.GONE);
			buploadleft.setVisibility(View.GONE);
		}

		final LinearLayout allbtns =(LinearLayout) vi.findViewById(R.id.btnList);
		final LinearLayout allbtnsleft =(LinearLayout) vi.findViewById(R.id.btnListLeft);
		final RelativeLayout allinfotxt =(RelativeLayout) vi.findViewById(R.id.relallinfo);

//		allinfotxt.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (allbtns.getVisibility() == View.VISIBLE) {
//					allbtns.setVisibility(View.INVISIBLE);
//					allbtnsleft.setVisibility(View.INVISIBLE);
//				} else {
//					allbtns.setVisibility(View.VISIBLE);
//					allbtnsleft.setVisibility(View.VISIBLE);
//				}
//			}
//		});
		bedit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent iListPendingUpload = new Intent(mContext, EditBendDetailsActivity.class);
				iListPendingUpload.putExtra("EDIT", "EDIT");
				iListPendingUpload.putExtra("BENID", mEntries.get(position).getStdbenid());
				mContext.startActivity(iListPendingUpload);

			}

		});
		beditleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent iListPendingUpload = new Intent(mContext, EditBendDetailsActivity.class);
				iListPendingUpload.putExtra("EDIT", "EDIT");
				iListPendingUpload.putExtra("BENID", mEntries.get(position).getStdbenid());
				mContext.startActivity(iListPendingUpload);

			}

		});


		bupload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowAlertMSG( mEntries.get(position).getStdbenid());

			}
		});
		buploadleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowAlertMSG( mEntries.get(position).getStdbenid());

			}
		});

		String  _lang= PreferenceManager.getDefaultSharedPreferences(mContext).getString("LANG", "");



		String fn=mEntries.get(position).getStdfname();
		if(fn!=null)
		{
			fn=!mEntries.get(position).getStdfname().contains("anyType{}") ? mEntries.get(position).getStdfname() : "";
		}
		txtfn.setText(fn);


		String mn=mEntries.get(position).getStdmname();
		if(mn!=null)
		{
			mn=!mEntries.get(position).getStdmname().contains("anyType{}") ? mEntries.get(position).getStdmname() : "";
		}
		txtmn.setText(mn);

		String dob=mEntries.get(position).getStdDOB();
		if(dob!=null)
		{
			//20190809
			dob=!mEntries.get(position).getStdDOB().contains("anyType{}") ? mEntries.get(position).getStdDOB() : "";
			Log.e("DoB",dob);
			if(dob.length()>7) {
//					dob = dob.substring(6, 8) + "-" + dob.substring(4, 6) + "-" + dob.substring(0, 4);
				dob=dob;
			}
		}
		txtdob.setText(dob);

		String sn=mEntries.get(position).getStdname();
		if(sn!=null)
		{
			sn=!mEntries.get(position).getStdname().contains("anyType{}") ? mEntries.get(position).getStdname() : "";
		}
		txtname.setText(sn);

		String an=mEntries.get(position).getStdadmnum();
		if(an!=null)
		{
			an=!mEntries.get(position).getStdadmnum().contains("anyType{}") ? mEntries.get(position).getStdadmnum() : "";
		}
		txtadnum.setText(an);

		//txt_ACNum, txt_IFSC, txt_AdmDt
		String acn=mEntries.get(position).getStdacnum();
		if(acn!=null)
		{
			acn=!mEntries.get(position).getStdacnum().contains("anyType{}") ? mEntries.get(position).getStdacnum() : "";
		}
		if(acn.length()>4)
		{
			//acn="XXXXXX"+acn.substring(acn.length()-4,4);
			acn="XXXXXX"+acn.substring(acn.length()-4);
		}
		Log.e("AC",acn);

		txt_ACNum.setText(acn);

		String ifsc=mEntries.get(position).getStdifsc();
		if(ifsc!=null)
		{
			ifsc=!mEntries.get(position).getStdifsc().contains("anyType{}") ? mEntries.get(position).getStdifsc() : "";
		}
		txt_IFSC.setText(ifsc);

		String admdt=mEntries.get(position).getStdadmdate();
		if(admdt!=null)
		{
			admdt=!mEntries.get(position).getStdadmdate().contains("anyType{}") ? mEntries.get(position).getStdadmdate() : "";
			if(admdt.length()>7) {
//					admdt = admdt.substring(6, 8) + "-" + admdt.substring(4, 6) + "-" + admdt.substring(0, 4);
				admdt=admdt;
			}

		}
		txt_AdmDt.setText(admdt);

		String mob=mEntries.get(position).getStdmobile();
		if(mob!=null)
		{
			mob=!mEntries.get(position).getStdmobile().contains("anyType{}") ? mEntries.get(position).getStdmobile() : "";

		}
		if(mob.length()>4)
		{
			mob="XXXXXX"+mob.substring(mob.length()-4);
		}
		txt_Mob.setText(mob);
		//------------------
		String gen=mEntries.get(position).getStdgenid();
		if(gen!=null)
		{
			gen=!mEntries.get(position).getStdgenid().contains("anyType{}") ? mEntries.get(position).getStdgenid() : "";

		}
		txt_Gen.setText(gen);
		txt_Gen.setText(getValue(gen,"GenderHN"));

		String ward=mEntries.get(position).getWardVillage();
		if(ward!=null)
		{
			ward=!mEntries.get(position).getWardVillage().contains("anyType{}") ? mEntries.get(position).getWardVillage() : "";

		}
		txt_Ward.setText(ward);

		String cat=mEntries.get(position).getStdcatid();
		if(cat!=null)
		{
			cat=!mEntries.get(position).getStdcatid().contains("anyType{}") ? mEntries.get(position).getStdcatid() : "";

		}
		//txt_Cat.setText(cat);
		txt_Cat.setText(getValue(cat,"CategoryHN"));

		String mino=mEntries.get(position).getStdisminority();
		if(mino!=null)
		{
			mino=!mEntries.get(position).getStdisminority().contains("anyType{}") ? mEntries.get(position).getStdisminority() : "";
		}
		txt_Minority.setText(mino);

		String hand=mEntries.get(position).getStdishandicaped();
		if(hand!=null)
		{
			hand=!mEntries.get(position).getStdishandicaped().contains("anyType{}") ? mEntries.get(position).getStdishandicaped() : "";
		}
		txt_Handi.setText(hand);

		String aadnum=mEntries.get(position).getStdaadharcardno();
		if(aadnum!=null)
		{
			aadnum=!mEntries.get(position).getStdaadharcardno().contains("anyType{}") ? mEntries.get(position).getStdaadharcardno() : "";

		}
		if(aadnum.length()>4)
		{
			aadnum="XXXXXX"+aadnum.substring(mob.length()-4);
		}
		txt_AdNum.setText(aadnum);

		String aadnam=mEntries.get(position).getStdaadharcardname();
		if(aadnam!=null)
		{
			aadnam=!mEntries.get(position).getStdaadharcardname().contains("anyType{}") ? mEntries.get(position).getStdaadharcardname() : "";

		}

		txt_AdName.setText(aadnam);


		String reason=mEntries.get(position).get_eupi_Reason();
		if(reason!=null)
		{
			reason=!mEntries.get(position).get_eupi_Reason().contains("anyType{}") ? mEntries.get(position).get_eupi_Reason() : "";

		}

		txt_reason.setText(reason);


		return vi;
	}

	public void ShowAlertMSG(final String BenID)
	{
		final String _lang= PreferenceManager.getDefaultSharedPreferences(mContext).getString("LANG", "");
		final String _disecode= PreferenceManager.getDefaultSharedPreferences(mContext).getString("DISECODE", "");
		if(Utiilties.isOnline(mContext)) {
			android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(
					mContext);
			if(_lang.equalsIgnoreCase("en")) {
				ab.setTitle("Upload Records");
				ab.setMessage("Are you sure ? Want to upload records to the server ?");
				ab.setNegativeButton("[ NO ]", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

				ab.setPositiveButton(
						"[ YES ]", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
								UploadRecord(_lang,_disecode,BenID);
							}
						});

				ab.show();
			}
			else
			{
				ab.setTitle("रिकॉर्ड अपलोड करें");
				ab.setMessage("क्या आपको यकीन है ? सर्वर पर रिकॉर्ड अपलोड करना चाहते हैं?");
				ab.setNegativeButton("[ नही ]", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

				ab.setPositiveButton(
						"[ हाॅं ]", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
								UploadRecord(_lang,_disecode,BenID);
							}
						});

				ab.show();
			}
		}
		else
		{
			final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
			if(_lang.equalsIgnoreCase("en")) {
				alertDialog.setTitle(mContext.getResources().getString(R.string.no_internet_title));
				alertDialog.setMessage(mContext.getResources().getString(R.string.no_internet_msg));
				alertDialog.setButton(mContext.getResources().getString(R.string.turnon_now), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						alertDialog.cancel();
						Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						mContext.startActivity(I);
					}
				});

				alertDialog.show();
			}
			else
			{
				alertDialog.setTitle(mContext.getResources().getString(R.string.no_internet_titlehn));
				alertDialog.setMessage(mContext.getResources().getString(R.string.no_internet_msghn));
				alertDialog.setButton(mContext.getResources().getString(R.string.turnon_nowhn), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						alertDialog.cancel();
						Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						mContext.startActivity(I);
					}
				});

				alertDialog.show();
			}
		}
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {

			return model.toUpperCase();
		} else {

			return manufacturer.toUpperCase() + " " + model;
		}
	}
	public String getAppVersion(){
		try {
			version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public void UploadRecord(String lang,String disecode,String BenID) {

		String diseCode= PreferenceManager.getDefaultSharedPreferences(mContext).getString("DISECODE", "");
		DataBaseHelper placeData = new DataBaseHelper(mContext);
		SQLiteDatabase db = placeData.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT * FROM StudentListForAttendance WHERE IsRecordUpdated='Y' AND BenID='"+ BenID +"'",
						null);

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String[] param = new String[42];

				param[0] = cursor.getString(cursor.getColumnIndex("StdRegNum"));
				param[1] = cursor.getString(cursor.getColumnIndex("Addmissiondate"));
				param[2] = cursor.getString(cursor.getColumnIndex("StdName"));
				param[3] = cursor.getString(cursor.getColumnIndex("StdNameHn"));
				param[4] = cursor.getString(cursor.getColumnIndex("StdFName"));
				param[5] = cursor.getString(cursor.getColumnIndex("StdFNameHn"));

				param[6] = cursor.getString(cursor.getColumnIndex("StdMName"));
				param[7] = cursor.getString(cursor.getColumnIndex("StdMNameHn"));

				param[8] = cursor.getString(cursor.getColumnIndex("StdGenderID"));
				param[9] = cursor.getString(cursor.getColumnIndex("StdGender"));
				param[10] = cursor.getString(cursor.getColumnIndex("StdGenderHn"));

				param[11] = cursor.getString(cursor.getColumnIndex("StdCategoryID"));
				param[12] = cursor.getString(cursor.getColumnIndex("StdCategoryName"));
				param[13] = cursor.getString(cursor.getColumnIndex("StdCategoryNameHn"));

				param[14] = cursor.getString(cursor.getColumnIndex("IsMinority"));
				param[15] = cursor.getString(cursor.getColumnIndex("IsHandicapped"));
				param[16] = cursor.getString(cursor.getColumnIndex("IsBPL"));

				param[17] = cursor.getString(cursor.getColumnIndex("AadharCardNo"));
				param[18] = cursor.getString(cursor.getColumnIndex("AadharCardName"));
				param[19] = cursor.getString(cursor.getColumnIndex("StdMobNum"));

				param[20] = cursor.getString(cursor.getColumnIndex("EmailId"));
				param[21] = cursor.getString(cursor.getColumnIndex("IFSC"));
				param[22] = cursor.getString(cursor.getColumnIndex("AccountNo"));
				param[23] = cursor.getString(cursor.getColumnIndex("AccountHolderType_Id"));
				param[24] = cursor.getString(cursor.getColumnIndex("FYearID"));
				param[25] =diseCode;
				param[26] =cursor.getString(cursor.getColumnIndex("StdClassID"));
				param[27] =cursor.getString(cursor.getColumnIndex("StdSectionID"));

				param[28] =cursor.getString(cursor.getColumnIndex("StdDoB"));
				param[29] =cursor.getString(cursor.getColumnIndex("CreatedBy"));
				param[30] =cursor.getString(cursor.getColumnIndex("CreatedDate"));
				// param[31] =cursor.getString(cursor.getColumnIndex("BenID"));
				param[31] =cursor.getString(cursor.getColumnIndex("a_ID"));
				param[32] =cursor.getString(cursor.getColumnIndex("WardVillage"));

				param[33] =cursor.getString(cursor.getColumnIndex("WardCode"));
				param[34] =cursor.getString(cursor.getColumnIndex("BlockCode"));

				param[35] =cursor.getString(cursor.getColumnIndex("CUBy"));
				param[36] =cursor.getString(cursor.getColumnIndex("CUDate"));
				param[37] =cursor.getString(cursor.getColumnIndex("OptVerify"));

				param[38] =getIMEI();
				param[39] =cursor.getString(cursor.getColumnIndex("DistrictCode"));
				//param[40] =cursor.getString(cursor.getColumnIndex("AccountHolderName"));
				param[40] =cursor.getString(cursor.getColumnIndex("AccountHolderType_Name"));
				param[41] =cursor.getString(cursor.getColumnIndex("sendPfms_id"));
				new UploadEditedDetailsOfStudent(lang).execute(param);
				//----new UploadEditedDetailsOfStudentArray(lang).execute(param);
			}
		} else {
			if(lang.equalsIgnoreCase("en")) {
				Toast.makeText(mContext, "You have no upload pending !", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(mContext, "आपके पास कोई अपलोड लंबित नहीं है!", Toast.LENGTH_SHORT).show();
			}
		}
		cursor.close();
		db.close();
		placeData.getReadableDatabase().close();
	}
	private String getIMEI(){
		TelephonyManager tm = null;
		String imei = null;
		MarshmallowPermission permission;
		permission=new MarshmallowPermission(mContext, Manifest.permission.READ_PHONE_STATE);
		if(permission.result==-1 || permission.result==0)
		{
			try
			{
				tm= (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

				if(tm!=null) imei = tm.getDeviceId();
			}catch(Exception e){
				imei="000000000000";

				imei = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		}
		return imei;
	}
	private class UploadEditedDetailsOfStudent extends AsyncTask<String, Void, String> {

		String _bid;
		String _lang;

		private final ProgressDialog progressDialog = new ProgressDialog(mContext);


		UploadEditedDetailsOfStudent(String lang)
		{
			this._lang=lang;
		}
		protected void onPreExecute() {
			progressDialog.setCancelable(false);
			progressDialog.show();
			progressDialog.setMessage("Uploading/अपलोडिंग ...");
		}

		@Override
		protected String doInBackground(String... param) {

			String devicename=getDeviceName();
			String app_version=getAppVersion();
			boolean isTablet=isTablet(mContext);
			if(isTablet) {
				devicename="Tablet::"+devicename;
				Log.e("DEVICE_TYPE", "Tablet");
			} else {
				devicename="Mobile::"+devicename;
				Log.e("DEVICE_TYPE", "Mobile");
			}


			this._bid=param[31];
			String res= WebServiceHelper.UploadStdUpdatedDetails(mContext,devicename,app_version,param);

			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();

				}
			}
			catch(Exception e)
			{

			}

			try
			{
				if(result!=null)
				{
					if(result.contains("रिकॉर्ड सफलतापूर्वक अपडेट हो  गया है.."))
					{
						removeShortcut();
						DataBaseHelper localDB = new DataBaseHelper(mContext);
						ContentValues values = new ContentValues();
						values.put("IsRecordUpdated","N");
						String[] whereArgsss = new String[]{_bid};
						SQLiteDatabase db = localDB.getWritableDatabase();
						long c = db.update("StudentListForAttendance", values, "a_ID=?", whereArgsss);

						if(c>0)
						{
							refresList();
						}
						if(_lang.equalsIgnoreCase("en")) {
							Toast.makeText(mContext, "Uploaded to server successfully for ben id " + _bid, Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(mContext, _bid +" बेन आईडी के लिए सफलतापूर्वक सर्वर पर अपलोड किया गया " , Toast.LENGTH_SHORT).show();
						}
						Log.e("Updated For Ben ID ","");
						createShortCut();
					}
					else {
						if(_lang.equalsIgnoreCase("en")) {
							Toast.makeText(mContext, "Sorry! failed to upload for ben id " + _bid + "\nResponse " + result, Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(mContext, "माफ़ कीजिये! बेन आईडी " + _bid + " के लिए अपलोड विफल \nरेस्पोंस " + result, Toast.LENGTH_SHORT).show();
						}
						if(_lang.equalsIgnoreCase("hn"))
						{
							final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
							alertDialog.setTitle("FAILED TO UPLOAD");
							alertDialog.setMessage(result);
							alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

									alertDialog.cancel();

								}
							});

							alertDialog.show();
						}
						else
						{
							final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
							alertDialog.setTitle("FAILED TO UPLOAD");
							alertDialog.setMessage(result);
							alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {

									alertDialog.cancel();
								}
							});

							alertDialog.show();
						}
					}
				}
				else
				{
					if(_lang.equalsIgnoreCase("en")) {
						Toast.makeText(mContext, "Response:NULL, Sorry! failed to upload", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(mContext, "रेस्पोंस: NULL, क्षमा करें! अपलोड करने में विफल", Toast.LENGTH_SHORT).show();
					}
				}
			}
			catch (Exception e)
			{

			}
		}
	}

	private class UploadEditedDetailsOfStudentArray extends AsyncTask<String, Void, String> {

		String _bid;
		String _lang;

		private final ProgressDialog progressDialog = new ProgressDialog(mContext);


		UploadEditedDetailsOfStudentArray(String lang)
		{
			this._lang=lang;
		}
		protected void onPreExecute() {
			progressDialog.setCancelable(false);
			progressDialog.show();
			progressDialog.setMessage("Uploading/अपलोडिंग ...");
		}

		@Override
		protected String doInBackground(String... param) {
			this._bid=param[31];
			String res= WebServiceHelper.UploadUploadAttendanceDataArray(mContext,param);

			return res;
		}

		@Override
		protected void onPostExecute(String  result) {
			try {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();

				}
			}
			catch(Exception e)
			{

			}

			try
			{
				if(result!=null)
				{
					Log.e("RESULT--",result);
					Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

				}
				else
				{
					if(_lang.equalsIgnoreCase("en")) {
						Toast.makeText(mContext, "Response:NULL, Sorry! failed to upload", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(mContext, "रेस्पोंस: NULL, क्षमा करें! अपलोड करने में विफल", Toast.LENGTH_SHORT).show();
					}

				}
			}
			catch (Exception e)
			{

			}
		}
	}

	public void refresList()
	{
		ArrayList<studentList> stdDataEntities = new ArrayList<studentList>();
		DataBaseHelper helper = new DataBaseHelper(mContext);
		DataBaseHelper placeData = new DataBaseHelper(mContext);
		SQLiteDatabase db = placeData.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT * FROM StudentListForAttendance WHERE IsRecordUpdated='Y'",
						null);
		((ViewEditUpdatedListActivity)mContext).setCount(cursor.getCount());
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				studentList stdData = new studentList();
				stdData.setStdname((cursor.getString(cursor.getColumnIndex("StdName"))));
				stdData.set_eupi_Reason((cursor.getString(cursor.getColumnIndex("eupi_reject_reason"))));
				stdData.setStdnamehn((cursor.getString(cursor.getColumnIndex("StdNameHn"))));
				stdData.setStdfname((cursor.getString(cursor.getColumnIndex("StdFName"))));
				stdData.setStdfnamehn((cursor.getString(cursor.getColumnIndex("StdFNameHn"))));
				stdData.setStdmname((cursor.getString(cursor.getColumnIndex("StdMName"))));

				stdData.setStdclass((cursor.getString(cursor.getColumnIndex("StdClass"))));
				stdData.setStdclassid((cursor.getString(cursor.getColumnIndex("StdClassID"))));


				String sec;
				if(cursor.getString(cursor.getColumnIndex("StdSession"))==null)
				{
					sec="";
				}
				else if(cursor.getString(cursor.getColumnIndex("StdSession")).equalsIgnoreCase("anyType{}"))
				{
					sec="";
				}
				else
				{
					sec=cursor.getString(cursor.getColumnIndex("StdSession"));
				}
				stdData.setStdsession(sec);

				stdData.setStdsessionid((cursor.getString(cursor.getColumnIndex("StdSectionID"))));
				/////   stdData.setStdcategory((cursor.getString(cursor.getColumnIndex("StdCategory"))));

				stdData.setStdbenid((cursor.getString(cursor.getColumnIndex("BenID"))));
				//////  stdData.setStdgender((cursor.getString(cursor.getColumnIndex("StdGender"))));
				stdData.setStdadmnum((cursor.getString(cursor.getColumnIndex("StdRegNum"))));
				stdData.setStdateendanceless((cursor.getString(cursor.getColumnIndex("StdAttendanceLess"))));
				stdData.setStdattseventyfiveper((cursor.getString(cursor.getColumnIndex("AttSeventyFivePercent"))));

				String dob;
				//String dob=cursor.getString(cursor.getColumnIndex("StdDoB")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdDoB"));
				//dob=cursor.getString(cursor.getColumnIndex("StdDoB"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("StdDoB"));

				if(cursor.getString(cursor.getColumnIndex("StdDoB"))==null)
				{
					dob="";
				}
				else if(cursor.getString(cursor.getColumnIndex("StdDoB")).equalsIgnoreCase("anyType{}"))
				{
					dob="";
				}
				else
				{
					dob=cursor.getString(cursor.getColumnIndex("StdDoB"));
				}

				stdData.setStdDOB(dob);

				String mobnum;
				//String mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum")).contains("anyType{}")?"":cursor.getString(cursor.getColumnIndex("StdMobNum"));
				if(cursor.getString(cursor.getColumnIndex("StdMobNum"))==null)
				{
					mobnum="";
				}
				else if(cursor.getString(cursor.getColumnIndex("StdMobNum")).equalsIgnoreCase("anyType{}"))
				{
					mobnum="";
				}
				else
				{
					mobnum=cursor.getString(cursor.getColumnIndex("StdMobNum"));
				}
				if(mobnum.length()>4)
				{
					mobnum="XXXXXX"+mobnum.substring(mobnum.length()-4);
				}
				stdData.setStdmobile(mobnum);


				String admdate;
				if(cursor.getString(cursor.getColumnIndex("Addmissiondate"))==null)
				{
					admdate="";
				}
				else if(cursor.getString(cursor.getColumnIndex("Addmissiondate")).equalsIgnoreCase("anyType{}"))
				{
					admdate="";
				}
				else
				{
					admdate=cursor.getString(cursor.getColumnIndex("Addmissiondate"));
				}
				stdData.setStdadmdate(admdate);

				stdData.setStdisminority((cursor.getString(cursor.getColumnIndex("IsMinority"))));
				stdData.setStdishandicaped((cursor.getString(cursor.getColumnIndex("IsHandicapped"))));

				String aadharno;
				//=cursor.getString(cursor.getColumnIndex("AadharCardNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AadharCardNo"));
				if(cursor.getString(cursor.getColumnIndex("AadharCardNo"))==null)
				{
					aadharno="";
				}
				else if(cursor.getString(cursor.getColumnIndex("AadharCardNo")).equalsIgnoreCase("anyType{}"))
				{
					aadharno="";
				}
				else
				{
					aadharno=cursor.getString(cursor.getColumnIndex("AadharCardNo"));
				}
				if(aadharno.length()>4)
				{
					aadharno="XXXXXXXX"+aadharno.substring(aadharno.length()-4);
				}
				stdData.setStdaadharcardno(aadharno);

				stdData.setStdaadharcardname((cursor.getString(cursor.getColumnIndex("AadharCardName"))));

				String email;
				//=cursor.getString(cursor.getColumnIndex("EmailId"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("EmailId"));
				if(cursor.getString(cursor.getColumnIndex("EmailId"))==null)
				{
					email="";
				}
				else if(cursor.getString(cursor.getColumnIndex("EmailId")).equalsIgnoreCase("anyType{}"))
				{
					email="";
				}
				else
				{
					email=cursor.getString(cursor.getColumnIndex("EmailId"));
				}
				stdData.setStdemailid(email);

				stdData.setStdisbpl((cursor.getString(cursor.getColumnIndex("IsBPL"))));

				String ifsc;
				//=cursor.getString(cursor.getColumnIndex("IFSC"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("IFSC"));
				if(cursor.getString(cursor.getColumnIndex("IFSC"))==null)
				{
					ifsc="";
				}
				else if(cursor.getString(cursor.getColumnIndex("IFSC")).equalsIgnoreCase("anyType{}"))
				{
					ifsc="";
				}
				else
				{
					ifsc=cursor.getString(cursor.getColumnIndex("IFSC"));
				}
				stdData.setStdifsc(ifsc);

				String acnum;
				//=cursor.getString(cursor.getColumnIndex("AccountNo"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountNo"));
				if(cursor.getString(cursor.getColumnIndex("AccountNo"))==null)
				{
					acnum="";
				}
				else if(cursor.getString(cursor.getColumnIndex("AccountNo")).equalsIgnoreCase("anyType{}"))
				{
					acnum="";
				}
				else
				{
					acnum=cursor.getString(cursor.getColumnIndex("AccountNo"));
				}
				if(acnum.length()>4)
				{
					acnum="XXXXXX"+acnum.substring(acnum.length()-4);
				}
				stdData.setStdacnum(acnum);

				String acname;
				//=cursor.getString(cursor.getColumnIndex("AccountHolderName"))=="anyType{}"?"":cursor.getString(cursor.getColumnIndex("AccountHolderName"));
				if(cursor.getString(cursor.getColumnIndex("AccountHolderName"))==null)
				{
					acname="";
				}
				else if(cursor.getString(cursor.getColumnIndex("AccountHolderName")).equalsIgnoreCase("anyType{}"))
				{
					acname="";
				}
				else
				{
					acname=cursor.getString(cursor.getColumnIndex("AccountHolderName"));
				}
				stdData.setStdacholdername(acname);


				String cat;
				if(cursor.getString(cursor.getColumnIndex("StdCategoryID"))==null)
				{
					cat="";
				}
				else if(cursor.getString(cursor.getColumnIndex("StdCategoryID")).equalsIgnoreCase("anyType{}"))
				{
					cat="";
				}
				else
				{
					cat=cursor.getString(cursor.getColumnIndex("StdCategoryID"));
				}
				stdData.setStdcatid(cat);


				String fnyear;
				if(cursor.getString(cursor.getColumnIndex("FYearName"))==null)
				{
					fnyear="";
				}
				else if(cursor.getString(cursor.getColumnIndex("FYearName")).equalsIgnoreCase("anyType{}"))
				{
					fnyear="";
				}
				else
				{
					fnyear=cursor.getString(cursor.getColumnIndex("FYearName"));
				}
				stdData.setStdfyearval(fnyear);

				stdDataEntities.add(stdData);

			}
		}
		mEntries =stdDataEntities;
		upDateEntries(mEntries);

	}

	public String getValue(String forID, String tblName)
	{
		String val="";
		DataBaseHelper helper=new DataBaseHelper(mContext);

		if(tblName.equalsIgnoreCase("CategoryHN"))
		{
			val=helper.getValueOfID("SELECT * FROM  "+tblName+" WHERE CategoryId="+forID);
		}
		else if(tblName.equalsIgnoreCase("GenderHN"))
		{
			val=helper.getValueOfID("SELECT * FROM  "+tblName+" WHERE GenderId="+forID);
		}
		return  val;
	}
	public void createShortCut()
	{
		ico.CreateShortCut("","U");
	}

	private void removeShortcut() {
		ico.removeShortcut(mContext.getString(R.string.app_name));
	}


	public void upDateEntries(ArrayList<studentList> entries) {
		mEntries = entries;
		notifyDataSetChanged();
	}

}
