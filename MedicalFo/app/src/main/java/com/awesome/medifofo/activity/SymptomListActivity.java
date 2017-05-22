package com.awesome.medifofo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.awesome.medifofo.R;
import com.awesome.medifofo.RecyclerItemClickListener;
import com.awesome.medifofo.adapter.SymptomListAdapter;
import com.awesome.medifofo.model.ListItem;
import com.awesome.medifofo.model.SymptomData;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SymptomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SymptomListAdapter adapter;
    private int gridViewPosition = 0;
    private Context context;
    private String partname[] = {
            "head", "face", "eye", "nose", "ear", "mouth", "jaw", "neck", "chest", " belly", "back", "spine", "arms", "elbow", "hands", "finger", "legs", "hip", "ankle",
            "foot", "man", "woman", "digestive", "respiratory", "heart"
    };

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
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
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
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 2:
                adapter = new SymptomListAdapter(SymptomData.getListData3(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 3:
                adapter = new SymptomListAdapter(SymptomData.getListData4(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 4:
                adapter = new SymptomListAdapter(SymptomData.getListData5(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 5:
                adapter = new SymptomListAdapter(SymptomData.getListData6(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 6:
                adapter = new SymptomListAdapter(SymptomData.getListData7(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 7:
                adapter = new SymptomListAdapter(SymptomData.getListData8(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 8:
                adapter = new SymptomListAdapter(SymptomData.getListData9(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 9:
                adapter = new SymptomListAdapter(SymptomData.getListData10(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 10:
                adapter = new SymptomListAdapter(SymptomData.getListData11(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 11:
                adapter = new SymptomListAdapter(SymptomData.getListData12(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 12:
                adapter = new SymptomListAdapter(SymptomData.getListData13(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 13:
                adapter = new SymptomListAdapter(SymptomData.getListData14(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 14:
                adapter = new SymptomListAdapter(SymptomData.getListData15(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 15:
                adapter = new SymptomListAdapter(SymptomData.getListData16(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 16:
                adapter = new SymptomListAdapter(SymptomData.getListData17(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 17:
                adapter = new SymptomListAdapter(SymptomData.getListData18(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 18:
                adapter = new SymptomListAdapter(SymptomData.getListData19(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 19:
                adapter = new SymptomListAdapter(SymptomData.getListData20(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 20:
                adapter = new SymptomListAdapter(SymptomData.getListData21(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
                break;
            case 21:
                adapter = new SymptomListAdapter(SymptomData.getListData22(), this);
                new SymptomListActivity.HttpAsyncTask().execute("http://igrus.mireene.com/medifofo/medi_symptom_list.php");
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

        //builder.setTitle(symptomList[gridViewPosition][position]);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(SymptomListActivity.this, "Fail. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
            try {
                Log.d("jsoncheck", result);
                JSONArray jobj = new JSONArray(result);
                List<ListItem> data = new ArrayList<>();
                for (int i = 0; i < jobj.length(); i++) {
                    ListItem item = new ListItem();
                    item.setTitle(jobj.getJSONObject(i).getString("symptomname"));
                    data.add(item);
                    adapter = new SymptomListAdapter(data, context);
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
            nameValuePair.add(new BasicNameValuePair("partname", partname[gridViewPosition]));

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));

            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            //httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            // Log.d("InputStream", e.getLocalizedMessage());
        }
        // Log.d("http", result);

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        Log.d("Ddd", inputStream.toString());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();

        if (!result.contains("]"))
            result += "]";
        return result;
    }

}

