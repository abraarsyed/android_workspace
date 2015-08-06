package com.example.abraarsyed.tables;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et1=(EditText)findViewById(R.id.edit);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checknull = et1.getText().toString();
                if (checknull.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter text", Toast.LENGTH_LONG).show();
                }
                else {
                    final TableRow row = new TableRow(MainActivity.this);
                    TextView txt = new TextView(MainActivity.this);
                    txt.setText(et1.getText());
                    Button btn = new Button(MainActivity.this);
                    btn.setText("Remove");
                    row.addView(txt);
                    row.addView(btn);
                    final TableLayout table = (TableLayout) findViewById(R.id.table); //(TableLayout) findViewById(R.id.table);
                    table.addView(row, 0);
                    et1.setText("");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            table.removeView(row);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
