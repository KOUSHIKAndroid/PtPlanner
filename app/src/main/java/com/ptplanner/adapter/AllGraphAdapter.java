package com.ptplanner.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import com.ptplanner.R;
import com.ptplanner.customgraphview.LineView;
import com.ptplanner.datatype.ClientAllGraphDataType;
import com.ptplanner.datatype.ClientAllGraphPointDataType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ltp on 05/08/15.
 */
public class AllGraphAdapter extends ArrayAdapter<ClientAllGraphDataType> {

    Context context;
    LayoutInflater inflator;
    FragmentManager fragmentManager;
    ViewHolder holder;
    ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeLinkedList;

    public AllGraphAdapter(Context context,
                           int resource,
                           ArrayList<ClientAllGraphDataType> clientAllGraphDataTypeLinkedList) {
        super(context, resource, clientAllGraphDataTypeLinkedList);

        this.context = context;
        this.clientAllGraphDataTypeLinkedList = clientAllGraphDataTypeLinkedList;
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflator.inflate(R.layout.allgraph_listitem, parent, false);

            holder = new ViewHolder();

            holder.lineView = (LineView) convertView.findViewById(R.id.line_view);
            holder.horizontalScrollView = (HorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView);

//            holder.horizontalScrollView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    holder.horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//                }
//            }, 500);

            holder.graphBar = (ProgressBar) convertView.findViewById(R.id.graph_pbar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArrayList<String> test = new ArrayList<String>();
        for (int i = 0; i < clientAllGraphDataTypeLinkedList.get(position).getClientAllGraphPointDataTypeArrayList().size(); i++) {
            test.add(
                    changeDateFormat(
                            clientAllGraphDataTypeLinkedList.get(position).getClientAllGraphPointDataTypeArrayList().get(i).getX_axis_point()
                    )
            );
        }

        holder.lineView.setBottomTextList(test);
        holder.lineView.setDrawDotLine(true);
        holder.lineView.setShowPopup(LineView.SHOW_POPUPS_All);

        randomSet(holder.lineView, clientAllGraphDataTypeLinkedList.get(position).getClientAllGraphPointDataTypeArrayList());

        return convertView;
    }

    protected class ViewHolder {
        LineView lineView;
        HorizontalScrollView horizontalScrollView;
        ProgressBar graphBar;
        // ViewTreeObserver viewTreeObserver;
    }

    private void randomSet(LineView lineView, ArrayList<ClientAllGraphPointDataType> clientAllGraphPointDataTypeArrayList) {
        ArrayList<Integer> dataList = new ArrayList<Integer>();
        for (int i = 0; i < clientAllGraphPointDataTypeArrayList.size(); i++) {
            dataList.add(
                    (int) Float.parseFloat(clientAllGraphPointDataTypeArrayList.get(i).getY_axis_point())
            );
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
        dataLists.add(dataList);

        lineView.setDataList(dataLists);
    }

    public String changeDateFormat(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM");
        String finalDate = timeFormat.format(myDate);

        return finalDate;
    }

}
