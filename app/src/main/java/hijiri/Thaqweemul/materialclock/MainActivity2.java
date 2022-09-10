package hijiri.Thaqweemul.materialclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity2 extends Fragment {



    public MainActivity2() {
        // Required empty public constructor
    }

    LottieAnimationView loader;
    ImageView refresh;
    private RecyclerView mRecyclerView;
    private List<HijiriModel> viewItems = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = "MainActivity2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        View v = getView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        refresh = v.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ApplicationClass globalVariable = ((ApplicationClass)getActivity().getApplicationContext());
                mRecyclerView.smoothScrollToPosition(globalVariable.getPosition1());

            }
        });
        loader=v.findViewById(R.id.loading);

        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                mRecyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                mAdapter = new HijriAdapter(getActivity(), viewItems);
                mRecyclerView.setAdapter(mAdapter);


                try {
                    addItemsFromJSON();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);





    }



    private void addItemsFromJSON() {
        try {

            String jsonDataString = readJSONDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i=0; i<jsonArray.length(); ++i) {

                JSONObject itemObj = jsonArray.getJSONObject(i);

                String hijridate = itemObj.getString("hijridate");
                String ggdate = itemObj.getString("ggdate");
                String month = itemObj.getString("month");
                String year = itemObj.getString("year");
                String time = itemObj.getString("time");
                String fq = itemObj.getString("fq");
                String fm = itemObj.getString("fm");
                String lq = itemObj.getString("lq");

                HijiriModel holidays = new HijiriModel(hijridate,ggdate,month,year,time,fq,fm,lq);
                viewItems.add(holidays);
            }

        } catch (JSONException | IOException e) {
            Log.d(TAG, "addItemsFromJSON: ", e);
        }



        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final ApplicationClass globalVariable = ((ApplicationClass)getActivity().getApplicationContext());
                Log.d("testingggg", String.valueOf(globalVariable.getPosition1()));
               mRecyclerView.getLayoutManager().scrollToPosition(globalVariable.getPosition1()-10);
                final Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.GONE);
                        refresh.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mRecyclerView.smoothScrollToPosition(globalVariable.getPosition1());
                    }
                }, 200);
            }
        }, 1000);
    }

    private String readJSONDataFromFile() throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(R.raw.hrdat);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }



}