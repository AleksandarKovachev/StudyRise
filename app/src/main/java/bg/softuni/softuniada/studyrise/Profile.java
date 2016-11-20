package bg.softuni.softuniada.studyrise;

import android.content.Context;
import android.content.SharedPreferences;

public class Profile {
    private String personalPoints;
    private String dailyGoals;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public String getPersonalPoints() {
        return personalPoints;
    }

    public void setPersonalPoints(String personalPoints, Context context, String string) {
        if (getPersonalPoints() == null)
            this.personalPoints = personalPoints;
        else {

            int a = Integer.parseInt(personalPoints);
            int b = Integer.parseInt(getPersonalPoints());

            if (string.equals("activ")) {
                this.personalPoints = a + b + "";
                savePoints(context);
            } else if (string.equals("achievement")) {
                this.personalPoints = b - a + "";
                savePoints(context);
            } else
                this.personalPoints = personalPoints;


        }
    }

    private void savePoints(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ProfilePoints", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("points", this.personalPoints + "");
        editor.commit();
    }

    public String getDailyGoals() {
        return dailyGoals;
    }

    public void setDailyGoals(String dailyGoals) {
        this.dailyGoals = dailyGoals;
    }

}
