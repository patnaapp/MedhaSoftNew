package bih.nic.medhasoft.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;




public class Utiilties {

	public Utiilties() {
		// TODO Auto-generated constructor stub
	}

	public static void ShowMessage(Context context, String Title, String Message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(Title);
		alertDialog.setMessage(Message);
		alertDialog.show();
	}

	


	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected() == true);
	}

	public static Bitmap GenerateThumbnail(Bitmap imageBitmap,
                                           int THUMBNAIL_HEIGHT, int THUMBNAIL_WIDTH) {

		Float width = new Float(imageBitmap.getWidth());
		Float height = new Float(imageBitmap.getHeight());
		Float ratio = width / height;
		Bitmap CompressedBitmap = Bitmap.createScaledBitmap(imageBitmap,
				(int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_HEIGHT, false);

		return CompressedBitmap;
	}





	public static Object deserialize(byte[] data) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String getDateString() {
		SimpleDateFormat postFormater = new SimpleDateFormat(
				"MMMM dd, yyyy hh:mm a");

		String newDateStr = postFormater.format(Calendar.getInstance()
				.getTime());
		return newDateStr;




	}

	public static String getDateString(String Formats) {
		SimpleDateFormat postFormater = new SimpleDateFormat(Formats);

		String newDateStr = postFormater.format(Calendar.getInstance()
				.getTime());
		return newDateStr;
	}






	public static void setStatusBarColor(Activity activity)
	{
		if (Build.VERSION.SDK_INT >= 21) {

			Window window = activity.getWindow();


			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			// finally change the color
			window.setStatusBarColor(Color.parseColor("#1565a9"));
		}
	}


	public static String getCurrentDateMDY()
	{
		Calendar cal= Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		month=month+1;

		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		int s=cal.get(Calendar.SECOND);

		String date=month+"-"+day+"-"+year;
		//String date=day+"-"+month+"-"+year;
		Log.e("getCurrentDate",date);
		return date;
	}

	public static String getCurrentDate()
	{
		Calendar cal= Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		month=month+1;

		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		int s=cal.get(Calendar.SECOND);

		//String date=month+"-"+day+"-"+year;
		String date=day+"-"+month+"-"+year;
		Log.e("getCurrentDate",date);
		return date;
	}
	public static String getCurrentDateMonth()
	{
		Calendar cal= Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		month=month+1;

		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		int s=cal.get(Calendar.SECOND);

		//String date=month+"-"+day+"-"+year;
		String date=day+"-"+month+"-"+year;
		Log.e("getCurrentDate",date);
		return date;
	}




	public static String getCurrentDateWithTime() throws ParseException {

			SimpleDateFormat f = new SimpleDateFormat("MMM d,yyyy HH:mm");
		Date date=null;
		date=f.parse(getDateString());
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d,yyyy HH:mm a");
		String dateString = formatter.format(date);
		return dateString;
	}



	public static String getDateTime() {

		String date=getDateString();
		String a="";
		StringTokenizer st=new StringTokenizer(date," ");
		while (st.hasMoreTokens()){
			a=st.nextToken();
		}

		if(a.equals("a.m."))
		{

			date=date.replace(a,"AM");
		}
		if(a.equals("p.m."))
		{
			date=date.replace(a,"PM");


		}


		return date;
	}



	public static String getshowCurrentDate()
	{
		Calendar cal= Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		month =month+1;

		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		int s=cal.get(Calendar.SECOND);

		String date=day+"/"+month+"/"+year;
		return date;

	}

	public static String parseDate(String date)
	{
		StringTokenizer st=new StringTokenizer(date,"/");
		String month="",day="",year="";
		try {
			month = st.nextToken();
			day = st.nextToken();
			year = st.nextToken();
		}catch (Exception e){e.printStackTrace();}

		return day+"/"+month+"/"+year;
	}

	public static void displayPromptForEnablingGPS(final Activity activity)
	{

		final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
		final String message = "Do you want open GPS setting?";

		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								activity.startActivity(new Intent(action));
								d.dismiss();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								d.cancel();
								activity.finish();
							}
						});
		builder.create().show();
	}
	public static boolean isGPSEnabled (Context mContext){
		LocationManager locationManager = (LocationManager)
				mContext.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public static boolean isfrontCameraAvalable(){
		int numCameras= Camera.getNumberOfCameras();
		for(int i=0;i<numCameras;i++){
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if(Camera.CameraInfo.CAMERA_FACING_FRONT == info.facing){
				return true;
			}
		}
		return false;
	}
	public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static boolean checkPermission(final Context context)
	{
		int currentAPIVersion = Build.VERSION.SDK_INT;
		if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
		{
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage("External storage permission is necessary");
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					});
					AlertDialog alert = alertBuilder.create();
					alert.show();
				} else {
					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
				}
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	public static Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public static byte[] bitmaptoByte(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static String BitArrayToString(byte[] b1) {
		byte[] b = b1;
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	public static String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
	public static String getValue(String fromThisVal)
	{
		String val=fromThisVal;
		if(val!=null)
		{
			val=!val.contains("anyType{}") ? val: "";
		}
		else
		{
			val="";
		}
		return val;
	}
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@SuppressWarnings("deprecation")
	public  PrintJob printOrCreatePdfFromWebview(WebView webview, String jobName) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Context c = webview.getContext();
			PrintDocumentAdapter printAdapter;
			PrintManager printManager = (PrintManager) c.getSystemService(Context.PRINT_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				printAdapter = webview.createPrintDocumentAdapter(jobName);
			} else {
				printAdapter = webview.createPrintDocumentAdapter();
			}
			if (printManager != null) {
				return printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
			}
		} else {
			Log.e(getClass().getName(), "ERROR: Method called on too low Android API version");
		}
		return null;
	}

	public static String cleanStringForVulnerability(String aString) {
		if (aString == null) return null;
		String cleanString = "";
		for (int i = 0; i < aString.length(); ++i) {
			cleanString += cleanChar(aString.charAt(i));
		}
		return cleanString;
	}

	private static char cleanChar(char aChar) {

		// 0 - 9
		for (int i = 48; i < 58; ++i) {
			if (aChar == i) return (char) i;
		}

		// 'A' - 'Z'
		for (int i = 65; i < 91; ++i) {
			if (aChar == i) return (char) i;
		}

		// 'a' - 'z'
		for (int i = 97; i < 123; ++i) {
			if (aChar == i) return (char) i;
		}

		// other valid characters
		switch (aChar) {
			case '/':
				return '/';
			case '.':
				return '.';
			case '-':
				return '-';
			case '_':
				return '_';
			case ' ':
				return ' ';
		}
		return '%';
	}
}
