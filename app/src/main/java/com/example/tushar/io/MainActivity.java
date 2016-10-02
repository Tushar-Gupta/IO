package com.example.tushar.io;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
	SharedPreferences sharedpreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sharedpreferences = getSharedPreferences("IOPreferences", MODE_PRIVATE);
		String appColor = sharedpreferences.getString("AppColor", null);
		if (appColor != null) {
			switch (appColor) {
				case ("Red"):
					((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor("#ef7a5d"));
					break;
				case ("Black"):
					((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor("#464646"));
					break;
				case ("Blue"):
					((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor("#5d8eef"));
					break;
				case ("White"):
					((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor("#ffffff"));
					break;
			}
		}

	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public void colorPick(View colorPicked)
	{
		String redColor = new String("#ef7a5d");
		String blackColor = new String("#464646");
		String blueColor = new String("#5d8eef");
		String whiteColor = new String("#ffffff");
		sharedpreferences = getSharedPreferences("IOPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		switch (colorPicked.getId()) {
			case (R.id.pickRed):
				((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor(redColor));
				editor.putString("AppColor", "Red");
				break;
			case (R.id.pickBlack):
				((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor(blackColor));
				editor.putString("AppColor", "Black");
				break;
			case (R.id.pickBlue):
				((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor(blueColor));
				editor.putString("AppColor", "Blue");
				break;
			case (R.id.pickWhite):
				((LinearLayout) findViewById(R.id.mainLayout)).setBackgroundColor(Color.parseColor(whiteColor));
				editor.putString("AppColor", "White");
				break;
		}
		editor.commit();
	}

	/** Called when the user clicks the 'Write to internal storage' button */
	public void write2Internal(View view) {
		Context context = getApplicationContext();
		EditText editText2write = (EditText) findViewById(R.id.stringForIntStorage);
		String string2write = editText2write.getText().toString();
		if (string2write.length()>0)
		{
			String filename = "IOFile";
			FileOutputStream outputStream;
			try {
				outputStream = openFileOutput(filename, Context.MODE_APPEND);
				outputStream.write(string2write.getBytes());
				outputStream.close();

				CharSequence text = "Written to internal storage.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			CharSequence text = "Please enter a valid string";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	/** Called when the user clicks the 'Read internal storage' button */
	public void readInternal(View view)
	{
		try {
			FileInputStream inputStream = openFileInput("IOFile");
			int charRead;
			String stringRead = "";
			while ((charRead = inputStream.read()) != -1) {
				stringRead = stringRead + Character.toString((char) charRead);
			}
			inputStream.close();
			TextView internalOutputView = (TextView) (findViewById(R.id.internalOutput));
			internalOutputView.setText(stringRead);
		}
		catch (Exception e)
		{
			TextView internalOutputView = (TextView) (findViewById(R.id.internalOutput));
			internalOutputView.setText("");
			Context context = getApplicationContext();
			CharSequence text = "No such file.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	/** Called when the user clicks the 'Delete' button for internal storage */
	public void deleteInternal(View view)
	{
		try {
			File dir = getFilesDir();
			File file = new File(dir, "IOFile");
			file.delete();
		}
		catch (Exception e)
		{
			Context context = getApplicationContext();
			CharSequence text = "File already deleted";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	public File getFileStorageDir(Context context, String dirName) {
		// Get the directory for the app's private files directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);

		boolean success = true;
		if (!file.exists()) {
			if (Environment.isExternalStorageEmulated())
			{
				CharSequence text = "Ext is emulated.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			success = file.mkdir();
		}
		if (!success)
		{

			CharSequence text = "Directory not created.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		return file;
	}

	/** Called when the user clicks the 'Write to external storage' button */
	public void write2External(View view) {
		Context context = getApplicationContext();
		if (isExternalStorageWritable())
		{
			EditText editText2write = (EditText) findViewById(R.id.stringForExtStorage);
			String string2write = editText2write.getText().toString();
			if (string2write.length()>0)
			{
				String filename = "IOFile";
				File file = new File(getFileStorageDir(context,"IO"),"IOFile.txt");
				if (file != null)
				{
					try {
						FileWriter extFileWriter = new FileWriter(file, true);
						extFileWriter.append(string2write);
						extFileWriter.close();
						CharSequence text = "Written to external storage.";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else
			{
				CharSequence text = "Please enter a valid string";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		else
		{
			CharSequence text = "External storage is not writeable";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/** Called when the user clicks the 'Read external storage' button */
	public void readExternal(View view)
	{
		Context context = getApplicationContext();
		if (isExternalStorageWritable())
		{
			try {
				File dir = getFileStorageDir(context,"IO");
				File file = new File(dir, "IOFile.txt");
				FileInputStream inputStream = new FileInputStream(file);
				int charRead;
				String stringRead = "";
				while ((charRead = inputStream.read()) != -1) {
					stringRead = stringRead + Character.toString((char) charRead);
				}
				inputStream.close();
				TextView externalOutputView = (TextView) (findViewById(R.id.externalOutput));
				externalOutputView.setText(stringRead);
			}
			catch (Exception e)
			{
				TextView externalOutputView = (TextView) (findViewById(R.id.externalOutput));
				externalOutputView.setText("");

				CharSequence text = "No such file.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		else
		{
			CharSequence text = "External storage is not writeable";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	/** Called when the user clicks the 'Delete' button for external storage */
	public void deleteExternal(View view)
	{
		Context context = getApplicationContext();
		if (isExternalStorageWritable() == true)
		{
			try
			{
				File dir = getFileStorageDir(context,"IO");
				File file = new File(dir, "IOFile.txt");
				file.delete();
			}
			catch (Exception e)
			{

				CharSequence text = "File already deleted";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		else
		{
			CharSequence text = "External storage is not writeable";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	public void startDatabaseActivity(View view)
	{
		Intent intent = new Intent(this, DatabaseActivity.class);
		startActivity(intent);
	}


}
