package com.example.tushar.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity extends AppCompatActivity {

	SQLiteDatabase database;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		database = openOrCreateDatabase("IODatabase",MODE_PRIVATE,null);
		database.execSQL("CREATE TABLE IF NOT EXISTS studentsTable(RollNo INT PRIMARY KEY, Name VARCHAR, Semester VARCHAR);");
	}

	public void insertEntry(View view)
	{
		Context context = getApplicationContext();
		EditText rollNo = (EditText) findViewById(R.id.rollNo2insert);
		String rollNoString = rollNo.getText().toString();
		EditText name = (EditText) findViewById(R.id.name2insert);
		String nameString = name.getText().toString();
		EditText sem = (EditText) findViewById(R.id.sem2insert);
		String semString = sem.getText().toString();
		if ( nameString.length() == 0 || semString.length() == 0 || rollNoString.length() == 0 )
		{
			CharSequence text = "Fields can't be empty.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			try
			{
				int rollNumInteger = Integer.parseInt(rollNoString);
				Cursor cursor = database.rawQuery("SELECT RollNo FROM studentsTable WHERE RollNo=?",new String[] {rollNumInteger + ""});
				if (cursor.getCount() == 0)
				{
					ContentValues values = new ContentValues();
					values.put("RollNo",rollNumInteger);
					values.put("Name",nameString);
					values.put("Semester",semString);
					database.insert("studentsTable",null,values);
				}
				else
				{
					CharSequence text = "Roll number already exists";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				cursor.close();
			}
			catch (NumberFormatException e) {

				CharSequence text = "Roll number should be an integer";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}

	}
	public void deleteEntry(View view)
	{
		Context context = getApplicationContext();
		EditText rollNo = (EditText) findViewById(R.id.rollNo2Delete);
		String rollNoString = rollNo.getText().toString();
		if ( rollNoString.length() == 0 )
		{
			CharSequence text = "Roll number cannot be empty.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			try
			{
				int rollNumInteger = Integer.parseInt(rollNoString);
				if ( database.delete("studentsTable","rollNo = " + rollNumInteger,null) > 0 )
				{
					CharSequence text = "Deleted";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				else
				{
					CharSequence text = "No such row.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

			}
			catch (NumberFormatException e) {

				CharSequence text = "Roll number should be an integer";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
	}
	public void updateEntry(View view)
	{
		Context context = getApplicationContext();
		EditText rollNoToUpdate = (EditText) findViewById(R.id.rollNoToUpdate);
		String rollNoToUpdateString = rollNoToUpdate.getText().toString();
		EditText updatedRollNo = (EditText) findViewById(R.id.updatedRollNumber);
		String updatedRollNoString = updatedRollNo.getText().toString();
		EditText updatedName = (EditText) findViewById(R.id.updatedName);
		String updatedNameString = updatedName.getText().toString();
		EditText updatedSem = (EditText) findViewById(R.id.updatedSemester);
		String updatedSemString = updatedSem.getText().toString();
		if ( rollNoToUpdateString.length() == 0 || updatedRollNoString.length() == 0 )
		{
			CharSequence text = "Roll numbers can't be empty. Enter same roll numbers if required";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			try {
				int rollNoToUpdateInteger = Integer.parseInt(rollNoToUpdateString);
				int updatedRollNoInteger = Integer.parseInt(updatedRollNoString);
				ContentValues values = new ContentValues();
				if ( updatedNameString.length() == 0 && updatedSemString.length() == 0 )
				{
					values.put("RollNo", updatedRollNoInteger);
				}
				else if ( updatedNameString.length() != 0 && updatedSemString.length() != 0 )
				{
					values.put("RollNo", updatedRollNoInteger);
					values.put("Name", updatedNameString);
					values.put("Semester", updatedSemString);
				}
				else if ( updatedSemString.length() != 0 )
				{
					values.put("RollNo", updatedRollNoInteger);
					values.put("Semester", updatedSemString);
				}
				else
				{
					values.put("RollNo", updatedRollNoInteger);
					values.put("Name", updatedNameString);
				}
				if ( database.update("studentsTable", values, "RollNo = ? ", new String[] { rollNoToUpdateInteger + "" } ) > 0 )
				{
					CharSequence text = "Updated";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				else
				{
					CharSequence text = "No record changed";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

			}
			catch (NumberFormatException e)
			{
				CharSequence text = "Both roll numbers should be integers";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

		}

	}
	public void displayAll(View view)
	{
		Context context = getApplicationContext();
		Cursor cursor = database.rawQuery("SELECT * FROM studentsTable",null);
		if (cursor.getCount() == 0)
		{
			LinearLayout outputWindow = (LinearLayout)findViewById(R.id.databaseOutput);
			outputWindow.removeAllViews();
			CharSequence text = "Table empty.";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			LinearLayout outputWindow = (LinearLayout)findViewById(R.id.databaseOutput);
			outputWindow.removeAllViews();
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				TextView row = new TextView(this);
				row.setText(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
				outputWindow.addView(row);
				cursor.moveToNext();
			}
		}
		cursor.close();
	}
}
