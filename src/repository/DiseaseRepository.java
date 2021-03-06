package repository;

import common.Configuration;
import common.pojo.Disease;
import common.pojo.Symptom;
import dao.hpo.HpoDao;
import dao.omim.csv.OmimCsvDao;
import dao.omim.txt.OmimTxtDao;
import lucene.searcher.LuceneSearcherBase;
import lucene.searcher.SearchParam;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Disease repository providing entry points for Disease fetching and creation
 */
public class DiseaseRepository extends RepositoryBase<Disease> {

    /**
     * Default constructor
     */
    public DiseaseRepository() throws IOException {
        super(new OmimTxtDao().createIndexReader(),
                new OmimCsvDao().createIndexReader(),
                new HpoDao().createIndexReader());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Disease createFromDocument(Document document) {
        return new Disease(
                document.get(Configuration.Lucene.IndexKey.Disease.NAME),
                document.get(Configuration.Lucene.IndexKey.Disease.HPO_SIGN_ID),
                document.get(Configuration.Lucene.IndexKey.Disease.HPO_ID),
                document.get(Configuration.Lucene.IndexKey.Disease.HPO_DB_NAME),
                document.get(Configuration.Lucene.IndexKey.Disease.CUI_LIST)
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void mergeResult(Map<String, Disease> recordsMap, Disease toMerge) {
        final String diseaseName = toMerge.getName();

        // Get current record or create it
        recordsMap.putIfAbsent(diseaseName, new Disease(diseaseName));
        Disease currentDisease = recordsMap.get(diseaseName);

        // Merge data
        if (currentDisease.getHpoDbName() == null
                && toMerge.getHpoDbName() != null) {
            currentDisease.setHpoDbName(toMerge.getHpoDbName());
        }

        if (currentDisease.getHpoId() == null
                && toMerge.getHpoId() != null) {
            currentDisease.setHpoId(toMerge.getHpoId());
        }

        if (currentDisease.getHpoSignId() == null
                && toMerge.getHpoSignId() != null) {
            currentDisease.setHpoSignId(toMerge.getHpoSignId());
        }

        if (currentDisease.getCuiList() == null
                && toMerge.getCuiList() != null) {
            currentDisease.setCuiList(toMerge.getCuiList());
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<SearchParam> generateSearchParamsFromSymptom(Symptom symptom) {
        List<SearchParam> searchParams = new Stack<>();

        if (symptom.getHpoId() != null) {
            searchParams.add(
                    new SearchParam(
                            Configuration.Lucene.IndexKey.Disease.HPO_SIGN_ID,
                            symptom.getHpoId()
            ));
        }

        if (symptom.getCui() != null) {
            searchParams.add(
                    new SearchParam(
                            Configuration.Lucene.IndexKey.Disease.CUI_LIST,
                            symptom.getCui()
            ));
        }
        if (symptom.getName() != null && !symptom.getName().equals("")) {
            searchParams.add(
                    new SearchParam(
                            Configuration.Lucene.IndexKey.Disease.SYMPTOMS,
                            LuceneSearcherBase.getFieldForLuceneExactSearchOn(symptom.getName())
                    ));
        }

        return searchParams;
    }

}
