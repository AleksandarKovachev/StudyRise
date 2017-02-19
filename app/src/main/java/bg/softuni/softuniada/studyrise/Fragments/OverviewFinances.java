package bg.softuni.softuniada.studyrise.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.softuni.softuniada.studyrise.Adapters.SpinnerAdapterFinance;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class OverviewFinances extends Fragment implements ActionBar.OnNavigationListener {

    protected BarChart mChart;

    private Cursor c;
    private DBPref pref;
    private List<Finance> listFinances;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View root = inflater.inflate(R.layout.finance_overview_fragment, container, false);

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_finance);

        spinner.setAdapter(new SpinnerAdapterFinance(getContext(), R.layout.finance_row_spinner, getResources().getStringArray(R.array.finance)));

        mChart = (BarChart) root.findViewById(R.id.bar_chart);
        mChart.setBackgroundColor(getResources().getColor(R.color.icons));
        mChart.setExtraTopOffset(-30f);
        mChart.setExtraBottomOffset(10f);
        mChart.setExtraLeftOffset(70f);
        mChart.setExtraRightOffset(70f);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(7);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(10f);
        left.setSpaceBottom(10f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        listFinances = new ArrayList<>();

        pref = new DBPref(getContext());
        c = pref.getVals("profit_expense", "");
        getVals();


        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();

        float count = 0f;

        for (Finance finance : listFinances) {
            String[] dateArray = finance.getDate().split(" ");
            String date = dateArray[2] + " " + dateArray[3] + " " + dateArray[4];
            String datePattern = "dd MMM yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            String currentDate = dateFormat.format(new Date(System.currentTimeMillis()));
            if (date.equals(currentDate)) {
                if (finance.getType().equals("Разход"))
                    data.add(new Data(count, (float) (finance.getValue() * -1), finance.getDate().substring(0, 9)));
                else if (finance.getType().equals("Приход"))
                    data.add(new Data(count, (float) finance.getValue(), finance.getDate().substring(0, 9)));
                count++;
            }
        }
        TextView textView = (TextView) root.findViewById(R.id.null_data_chart);
        if (data.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);

            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return data.get(Math.min(Math.max((int) value, 0), data.size() - 1)).xAxisValue;
                }
            });

            mChart.animateXY(3000, 3000);

            setData(data);
        }
        return root;
    }


    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();

        int green = getResources().getColor(R.color.green);
        int red = Color.RED;

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(green);
            else
                colors.add(red);
        }

        BarDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            data.setValueFormatter(new ValueFormatter());
            data.setBarWidth(0.8f);

            mChart.setData(data);
            mChart.invalidate();
        }
    }

    public void getVals() {

        if (c.moveToFirst()) {
            do {
                Finance finance = new Finance();
                finance.setType(c.getString(c.getColumnIndex("type")));
                finance.setName(c.getString(c.getColumnIndex("name")));
                finance.setValue(c.getDouble(c.getColumnIndex("value")));
                finance.setDate(c.getString(c.getColumnIndex("date")));
                listFinances.add(finance);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();
    }

    private class Data {

        public String xAxisValue;
        public float yValue;
        public float xValue;

        public Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    private class ValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("######.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    public OverviewFinances() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            pref = new DBPref(getContext());
            c = pref.getVals("profit_expense", "");
            getVals();

            Finance finance = (Finance) data.getSerializableExtra("finance");

            if (finance.getType().equals("Приход")) {
                Profit.listFinances.add(0, finance);
                Profit.profitAdapter.notifyDataSetChanged();
                Profit.recyclerView.invalidate();
            } else {
                Expense.listFinances.add(0, finance);
                Expense.profitAdapter.notifyDataSetChanged();
                Profit.recyclerView.invalidate();
            }
        }
    }
}
