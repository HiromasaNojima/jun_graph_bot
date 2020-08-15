
package app.juntrack.youtube.http.response.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageInfo {

    @SerializedName("totalResults")
    @Expose
    private Long totalResults;
    @SerializedName("resultsPerPage")
    @Expose
    private Long resultsPerPage;

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Long getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Long resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

}
