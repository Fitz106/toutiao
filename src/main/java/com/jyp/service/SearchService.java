package com.jyp.service;

import com.jyp.model.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/toutiao";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String NEWS_TITLE_FIELD = "news_title";

    public List<News> searchNews(String keyword, int offset, int count,
                                 String hlPre, String hlPos) throws Exception {
        List<News> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl", NEWS_TITLE_FIELD);
        QueryResponse response = client.query(query);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            News q = new News();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(NEWS_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(NEWS_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        return questionList;
    }

    public boolean indexNews(int qid, String title) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(NEWS_TITLE_FIELD, title);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }

}
