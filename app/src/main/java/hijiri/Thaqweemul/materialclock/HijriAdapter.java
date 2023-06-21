package hijiri.Thaqweemul.materialclock;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class HijriAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE = 1;
    private final Context context;
    private final List<HijiriModel> listRecyclerItem;
    int stopLoop = 0;

    public HijriAdapter(Context context, List<HijiriModel> listRecyclerItem) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView day;
        private TextView hjdate;
        private TextView ggdate;
        private TextView fq,fm,lq,lvc;
        ImageView moonPhase;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
            hjdate = (TextView) itemView.findViewById(R.id.hjdate);
            ggdate = (TextView) itemView.findViewById(R.id.ggdate);
            fq = (TextView) itemView.findViewById(R.id.fq);
            fm = (TextView) itemView.findViewById(R.id.fm);
            lq = (TextView) itemView.findViewById(R.id.lq);
            lvc = (TextView) itemView.findViewById(R.id.lvc);
            moonPhase = (ImageView) itemView.findViewById(R.id.moonPhase);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (stopLoop==0)
        {
          getItemPosition();
        }

        switch (i) {
            case TYPE:

            default:

                View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_view, viewGroup, false);

                return new ItemViewHolder((layoutView));
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int viewType = getItemViewType(i);

        switch (viewType) {
            case TYPE:
            default:

                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                HijiriModel hijiriModel = (HijiriModel) listRecyclerItem.get(i);

                //itemViewHolder.day.setText(getDay(hijiriModel.getGgdate()));
                itemViewHolder.day.setText(hijiriModel.getHijridate());
                itemViewHolder.hjdate.setText(hijiriModel.getMonth());
                itemViewHolder.ggdate.setText(hijiriModel.getGgdate());
                itemViewHolder.fq.setText(hijiriModel.getFq());
                itemViewHolder.fm.setText(hijiriModel.getFm());
                itemViewHolder.lq.setText(hijiriModel.getLq());
                itemViewHolder.moonPhase.setImageResource(context.getResources().getIdentifier("a"+hijiriModel.getHijridate(), "drawable", context.getPackageName()));

                try {
                    itemViewHolder.lvc.setText(getDay(hijiriModel.getLq()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

        }

    }


    public String getDay(String dateValue) throws ParseException {
        String data = "";

        String sDate = dateValue;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = dateFormat.parse(sDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        data = dateFormat.format(calendar.getTime());
        return data;
    }


    public void getItemPosition() {
        int a;
        String date = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(new Date());
        String date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        for (int i = 44320; i < 72645; i++) {
            if (listRecyclerItem.get(i).getGgdate().equals(date)) {
                a= i;
                final ApplicationClass applicationClass = (ApplicationClass) context.getApplicationContext();
                applicationClass.setPosition(a);
                stopLoop=1;
            }
            else if (listRecyclerItem.get(i).getGgdate().equals(date1)) {
                a= i;
                final ApplicationClass applicationClass = (ApplicationClass) context.getApplicationContext();
                applicationClass.setPosition(a);
                stopLoop=1;
            }
        }


    }


    @Override
    public int getItemCount() {

        return listRecyclerItem.size();

    }




}
