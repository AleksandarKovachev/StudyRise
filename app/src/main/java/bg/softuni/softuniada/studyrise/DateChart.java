package bg.softuni.softuniada.studyrise;

public class DateChart {

    int year;
    int month;
    int day;
    DateType type;

    public DateChart(int year, int month, int day, DateType type) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public DateType getType() {
        return type;
    }

    public void setType(DateType type) {
        this.type = type;
    }
}
