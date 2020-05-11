package dao.omim;

import common.Configuration;
import common.pojo.Disease;
import dao.TextSourceDaoBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import util.indexer.IIndexer;
import util.indexer.IndexerBase;

import java.nio.file.Paths;

/**
 * DAO for the OMIM data source
 */
public class OmimDao extends TextSourceDaoBase<Disease> implements IIndexer<Disease> {

    /**
     * Default constructor
     */
    public OmimDao() {
        super(Paths.get(Configuration.Omim.Paths.INDEX));
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void initialize() {
        dataSource = Paths.get(Configuration.Omim.Paths.SOURCE);
        parser = new OmimParser();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Document getAsDocument(Disease sourceObject) {
        Document document = new Document();

        // Disease's name
        document.add(new StringField(
                Configuration.Lucene.IndexKey.Disease.NAME,
                sourceObject.getName(),
                Field.Store.YES
        ));

        // Disease's symptoms
        document.add(new TextField(
                Configuration.Lucene.IndexKey.Disease.SYNONYMS,
                IndexerBase.getJoinedStringCollection(sourceObject.getSynonyms()),
                Field.Store.YES
        ));

        return document;
    }
}
