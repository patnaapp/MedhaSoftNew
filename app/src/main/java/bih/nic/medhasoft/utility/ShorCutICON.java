package bih.nic.medhasoft.utility;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import bih.nic.medhasoft.AttendanceListActivity;
import bih.nic.medhasoft.PREHomeActivity;
import bih.nic.medhasoft.R;
import bih.nic.medhasoft.database.DataBaseHelper;


public class ShorCutICON {
    Context _context;
    DataBaseHelper localDBHelper;
    public ShorCutICON(Context context)
    {
        this._context=context;
        localDBHelper=new DataBaseHelper(_context);
    }
    public void CreateShortCut(String number, String UType) {

        try {
            DataBaseHelper helper = new DataBaseHelper(_context);
            long countstd = helper.getStudentCountForUploading();
            long countattstd = helper.getStudentCountForAttendanceUploading();

            long total = countattstd + countstd;
            number = String.valueOf(total);


            String APP_NAME = _context.getResources().getString(R.string.app_name);

            Log.e("Num", "" + number);

            Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), R.mipmap.ic_launcher);


            if(bitmap==null)
            {
                bitmap= BitmapFactory.decodeResource(_context.getResources(), R.mipmap.ic_launcher_round);
            }
            if(bitmap==null)
            {
                bitmap= BitmapFactory.decodeResource(_context.getResources(), R.mipmap.ic_launcher_foreground);
            }
            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Paint paint = new Paint();
            //paint.setColor(0xFFFF0000); // gray
            paint.setColor(Color.BLUE); // gray
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            Canvas canvas = new Canvas(mutableBitmap);

            int xPos = (canvas.getWidth() / 2);     //-2 is for regulating the x position offset

            int yPos = (int) ((canvas.getHeight()) + ((paint.descent() + paint.ascent()) * 2)) - 30;

            Paint.FontMetrics fm = new Paint.FontMetrics();
            int margin = 2;
            if (UType.equalsIgnoreCase("P")) {
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.RED);
            }

            paint.setTextSize(35.0f);
            paint.getFontMetrics(fm);

            if (!number.equalsIgnoreCase("0")) {
                canvas.drawCircle(xPos + 35, yPos - 67, 35, paint);


                if (UType.equalsIgnoreCase("P")) {
                    paint.setColor(Color.BLACK); // gray
                } else {
                    paint.setColor(Color.YELLOW); // gray
                }

                // canvas.drawText(""+number, xPos-5 , yPos, paint); //for circle

                if (number.length() > 2) {
                    canvas.drawText("" + number, xPos + 8, yPos - 59, paint);

                } else if (number.length() == 2) {
                    canvas.drawText("" + number, xPos + 15, yPos - 59, paint);

                } else {
                    canvas.drawText("" + number, xPos + 24, yPos - 59, paint);

                }
            }
            Intent shortcutIntent = new Intent(_context, PREHomeActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, APP_NAME);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, mutableBitmap);

            // Inform launcher to create shortcut
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            _context.sendBroadcast(addIntent);

            mutableBitmap.eraseColor(Color.TRANSPARENT);
            mutableBitmap.recycle();

        }
        catch (Exception e)
        {
            e.printStackTrace();
           Log.e("1EXCP:ICON", e.getMessage());
           Log.e("2EXCP:ICON", e.getLocalizedMessage());
           Log.e("3EXCP:ICON", "XXXXXXX");
            Toast.makeText(_context, "EXCP::NO SHORTCUT ICON", Toast.LENGTH_SHORT).show();
        }

    }
    public void removeShortcut(String appname) {

        String APP_NAME = PreferenceManager.getDefaultSharedPreferences(_context).getString("APP_NAME", "");
        Intent shortcutIntent = new Intent(_context,	PREHomeActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appname);
        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        _context.sendBroadcast(addIntent);

    }
}
