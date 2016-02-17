// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.core.model.mapping;

import static org.mockito.Mockito.*;
import junit.framework.Assert;

import org.junit.Test;

/**
 * created by hcyi on Feb 16, 2016 Detailled comment
 *
 */
public class WidgetFieldTypeMappingReaderTest {

    @Test
    public void testGetFieldType() {
        WidgetFieldTypeMappingReader mappingReader = mock(WidgetFieldTypeMappingReader.class);
        String fieldType = mappingReader.getFieldType("DEFAULT", "STRING");
        Assert.assertEquals("TEXT", fieldType);
    }
}