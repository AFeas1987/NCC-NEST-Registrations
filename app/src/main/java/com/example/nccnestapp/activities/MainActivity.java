package com.example.nccnestapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nccnestapp.R;
import com.example.nccnestapp.adapters.ListAdapter;
import com.example.nccnestapp.utilities.ListElement;

import java.util.ArrayList;
import java.util.List;

import static com.example.nccnestapp.utilities.Constants.DISPLAY_NAME;
import static com.example.nccnestapp.utilities.Constants.DISPLAY_SUBTEXT;
import static com.example.nccnestapp.utilities.Constants.FORMS_URI;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isCollectAppInstalled()) {
            finish();
            Toast.makeText(this, getString(R.string.collect_app_not_installed), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(getListFromCursor(getCursor()), new ListAdapter.OnItemClickListener() {
            @Override public void onItemClick(ListElement item) {
                Intent i = new Intent(Intent.ACTION_EDIT, Uri.parse(FORMS_URI + "/" + item.getId()));
                startActivityIfAvailable(i);
            }
        }));

        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        if (getCursor().getCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    private Cursor getCursor() {
        return getContentResolver().query(Uri.parse(FORMS_URI), null, null, null, null);
    }

    private List<ListElement> getListFromCursor(Cursor cursor) {
        List<ListElement> listElements = new ArrayList<>();

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                    String text1 = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    String text2 = cursor.getString(cursor.getColumnIndex(DISPLAY_SUBTEXT));
                    listElements.add(new ListElement(id, text1, text2));
                }
            } finally {
                cursor.close();
            }
        }

        return listElements;
    }


    public void mainButtonClick(View v){

    }
}
