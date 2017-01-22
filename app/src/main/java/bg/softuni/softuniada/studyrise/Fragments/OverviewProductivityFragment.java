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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.MainActivity;
import bg.softuni.softuniada.studyrise.Adapters.HistoryAdapter;
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

    private PieChart mChart;

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

            mChart = (PieChart) root.findViewById(R.id.pie_chart);
            mChart.setUsePercentValues(true);
            mChart.getDescription().setEnabled(false);
            mChart.setExtraOffsets(5, 10, 5, 5);

            mChart.setDragDecelerationFrictionCoef(0.95f);

            mChart.setCenterText(generateCenterSpannableText());

            mChart.setDrawHoleEnabled(true);
            mChart.setHoleColor(getResources().getColor(R.color.colorPrimary));

            mChart.setTransparentCircleColor(Color.WHITE);
            mChart.setTransparentCircleAlpha(110);

            mChart.setHoleRadius(58f);
            mChart.setTransparentCircleRadius(61f);

            mChart.setDrawCenterText(true);

            mChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            mChart.setRotationEnabled(true);
            mChart.setHighlightPerTapEnabled(true);

            // add a selection listener
            mChart.setOnChartValueSelectedListener(this);

            dailyPoints = 0;

            for (History history : list) {
                if (history.getType().equals("Activ")) {
                    String[] dateArray = history.getDate().split(" ");
                    String date = dateArray[2] + " " + dateArray[3] + " " + dateArray[4];
                    String datePattern = "dd MMM yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                    String currentDate = dateFormat.format(new Date(System.currentTimeMillis()));
                    if (date.equals(currentDate)) {
                        dailyPoints += Float.parseFloat(history.getPoints());
                    }
                }
            }

            if (dailyPoints < 100)
                setData(100, dailyPoints);
            else
                setData(0, dailyPoints);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            // entry label styling
            mChart.setEntryLabelColor(Color.WHITE);
            mChart.setEntryLabelTextSize(12f);

            recyclerView = (RecyclerView) root.findViewById(R.id.history_recycler_view);

            adapter = new HistoryAdapter(getContext(), list, recyclerView);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.clearFocus();

            mChart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartLongPressed(MotionEvent me) {

                }

                @Override
                public void onChartDoubleTapped(MotionEvent me) {

                }

                @Override
                public void onChartSingleTapped(MotionEvent me) {
                    mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                }

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

                }

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

                }

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {

                }
            });
        }
        return root;
    }

    private void setData(float range, float points) {

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
            data.setValueFormatter(new LargeValueFormatter());

        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
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
        mChart.setCenterText(generateCenterSpannableText());
        if (dailyPoints < 100)
            setData(100, dailyPoints);
        else
            setData(0, dailyPoints);
        mChart.animateY(1400, Easing.EasingOption.EaseInExpo);
        mChart.invalidate();
    }
}