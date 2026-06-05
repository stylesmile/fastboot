package io.github.stylesmile.milvus;

import io.milvus.grpc.MutationResult;
import io.milvus.grpc.QueryResults;
import io.milvus.grpc.SearchResults;
import io.milvus.grpc.FlushResponse;
import io.milvus.param.R;
import io.milvus.param.collection.FlushParam;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.QueryParam;
import io.milvus.param.dml.SearchParam;

/**
 * Milvus 操作接口，便于测试和扩展。
 */
public interface MilvusOperations {

    R<Boolean> hasCollection(HasCollectionParam param);

    R<MutationResult> insert(InsertParam param);

    R<SearchResults> search(SearchParam param);

    R<QueryResults> query(QueryParam param);

    R<FlushResponse> flush(FlushParam param);
}
