package net.sppan.jfinalblog.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.sppan.jfinalblog.service.BlogService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Current
public class LuceneSearcher implements ISearcher {

    static Analyzer analyzer = null;//分词器
    public static Log log = Log.getLog(LuceneSearcher.class);

    public static String INDEX_PATH;

    private static Directory directory;

    static {
        INDEX_PATH = "C://lucene/";
        if (INDEX_PATH == null) {
            INDEX_PATH = "~/indexes/";
        }
    }


    @Override
    public void init() {
        try {
            if(log.isWarnEnabled()) {
                log.warn("init lucene config");
            }
            File indexDir = new File(INDEX_PATH);
            if (!indexDir.exists()) {
                indexDir.mkdirs();
            }
            directory = NIOFSDirectory.open(indexDir);
        } catch (IOException e) {
            log.error("init lucene path error",e);
        }
    }
    public static ReentrantLock lock = new ReentrantLock();
    
    public void getCurrentLock() throws InterruptedException {
        boolean _lock = lock.tryLock(300,TimeUnit.SECONDS);
        while(!_lock){
            _lock = lock.tryLock(300,TimeUnit.SECONDS);
        }
    }
    @Override
    public void addBean(SearcherBean bean) {
        IndexWriter writer = null;
        try {
            getCurrentLock();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, new IKAnalyzer());
            writer = new IndexWriter(directory, iwc);
            Document doc = createDoc(bean);
            writer.addDocument(doc);
        } catch (IOException e) {
            log.error("add bean to lucene error", e);
        } catch (InterruptedException e) {
            log.error("add bean to lucene error", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                log.error("close failed", e);
            }
            lock.unlock();
        }
    }

    @Override
    public void deleteBean(String beanId) {
        IndexWriter writer = null;
        try {
            getCurrentLock();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, new IKAnalyzer());
            writer = new IndexWriter(directory, iwc);
            writer.deleteDocuments(new Term("id", beanId));
        } catch (IOException e) {
            log.error("delete bean to lucene error,beanId:"+beanId,e);
        } catch (InterruptedException e) {
            log.error("delete bean to lucene error,beanId:"+beanId,e);
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                log.error("close failed", e);
            }
            lock.unlock();
        }
    }

    @Override
    public void updateBean(SearcherBean bean) {
        deleteBean(bean.getId());
        addBean(bean);

    }

    /**
     * 创建Doc
     * @param bean
     * @return
     */
    private Document createDoc(SearcherBean bean) {
        Document doc = new Document();
        doc.add(new StringField("id", bean.getId(), Field.Store.YES));
        doc.add(new TextField("title", bean.getTitle(), Field.Store.YES));
        doc.add(new TextField("summary", bean.getSummary(), Field.Store.YES));
        doc.add(new TextField("content", bean.getContent(), Field.Store.YES));
        
        doc.add(new StringField("authorName", bean.getAuthorName(), Field.Store.YES));
        doc.add(new IntField("views", bean.getViews(), Field.Store.YES));
        doc.add(new StringField("createdAt", DateTools.dateToString(bean.getCreateAt(), DateTools.Resolution.MILLISECOND), Field.Store.YES));
        
        return doc;
    }

    @Override
    public Page<SearcherBean> search(String keyword) {
        try {
            IndexReader aIndexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = null;
            searcher = new IndexSearcher(aIndexReader);
            Query query = getQuery(keyword);
            TopDocs topDocs = searcher.search(query, 50);
            List<SearcherBean> searcherBeans = getSearcherBeans(searcher, topDocs);
            Page<SearcherBean> searcherBeanPage = new Page<>(searcherBeans, 1, 10, 100, 1000);
            return searcherBeanPage;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *  转换为SearchBean
     * @param searcher
     * @param topDocs
     * @return
     * @throws IOException
     */
    private List<SearcherBean> getSearcherBeans(IndexSearcher searcher, TopDocs topDocs) throws IOException {
        List<SearcherBean> searcherBeans = new ArrayList<SearcherBean>();
        for (ScoreDoc item : topDocs.scoreDocs) {
            Document doc = searcher.doc(item.doc);
            SearcherBean searcherBean = new SearcherBean();
            searcherBean.setId(doc.get("id"));
            searcherBean.setTitle(doc.get("title"));
            searcherBean.setSummary(doc.get("summary"));
            searcherBean.setContent(doc.get("content"));
            
            searcherBean.setViews(Integer.parseInt(doc.get("views")));
            searcherBean.setAuthorName(doc.get("authorName"));
            try {
				searcherBean.setCreateAt(DateTools.stringToDate(doc.get("createAt")));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
            searcherBeans.add(searcherBean);
        }
        return searcherBeans;
    }

    /**
     * 获取Query 对象
     * @param keyword
     * @param module
     * @return
     */
    private Query getQuery(String keyword) {
        try {
            QueryParser queryParser1 = new QueryParser(Version.LUCENE_47, "content", new IKAnalyzer());
            Query termQuery1 = queryParser1.parse(keyword);
            
            QueryParser queryParser2 = new QueryParser(Version.LUCENE_47, "title", new IKAnalyzer());
            Query termQuery2 = queryParser2.parse(keyword);
            
            QueryParser queryParser3 = new QueryParser(Version.LUCENE_47, "summary", new IKAnalyzer());
            Query termQuery3 = queryParser3.parse(keyword);
            
            BooleanQuery booleanClauses = new BooleanQuery();
            booleanClauses.add(new BooleanClause(termQuery1, BooleanClause.Occur.SHOULD));
            booleanClauses.add(new BooleanClause(termQuery2, BooleanClause.Occur.SHOULD));
            booleanClauses.add(new BooleanClause(termQuery3, BooleanClause.Occur.SHOULD));
            
            booleanClauses.setMinimumNumberShouldMatch(1);
            return booleanClauses;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<SearcherBean> search(String queryString, int pageNum, int pageSize) {
        IndexReader aIndexReader = null;
        try {
            aIndexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = null;
            searcher = new IndexSearcher(aIndexReader);
            Query query = getQuery(queryString);
            // Doc  searcher.search(booleanClauses, 50);
            ScoreDoc lastScoreDoc = getLastScoreDoc(pageNum, pageSize, query, searcher);
            TopDocs topDocs = searcher.searchAfter(lastScoreDoc, query, pageSize);
            List<SearcherBean> searcherBeans = getSearcherBeans(searcher, topDocs);
            int totalRow = searchTotalRecord(searcher, query);
            int totalPages;
            if ((totalRow % pageSize) == 0) {
                totalPages = totalRow / pageSize;
            } else {
                totalPages = totalRow / pageSize + 1;
            }
            Page<SearcherBean> searcherBeanPage = new Page<>(searcherBeans, pageNum, pageSize, totalPages, totalRow);
            return searcherBeanPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据页码和分页大小获取上一次最后一个ScoreDoc
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = indexSearcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }

    /**
     * @param query
     * @return
     * @throws IOException
     * @Title: searchTotalRecord
     * @Description: 获取符合条件的总记录数
     */
    public static int searchTotalRecord(IndexSearcher searcher, Query query) throws IOException {
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
        if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {
            return 0;
        }
        ScoreDoc[] docs = topDocs.scoreDocs;
        return docs.length;
    }

    /**
     * 重建索引
     */
    public static void reloadIndex() {
    	List<Record> records = BlogService.me.findList4Search();
    	for (Record record : records) {
            SearcherBean searcherBean = new SearcherBean();
            searcherBean.setId(String.valueOf(record.getLong("id")));
            searcherBean.setTitle(record.getStr("title"));
            searcherBean.setSummary(record.getStr("summary"));
            searcherBean.setContent(record.getStr("content"));
            
            searcherBean.setViews(record.getInt("views"));
            searcherBean.setAuthorName(record.getStr("authorName"));
            searcherBean.setCreateAt(record.getDate("createAt"));
            new LuceneSearcher().updateBean(searcherBean);
        }
    }

}
