
package app.juntrack.youtube.http.response.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStreamingDetails {

    @SerializedName("actualStartTime")
    @Expose
    private String actualStartTime;
    @SerializedName("actualEndTime")
    @Expose
    private String actualEndTime;
    @SerializedName("scheduledStartTime")
    @Expose
    private String scheduledStartTime;
    @SerializedName("scheduledEndTime")
    @Expose
    private String scheduledEndTime;
    @SerializedName("concurrentViewers")
    @Expose
    private String concurrentViewers;
    @SerializedName("activeLiveChatId")
    @Expose
    private String activeLiveChatId;

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(String scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public String getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(String scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public String getConcurrentViewers() {
        return concurrentViewers;
    }

    public void setConcurrentViewers(String concurrentViewers) {
        this.concurrentViewers = concurrentViewers;
    }

    public String getActiveLiveChatId() {
        return activeLiveChatId;
    }

    public void setActiveLiveChatId(String activeLiveChatId) {
        this.activeLiveChatId = activeLiveChatId;
    }

}
