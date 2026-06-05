package io.github.stylesmile.milvus;

import io.milvus.grpc.FlushResponse;
import io.milvus.grpc.MutationResult;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for MilvusTemplate.
 */
public class MilvusTemplateTest {

    @Test
    public void testHasCollectionUsesCollectionName() {
        CapturingMilvusOperations operations = new CapturingMilvusOperations();
        MilvusTemplate template = new MilvusTemplate(operations);

        template.hasCollection("documents");

        assertNotNull(operations.hasCollectionParam);
        assertEquals("documents", operations.hasCollectionParam.getCollectionName());
    }

    @Test
    public void testInsertRequiresCollectionName() {
        MilvusTemplate template = new MilvusTemplate(new CapturingMilvusOperations());

        try {
            template.insert(" ", Collections.<InsertParam.Field>emptyList());
            fail("blank collection name should fail");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("collectionName"));
        }
    }

    @Test
    public void testInsertBuildsInsertParam() {
        CapturingMilvusOperations operations = new CapturingMilvusOperations();
        MilvusTemplate template = new MilvusTemplate(operations);
        List<InsertParam.Field> fields = Arrays.asList(
                new InsertParam.Field("id", Arrays.asList(1L, 2L)),
                new InsertParam.Field("vector", Arrays.asList(
                        Arrays.asList(0.1f, 0.2f),
                        Arrays.asList(0.3f, 0.4f)
                ))
        );

        template.insert("documents", fields);

        assertNotNull(operations.insertParam);
        assertEquals("documents", operations.insertParam.getCollectionName());
        assertEquals(fields, operations.insertParam.getFields());
    }

    @Test
    public void testSearchBuildsSearchParam() {
        CapturingMilvusOperations operations = new CapturingMilvusOperations();
        MilvusTemplate template = new MilvusTemplate(operations);
        List<List<Float>> vectors = Collections.singletonList(Arrays.asList(0.1f, 0.2f));
        List<String> outputFields = Collections.singletonList("content");

        template.search("documents", "embedding", vectors, 3, outputFields);

        assertNotNull(operations.searchParam);
        assertEquals("documents", operations.searchParam.getCollectionName());
        assertEquals("embedding", operations.searchParam.getVectorFieldName());
        assertEquals(3, operations.searchParam.getTopK().intValue());
        assertEquals(outputFields, operations.searchParam.getOutFields());
    }

    @Test
    public void testQueryBuildsQueryParam() {
        CapturingMilvusOperations operations = new CapturingMilvusOperations();
        MilvusTemplate template = new MilvusTemplate(operations);
        List<String> outputFields = Arrays.asList("id", "content");

        template.query("documents", "id in [1,2]", outputFields);

        assertNotNull(operations.queryParam);
        assertEquals("documents", operations.queryParam.getCollectionName());
        assertEquals("id in [1,2]", operations.queryParam.getExpr());
        assertEquals(outputFields, operations.queryParam.getOutFields());
    }

    @Test
    public void testFlushBuildsFlushParam() {
        CapturingMilvusOperations operations = new CapturingMilvusOperations();
        MilvusTemplate template = new MilvusTemplate(operations);

        template.flush("documents");

        assertNotNull(operations.flushParam);
        assertEquals(Collections.singletonList("documents"), operations.flushParam.getCollectionNames());
    }

    private static class CapturingMilvusOperations implements MilvusOperations {
        private HasCollectionParam hasCollectionParam;
        private InsertParam insertParam;
        private SearchParam searchParam;
        private QueryParam queryParam;
        private FlushParam flushParam;

        @Override
        public R<Boolean> hasCollection(HasCollectionParam param) {
            this.hasCollectionParam = param;
            return R.success(Boolean.TRUE);
        }

        @Override
        public R<MutationResult> insert(InsertParam param) {
            this.insertParam = param;
            return R.success(MutationResult.getDefaultInstance());
        }

        @Override
        public R<SearchResults> search(SearchParam param) {
            this.searchParam = param;
            return R.success(SearchResults.getDefaultInstance());
        }

        @Override
        public R<QueryResults> query(QueryParam param) {
            this.queryParam = param;
            return R.success(QueryResults.getDefaultInstance());
        }

        @Override
        public R<FlushResponse> flush(FlushParam param) {
            this.flushParam = param;
            return R.success(FlushResponse.getDefaultInstance());
        }
    }
}
