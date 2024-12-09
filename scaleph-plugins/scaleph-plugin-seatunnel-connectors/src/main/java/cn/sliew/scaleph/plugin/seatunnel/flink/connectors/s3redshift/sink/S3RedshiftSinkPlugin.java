/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.s3redshift.sink;

import cn.sliew.carp.module.datasource.modal.DataSourceInfo;
import cn.sliew.carp.module.datasource.modal.file.S3DataSourceProperties;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.resource.ResourceProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileProperties.FIELD_DELIMITER;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileProperties.PATH;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.FileSinkProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.s3.S3Properties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.s3redshift.S3RedshiftProperties.BUCKET;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.s3redshift.S3RedshiftProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class S3RedshiftSinkPlugin extends SeaTunnelConnectorPlugin {

    public S3RedshiftSinkPlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "Output data to AWS Redshift.",
                S3RedshiftSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(JDBC_URL);
        props.add(JDBC_USER);
        props.add(JDBC_PASSWORD);
        props.add(EXECUTE_SQL);

        props.add(HADOOP_S3_PROPERTIES);
        props.add(PATH);
        props.add(FILE_FORMAT_TYPE);
        props.add(FILE_NAME_EXPRESSION);
        props.add(FILENAME_TIME_FORMAT);
        props.add(FIELD_DELIMITER);
        props.add(ROW_DELIMITER);
        props.add(PARTITION_BY);
        props.add(PARTITION_DIR_EXPRESSION);
        props.add(IS_PARTITION_FIELD_WRITE_IN_FILE);
        props.add(SINK_COLUMNS);
        props.add(IS_ENABLE_TRANSACTION);
        props.add(BATCH_SIZE);

        props.add(CommonProperties.PARALLELISM);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public List<ResourceProperty> getRequiredResources() {
        return Collections.singletonList(ResourceProperties.DATASOURCE_RESOURCE);
    }

    @Override
    public ObjectNode createConf() {
        ObjectNode conf = super.createConf();
        JsonNode jsonNode = properties.get(ResourceProperties.DATASOURCE);
        DataSourceInfo dataSourceInfo = JacksonUtil.toObject(jsonNode, DataSourceInfo.class);
        S3DataSourceProperties props = (S3DataSourceProperties) dataSourceInfo.getProps();

        conf.putPOJO(BUCKET.getName(), props.getBucket());
        conf.putPOJO(ACCESS_KEY.getName(), props.getAccessKey());
        conf.putPOJO(ACCESS_SECRET.getName(), props.getAccessSecret());
        return conf;
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_S3REDSHIFT;
    }
}
