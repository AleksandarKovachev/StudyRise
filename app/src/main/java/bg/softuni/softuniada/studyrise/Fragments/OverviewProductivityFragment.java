package bg.softuni.softuniada.studyrise.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.MainActivity;
import bg.softuni.softuniada.studyrise.Adapters.HistoryAdapter;
import bg.softuni.softuniada.studyrise.DateChart;
import bg.softuni.softuniada.studyrise.DateType;
import bg.softuni.softuniada.studyrise.History;
import bg.softuni.softuniada.studyrise.Points;
import bg.softuni.softuniada.studyrise.Profile;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class OverviewProductivityFragment extends Fragment implements OnChartValueSelectedListener {

    public static Profile profile;
    private String programId;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<History> list;

    private int dailyPoints = 0;

    private PieChart pieChart;

    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_overview_productivity, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Program", 0);
        programId = sharedPreferences.getString("program", null);


        SharedPreferences sharedPreferencesPoints = getContext().getSharedPreferences("ProfilePoints", 0);
        String points = sharedPreferencesPoints.getString("points", null);

        if (profile == null) {
            profile = new Profile();
            profile.setId(Long.parseLong(programId));
            profile.setPersonalPoints("0", getContext(), "");
        }

        profile.setId(Long.parseLong(programId));

        if (points != null) {
            MainActivity.setText(points.toString());
            profile.setPersonalPoints(points, getContext(), "");

            list = new ArrayList<>();

            DBPref pref = new DBPref(getContext());
            Cursor c = pref.getVals("history", programId);

            if (c.moveToFirst()) {
                do {
                    History history = new History();
                    history.setType(c.getString(c.getColumnIndex("type")));
                    history.setName(c.getString(c.getColumnIndex("name")));
                    history.setDate(c.getString(c.getColumnIndex("date")));
                    history.setPoints(c.getString(c.getColumnIndex("points")));
                    list.add(history);
                } while (c.moveToNext());
            }
            c.close();
            pref.close();

            lineChart = (LineChart) root.findViewById(R.id.lineChart1);

            pieChart = (PieChart) root.findViewById(R.id.pie_chart);

            pieChart();

            recyclerView = (RecyclerView) root.findViewById(R.id.history_recycler_view);

            adapter = new HistoryAdapter(getContext(), list, recyclerView);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.clearFocus();

            pieChart.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                    return true;
                }
            });
        }
        return root;
    }

    private void setData(int range, int points) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        if (range == 0) {
            entries.add(new PieEntry(100, "днес\n" + points));
        } else {
            entries.add(new PieEntry(points, "днес"));
            entries.add(new PieEntry(range - points, "остават"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSelectionShift(4f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(Color.RED);

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        if (range == 0)
            data.setValueFormatter(new PercentFormatter());
        else
            data.setValueFormatter(new DefaultValueFormatter(0));

        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Общо:\n" + profile.getPersonalPoints());
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 5, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 6, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(5f), 6, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 6, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Points points) {
        dailyPoints += points.getPoints();
        pieChart.setCenterText(generateCenterSpannableText());
        if (dailyPoints < 100)
            setData(100, dailyPoints);
        else
            setData(0, dailyPoints);
        pieChart.animateY(1400, Easing.EasingOption.EaseInExpo);
        pieChart.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DateChart dateChart) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(dateChart.getYear(), dateChart.getMonth(), dateChart.getDay());

        Calendar historyCalendar = Calendar.getInstance();
        historyCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        List<History> data = new LinkedList<>();
        switch (dateChart.getType()) {
            case DAY:
                for (History history : list) {
                    historyCalendar.setTime(new Date(history.getDate()));
                    if (historyCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        data.add(history);
                    }
                }
                break;
            case WEEK:
                for (History history : list) {
                    historyCalendar.setTime(new Date(history.getDate()));
                    if (historyCalendar.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR)) {
                        data.add(history);
                    }
                }
                break;
            case MONTH:
                for (History history : list) {
                    historyCalendar.setTime(new Date(history.getDate()));
                    if (historyCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                        data.add(history);
                    }
                }
                break;
            case YEAR:
                for (History history : list) {
                    historyCalendar.setTime(new Date(history.getDate()));
                    if (historyCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                        data.add(history);
                    }
                }
                break;
            case DAILY:
                pieChart();
                break;
        }
        if (data.size() > 0)
            lineChart(data);
        else if (dateChart.getType() != DateType.DAILY)
            Toast.makeText(getContext(), "Няма активност за посочената дата.", Toast.LENGTH_LONG).show();
    }

    private void lineChart(List<History> data) {
        pieChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        lineChart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        ArrayList<Entry> values = new ArrayList<>();

        int max = 0;
        int min = 0;

        Collections.reverse(data);

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getType().equals("Activ")) {
                if (max < Integer.parseInt(data.get(i).getPoints())) {
                    max = Integer.parseInt(data.get(i).getPoints());
                }
                values.add(new Entry(i, Integer.parseInt(data.get(i).getPoints())));
            } else {
                if (min > Integer.parseInt(data.get(i).getPoints()) * -1) {
                    min = Integer.parseInt(data.get(i).getPoints()) * -1;
                }
                values.add(new Entry(i, Integer.parseInt(data.get(i).getPoints()) * -1));
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "Активност");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(getResources().getColor(R.color.green));
        set1.setValueTextColor(getResources().getColor(R.color.green));
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(true);
        set1.setDrawValues(true);
        set1.setFillAlpha(65);
        set1.setFillColor(getResources().getColor(R.color.green));
        set1.setHighLightColor(getResources().getColor(R.color.green));
        set1.setDrawCircleHole(true);

        // create a data object with the datasets
        LineData lineData = new LineData(set1);
        lineData.setValueTextColor(Color.WHITE);
        lineData.setValueTextSize(9f);

        // set data
        lineChart.setData(lineData);

        lineChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM YYYY");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date());
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(min - 20);
        leftAxis.setAxisMaximum(max + 20);
        leftAxis.setYOffset(-20f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        lineChart.animateXY(3000, 2000);

        lineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lineChart.animateXY(3000, 2000);
                return true;
            }
        });
    }

    private void pieChart() {
        lineChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorPrimary));

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        dailyPoints = 0;

        for (History history : list) {
            if (history.getType().equals("Activ")) {
                String[] dateArray = history.getDate().split(" ");
                String date = dateArray[2] + " " + dateArray[3] + " " + dateArray[4];
                String datePattern = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                String currentDate = dateFormat.format(new Date(System.currentTimeMillis()));
                if (date.equals(currentDate)) {
                    dailyPoints += Integer.parseInt(history.getPoints());
                }
            }
        }

        if (dailyPoints < 100)
            setData(100, dailyPoints);
        else
            setData(0, dailyPoints);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }
}