package org.kll.app.meromapv1.Manipulation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.kll.app.meromapv1.Database.DBManager;
import org.kll.app.meromapv1.Database.DatabaseHelper;
import org.kll.app.meromapv1.FrontActivity.BaseActivity;
import org.kll.app.meromapv1.R;
import org.w3c.dom.Text;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */

public class ListActivity extends BaseActivity{

    private DBManager dbManager;

    private ListView listView;

    private android.support.v4.widget.SimpleCursorAdapter adapter;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.DESC, DatabaseHelper.CONTACT};

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.conta};


    @Override
    protected boolean useDrawerToggle() {
        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_empty_list);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.list_activity, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView descTextView = (TextView) view.findViewById(R.id.desc);
                TextView contTextView = (TextView) view.findViewById(R.id.conta);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String cont = contTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyActivity.class);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("name", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("cont", cont);

                startActivity(modify_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {

            Intent add_mem = new Intent(this, DetailInfo.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }


}