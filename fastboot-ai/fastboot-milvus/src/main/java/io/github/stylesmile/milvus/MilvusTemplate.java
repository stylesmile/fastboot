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

import java.util.Collections;
import java.util.List;

/**
 * Milvus 常用操作模板。
 */
public class MilvusTemplate {

    private final MilvusOperations milvusOperations;

    public MilvusTemplate(MilvusOperations milvusOperations) {
        if (milvusOperations == null) {
            throw new IllegalArgumentException("milvusOperations must not be null");
        }
        this.milvusOperations = milvusOperations;
    }

    public R<Boolean> hasCollection(String collectionName) {
        checkNotBlank(collectionName, "collectionName");
        HasCollectionParam param = HasCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .build();
        return milvusOperations.hasCollection(param);
    }

    public R<MutationResult> insert(String collectionName, List<InsertParam.Field> fields) {
        checkNotBlank(collectionName, "collectionName");
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("fields must not be empty");
        }
        InsertParam param = InsertParam.newBuilder()
                .withCollectionName(collectionName)
                .withFields(fields)
                .build();
        return milvusOperations.insert(param);
    }

    public R<SearchResults> search(String collectionName, String vectorFieldName, List<List<Float>> vectors,
                                   int topK, List<String> outputFields) {
        checkNotBlank(collectionName, "collectionName");
        checkNotBlank(vectorFieldName, "vectorFieldName");
        if (vectors == null || vectors.isEmpty()) {
            throw new IllegalArgumentException("vectors must not be empty");
        }
        if (topK <= 0) {
            throw new IllegalArgumentException("topK must be greater than 0");
        }
        SearchParam param = SearchParam.newBuilder()
                .withCollectionName(collectionName)
                .withVectorFieldName(vectorFieldName)
                .withVectors(vectors)
                .withTopK(topK)
                .withOutFields(outputFields)
                .build();
        return milvusOperations.search(param);
    }

    public R<QueryResults> query(String collectionName, String expr, List<String> outputFields) {
        checkNotBlank(collectionName, "collectionName");
        checkNotBlank(expr, "expr");
        QueryParam param = QueryParam.newBuilder()
                .withCollectionName(collectionName)
                .withExpr(expr)
                .withOutFields(outputFields)
                .build();
        return milvusOperations.query(param);
    }

    public R<FlushResponse> flush(String collectionName) {
        checkNotBlank(collectionName, "collectionName");
        FlushParam param = FlushParam.newBuilder()
                .withCollectionNames(Collections.singletonList(collectionName))
                .build();
        return milvusOperations.flush(param);
    }

    private static void checkNotBlank(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
