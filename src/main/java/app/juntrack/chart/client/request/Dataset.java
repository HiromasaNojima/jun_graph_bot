
package app.juntrack.chart.client.request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dataset {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("data")
    @Expose
    private List<Integer> data = null;
    @SerializedName("fill")
    @Expose
    private Boolean fill;
    @SerializedName("borderColor")
    @Expose
    private String borderColor;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

}
