package com.awesome.medifofo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.awesome.medifofo.RecyclerItemClickListener;
import com.awesome.medifofo.adapter.SymptomListAdapter;
import com.awesome.medifofo.model.SymptomData;

import java.util.ArrayList;
import java.util.List;

import static com.awesome.medifofo.model.SymptomData.symptomList;

public class SymptomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SymptomListAdapter adapter;
    private int gridViewPosition = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);


        Intent intent = getIntent();
        gridViewPosition = intent.getExtras().getInt("POSITION"); // Get gridView's position

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initRecyclerView(gridViewPosition);
    }


    private void initRecyclerView(int gridViewPosition) {
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        switch (gridViewPosition) {
            case 0:
                adapter = new SymptomListAdapter(SymptomData.getListData(), this);
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (position == 2 || position == 4) {
                                    Toast.makeText(getApplicationContext(), "There is no question in this symptom", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialogShow(position);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                        })
                );
                break;
            case 1:
                adapter = new SymptomListAdapter(SymptomData.getListData2(), this);
                break;
            case 2:
                adapter = new SymptomListAdapter(SymptomData.getListData3(), this);
                break;
            case 3:
                adapter = new SymptomListAdapter(SymptomData.getListData4(), this);
                break;
            case 4:
                adapter = new SymptomListAdapter(SymptomData.getListData5(), this);
                break;
            case 5:
                adapter = new SymptomListAdapter(SymptomData.getListData6(), this);
                break;
            case 6:
                adapter = new SymptomListAdapter(SymptomData.getListData7(), this);
                break;
            case 7:
                adapter = new SymptomListAdapter(SymptomData.getListData8(), this);
                break;
            case 8:
                adapter = new SymptomListAdapter(SymptomData.getListData9(), this);
                break;
            case 9:
                adapter = new SymptomListAdapter(SymptomData.getListData10(), this);
                break;
            case 10:
                adapter = new SymptomListAdapter(SymptomData.getListData11(), this);
                break;
            case 11:
                adapter = new SymptomListAdapter(SymptomData.getListData12(), this);
                break;
            case 12:
                adapter = new SymptomListAdapter(SymptomData.getListData13(), this);
                break;
            case 13:
                adapter = new SymptomListAdapter(SymptomData.getListData14(), this);
                break;
            case 14:
                adapter = new SymptomListAdapter(SymptomData.getListData15(), this);
                break;
            case 15:
                adapter = new SymptomListAdapter(SymptomData.getListData16(), this);
                break;
            case 16:
                adapter = new SymptomListAdapter(SymptomData.getListData17(), this);
                break;
            case 17:
                adapter = new SymptomListAdapter(SymptomData.getListData18(), this);
                break;
            case 18:
                adapter = new SymptomListAdapter(SymptomData.getListData19(), this);
                break;
            case 19:
                adapter = new SymptomListAdapter(SymptomData.getListData20(), this);
                break;
            case 20:
                adapter = new SymptomListAdapter(SymptomData.getListData21(), this);
                break;
            case 21:
                adapter = new SymptomListAdapter(SymptomData.getListData22(), this);
                break;
            default:
        }

        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_symptom_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                adapter.filter(text);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialogShow(int position) {
        final List<String> listItems = new ArrayList<>();

        switch (position) {
            case 0:
                listItems.add("mild");
                listItems.add("moderate");
                listItems.add("severe");
                listItems.add("None of above");
                break;
            case 1:
                listItems.add("or made worse by emotional stress");
                listItems.add("or made worse by caffeine");
                listItems.add("None of above");
                break;
            case 3:
                listItems.add("receding hairline");
                listItems.add("None of above");
                break;
            default:
                break;
        }

        final CharSequence[] items = listItems.toArray(new String[listItems.size()]);
        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(symptomList[gridViewPosition][position]);
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String msg = "";

                        if (!SelectedItems.isEmpty()) {
                            int index = (int) SelectedItems.get(0);
                            msg = listItems.get(index);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Items Selected.\n" + msg, Toast.LENGTH_LONG)
                                .show();
                    }
                });

        builder.setNeutralButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        Toast.makeText(getApplicationContext(), "POSITION: " + position, Toast.LENGTH_SHORT).show();
        builder.show();
    }

}

