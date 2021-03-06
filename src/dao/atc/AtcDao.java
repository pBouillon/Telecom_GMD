package dao.atc;

import common.Configuration;
import common.pojo.Drug;
import dao.TextSourceDaoBase;
import lucene.indexer.ILuceneIndexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.nio.file.Paths;

/**
 * DAO for the Atc data source
 */
public class AtcDao extends TextSourceDaoBase<Drug> implements ILuceneIndexer<Drug> {

    /**
     * Default constructor, initialize the data source from the constants
     * @see Configuration
     */
    public AtcDao() {
        super(Paths.get(Configuration.Atc.Paths.INDEX));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Document getAsDocument(Drug sourceObject) {
        Document document = new Document();

        // Drug's ATC
        document.add(new StringField(
                Configuration.Lucene.IndexKey.Drug.ATC,
                sourceObject.getATC(),
                Field.Store.YES
        ));

        // Drug's name
        document.add(new TextField(
                Configuration.Lucene.IndexKey.Drug.NAME,
                sourceObject.getName(),
                Field.Store.YES
        ));

        return document;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void initialize() {
        dataSource = Paths.get(Configuration.Atc.Paths.SOURCE);
        parser = new AtcParser();
    }
}
