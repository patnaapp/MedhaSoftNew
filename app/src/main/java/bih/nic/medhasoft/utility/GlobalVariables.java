package bih.nic.medhasoft.utility;

import android.location.Location;

import java.util.ArrayList;

import bih.nic.medhasoft.entity.BlkUserDetails;
import bih.nic.medhasoft.entity.CheckUnCheck;
import bih.nic.medhasoft.entity.Get_usersalldetails;
import bih.nic.medhasoft.entity.UserDetails;


public class GlobalVariables {

	public static BlkUserDetails LoggedUser;
	public static boolean isOffline = false;
	public static boolean isOfflineGPS = false;
    public static int uploadNo=0;
    public static int listSize=0;
    public static String LANG="en";


	public static String REPORTTYPE="ReportType";
	public static int rtype=0;


    public static boolean fieldDownloaded=false;
    public static boolean SpinnerDataDownloaded=false;
    public static boolean downloadFyearData=false;
    public static boolean downloadDistrictData=false;
    
	
	public static String MunicipalCorporationId="";
	
	public static String WardId="";
	public static String AreaId="";
	public static String UserId="";
	
	public static String Last_Visited="";
	public static Location glocation=null;
	//public static ArrayList<CheckUnCheck> benIDArrayList;

	public static ArrayList<CheckUnCheck> benIDArrayList=new ArrayList<>();





}
