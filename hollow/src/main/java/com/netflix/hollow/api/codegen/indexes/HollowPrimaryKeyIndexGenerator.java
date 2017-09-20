/*
 *
 *  Copyright 2017 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.hollow.api.codegen.indexes;


import static com.netflix.hollow.api.codegen.HollowCodeGenerationUtils.hollowImplClassname;

import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.core.schema.HollowObjectSchema;

/**
 * This class contains template logic for generating a {@link HollowAPI} implementation.  Not intended for external consumption.
 *
 * @see HollowAPIGenerator
 *
 */
public class HollowPrimaryKeyIndexGenerator extends HollowUniqueKeyIndexGenerator {

    public HollowPrimaryKeyIndexGenerator(String packageName, String apiClassname, String classPostfix, boolean useAggressiveSubstitutions, HollowObjectSchema schema) {
        super(packageName, apiClassname, classPostfix, useAggressiveSubstitutions, schema);
        isGenDefaultConstructor = true;
        isParameterizedConstructorPublic = false;
    }

    @Override
    protected String getClassName(HollowObjectSchema schema) {
        return schema.getName() + "PrimaryKeyIndex";
    }

    @Override
    protected void genFindMatchAPI(StringBuilder builder) {
        builder.append("    // @TODO: Need to use actual Param Types\n");
        builder.append("    public " + hollowImplClassname(schema.getName(), classPostfix, useAggressiveSubstitutions) + " findMatch(Object... keys) {\n");
        builder.append("        int ordinal = idx.getMatchingOrdinal(keys);\n");
        builder.append("        if(ordinal == -1)\n");
        builder.append("            return null;\n");
        builder.append("        return api.get" + hollowImplClassname(schema.getName(), classPostfix, useAggressiveSubstitutions) + "(ordinal);\n");
        builder.append("    }\n\n");
    }
}