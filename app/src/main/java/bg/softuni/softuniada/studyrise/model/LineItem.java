package bg.softuni.softuniada.studyrise.Model;

/**
 * 页面描述：
 * <p>
 * Created by ditclear on 2016/12/17.
 */

public class LineItem {

    private String content;
    private boolean isTitle;
    private String points;

    public LineItem(String content, boolean isTitle, String points) {
        this.content = content;
        this.isTitle = isTitle;
        this.points = points;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
