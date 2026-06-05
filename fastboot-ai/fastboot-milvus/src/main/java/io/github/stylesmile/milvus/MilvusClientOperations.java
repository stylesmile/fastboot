package io.github.stylesmile.milvus;

import io.milvus.client.MilvusClient;
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

/**
 * Milvus SDK 客户端适配器。
 */
public class MilvusClientOperations implements MilvusOperations {

    private final MilvusClient milvusClient;

    public MilvusClientOperations(MilvusClient milvusClient) {
        if (milvusClient == null) {
            throw new IllegalArgumentException("milvusClient must not be null");
        }
        this.milvusClient = milvusClient;
    }

    @Override
    public R<Boolean> hasCollection(HasCollectionParam param) {
        return milvusClient.hasCollection(param);
    }

    @Override
    public R<MutationResult> insert(InsertParam param) {
        return milvusClient.insert(param);
    }

    @Override
    public R<SearchResults> search(SearchParam param) {
        return milvusClient.search(param);
    }

    @Override
    public R<QueryResults> query(QueryParam param) {
        return milvusClient.query(param);
    }

    @Override
    public R<FlushResponse> flush(FlushParam param) {
        return milvusClient.flush(param);
    }
}
