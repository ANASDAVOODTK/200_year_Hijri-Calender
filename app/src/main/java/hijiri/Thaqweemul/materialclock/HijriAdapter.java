package hijiri.Thaqweemul.materialclock;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        private TextView fq,fm,lq;
        ImageView moonPhase;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
            hjdate = (TextView) itemView.findViewById(R.id.hjdate);
            ggdate = (TextView) itemView.findViewById(R.id.ggdate);
            fq = (TextView) itemView.findViewById(R.id.fq);
            fm = (TextView) itemView.findViewById(R.id.fm);
            lq = (TextView) itemView.findViewById(R.id.lq);
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

                itemViewHolder.day.setText(getDay(hijiriModel.getGgdate()));
                itemViewHolder.hjdate.setText(hijiriModel.getHijridate()+"-"+hijiriModel.getMonth()+"-"+hijiriModel.getYear());
                itemViewHolder.ggdate.setText(hijiriModel.getGgdate());
                itemViewHolder.fq.setText("First Quarter\n"+hijiriModel.getFq());
                itemViewHolder.fm.setText("Full Moon\n"+hijiriModel.getFm());
                itemViewHolder.lq.setText("Last Quarter\n"+hijiriModel.getLq());
                itemViewHolder.moonPhase.setImageResource(context.getResources().getIdentifier("a"+hijiriModel.getHijridate(), "drawable", context.getPackageName()));



        }

    }


    public String getDay(String dateValue)
    {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = inFormat.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String week = outFormat.format(date);
        return week;
    }


    public void getItemPosition() {
        int a;
        for (int i = 0; i < listRecyclerItem.size(); i++) {
            if (listRecyclerItem.get(i).getGgdate().equals("2022-8-17")) {
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
