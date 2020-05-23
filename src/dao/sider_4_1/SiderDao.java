package dao.sider_4_1;

import common.Configuration;
import common.pojo.Symptom;
import dao.DataAccessObjectBase;
import dao.DatabaseDaoBase;
import lucene.indexer.ILuceneIndexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * DAO for the Sider data source
 */
public class SiderDao extends DatabaseDaoBase<Symptom> implements ILuceneIndexer<Symptom> {

    /**
     * Default constructor
     * @see DataAccessObjectBase
     */
    public SiderDao() {
        super(Paths.get(Configuration.Sider.Paths.INDEX));
    }

    /**
     * Initialize the data extractor from MeDRA databases
     *
     * @inheritDoc
     */
    @Override
    protected void initialize() {
        extractor = new MeDRAExtractor();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Document getAsDocument(Symptom sourceObject) {
        Document symptomDocument = new Document();

        // Add the side effect's name to the document
        symptomDocument.add(new TextField(
                Configuration.Lucene.IndexKey.Symptom.NAME,
                sourceObject.getName(),
                Field.Store.YES));

        return symptomDocument;
    }
}
